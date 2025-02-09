/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.jxp3model;

import java.util.HashSet;
import java.util.Set;

import org.jtool.prmodel.PullRequest;

public class MethodChange extends CommonChange {
    
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
    
    public MethodChange(PullRequest pullRequest, String changeType,
            String name, String qualifiedName, String type,
            String sourceCodeBefore, String sourceCodeAfter) {
        super(pullRequest, changeType, name, qualifiedName, type, sourceCodeBefore, sourceCodeAfter);
    }
    
    public void setClassChange(ClassChange classChange) {
        this.classChange = classChange;
    }
    
    public void print() {
        String prefix = "MethodChange ";
        System.out.println();
        super.print(prefix);
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    /**
     * Returns calling methods of this MethodChange in the pre files.
     * @return a Set of CodeElements
     */
    public Set<CodeElement> getCallingMethodsBefore() {
        return callingMethodsBefore;
    }
    
    /**
     * Returns calling methods of this MethodChange in the post files.
     * @return a Set of CodeElements
     */
    public Set<CodeElement> getCallingMethodsAfter() {
        return callingMethodsAfter;
    }
    
    /**
     * Returns called methods of this MethodChange in the pre files.
     * @return a Set of CodeElements
     */
    public Set<CodeElement> getCalledMethodsBefore() {
        return calledMethodsBefore;
    }
    
    /**
     * Returns called methods of this MethodChange in the post files.
     * @return a Set of CodeElements
     */
    public Set<CodeElement> getCalledMethodsAfter() {
        return calledMethodsAfter;
    }
    
    /**
     * Returns ClassChange contains this MethodChange.
     * @return a ClassChange
     */
    public ClassChange getClassChange() {
        return classChange;
    }
}
