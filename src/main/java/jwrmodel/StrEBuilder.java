package jwrmodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import org.jtool.tinyjxplatform.srcmodel.JavaMethod;
import prmodel.*;
import jxp3model.*;

public class StrEBuilder {
	
	PullRequest pr;
	Str_PullRequest str_pr;
	
	public StrEBuilder(PullRequest pr)
	{
		this.pr = pr;
		
	}
	
	 public Str_PullRequest buildJwrModel()
    {
	   this.str_pr = new Str_PullRequest();
	   this.buildPrBase();
	   this.buildDescription();
	   this.buildParticipants();
	   this.buildConversation();
	   this.buildCommits();
	   this.buildPRFilesChanged();
	   this.buildLabels();
	   return this.str_pr;
    }
	 
	 void buildPrBase()
     {
    	 this.str_pr.title = this.pr.getTitle();
    	 this.str_pr.id = this.pr.getId();
    	 this.str_pr.state = this.pr.getState();
    	 this.str_pr.repositoryName = this.pr.getRepositoryName();
    	 this.str_pr.createDate = this.pr.getCreateDate();
    	 this.str_pr.endDate = this.pr.getEndDate();
    	 this.str_pr.isMerged = this.pr.isMerged();
    	 this.str_pr.isStandardMerged = this.pr.isStandardMerged();
    	 this.str_pr.srcRetrievable = this.pr.isSrcRetrievable();
    	 this.str_pr.mergeBranch = this.pr.getMergeBranch();
    	 this.str_pr.headBranch = this.pr.getHeadBranch();
    	 this.str_pr.prPageUrl = this.pr.getPrPageUrl();
    	 this.str_pr.repositorySrcDLUrl = this.pr.getRepositorySrcDLUrl();
    	 this.str_pr.headRepositorySrcDLUrl = this.pr.getHeadRepositorySrcDLUrl();
    	 this.str_pr.repositoryBranches = this.pr.getRepositoryBranches();
    	 this.str_pr.headRepositoryBranches = this.pr.getHeadRepositoryBranches();
    	 this.str_pr.htmlDescription = this.pr.getScrpe().getDescriptionScrp();
     }
	 
	 void buildDescription()
     {
    	 Str_Description str_des = new Str_Description();
    	 str_des.setInstId( this.pr.getDcpt().getInstId());
    	 str_des.setBody(this.pr.getDcpt().getBody());
    	 
    	 Str_MarkdownElement smkde = new Str_MarkdownElement();
    	 MarkdownElement mkde = this.pr.getDcpt().getMde();
    	 Str_MarkdownElementValueSet(smkde, mkde);
    	 str_des.setSmde(smkde);
    	 this.str_pr.str_description = str_des;
     }
	 
	 void Str_MarkdownElementValueSet(Str_MarkdownElement smkde, MarkdownElement mkde)
     {
    	 smkde.setHeadingStrings(mkde.getHeadingStrings());
    	 smkde.setBoldStrings(mkde.getBoldStrings());
    	 smkde.setItalicStrings(mkde.getItalicStrings());
    	 smkde.setQuoteStrings(mkde.getQuoteStrings());
    	 smkde.setLinkStrings(mkde.getLinkStrings());
    	 smkde.setCodeStrings(mkde.getCodeStrings());
    	 smkde.setCodeBlockStrings(mkde.getCodeBlockStrings());
    	 smkde.setTextStrings(mkde.getTextStrings());
    	 smkde.setMentionStrings(mkde.getMentionStrings());
    	 smkde.setTextLinkStrings(mkde.getTextLinkStrings());
    	 smkde.setPullLinkStrings(mkde.getPullLinkStrings());
    	 smkde.setIssueLinkStrings(mkde.getIssueLinkStrings());
    	 smkde.setHtmlComments(mkde.getHtmlComments());
     }
	 
	 void buildParticipants()
     {
    	 HashSet<Str_Participant> str_prts = new HashSet<>();
    	 for(Participant prti : this.pr.getParticipants())
    	 {
    		 Str_Participant str_prti = new Str_Participant();
    		 str_prti.setInstId(prti.getInstId());
    		 str_prti.setLoginName(prti.getLoginName());
    		 str_prti.setName(prti.getName());
    		 str_prti.setId(prti.getId());
    		 str_prti.setRole(prti.getRole());
    		 str_prti.setLocation(prti.getLocation());
    		 str_prti.setRecord(prti.getRecord());
    		 str_prti.setFollowers(prti.getFollowers());
    		 str_prti.setFollowings(prti.getFollowings());
    		 str_prts.add(str_prti);
    	 }
    	 this.str_pr.str_participants = str_prts;
     }
	 
