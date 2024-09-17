package org.jtool.prmodel;

public class ReviewComment extends PRAction {
    
    /* -------- Attributes -------- */
    
    // PRModelDate date;
    // String body;
    
    /* -------- Attributes -------- */
    
    private MarkdownDoc markdownDoc;
    private CodeReviewSnippet snippet;
    
    public ReviewComment(PullRequest pullRequest, PRModelDate date, String body) {
        super(pullRequest, date, body);
    }
    
    public void setCodeReviewSnippet(CodeReviewSnippet snippet) {
        this.snippet = snippet;
    }
    
    public void print() {
        super.print("ReviewComment ");
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public MarkdownDoc getMarkdownDoc() {
        return markdownDoc;
    }
    
    public CodeReviewSnippet getCodeReviewSnippet() {
        return snippet;
    }
}
