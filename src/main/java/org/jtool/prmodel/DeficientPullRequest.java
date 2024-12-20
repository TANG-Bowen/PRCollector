package org.jtool.prmodel;

public class DeficientPullRequest extends PullRequest {
    
    /* -------- Attributes -------- */
    
    private final String lossType;
    private final String exceptionOutput;
    
    /* -------- Attributes -------- */
    
    public DeficientPullRequest(String lossType, String exceptionOutput, 
            String id, String title, String repositoryName, String state,
            PRModelDate createDate, PRModelDate endDate,
            String mergeBranch, String headBranch, String pageUrl, String repositorySrcDLUrl,
            String headRepositorySrcDLUrl,
            boolean isMerged, boolean isStandardMerged, boolean sourceCodeRetrievable) {
        super(id, title, repositoryName, state, createDate, endDate, 
                mergeBranch, headBranch, pageUrl, repositorySrcDLUrl, headRepositorySrcDLUrl,
                isMerged, isStandardMerged, sourceCodeRetrievable,
                null, null);
        this.lossType = lossType;
        this.exceptionOutput = exceptionOutput;
    }
    
    @Override
    public void print() {
        String prefix = "DeficientPullRequest ";
        printPRBase(prefix);
        System.out.println();
        System.out.println("lossType : " + this.lossType);
        System.out.println("exceptionOutput : " + this.exceptionOutput);
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public String getLossType() {
        return lossType;
    }
    
    public String getExceptionOutput() {
        return exceptionOutput;
    }
}
