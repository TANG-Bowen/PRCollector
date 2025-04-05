/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.jwrmodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.jtool.jxp3model.ProjectChange;
import org.jtool.jxp3model.FileChange;
import org.jtool.jxp3model.ClassChange;
import org.jtool.jxp3model.CodeElement;
import org.jtool.jxp3model.FieldChange;
import org.jtool.jxp3model.MethodChange;

public class StringConverter {
    
    public StringConverter() {
    }
    
    public Str_PullRequest buildPullRequest(PullRequest pullRequest) {
        Str_PullRequest str_pr = new Str_PullRequest();
        
        str_pr.prmodelId = pullRequest.getPRModelId();
        
        str_pr.id = pullRequest.getId();
        str_pr.title = pullRequest.getTitle();
        str_pr.repositoryName = pullRequest.getRepositoryName();
        str_pr.state = pullRequest.getState();
        if(pullRequest.getCreateDate() != null) {
            str_pr.createDate = pullRequest.getCreateDate().toString();
        }
        if(pullRequest.getEndDate() != null) {
            str_pr.endDate = pullRequest.getEndDate().toString();
        }
        str_pr.mergeBranch = pullRequest.getMergeBranch();
        str_pr.headBranch = pullRequest.getHeadBranch();
        str_pr.pageUrl = pullRequest.getPageUrl();
        str_pr.repositorySrcDLUrl = pullRequest.getRepositorySrcDLUrl();
        str_pr.headRepositorySrcDLUrl = pullRequest.getHeadRepositorySrcDLUrl();
        
        str_pr.isMerged = pullRequest.isMerged();
        str_pr.isStandardMerged = pullRequest.isStandardMerged();
        
        str_pr.repositoryBranches = pullRequest.getRepositoryBranches();
        str_pr.headRepositoryBranches = pullRequest.getHeadRepositoryBranches();
        
        str_pr.participantRetrievable = pullRequest.isParticipantRetrievable();
        str_pr.issueEventRetrievable = pullRequest.isIssueEventRetrievable();
        str_pr.issueCommentRetrievable = pullRequest.isIssueCommentRetrievable();
        str_pr.reviewEventRetrievable = pullRequest.isReviewCommentRetrievable();
        str_pr.reviewCommentRetrievable = pullRequest.isReviewCommentRetrievable();
        str_pr.commitRetrievable = pullRequest.isCommitRetrievable();
        str_pr.sourceCodeRetrievable = pullRequest.isSourceCodeRetrievable();
        
        str_pr.participants = buildParticipants(pullRequest.getParticipants());
        str_pr.description = buildDescription(pullRequest.getDescription());
        str_pr.htmlDescription = buildHTMLDescription(pullRequest.getHtmlDescription());
        str_pr.conversation = buildConversation(pullRequest.getConversation());
        str_pr.commits = buildCommits(pullRequest.getCommits());
        str_pr.changeSummary = buildChangeSummary(pullRequest.getChangeSummary());
        str_pr.addedLabels = buildLabels(pullRequest.getAddedLabels());
        str_pr.removedLabels = buildLabels(pullRequest.getRemovedLabels());
        str_pr.finalLabels = buildLabels(pullRequest.getFinalLabels());
        
        return str_pr;
    }
    
