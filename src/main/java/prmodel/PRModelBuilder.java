package prmodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommitStatus;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHIssueEvent;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestCommitDetail;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHPullRequestReview;
import org.kohsuke.github.GHPullRequestReviewComment;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.PagedIterable;

import com.vladsch.flexmark.ast.BlockQuote;
import com.vladsch.flexmark.ast.Code;
import com.vladsch.flexmark.ast.Emphasis;
import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.HtmlCommentBlock;
import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.ast.StrongEmphasis;
import com.vladsch.flexmark.ast.Text;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.sequence.BasedSequence;

import customExceptions.commitMissingException;
import jprTool.JsonFileWriter;
import jxp3model.ChangedFileUnit;
import jxp3model.ChangedProjectUnit;
import jxp3model.CodeElement;
import jxp3model.Jxp3ModelBuilder;


public class PRModelBuilder {
	
    String GHToken ="";
    String repositoryName="";
    int prNum;
    GitHub github;
    GHRepository repository;
	GHPullRequest ghpr;
	
	PullRequest pr;
	
	String rootSrcPath="";
	File rootDir;
	File datasetDir;
	File repositoryDir;
	File prDir;
	Jxp3ModelBuilder j3mdb ;
	
	HashSet<GHLabel> repoLabels;
	ArrayList<GHPullRequestFileDetail> ghprFinalFiles;
	ArrayList<GHFilesChangedUnit> ghFilesChangedUnits;
	int downloadChangedFiles_min=0;
	int downloadChangedFiles_max=-9999;
	int commitMin=0;
	int commitMax=-9999;
	ArrayList<String> downloadBannedLabels;
	boolean download_ChangedFiles=true;
	boolean download_CommitNum=true;
	boolean download_BannedLabels=true;
	boolean writeFile =false;
	boolean deleteSrcFile = false;
	public boolean exceptionDone = false;
	boolean writeErrorLogs = true;
	
	public PRModelBuilder(String token, String repositoryName, int prNum)
	{
		this.GHToken = token;
		this.repositoryName = repositoryName;
		this.prNum = prNum;
		this.repoLabels = new HashSet<>();
		this.downloadBannedLabels= new ArrayList<>();
		this.ghprFinalFiles = new ArrayList<>();
		this.ghFilesChangedUnits = new ArrayList<>();
	}
	
	public void build() throws IOException
	{			
		this.buildGHPR();
		System.out.println("GHPR builded");
		if (!this.checkRepeatedDirectory()) {
			this.buildModelSourceDir();
			System.out.println("ModelSourceDir builded");
			if(!this.exceptionDone)
			{
			this.checkChangedFilesFlag();
			this.checkCommitNumFlag();
			}
			if(this.download_ChangedFiles)
			{
			if(this.download_CommitNum)
			{
			if(!this.exceptionDone) 
			{
			this.buildPR();
			System.out.println("PR builded");
			}
			if(!this.exceptionDone)
			{
			this.buildScrapingElement();
			System.out.println("ScrapingElement builded");
			}
			if(!this.exceptionDone)
			{
			this.buildParticipants();
			System.out.println("Participants builded");
			}
			//this.pr.printParticipants();//
			if(!this.exceptionDone)
			{
			this.buildConversation();
			System.out.println("Conversation builded");
			}
			
			if(!this.exceptionDone)
			{
			this.buildMarkdownElements();
			System.out.println("MarkdownElements builded");
			}
			if(!this.exceptionDone)
			{
			this.buildRepoLabels();
			System.out.println("Repo Labels builded");
			}
			if(!this.exceptionDone)
			{
			this.buildLabels();
			System.out.println("Labels builded");
			}
			if(!this.exceptionDone)
			{
			this.buildCommits();
			System.out.println("Commits builded");
			}
			
			if (this.pr.isSrcRetrievable()) {
					if (this.download_BannedLabels) {
						if(!this.exceptionDone)
						{
						  this.buildGHPRFinalFilesChanged();
						}
						if(!this.exceptionDone)
						{
						  this.buildGHFilesChangedUnits();
						}
						if(!this.exceptionDone)
						{
						this.buildFilesChanged();
						System.out.println("FilesChanged builded");
						}
						if(!this.exceptionDone)
						{
						this.buildJxp3Elements();
						System.out.println("Jxp3Element builded");
						}
						if(!this.exceptionDone)
						{
						this.buildDiffFileUnitIsTest();
						}
						if(!this.exceptionDone)
						{
							this.buildPrFilesChanged();
							System.out.println("PrFilesChanged builded");
						}
						this.removeRepetitiveDiffFileUnitInPRFilesChanged();
					}
				
			}
			if (this.writeFile) {
				if (!this.exceptionDone) {
					JsonFileWriter jfw = new JsonFileWriter(this.pr, this.getPrDir() + File.separator
							+ this.pr.getRepositoryName() + "_" + this.pr.getId() + "_str.json");
					jfw.makeFile();
					if (this.deleteSrcFile) {
						jfw.deleteGitSrc();
					}
				}else {
					System.out.println("delete src after error logs");
					JsonFileWriter jfw = new JsonFileWriter(this.pr);
					jfw.deleteGitSrc();
				}
			}
			}else {
				System.out.println("Not build for out num of commits");
			}
			}else {
				System.out.println("Not build for out num of files changed");
			}
			
		}else {
			System.out.println("Repeated file exits : " + this.repository.getName()+"  ---  "+this.ghpr.getNumber());
		}
	}
	
	//build pr, ghpr start-----------------------------------------------------------------------------------------
	
