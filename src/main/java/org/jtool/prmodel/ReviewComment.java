package org.jtool.prmodel;

public class ReviewComment extends Comment {
    
    /* -------- Attributes -------- */
    
    // PRModelDate date;
    // String body;
    
    /* -------- Attributes -------- */
    
    private CodeReviewSnippet snippet;
    
    public ReviewComment(PullRequest pullRequest, PRModelDate date, String body) {
        super(pullRequest, date, body);
    }
    
    public void setCodeReviewSnippet(CodeReviewSnippet snippet) {
        this.snippet = snippet;
    }
    
    @Override
    public void print() {
        super.print("ReviewComment ");
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public CodeReviewSnippet getCodeReviewSnippet() {
        return snippet;
    }
}
