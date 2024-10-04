package org.jtool.jxp3model;

import java.util.Set;
import java.util.HashSet;

import org.jtool.prmodel.CodeChange;
import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.PullRequest;

public class ProjectChange extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String name;
    private final String path;
    
    /* -------- Attributes -------- */
    
    private CodeChange codeChange;
    private Set<FileChange> fileChanges = new HashSet<>();
    
    public void setCodeChange(CodeChange codeChange) {
        this.codeChange = codeChange;
    }
    
    public ProjectChange(PullRequest pullRequest, String name, String path) {
        super(pullRequest);
        this.name = name;
        this.path = path;
    }
    
    public void print() {
        String prefix = "ProjectChange ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "name : " + name);
        System.out.println(prefix + "path : " + path);
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ProjectChange) ? equals((ProjectChange)obj) : false;
    }
    
    public boolean equals(ProjectChange change) {
        return change != null && (this == change || path.equals(change.path));
    }
    
    @Override
    public int hashCode() {
        return path.hashCode();
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public String getName() {
        return name;
    }
    
    public String getPath() {
        return path;
    }
    
    public CodeChange getCodeChange() {
        return codeChange;
    }
    
    public Set<FileChange> getFileChanges() {
        return fileChanges;
    }
}