	 void buildConversation()
     {
    	 Str_Conversation str_conv = new Str_Conversation();
    	 this.buildComments(str_conv);
    	 this.buildReviewComments(str_conv);
    	 this.buildEvents(str_conv);
    	 this.buildReviewEvents(str_conv);
    	 this.buildCodeLineReview(str_conv);
    	 this.buildTimeLine(str_conv);
    	 this.str_pr.str_conversation = str_conv;
     }
	 
	 void buildComments(Str_Conversation str_conv)
     {
    	 LinkedHashSet<Str_Comment> str_comments = new LinkedHashSet<>();
    	 LinkedHashSet<Comment> comments = this.pr.getConv().getComments();
    	 if(comments.size()!=0)
    	 {
    		 for(Comment cmti : comments)
    		 {
    			 Str_Comment str_cmti = new Str_Comment();
    			 str_cmti.setParticipantInstId(cmti.getCommenter().getInstId());
    			 str_cmti.setCommentDate(cmti.getCommentDate());
    			 str_cmti.setBody(cmti.getBody());
    			 str_cmti.setInstId(cmti.getInstId());
    			 Str_MarkdownElement smkde = new Str_MarkdownElement();
    			 MarkdownElement mkde = cmti.getMde();
    			 Str_MarkdownElementValueSet(smkde, mkde);
    			 str_cmti.setStr_markdownElement(smkde);
    			 str_comments.add(str_cmti);
    		 }
    		 str_conv.setComments(str_comments);
    	 }
     }
	 
	 void buildReviewComments(Str_Conversation str_conv)
     {
    	 LinkedHashSet<Str_ReviewComment> str_reviewComments = new LinkedHashSet<>();
    	 LinkedHashSet<ReviewComment> reviewComments = this.pr.getConv().getReviewComments();
    	 if(!reviewComments.isEmpty())
    	 {
    		 for(ReviewComment rcmti : reviewComments)
    		 {
    			 Str_ReviewComment str_rcmti = new Str_ReviewComment();
    			 str_rcmti.setParticipantInstId(rcmti.getCommenter().getInstId());
    			 str_rcmti.setCommentDate(rcmti.getCommentDate());
    			 str_rcmti.setBody(rcmti.getBody());
    			 str_rcmti.setInstId(rcmti.getInstId());
    			 str_rcmti.setCodeLineReviewInstId(rcmti.getClr().getInstId());
    			 Str_MarkdownElement smkde = new Str_MarkdownElement();
    			 MarkdownElement mkde = rcmti.getMde();
    			 Str_MarkdownElementValueSet(smkde, mkde);
    			 str_rcmti.setStr_markdownElement(smkde);
    			 str_reviewComments.add(str_rcmti);
    		 }
    		 str_conv.setReviewComments(str_reviewComments);
    	 }
    	 
     }
	 
	 
	 void buildEvents(Str_Conversation str_conv)
     {
    	 LinkedHashSet<Str_Event> str_events = new LinkedHashSet<>();
    	 LinkedHashSet<Event> events = this.pr.getConv().getEvents();
    	 if(events.size()!=0)
    	 {
    		 for(Event evti : events)
    		 {
    			 Str_Event str_evti = new Str_Event();
    			 str_evti.setActorInstId(evti.getActor().getInstId());
    			 str_evti.setCommentDate(evti.getEventDate());
    			 str_evti.setBody(evti.getBody());
    			 str_evti.setInstId(evti.getInstId());
    			 str_events.add(str_evti);
    		 }
    		 str_conv.setEvents(str_events);
    	 }
     }
	 
