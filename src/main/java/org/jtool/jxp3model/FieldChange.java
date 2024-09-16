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
    
    private Set<CodeElement> callingMethodsBefore = new HashSet<>();
    private Set<CodeElement> callingMethodsAfter = new HashSet<>();
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
    
    public Set<CodeElement> getCallingMethodsBefore() {
        return callingMethodsBefore;
    }
    
    public Set<CodeElement> getCallingMethodsAfter() {
        return callingMethodsAfter;
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
