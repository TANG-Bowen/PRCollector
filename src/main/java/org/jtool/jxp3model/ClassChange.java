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
        methodChanges.forEach(e -> e.print());
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public Set<CodeElement> getAfferentClassesBefore() {
        return afferentClassesBefore;
    }
    
    public Set<CodeElement> getAfferentClassesAfter() {
        return afferentClassesAfter;
    }
    
    public Set<CodeElement> getEfferentClassesBefore() {
        return efferentClassesBefore;
    }
    
    public Set<CodeElement> getEfferentClassesAfter() {
        return efferentClassesAfter;
    }
    
    public Set<FieldChange> getFieldChanges() {
        return fieldChanges;
    }
    
    public Set<MethodChange> getMethodChanges() {
        return methodChanges;
    }
}