	 void buildReviewEvents(Str_Conversation str_conv)
     {
    	LinkedHashSet<Str_ReviewEvent> str_reviewEvents = new LinkedHashSet<>();
    	LinkedHashSet<ReviewEvent> reviewEvents = this.pr.getConv().getReviewEvents();
    	if(!reviewEvents.isEmpty())
    	{
    		for(ReviewEvent revti : reviewEvents)
    		{
    			Str_ReviewEvent str_revti = new Str_ReviewEvent();
    			str_revti.setActorInstId(revti.getActor().getInstId());
    			str_revti.setCommentDate(revti.getEventDate());
    			str_revti.setBody(revti.getBody());
    			str_revti.setInstId(revti.getInstId());
    			Str_MarkdownElement smkde = new Str_MarkdownElement();
    			MarkdownElement mkde = revti.getMde();
    			Str_MarkdownElementValueSet(smkde,mkde);
    			str_revti.setStr_markdownElement(smkde);
    			str_reviewEvents.add(str_revti);
    		}
    		str_conv.setReviewEvents(str_reviewEvents);
    	}
     }
	 
	 void buildCodeLineReview(Str_Conversation str_conv)
	 {
		LinkedHashSet<Str_CodeLineReview> str_clrs = new LinkedHashSet<>();
		LinkedHashSet<CodeReviewSnippet> clrs = this.pr.getConv().getCodeLineReviews();
		if(!clrs.isEmpty())
		{
			for(CodeReviewSnippet clri : clrs)
			{
				Str_CodeLineReview str_clri = new Str_CodeLineReview();
				str_clri.setInstId(clri.getInstId());
				str_clri.setDiff(clri.getDiff());
				if(!clri.getParticipants().isEmpty())
				{
					HashSet<String> partiInstIds = new HashSet<>();
					for(Participant parti : clri.getParticipants())
					{
						partiInstIds.add(parti.getInstId());
					}
					str_clri.setParticipantInstIds(partiInstIds);
				}
				
				if(!clri.getReviewComments().isEmpty())
				{
					ArrayList<String> rcmInstIds = new ArrayList<>();
					for(ReviewComment rcmti : clri.getReviewComments())
					{
						rcmInstIds.add(rcmti.getInstId());
					}
					str_clri.setReviewCommentInstIds(rcmInstIds);
				}
				str_clrs.add(str_clri);
			}
			str_conv.setCodeLineReviews(str_clrs);
		}
		
	 }
	 
	 void buildTimeLine(Str_Conversation str_conv)
     {
    	 LinkedHashSet<Str_PRAction> str_timeLine = new LinkedHashSet<>();
    	 LinkedHashSet<PRAction> timeLine = this.pr.getConv().getTimeLine();
    	 if(timeLine.size()!=0)
    	 {
    		 for(PRAction prai : timeLine)
    		 {
    			 Str_PRAction str_prai = new Str_PRAction();
    			 str_prai.setParticipantInstId(prai.getExecutor().getInstId());
    			 str_prai.setActionDate(prai.getActionDate());
    			 str_prai.setBody(prai.getBody());
    			 str_prai.setType(prai.getType());
    			 str_prai.setInstId(prai.getInstId());
    			 str_timeLine.add(str_prai);
    		 }
    		 str_conv.setTimeLine(str_timeLine);
    	 }
     }
	 
	 void buildCommits()
     {
    	 ArrayList<Str_Commit> str_commits = new ArrayList<>();
    	 ArrayList<Commit> commits = this.pr.getCommits();
    	 if(commits.size()!=0)
    	 {
    		 for(Commit cmiti : commits)
    		 {
    			 Str_Commit str_cmiti = new Str_Commit();
    			 str_cmiti.setAuthorInstId(cmiti.getAuthor().getInstId());
    			 str_cmiti.setCommitDate(cmiti.getCommitDate());
    			 str_cmiti.setSha(cmiti.getSha());
    			 str_cmiti.setShortSha(cmiti.getShortSha());
    			 str_cmiti.setMessage(cmiti.getMessage());
    			 str_cmiti.setCommitType(cmiti.getCommitType());
    			 str_cmiti.setInstId(cmiti.getInstId());
    			 Str_FilesChanged str_filesChanged = new Str_FilesChanged();
    			 FilesChanged filesChanged = cmiti.getFlcg();
    			 this.buildFilesChanged(str_filesChanged, filesChanged);
    			 str_cmiti.setFilesChanged(str_filesChanged);
    			 if(!cmiti.getCis().isEmpty())
    			 {
    				 ArrayList<Str_CIStatus> str_ciss = new ArrayList<>();
    				 for(CIStatus cisi : cmiti.getCis())
    				 {
    					 Str_CIStatus str_cisi = new Str_CIStatus();
    					 this.buildCIStatus(cisi, str_cisi);
    					 str_cisi.setCommitInstId(cmiti.getInstId());
    					 str_ciss.add(str_cisi);
    				 }
    				 str_cmiti.setCis(str_ciss);
    			 }
    			 
    			 str_commits.add(str_cmiti);
    		 }
    		 this.str_pr.str_commits = str_commits;
    	 }
     }
	 
