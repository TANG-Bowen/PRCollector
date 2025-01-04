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
    
    public boolean hasJavaFile() {
        return hasJavaFile;
    }
    
    public List<DiffFile> getDiffFiles() {
        return diffFiles;
    }
}
