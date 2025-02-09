/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

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
    /**
     * Returns change type of this DiffLine add or delete.
     * @return changeType String
     */
    public String getChangeType() {
        return changeType;
    }
    
    /**
     * Returns line text of this DiffLine.
     * @return the text String
     */
    public String getText() {
        return text;
    }
    
    /**
     * Returns the DiffFile belongs.
     * @return the DiffFile
     */
    public DiffFile getDiffFile() {
        return diffFile;
    }
}
