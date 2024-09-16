package org.jtool.jxp3model;

import java.util.Set;
import java.util.HashSet;

import org.jtool.prmodel.Commit;
import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.PullRequest;

public class CodeChange extends PRElement {
    
    /* -------- Attributes -------- */
    
    /* -------- Attributes -------- */
    
    private Commit commit;
    private Set<ProjectChange> projectChanges = new HashSet<>();
    private Set<FileChange> fileChanges = new HashSet<>();
    
    public CodeChange(PullRequest pullRequest) {
        super(pullRequest);
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
        String prefix = "CodeElementChangePerCommit ";
        System.out.println();
        System.out.println(prefix + super.toString());
        projectChanges.forEach(e -> e.print());
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public Commit getCommit() {
        return commit;
    }
    
    public Set<ProjectChange> getProjectChanges() {
        return projectChanges;
    }
    
    public Set<FileChange> getFileChanges() {
        return fileChanges;
    }
}