    public Str_DeficientPullRequest buildDeficientPullRequest(DeficientPullRequest pullRequest) {
        Str_DeficientPullRequest str_pr = new Str_DeficientPullRequest();
        
        str_pr.prmodelId = pullRequest.getPRModelId();
        
        str_pr.id = pullRequest.getId();
        str_pr.title = pullRequest.getTitle();
        str_pr.repositoryName = pullRequest.getRepositoryName();
        str_pr.state = pullRequest.getState();
        
        str_pr.createDate = pullRequest.getCreateDate().toString();
        str_pr.endDate = pullRequest.getEndDate().toString();
        
        str_pr.mergeBranch = pullRequest.getMergeBranch();
        str_pr.headBranch = pullRequest.getHeadBranch();
        str_pr.pageUrl = pullRequest.getPageUrl();
        str_pr.repositorySrcDLUrl = pullRequest.getRepositorySrcDLUrl();
        str_pr.headRepositorySrcDLUrl = pullRequest.getHeadRepositorySrcDLUrl();
        
        str_pr.isMerged = pullRequest.isMerged();
        str_pr.isStandardMerged = pullRequest.isStandardMerged();
        
        str_pr.repositoryBranches = pullRequest.getRepositoryBranches();
        str_pr.headRepositoryBranches = pullRequest.getHeadRepositoryBranches();
        
        str_pr.participantRetrievable = pullRequest.isParticipantRetrievable();
        str_pr.issueEventRetrievable = pullRequest.isIssueEventRetrievable();
        str_pr.issueCommentRetrievable = pullRequest.isIssueCommentRetrievable();
        str_pr.reviewEventRetrievable = pullRequest.isReviewEventRetrievable();
        str_pr.reviewCommentRetrievable = pullRequest.isReviewCommentRetrievable();
        str_pr.commitRetrievable = pullRequest.isCommitRetrievable();
        str_pr.sourceCodeRetrievable = pullRequest.isSourceCodeRetrievable();
        
        str_pr.exceptionOutput = pullRequest.getExceptionOutput();
        
        str_pr.participants = buildParticipants(pullRequest.getParticipants());
        str_pr.description = buildDescription(pullRequest.getDescription());
        str_pr.htmlDescription = buildHTMLDescription(pullRequest.getHtmlDescription());
        str_pr.conversation = buildConversation(pullRequest.getConversation());
        str_pr.commits = buildCommits(pullRequest.getCommits());
        str_pr.changeSummary = buildChangeSummary(pullRequest.getChangeSummary());
        str_pr.addedLabels = buildLabels(pullRequest.getAddedLabels());
        str_pr.removedLabels = buildLabels(pullRequest.getRemovedLabels());
        str_pr.finalLabels = buildLabels(pullRequest.getFinalLabels());
        
        return str_pr;
    }
    
    private Set<Str_Participant> buildParticipants(Set<Participant> participants) {
        Set<Str_Participant> str_pts = new HashSet<>();
        for (Participant pt : participants) {
            Str_Participant str_pt = new Str_Participant();
            str_pts.add(str_pt);
            
            str_pt.prmodelId = pt.getPRModelId();
            
            str_pt.userId = pt.getUserId();
            str_pt.login = pt.getLogin();
            str_pt.name = pt.getName();
            str_pt.location = pt.getLocation();
            str_pt.role = pt.getRole();
            str_pt.followers = pt.getFollowers();
            str_pt.follows = pt.getFollows();
            str_pt.actionRecord = pt.getActionRecord();
        }
        return str_pts;
    }
    
    private Str_Description buildDescription(Description description) {
        Str_Description str_dp = new Str_Description();
        
        str_dp.prmodelId = description.getPRModelId();
        
        str_dp.body = description.getBody();
        
        str_dp.markdownDoc = buildMarkdownDoc(description.getMarkdownDoc());
        return str_dp;
    }
    
    private Str_MarkdownDoc buildMarkdownDoc(MarkdownDoc markdownDoc) {
        Str_MarkdownDoc str_md = new Str_MarkdownDoc();
        str_md.prmodelId = markdownDoc.getPRModelId();
        
        str_md.headingStrings = markdownDoc.getHeadingStrings();
        str_md.boldStrings = markdownDoc.getBoldStrings();
        str_md.italicStrings = markdownDoc.getItalicStrings();
        str_md.quoteStrings = markdownDoc.getQuoteStrings();
        str_md.linkStrings = markdownDoc.getLinkStrings();
        str_md.codeStrings = markdownDoc.getCodeStrings();
        str_md.codeBlockStrings = markdownDoc.getCodeBlockStrings();
        str_md.mentionStrings = markdownDoc.getMentionStrings();
        str_md.textStrings = markdownDoc.getTextStrings();
        
        str_md.pullLinkStrings = markdownDoc.getPullLinkStrings();
        str_md.issueLinkStrings = markdownDoc.getIssueLinkStrings();
        
        str_md.htmlComments = markdownDoc.getHtmlComments();
        return str_md;
    }
    
