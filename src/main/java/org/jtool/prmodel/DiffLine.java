package org.jtool.prmodel;

public class DiffLine extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String changeType;
    private final String text;
    
    /* -------- Attributes -------- */
    
    private DiffFile diffFile;
    
    public DiffLine(PullRequest pullRequest, String changeType, String text) {
        super(pullRequest);
        this.changeType = changeType;
        this.text = text;
    }
    
    public void setDiffFile(DiffFile diffFile) {
        this.diffFile = diffFile;
    }
    
    public void print() {
        String prefix = "DiffLine ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "changeType : " + changeType);
        System.out.println(prefix + "text : " + text);
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public String getChangeType() {
        return changeType;
    }
    
    public String getText() {
        return text;
    }
    
    public DiffFile getDiffFile() {
        return diffFile;
    }
}