	 void buildFilesChanged(Str_FilesChanged str_filesChanged, FilesChanged filesChanged)
     {
		 if(filesChanged!=null)
		 {
    	 str_filesChanged.setSrcBeforeDirName(filesChanged.getSrcBeforeDirName());
    	 str_filesChanged.setSrcAfterDirName(filesChanged.getSrcAfterDirName());
    	 str_filesChanged.setHasJavaSrcFile(filesChanged.isHasJavaSrcFile());
    	 str_filesChanged.setInstId(filesChanged.getInstId());
    	 
    	 ArrayList<Str_DiffFileUnit> str_diffFileUnits = new ArrayList<>();
    	 ArrayList<DiffFileUnit> diffFileUnits = filesChanged.getDiffFileUnits();
    	 
    	 if(diffFileUnits.size()!=0)
    	 {
    		 for(DiffFileUnit dfui : diffFileUnits)
    		 {
    			 Str_DiffFileUnit str_dfui = new Str_DiffFileUnit();
    			 str_dfui.setAbsolutePathBefore(dfui.getAbsolutePathBefore());
    			 str_dfui.setAbsolutePathAfter(dfui.getAbsolutePathAfter());
    			 str_dfui.setRelativePath(dfui.getRelativePath());
    			 str_dfui.setDiffBodyAll(dfui.getDiffBodyAll());
    			 str_dfui.setDiffBodyAdd(dfui.getDiffBodyAdd());
    			 str_dfui.setDiffBodyDelete(dfui.getDiffBodyDelete());
    			 str_dfui.setFileType(dfui.getFileType());
    			 str_dfui.setJavaSrcFile(dfui.isJavaSrcFile());
    			 str_dfui.setTest(dfui.isTest());
    			 
    			 ArrayList<Str_DiffLineUnit> str_diffLineUnits = new ArrayList<>();
    			 ArrayList<DiffLineUnit> diffLineUnits = dfui.getDiffLineUnits();
    			 if(diffLineUnits.size()!=0)
    			 {
    				 for(DiffLineUnit dfli : diffLineUnits)
    				 {
    					 Str_DiffLineUnit str_dfli = new Str_DiffLineUnit();
    					 str_dfli.setText(dfli.getText());
    					 str_dfli.setType(dfli.getType());
    					 str_diffLineUnits.add(str_dfli);
    				 }
    				 str_dfui.setDiffLineUnits(str_diffLineUnits);
    			 }
    			 		 
    			 str_diffFileUnits.add(str_dfui);	 
    		 } 		 
    		 str_filesChanged.setStr_diffFileUnits(str_diffFileUnits);  		 
    	 }
    	 this.buildCodeElement(str_filesChanged, filesChanged);
    	    	 
		 } 
     }
	 
	 void buildPrFilesChanged(Str_FilesChanged str_filesChanged, FilesChanged filesChanged)
	 {
		 if(filesChanged!=null)
		 {
			 str_filesChanged.setHasJavaSrcFile(filesChanged.isHasJavaSrcFile());
	    	 str_filesChanged.setInstId(filesChanged.getInstId());
	    	 ArrayList<Str_DiffFileUnit> str_diffFileUnits = new ArrayList<>();
	    	 ArrayList<DiffFileUnit> diffFileUnits = filesChanged.getDiffFileUnits();
	    	 if(diffFileUnits.size()!=0)
	    	 {
	    		 for(DiffFileUnit dfui : diffFileUnits)
	    		 {
	    			 Str_DiffFileUnit str_dfui = new Str_DiffFileUnit();
	    			 str_dfui.setAbsolutePathBefore(dfui.getAbsolutePathBefore());
	    			 str_dfui.setAbsolutePathAfter(dfui.getAbsolutePathAfter());
	    			 str_dfui.setRelativePath(dfui.getRelativePath());
	    			 str_dfui.setDiffBodyAll(dfui.getDiffBodyAll());
	    			 str_dfui.setDiffBodyAdd(dfui.getDiffBodyAdd());
	    			 str_dfui.setDiffBodyDelete(dfui.getDiffBodyDelete());
	    			 str_dfui.setFileType(dfui.getFileType());
	    			 str_dfui.setJavaSrcFile(dfui.isJavaSrcFile());
	    			 str_dfui.setTest(dfui.isTest());
	    			 
	    			 ArrayList<Str_DiffLineUnit> str_diffLineUnits = new ArrayList<>();
	    			 ArrayList<DiffLineUnit> diffLineUnits = dfui.getDiffLineUnits();
	    			 if(diffLineUnits.size()!=0)
	    			 {
	    				 for(DiffLineUnit dfli : diffLineUnits)
	    				 {
	    					 Str_DiffLineUnit str_dfli = new Str_DiffLineUnit();
	    					 str_dfli.setText(dfli.getText());
	    					 str_dfli.setType(dfli.getType());
	    					 str_diffLineUnits.add(str_dfli);
	    				 }
	    				 str_dfui.setDiffLineUnits(str_diffLineUnits);
	    			 }
	    			 		 
	    			 str_diffFileUnits.add(str_dfui);	 
	    		 } 		 
	    		 str_filesChanged.setStr_diffFileUnits(str_diffFileUnits);  		 
	    	 }
	    	 this.buildPrCodeElement(str_filesChanged, filesChanged);
		 }
	 }
	 
