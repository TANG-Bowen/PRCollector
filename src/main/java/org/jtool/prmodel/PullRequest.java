package org.jtool.prmodel;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class PullRequest extends PRElement {
    
    /* -------- Attributes -------- */
    
    protected final String id;
    protected final String title;
    protected final String repositoryName;
    protected final String state;
    
    protected final PRModelDate createDate;
    protected final PRModelDate endDate;
    
    protected final String mergeBranch;
    protected final String headBranch;
    protected final String pageUrl;
    protected final String repositorySrcDLUrl;
    protected final String headRepositorySrcDLUrl;
    
    protected final boolean isMerged;
    protected final boolean isStandardMerged;
    
    protected final List<String> repositoryBranches;
    protected final List<String> headRepositoryBranches;
    
    protected boolean participantRetrievable = true;
    protected boolean commentRetrievable = true;
    protected boolean reviewCommentRetrievable = true;
    protected boolean eventRetrievable = true;
    protected boolean reviewEventRetrievable = true;
    protected boolean commitRetrievable = true;
    protected boolean sourceCodeRetrievable = true;
    
    /* -------- Attributes -------- */
    
    protected Set<Participant> participants = new HashSet<>();
    protected Conversation conversation;
    protected List<Commit> commits = new ArrayList<>();
    protected Description description;
    protected HTMLDescription htmlDescription;
    protected FilesChanged filesChanged;
    
    protected Set<Label> addedLabels = new HashSet<>();
    protected Set<Label> removedLabels = new HashSet<>();
    protected Set<Label> finalLabels = new HashSet<>();
    
    public PullRequest(String id, String title, String repositoryName, String state,
            PRModelDate createDate, PRModelDate endDate,
            String mergeBranch, String headBranch, String pageUrl,
            String repositorySrcDLUrl, String headRepositorySrcDLUrl,
            boolean isMerged, boolean isStandardMerged,
            List<String> repositoryBranches, List<String> headRepositoryBranches) {
        super(null);
        
        this.id = id;
        this.title = title;
        this.repositoryName = repositoryName;
        this.state = state;
        
        this.createDate = createDate;
        this.endDate = endDate;
        
        this.mergeBranch = mergeBranch;
        this.headBranch = headBranch;
        this.pageUrl = pageUrl;
        this.repositorySrcDLUrl = repositorySrcDLUrl;
        this.headRepositorySrcDLUrl = headRepositorySrcDLUrl;
        
        this.isMerged = isMerged;
        this.isStandardMerged = isStandardMerged;
        
        this.repositoryBranches = repositoryBranches;
        this.headRepositoryBranches = headRepositoryBranches;
    }
    
    public void addParticipant(Participant participant) {
        participants.add(participant);
    }
    
    public void setParticipantRetrievable(boolean bool) {
        participantRetrievable = bool;
    }
    
    public void setCommentRetrievable(boolean bool) {
        commentRetrievable = bool;
    }
    
    public void setReviewCommentRetrievable(boolean bool) {
        reviewCommentRetrievable = bool;
    }
    
    public void setEventRetrievable(boolean bool) {
        eventRetrievable = bool;
    }
    
    public void setReviewEventRetrievable(boolean bool) {
        reviewEventRetrievable = bool;
    }
    
    public void setCommitRetrievable(boolean bool) {
        commitRetrievable = bool;
    }
    
    public void setSourceCodeRetrievable(boolean bool) {
        sourceCodeRetrievable = bool;
    }
    
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
    
    public void setDescription(Description description) {
        this.description = description;
    }
    
    public void setHTMLDescription(HTMLDescription description) {
        this.htmlDescription = description;
    }
    
    public void setFilesChanged(FilesChanged filesChanged) {
        this.filesChanged = filesChanged;
    }
    
    public void print() {
        String prefix = "PullRequest ";
        printPRBase(prefix);
        description.print();
        htmlDescription.print();
        participants.forEach(e -> e.print());
        conversation.print();
        commits.forEach(e -> e.print());
        
        filesChanged.print();
        
        addedLabels.forEach(e -> e.print());
        removedLabels.forEach(e -> e.print());
        finalLabels.forEach(e -> e.print());
    }
    
    protected void printPRBase(String prefix) {
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "id : " + id);
        System.out.println(prefix + "title : " + title);
        System.out.println(prefix + "repositoryName : " + repositoryName);
        System.out.println(prefix + "state : " + state);
        
        System.out.println(prefix + "createDate : " + createDate.toString());
        System.out.println(prefix + "endDate : " + endDate.toString());
        
        System.out.println(prefix + "mergeBranch : " + mergeBranch);
        System.out.println(prefix + "headBranch : " + headBranch);
        System.out.println(prefix + "pageUrl : " + pageUrl);
        System.out.println(prefix + "repositorySrcDLUrl : " + repositorySrcDLUrl);
        
        System.out.println(prefix + "isMerged : " + isMerged);
        System.out.println(prefix + "isStandardMerged : " + isStandardMerged);
        System.out.println(prefix + "sourceCodeRetrievable : " + sourceCodeRetrievable);
        
        
        System.out.println(prefix + "headRepositorySrcDLUrl : " + headRepositorySrcDLUrl);
    }
    
    public List<Commit> getTragetCommits() {
        return commits.stream()
                      .filter(c -> !c.getType().equals("merge") && !c.getType().equals("inital"))
                      .collect(Collectors.toList());
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PullRequest) ? equals((PullRequest)obj) : false;
    }
    
    public boolean equals(PullRequest pullRequest) {
        return pullRequest != null && (this == pullRequest || this.id == pullRequest.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    /* ------------------------------------
     * APIs
     --------------------------------------*/
    
    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getRepositoryName() {
        return repositoryName;
    }
    
    public String getState() {
        return state.toString();
    }
    
    public PRModelDate getCreateDate() {
        return createDate;
    }
    
    public PRModelDate getEndDate() {
        return endDate;
    }
    
    public boolean isMerged() {
        return isMerged;
    }
    
    public boolean isStandardMerged() {
        return isStandardMerged;
    }
    
    public String getMergeBranch() {
        return mergeBranch;
    }
    
    public String getHeadBranch() {
        return headBranch;
    }
    
    public String getPageUrl() {
        return pageUrl;
    }
    
    public String getRepositorySrcDLUrl() {
        return repositorySrcDLUrl;
    }
    
    public String getHeadRepositorySrcDLUrl() {
        return headRepositorySrcDLUrl;
    }
    
    public List<String> getRepositoryBranches() {
        return repositoryBranches;
    }
    
    public List<String> getHeadRepositoryBranches() {
        return headRepositoryBranches;
    }
    
    public boolean isParticipantRetrievable() {
        return participantRetrievable;
    }
    
    public boolean isCommentRetrievable() {
        return commentRetrievable;
    }
    
    public boolean isReviewCommentRetrievable() {
        return reviewCommentRetrievable;
    }
    
    public boolean isEventRetrievable() {
        return eventRetrievable;
    }
    
    public boolean isReviewEventRetrievable() {
        return reviewEventRetrievable;
    }
    
    public boolean isCommitRetrievable() {
        return commitRetrievable;
    }
    
    public boolean isSourceCodeRetrievable() {
        return sourceCodeRetrievable;
    }
    
    public boolean isDeficient() {
        return false;
    }
    
    public Set<Participant> getParticipants() {
        return participants;
    }
    
    public Participant getParticipant(String name) {
        return participants.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
    }
    
    public Conversation getConversation() {
        return conversation;
    }
    
    public List<Commit> getCommits() {
        return commits;
    }
    
    public Commit getCommit(String commitSha) {
        return commits.stream().filter(c -> c.getSha().equals(commitSha)).findFirst().orElse(null);
    }
    
    public Description getDescription() {
        return description;
    }
    
    public HTMLDescription getHtmlDescription() {
        return htmlDescription;
    }
    
    public FilesChanged getFilesChanged() {
        return filesChanged;
    }
    
    public Set<Label> getAddedLabels() {
        return addedLabels;
    }
    
    public Set<Label> getRemovedLabels() {
        return removedLabels;
    }
    
    public Set<Label> getFinalLabels() {
        return finalLabels;
    }
}
