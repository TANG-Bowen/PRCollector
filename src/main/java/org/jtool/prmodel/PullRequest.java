package org.jtool.prmodel;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class PullRequest extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String id;
    private final String title;
    private final String repositoryName;
    private final String state;
    
    private final PRModelDate createDate;
    private final PRModelDate endDate;
    
    private final String mergeBranch;
    private final String headBranch;
    private final String pageUrl;
    private final String repositorySrcDLUrl;
    private final String headRepositorySrcDLUrl;
    
    private final boolean isMerged;
    private final boolean isStandardMerged;
    private final boolean sourceCodeRetrievable;
    
    private final List<String> repositoryBranches;
    private final List<String> headRepositoryBranches;
    
    /* -------- Attributes -------- */
    
    private Set<Participant> participants = new HashSet<>();
    private Conversation conversation;
    private List<Commit> commits = new ArrayList<>();
    private Description description;
    private HTMLDescription htmlDescription;
    private FilesChanged filesChanged;
    
    private Set<Label> addedLabels = new HashSet<>();
    private Set<Label> removedLabels = new HashSet<>();
    private Set<Label> finalLabels = new HashSet<>();
    
    public PullRequest(String id, String title, String repositoryName, String state,
            PRModelDate createDate, PRModelDate endDate,
            String mergeBranch, String headBranch, String pageUrl, String repositorySrcDLUrl,
            String headRepositorySrcDLUrl,
            boolean isMerged, boolean isStandardMerged, boolean sourceCodeRetrievable,
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
        this.sourceCodeRetrievable = sourceCodeRetrievable;
        
        this.repositoryBranches = repositoryBranches;
        this.headRepositoryBranches = headRepositoryBranches;
    }
    
    public void addParticipant(Participant participant) {
        participants.add(participant);
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
    
    private void printPRBase(String prefix) {
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
    
    public boolean isSourceCodeRetrievable() {
        return sourceCodeRetrievable;
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
    
    public Set<Participant> getParticipants() {
        return participants;
    }
    
    public Conversation getConversation() {
        return conversation;
    }
    
    public List<Commit> getCommits() {
        return commits;
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
