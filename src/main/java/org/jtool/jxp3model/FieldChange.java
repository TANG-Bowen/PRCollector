package org.jtool.jxp3model;

import java.util.Set;
import java.util.HashSet;

import org.jtool.prmodel.PullRequest;

public class FieldChange extends CommonChange {
    
    /* -------- Attributes -------- */
    
    // String changeType;
    // String name;
    // String qualifiedName;
    // String type;
    // CodeElement before;
    // CodeElement after;
    // boolean isTest;
    
    private Set<CodeElement> accessingMethodsBefore = new HashSet<>();
    private Set<CodeElement> accessingMethodsAfter = new HashSet<>();
    private Set<CodeElement> calledMethodsBefore = new HashSet<>();
    private Set<CodeElement> calledMethodsAfter = new HashSet<>();
    
    /* -------- Attributes -------- */
    
    private ClassChange classChange;
    
    public FieldChange(PullRequest pullRequest, String changeType,
            String name, String qualifiedName, String type,
            String sourceCodeBefore, String sourceCodeAfter) {
        super(pullRequest, changeType, name, qualifiedName, type, sourceCodeBefore, sourceCodeAfter);
    }
    
    public void setClassChange(ClassChange classChange) {
        this.classChange = classChange;
    }
    
    public void print() {
        String prefix = "FieldChange ";
        System.out.println();
        super.print(prefix);
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public Set<CodeElement> getAccessingMethodsBefore() {
        return accessingMethodsBefore;
    }
    
    public Set<CodeElement> getAccessingMethodsAfter() {
        return accessingMethodsAfter;
    }
    
    public Set<CodeElement> getCalledMethodsBefore() {
        return calledMethodsBefore;
    }
    
    public Set<CodeElement> getCalledMethodsAfter() {
        return calledMethodsAfter;
    }
    
    public ClassChange getClassChange() {
        return classChange;
    }
}