    private Str_HTMLDescription buildHTMLDescription(HTMLDescription description) {
        Str_HTMLDescription str_dp = new Str_HTMLDescription();
        if (description != null) {
            str_dp.prmodelId = description.getPRModelId();
            
            str_dp.body = description.getBody();
            str_dp.mentionUsers = description.getMentionUsers();
        }
        return str_dp;
    }
    
    private Str_Conversation buildConversation(Conversation conversation) {
        Str_Conversation str_cv = new Str_Conversation();
        
        str_cv.prmodelId = conversation.getPRModelId();
        
        str_cv.issueComments = buildIssueComments(conversation.getIssueComments());
        str_cv.issueEvents = buildIssueEvents(conversation.getIssueEvents());
        str_cv.reviewComments = buildReviewComments(conversation.getReviewComments());
        str_cv.reviewEvents = buildReviews(conversation.getReviewEvents());
        
        str_cv.timeLineIds = buildTimeLine(conversation.getTimeLine());
        
        str_cv.codeReviews = buildCodeReviewSnippets(conversation.getCodeReviews());
        
        return str_cv;
    }
    
    private LinkedHashSet<Str_IssueComment> buildIssueComments(LinkedHashSet<IssueComment> comments) {
        LinkedHashSet<Str_IssueComment> str_cts = new LinkedHashSet<>();
        for (IssueComment comment : comments) {
            Str_IssueComment str_ct = new Str_IssueComment();
            str_cts.add(str_ct);
            
            str_ct.prmodelId = comment.getPRModelId();
            
            str_ct.date = comment.getDate().toString();
            str_ct.body = comment.getBody();
            
            str_ct.markdownDoc = buildMarkdownDoc(comment.getMarkdownDoc());
            if (comment.getParticipant()!=null) {
                str_ct.participantId = comment.getParticipant().getPRModelId();
            }
        }
        return str_cts;
    }
    
    private LinkedHashSet<Str_IssueEvent> buildIssueEvents(LinkedHashSet<IssueEvent> events) {
        LinkedHashSet<Str_IssueEvent> str_ets = new LinkedHashSet<>();
        for (IssueEvent event : events) {
            Str_IssueEvent str_et = new Str_IssueEvent();
            str_ets.add(str_et);
            
            str_et.prmodelId = event.getPRModelId();
            
            str_et.date = event.getDate().toString();
            str_et.body = event.getBody();
            if (event.getParticipant()!=null) {
                str_et.participantId = event.getParticipant().getPRModelId();
            }
        }
        return str_ets;
    }
    
    private LinkedHashSet<Str_ReviewComment> buildReviewComments(LinkedHashSet<ReviewComment> comments) {
        LinkedHashSet<Str_ReviewComment> str_cts = new LinkedHashSet<>();
        for (ReviewComment comment : comments) {
            Str_ReviewComment str_ct = new Str_ReviewComment();
            str_cts.add(str_ct);
            
            str_ct.prmodelId = comment.getPRModelId();
            
            str_ct.date = comment.getDate().toString();
            str_ct.body = comment.getBody();
            
            str_ct.markdownDoc = buildMarkdownDoc(comment.getMarkdownDoc());
            if (comment.getParticipant()!=null) {
                str_ct.participantId = comment.getParticipant().getPRModelId();
            }
            str_ct.snippetId = comment.getCodeReviewSnippet().getPRModelId();
        }
        return str_cts;
    }
    
    private LinkedHashSet<Str_ReviewEvent> buildReviews(LinkedHashSet<ReviewEvent> reviews) {
       LinkedHashSet<Str_ReviewEvent> str_rvs = new LinkedHashSet<>();
       for (ReviewEvent review : reviews){
           Str_ReviewEvent str_rv = new Str_ReviewEvent();
           str_rvs.add(str_rv);
           
           str_rv.prmodelId = review.getPRModelId();
           
           str_rv.date = review.getDate().toString();
           str_rv.body = review.getBody();
           
           str_rv.markdownDoc = buildMarkdownDoc(review.getMarkdownDoc());
           if (review.getParticipant()!=null) {
               str_rv.participantId = review.getParticipant().getPRModelId();
           }
       }
       return str_rvs;
    }
    
