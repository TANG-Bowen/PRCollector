package org.jtool.prmodel;

import java.util.Set;
import java.util.HashSet;

import org.jtool.jxp3model.FileChange;

public class AllFilesChanged extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final boolean hasJavaFile;
    
    /* -------- Attributes -------- */
    
    private Set<DiffFile> diffFiles = new HashSet<>();
    private Set<FileChange> fileChanges = new HashSet<>();
    
    public AllFilesChanged(PullRequest pullRequest, boolean hasJavaFile) {
        super(pullRequest);
        this.hasJavaFile = hasJavaFile;
    }
    
    public void collect() {
        for (Commit commit : pullRequest.getTragetCommits()) {
            for (DiffFile diffFile : commit.getDiff().getDiffFiles()) {
                if (diffFiles.contains(diffFile)) {
                    diffFiles.add(diffFile);
                }
            }
        }
        for (Commit commit : pullRequest.getTragetCommits()) {
            for (FileChange fileChange : commit.getCodeChange().getFileChanges()) {
                if (fileChanges.contains(fileChange)) {
                    fileChanges.add(fileChange);
                }
            }
        }
    }
    
    public void print() {
        String prefix = "FilesChangedInfo ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "hasJavaFile : " + hasJavaFile);
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public boolean hasJavaFile() {
        return hasJavaFile;
    }
    
    public Set<DiffFile> getDiffFiles() {
        return diffFiles;
    }
    
    public Set<FileChange> getFileChanges() {
        return fileChanges;
    }
}
