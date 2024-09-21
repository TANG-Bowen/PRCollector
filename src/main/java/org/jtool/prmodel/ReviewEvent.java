package org.jtool.prmodel;

public class ReviewEvent extends Action {
    
    /* -------- Attributes -------- */
    
    // PRModelDate date;
    // String body;
    
    /* -------- Attributes -------- */
    
    private MarkdownDoc markdownDoc;
    
    public ReviewEvent(PullRequest pullRequest, PRModelDate date, String body) {
        super(pullRequest, date, body);
    }
    
    public void setMarkdownDoc(MarkdownDoc markdownDoc) {
        this.markdownDoc = markdownDoc;
    }
    
    public void print() {
        super.print("Review ");
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public MarkdownDoc getMarkdownDoc() {
        return markdownDoc;
    }
}
