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
    protected boolean issueEventRetrievable = true;
    protected boolean issueCommentRetrievable = true;
    protected boolean reviewEventRetrievable = true;
    protected boolean reviewCommentRetrievable = true;
    protected boolean commitRetrievable = true;
    protected boolean sourceCodeRetrievable = true;
    
    /* -------- Attributes -------- */
    
    protected Set<Participant> participants = new HashSet<>();
    protected Conversation conversation;
    protected List<Commit> commits = new ArrayList<>();
    protected Description description;
    protected HTMLDescription htmlDescription;
    protected ChangeSummary changeSummary;
    
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
    
    public void setIssueCommentRetrievable(boolean bool) {
        issueCommentRetrievable = bool;
    }
    
    public void setReviewCommentRetrievable(boolean bool) {
        reviewCommentRetrievable = bool;
    }
    
    public void setIssueEventRetrievable(boolean bool) {
        issueEventRetrievable = bool;
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
    
    public void setChangeSummary(ChangeSummary changeSummary) {
        this.changeSummary = changeSummary;
    }
    
    public void print() {
        String prefix = "PullRequest ";
        printPRBase(prefix);
        description.print();
        htmlDescription.print();
        participants.forEach(e -> e.print());
        conversation.print();
        commits.forEach(e -> e.print());
        
        if (changeSummary != null) {
            changeSummary.print();
        }
        
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
        System.out.println(prefix + "participantRetrievable : " + participantRetrievable);
        System.out.println(prefix + "issueCommentRetrievable : " + issueCommentRetrievable);
        System.out.println(prefix + "reviewCommentRetrievable : " + reviewCommentRetrievable);
        System.out.println(prefix + "issueEventRetrievable : " + issueEventRetrievable);
        System.out.println(prefix + "reviewEventRetrievable : " + reviewEventRetrievable);
        System.out.println(prefix + "commitRetrievable : " + commitRetrievable);
        
        System.out.println(prefix + "headRepositorySrcDLUrl : " + headRepositorySrcDLUrl);
        
        
    }
    
    public List<Commit> getTargetCommits() {
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
    
    /**
     * Returns pull request's number id.
     * @return id String in format: repository name#id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Returns pull request's number.
     * @return the number
     */
    public int getNumber() {
        String number = id.split("#")[1];
        return Integer.parseInt(number);
    }
    
    /**
     * Returns pull request's title.
     * @return title String
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Returns pull request's repository name.
     * @return repository name String
     */
    public String getRepositoryName() {
        return repositoryName;
    }
    
    /**
     * Returns pull request's state (open or closed).
     * @return state String
     */
    public String getState() {
        return state.toString();
    }
    
    /**
     * Returns pull request's create date.
     * @return a PRModelDate
     */
    public PRModelDate getCreateDate() {
        return createDate;
    }
    
    /**
     * Returns pull request's the date when in closed state.
     * @return a PRModelDate
     */
    public PRModelDate getEndDate() {
        return endDate;
    }
    
    /**
     * Returns if the pull request is merged include common process and reviewer commit.
     * @return true if is merged
     */
    public boolean isMerged() {
        return isMerged;
    }
    
    /**
     * Returns if the pull request is directly merged in a common way.
     * @return true if is merged
     */
    public boolean isStandardMerged() {
        return isStandardMerged;
    }
    
    /**
     * Returns the aim branch in the remote repository which the pull request is merged to.
     * @return branch name String
     */
    public String getMergeBranch() {
        return mergeBranch;
    }
    
    /**
     * Returns the aim branch in the author local repository which the pull request's commits belongs to.
     * @return branch name String
     */
    public String getHeadBranch() {
        return headBranch;
    }
    
    /**
     * Returns the page url of this pull request .
     * @return url String
     */
    public String getPageUrl() {
        return pageUrl;
    }
    
    /**
     * Returns the source file download url of remote repository.
     * @return url String
     */
    public String getRepositorySrcDLUrl() {
        return repositorySrcDLUrl;
    }
    
    /**
     * Returns the source file download url of pull request local repository.
     * @return url String
     */
    public String getHeadRepositorySrcDLUrl() {
        return headRepositorySrcDLUrl;
    }
    
    /**
     * Returns all branches'name in the remote repository.
     * @return a List of branch name String
     */
    public List<String> getRepositoryBranches() {
        return repositoryBranches;
    }
    
    /**
     * Returns all branches' name in the pull request local repository.
     * @return a List of branch name String
     */
    public List<String> getHeadRepositoryBranches() {
        return headRepositoryBranches;
    }
    
    /**
     * Returns if complete participant information can access.
     * @return true if no data loss in information.
     */
    public boolean isParticipantRetrievable() {
        return participantRetrievable;
    }
    
    /**
     * Returns if complete issue comment information can access.
     * @return true if no data loss in information.
     */
    public boolean isIssueCommentRetrievable() {
        return issueCommentRetrievable;
    }
    
    /**
     * Returns if complete review comment information can access.
     * @return true if no data loss in information.
     */
    public boolean isReviewCommentRetrievable() {
        return reviewCommentRetrievable;
    }
    
    /**
     * Returns if complete issue event information can access.
     * @return true if no data loss in information.
     */
    public boolean isIssueEventRetrievable() {
        return issueEventRetrievable;
    }
    
    /**
     * Returns if complete review event information can access.
     * @return true if no data loss in information.
     */
    public boolean isReviewEventRetrievable() {
        return reviewEventRetrievable;
    }
    
    /**
     * Returns if complete commit information can access.
     * @return true if no data loss in information.
     */
    public boolean isCommitRetrievable() {
        return commitRetrievable;
    }
    
    /**
     * Returns if complete source code information can access.
     * @return true if no data loss in information.
     */
    public boolean isSourceCodeRetrievable() {
        return sourceCodeRetrievable;
    }
    
    /**
     * Returns if any data loss in pull request.
     * @return false if no data loss occurred.
     */
    public boolean isDeficient() {
        return false;
    }
    
    /**
     * Returns all participants in this pull request.
     * @return a Set of Participant.
     */
    public Set<Participant> getParticipants() {
        return participants;
    }
    
    /**
     * Returns the participant with the login name in this pull request.
     * @param  a participant's login name 
     * @return a Participant.
     */
    public Participant getParticipant(String name) {
        return participants.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
    }
    
    /**
     * Returns conversation in this pull request.
     * @return a Conversation.
     */
    public Conversation getConversation() {
        return conversation;
    }
    
    /**
     * Returns all commits in this pull request.
     * @return a List of Commit.
     */
    public List<Commit> getCommits() {
        return commits;
    }
    
    /**
     * Returns the commit having the sha in this pull request.
     * @param  a commit's full sha 
     * @return a Commit.
     */
    public Commit getCommit(String commitSha) {
        return commits.stream().filter(c -> c.getSha().equals(commitSha)).findFirst().orElse(null);
    }
    
    /**
     * Returns description in this pull request.
     * @return a Description.
     */
    public Description getDescription() {
        return description;
    }
    
    /**
     * Returns html description in this pull request.
     * @return a HTMLDescription.
     */
    public HTMLDescription getHtmlDescription() {
        return htmlDescription;
    }
    
    /**
     * Returns ChangeSummary in this pull request.
     * @return a ChangeSummary.
     */
    public ChangeSummary getChangeSummary() {
        return changeSummary;
    }
    
    /**
     * Returns added labels in this pull request.
     * @return a Set of Label.
     */
    public Set<Label> getAddedLabels() {
        return addedLabels;
    }
    
    /**
     * Returns deleted labels in this pull request.
     * @return a Set of Label.
     */
    public Set<Label> getRemovedLabels() {
        return removedLabels;
    }
    
    /**
     * Returns finally added labels in this pull request.
     * @return a Set of Label.
     */
    public Set<Label> getFinalLabels() {
        return finalLabels;
    }
}