    private LinkedHashSet<Str_CodeReviewSnippet> buildCodeReviewSnippets(LinkedHashSet<CodeReviewSnippet> reviews) {
        LinkedHashSet<Str_CodeReviewSnippet> str_rvs = new LinkedHashSet<>();
        for (CodeReviewSnippet review : reviews){
            Str_CodeReviewSnippet str_sp = new Str_CodeReviewSnippet();
            str_rvs.add(str_sp);
            
            str_sp.prmodelId = review.getPRModelId();
            
            str_sp.diffHunk = review.getDiffHunk();
            
            str_sp.reviewCommentIds = review.getReviewComments().stream().map(c -> c.getPRModelId())
                                            .collect(Collectors.toList());
        }
        return str_rvs;
    }
    
    private LinkedHashSet<String> buildTimeLine(LinkedHashSet<Action> actions) {
        LinkedHashSet<String> str_tlIds = new LinkedHashSet<>();
        for (Action action : actions) {
            str_tlIds.add(action.getPRModelId());
        }
        return str_tlIds;
    }
    
    private List<Str_Commit> buildCommits(List<Commit> commits) {
        List<Str_Commit> str_cts = new ArrayList<>();
        for (Commit commit: commits) {
            Str_Commit str_ct = new Str_Commit();
            str_cts.add(str_ct);
            
            str_ct.prmodelId = commit.getPRModelId();
            
            str_ct.sha = commit.getSha();
            str_ct.shortSha = commit.getShortSha();
            str_ct.date = commit.getDate().toString();
            str_ct.type = commit.getType();
            str_ct.message = commit.getMessage();
            
            str_ct.committerId = commit.getCommitter().getPRModelId();
            str_ct.codeChange = buildCodeChange(commit.getCodeChange());
            str_ct.ciStatus = buildCIStatus(commit);
        }
        return str_cts;
    }
    
    private Str_CodeChange buildCodeChange(CodeChange codeChange) {
        Str_CodeChange str_ch = new Str_CodeChange();
        if (codeChange != null) {
            str_ch.prmodelId = codeChange.getPRModelId();
            
            str_ch.hasJavaFile = codeChange.hasJavaFile();
            
            str_ch.diffFiles = buildDiffFiles(codeChange.getDiffFiles());
            str_ch.projectChanges = buildProjectChange(codeChange.getProjectChanges());
        }
        return str_ch;
    }
    
    private List<Str_DiffFile> buildDiffFiles(List<DiffFile> files) {
        List<Str_DiffFile> str_dfs = new ArrayList<>();
        for (DiffFile diffFile : files) {
            Str_DiffFile str_df = new Str_DiffFile();
            str_dfs.add(str_df);
            
            str_df.prmodelId = diffFile.getPRModelId();
            
            str_df.changeType = diffFile.getChangeType().toString();
            str_df.path = diffFile.getPath();
            str_df.bodyAll = diffFile.getBodyAll();
            str_df.bodyAdd = diffFile.getBodyAdd();
            str_df.bodyDel = diffFile.getBodyDel();
            str_df.sourceCodeBefore = diffFile.getSourceCodeBefore();
            str_df.sourceCodeAfter = diffFile.getSourceCodeAfter();
            str_df.isJavaFile = diffFile.isJavaFile();
            str_df.isTest = diffFile.isTest();
            
            str_df.diffLines = buildDiffLines(diffFile.getDiffLines());
        }
        return str_dfs;
    }
    
    private List<Str_DiffLine> buildDiffLines(List<DiffLine> lines) {
        List<Str_DiffLine> str_dls = new ArrayList<>();
        for (DiffLine diffLine : lines) {
            Str_DiffLine str_dl = new Str_DiffLine();
            str_dls.add(str_dl);
            
            str_dl.prmodelId = diffLine.getPRModelId();
            
            str_dl.changeType = diffLine.getChangeType().toString();
            str_dl.text = diffLine.getText();
        }
        return str_dls;
    }
    private Set<Str_ProjectChange> buildProjectChange(Set<ProjectChange> pchanges) {
        Set<Str_ProjectChange> str_pjs = new HashSet<>();
        for (ProjectChange pchange : pchanges) {
            Str_ProjectChange str_pj = new Str_ProjectChange();
            str_pjs.add(str_pj);
            
            str_pj.prmodelId = pchange.getPRModelId();
            
            str_pj.name = pchange.getName();
            str_pj.path = pchange.getPath();
            
            str_pj.fileChanges = buildFileChange(pchange.getFileChanges());
        }
        return str_pjs;
    }
    
