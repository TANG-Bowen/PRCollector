package org.jtool.prmodel;

import java.util.List;

public class DeficientPullRequest extends PullRequest {
    
    /* -------- Attributes -------- */
    
    private final String exceptionOutput;
    
    /* -------- Attributes -------- */
    
    public DeficientPullRequest(String id, String title, String repositoryName, String state,
            PRModelDate createDate, PRModelDate endDate,
            String mergeBranch, String headBranch, String pageUrl,
            String repositorySrcDLUrl, String headRepositorySrcDLUrl,
            boolean isMerged, boolean isStandardMerged,
            List<String> repositoryBranches, List<String> headRepositoryBranches, String exceptionOutput) {
        super(id, title, repositoryName, state,
                createDate, endDate, 
                mergeBranch, headBranch, pageUrl,
                repositorySrcDLUrl, headRepositorySrcDLUrl,
                isMerged, isStandardMerged,
                repositoryBranches, headRepositoryBranches);
        this.exceptionOutput = exceptionOutput;
    }
    
    public DeficientPullRequest(PullRequest pr, String exceptionOutput) {
        this(pr.id, pr.title, pr.repositoryName, pr.state,
                pr.createDate, pr.endDate, 
                pr.mergeBranch, pr.headBranch, pr.pageUrl,
                pr.repositorySrcDLUrl, pr.headRepositorySrcDLUrl,
                pr.isMerged, pr.isStandardMerged,
                pr.repositoryBranches, pr.headRepositoryBranches, exceptionOutput);
        
        this.participantRetrievable = pr.participantRetrievable;
        this.issueEventRetrievable = pr.issueEventRetrievable;
        this.issueCommentRetrievable = pr.issueCommentRetrievable;
        this.reviewEventRetrievable = pr.reviewEventRetrievable;
        this.reviewCommentRetrievable = pr.reviewCommentRetrievable;
        this.commitRetrievable = pr.commitRetrievable;
        this.sourceCodeRetrievable = pr.sourceCodeRetrievable;
        
        this.participants = pr.participants;
        this.conversation = pr.conversation;
        this.commits = pr.commits;
        this.description = pr.description;
        this.htmlDescription = pr.htmlDescription;
        this.changeSummary = pr.changeSummary;
        
        this.addedLabels = pr.addedLabels;
        this.removedLabels = pr.removedLabels;
        this.finalLabels = pr.finalLabels;
    }
    
    @Override
    public void print() {
        String prefix = "DeficientPullRequest ";
        printPRBase(prefix);
        System.out.println();
        System.out.println("exceptionOutput : " + this.exceptionOutput);
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    /**
     * If the PR model is deficient.
     * @return true for deficient judgment
     */
    @Override
    public boolean isDeficient() {
        return true;
    }
    
    /**
     * If the instance leading to nonCategorized deficient case.
     * @return false if any retrievable flag is false
     */
    public boolean nonCategorized() {
        return participantRetrievable &&
               issueEventRetrievable &&
               issueCommentRetrievable &&
               reviewEventRetrievable &&
               reviewCommentRetrievable &&
               commitRetrievable &&
               sourceCodeRetrievable;
    }
    
    /**
     * Returns exception information.
     * @return exception output String
     */
    public String getExceptionOutput() {
        return exceptionOutput;
    }
}
