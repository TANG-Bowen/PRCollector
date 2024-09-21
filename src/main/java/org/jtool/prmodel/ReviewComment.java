package org.jtool.prmodel;

public class ReviewComment extends Action {
    
    /* -------- Attributes -------- */
    
    // PRModelDate date;
    // String body;
    
    /* -------- Attributes -------- */
    
    private MarkdownDoc markdownDoc;
    private CodeReviewSnippet snippet;
    
    public ReviewComment(PullRequest pullRequest, PRModelDate date, String body) {
        super(pullRequest, date, body);
    }
    
    public void setMarkdownDoc(MarkdownDoc markdownDoc) {
        this.markdownDoc = markdownDoc;
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
