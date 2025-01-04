package org.jtool.prmodel;

import java.util.Set;
import java.util.HashSet;

import org.jtool.jxp3model.FileChange;

public class FilesChanged extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final boolean hasJavaFile;
    
    /* -------- Attributes -------- */
    
    private Set<DiffFile> diffFiles = new HashSet<>();
    private Set<FileChange> fileChanges = new HashSet<>();
    
    public FilesChanged(PullRequest pullRequest, boolean hasJavaFile) {
        super(pullRequest);
        this.hasJavaFile = hasJavaFile;
    }
    
    public void collect() {
        for (Commit commit : pullRequest.getTragetCommits()) {
            for (DiffFile diffFile : commit.getCodeChange().getDiffFiles()) {
                if (!diffFiles.contains(diffFile)) {
                    diffFiles.add(diffFile);
                }
            }
        }
        for (Commit commit : pullRequest.getTragetCommits()) {
            for (FileChange fileChange : commit.getCodeChange().getFileChanges()) {
                if (!fileChanges.contains(fileChange)) {
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
        diffFiles.forEach(e->e.print());
        System.out.println(prefix + "diffFiles(size) : "+diffFiles.size());
        fileChanges.forEach(e->e.print());
        System.out.println(prefix + "fileChanges(size) : "+fileChanges.size());
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
    public Set<DiffFile> getDiffFiles() {
        return diffFiles;
    }
    
    /**
     * Returns FileChanges inside.
     * @return a Set of FileChanges
     */
    public Set<FileChange> getFileChanges() {
        return fileChanges;
    }
}
