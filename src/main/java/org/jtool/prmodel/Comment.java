package org.jtool.prmodel;

public class Comment extends PRAction {
    
    /* -------- Attributes -------- */
    
    // PRModelDate date;
    // String body;
    
    /* -------- Attributes -------- */
    
    private MarkdownDoc markdownDoc;
    
    public Comment(PullRequest pullRequest, PRModelDate date, String body) {
        super(pullRequest, date, body);
    }
    
    public void setMarkdownDoc(MarkdownDoc markdownDoc) {
        this.markdownDoc = markdownDoc;
    }
    
    public void print() {
        super.print("Comment ");
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public MarkdownDoc getMarkdownDoc() {
        return markdownDoc;
    }
}
