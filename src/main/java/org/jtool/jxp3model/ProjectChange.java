/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

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
        fileChanges.forEach(e->e.print());
        System.out.println("fileChanges size in projectChange : "+fileChanges.size());
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
    
    /**
     * Returns name of this ProjectChange.
     * @return a name String
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns path of this ProjectChange.
     * @return a path String
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Returns CodeChange contains this ProjectChange.
     * @return a CodeChange
     */
    public CodeChange getCodeChange() {
        return codeChange;
    }
    
    /**
     * Returns FileChanges in  this ProjectChange.
     * @return a Set of FileChanges
     */
    public Set<FileChange> getFileChanges() {
        return fileChanges;
    }
}
