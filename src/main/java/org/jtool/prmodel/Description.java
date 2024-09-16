package org.jtool.prmodel;

public class Description extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String body;
    
    /* -------- Attributes -------- */
    
    private MarkdownDoc markdownDoc;
    
    public void setMardownDoc(MarkdownDoc markdownDoc) {
        this.markdownDoc = markdownDoc;
    }
    
    public Description(PullRequest pullRequest, String body) {
        super(pullRequest);
        this.body = body;
    }
    
    public void print() {
        String prefix = "Description ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "body : " + body);
        markdownDoc.print();
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public String getBody() {
        return body;
    }
    
    public MarkdownDoc getMarkdownDoc() {
        return markdownDoc;
    }
}
