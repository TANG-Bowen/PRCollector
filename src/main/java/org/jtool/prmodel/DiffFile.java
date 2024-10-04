package org.jtool.prmodel;

import java.util.List;
import java.util.ArrayList;

public class DiffFile extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String changeType;
    private final String path;
    private final String bodyAll;
    private final String bodyAdd;
    private final String bodyDel;
    private final String sourceCodeBefore;
    private final String sourceCodeAfter;
    private final boolean isJavaFile;
    boolean isTest = false;
    
    /* -------- Attributes -------- */
    
    private CodeChange codeChange;
    private List<DiffLine> diffLines = new ArrayList<>();
    
    public DiffFile(PullRequest pullRequest, String changeType, String path,
            String bodyAll, String bodyAdd, String bodyDel,
            String sourceCodeBefore, String sourceCodeAfter, boolean isJavaFile) {
        super(pullRequest);
        this.changeType = changeType;
        this.path = path;
        this.bodyAll = bodyAll;
        this.bodyAdd = bodyAdd;
        this.bodyDel = bodyDel;
        this.sourceCodeBefore = sourceCodeBefore;
        this.sourceCodeAfter = sourceCodeAfter;
        this.isJavaFile = isJavaFile;
    }
    
    public void setTest(boolean bool) {
        this.isTest = bool;
    }
    
    public void setCodeChange(CodeChange codeChange) {
        this.codeChange = codeChange;
    }
    
    public void print() {
        String prefix = "DiffFile ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "changeType : " + changeType);
        System.out.println(prefix + "path : " + path);
        System.out.println(prefix + "isJavaFile : " + isJavaFile);
        System.out.println(prefix + "isTest : " + isTest);
        System.out.println(prefix + "bodyAll : " + bodyAll);
        System.out.println(prefix + "bodyAdd : "+ bodyAdd);
        System.out.println(prefix + "bodyDel : " + bodyDel );
        diffLines.forEach(e -> e.print());
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public String getPath() {
        return path;
    }
    
    public String getChangeType() {
        return changeType;
    }
    
    public String getBodyAll() {
        return bodyAll;
    }
    
    public String getBodyAdd() {
        return bodyAdd;
    }
    
    public String getBodyDel() {
        return bodyDel;
    }
    
    public String getSourceCodeBefore() {
        return sourceCodeBefore;
    }
    
    public String getSourceCodeAfter() {
        return sourceCodeAfter;
    }
    
    public boolean isJavaFile() {
        return isJavaFile;
    }
    
    public boolean isTest() {
        return isTest;
    }
    
    public CodeChange getCodeChange() {
        return codeChange;
    }
    
    public List<DiffLine> getDiffLines() {
        return diffLines;
    }
}
