package org.jtool.prmodel;

import java.util.List;

import org.jtool.jxp3model.FileChange;

import java.util.ArrayList;

public class ChangeSummary extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final boolean hasJavaFile;
    
    /* -------- Attributes -------- */
    
    private List<DiffFile> diffFiles = new ArrayList<>();
    private List<FileChange> fileChanges = new ArrayList<>();
    
    public ChangeSummary(PullRequest pullRequest, boolean hasJavaFile) {
        super(pullRequest);
        this.hasJavaFile = hasJavaFile;
    }
    
    public void print() {
        String prefix = "ChangeSummary ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "hasJavaFile : " + hasJavaFile);
        diffFiles.forEach(e -> e.print());
        System.out.println(prefix + "diffFiles(size) : " + diffFiles.size());
        fileChanges.forEach(e->e.print());
        System.out.println(prefix + "fileChanges(size) : " + fileChanges.size());
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
     * @return a List of DiffFiles
     */
    public List<DiffFile> getDiffFiles() {
        return diffFiles;
    }
    
    /**
     * Returns FileChanges inside.
     * @return a List of FileChanges
     */
    public List<FileChange> getFileChanges(){
    	return fileChanges;
    }
    
}