	void buildGHPR()
	{
		
		try {
		      this.github = new GitHubBuilder().withOAuthToken(this.GHToken).build();
			  this.repository = github.getRepository(this.repositoryName);
			  this.ghpr = this.repository.getPullRequest(this.prNum);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	void buildPR()
	{
		this.pr = new PullRequest();
		this.pr.title = ghpr.getTitle();
		this.pr.id = ghpr.getNumber();
		this.pr.state = ghpr.getState().name();
		this.pr.repositoryName = ghpr.getRepository().getName();
		try {
			this.pr.createDate = ghpr.getCreatedAt();
			this.pr.endDate = ghpr.getClosedAt();
			this.pr.mergeBranch = ghpr.getBase().getRef();
			this.pr.headBranch = ghpr.getHead().getRef();
			this.pr.prPageUrl = ghpr.getHtmlUrl().toString();
			this.pr.repositorySrcDLUrl = ghpr.getRepository().getHttpTransportUrl();
			Map<String, GHBranch> repoBranches = ghpr.getRepository().getBranches();
			for(String bri : repoBranches.keySet())
			{
				this.pr.repositoryBranches.add(bri);
			}
	        if(this.ghpr.getHead().getRepository()!=null)
	        {
			    this.pr.headRepositorySrcDLUrl = ghpr.getHead().getRepository().getHttpTransportUrl();
			    Map<String, GHBranch> headRepoBranches = ghpr.getHead().getRepository().getBranches();
			    for(String bri : headRepoBranches.keySet())
			    {
				    this.pr.headRepositoryBranches.add(bri);
			    }
	        }
			if(this.pr.state.equals("CLOSED"))
			{
				if(this.ghpr.isMerged())
				{
					this.pr.isMerged=true;
					this.pr.isStandardMerged=true;
				}else if(this.ghpr.isMerged()==false)
				{
					GHIssueEvent closedEvent=null;
					for(GHIssueEvent evi : this.ghpr.listEvents().toList())
					{
						if(evi.getEvent().equals("closed"))
						{
							closedEvent = evi;
						}
					}
					
					if(closedEvent!=null)
					{
						if(closedEvent.getCommitId()!=null)
						{
							this.pr.isMerged = true;
							this.pr.isStandardMerged = false;
						}else {
							this.pr.isMerged = false;
							this.pr.isStandardMerged = false;
						}
					}
				}
			}
			
			if(this.pr.headRepositoryBranches.contains(this.pr.headBranch))
			{
				this.pr.srcRetrievable = true;
			}else {
				this.pr.srcRetrievable = false;
			}
			
			this.pr.dcpt = new Description(this.pr);
			this.pr.dcpt.body = this.ghpr.getBody();
			this.pr.dcpt.instId = "description0";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.recordException(e);
		}
		
	}
	
	//build pr, ghpr end-----------------------------------------------------------------------------------------
	
	//build scrapingElement  start-----------------------------------------------------------------------------------------
	void buildScrapingElement()
	{
		int scrapingElementCounter= -1;
		ScrapingElement scrpe = new ScrapingElement(this.pr);
		try {
			org.jsoup.nodes.Document hdoc = Jsoup.connect(this.pr.prPageUrl).get();
			Elements comments = hdoc.getElementsByClass("comment-body");
			Element descr = comments.get(0);
			scrpe.descriptionScrp = descr.text();
			scrapingElementCounter ++;
			scrpe.instId = "scrapingElement" + scrapingElementCounter;
			Elements mentions = hdoc.getElementsByClass("user-mention notranslate");
			if(mentions.isEmpty()==false)
			{
				for(Element menti : mentions)
				{
					scrpe.mentionUsers.add(menti.text());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.recordException(e);
		}
		this.pr.scrpe = scrpe;
		this.pr.htmlDescription = scrpe.descriptionScrp;
	}
	
	//build scrapingElement  end-----------------------------------------------------------------------------------------
	
	//build participants start-----------------------------------------------------------------------------------------
	void buildParticipants()
	{
		int participantCounter = -1;
		GHUser author;
		try {
			author = this.ghpr.getUser();
			Participant apr = new Participant(this.pr);
			buildParticipantUnit(author,apr);
			apr.role = "Author";
			apr.record.add("Creation");
			participantCounter ++ ;
			apr.instId = "participant"+participantCounter;
			this.pr.participants.add(apr);
			for(GHIssueComment isCommenti : this.ghpr.getComments())
			{
				GHUser reviewer = isCommenti.getUser();		
				Participant inSetPr = this.getInParticipants(reviewer.getId());
				if(inSetPr!=null)
				{
				  inSetPr.record.add("Comment");
				}else {
					Participant rpr = new Participant(this.pr);
					this.buildParticipantUnit(reviewer, rpr);
					rpr.role = "Reviewer";
					rpr.record.add("Comment");
					participantCounter ++;
					rpr.instId = "participant"+participantCounter;
					this.pr.participants.add(rpr);
				}
			}
			for(GHPullRequestReviewComment prrvci : this.ghpr.listReviewComments())
			{
				GHUser reviewer = prrvci.getUser();
				Participant inSetPr = this.getInParticipants(reviewer.getId());
				
				if(inSetPr!=null)
				{
				  inSetPr.record.add("ReviewComment");
				}else {
					Participant rpr = new Participant(this.pr);
					this.buildParticipantUnit(reviewer, rpr);
					rpr.role = "Reviewer";
					rpr.record.add("ReviewComment");
					participantCounter++;
					rpr.instId = "participant"+participantCounter;
					this.pr.participants.add(rpr);
				}
			}
			
			
			for(GHIssueEvent iei : this.ghpr.listEvents())
			{
				GHUser eventReviewer = iei.getActor();
				Participant inSetPr = this.getInParticipants(eventReviewer.getId());
				if(inSetPr!=null)
				{
				   inSetPr.record.add("Event");
				}else {
					Participant etr = new Participant(this.pr);
					this.buildParticipantUnit(eventReviewer, etr);
					etr.role = "Reviewer";
					etr.record.add("Event");
					participantCounter++;
					etr.instId = "participant"+participantCounter;
					this.pr.participants.add(etr);
				}
			}
			for(GHPullRequestReview prrvi : this.ghpr.listReviews())
			{
				GHUser reviewer = prrvi.getUser();
				Participant inSetPr = this.getInParticipants(reviewer.getId());
				
				if(inSetPr!=null)
				{
				   inSetPr.record.add("ReviewEvent");
				}else {
				   Participant rpr = new Participant(this.pr);
				   this.buildParticipantUnit(reviewer, rpr);
				   rpr.role = "Reviewer";
				   rpr.record.add("ReviewEvent");
				   participantCounter++;
				   rpr.instId = "participant"+participantCounter;
				   this.pr.participants.add(rpr);
				}
			}
			PagedIterable<GHPullRequestCommitDetail> ghcmitds = ghpr.listCommits();
			for(GHPullRequestCommitDetail ghcmitdi : ghcmitds)
			{
				GHCommit ghcmit = this.ghpr.getRepository().getCommit(ghcmitdi.getSha());
				GHUser commitAuthor = ghcmit.getAuthor();
				Participant inSetPr = this.getInParticipants(commitAuthor.getId());
				if(inSetPr!=null)
				{
					inSetPr.record.add("Commit");
				}else {
					Participant cpr = new Participant(this.pr);
					this.buildParticipantUnit(commitAuthor, cpr);
					cpr.role = "Reviewer";
					cpr.record.add("Commit");
					participantCounter++;
					cpr.instId ="participant"+participantCounter;
					this.pr.participants.add(cpr);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.recordException(e);
		}
		
		
	}
	
	boolean inParticipants(Participant prtAim)
	{
		for(Participant prti : this.pr.participants)
		{
			if(prti.loginName.equals(prtAim.loginName))
			{
				return true;
			}
		}
		return false;
	}
	
	boolean inIssueComments(Participant pri)
	{
		try {
			for(GHIssueComment icmi : this.ghpr.getComments())
			{
				if(pri.loginName.equals(icmi.getUser().getLogin()))
				{
					return true;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.recordException(e);
		}
		return false;
	}
	
	boolean inReviewComments(Participant pri)
	{
		try {
		    for(GHPullRequestReviewComment prrci : this.ghpr.listReviewComments())
		    {
			    if(pri.loginName.equals(prrci.getUser().getLogin()))
			    {
			    	return true;
			    }
		    }
		}catch(Exception e) {
			// TODO Auto-generated catch block
		    //e.printStackTrace();
			this.recordException(e);
		}
		return false;
	}
	
	boolean inIssueEvent(Participant pri)
	{
		try {
			for(GHIssueEvent iei : this.ghpr.listEvents())
			{
				if(pri.loginName.equals(iei.getActor().getLogin()))
				{
					return true;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.recordException(e);
		}
		return false;
	}
	
	boolean inReviewEvent(Participant pri)
	{
		try {
			for(GHPullRequestReview prri : this.ghpr.listReviews())
			{
				if(pri.loginName.equals(prri.getUser().getLogin()))
				{
					return true;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.recordException(e);
		}
		return false;
	}
	
	Participant getInParticipants(long aimId)
	{
		for(Participant pri : this.pr.participants)
		{
			if(pri.id==aimId)
			{
				return pri;
			}
		}
		
		return null;
	}
	
	ReviewComment getInReviewComments(String body)
	{
		for(ReviewComment rci : this.pr.conv.reviewComments)
		{
			if(rci.body.equals(body))
			{
				return rci;
			}
		}
		return null;
	}
	
	
	void buildParticipantUnit(GHUser urAim , Participant prAim) throws IOException
	{
		prAim.user = urAim;
		prAim.loginName = urAim.getLogin();
		prAim.name = urAim.getName();
		prAim.id = urAim.getId();
		prAim.location = urAim.getLocation();
		for(GHUser uri : prAim.user.getFollowers())
		{
			prAim.followers.add(uri.getLogin());
		}
		for(GHUser uri : prAim.user.getFollows())
		{
		    prAim.followings.add(uri.getLogin());
		}
		
	}
	//build participants end-----------------------------------------------------------------------------------------
	
	//build conversation start-----------------------------------------------------------------------------------------
	void buildConversation()
	{
		int conversationCounter = -1;
		Conversation conv = new Conversation(this.pr);
		conversationCounter++;
		conv.instId = "conversation"+conversationCounter;
		this.pr.conv = conv;
		this.buildComments();
		this.buildReviewComment();
		this.buildEvent();
		this.buildReviewEvent();
		this.buildCodeLineReview();
		this.buildReviewCommentWithCdlr();
		this.buildTimeLine();
	}
	
	void buildComments()
	{
		int commentCounter = -1;
		try {
			for(GHIssueComment icmti : this.ghpr.listComments())
			{
				Comment cmt = new Comment(this.pr.conv);
				cmt.ghComment = icmti;
				cmt.commentDate = icmti.getCreatedAt();
				cmt.body = icmti.getBody();
				for(Participant prti : this.pr.participants)
				{
					if(prti.loginName.equals(icmti.getUser().getLogin()))
					{
						cmt.commenter = prti;
					}
				}
				commentCounter++;
				cmt.instId = "comment"+commentCounter;
				this.pr.conv.comments.add(cmt);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.recordException(e);
		}
	}
	
	void buildReviewComment()
	{
		int reviewCommentCounter= -1;
		try {
			for(GHPullRequestReviewComment prrci : this.ghpr.listReviewComments())
			{
				ReviewComment rcmt = new ReviewComment(this.pr.conv);
				rcmt.ghReviewComment = prrci;
				rcmt.commentDate = prrci.getCreatedAt();
				rcmt.body = prrci.getBody();
				for(Participant prti : this.pr.participants)
				{
					if(prti.loginName.equals(prrci.getUser().getLogin()))
					{
						rcmt.commenter = prti;
					}
				}
				reviewCommentCounter++;
				rcmt.instId = "reviewComment"+reviewCommentCounter;
				this.pr.conv.reviewComments.add(rcmt);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.recordException(e);
		}
	}
	
	void buildEvent()
	{
		int eventCounter = -1;
		try {
			for(GHIssueEvent isei : this.ghpr.listEvents())
			{
				Event et = new Event(this.pr.conv);
				et.ghEvent = isei;
				et.eventDate = isei.getCreatedAt();
				et.body = isei.getEvent();
				for(Participant prti : this.pr.participants)
				{
					if(prti.loginName.equals(isei.getActor().getLogin()))
					{
						et.actor = prti;
					}
				}
				eventCounter ++;
				et.instId = "event"+ eventCounter;
				this.pr.conv.events.add(et);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.recordException(e);
		}
	}
	
	void buildReviewEvent()
	{
		int reviewEventCounter =-1;
		try {
		for(GHPullRequestReview prri : this.ghpr.listReviews())
		{
			ReviewEvent ret = new ReviewEvent(this.pr.conv);
			ret.ghReview = prri;
			ret.eventDate = prri.getCreatedAt();
			ret.body = prri.getBody();
			for(Participant prti : this.pr.participants)
			{
				if(prti.loginName.equals(prri.getUser().getLogin()))
				{
					ret.actor = prti;
				}
			}
			reviewEventCounter++;
			ret.instId = "reviewEvent" + reviewEventCounter;
			this.pr.conv.reviewEvents.add(ret);
		}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.recordException(e);
		}
	}
	
	void buildCodeLineReview()
	{
		int codeLineReviewCounter = -1;
		try {
			for(GHPullRequestReviewComment prci : this.ghpr.listReviewComments())
			{
				CodeReviewSnippet clri = new CodeReviewSnippet(this.pr.conv);
				ArrayList<GHPullRequestReviewComment> prrcList = new ArrayList<>();
				if(this.inGHPullRequestReviewCommentList(prci, prrcList)==false)
				{
					prrcList.add(prci);
				}
				
				for(GHPullRequestReviewComment prcj : this.ghpr.listReviewComments())
				{
					if(prci.getDiffHunk().equals(prcj.getDiffHunk()))
					{
						if(this.inGHPullRequestReviewCommentList(prcj, prrcList)==false)
						{
							prrcList.add(prcj);
						}
					}
				}

				Collections.sort(prrcList, new Comparator<GHPullRequestReviewComment>() {
					@Override
					public int compare(GHPullRequestReviewComment prrc1 , GHPullRequestReviewComment prrc2)
					{
						try {
							return prrc1.getCreatedAt().compareTo(prrc2.getCreatedAt());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						return -33;
					}
					
				});
				clri.diff = prci.getDiffHunk();
				
				if(this.inCodeLineReview(clri)==false)
				{
				  codeLineReviewCounter++;
				  clri.instId = "codeLineReview"+ codeLineReviewCounter;
				  this.pr.conv.codeLineReviews.add(clri);
				}
				
				for(GHPullRequestReviewComment prrci : prrcList)
				{
					ReviewComment rci = this.getInReviewComments(prrci.getBody());
					if(rci!=null)
					{
						clri.reviewComments.add(rci);
						clri.participants.add(rci.commenter);
						rci.clr = clri;
					}										
				}
								
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.recordException(e);
		}
	}
	
	boolean inGHPullRequestReviewCommentList(GHPullRequestReviewComment prrcAim, ArrayList<GHPullRequestReviewComment> aimList)
	{
		if(!aimList.isEmpty())
		{
			for(GHPullRequestReviewComment prrci : aimList)
			{
				if(prrci.getBody().equals(prrcAim.getBody()))
				{
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	boolean inCodeLineReview(CodeReviewSnippet clrAim)
	{
		if(!this.pr.conv.codeLineReviews.isEmpty())
		{
			for(CodeReviewSnippet clri : this.pr.conv.codeLineReviews)
			{
				if(clri.diff.equals(clrAim.diff))
				{
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	void buildReviewCommentWithCdlr()
	{
		if(!this.pr.getConv().getReviewComments().isEmpty())
		{
			for(ReviewComment rci : this.pr.getConv().getReviewComments())
			{
				if(!this.pr.getConv().getCodeLineReviews().isEmpty())
				{
					for(CodeReviewSnippet clri : this.pr.getConv().getCodeLineReviews())
					{
						if(!clri.getReviewComments().isEmpty())
						{
							for(ReviewComment rcj : clri.getReviewComments())
							{
								if(rci.getBody().equals(rcj.getBody()))
								{
									rci.clr = clri;
								}
							}
								
						}
					}
				}
			}
		}
	}
	
	void buildTimeLine()
	{
		int prActionCounter = -1;
		ArrayList<PRAction> actions = new ArrayList<>();
		for(Comment cmti : this.pr.conv.comments)
		{
			PRAction pra = new PRAction(this.pr.conv);
			pra.body = cmti.body;
			pra.executor = cmti.commenter;
			pra.actionDate = cmti.commentDate;
			pra.type="Comment";
			prActionCounter++;
			pra.instId = "prAction"+prActionCounter;
			actions.add(pra);
		}
		
		for(ReviewComment rcmti : this.pr.conv.reviewComments)
		{
			PRAction pra = new PRAction(this.pr.conv);
			pra.body = rcmti.body;
			pra.executor = rcmti.commenter;
			pra.actionDate = rcmti.commentDate;
			pra.type="ReviewComment";
			prActionCounter++;
			pra.instId = "prAction"+prActionCounter;
			actions.add(pra);
		}
		
		for(Event eti : this.pr.conv.events)
		{
			PRAction pra = new PRAction(this.pr.conv);
			pra.body = eti.body;
			pra.executor = eti.actor;
			pra.actionDate = eti.eventDate;
			pra.type="Event";
			prActionCounter++;
			pra.instId = "prAction"+prActionCounter;
			actions.add(pra);
		}
		
		for(ReviewEvent reti : this.pr.conv.reviewEvents)
		{
			PRAction pra = new PRAction(this.pr.conv);
			pra.body = reti.body;
			pra.executor = reti.actor;
			pra.actionDate = reti.eventDate;
			pra.type="ReviewEvent";
			prActionCounter++;
			pra.instId = "prAction"+prActionCounter;
			actions.add(pra);
		}
		
		Collections.sort(actions, new Comparator<PRAction>() {
			@Override
			public int compare(PRAction prac1 , PRAction prac2)
			{
				return prac1.actionDate.compareTo(prac2.actionDate);
				
			}
		});
		this.pr.conv.timeLine.addAll(actions);
	}
	
	//build conversation end-----------------------------------------------------------------------------------------
	
	//build commits start-----------------------------------------------------------------------------------------
	void buildCommits()
	{
		int commitCounter = -1;
		PagedIterable<GHPullRequestCommitDetail> ghcmitds = ghpr.listCommits();
		for(GHPullRequestCommitDetail ghcmitdi : ghcmitds)
		{
			Commit cmit = new Commit(this.pr);
			cmit.sha = ghcmitdi.getSha();
			cmit.shortSha = ghcmitdi.getSha().substring(0, 6);
			try {
				GHCommit ghcmit = this.ghpr.getRepository().getCommit(ghcmitdi.getSha());
				System.out.println("Commit author : "+" name : "+ ghcmit.getAuthor().getLogin()+ " id : "+ghcmit.getAuthor().getId());
				Participant pri = this.getInParticipants(ghcmit.getAuthor().getId());
				if(pri!=null)
				{
					cmit.author = pri;
				}
				cmit.commitDate = ghcmit.getCommitDate();
				cmit.message = ghcmit.getCommitShortInfo().getMessage();
				List<GHCommit> parents = ghcmit.getParents();
				int parentCount = parents.size();
				if(parentCount == 0)
				{
					cmit.commitType = "initial";
				}else if(parentCount >1)
				{
					cmit.commitType = "merge";
				}else if(cmit.message.startsWith("Revert"))
				{
					cmit.commitType ="revert";
				}else if(cmit.message.startsWith("fixup!"))
				{
					cmit.commitType ="fixup";
				}else if(cmit.message.startsWith("squash!"))
				{
					cmit.commitType ="squash";
				}else if(cmit.message.trim().isEmpty())
				{
					cmit.commitType ="empty";
				}else {
					cmit.commitType ="regular";
				}
				commitCounter++;
				cmit.instId = "commit"+commitCounter;
				ArrayList<GHCommitStatus> cmitsts = new ArrayList<>();
				cmitsts.addAll(ghcmit.listStatuses().toList());
				ArrayList<CIStatus> cis = new ArrayList<>();
				if(!cmitsts.isEmpty())
				{
					for(GHCommitStatus ghcsi : cmitsts)
					{
						CIStatus cisi = new CIStatus(cmit);
						cisi.setContext(ghcsi.getContext());
						cisi.setState(ghcsi.getState().toString());
						cisi.setDescription(ghcsi.getDescription());
						cisi.setTargetUrl(ghcsi.getTargetUrl());
						cisi.setCreateDate(ghcsi.getCreatedAt());
						cisi.setUpdateDate(ghcsi.getUpdatedAt());
						cis.add(cisi);					
					}
					Collections.sort(cis, new Comparator<CIStatus>(){
						@Override
						public int compare(CIStatus cisi1, CIStatus cisi2)
						{
							return cisi1.updateDate.compareTo(cisi2.updateDate);
						}
					});
					cmit.setCis(cis);
				}
				
				this.pr.commits.add(cmit);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				this.recordException(e);
			} 
		}
		Collections.sort(this.pr.commits, new Comparator<Commit>() {
			@Override
			public int compare(Commit cmt1, Commit cmt2)
			{
				return cmt1.commitDate.compareTo(cmt2.commitDate);
			}
		});
	}
	
	
	
	
	//build commits end-----------------------------------------------------------------------------------------
	
	//build MarkdownElements start-----------------------------------------------------------------------------------------
	void buildMarkdownElements()
	{
		MarkdownElement dmkd = new MarkdownElement();    //markdownElements in description
		MutableDataSet doptions = new MutableDataSet();
		Parser dparser = Parser.builder(doptions).build();
		Document ddocument = dparser.parse(this.pr.dcpt.body);
		this.traverse(ddocument, dmkd);
		this.buildMentionUsersInEachMkd(dmkd);
		this.buildIssueLinksAndPullLinks(this.pr.dcpt.body, dmkd);
		this.pr.dcpt.mde=dmkd;
		
		if(this.pr.conv.comments.isEmpty()==false)
		{
			
			for(Comment cmti : this.pr.conv.comments)
			{
				MarkdownElement mkd = new MarkdownElement();
				MutableDataSet options = new MutableDataSet();
				Parser parser = Parser.builder(options).build();
				Document document = parser.parse(cmti.body);
				this.traverse(document, mkd);
			    this.buildMentionUsersInEachMkd(mkd);
			    this.buildIssueLinksAndPullLinks(cmti.body, mkd);
				cmti.mde=mkd;
			}
		}
		
		if(this.pr.conv.reviewComments.isEmpty()==false)
		{	
			for(ReviewComment rcmti : this.pr.conv.reviewComments)
			{
				MarkdownElement mkd = new MarkdownElement();
				MutableDataSet options = new MutableDataSet();
				Parser parser = Parser.builder(options).build();
				Document document = parser.parse(rcmti.body);
				this.traverse(document, mkd);
				this.buildMentionUsersInEachMkd(mkd);
				this.buildIssueLinksAndPullLinks(rcmti.body, mkd);
				rcmti.mde=mkd;
			}
		}
		
		if(this.pr.conv.reviewEvents.isEmpty()==false)
		{
			for(ReviewEvent reti : this.pr.conv.reviewEvents)
			{
				MarkdownElement mkd = new MarkdownElement();
				MutableDataSet options = new MutableDataSet();
				Parser parser = Parser.builder(options).build();
				Document document = parser.parse(reti.body);
				this.traverse(document, mkd);
				this.buildMentionUsersInEachMkd(mkd);
				this.buildIssueLinksAndPullLinks(reti.body, mkd);
				reti.mde=mkd;
			}
		}
		
	}
	
	void traverse(Node node, MarkdownElement mkd)
	{
		for(Node child = node.getFirstChild();child!=null;child = child.getNext())
		{
			if(child instanceof Heading)
			{
				Node headingContent = child.getFirstChild();
				if(headingContent != null)
				{
					mkd.headingStrings.add(headingContent.getChars().toString());
				}
			}else if(child instanceof Emphasis)
			{
				Node emphasisContent = child.getFirstChild();
				if(emphasisContent != null)
				{
					mkd.italicStrings.add(emphasisContent.getChars().toString());
				}
			}else if(child instanceof StrongEmphasis)
			{
				Node strongEmphasisContent = child.getFirstChild();
				if(strongEmphasisContent != null)
				{
					mkd.boldStrings.add(strongEmphasisContent.getChars().toString());
				}
			}else if(child instanceof BlockQuote)
			{
				Node quoteContent = child.getFirstChild();
				if(quoteContent != null)
				{
					mkd.quoteStrings.add(quoteContent.getChars().toString());
				}
			}else if(child instanceof Link)
			{
				Link lk = (Link)child;
				BasedSequence linkText = lk.getText();
				BasedSequence linkUrl = lk.getUrl();
				String addString = linkText+"->"+linkUrl;
				mkd.linkStrings.add(addString);
			}else if(child instanceof Code)
			{
				Node codeContent = child.getFirstChild();
				if(codeContent != null)
				{
					mkd.codeStrings.add(codeContent.getChars().toString());
				}
			}else if(child instanceof FencedCodeBlock)
			{
				Node codeBlockContent = child.getFirstChild();
				if(codeBlockContent != null)
				{
					mkd.codeBlockStrings.add(codeBlockContent.getChars().toString());
				}
			}else if(child instanceof Text)
			{
				String text = child.getChars().unescape();
				mkd.textStrings.add(text);
				
				traverse(child,mkd);
			}else if(child instanceof HtmlCommentBlock)
			{
				HtmlCommentBlock htcmt = (HtmlCommentBlock) child;
				mkd.htmlComments.add(htcmt.getChars().toString());
				
			}else {
			
				traverse(child,mkd);
			}
		}
	}
	
//	void addTextLink(String text, MarkdownElement mkd)// Link classifying
//	{
//		Pattern pattern = Pattern.compile("\\bhttps?:\\/\\/[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)");
//		Matcher matcher = pattern.matcher(text);
//		while (matcher.find()) {
//			String textLink = matcher.group();
//            mkd.textLinkStrings.add(textLink);
//            String[] ss = textLink.split("/");
//            
//        }
//	}
	void buildMentionUsersInEachMkd(MarkdownElement mkd)
	{
		boolean checkUser=true;
		if(this.pr.scrpe.mentionUsers.isEmpty()==false)
		{
			for(String menti : this.pr.scrpe.mentionUsers)
			{
				if(mkd.textStrings.isEmpty()==false)
				{
					for(String txti : mkd.textStrings)
					{
						if(txti.contains(menti))
						{
							String ss[]=menti.split("@");
							int l=ss.length;							
							try {
								GHUser user = this.github.getUser(ss[l-1]);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								checkUser =false;
							}
							if(checkUser)
							{
							   mkd.mentionStrings.add(ss[l-1]);
							}
						}
					}
				}
			}
		}
	}
	
	void buildIssueLinksAndPullLinks(String str ,MarkdownElement mkd)
	{
		if(str!=null)
		{
		Pattern issuePtrn = Pattern.compile("#\\d+");
		Matcher issueMtc = issuePtrn.matcher(str);
		while(issueMtc.find())
		{
			mkd.issueLinkStrings.add(issueMtc.group());
		}
		Pattern prLinkPtrn = Pattern.compile("gh-\\d+");
		Matcher prLinkMtc = prLinkPtrn.matcher(str);
		while(prLinkMtc.find())
		{
			mkd.pullLinkStrings.add(prLinkMtc.group());
		}
		}
	}
	
	//build MarkdownElements end-----------------------------------------------------------------------------------------	
	
	//build FilesChanged start-----------------------------------------------------------------------------------------
	
	void buildFilesChanged()
	{
		int filesChangedCounter = -1;
		boolean commitNotMissing = false;
		for(int i=0;i<this.pr.commits.size();i++)
		{
			Commit cmiti = this.pr.commits.get(i);
			if(!cmiti.commitType.equals("merge") && !cmiti.commitType.equals("initial"))
			{
			
			FilesChanged flcg = new FilesChanged(this.pr);
			String srcBeforePath = this.prDir.getAbsolutePath() + File.separator + "before_"+cmiti.shortSha;
			flcg.srcBeforeDir = this.buildFile(srcBeforePath);
			String srcAfterPath = this.prDir.getAbsolutePath() + File.separator + "after_"+ cmiti.shortSha;
			flcg.srcAfterDir = this.buildFile(srcAfterPath);
			flcg.srcBeforeDirName = "before_"+cmiti.shortSha;
			flcg.srcAfterDirName = "after_"+cmiti.shortSha;
			
			String C_cdAfterPath = "cd "+ flcg.srcAfterDir.getAbsolutePath() + " ;";
			String C_cdBeforePath = "cd "+ flcg.srcBeforeDir.getAbsolutePath() + " ;";
			String C_gitClone_After = "git clone "+ this.pr.headRepositorySrcDLUrl+" "+flcg.srcAfterDir.getAbsolutePath()+" ;";
			String C_gitClone_before= "git clone "+ this.pr.headRepositorySrcDLUrl+" "+flcg.srcBeforeDir.getAbsolutePath()+" ;";
			String C_gitCheckout_Branch = "git checkout "+ this.pr.headBranch+" ;";
			String C_gitCheckout_Commit = "git checkout "+ cmiti.sha +" ;";
			String C_gitCheckout_BeforeCommit = "git checkout "+"HEAD~"+" --"+" ;";
			String C_gitCheckout_BeforeMergeCommit = "git checkout "+"HEAD~1^2 --"+" ;";
			String command = "cd ;" +
			          C_gitClone_After +
			          C_cdAfterPath +
			          C_gitCheckout_Branch +
			          C_gitCheckout_Commit +
			          "cd ;"+
			          C_gitClone_before +
			          C_cdBeforePath +
			          C_gitCheckout_Branch +
			          C_gitCheckout_Commit +
			          C_gitCheckout_BeforeCommit;
			String commandChangeToCommit = "cd ;"+ 
			                            C_cdAfterPath + 
			                            C_gitCheckout_Commit + 
			                            "cd ;" + 
			                            C_cdBeforePath + 
			                            C_gitCheckout_Commit + 
			                            C_gitCheckout_BeforeCommit;
			
			this.srcDownload(command);
			System.out.println("Download Over !!!");
			try {
				commitNotMissing = this.checkCommitExist(commandChangeToCommit);
			if(commitNotMissing)
			{
			String C_gitDiff = "git diff " + flcg.srcBeforeDir + " " + flcg.srcAfterDir;
			String commandDiff = "cd ;" + C_gitDiff;
			this.excDiff(commandDiff, flcg);
			this.buildDiffFileUnitElements(flcg);
			filesChangedCounter++;
			flcg.instId = "filesChanged"+filesChangedCounter;
			cmiti.flcg = flcg;
			flcg.cmit = cmiti;
			}else {
				System.out.println("Commit missing found !!!");
				throw new commitMissingException("fatal: reference is not a tree:");
			}
			}catch(Exception e)
			{
				this.recordException(e);
			}
			}
			if(this.exceptionDone)
			{
				break;
			}
		}
	}
	
	void buildPrFilesChanged()
	{
		if(!this.pr.commits.isEmpty())
		{
			if(this.pr.commits.size() > 1)
			{
				FilesChanged flcgPr = new FilesChanged(this.pr);
				CodeElement cdePr = new CodeElement(flcgPr, this.pr);
				flcgPr.cde = cdePr;
				flcgPr.instId="prFilesChanged";
				cdePr.setInstId("prCodeElement");
				for(Commit cmiti : this.pr.commits)
				{
					if(!cmiti.commitType.equals("merge") && !cmiti.commitType.equals("initial"))
					{
					  if(!this.ghFilesChangedUnits.isEmpty() && !cmiti.flcg.diffFileUnits.isEmpty())
					  {
						  for(DiffFileUnit dfui : cmiti.flcg.diffFileUnits) 
						  {
							  if(this.inFinalFilesChanged(dfui))
							  {
								  flcgPr.diffFileUnits.add(dfui);
							  }
							  
						  }
						  
					  }
					if(flcgPr.hasJavaSrcFile==false)
					{
					  flcgPr.hasJavaSrcFile = cmiti.flcg.hasJavaSrcFile;
					}
					
					if (!cmiti.flcg.cde.getCgProjectUnits().isEmpty()) {
						for (ChangedProjectUnit cgpjui : cmiti.flcg.cde.getCgProjectUnits()) {
							if (!cgpjui.getCgFileUnits().isEmpty()) {
								for (ChangedFileUnit cgfui : cgpjui.getCgFileUnits()) {
									if (this.inFinalFilesChanged(cgfui)) {
										if (!this.inPrCdeCgFileUnits(cdePr, cgfui)) {
											cdePr.getCgFileUnits().add(cgfui);
										}
									}
								}
							}
						}
					}

					}
				}
				this.pr.flcg = flcgPr;
			}else if(this.pr.commits.size() ==1)
			{
				try {
				this.pr.flcg = this.pr.commits.get(0).flcg;
				this.pr.flcg.instId="prFilesChanged";
				this.pr.flcg.cde.setInstId("prCodeElement");
				if(!this.pr.commits.get(0).flcg.cde.getCgProjectUnits().isEmpty())
				{
					for(ChangedProjectUnit cgpjui : this.pr.commits.get(0).flcg.cde.getCgProjectUnits())
					{
						this.pr.flcg.cde.getCgFileUnits().addAll(cgpjui.getCgFileUnits());
					}
				}
				}catch(Exception e)
				{
					this.recordException(e);
				}
			}
		}
	}
	
	
	
	void buildGHPRFinalFilesChanged()
	{
		try {
			if(!ghpr.listFiles().toList().isEmpty())
			{
				for(GHPullRequestFileDetail ghfdi : ghpr.listFiles().toList())
				{
					this.ghprFinalFiles.add(ghfdi);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.recordException(e);
		}
	}
		
	void buildGHFilesChangedUnits()
	{
		if(!this.ghprFinalFiles.isEmpty())
		{
			for(GHPullRequestFileDetail ghfdi : this.ghprFinalFiles)
			{
				if(!ghfdi.getStatus().equals("removed"))
				{
				try {
					GHContent content = this.repository.getFileContent(ghfdi.getFilename(), this.ghpr.getHead().getSha());
		            
					BufferedReader reader = new BufferedReader(new InputStreamReader(content.read()));
					String s="";
					String alls="";
					while((s = reader.readLine())!=null)
					{
						alls += s;
					}
					GHFilesChangedUnit ghfcui = new GHFilesChangedUnit(content.getPath(), alls);
					this.ghFilesChangedUnits.add(ghfcui);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					this.recordException(e);
				}
				}else if (ghfdi.getStatus().equals("removed"))
				{
					String removedPath="";
					String removedContent="";
					for(GHPullRequestCommitDetail ghctdi : ghpr.listCommits() )
					{
						try {
							GHCommit ghcmti = this.repository.getCommit(ghctdi.getSha());
							for(GHCommit.File file : ghcmti.listFiles())
							{
								if(file.getFileName().equals(ghfdi.getFilename()) && file.getStatus().equals("removed"))
								{
									removedPath = file.getFileName();
									removedContent = file.getPatch();
									GHFilesChangedUnit ghfcui = new GHFilesChangedUnit(removedPath,removedContent);
									this.ghFilesChangedUnits.add(ghfcui);
								}
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							this.recordException(e);
						}
						
					}
				}
			}
		}
	}
	
	void printGHFilesChangedUnits()
	{
		if(!this.ghFilesChangedUnits.isEmpty())
		{
			for(GHFilesChangedUnit ghfcui : this.ghFilesChangedUnits)
			{
				System.out.println("  GHFilesChangedUnit Path :"+ghfcui.filePath);
				System.out.println("  GHFilesChangedUnit Content :"+ghfcui.fileContent);
			}
		}
	}
	
	boolean inFinalFilesChanged(DiffFileUnit dfua)
	{
		if(!this.ghFilesChangedUnits.isEmpty())
		{
			for(GHFilesChangedUnit ghfcui : this.ghFilesChangedUnits)
			{
				if(dfua.fileType.equals("add") || dfua.fileType.equals("change"))
				{
					if(ghfcui.getFilepath().equals(dfua.relativePath) && ghfcui.getFileContent().contains(dfua.diffBodyAdd))
					{
						return true;
					}	
				}else if(dfua.fileType.equals("delete"))
				{
					if(ghfcui.getFilepath().equals(dfua.relativePath) && ghfcui.getFileContent().contains(dfua.diffBodyDelete))
					{
						return true;
					}
				}	
			}
		}
		return false;
	}
	
	boolean inFinalFilesChanged(ChangedFileUnit cgfua)
	{
		if(!this.ghFilesChangedUnits.isEmpty())
		{
			String exLineSource="";
			if(cgfua.getType().equals("add") || cgfua.getType().equals("change"))
			{
				String[] ss = cgfua.getSourceAfter().split("\n");
				for(int i=0;i<ss.length;i++)
				{
					exLineSource += ss[i];
				}
			}else if(cgfua.getType().equals("delete"))
			{
				String[] ss = cgfua.getSourceBefore().split("\n");
				for(int i=0;i<ss.length;i++)
				{
					exLineSource += ss[i];
				}
			}
			for(GHFilesChangedUnit ghfcui : this.ghFilesChangedUnits)
			{
				if(cgfua.getType().equals("add") || cgfua.getType().equals("change"))
				{
					if(cgfua.getPathAfter().contains(ghfcui.filePath) && ghfcui.fileContent.equals(exLineSource))
					{
						return true;
					}
				}else if(cgfua.getType().equals("delete"))
				{
					if(cgfua.getPathBefore().contains(ghfcui.filePath) && ghfcui.fileContent.equals(exLineSource))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	boolean inPrCdeCgFileUnits(CodeElement cde, ChangedFileUnit cgfua)
	{
		if(!cde.getCgFileUnits().isEmpty())
		{
			for(ChangedFileUnit cgfui : cde.getCgFileUnits())
			{		
				if(Objects.equals(cgfui.getPathBefore(), cgfua.getPathBefore()) && Objects.equals(cgfui.getPathAfter(), cgfua.getPathAfter()))
				{
					if(Objects.equals(cgfui.getSourceBefore(), cgfua.getSourceBefore()) && Objects.equals(cgfui.getSourceAfter(), cgfua.getSourceAfter()))
					{
						return true;
					}
				}
			}
		
		}
		return false;
	}
	
	void removeRepetitiveDiffFileUnitInPRFilesChanged()
	{
		if(this.pr.flcg != null)
		{
			if(!this.pr.flcg.diffFileUnits.isEmpty())
			{
				ArrayList<DiffFileUnit> ListA = new ArrayList<>();
				ArrayList<DiffFileUnit> ListB = new ArrayList<>();
				ListA.addAll(this.pr.flcg.diffFileUnits);
				ListB.addAll(this.pr.flcg.diffFileUnits);				
				
				for(DiffFileUnit dfuia : ListA)
				{
					for(DiffFileUnit dfuib : ListB)
					{
						if(dfuia.fileType.equals("add") || dfuia.fileType.equals("change"))
						{
							if(dfuib.fileType.equals("add") || dfuib.fileType.equals("change"))
							{
								if(dfuia.diffBodyAdd.contains(dfuib.diffBodyAdd) && !dfuia.diffBodyAdd.equals(dfuib.diffBodyAdd))
								{
									String[] ssA = dfuia.diffBodyAdd.split(" ");
									int wordsA = ssA.length;
									String[] ssB = dfuib.diffBodyAdd.split(" ");
									int wordsB = ssB.length;
									if(wordsA > wordsB)
									{
									   Iterator<DiffFileUnit> itr = this.pr.flcg.diffFileUnits.iterator();
									   while(itr.hasNext())
									   {
										   DiffFileUnit dfui = itr.next();
										   if(dfui.relativePath.equals(dfuib.relativePath) && dfui.fileType.equals(dfuib.fileType) && dfui.diffBodyAll.equals(dfuib.diffBodyAll))
										   {
											   itr.remove();
										   }
									   }
										
									}else if(wordsA < wordsB)
									{
										Iterator<DiffFileUnit> itr = this.pr.flcg.diffFileUnits.iterator();
										   while(itr.hasNext())
										   {
											   DiffFileUnit dfui = itr.next();
											   if(dfui.relativePath.equals(dfuia.relativePath) && dfui.fileType.equals(dfuia.fileType) && dfui.diffBodyAll.equals(dfuia.diffBodyAll))
											   {
												   itr.remove();
											   }
										   }
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	Commit getFirstProcessingCommit(ArrayList<Commit> commits)
	{
		if(!commits.isEmpty())
		{
			for(Commit cmiti : commits)
			{
				if(!cmiti.commitType.equals("merge") && !cmiti.commitType.equals("initial"))
				{
					return cmiti;
				}
			}
		}
		return null;
	}
	
	Commit getLastProcessingCommit(ArrayList<Commit> commits)
	{
		if(!commits.isEmpty())
		{
			for(int i = commits.size()-1;i>=0;i--)
			{
				Commit cmiti = commits.get(i);
				if(!cmiti.commitType.equals("merge") && !cmiti.commitType.equals("initial"))
				{
					return cmiti;
				}
			}
		}
		return null;
	}
	
	boolean hasMergeCommit()
	{
		if(!this.pr.commits.isEmpty())
		{
			for(Commit cmiti : this.pr.commits)
			{
				if(cmiti.commitType.equals("merge"))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public void setRootSrcPath(String path)
	{
		this.rootSrcPath = path;
	}
	
	void buildModelSourceDir()
	{
		String rootPath = this.rootSrcPath + File.separator + "PRCollector";
		this.rootDir = this.buildFile(rootPath);
		String repoPath = this.rootDir.getAbsolutePath() + File.separator + this.repository.getName();
		this.repositoryDir = this.buildFile(repoPath);
		String prPath = this.repositoryDir.getAbsolutePath() + File.separator + this.ghpr.getNumber();
		this.prDir = this.buildFile(prPath);
	
	}
	
	
	File buildFile(String path)
	{
		File f = new File(path);
		if(f.exists() == false)
		{
			f.mkdir();
			return f;
		}else {
			return f;
		}
	}
	
	void srcDownload(String command)
	{
        int exitCode = 8888;		
		ProcessBuilder processBuilder = new ProcessBuilder("bash","-c",command);
		try {
			processBuilder.inheritIO();
			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			
			while ((line = reader.readLine())!=null)
			{
				System.out.println(line+"      ************");//execute message
			}
			exitCode = process.waitFor();		
			reader.close();	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.recordException(e);
		}
	}
	
	
	void excDiff(String commandDiff, FilesChanged flcg)
	{
		int exitCode = 7777;//exitCode default value
		ProcessBuilder processBuilder = new ProcessBuilder("bash","-c",commandDiff);
		try {
			//processBuilder.inheritIO();
			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			StringBuilder diffOutput =  new StringBuilder();
			
			while ((line = reader.readLine())!=null)
			{
				diffOutput.append(line).append("\n");
			}
			exitCode = process.waitFor();		
			reader.close();		
			//System.out.println(diffOutput.toString());
			String s = diffOutput.toString();
			this.buildDiffFileUnits(s,flcg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.recordException(e);
		}
		
	}
	
	boolean checkCommitExist(String commandChangeToCommit)
	{
		int exitCode = 6666;
		ProcessBuilder processBuilder = new ProcessBuilder("bash","-c",commandChangeToCommit);
		processBuilder.redirectErrorStream(true);
		try {
			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while((line = reader.readLine()) != null)
			{
				System.out.println("      "+ line +"****************");
				if(line.contains("fatal: reference is not a tree:"))
				{
					return false;
					
				}
			}
			exitCode = process.waitFor();
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.recordException(e);
		}
		return true;
		
	}
	
	void buildDiffFileUnits(String s, FilesChanged flcg)
	{
		String[] ss = s.split("diff --git ");
    	if(ss.length >1)
    	{	
    	for(int i=1;i<ss.length;i++)
    	{
    		String ssi = ss[i];
    		String[] lss = ssi.split("\n");
    		if(lss.length != 0 && lss[0].contains("/.git/")==false && lss[0]!=null)
    		{
    			DiffFileUnit dfu = new DiffFileUnit();
    			int plusCt =9999;
    			for(int j=0; j<lss.length;j++)
        		{
    				String[] tss = lss[j].split(" "); 
        			if(tss.length == 2 && tss[0].equals("---") && tss[1].equals("/dev/null") == false && j < plusCt)
        			{
        				String [] fss = lss[j].split("--- a");
        				dfu.absolutePathBefore = fss[1];
        			}else if(tss.length==2 &&  tss[0].equals("+++") && j < plusCt)
        			{
        				if(tss[1].equals("/dev/null") == false)
        				{
        				   String[] fss = lss[j].split("\\+\\+\\+ b");
        				   dfu.absolutePathAfter = fss[1];
        				}	
        				plusCt=j;
        			}else if(j > plusCt)
        			{
        				char firstC = lss[j].charAt(0);
        				if(firstC == '-')
        				{
        					DiffLineUnit dflu = new DiffLineUnit();
        					dflu.type ="delete";
        					dflu.text = lss[j].substring(1);
        					dfu.diffLineUnits.add(dflu);
        				}else if(firstC == '+')
        				{
        					DiffLineUnit dflu = new DiffLineUnit();
        					dflu.type = "add";
        					dflu.text = lss[j].substring(1);
        					dfu.diffLineUnits.add(dflu);
        				}
        			}
        		}    			 
    			flcg.diffFileUnits.add(dfu);
    		}   		
    	  }
    	}
    	
	}
	
	void buildDiffFileUnitElements(FilesChanged flcg)
	{
		if(flcg.diffFileUnits.isEmpty()==false)
		{
			for(DiffFileUnit dfui : flcg.diffFileUnits)
			{
				if(dfui.absolutePathBefore.equals("") == false)
				{
					String[] ss = dfui.absolutePathBefore.split(flcg.srcBeforeDir+File.separator);
					dfui.relativePath = ss[1];
				}else if(dfui.absolutePathAfter.equals("") == false)
				{
					String[] ss = dfui.absolutePathAfter.split(flcg.srcAfterDir+File.separator);
					dfui.relativePath = ss[1];
				}
				
				if(dfui.diffLineUnits.isEmpty() == false)
				{
					for(DiffLineUnit dflui : dfui.diffLineUnits)
					{
						dfui.diffBodyAll += dflui.text; 
						if(dflui.type.equals("delete"))
						{
							dfui.diffBodyDelete += dflui.text;
						}else if(dflui.type.equals("add"))
						{
							dfui.diffBodyAdd += dflui.text;
						}
					}
				}
				
				String[]ss = dfui.relativePath.split(File.separator);
				int l = ss.length;
				if(ss[l-1].contains(".java"))
				{
					dfui.isJavaSrcFile = true;
					flcg.hasJavaSrcFile = true;
				}
				

				if(dfui.absolutePathBefore!="" && dfui.absolutePathAfter=="")
				{
					dfui.fileType ="delete";
				}else if(dfui.absolutePathBefore=="" && dfui.absolutePathAfter!="")
				{
					dfui.fileType ="add";
				}else if(dfui.absolutePathBefore!="" && dfui.absolutePathAfter!="")
				{
					dfui.fileType ="change";
				}
				
			}
		}
			
	}
	
	void buildJxp3Elements()
	{
		System.out.println("   -------flag of Jxp3Elements builder");
		this.j3mdb = new Jxp3ModelBuilder(this);
		j3mdb.build();
	}
	
	void buildDiffFileUnitIsTest()
	{
		if(!this.pr.getCommits().isEmpty())
		{
			for(Commit cmiti : this.pr.getCommits())
			{
				if(!cmiti.getCommitType().equals("merge") && !cmiti.getCommitType().equals("initial"))
				{
				  this.buildDiffFileUnitIsTest1(cmiti.getFlcg());
				}
			}
		}
		this.buildDiffFileUnitIsTest1(this.pr.getFlcg());
	}
	
	void buildDiffFileUnitIsTest1(FilesChanged fcg)
	{
		try {
		if (fcg!=null && !fcg.getDiffFileUnits().isEmpty()) 
		{
			for (DiffFileUnit dfui : fcg.getDiffFileUnits()) 
			{
				if (dfui.isJavaSrcFile) 
				{
					for (ChangedProjectUnit cgpji : fcg.getCde().getCgProjectUnits()) 
					{
						for (ChangedFileUnit cgfli : cgpji.getCgFileUnits()) 
						{
							if (dfui.getFileType().equals("add") && dfui.getFileType().equals(cgfli.getType())) 
							{
								if (dfui.getAbsolutePathAfter().equals(cgfli.getPathAfter())) 
								{
									dfui.setTest(cgfli.isTest());
								}
							}else if(dfui.getFileType().equals("delete") && dfui.getFileType().equals(cgfli.getType()))
							{
								if(dfui.getAbsolutePathBefore().equals(cgfli.getPathBefore()))
								{
									dfui.setTest(cgfli.isTest());
								}
							}else if(dfui.getFileType().equals("change") && dfui.getFileType().equals(cgfli.getType()))
							{
								if(dfui.getAbsolutePathBefore().equals(cgfli.getPathBefore()) && dfui.getAbsolutePathAfter().equals(cgfli.getPathAfter()))
								{
									dfui.setTest(cgfli.isTest());
								}
							}
						}
					}
				}
			}
		}
			
		}catch(Exception e)
		{
			this.recordException(e);
		}
		
	}
	
	//build FilesChanged end-----------------------------------------------------------------------------------------	
	
	//build Labels start-----------------------------------------------------------------------------------------	
	void buildRepoLabels()
	{		
		try {
			PagedIterable<GHLabel> repoLbs = this.repository.listLabels();
			for(GHLabel ghlbi : repoLbs)
			{
				this.repoLabels.add(ghlbi);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.recordException(e);
		}
	}
	
	void buildLabels()
	{
		int prLabelCounter= -1;
		try {
			List<GHIssueEvent> events = this.ghpr.listEvents().toList();
			for(GHIssueEvent iei : events)
			{
				String eventType = iei.getEvent();
				if(eventType.equals("labeled"))
				{
				   GHLabel labi = iei.getLabel();
				   PRLabel prlb = new PRLabel(this.pr);
				   prlb.name = labi.getName();
				   prlb.color = labi.getColor();
				   prlb.ghEvent = iei;
				   prLabelCounter ++;
				   prlb.instId = "prLabel"+prLabelCounter;
				   this.pr.addedLabels.add(prlb);
				}else if(eventType.equals("unlabeled"))
				{
					GHLabel labi = iei.getLabel();
					PRLabel prlb = new PRLabel(this.pr);
					prlb.name = labi.getName();
					prlb.color = labi.getColor();
					prlb.ghEvent = iei;
					prLabelCounter ++;
					prlb.instId = "prLabel"+prLabelCounter;
					this.pr.removedLabels.add(prlb);
				}
				
			}
						
			
			for(GHLabel rghlbi : this.repoLabels)
			{
				for(PRLabel prlbi : this.pr.addedLabels)
				{
				   if(rghlbi.getName().equals(prlbi.name) && rghlbi.getColor().equals(prlbi.color))
				  {
					prlbi.ghid = rghlbi.getId();
					prlbi.description = rghlbi.getDescription();
				  }
				}
				for(PRLabel prlbi : this.pr.removedLabels)
				{
				   if(rghlbi.getName().equals(prlbi.name) && rghlbi.getColor().equals(prlbi.color))
				  {
						prlbi.ghid = rghlbi.getId();
						prlbi.description = rghlbi.getDescription();
				  }
				}
			}
			
			
			for(Event evi : this.pr.conv.events)
			{
				for(PRLabel prlbi : this.pr.addedLabels)
				{
					if(evi.ghEvent.getId() == prlbi.ghEvent.getId())
					{
						prlbi.event = evi;
					}
				}
				
				for(PRLabel prlbi : this.pr.removedLabels)
				{
					if(evi.ghEvent.getId() == prlbi.ghEvent.getId())
					{
						prlbi.event = evi;
					}
				}
				
			}
			
			for(PRLabel prlbi : this.pr.addedLabels)
			{
				if(this.isInRemoveLabels(prlbi)==false)
				{
					this.pr.finalLabels.add(prlbi);
				}
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.recordException(e);
			
		}
	}
	
	boolean isInRemoveLabels(PRLabel prlba)
	{
		for(PRLabel prlbi : this.pr.removedLabels)
		{
			if(prlbi.name.equals(prlba.name) && prlbi.color.equals(prlba.color))
			{
				return true;
			}
		}
		return false;
	}
	
	//build Labels end-----------------------------------------------------------------------------------------	
	
	//check repeated file
	boolean checkRepeatedDirectory()
	{
		String repeatedPath=this.rootSrcPath+File.separator+"PRCollector"+File.separator+this.repository.getName()+File.separator+this.ghpr.getNumber();
		File f = new File(repeatedPath);
		if(f.exists())
		{
			return true;
		}else {
			return false;
		}
	}
	
	public void recordException(Exception outputE)
	{
		if(this.writeErrorLogs)
		{
		String eFilePath = this.rootSrcPath+File.separator+"PRCollector"+ File.separator+ this.repository.getName()+File.separator+"error.txt";
		File f = new File(eFilePath);
		System.out.println(f.getAbsolutePath());
		try {
			FileWriter fw = new FileWriter(f,true);
			PrintWriter pw = new PrintWriter(fw);
			outputE.printStackTrace(pw);
			pw.println("------------------------------------------------------"+this.repository.getName()+" : "+this.ghpr.getNumber());
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.exceptionDone = true;
		System.out.println("Error logs has written.  ");
		}else {
			outputE.printStackTrace();
		}
	}
	

	public String getGHToken() {
		return GHToken;
	}

	public void setGHToken(String gHToken) {
		GHToken = gHToken;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public int getPrNum() {
		return prNum;
	}

	public void setPrNum(int prNum) {
		this.prNum = prNum;
	}

	public GitHub getGithub() {
		return github;
	}

	public void setGithub(GitHub github) {
		this.github = github;
	}

	public GHRepository getRepository() {
		return repository;
	}

	public void setRepository(GHRepository repository) {
		this.repository = repository;
	}

	public GHPullRequest getGhpr() {
		return ghpr;
	}

	public void setGhpr(GHPullRequest ghpr) {
		this.ghpr = ghpr;
	}

	public PullRequest getPr() {
		return pr;
	}

	public void setPr(PullRequest pr) {
		this.pr = pr;
	}

	public File getRootDir() {
		return rootDir;
	}

	public void setRootDir(File rootDir) {
		this.rootDir = rootDir;
	}

	public File getDatasetDir() {
		return datasetDir;
	}

	public void setDatasetDir(File datasetDir) {
		this.datasetDir = datasetDir;
	}

	public File getRepositoryDir() {
		return repositoryDir;
	}

	public void setRepositoryDir(File repositoryDir) {
		this.repositoryDir = repositoryDir;
	}

	public File getPrDir() {
		return prDir;
	}

	public void setPrDir(File prDir) {
		this.prDir = prDir;
	}

	public HashSet<GHLabel> getRepoLabels() {
		return repoLabels;
	}

	public void setRepoLabels(HashSet<GHLabel> repoLabels) {
		this.repoLabels = repoLabels;
	}

	public String getRootSrcPath() {
		return rootSrcPath;
	}
	
	public void downloadFilterChangedFiles(int min, int max)
	{
		this.downloadChangedFiles_min = min;
		this.downloadChangedFiles_max = max;
	}
	
	public void downloadFilterCommitNum(int min, int max)
	{
		this.commitMin = min;
		this.commitMax = max;
	}
	
	public void downloadBannedLabels(ArrayList<String> labels)
	{
		this.downloadBannedLabels = labels;
	}
	
	void checkChangedFilesFlag()
	{
		int changedFilesNum;
		try {
			changedFilesNum = this.ghpr.listFiles().toList().size();
			if(this.downloadChangedFiles_min > changedFilesNum || changedFilesNum > this.downloadChangedFiles_max)
			{
				this.download_ChangedFiles = false;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.recordException(e);
		}
		
		
	}
	
	void checkCommitNumFlag()
	{
		int commitNum;
		try {
			commitNum = this.ghpr.listCommits().toList().size();
			if(this.commitMin > commitNum || commitNum > this.commitMax)
			{
				this.download_CommitNum = false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.recordException(e);
		}
	}
	
	void checkBannedLabelsFlag()
	{
		if(!this.downloadBannedLabels.isEmpty())
		{
			boolean flag=true;
			for(String lbi : this.downloadBannedLabels)
			{
				if(!this.pr.getFinalLabels().isEmpty())
				{
					for(PRLabel prlbi : this.pr.getFinalLabels())
					{
						if(lbi.equals(prlbi.getName()))
						{
							flag=false;
						}
					}
				}
			}
			this.download_BannedLabels=flag;
		}
		
	}
	
	public void writeFile(boolean writeFile)
	{
		this.writeFile = writeFile;
	}
	
	public void deleteSrcFile(boolean deleteSrcFile)
	{
		this.deleteSrcFile = deleteSrcFile;
	}
	
	public void setWriteErrorLogs(boolean f)
	{
		this.writeErrorLogs=f;
	}
	
	
	
}