    private Set<Str_FileChange> buildFileChange(Set<FileChange> fchanges) {
        Set<Str_FileChange> str_fls = new HashSet<>();
        for (FileChange fchange : fchanges) {
            Str_FileChange str_fl = new Str_FileChange();
            str_fls.add(str_fl);
            
            str_fl.prmodelId = fchange.getPRModelId();
            
            str_fl.changeType = fchange.getChangeType().toString();
            str_fl.name = fchange.getName();
            str_fl.path = fchange.getPath();
            str_fl.sourceCodeBefore = fchange.getSourceCodeBefore();
            str_fl.sourceCodeAfter = fchange.getSourceCodeAfter();
            str_fl.isTest = fchange.isTest();
            
            str_fl.classChanges = buildClassChange(fchange.getClassChanges());
        }
        return str_fls;
    }
    
    private Set<Str_ClassChange> buildClassChange(Set<ClassChange> cchanges) {
       Set<Str_ClassChange> str_cls = new HashSet<>(); 
       for (ClassChange cchange : cchanges) {
           Str_ClassChange str_cl = new Str_ClassChange();
           str_cls.add(str_cl);
           
           str_cl.prmodelId = cchange.getPRModelId();
           
           str_cl.changeType = cchange.getChangeType();
           str_cl.name = cchange.getName();
           str_cl.qualifiedName = cchange.getQualifiedName();
           str_cl.type = cchange.getType();
           str_cl.sourceCodeBefore = cchange.getSourceCodeBefore();
           str_cl.sourceCodeAfter = cchange.getSourceCodeAfter();
           str_cl.isTest = cchange.isTest();
           
           str_cl.afferentClassesBefore = buildClassElement(cchange.getAfferentClassesBefore());
           str_cl.afferentClassesAfter = buildClassElement(cchange.getAfferentClassesAfter());
           str_cl.efferentClassesBefore = buildClassElement(cchange.getEfferentClassesBefore());
           str_cl.efferentClassesAfter = buildClassElement(cchange.getEfferentClassesAfter());
           
           str_cl.fieldChanges = buildFieldChange(cchange.getFieldChanges());
           str_cl.methodChanges = buildMethodChange(cchange.getMethodChanges());
       }
       return str_cls;
    }
    
    private Set<Str_CodeElement> buildClassElement(Set<CodeElement> codeElements){
        Set<Str_CodeElement> str_ces = new HashSet<>();
        for(CodeElement cei : codeElements)
        {
            Str_CodeElement str_ce = new Str_CodeElement();
            str_ces.add(str_ce);
            
            str_ce.prmodelId = cei.getPRModelId();
            
            str_ce.stage = cei.getStage();
            str_ce.qulifiedName = cei.getQualifiedName();
            str_ce.sourceCode = cei.getSourceCode();
        }
        return str_ces;
    }
    
    private Set<Str_FieldChange> buildFieldChange(Set<FieldChange> fchanges) {
        Set<Str_FieldChange> str_fds = new HashSet<>();
        for (FieldChange fchange : fchanges) {
            Str_FieldChange str_fd = new Str_FieldChange();
            str_fds.add(str_fd);
            
            str_fd.prmodelId = fchange.getPRModelId();
            
            str_fd.changeType = fchange.getChangeType().toString();
            str_fd.name = fchange.getName();
            str_fd.qualifiedName = fchange.getQualifiedName();
            str_fd.type = fchange.getType();
            str_fd.sourceCodeBefore = fchange.getSourceCodeBefore();
            str_fd.sourceCodeAfter = fchange.getSourceCodeAfter();
            str_fd.isTest = fchange.isTest();
            
            str_fd.accessingMethodsBefore = buildMethodElement(fchange.getAccessingMethodsBefore());
            str_fd.accessingMethodsAfter = buildMethodElement(fchange.getAccessingMethodsAfter());
            str_fd.calledMethodsBefore = buildMethodElement(fchange.getCalledMethodsBefore());
            str_fd.calledMethodsAfter = buildMethodElement(fchange.getCalledMethodsAfter());
        }
        return str_fds;
    }
    
