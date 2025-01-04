package org.jtool.jxp3model;

import java.util.Set;
import java.util.HashSet;

import org.jtool.prmodel.CodeChange;
import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.PullRequest;

public class FileChange extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String changeType;
    private final String name;
    private final String path;
    private final String sourceCodeBefore;
    private final String sourceCodeAfter;
    private boolean isTest = false;
    
    /* -------- Attributes -------- */
    
    private CodeChange codeChange;
    private ProjectChange projectChange;
    private Set<ClassChange> classChanges = new HashSet<>();
    
    public FileChange(PullRequest pullRequest, String changeType,
            String name, String path, String sourceCodeBefore, String sourceCodeAfter) {
        super(pullRequest);
        this.changeType = changeType;
        this.name = name;
        this.path = path;
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
        System.out.println(prefix + "path : " + path);
        System.out.println(prefix + "isTest : " + isTest);
        classChanges.forEach(e->e.print());
        System.out.println("classChanges size in fileChange : "+classChanges.size());
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ProjectChange) ? equals((FileChange)obj) : false;
    }
    
    public boolean equals(FileChange change) {
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
     * Returns change type of this FileChange.
     * @return a changType String
     */
    public String getChangeType() {
        return changeType;
    }
    
    /**
     * Returns name of this FileChange.
     * @return a name String
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns absolute path of this FileChange.
     * @return a path String
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Returns source code of this FileChange in the pre file.
     * @return a source code String
     */
    public String getSourceCodeBefore() {
        return sourceCodeBefore;
    }
    
    /**
     * Returns source code of this FileChange in the post file.
     * @return a source code String
     */
    public String getSourceCodeAfter() {
        return sourceCodeAfter;
    }
    
    /**
     * Returns if this FileChange relates to tests.
     * @return true if relation to test exists
     */
    public boolean isTest() {
        return isTest;
    }
    
    /**
     * Returns CodeChange relates this FileChange.
     * @return a CodeChange
     */
    public CodeChange getCodeChange() {
        return codeChange;
    }
    
    /**
     * Returns ProjectChange relates this FileChange.
     * @return a ProjectChange
     */
    public ProjectChange getProjectChange() {
        return projectChange;
    }
    
    /**
     * Returns ClassChange in this FileChange.
     * @return a ClassChange
     */
    public Set<ClassChange> getClassChanges() {
        return classChanges;
    }
}
