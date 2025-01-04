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
        System.out.println(prefix + "diffLine(size) : "+diffLines.size());
        diffLines.forEach(e -> e.print());
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    /**
     * Returns relative path of this DiffFile.
     * @return String of relative path
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Returns change type of this DiffFile.
     * @return String of change type
     */
    public String getChangeType() {
        return changeType;
    }
    
    /**
     * Returns full body include addition and deletion.
     * @return full body String
     */
    public String getBodyAll() {
        return bodyAll;
    }
    
    /**
     * Returns addition body.
     * @return addition body String only
     */
    public String getBodyAdd() {
        return bodyAdd;
    }
    
    /**
     * Returns deletion body.
     * @return deletion body String only
     */
    public String getBodyDel() {
        return bodyDel;
    }
    
    /**
     * Returns source body in pre-file.
     * @return source body String
     */
    public String getSourceCodeBefore() {
        return sourceCodeBefore;
    }
    
    /**
     * Returns source body in post-file.
     * @return source body String
     */
    public String getSourceCodeAfter() {
        return sourceCodeAfter;
    }
    
    /**
     * If changed file is a java file.
     * @return true if the file in this DiffFile a java file
     */
    public boolean isJavaFile() {
        return isJavaFile;
    }
    
    /**
     * If changed file is for testing.
     * @return true if the file in this DiffFile a java test file
     */
    public boolean isTest() {
        return isTest;
    }
    
    /**
     * Returns CodeChange in this DiffFile.
     * @return the CodeChange
     */
    public CodeChange getCodeChange() {
        return codeChange;
    }
    
    /**
     * Returns diff line info in this DiffFile.
     * @return a List of DiffLines include added lines and deleted lines
     */
    public List<DiffLine> getDiffLines() {
        return diffLines;
    }
}