    private Set<Str_MethodChange> buildMethodChange(Set<MethodChange> mchanges) {
        Set<Str_MethodChange> str_mds = new HashSet<>();
        for (MethodChange mchange : mchanges) {
            Str_MethodChange str_md = new Str_MethodChange();
            str_mds.add(str_md);
            
            str_md.prmodelId = mchange.getPRModelId();
            
            str_md.changeType = mchange.getChangeType().toString();
            str_md.name = mchange.getName();
            str_md.qualifiedName = mchange.getQualifiedName();
            str_md.type = mchange.getType();
            str_md.sourceCodeBefore = mchange.getSourceCodeBefore();
            str_md.sourceCodeAfter = mchange.getSourceCodeAfter();
            str_md.isTest = mchange.isTest();
            
            str_md.callingMethodsBefore = buildMethodElement(mchange.getCallingMethodsBefore());
            str_md.callingMethodsAfter = buildMethodElement(mchange.getCallingMethodsAfter());
            str_md.calledMethodsBefore = buildMethodElement(mchange.getCalledMethodsBefore());
            str_md.calledMethodsAfter = buildMethodElement(mchange.getCalledMethodsAfter());
        }
        return str_mds;
    }
    
    private Set<Str_CodeElement> buildMethodElement(Set<CodeElement> codeElements) {
        Set<Str_CodeElement> str_mces = new HashSet<>();
        for(CodeElement cei : codeElements)
        {
            Str_CodeElement str_codeElement = new Str_CodeElement();
            str_mces.add(str_codeElement);
            
            str_codeElement.prmodelId = cei.getPRModelId();
            
            str_codeElement.qulifiedName = cei.getQualifiedName();
            str_codeElement.stage = cei.getStage();
            str_codeElement.sourceCode = cei.getSourceCode();
        }
        return str_mces;
    }
    
    private List<Str_CIStatus> buildCIStatus(Commit commit) {
        List<Str_CIStatus> str_statuses = new ArrayList<>();
        for (CIStatus status : commit.getCIStatus()) {
            Str_CIStatus str_ci = new Str_CIStatus();
            str_statuses.add(str_ci);
            
            str_ci.prmodelId = status.getPRModelId();
            
            str_ci.context = status.getContext();
            str_ci.state = status.getState();
            str_ci.description = status.getDescription();
            str_ci.targetUrl = status.getTargetUrl();
            str_ci.createDate = status.getCreateDate().toString();
            str_ci.updateDate = status.getUpdateDate().toString();
        }
        return str_statuses;
    }
    
    private Str_ChangeSummary buildChangeSummary(ChangeSummary changeSummary) {
        Str_ChangeSummary str_summary = new Str_ChangeSummary();
        if (changeSummary != null) {
            str_summary.prmodelId = changeSummary.getPRModelId();
            
            str_summary.hasJavaFile = changeSummary.hasJavaFile();
            
            str_summary.diffFiles = buildDiffFiles(changeSummary.getDiffFiles());
            
            Set<FileChange> fileChanges = new HashSet<>(changeSummary.getFileChanges());
            str_summary.fileChanges = new ArrayList<>(buildFileChange(fileChanges));
        }
        return str_summary;
    }
    
    private Set<Str_Label> buildLabels(Set<Label> labels) {
        Set<Str_Label> str_labels = new HashSet<>();
        for (Label label : labels) {
            Str_Label str_lb = new Str_Label();
            str_labels.add(str_lb);
            
            str_lb.prmodelId = label.getPRModelId();
            
            str_lb.name = label.getName();
            str_lb.color = label.getColor();
            str_lb.description = label.getDescription();
            if (label.getIssueEvent()!=null) {
                str_lb.issueEventId = label.getIssueEvent().getPRModelId();
            }
        }
        return str_labels;
     }
}
