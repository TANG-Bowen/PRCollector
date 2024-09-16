package org.jtool.prmodel;

import java.util.List;
import java.util.ArrayList;

public class Diff extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String sourceCodePathBefore;
    private final String sourceCodePathAfter;
    private final String sourceCodeDirNameBefore;
    private final String sourceCodeDirNameAfter;
    private boolean hasJavaFile = false;
    
    /* -------- Attributes -------- */
    
    private Commit commit;
    private List<DiffFile> diffFiles = new ArrayList<>();
    
    public Diff(PullRequest pullRequest, String pathBefore, String pathAfter,
            String dirNameBefore, String dirNameAfter) {
        super(pullRequest);
        this.sourceCodePathBefore = pathBefore;
        this.sourceCodePathAfter = pathAfter;
        this.sourceCodeDirNameBefore = dirNameBefore;
        this.sourceCodeDirNameAfter = dirNameAfter;
    }
    
    public void hasJavaFile(boolean bool) {
        this.hasJavaFile = bool;
    }
    
    public void setCommit(Commit commit) {
        this.commit = commit;
    }
    
    public void print() {
        String prefix = "Diff ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "sourceCodeBeforePath : " + sourceCodePathBefore);
        System.out.println(prefix + "sourceCodeAfterPath : " + sourceCodePathAfter);
        System.out.println(prefix + "sourceCodeBeforeDirName : " + sourceCodeDirNameBefore);
        System.out.println(prefix + "sourceCodeAfterDirName : " + sourceCodeDirNameAfter);
        System.out.println(prefix + "hasJavaFile : " + hasJavaFile);
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public String getSourceCodePathBefore() {
        return sourceCodePathBefore;
    }
    
    public String getSourceCodePathAfter() {
        return sourceCodePathAfter;
    }
    
    public String getSourceCodeDirNameBefore() {
        return sourceCodeDirNameBefore;
    }
    
    public String getSourceCodeDirNameAfter() {
        return sourceCodeDirNameAfter;
    }
    
    public boolean hasJavaFile() {
        return hasJavaFile;
    }
    
    public Commit getCommit() {
        return commit;
    }
    
    public List<DiffFile> getDiffFiles() {
        return diffFiles;
    }
}
