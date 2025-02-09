/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.prmodel;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import org.jtool.jxp3model.ProjectChange;
import org.jtool.jxp3model.FileChange;

public class CodeChange extends PRElement {
    
    /* -------- Attributes -------- */
    
    private boolean hasJavaFile = false;
    
    /* -------- Attributes -------- */
    
    private Commit commit;
    private List<DiffFile> diffFiles = new ArrayList<>();
    private Set<ProjectChange> projectChanges = new HashSet<>();
    private Set<FileChange> fileChanges = new HashSet<>();
    
    public CodeChange(PullRequest pullRequest) {
        super(pullRequest);
    }
    
    public void hasJavaFile(boolean bool) {
        this.hasJavaFile = bool;
    }
    
    public void setCommit(Commit commit) {
        this.commit = commit;
    }
    
    public void setFileChanges() {
        for (ProjectChange projectChange : projectChanges) {
            fileChanges.addAll(projectChange.getFileChanges());
        }
    }
    
    public void print() {
        String prefix = "CodeChange ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "hasJavaFile : " + hasJavaFile);
        diffFiles.forEach(e->e.print());
        System.out.println("DiffFiles size in CodeChange : "+diffFiles.size());
        projectChanges.forEach(e -> e.print());
        System.out.println("ProjectChanges size in CodeChange : "+projectChanges.size());
        fileChanges.forEach(e->e.print());
        System.out.println("fileChanges size in CodeChange : "+fileChanges.size());
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    /**
     * If there is at least one java file in the CodeChange.
     * @return true if has at least one java file
     */
    public boolean hasJavaFile() {
        return hasJavaFile;
    }
    
    /**
     * Returns the commit which this CodeChange belongs.
     * @return the commit
     */
    public Commit getCommit() {
        return commit;
    }
    
    /**
     * Returns ProjectChanges in this CodeChange.
     * @return a Set of ProjectChanges
     */
    public Set<ProjectChange> getProjectChanges() {
        return projectChanges;
    }
    
    /**
     * Returns FileChanges in this CodeChange.
     * @return a Set of FileChanges
     */
    public Set<FileChange> getFileChanges() {
        return fileChanges;
    }
    
    /**
     * Returns DiffFiles in this CodeChange.
     * @return a List of DiffFiles
     */
    public List<DiffFile> getDiffFiles() {
        return diffFiles;
    }
}
