package org.jtool.prmodel;

import java.util.List;
import java.util.ArrayList;

public class DiffFile extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String pathBefore;
    private final String pathAfter;
    private final String relativePath;
    private final String changeType;
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
    
    public DiffFile(PullRequest pullRequest, String pathBefore, String pathAfter,
            String relativePath, String changeType,
            String bodyAll, String bodyAdd, String bodyDel,
            String sourceCodeBefore, String sourceCodeAfter, boolean isJavaFile) {
        super(pullRequest);
        this.pathBefore = pathBefore;
        this.pathAfter = pathAfter;
        this.relativePath = relativePath;
        this.changeType = changeType;
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
        System.out.println(prefix + "pathBefore : " + pathBefore);
        System.out.println(prefix + "pathAfter : " + pathAfter);
        System.out.println(prefix + "relativePath : " + relativePath);
        System.out.println(prefix + "changeType : " + changeType);
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
    
    public String getPathBefore() {
        return pathBefore;
    }
    
    public String getPathAfter() {
        return pathAfter;
    }
    
    public String getRelativePath() {
        return relativePath;
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
