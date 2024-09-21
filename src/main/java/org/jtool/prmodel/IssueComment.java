package org.jtool.prmodel;

public class IssueComment extends Action {
    
    /* -------- Attributes -------- */
    
    // PRModelDate date;
    // String body;
    
    /* -------- Attributes -------- */
    
    private MarkdownDoc markdownDoc;
    
    public IssueComment(PullRequest pullRequest, PRModelDate date, String body) {
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
