/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.jwrmodel;

import java.io.File;
import java.io.IOException;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.LinkedHashSet;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.Participant;
import org.jtool.prmodel.Conversation;
import org.jtool.prmodel.DeficientPullRequest;
import org.jtool.prmodel.IssueComment;
import org.jtool.prmodel.ReviewComment;
import org.jtool.prmodel.IssueEvent;
import org.jtool.prmodel.MarkdownDoc;
import org.jtool.prmodel.ReviewEvent;
import org.jtool.prmodel.CodeReviewSnippet;
import org.jtool.prmodel.Action;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.DiffFile;
import org.jtool.prmodel.DiffLine;
import org.jtool.prmodel.CIStatus;
import org.jtool.prmodel.CodeChange;
import org.jtool.prmodel.Description;
import org.jtool.prmodel.HTMLDescription;
import org.jtool.prmodel.ChangeSummary;
import org.jtool.prmodel.Label;
import org.jtool.prmodel.PRModelDate;
import org.jtool.prmodel.PRModelLoader;
import org.jtool.jxp3model.CodeElement;
import org.jtool.jxp3model.ProjectChange;
import org.jtool.jxp3model.FileChange;
import org.jtool.jxp3model.ClassChange;
import org.jtool.jxp3model.FieldChange;
import org.jtool.jxp3model.MethodChange;

public class JsonFileReader {
    
    private final PRModelLoader loader;
    private final String filePath;
    
    private Set<PullRequest> pullRequests = new HashSet<>();
    private Set<DeficientPullRequest> deficientPullRequests = new HashSet<>();
    
    private Map<String, Participant> participantMap = new HashMap<>();
    
    private Map<String, IssueComment> commentMap = new HashMap<>();
    private Map<String, ReviewComment> reviewCommentMap = new HashMap<>();
    private Map<String, IssueEvent> eventMap = new HashMap<>();
    private Map<String, ReviewEvent> reviewMap = new HashMap<>();
    
    private Map<String, DiffFile> diffFileMap = new HashMap<>();
    private Map<String, FileChange> fileChangeMap = new HashMap<>();
    
    private Map<ClassChange, Str_ClassChange> classMap = new HashMap<>();
    private Map<MethodChange, Str_MethodChange> methodMap = new HashMap<>();
    private Map<FieldChange, Str_FieldChange> fieldMap = new HashMap<>();
    
    private Map<String, CodeElement> classElementBeforeMap = new HashMap<>();
    private Map<String, CodeElement> classElementAfterMap = new HashMap<>();
    private Map<String, CodeElement> methodElementBeforeMap = new HashMap<>();
    private Map<String, CodeElement> methodElementAfterMap = new HashMap<>();
    
    public JsonFileReader(PRModelLoader loader, String filePath) {
        this.loader = loader;
        this.filePath = filePath;
    }
    
    public Set<PullRequest> getPullRequests() {
        return pullRequests;
    }
    
    public Set<DeficientPullRequest> getDeficientPullRequests() {
    	return deficientPullRequests;
    }
    
    public void read() {
        List<File> files = getJSONFiles();
        readFiles(files, true);
    }
    
    public void read(int num) {
        List<File> files = getJSONFiles();
        List<File> targetFiles = files.subList(0, num);
        readFiles(targetFiles, true);
    }
    
    public void read(List<File> Files) {
    	readFiles(Files, true);
    }
    
    public void read(File file) {
    	List<File> targetFiles = new ArrayList<>();
    	targetFiles.add(file);
    	readFiles(targetFiles, true);
    }
    
    public void hookedRead() {
        List<File> files = getJSONFiles();
        readFiles(files, false);
    }
    
