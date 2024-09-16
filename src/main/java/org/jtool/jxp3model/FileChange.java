package org.jtool.jxp3model;

import java.util.Set;
import java.util.HashSet;

import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.PullRequest;

public class FileChange extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String changeType;
    private final String name;
    private final String pathBefore;
    private final String pathAfter;
    private final String sourceCodeBefore;
    private final String sourceCodeAfter;
    private boolean isTest = false;
    
    /* -------- Attributes -------- */
    
    private CodeChange codeChange;
    private ProjectChange projectChange;
    private Set<ClassChange> classChanges = new HashSet<>();
    
    public FileChange(PullRequest pullRequest, String changeType,
            String name, String pathBefore, String pathAfter,
            String sourceCodeBefore, String sourceCodeAfter) {
        super(pullRequest);
        this.changeType = changeType;
        this.name = name;
        this.pathBefore = pathBefore;
        this.pathAfter = pathAfter;
        this.sourceCodeBefore = sourceCodeBefore;
        this.sourceCodeAfter = sourceCodeAfter;
    }
    
    public void setCodeChange(CodeChange codeChange) {
        this.codeChange = codeChange;
    }
    
    public void setProjectChange(ProjectChange projectChange) {
        this.projectChange = projectChange;
    }
    
    public void setTest(boolean bool) {
        this.isTest = bool;
    }
    
    public void print() {
        String prefix = "FileChange ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "changeType : " + changeType);
        System.out.println(prefix + "name : " + name);
        System.out.println(prefix + "pathBefore : " + pathBefore);
        System.out.println(prefix + "pathAfter : " + pathAfter);
        System.out.println(prefix + "isTest : " + isTest);
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ProjectChange) ? equals((FileChange)obj) : false;
    }
    
    public boolean equals(FileChange change) {
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
    
    public String getChangeType() {
        return changeType;
    }
    
    public String getName() {
        return name;
    }
    public String getPathBefore() {
        return pathBefore;
    }
    
    public String getPathAfter() {
        return pathAfter;
    }
    
    public String getSourceCodeBefore() {
        return sourceCodeBefore;
    }
    
    public String getSourceCodeAfter() {
        return sourceCodeAfter;
    }
    
    public boolean isTest() {
        return isTest;
    }
    
    public CodeChange getCodeChange() {
        return codeChange;
    }
    
    public ProjectChange getProjectChange() {
        return projectChange;
    }
    
    public Set<ClassChange> getClassChanges() {
        return classChanges;
    }
}
