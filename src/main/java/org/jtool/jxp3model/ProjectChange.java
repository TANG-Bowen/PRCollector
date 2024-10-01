package org.jtool.jxp3model;

import java.util.Set;
import java.util.HashSet;

import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.PullRequest;

public class ProjectChange extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String nameBefore;
    private final String nameAfter;
    private final String pathBefore;
    private final String pathAfter;
    
    /* -------- Attributes -------- */
    
    private CodeChange codeChange;
    private Set<FileChange> fileChanges = new HashSet<>();
    
    public void setCodeChange(CodeChange codeChange) {
        this.codeChange = codeChange;
    }
    
    public ProjectChange(PullRequest pullRequest,
            String nameBefore, String nameAfter, String pathBefore, String pathAfter) {
        super(pullRequest);
        this.nameBefore = nameBefore;
        this.nameAfter = nameAfter;
        this.pathBefore = pathBefore;
        this.pathAfter = pathAfter;
        
    }
    
    public void print() {
        String prefix = "ProjectChange ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "nameBefore : " + nameBefore);
        System.out.println(prefix + "nameAfter : " + nameAfter);
        System.out.println(prefix + "pathBefore : " + pathBefore);
        System.out.println(prefix + "pathAfter : " + pathAfter);
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ProjectChange) ? equals((ProjectChange)obj) : false;
    }
    
    public boolean equals(ProjectChange change) {
        return change != null && (this == change ||
              (pathBefore.equals(change.pathBefore) && pathAfter.equals(change.pathAfter)));
    }
    
    @Override
    public int hashCode() {
        String str = pathBefore+ "/" + pathAfter;
        return str.hashCode();
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public String getNameBefore() {
        return nameBefore;
    }
    
    public String getnameAfter() {
        return nameAfter;
    }
    
    public String getPathBefore() {
        return pathBefore;
    }
    
    public String getpathAfter() {
        return pathAfter;
    }
    
    public CodeChange getCodeChange() {
        return codeChange;
    }
    
    public Set<FileChange> getFileChanges() {
        return fileChanges;
    }
}