    public List<File> getJSONFiles() {
        List<File> files = new ArrayList<>();
        
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File or directory does not exist : " + filePath);
        } else {
            
            if (file.isFile()) {
                if (isPRJsonFromPRCollector(file) || isDataLossJsonFromPRCollector(file)) {
                    files.add(file);
                }
            } else if (file.isDirectory()) {
                File directory = new File(filePath);
                files.addAll(listAllFiles(directory));
            }
        }
        return getSortedFileList(files);
    }
    
    private boolean isPRJsonFromPRCollector(File file) {
    	String pattern ="^[\\p{L}-]+_[\\p{L}-]+#\\d+_str\\.json$";
    	return Pattern.matches(pattern, file.getName());
    }
    
    private boolean isDataLossJsonFromPRCollector(File file) {
        String pattern ="^[\\p{L}-]+_[\\p{L}-]+#\\d+_loss\\.json$";
        return Pattern.matches(pattern, file.getName());
    }
    
    @SuppressWarnings("unused")
    private String getFileExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < name.length()) {
            return name.substring(dotIndex + 1);
        }
        return "";
    }
    
    private List<File> listAllFiles(File directory) {
        List<File> fileList = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (isPRJsonFromPRCollector(file)|| isDataLossJsonFromPRCollector(file)) {
                        fileList.add(file);
                    }
                } else if (file.isDirectory()) {
                    fileList.addAll(listAllFiles(file));
                }
            }
        }
        return fileList;
    }
    
    private List<File> getSortedFileList(List<File> files) {
        List<File> sortedList = files.stream()
            .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
            .collect(Collectors.toList());
            return sortedList;
    }
    
    private void readFiles(List<File> files, boolean saveToMemory) {
        for (File file : files) {
            try {
            	InputStream is = new FileInputStream(file);
            	InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            	JsonReader reader = new JsonReader(isr);

                Gson gson = new Gson();
                if (isPRJsonFromPRCollector(file)) {
                    Str_PullRequest str_pr = gson.fromJson(reader, Str_PullRequest.class);
                    PullRequest pullRequest = loadPRModel(str_pr);
                    System.out.println("Loaded PR model for " + file.getPath().toString());
                    
                    if (saveToMemory) {
                        pullRequests.add(pullRequest);
                    } else {
                        loader.actionPerformed(pullRequest);
                        pullRequest = null;
                    }
                } else if (isDataLossJsonFromPRCollector(file)) {
                    Str_DeficientPullRequest str_pr = gson.fromJson(reader, Str_DeficientPullRequest.class);
                    DeficientPullRequest pullRequest = loadPRModelWithDataLoss(str_pr);
                    System.out.println("Loaded deficient PR for " + file.getPath().toString());
                    
                    if (saveToMemory) {
                        deficientPullRequests.add(pullRequest);
                    } else {
                        loader.actionPerformed(pullRequest);
                        pullRequest = null;
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            } catch (OutOfMemoryError e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    private PullRequest loadPRModel(Str_PullRequest str_pr) {
        PRModelDate createDate = new PRModelDate(str_pr.createDate);
        PRModelDate endDate = new PRModelDate(str_pr.endDate);
        
        PullRequest pullRequest = new PullRequest(
                str_pr.id, str_pr.title, str_pr.repositoryName, str_pr.state,
                createDate, endDate,
                str_pr.mergeBranch, str_pr.headBranch, str_pr.pageUrl,
                str_pr.repositorySrcDLUrl, str_pr.headRepositorySrcDLUrl,
                str_pr.isMerged, str_pr.isStandardMerged,
                str_pr.repositoryBranches, str_pr.headRepositoryBranches);
        pullRequest.setPrmodelId(str_pr.prmodelId);
        
        pullRequest.setParticipantRetrievable(str_pr.participantRetrievable);
        pullRequest.setIssueEventRetrievable(str_pr.issueEventRetrievable);
        pullRequest.setIssueCommentRetrievable(str_pr.issueCommentRetrievable);
        pullRequest.setReviewEventRetrievable(str_pr.reviewEventRetrievable);
        pullRequest.setReviewCommentRetrievable(str_pr.reviewCommentRetrievable);
        pullRequest.setCommitRetrievable(str_pr.commitRetrievable);
        pullRequest.setSourceCodeRetrievable(str_pr.sourceCodeRetrievable);
        
        Set<Participant> participants = loadParticipants(pullRequest, str_pr.participants);
        pullRequest.getParticipants().addAll(participants);
        
        Description description = loadDescription(pullRequest, str_pr.description);
        pullRequest.setDescription(description);
        
        HTMLDescription htmlDescription = loadHTMLDescription(pullRequest, str_pr.htmlDescription);
        pullRequest.setHTMLDescription(htmlDescription);
        
        Conversation conversation = loadConversation(pullRequest, str_pr.conversation);
        pullRequest.setConversation(conversation);
        
        List<Commit> commits = loadCommits(pullRequest, str_pr.commits);
        pullRequest.getCommits().addAll(commits);
        
        ChangeSummary changeSummary = loadChangeSummary(pullRequest, str_pr.changeSummary);
        pullRequest.setChangeSummary(changeSummary);
        
        Set<Label> addedLabels = loadLabels(pullRequest, str_pr.addedLabels);
        pullRequest.getAddedLabels().addAll(addedLabels);
        Set<Label> removedLabels = loadLabels(pullRequest, str_pr.removedLabels);
        pullRequest.getRemovedLabels().addAll(removedLabels);
        Set<Label> finalLabels = loadLabels(pullRequest, str_pr.finalLabels);
        pullRequest.getFinalLabels().addAll(finalLabels);
        
        return pullRequest;
    }
    
    private DeficientPullRequest loadPRModelWithDataLoss(Str_DeficientPullRequest str_pr) {
        PRModelDate createDate = new PRModelDate(str_pr.createDate);
        PRModelDate endDate = new PRModelDate(str_pr.endDate);
        
        DeficientPullRequest pullRequest = new DeficientPullRequest(
                str_pr.id, str_pr.title, str_pr.repositoryName, str_pr.state,
                createDate, endDate,
                str_pr.mergeBranch, str_pr.headBranch, str_pr.pageUrl,
                str_pr.repositorySrcDLUrl, str_pr.headRepositorySrcDLUrl,
                str_pr.isMerged, str_pr.isStandardMerged,
                str_pr.repositoryBranches, str_pr.headRepositoryBranches,
                str_pr.exceptionOutput);
        pullRequest.setPrmodelId(str_pr.prmodelId);
        
        pullRequest.setParticipantRetrievable(str_pr.participantRetrievable);
        pullRequest.setIssueEventRetrievable(str_pr.issueEventRetrievable);
        pullRequest.setIssueCommentRetrievable(str_pr.issueCommentRetrievable);
        pullRequest.setReviewEventRetrievable(str_pr.reviewEventRetrievable);
        pullRequest.setReviewCommentRetrievable(str_pr.reviewCommentRetrievable);
        pullRequest.setCommitRetrievable(str_pr.commitRetrievable);
        pullRequest.setSourceCodeRetrievable(str_pr.sourceCodeRetrievable);
        
        Set<Participant> participants = loadParticipants(pullRequest, str_pr.participants);
        pullRequest.getParticipants().addAll(participants);
        
        Description description = loadDescription(pullRequest, str_pr.description);
        pullRequest.setDescription(description);
        
        HTMLDescription htmlDescription = loadHTMLDescription(pullRequest, str_pr.htmlDescription);
        pullRequest.setHTMLDescription(htmlDescription);
        
        Conversation conversation = loadConversation(pullRequest, str_pr.conversation);
        pullRequest.setConversation(conversation);
        
        List<Commit> commits = loadCommits(pullRequest, str_pr.commits);
        pullRequest.getCommits().addAll(commits);
        
        ChangeSummary changeSummary = loadChangeSummary(pullRequest, str_pr.changeSummary);
        pullRequest.setChangeSummary(changeSummary);
        
        Set<Label> addedLabels = loadLabels(pullRequest, str_pr.addedLabels);
        pullRequest.getAddedLabels().addAll(addedLabels);
        Set<Label> removedLabels = loadLabels(pullRequest, str_pr.removedLabels);
        pullRequest.getRemovedLabels().addAll(removedLabels);
        Set<Label> finalLabels = loadLabels(pullRequest, str_pr.finalLabels);
        pullRequest.getFinalLabels().addAll(finalLabels);
        
        return pullRequest;
    }
    
    private Set<Participant> loadParticipants(PullRequest pullRequest, Set<Str_Participant> str_pts) {
        Set<Participant> participants = new HashSet<>();
        for (Str_Participant str_pt : str_pts) {
            
            Participant participant = new Participant(pullRequest, str_pt.userId,
                    str_pt.login, str_pt.name, str_pt.location, str_pt.role,
                    str_pt.followers, str_pt.follows);
            participant.setPrmodelId(str_pt.prmodelId);
            
            participant.getActionRecord().addAll(str_pt.actionRecord);
            
            participants.add(participant);
            
            participantMap.put(participant.getPRModelId(), participant);
        }
        return participants;
    }
    
    private MarkdownDoc loadMarkdownElements(PullRequest pullRequest, Str_MarkdownDoc str_md) {
        MarkdownDoc doc = new MarkdownDoc(pullRequest);
        doc.setPrmodelId(str_md.prmodelId);
        
        doc.getHeadingStrings().addAll(str_md.headingStrings);
        doc.getBoldStrings().addAll(str_md.boldStrings);
        
        doc.getItalicStrings().addAll(str_md.italicStrings);
        doc.getQuoteStrings().addAll(str_md.quoteStrings);
        doc.getLinkStrings().addAll(str_md.linkStrings);
        doc.getCodeStrings().addAll(str_md.codeStrings);
        doc.getCodeBlockStrings().addAll(str_md.codeBlockStrings);
        doc.getMentionStrings().addAll(str_md.mentionStrings);
        doc.getTextStrings().addAll(str_md.textStrings);
        
        doc.getPullLinkStrings().addAll(str_md.pullLinkStrings);
        doc.getIssueLinkStrings().addAll(str_md.issueLinkStrings);
        
        doc.getHtmlComments().addAll(str_md.htmlComments);
        
        return doc;
    }
    
    private Description loadDescription(PullRequest pullRequest, Str_Description str_dp) {
        Description description = new Description(pullRequest, str_dp.body);
        description.setPrmodelId(str_dp.prmodelId);
        
        MarkdownDoc doc = loadMarkdownElements(pullRequest, str_dp.markdownDoc);
        description.setMardownDoc(doc);
        return description;
    }
    
    private HTMLDescription loadHTMLDescription(PullRequest pullRequest, Str_HTMLDescription str_dp) {
        HTMLDescription htmlDescription = new HTMLDescription(pullRequest, str_dp.body);
        htmlDescription.setPrmodelId(str_dp.prmodelId);
        
        if (str_dp.mentionUsers != null) {
            htmlDescription.getMentionUsers().addAll(str_dp.mentionUsers);
        }
        return htmlDescription;
    }
    
    private Conversation loadConversation(PullRequest pullRequest, Str_Conversation str_cv) {
        Conversation conversation = new Conversation(pullRequest);
        conversation.setPrmodelId(str_cv.prmodelId);
        
        conversation.getIssueEvents().addAll(loadEvents(pullRequest, conversation, str_cv.issueEvents));
        conversation.getIssueComments().addAll(loadComments(pullRequest, conversation, str_cv.issueComments));
        conversation.getReviewEvents().addAll(loadReviews(pullRequest, conversation, str_cv.reviewEvents));
        conversation.getReviewComments().addAll(loadReviewComments(pullRequest, conversation, str_cv.reviewComments));
        conversation.getCodeReviews().addAll(loadCodeReviewSnippets(pullRequest, conversation, str_cv.codeReviews));
        
        conversation.getTimeLine().addAll(loadTimeLine(pullRequest, conversation, str_cv.timeLineIds));
        return conversation;
    }
    
    private LinkedHashSet<IssueComment> loadComments(PullRequest pullRequest, Conversation conversation,
            Set<Str_IssueComment> str_cts) {
        LinkedHashSet<IssueComment> comments = new LinkedHashSet<>();
        for (Str_IssueComment str_ct : str_cts) {
            PRModelDate date = new PRModelDate(str_ct.date);
            
            IssueComment comment = new IssueComment(pullRequest, date, str_ct.body);
            comment.setPrmodelId(str_ct.prmodelId);
            
            comment.setConversation(conversation);
            
            MarkdownDoc doc = loadMarkdownElements(pullRequest, str_ct.markdownDoc);
            comment.setMarkdownDoc(doc);
            
            Participant participant = participantMap.get(str_ct.participantId);
            comment.setParticipant(participant);
            
            comments.add(comment);
            
            commentMap.put(comment.getPRModelId(), comment);
        }
        return comments;
    }
    
    private LinkedHashSet<ReviewComment> loadReviewComments(PullRequest pullRequest, Conversation conversation,
            Set<Str_ReviewComment> str_cts) {
        LinkedHashSet<ReviewComment> reviewComments = new LinkedHashSet<>();
        for (Str_ReviewComment str_ct : str_cts) {
            PRModelDate date = new PRModelDate(str_ct.date);
            
            ReviewComment comment = new ReviewComment(pullRequest, date, str_ct.body);
            comment.setPrmodelId(str_ct.prmodelId);
            
            comment.setConversation(conversation);
            
            MarkdownDoc doc = loadMarkdownElements(pullRequest, str_ct.markdownDoc);
            comment.setMarkdownDoc(doc);
            
            Participant participant = participantMap.get(str_ct.participantId);
            comment.setParticipant(participant);
            
            // Later assignment of codeReviewSnippet attribute
            
            reviewComments.add(comment);
            
            reviewCommentMap.put(comment.getPRModelId(), comment);
        }
        return reviewComments;
    }
    
    private LinkedHashSet<IssueEvent> loadEvents(PullRequest pullRequest, Conversation conversation,
            Set<Str_IssueEvent> str_ets) {
        LinkedHashSet<IssueEvent> events = new LinkedHashSet<>();
        for (Str_IssueEvent str_et : str_ets) {
            PRModelDate date = new PRModelDate(str_et.date);
            
            IssueEvent event = new IssueEvent(pullRequest, date, str_et.body);
            event.setPrmodelId(str_et.prmodelId);
            
            event.setConversation(conversation);
            
            Participant participant = participantMap.get(str_et.participantId);
            event.setParticipant(participant);
            
            events.add(event);
            
            eventMap.put(event.getPRModelId(), event);
        }
        return events;
    }
    
    private LinkedHashSet<ReviewEvent> loadReviews(PullRequest pullRequest, Conversation conversation,
            Set<Str_ReviewEvent> str_rvs) {
        LinkedHashSet<ReviewEvent> reviews = new LinkedHashSet<>();
        for (Str_ReviewEvent str_rv : str_rvs) {
            PRModelDate date = new PRModelDate(str_rv.date);
            
            ReviewEvent review = new ReviewEvent(pullRequest, date, str_rv.body);
            review.setPrmodelId(str_rv.prmodelId);
            
            review.setConversation(conversation);
            
            MarkdownDoc doc = loadMarkdownElements(pullRequest, str_rv.markdownDoc);
            review.setMarkdownDoc(doc);
            
            Participant participant = participantMap.get(str_rv.participantId);
            review.setParticipant(participant);
            
            reviews.add(review);
            
            reviewMap.put(review.getPRModelId(), review);
        }
        return reviews;
    }
    
    private LinkedHashSet<CodeReviewSnippet> loadCodeReviewSnippets(PullRequest pullRequest, Conversation conversation,
            Set<Str_CodeReviewSnippet> str_rvs) {
        LinkedHashSet<CodeReviewSnippet> codeReviewSnippets = new LinkedHashSet<>();
        for (Str_CodeReviewSnippet str_sp : str_rvs) {
            
            CodeReviewSnippet codeReviewSnippet = new CodeReviewSnippet(pullRequest, str_sp.diffHunk);
            codeReviewSnippet.setPrmodelId(str_sp.prmodelId);
            
            codeReviewSnippet.setConversation(conversation);
            
            for (String sid : str_sp.reviewCommentIds) {
                ReviewComment reviewComment = reviewCommentMap.get(sid);
                codeReviewSnippet.getReviewComments().add(reviewComment);
                if (reviewComment != null) {
                    reviewComment.setCodeReviewSnippet(codeReviewSnippet);
                }
            }
            
            codeReviewSnippets.add(codeReviewSnippet);
        }
        return codeReviewSnippets;
    }
    
    private LinkedHashSet<Action> loadTimeLine(PullRequest pullRequest, Conversation conversation,
            Set<String> aids) {
        Map<String, Action> actionMap = new HashMap<>();
        actionMap.putAll(commentMap);
        actionMap.putAll(reviewCommentMap);
        actionMap.putAll(eventMap);
        actionMap.putAll(reviewMap);
        
        LinkedHashSet<Action> actions = new LinkedHashSet<>();
        for (String aid : aids) {
            Action action = actionMap.get(aid);
            actions.add(action);
        }
        return actions;
    }
    
    private List<Commit> loadCommits(PullRequest pullRequest, List<Str_Commit> str_cts) {
        List<Commit> commits = new ArrayList<>();
        for (Str_Commit str_ct : str_cts) {
            PRModelDate date = new PRModelDate(str_ct.date);
            
            Commit commit = new Commit(pullRequest, str_ct.sha, str_ct.shortSha, date, str_ct.type, str_ct.message);
            commit.setPrmodelId(str_ct.prmodelId);
            
            Participant committer = participantMap.get(str_ct.committerId);
            commit.setCommiter(committer);
            
            commit.setCodeChange(loadCodeChange(pullRequest, commit, str_ct.codeChange));
            commit.getCIStatus().addAll(loadCIStatus(pullRequest, str_ct.ciStatus));
            
            commits.add(commit);
        }
        return commits;
    }
    
    private CodeChange loadCodeChange(PullRequest pullRequest, Commit commit, Str_CodeChange str_ch) {
        CodeChange codeChange = new CodeChange( pullRequest);
        codeChange.setPrmodelId(str_ch.prmodelId);
        
        codeChange.setCommit(commit);
        codeChange.hasJavaFile(str_ch.hasJavaFile);
        
        codeChange.getDiffFiles().addAll(loadDiffFiles(pullRequest, str_ch.diffFiles));
        
        codeChange.getProjectChanges().addAll(loadProjectChange(pullRequest, codeChange, str_ch.projectChanges));
        setReferenceRelation(pullRequest, codeChange);
        codeChange.setFileChanges();
        
        return codeChange;
    }
    
    private List<DiffFile> loadDiffFiles(PullRequest pullRequest, List<Str_DiffFile> str_dfs) {
        List<DiffFile> diffFiles = new ArrayList<>();
        if (str_dfs != null) {
            for (Str_DiffFile str_df : str_dfs) {
                DiffFile diffFile = new DiffFile(pullRequest, str_df.changeType, str_df.path, str_df.bodyAll,
                        str_df.bodyAdd, str_df.bodyDel, str_df.sourceCodeBefore, str_df.sourceCodeAfter,
                        str_df.isJavaFile);
                diffFile.setPrmodelId(str_df.prmodelId);
                
                diffFile.setTest(str_df.isTest);
                
                diffFile.getDiffLines().addAll(loadDiffLines(pullRequest, diffFile, str_df.diffLines));
                diffFiles.add(diffFile);
                diffFileMap.put(diffFile.getPRModelId(), diffFile);
            }
        }
        return diffFiles;
    }
    
    private List<DiffLine> loadDiffLines(PullRequest pullRequest, DiffFile diffFile, List<Str_DiffLine> str_dls) {
        List<DiffLine> diffLines = new ArrayList<>();
        if (str_dls != null) {
            for (Str_DiffLine str_dl : str_dls) {
                DiffLine diffLine = new DiffLine(pullRequest, str_dl.changeType, str_dl.text);
                diffLine.setPrmodelId(str_dl.prmodelId);
                diffLine.setDiffFile(diffFile);
                diffLines.add(diffLine);
            }
        }
        return diffLines;
    }
    
    private Set<ProjectChange> loadProjectChange(PullRequest pullRequest, CodeChange codeChange,
            Set<Str_ProjectChange> str_pjs) {
        Set<ProjectChange> projectChanges = new HashSet<>();
        if (str_pjs != null) {
            for (Str_ProjectChange str_pj : str_pjs) {
                ProjectChange projectChange = new ProjectChange(pullRequest, str_pj.name, str_pj.path);
                projectChange.setPrmodelId(str_pj.prmodelId);
                
                projectChange.setCodeChange(codeChange);
                projectChange.getFileChanges()
                        .addAll(loadFileChange(pullRequest, codeChange, projectChange, str_pj.fileChanges));
                
                projectChanges.add(projectChange);
            }
        }
        return projectChanges;
    }
    
    private Set<FileChange> loadFileChange(PullRequest pullRequest, CodeChange codeChange,
            ProjectChange ProjectChange, Set<Str_FileChange> str_fls) {
        Set<FileChange> fileChanges = new HashSet<>();
        for (Str_FileChange str_fl : str_fls) {
            FileChange fileChange = new FileChange(pullRequest, str_fl.changeType,
                    str_fl.name, str_fl.path, str_fl.sourceCodeBefore, str_fl.sourceCodeAfter);
            fileChange.setPrmodelId(str_fl.prmodelId);
            
            fileChange.setCodeChange(codeChange);
            fileChange.setProjectChange(ProjectChange);
            
            fileChange.getClassChanges().addAll(loadClassChange(pullRequest, codeChange,
                    fileChange, str_fl.classChanges));
            if (codeChange != null) {
                codeChange.getFileChanges().add(fileChange);
            }
            fileChanges.add(fileChange);
            
            fileChangeMap.put(fileChange.getPRModelId(), fileChange);
        }
        return fileChanges;
    }
    
    private Set<ClassChange> loadClassChange(PullRequest pullRequest, CodeChange codeChange,
            FileChange fileChange, Set<Str_ClassChange> str_cls) {
        Set<ClassChange> classChanges = new HashSet<>();
        for (Str_ClassChange str_cl : str_cls) {
            ClassChange classChange = new ClassChange(pullRequest, str_cl.changeType,
                    str_cl.name, str_cl.qualifiedName, str_cl.type,
                    str_cl.sourceCodeBefore, str_cl.sourceCodeAfter);
            classChange.setPrmodelId(str_cl.prmodelId);
            
            classChange.setTest(str_cl.isTest);
            
            classChange.getFieldChanges().addAll(loadFieldChange(pullRequest, codeChange, str_cl.fieldChanges));
            classChange.getMethodChanges().addAll(loadMethodChanges(pullRequest, codeChange, str_cl.methodChanges));
            
            classChanges.add(classChange);
            
            classMap.put(classChange, str_cl);
            
            CodeElement beforeElem = classChange.getCodeElementBefore();
            classElementBeforeMap.put(beforeElem.getIndex(), beforeElem);
            CodeElement afterElem = classChange.getCodeElementBefore();
            classElementAfterMap.put(afterElem.getIndex(), afterElem);
        }
        return classChanges;
    }
    
    private Set<FieldChange> loadFieldChange(PullRequest pullRequest, CodeChange codeChange,
            Set<Str_FieldChange> str_fds) {
        Set<FieldChange> fieldChanges = new HashSet<>();
        for (Str_FieldChange str_fd : str_fds) {
            FieldChange fieldChange = new FieldChange( pullRequest, str_fd.changeType,
                    str_fd.name, str_fd.qualifiedName, str_fd.type,
                    str_fd.sourceCodeBefore, str_fd.sourceCodeAfter);
            fieldChange.setPrmodelId(str_fd.prmodelId);
            
            fieldChange.setTest(str_fd.isTest);
            
            fieldChanges.add(fieldChange);
            
            fieldMap.put(fieldChange, str_fd);
        }
        return fieldChanges;
    }
    
    private Set<MethodChange> loadMethodChanges(PullRequest pullRequest, CodeChange codeChange,
            Set<Str_MethodChange> str_mds) {
        Set<MethodChange> methodChanges = new HashSet<>();
        for (Str_MethodChange str_md : str_mds) {
            MethodChange methodChange = new MethodChange(pullRequest, str_md.changeType,
                    str_md.name, str_md.qualifiedName, str_md.type,
                    str_md.sourceCodeBefore, str_md.sourceCodeAfter);
            methodChange.setPrmodelId(str_md.prmodelId);
            
            methodChange.setTest(str_md.isTest);
            
            methodChanges.add(methodChange);
            
            methodMap.put(methodChange, str_md);
            
            CodeElement beforeElem = methodChange.getCodeElementBefore();
            methodElementBeforeMap.put(beforeElem.getIndex(), beforeElem);
            CodeElement afterElem = methodChange.getCodeElementBefore();
            methodElementAfterMap.put(afterElem.getIndex(), afterElem);
        }
        return methodChanges;
    }
    
    private void setReferenceRelation(PullRequest pullRequest, CodeChange codeChange) {
        for (FileChange fileChange : codeChange.getFileChanges()) {
            fileChange.getClassChanges().forEach(c -> setReferenceRelation(pullRequest,c));
        }
    }
    private void setReferenceRelation(PullRequest pullRequest, ClassChange classChange) {
        Str_ClassChange str_cl = classMap.get(classChange);
        classChange.getAfferentClassesBefore().addAll(loadClassElements(pullRequest, str_cl.afferentClassesBefore));
        classChange.getAfferentClassesAfter().addAll(loadClassElements(pullRequest, str_cl.afferentClassesAfter));
        classChange.getEfferentClassesBefore().addAll(loadClassElements(pullRequest, str_cl.efferentClassesBefore));
        classChange.getEfferentClassesAfter().addAll(loadClassElements(pullRequest, str_cl.efferentClassesAfter));
        
        classChange.getFieldChanges().forEach(c -> setReferenceRelation(pullRequest,c));
        classChange.getMethodChanges().forEach(c -> setReferenceRelation(pullRequest,c));
    }
    
    private Set<CodeElement> loadClassElements(PullRequest pullRequest, Set<Str_CodeElement> str_codeElements) {
        Set<CodeElement> classElements = new HashSet<>();
        if (str_codeElements != null) {
            for (Str_CodeElement str_ce : str_codeElements) {
                CodeElement classElement = new CodeElement(pullRequest, str_ce.stage, str_ce.qulifiedName,
                        str_ce.sourceCode);
                classElement.setPrmodelId(str_ce.prmodelId);
                classElements.add(classElement);
            }
        }
        return classElements;
    }
    
    private void setReferenceRelation(PullRequest pullRequest, FieldChange fieldChange) {
        Str_FieldChange str_fd = fieldMap.get(fieldChange);
        fieldChange.getAccessingMethodsBefore().addAll(loadFieldElements(pullRequest,str_fd.accessingMethodsBefore));
        fieldChange.getAccessingMethodsAfter().addAll(loadFieldElements(pullRequest, str_fd.accessingMethodsAfter));
        fieldChange.getCalledMethodsBefore().addAll(loadFieldElements(pullRequest, str_fd.calledMethodsBefore));
        fieldChange.getCalledMethodsAfter().addAll(loadFieldElements(pullRequest,str_fd.calledMethodsAfter));
    }
    
    private Set<CodeElement> loadFieldElements(PullRequest pullRequest, Set<Str_CodeElement> str_codeElements) {
        Set<CodeElement> fieldElements = new HashSet<>();
        if (str_codeElements != null) {
            for (Str_CodeElement str_ce : str_codeElements) {
                CodeElement fieldElement = new CodeElement(pullRequest, str_ce.stage, str_ce.qulifiedName,
                        str_ce.sourceCode);
                fieldElement.setPrmodelId(str_ce.prmodelId);
                fieldElements.add(fieldElement);
            }
        }
        return fieldElements;
    }
    
    private void setReferenceRelation(PullRequest pullRequest , MethodChange methodChange) {
        Str_MethodChange str_md = methodMap.get(methodChange);
        methodChange.getCallingMethodsBefore().addAll(loadMethodElements(pullRequest,str_md.callingMethodsBefore));
        methodChange.getCallingMethodsAfter().addAll(loadMethodElements(pullRequest,str_md.callingMethodsAfter));
        methodChange.getCalledMethodsBefore().addAll(loadMethodElements(pullRequest, str_md.calledMethodsBefore));
        methodChange.getCalledMethodsAfter().addAll(loadMethodElements(pullRequest,str_md.calledMethodsAfter));
    }
    
    private Set<CodeElement> loadMethodElements(PullRequest pullRequest, Set<Str_CodeElement> str_codeElements) {
        Set<CodeElement> methodElements = new HashSet<>();
        if (str_codeElements != null) {
            for (Str_CodeElement str_ce : str_codeElements) {
                CodeElement methodElement = new CodeElement(pullRequest, str_ce.stage, str_ce.qulifiedName,
                        str_ce.sourceCode);
                methodElement.setPrmodelId(str_ce.prmodelId);
                methodElements.add(methodElement);
            }
        }
        return methodElements;
    }
    
    @SuppressWarnings("unused")
    private Set<CodeElement> getElements(Set<String> indices, Map<String, CodeElement> elemMap) {
        Set<CodeElement> elems = new HashSet<>();
        for (String index : indices) {
            CodeElement codeElem = elemMap.get(index);
            if (codeElem != null) {
                elems.add(codeElem);
            }
        }
        return elems;
    }
    
    private List<CIStatus> loadCIStatus(PullRequest pullRequest, List<Str_CIStatus> str_cis) {
        List<CIStatus> ciStaruses = new ArrayList<>();
        for (Str_CIStatus str_ci : str_cis) {
            PRModelDate createDate = new PRModelDate(str_ci.createDate);
            PRModelDate updateDate = new PRModelDate(str_ci.updateDate);
            
            CIStatus ciStarus = new CIStatus(pullRequest, str_ci.context,
                    str_ci.state, str_ci.description, str_ci.targetUrl, createDate, updateDate);
            ciStarus.setPrmodelId(str_ci.prmodelId);
            
            ciStaruses.add(ciStarus);
        }
        return ciStaruses;
    }
    
    private ChangeSummary loadChangeSummary(PullRequest pullRequest, Str_ChangeSummary str_summary) {
        ChangeSummary changeSummary = new ChangeSummary(pullRequest, str_summary.hasJavaFile);
        changeSummary.setPrmodelId(str_summary.prmodelId);
        
        changeSummary.getDiffFiles().addAll(loadDiffFiles(pullRequest, str_summary.diffFiles));
        CodeChange codeChange = null;
        ProjectChange projectChange=null;
        if (str_summary.fileChanges != null) {
            Set<Str_FileChange> str_fileChanges = new HashSet<>(str_summary.fileChanges);
            
            List<FileChange> fileChangesList = new ArrayList<>(
                    loadFileChange(pullRequest, codeChange, projectChange, str_fileChanges));
            changeSummary.getFileChanges().addAll(fileChangesList);
        }
        return changeSummary;
    }
    
    private Set<Label> loadLabels(PullRequest pullRequest, Set<Str_Label> str_lbs) {
        Set<Label> labels = new HashSet<>();
        for (Str_Label str_lb : str_lbs) {
            Label label = new Label(pullRequest, str_lb.name, str_lb.color, str_lb.description);
            label.setPrmodelId(str_lb.prmodelId);
            
            label.setIssueEvent(eventMap.get(str_lb.issueEventId));
            
            labels.add(label);
        }
        return labels;
    }
}
