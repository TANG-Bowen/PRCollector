package org.jtool.prmodel;

import java.util.List;
import java.util.ArrayList;

public class FilesChanged extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final boolean hasJavaFile;
    
    /* -------- Attributes -------- */
    
    private List<DiffFile> diffFiles = new ArrayList<>();
    
    public FilesChanged(PullRequest pullRequest, boolean hasJavaFile) {
        super(pullRequest);
        this.hasJavaFile = hasJavaFile;
    }
    
    public void print() {
        String prefix = "FilesChangedInfo ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "hasJavaFile : " + hasJavaFile);
        diffFiles.forEach(e -> e.print());
        System.out.println(prefix + "diffFiles(size) : " + diffFiles.size());
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    /**
     * If at least one java file in this FilesChange.
     * @return true if java file element in the Set of DiffFile
     */
    public boolean hasJavaFile() {
        return hasJavaFile;
    }
    
    /**
     * Returns DiffFiles inside.
     * @return a Set of DiffFiles
     */
    public List<DiffFile> getDiffFiles() {
        return diffFiles;
    }
}