	 void buildCodeElement(Str_FilesChanged str_filesChanged , FilesChanged filesChanged)
     {
    	 Str_CodeElement str_codeElement = new Str_CodeElement();
    	 CodeElement codeElement = filesChanged.getCde();
    	 str_codeElement.setInstId(codeElement.getInstId());
    	 LinkedHashSet<String> str_pjNameIndex = new LinkedHashSet<>();
    	 LinkedHashSet<String> pjNameIndex = filesChanged.getCde().getPjNameIndex();
    	 if(pjNameIndex.size()!=0)
    	 {
    		 for(String pjName : pjNameIndex)
    		 {
    			 str_pjNameIndex.add(pjName);
    		 }
    		 str_codeElement.setPjNameIndex(str_pjNameIndex);
    	 }
    	 this.buildChangedProject(str_codeElement, codeElement);
    	 str_filesChanged.setStr_codeElement(str_codeElement);
    	 
     }
	 
	 void buildPrCodeElement(Str_FilesChanged str_filesChanged , FilesChanged filesChanged)
	 {
		 Str_CodeElement str_codeElement = new Str_CodeElement();
    	 CodeElement codeElement = filesChanged.getCde();
    	 str_codeElement.setInstId(codeElement.getInstId());
    	 this.buildPrCdeChangedFile(str_codeElement, codeElement);
    	 str_filesChanged.setStr_codeElement(str_codeElement);
	 }
	 
	 void buildChangedProject(Str_CodeElement str_codeElement, CodeElement codeElement)
     {
    	 ArrayList<Str_ChangedProjectUnit> str_cgProjectUnits = new ArrayList<>();
    	 ArrayList<ChangedProjectUnit> cgProjectUnits = codeElement.getCgProjectUnits();
    	 
    	 if(!cgProjectUnits.isEmpty())
    	 {
    		 for(ChangedProjectUnit cgpjui : cgProjectUnits )
    		 {
    			 Str_ChangedProjectUnit str_cgpjui = new Str_ChangedProjectUnit();
    			 str_cgpjui.setInputPathBefore(cgpjui.getInputPathBefore());
    			 str_cgpjui.setInputNameBefore(cgpjui.getInputNameBefore());
    			 str_cgpjui.setInputPathAfter(cgpjui.getInputPathAfter());
    			 str_cgpjui.setInputNameAfter(cgpjui.getInputNameAfter());
    			 str_cgpjui.setType(cgpjui.getType());
    			 str_cgpjui.setName(cgpjui.getName());
    			 str_cgpjui.setInstId(cgpjui.getInstId());
    			 this.buildChangedFile(str_cgpjui, cgpjui);
    			 
    			 str_cgProjectUnits.add(str_cgpjui);
    		 }
    		 str_codeElement.setCgProjectUnits(str_cgProjectUnits);
    	 }
     }
	 
