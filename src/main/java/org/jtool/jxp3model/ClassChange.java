/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.jxp3model;

import java.util.Set;
import java.util.HashSet;

import org.jtool.prmodel.PullRequest;

public class ClassChange extends CommonChange {
    
    /* -------- Attributes -------- */
    
    // String changeType;
    // String name;
    // String qualifiedName;
    // String type;
    // CodeElement before;
    // CodeElement after;
    // boolean isTest;
    
    private Set<CodeElement> afferentClassesBefore = new HashSet<>();
    private Set<CodeElement> afferentClassesAfter = new HashSet<>();
    private Set<CodeElement> efferentClassesBefore = new HashSet<>();
    private Set<CodeElement> efferentClassesAfter = new HashSet<>();
    
    /* -------- Attributes -------- */
    
    private Set<FieldChange> fieldChanges = new HashSet<>();
    private Set<MethodChange> methodChanges = new HashSet<>();
    
    public ClassChange(PullRequest pullRequest, String changeType,
            String name, String qualifiedName, String type,
            String sourceCodeBefore, String sourceCodeAfter) {
        super(pullRequest, changeType, name, qualifiedName, type, sourceCodeBefore, sourceCodeAfter);
    }
    
    public void print() {
        String prefix = "ClassChange ";
        System.out.println();
        super.print(prefix);
        
        fieldChanges.forEach(e -> e.print());
        System.out.println("fieldChanges size in ClassChange : "+fieldChanges.size());
        methodChanges.forEach(e -> e.print());
        System.out.println("methodChanges size in ClassChange : "+methodChanges.size());
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    /**
     * Returns afferent classes of this ClassChange in the pre files.
     * @return a Set of CodeElements
     */
    public Set<CodeElement> getAfferentClassesBefore() {
        return afferentClassesBefore;
    }
    
    /**
     * Returns afferent classes of this ClassChange in the post files.
     * @return a Set of CodeElements
     */
    public Set<CodeElement> getAfferentClassesAfter() {
        return afferentClassesAfter;
    }
    
    /**
     * Returns efferent classes of this ClassChange in the pre files.
     * @return a Set of CodeElements
     */
    public Set<CodeElement> getEfferentClassesBefore() {
        return efferentClassesBefore;
    }
    
    /**
     * Returns efferent classes of this ClassChange in the post files.
     * @return a Set of CodeElements
     */
    public Set<CodeElement> getEfferentClassesAfter() {
        return efferentClassesAfter;
    }
    
    /**
     * Returns FieldChanges in this ClassChange.
     * @return a Set of FieldChanges
     */
    public Set<FieldChange> getFieldChanges() {
        return fieldChanges;
    }
    
    /**
     * Returns MethodChanges in this ClassChange.
     * @return a Set of MethodChanges
     */
    public Set<MethodChange> getMethodChanges() {
        return methodChanges;
    }
}
