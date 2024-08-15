package jprTool;

import prmodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jwrmodel.*;
import jxp3model.*;

public class JsonFileReader {
	
	PullRequest pr;
	Str_PullRequest str_pr;
	String filePath="";
	File aimFile;
	HashSet<File> aimFiles;
	HashSet<Str_PullRequest> str_prs;
	HashSet<PullRequest> prs;
	Gson gson;
	
	public JsonFileReader(String path)
	{
		filePath = path;		
		aimFiles = new HashSet<>();
		str_prs = new HashSet<>();
		prs = new HashSet<>();
		File fp = new File(path);
		aimFile = new File(this.filePath);
//		Gson gson = new GsonBuilder()
//	            .registerTypeAdapter(Date.class, new CustomDateDeserializer())
//	            .create();
		
		if(!fp.exists())
		{
			System.out.println("File in this path is not exists : "+this.aimFiles.size());
		}else if(fp.isFile() && this.getFileExtension(fp).equals("json"))
		{
			this.readFile();
		}else if(fp.isDirectory())
		{
			this.readFiles();
		}
		
	}
	
	public void readFile()
	{
		aimFile = new File(this.filePath);
		String content;
		try {
			
			content = new String(Files.readAllBytes(Paths.get(this.aimFile.getAbsolutePath())),"UTF-8");
			content = content.replace('\u00A0', ' ').replaceAll("\\p{Zs}", " ");
			this.gson = new Gson();
			this.str_pr = gson.fromJson(content, Str_PullRequest.class);
			this.loadPRModel();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void readFiles()
	{
		File filesDirectory = new File(this.filePath);
		this.aimFiles.addAll(this.listAllFiles(filesDirectory));
		String content;
		if(!this.aimFiles.isEmpty())
		{
			for(File fi : this.aimFiles)
			{
				if(this.getFileExtension(fi).equals("json"))
				{											
					try {
						
						content = new String(Files.readAllBytes(Paths.get(fi.getAbsolutePath())),"UTF-8");
						content = content.replace('\u00A0', ' ').replaceAll("\\p{Zs}", " ");
						this.gson = new Gson();
						Str_PullRequest str_pri = gson.fromJson(content, Str_PullRequest.class);
						this.str_prs.add(str_pri);
						
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						
						e.printStackTrace();
					}					
				}
			}
		}
		this.loadPRModels();
	}
	
	String getFileExtension(File f)
	{
		String name = f.getName();
		int dotIndex = name.lastIndexOf('.');
		if(dotIndex > 0 && dotIndex < name.length())
		{
			return name.substring(dotIndex+1);
		}
		return "";
	}
	
	public ArrayList<File> listAllFiles(File directory)
	{
		ArrayList<File> fileList = new ArrayList<>();
		if(directory.exists() && directory.isDirectory())
		{
			File[] files = directory.listFiles();
			if(files !=null)
			{
				for(File fi : files)
				{
					if(fi.isFile())
					{
						fileList.add(fi);
					}else if(fi.isDirectory())
					{
						fileList.addAll(listAllFiles(fi));
					}
				}
			}
		}
		return fileList;
	}
	
	PullRequest loadPRModel()
	{
		this.pr = new PullRequest();
		this.loadPRBase(this.pr, this.str_pr);
		this.loadDescription(this.pr, this.str_pr);
		this.loadParticipants(this.pr,this.str_pr);
		this.loadConversation(this.pr,this.str_pr);
		this.loadCommits(this.pr, this.str_pr);
		this.loadPRFilesChanged(this.pr, str_pr);
		this.loadLabel(this.pr, this.str_pr);
		return this.pr;
	}
	
	HashSet<PullRequest> loadPRModels()
	{
		if(!this.str_prs.isEmpty())
		{			
			for(Str_PullRequest str_pri : this.str_prs)
			{
				PullRequest pri = new PullRequest();
				this.loadPRBase(pri, str_pri);
				this.loadDescription(pri, str_pri);
				this.loadParticipants(pri, str_pri);
				this.loadConversation(pri, str_pri);
				this.loadCommits(pri, str_pri);
				this.loadPRFilesChanged(pri, str_pri);
				this.loadLabel(pri, str_pri);
				this.prs.add(pri);
			}
		}
		return this.prs;
	}
	
	void loadPRBase(PullRequest pr, Str_PullRequest str_pr)
	{
		pr.setTitle(str_pr.title);
		pr.setId(str_pr.id);
		pr.setState(str_pr.state);
		pr.setRepositoryName(str_pr.repositoryName);
		pr.setCreateDate(str_pr.createDate);
		pr.setEndDate(str_pr.endDate);
		pr.setMerged(str_pr.isMerged);
		pr.setStandardMerged(str_pr.isStandardMerged);
		pr.setSrcRetrievable(str_pr.srcRetrievable);
		pr.setMergeBranch(str_pr.mergeBranch);
		pr.setHeadBranch(str_pr.headBranch);
		pr.setPrPageUrl(str_pr.prPageUrl);
		pr.setRepositorySrcDLUrl(str_pr.repositorySrcDLUrl);
		pr.setHeadRepositorySrcDLUrl(str_pr.headRepositorySrcDLUrl);
		pr.setRepositoryBranches(str_pr.repositoryBranches);
		pr.setHeadRepositoryBranches(str_pr.headRepositoryBranches);
		pr.setHtmlDescription(str_pr.htmlDescription);
	}
	
	void loadDescription(PullRequest pr, Str_PullRequest str_pr)
	{
		Description dcsr = new Description(pr);
		dcsr.setBody(str_pr.str_description.getBody());
		dcsr.setInstId(str_pr.str_description.getInstId());
		MarkdownElement mkde = new MarkdownElement();
		this.loadMarkdownElement(mkde, str_pr.str_description.getSmde());
		dcsr.setMde(mkde);
		dcsr.setPr(pr);
		pr.setDcpt(dcsr);
	}
	
	void loadMarkdownElement(MarkdownElement mkde, Str_MarkdownElement str_mkde)
	{	
		mkde.setHeadingStrings(str_mkde.getHeadingStrings());
		mkde.setBoldStrings(str_mkde.getBoldStrings());
		mkde.setItalicStrings(str_mkde.getItalicStrings());
		mkde.setQuoteStrings(str_mkde.getQuoteStrings());
		mkde.setLinkStrings(str_mkde.getLinkStrings());
		mkde.setCodeStrings(str_mkde.getCodeStrings());
		mkde.setCodeBlockStrings(str_mkde.getCodeBlockStrings());
		mkde.setTextStrings(str_mkde.getTextStrings());
		mkde.setMentionStrings(str_mkde.getMentionStrings());
		mkde.setTextLinkStrings(str_mkde.getTextLinkStrings());
		mkde.setPullLinkStrings(str_mkde.getPullLinkStrings());
		mkde.setIssueLinkStrings(str_mkde.getIssueLinkStrings());
		mkde.setHtmlComments(str_mkde.getHtmlComments());
	}
	
	void loadParticipants(PullRequest pr, Str_PullRequest str_pr)
	{
		HashSet<Participant> participants = new HashSet<>();
		HashSet<Str_Participant> str_participants = str_pr.str_participants;
		
		if(str_participants!=null)
		{
		   for(Str_Participant str_prti : str_participants)
		  {
			Participant prti = new Participant(pr);
			prti.setInstId(str_prti.getInstId());
			prti.setLoginName(str_prti.getLoginName());
			prti.setName(str_prti.getName());
			prti.setId(str_prti.getId());
			prti.setRole(str_prti.getRole());
			prti.setLocation(str_prti.getLocation());
			prti.setRecord(str_prti.getRecord());
			prti.setFollowers(str_prti.getFollowers());
			prti.setFollowings(str_prti.getFollowings());
			prti.setPr(pr);
			participants.add(prti);
		  }
		   pr.setParticipants(participants);
		}
	}
	
	void loadConversation(PullRequest pr, Str_PullRequest str_pr)
	{
		Conversation conv = new Conversation(pr);
		pr.setConv(conv);
		conv.setInstId(str_pr.str_conversation.getInstId());
		this.loadComments(conv, pr, str_pr);
		this.loadReviewComments(conv,pr,str_pr);
		this.loadEvents(conv,pr,str_pr);
		this.loadReviewEvents(conv,pr,str_pr);
		this.loadCodeLineReview(conv,pr,str_pr);
		this.loadCodeLineReviewForOtherRCMs(conv);
		this.loadTimeLine(conv,pr,str_pr);
		
	}
	
	void loadComments(Conversation conv, PullRequest pr, Str_PullRequest str_pr)
	{
		LinkedHashSet<Comment> comments = new LinkedHashSet<>();
		LinkedHashSet<Str_Comment> str_comments = str_pr.str_conversation.getComments();
		
		if(str_comments!=null)
		{
			for(Str_Comment str_cmti : str_comments)
			{
				Comment cmti = new Comment(conv);
				cmti.setCommenter(this.findParticipant(str_cmti.getParticipantInstId(),pr));
				cmti.setCommentDate(str_cmti.getCommentDate());
				cmti.setBody(str_cmti.getBody());
				MarkdownElement mkde = new MarkdownElement();
				this.loadMarkdownElement(mkde, str_cmti.getStr_markdownElement());
				cmti.setMde(mkde);
				cmti.setInstId(str_cmti.getInstId());
				comments.add(cmti);
			}
			conv.setComments(comments);
		}
		
	}
	
	Participant findParticipant(String partInstId, PullRequest pr)
	{
		if(pr.getParticipants()!=null)
		{
			for(Participant parti : pr.getParticipants())
			{
				if(parti.getInstId().equals(partInstId))
				{
					return parti;
				}
			}
		}
		return null;
	}
	
	void loadReviewComments(Conversation conv, PullRequest pr, Str_PullRequest str_pr)
	{
		LinkedHashSet<ReviewComment> reviewComments = new LinkedHashSet<>();
		LinkedHashSet<Str_ReviewComment> str_reviewComments = str_pr.str_conversation.getReviewComments();
		
		if(str_reviewComments!=null)
		{
			for(Str_ReviewComment str_rcmti : str_reviewComments)
			{
				ReviewComment rcmti = new ReviewComment(conv);
				rcmti.setCommenter(this.findParticipant(str_rcmti.getParticipantInstId(),pr));
				rcmti.setCommentDate(str_rcmti.getCommentDate());
				rcmti.setBody(str_rcmti.getBody());
				MarkdownElement mkde = new MarkdownElement();
				this.loadMarkdownElement(mkde, str_rcmti.getStr_markdownElement());
				rcmti.setMde(mkde);
				rcmti.setInstId(str_rcmti.getInstId());
				reviewComments.add(rcmti);
			}
			conv.setReviewComments(reviewComments);
		}
	}
	
	void loadEvents(Conversation conv, PullRequest pr, Str_PullRequest str_pr)
	{
		LinkedHashSet<Event> events = new LinkedHashSet<>();
		LinkedHashSet<Str_Event> str_events = str_pr.str_conversation.getEvents();
		if(str_events!=null)
		{
			for(Str_Event str_evi : str_events)
			{
				Event evi = new Event(conv);
				evi.setActor(this.findParticipant(str_evi.getActorInstId(),pr));
				evi.setEventDate(str_evi.getCommentDate());
				evi.setBody(str_evi.getBody());
				evi.setInstId(str_evi.getInstId());
				events.add(evi);
			}
			conv.setEvents(events);
		}
	}
	
	void loadReviewEvents(Conversation conv, PullRequest pr, Str_PullRequest str_pr)
	{
		LinkedHashSet<ReviewEvent> reviewEvents = new LinkedHashSet<>();
		LinkedHashSet<Str_ReviewEvent> str_reviewEvents = str_pr.str_conversation.getReviewEvents();
		if(str_reviewEvents!=null)
		{
			for(Str_ReviewEvent str_revi : str_reviewEvents)
			{
				ReviewEvent revi = new ReviewEvent(conv);
				revi.setActor(this.findParticipant(str_revi.getActorInstId(),pr));
				revi.setEventDate(str_revi.getCommentDate());
				revi.setBody(str_revi.getBody());
				MarkdownElement mkde = new MarkdownElement();
				this.loadMarkdownElement(mkde, str_revi.getStr_markdownElement());
				revi.setMde(mkde);
				revi.setInstId(str_revi.getInstId());
				reviewEvents.add(revi);
			}
			conv.setReviewEvents(reviewEvents);
		}
	}
	
	void loadCodeLineReview(Conversation conv, PullRequest pr, Str_PullRequest str_pr)
	{
		LinkedHashSet<CodeReviewSnippet> clrs = new LinkedHashSet<>();
		LinkedHashSet<Str_CodeLineReview> str_clrs = str_pr.str_conversation.getCodeLineReviews();
		if(str_clrs!=null)
		{
			for(Str_CodeLineReview str_clri : str_clrs)
			{
				CodeReviewSnippet clri = new CodeReviewSnippet(conv);
				clri.setDiff(str_clri.getDiff());
				clri.setInstId(str_clri.getInstId());
				if(str_clri.getParticipantInstIds()!=null)
				{
					HashSet<Participant> partis = new HashSet<>();
					for(String partInstId : str_clri.getParticipantInstIds())
					{
						partis.add(this.findParticipant(partInstId,pr));
					}
					clri.setParticipants(partis);
				}
				
				if(str_clri.getReviewCommentInstIds()!=null)
				{
					ArrayList<ReviewComment> rcmts = new ArrayList<>();
					for(String rcmtInstId : str_clri.getReviewCommentInstIds())
					{
						rcmts.add(this.findReviewComment(rcmtInstId, pr));
					}
					clri.setReviewComments(rcmts);
				}
				clrs.add(clri);
			}
			conv.setCodeLineReviews(clrs);
		}
	}
	
	ReviewComment findReviewComment(String rcmtInstId, PullRequest pr)
	{
		if(pr.getConv().getReviewComments()!=null && ! pr.getConv().getReviewComments().isEmpty())
		{
			for(ReviewComment rcmti : pr.getConv().getReviewComments())
			{
				if(rcmti.getInstId().equals(rcmtInstId))
				{
					return rcmti;
				}
			}
		}
		return null;
	}
	
	void loadCodeLineReviewForOtherRCMs(Conversation conv)
	{
		LinkedHashSet<ReviewComment> rcms = conv.getReviewComments();
		LinkedHashSet<CodeReviewSnippet> clrs = conv.getCodeLineReviews();
		if(!clrs.isEmpty() && !rcms.isEmpty())
		{
			for(CodeReviewSnippet clri : clrs)
			{
				for(ReviewComment rcmi : rcms)
				{
					for(ReviewComment crcmi : clri.getReviewComments())
					{
						if(crcmi.getInstId().equals(rcmi.getInstId()))
						{
							rcmi.setClr(clri);
						}
					}
				}
			}
		}
	}
	
	void loadTimeLine(Conversation conv, PullRequest pr, Str_PullRequest str_pr)
	{
		LinkedHashSet<PRAction> acts = new LinkedHashSet<>();
		LinkedHashSet<Str_PRAction> str_acts = str_pr.str_conversation.getTimeLine();
		if(str_acts!=null)
		{
			for(Str_PRAction str_prai: str_acts)
			{
				PRAction prai = new PRAction(conv);
				prai.setBody(str_prai.getBody());
				prai.setExecutor(this.findParticipant(str_prai.getParticipantInstId(),pr));
				prai.setActionDate(str_prai.getActionDate());
				prai.setType(str_prai.getType());
				prai.setInstId(str_prai.getInstId());
				acts.add(prai);
			}
			conv.setTimeLine(acts);
		}
	}
	
	void loadCommits(PullRequest pr , Str_PullRequest str_pr)
	{
		ArrayList<Commit> commits = new ArrayList<>();
		if(str_pr.str_commits!=null)
		{
			for(Str_Commit str_cmiti : str_pr.str_commits)
			{
				Commit cmiti = new Commit(pr);
				cmiti.setSha(str_cmiti.getSha());
				cmiti.setShortSha(str_cmiti.getShortSha());
				cmiti.setAuthor(this.findParticipant(str_cmiti.getAuthorInstId(),pr));
				cmiti.setCommitDate(str_cmiti.getCommitDate());
				cmiti.setMessage(str_cmiti.getMessage());
				cmiti.setCommitType(str_cmiti.getCommitType());
				cmiti.setInstId(str_cmiti.getInstId());
				FilesChanged flcg = new FilesChanged(pr);
				Str_FilesChanged str_flcg = str_cmiti.getFilesChanged();
				this.loadFilesChanged(flcg, str_flcg,pr);
				flcg.setCmit(cmiti);
				cmiti.setFlcg(flcg);
				if( str_cmiti.getCis()!=null && !str_cmiti.getCis().isEmpty())
				{
					for(Str_CIStatus str_cisi : str_cmiti.getCis())
					{
						CIStatus cisi = new CIStatus(cmiti);
						this.loadCIStatus(cisi, str_cisi);
						cmiti.getCis().add(cisi);
					}
				}
				commits.add(cmiti);
			}
			pr.setCommits(commits);
		}
	}
	
	void loadCIStatus(CIStatus cisi, Str_CIStatus str_cisi)
	{
		cisi.setContext(str_cisi.getContext());
		cisi.setState(str_cisi.getState());
		cisi.setDescription(str_cisi.getDescription());
		cisi.setTargetUrl(str_cisi.getTargetUrl());
		cisi.setCreateDate(str_cisi.getCreateDate());
		cisi.setUpdateDate(str_cisi.getUpdateDate());
	}
	
	void loadFilesChanged(FilesChanged flcg, Str_FilesChanged str_flcg, PullRequest pr)
	{	
		if(str_flcg!=null)
		{
		flcg.setSrcBeforeDirName(str_flcg.getSrcBeforeDirName());
		flcg.setSrcAfterDirName(str_flcg.getSrcAfterDirName());
		flcg.setHasJavaSrcFile(str_flcg.isHasJavaSrcFile());
		flcg.setInstId(str_flcg.getInstId());	
		if(str_flcg.getStr_diffFileUnits()!=null)
		{
			ArrayList<DiffFileUnit> dfus = new ArrayList<>();
			for(Str_DiffFileUnit str_dfui : str_flcg.getStr_diffFileUnits())
			{
				DiffFileUnit dfui = new DiffFileUnit();
				dfui.setAbsolutePathBefore(str_dfui.getAbsolutePathBefore());
				dfui.setAbsolutePathAfter(str_dfui.getAbsolutePathAfter());
				dfui.setRelativePath(str_dfui.getRelativePath());
				dfui.setDiffBodyAll(str_dfui.getDiffBodyAll());
				dfui.setDiffBodyAdd(str_dfui.getDiffBodyAdd());
				dfui.setDiffBodyDelete(str_dfui.getDiffBodyDelete());
				dfui.setSourceBefore(str_dfui.getSourceBefore());
				dfui.setSourceAfter(str_dfui.getSourceAfter());
				dfui.setFileType(str_dfui.getFileType());
				dfui.setJavaSrcFile(str_dfui.isJavaSrcFile());
				dfui.setTest(str_dfui.isTest());
				if(str_dfui.getDiffLineUnits()!=null)
				{
					ArrayList<DiffLineUnit> dlus = new ArrayList<>();
					for(Str_DiffLineUnit str_dlui : str_dfui.getDiffLineUnits())
					{
						DiffLineUnit dlui = new DiffLineUnit();
						dlui.setText(str_dlui.getText());
						dlui.setType(str_dlui.getType());
						dlus.add(dlui);
					}
					dfui.setDiffLineUnits(dlus);
				}
				dfus.add(dfui);
			}
			flcg.setDiffFileUnits(dfus);
		}
		
		CodeElement cde = new CodeElement(flcg,pr);
		this.loadCodeElement(cde, str_flcg.getStr_codeElement());
		flcg.setCde(cde);
		}
	}
	
	void loadCodeElement(CodeElement cde , Str_CodeElement str_cde)
	{
		if(str_cde!=null)
		{
		cde.setInstId(str_cde.getInstId());
		cde.setPjNameIndex(str_cde.getPjNameIndex());
		if(str_cde.getCgProjectUnits()!=null)
		{
			for(Str_ChangedProjectUnit str_cgpjui : str_cde.getCgProjectUnits())
			{
				ChangedProjectUnit cgpjui = new ChangedProjectUnit(cde);
				this.loadChangedProjectUnit(cgpjui, str_cgpjui);
				cde.getCgProjectUnits().add(cgpjui);
			}
		}
		}
	}
	
	void loadChangedProjectUnit(ChangedProjectUnit cgpjui , Str_ChangedProjectUnit str_cgpjui)
	{
		cgpjui.setInstId(str_cgpjui.getInstId());
		cgpjui.setInputPathBefore(str_cgpjui.getInputPathBefore());
		cgpjui.setInputNameBefore(str_cgpjui.getInputNameBefore());
		cgpjui.setInputPathAfter(str_cgpjui.getInputPathAfter());
		cgpjui.setInputNameAfter(str_cgpjui.getInputNameAfter());
		cgpjui.setType(str_cgpjui.getType());
		cgpjui.setName(str_cgpjui.getName());
		if(str_cgpjui.getCgFileUnits()!=null)
		{
			for(Str_ChangedFileUnit str_cgflui : str_cgpjui.getCgFileUnits())
			{
				ChangedFileUnit cgflui = new ChangedFileUnit();
				this.loadChangedFileUnit(cgflui, str_cgflui);
				cgpjui.getCgFileUnits().add(cgflui);
			}
		}
	}
	
	void loadChangedFileUnit(ChangedFileUnit cgflui , Str_ChangedFileUnit str_cgflui)
	{
		cgflui.setInstId(str_cgflui.getInstId());
		cgflui.setName(str_cgflui.getName());
		cgflui.setType(str_cgflui.getType());
		cgflui.setSourceBefore(str_cgflui.getSourceBefore());
		cgflui.setSourceAfter(str_cgflui.getSourceAfter());
		cgflui.setPathBefore(str_cgflui.getPathBefore());
		cgflui.setPathAfter(str_cgflui.getPathAfter());
		cgflui.setTest(str_cgflui.isTest());
		if(str_cgflui.getCgClassUnits()!=null)
		{
			for(Str_ChangedClassUnit str_clsui : str_cgflui.getCgClassUnits())
			{
				ChangedClassUnit cgclsui = new ChangedClassUnit();
				this.loadChangedClassUnit(cgclsui, str_clsui);
				cgflui.getCgClassUnits().add(cgclsui);
			}
		}
	}
	
	void loadChangedClassUnit(ChangedClassUnit cgclsui, Str_ChangedClassUnit str_cgclsui)
	{
		cgclsui.setInstId(str_cgclsui.getInstId());
		cgclsui.setName(str_cgclsui.getName());
		cgclsui.setQualifiedName(str_cgclsui.getQualifiedName());
		cgclsui.setType(str_cgclsui.getType());
		cgclsui.setSourceBefore(str_cgclsui.getSourceBefore());
		cgclsui.setSourceAfter(str_cgclsui.getSourceAfter());
		cgclsui.setTest(str_cgclsui.isTest());
		if(str_cgclsui.getCgMethodUnits()!=null)
		{
			for(Str_ChangedMethodUnit str_cgmtui : str_cgclsui.getCgMethodUnits())
			{
				ChangedMethodUnit cgmtui = new ChangedMethodUnit();
				this.loadChangedMethodUnit(cgmtui, str_cgmtui);
				cgclsui.getCgMethodUnits().add(cgmtui);
			}
		}
		
		if(str_cgclsui.getCgFieldUnits()!=null)
		{
			for(Str_ChangedFieldUnit str_cgfdui : str_cgclsui.getCgFieldUnits())
			{
				ChangedFieldUnit cgfdui = new ChangedFieldUnit();
				this.loadChangedFieldUnit(cgfdui, str_cgfdui);
				cgclsui.getCgFieldUnits().add(cgfdui);
			}
		}
	}
	
	void loadChangedMethodUnit(ChangedMethodUnit cgmtui, Str_ChangedMethodUnit str_cgmtui)
	{
		cgmtui.setInstId(str_cgmtui.getInstId());
		cgmtui.setName(str_cgmtui.getName());
		cgmtui.setQualifiedName(str_cgmtui.getQualifiedName());
		cgmtui.setType(str_cgmtui.getType());
		cgmtui.setReturnType(str_cgmtui.getReturnType());
		cgmtui.setSignatureBefore(str_cgmtui.getSignatureBefore());
		cgmtui.setSourceBefore(str_cgmtui.getSourceBefore());
		cgmtui.setSignatureAfter(str_cgmtui.getSignatureAfter());
		cgmtui.setTest(str_cgmtui.isTest());
		if(str_cgmtui.getCallingMethodsBefore()!=null)
		{
			for(String clmb : str_cgmtui.getCallingMethodsBefore())
			{
				cgmtui.getCallingMethodSourcesBefore().add(clmb);
			}
		}
		if(str_cgmtui.getCallingMethodsAfter()!=null)
		{
			for(String clma : str_cgmtui.getCallingMethodsAfter())
			{
				cgmtui.getCallingMethodSourcesAfter().add(clma);
			}
		}
		if(str_cgmtui.getCalledMethodsBefore()!=null)
		{
			for(String cldb : str_cgmtui.getCalledMethodsBefore())
			{
				cgmtui.getCalledMethodSourcesBefore().add(cldb);
			}
		}
		if(str_cgmtui.getCalledMethodsAfter()!=null)
		{
			for(String clda : str_cgmtui.getCalledMethodsAfter())
			{
				cgmtui.getCalledMethodSourcesAfter().add(clda);
			}
		}
	}
	
	void loadChangedFieldUnit(ChangedFieldUnit cgfdui, Str_ChangedFieldUnit str_cgfdui)
	{
		cgfdui.setInstId(str_cgfdui.getInstId());
		cgfdui.setName(str_cgfdui.getName());
		cgfdui.setQualifiedName(str_cgfdui.getQualifiedName());
		cgfdui.setType(str_cgfdui.getType());
		cgfdui.setReturnType(str_cgfdui.getReturnType());
		cgfdui.setSourceBefore(str_cgfdui.getSourceBefore());
		cgfdui.setSourceAfter(str_cgfdui.getSourceAfter());
		cgfdui.setTest(str_cgfdui.isTest());
		if(str_cgfdui.getAccessMethodsBefore()!=null)
		{
			for(String acmb : str_cgfdui.getAccessMethodsBefore())
			{
				cgfdui.getAccessMethodSourcesBefore().add(acmb);
			}
		}
		if(str_cgfdui.getAccessMethodsAfter()!=null)
		{
			for(String acma : str_cgfdui.getAccessMethodsAfter())
			{
				cgfdui.getAccessMethodSourcesAfter().add(acma);
			}
		}
	}
	
	void loadPRFilesChanged(PullRequest pr , Str_PullRequest str_pr)
	{
		FilesChanged flcg = new FilesChanged(pr);
		Str_FilesChanged str_flcg = str_pr.str_prFilesChanged;
		this.loadFilesChanged(flcg, str_flcg, pr);
		pr.setFlcg(flcg);
		
	}
	
	void loadLabel(PullRequest pr, Str_PullRequest str_pr)
	{
		if(str_pr.str_finalLabels!=null)
		{
			for(Str_PRLabel str_lbi : str_pr.str_finalLabels)
			{
				PRLabel lbi = new PRLabel(pr);
				lbi.setName(str_lbi.getName());
				lbi.setColor(str_lbi.getColor());
				lbi.setGhid(str_lbi.getGhid());
				lbi.setDescription(str_lbi.getDescription());
				lbi.setInstId(str_lbi.getInstId());
				lbi.setEvent(this.findEvent(str_lbi.getEventInstId(),pr));
				pr.getFinalLabels().add(lbi);
			}
		}
		if(str_pr.str_addedLabels!=null)
		{
			for(Str_PRLabel str_lbi : str_pr.str_addedLabels)
			{
				PRLabel lbi = new PRLabel(pr);
				lbi.setName(str_lbi.getName());
				lbi.setColor(str_lbi.getColor());
				lbi.setGhid(str_lbi.getGhid());
				lbi.setDescription(str_lbi.getDescription());
				lbi.setInstId(str_lbi.getInstId());
				lbi.setEvent(this.findEvent(str_lbi.getEventInstId(),pr));
				pr.getAddedLabels().add(lbi);
			}
		}
		if(str_pr.str_removedLabels!=null)
		{
			for(Str_PRLabel str_lbi : str_pr.str_removedLabels)
			{
				PRLabel lbi = new PRLabel(pr);
				lbi.setName(str_lbi.getName());
				lbi.setColor(str_lbi.getColor());
				lbi.setGhid(str_lbi.getGhid());
				lbi.setDescription(str_lbi.getDescription());
				lbi.setInstId(str_lbi.getInstId());
				lbi.setEvent(this.findEvent(str_lbi.getEventInstId(),pr));
				pr.getRemovedLabels().add(lbi);
			}
		}
	}
	
	Event findEvent(String instId, PullRequest pr)
	{
		if(pr.getConv().getEvents()!=null && !pr.getConv().getEvents().isEmpty())
		{
			for(Event evi : pr.getConv().getEvents())
			{			
				if(evi.getInstId().equals(instId))
				{
					
					return evi;
				}
			}
		}
		return null;	
		
	}
	
	public PullRequest getPR()
	{
		return this.pr;
	}
	
	public HashSet<PullRequest> getPRs()
	{
		return this.prs;
	}
	
	

}