	 void buildChangedFile(Str_ChangedProjectUnit str_cgpju , ChangedProjectUnit cgpju)
     {
    	 HashSet<Str_ChangedFileUnit> str_cgFileUnits = new HashSet<>();
    	 HashSet<ChangedFileUnit> cgFileUnits = cgpju.getCgFileUnits();
    	 
    	 if(!cgFileUnits.isEmpty())
    	 {
    		 for(ChangedFileUnit cgflui : cgFileUnits)
    		 {
    			 Str_ChangedFileUnit str_cgflui = new Str_ChangedFileUnit();
    			 str_cgflui.setInstId(cgflui.getInstId());
    			 str_cgflui.setName(cgflui.getName());
    			 str_cgflui.setType(cgflui.getType());
    			 str_cgflui.setSourceBefore(cgflui.getSourceBefore());
    			 str_cgflui.setSourceAfter(cgflui.getSourceAfter());
    			 str_cgflui.setPathBefore(cgflui.getPathBefore());
    			 str_cgflui.setPathAfter(cgflui.getPathAfter());
    			 str_cgflui.setTest(cgflui.isTest());
    			 this.buildChangedClass(str_cgflui, cgflui);
	 
    			 str_cgFileUnits.add(str_cgflui);
    		 }
    		 str_cgpju.setCgFileUnits(str_cgFileUnits);
    	 }
     }
	 
	 void buildPrCdeChangedFile(Str_CodeElement str_codeElement, CodeElement codeElement)
	 {
		 ArrayList<Str_ChangedFileUnit> str_cgFileUnits = new ArrayList<>();
    	 HashSet<ChangedFileUnit> cgFileUnits = new HashSet<>();
    	 cgFileUnits.addAll(codeElement.getCgFileUnits());
    	 if(!cgFileUnits.isEmpty())
    	 {
    		 for(ChangedFileUnit cgflui : cgFileUnits)
    		 {
    			 Str_ChangedFileUnit str_cgflui = new Str_ChangedFileUnit();
    			 str_cgflui.setInstId(cgflui.getInstId());
    			 str_cgflui.setName(cgflui.getName());
    			 str_cgflui.setType(cgflui.getType());
    			 str_cgflui.setSourceBefore(cgflui.getSourceBefore());
    			 str_cgflui.setSourceAfter(cgflui.getSourceAfter());
    			 str_cgflui.setPathBefore(cgflui.getPathBefore());
    			 str_cgflui.setPathAfter(cgflui.getPathAfter());
    			 str_cgflui.setTest(cgflui.isTest());
    			 this.buildChangedClass(str_cgflui, cgflui); 
    			 str_cgFileUnits.add(str_cgflui);
    		 }
    		 
    		 str_codeElement.setCgFileUnits(str_cgFileUnits);
    	 }
    	 
	 }
	 
	 void buildChangedClass(Str_ChangedFileUnit str_cgflu, ChangedFileUnit cgflu)
     {
    	HashSet<Str_ChangedClassUnit> str_cgClassUnits = new HashSet<>(); 
    	HashSet<ChangedClassUnit> cgClassUnits = cgflu.getCgClassUnits();
    	
    	if(!cgClassUnits.isEmpty())
    	{
    		for(ChangedClassUnit cgclsui : cgClassUnits)
    		{
    			Str_ChangedClassUnit str_cgclsui = new Str_ChangedClassUnit();
    			str_cgclsui.setInstId(cgclsui.getInstId());
    			str_cgclsui.setName(cgclsui.getName());
    			str_cgclsui.setQualifiedName(cgclsui.getQualifiedName());
    			str_cgclsui.setType(cgclsui.getType());
    			str_cgclsui.setSourceBefore(cgclsui.getSourceBefore());
    			str_cgclsui.setSourceAfter(cgclsui.getSourceAfter());
    			str_cgclsui.setTest(cgclsui.isTest());
    			this.buildChangedMethod(str_cgclsui, cgclsui);
    			this.buildChangedField(str_cgclsui, cgclsui);
    			str_cgClassUnits.add(str_cgclsui);
    		}
    		str_cgflu.setCgClassUnits(str_cgClassUnits);
    	}
     }
	 
