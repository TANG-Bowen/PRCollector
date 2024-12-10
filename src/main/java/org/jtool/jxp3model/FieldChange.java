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
    
<<<<<<< HEAD
    private Set<CodeElement> accessingMethodsBefore = new HashSet<>();
    private Set<CodeElement> accessingMethodsAfter = new HashSet<>();
=======
    private Set<CodeElement> callingMethodsBefore = new HashSet<>();
    private Set<CodeElement> callingMethodsAfter = new HashSet<>();
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
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
    
<<<<<<< HEAD
    public Set<CodeElement> getAccessingMethodsBefore() {
        return accessingMethodsBefore;
    }
    
    public Set<CodeElement> getAccessingMethodsAfter() {
        return accessingMethodsAfter;
=======
    public Set<CodeElement> getCallingMethodsBefore() {
        return callingMethodsBefore;
    }
    
    public Set<CodeElement> getCallingMethodsAfter() {
        return callingMethodsAfter;
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
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
