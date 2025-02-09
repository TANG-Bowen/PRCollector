/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

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
    
    /**
     * Returns accessing methods in this FieldChange in pre files.
     * @return a Set of CodeElements
     */
    public Set<CodeElement> getAccessingMethodsBefore() {
        return accessingMethodsBefore;
    }
    
    /**
     * Returns accessing methods in this FieldChange in post files.
     * @return a Set of CodeElements
     */
    public Set<CodeElement> getAccessingMethodsAfter() {
        return accessingMethodsAfter;
    }
    
    /**
     * Returns called methods in this FieldChange in pre files.
     * @return a Set of CodeElements
     */
    public Set<CodeElement> getCalledMethodsBefore() {
        return calledMethodsBefore;
    }
    
    /**
     * Returns called methods in this FieldChange in post files.
     * @return a Set of CodeElements
     */
    public Set<CodeElement> getCalledMethodsAfter() {
        return calledMethodsAfter;
    }
    
    /**
     * Returns ClassChange which contains this FieldChange.
     * @return a ClassChange
     */
    public ClassChange getClassChange() {
        return classChange;
    }
}