	 void buildChangedMethod(Str_ChangedClassUnit str_cgclsu, ChangedClassUnit cgclsu)
     {
    	 HashSet<Str_ChangedMethodUnit> str_cgMethodUnits = new HashSet<>();
    	 HashSet<ChangedMethodUnit> cgMethodUnits = cgclsu.getCgMethodUnits();
    	 
    	 if(!cgMethodUnits.isEmpty())
    	 {
    		 for(ChangedMethodUnit cgmdui : cgMethodUnits)
    		 {
    			 Str_ChangedMethodUnit str_cgmdui = new Str_ChangedMethodUnit();
    			 str_cgmdui.setInstId(cgmdui.getInstId());
    			 str_cgmdui.setName(cgmdui.getName());
    			 str_cgmdui.setQualifiedName(cgmdui.getQualifiedName());
    			 str_cgmdui.setType(cgmdui.getType());
    			 str_cgmdui.setReturnType(cgmdui.getReturnType());
    			 str_cgmdui.setSignatureBefore(cgmdui.getSignatureBefore());
    			 str_cgmdui.setSignatureAfter(cgmdui.getSignatureAfter());
    			 str_cgmdui.setSourceBefore(cgmdui.getSourceBefore());
    			 str_cgmdui.setSourceAfter(cgmdui.getSourceAfter());
    			 str_cgmdui.setTest(cgmdui.isTest());
    			 
    			 if(!cgmdui.getCallingMethodsBefore().isEmpty())
    			 {
    				 HashSet<String> callingMethodsBefore = new HashSet<>();
    				 for(JavaMethod climi : cgmdui.getCallingMethodsBefore())
    				 {
    					 callingMethodsBefore.add(climi.getSource());
    				 }
    				 str_cgmdui.setCallingMethodsBefore(callingMethodsBefore);
    			 }
    			 
    			 if(!cgmdui.getCallingMethodsAfter().isEmpty())
    			 {
    				 HashSet<String> callingMethodsAfter = new HashSet<>();
    				 for(JavaMethod climi : cgmdui.getCallingMethodsAfter())
    				 {
    					 callingMethodsAfter.add(climi.getSource());
    				 }
    				 str_cgmdui.setCallingMethodsAfter(callingMethodsAfter);
    			 }
    			 
    			 if(!cgmdui.getCalledMethodsBefore().isEmpty())
    			 {
    				 HashSet<String> calledMethodsBefore = new HashSet<>();
    				 for(JavaMethod cldmi : cgmdui.getCalledMethodsBefore())
    				 {
    					 calledMethodsBefore.add(cldmi.getSource());
    				 }
    				 str_cgmdui.setCalledMethodsBefore(calledMethodsBefore);
    			 }
    			 
    			 if(!cgmdui.getCalledMethodsAfter().isEmpty())
    			 {
    				 HashSet<String> calledMethodsAfter = new HashSet<>();
    				 for(JavaMethod cldmi : cgmdui.getCalledMethodsAfter())
    				 {
    					 calledMethodsAfter.add(cldmi.getSource());
    				 }
    				 str_cgmdui.setCalledMethodsAfter(calledMethodsAfter);
    			 }
    			 
    			 str_cgMethodUnits.add(str_cgmdui);
    			 
    		 }
    		 str_cgclsu.setCgMethodUnits(str_cgMethodUnits);
    	 }
     }
     
	 void buildChangedField(Str_ChangedClassUnit str_cgclsu, ChangedClassUnit cgclsu)
     {
    	 HashSet<Str_ChangedFieldUnit> str_cgFieldUnits =  new HashSet<>();
    	 HashSet<ChangedFieldUnit> cgFieldUnits = cgclsu.getCgFieldUnits();
    	 
    	 if(!cgFieldUnits.isEmpty())
    	 {
    		for(ChangedFieldUnit cgfldui : cgFieldUnits)
    		{
    			Str_ChangedFieldUnit str_cgfldui = new Str_ChangedFieldUnit();
    			str_cgfldui.setInstId(cgfldui.getInstId());
    			str_cgfldui.setName(cgfldui.getName());
    			str_cgfldui.setQualifiedName(cgfldui.getQualifiedName());
    			str_cgfldui.setType(cgfldui.getType());
    			str_cgfldui.setReturnType(cgfldui.getReturnType());
    			str_cgfldui.setSourceBefore(cgfldui.getSourceBefore());
    			str_cgfldui.setSourceAfter(cgfldui.getSourceAfter());
    			str_cgfldui.setTest(cgfldui.isTest());
    			
    			if(!cgfldui.getAccessMethodsBefore().isEmpty())
    			{
    				HashSet<String> accessMethodsBefore = new HashSet<>();
    				for(JavaMethod acmbi : cgfldui.getAccessMethodsBefore())
    				{
    					accessMethodsBefore.add(acmbi.getSource());
    				}
    				str_cgfldui.setAccessMethodsBefore(accessMethodsBefore);
    			}
    			
    			if(!cgfldui.getAccessMethodsAfter().isEmpty())
    			{
    				HashSet<String> accessMethodsAfter  = new HashSet<>();
    				for(JavaMethod acmai : cgfldui.getAccessMethodsAfter())
    				{
    					accessMethodsAfter.add(acmai.getSource());
    				}
    				str_cgfldui.setAccessMethodsAfter(accessMethodsAfter);
    			}
    			str_cgFieldUnits.add(str_cgfldui);
    		}
    		str_cgclsu.setCgFieldUnits(str_cgFieldUnits);
    	 }
    	   	 
     }
	 
