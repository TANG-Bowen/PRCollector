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
        projectChanges.forEach(e -> e.print());
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public boolean hasJavaFile() {
        return hasJavaFile;
    }
    
    public Commit getCommit() {
        return commit;
    }
    
    public Set<ProjectChange> getProjectChanges() {
        return projectChanges;
    }
    
    public Set<FileChange> getFileChanges() {
        return fileChanges;
    }
    
    public List<DiffFile> getDiffFiles() {
        return diffFiles;
    }
}