	 void buildPRFilesChanged()
     {
		 if(this.pr.getFlcg()!=null)
		 {
    	   Str_FilesChanged str_flscg = new Str_FilesChanged();
    	   FilesChanged flscg = this.pr.getFlcg();
    	   this.buildPrFilesChanged(str_flscg, flscg);
    	   this.str_pr.str_prFilesChanged = str_flscg;
		 }else {
			 Str_FilesChanged str_flscg = new Str_FilesChanged();
		 }
     }
	 
	 void buildLabels()
     {
    	 HashSet<Str_PRLabel> str_finalLabels = new HashSet<>();
    	 if(!this.pr.getFinalLabels().isEmpty())
    	 {
    		 for(PRLabel lbi : this.pr.getFinalLabels())
    		 {
    			 Str_PRLabel str_lbi = new Str_PRLabel();
    			 str_lbi.setName(lbi.getName());
    			 str_lbi.setColor(lbi.getColor());
    			 str_lbi.setGhid(lbi.getGhid());
    			 str_lbi.setDescription(lbi.getDescription());
    			 str_lbi.setEventInstId(lbi.getEvent().getInstId());
    			 str_lbi.setInstId(lbi.getInstId());
    			 str_finalLabels.add(str_lbi);
    		 }
    		this.str_pr.str_finalLabels = str_finalLabels; 
    	 }
    	 
    	 HashSet<Str_PRLabel> str_addedLabels = new HashSet<>();
    	 if(!this.pr.getAddedLabels().isEmpty())
    	 {
    		 for(PRLabel lbi : this.pr.getAddedLabels())
    		 {
    			 Str_PRLabel str_lbi = new Str_PRLabel();
    			 str_lbi.setName(lbi.getName());
    			 str_lbi.setColor(lbi.getColor());
    			 str_lbi.setGhid(lbi.getGhid());
    			 str_lbi.setDescription(lbi.getDescription());
    			 str_lbi.setEventInstId(lbi.getEvent().getInstId());
    			 str_lbi.setInstId(lbi.getInstId());
    			 str_addedLabels.add(str_lbi);
    		 }
    		 this.str_pr.str_addedLabels = str_addedLabels;
    	 }
    	 
    	 HashSet<Str_PRLabel> str_removedLabels = new HashSet<>();
    	 if(!this.pr.getRemovedLabels().isEmpty())
    	 {
    		 for(PRLabel lbi : this.pr.getRemovedLabels())
    		 {
    			 Str_PRLabel str_lbi = new Str_PRLabel();
    			 str_lbi.setName(lbi.getName());
    			 str_lbi.setColor(lbi.getColor());
    			 str_lbi.setGhid(lbi.getGhid());
    			 str_lbi.setDescription(lbi.getDescription());
    			 str_lbi.setEventInstId(lbi.getEvent().getInstId());
    			 str_lbi.setInstId(lbi.getInstId());
    			 str_removedLabels.add(str_lbi);
    		 }
    		 this.str_pr.str_removedLabels = str_removedLabels;
    	 }
     }
	 
	 void buildCIStatus(CIStatus cisi , Str_CIStatus str_cisi)
	 {
		str_cisi.setContext(cisi.getContext());
		str_cisi.setState(cisi.getState());
		str_cisi.setDescription(cisi.getDescription());
		str_cisi.setTargetUrl(cisi.getTargetUrl());
		str_cisi.setCreateDate(cisi.getCreateDate());
		str_cisi.setUpdateDate(cisi.getUpdateDate());
	 }

	public Str_PullRequest getStr_pr() {
		return str_pr;
	}

	public void setStr_pr(Str_PullRequest str_pr) {
		this.str_pr = str_pr;
	}
	 
	 
	 

}
