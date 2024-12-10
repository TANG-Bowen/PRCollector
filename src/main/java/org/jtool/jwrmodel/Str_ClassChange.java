package org.jtool.jwrmodel;

import java.util.Set;

public class Str_ClassChange {
    
    String prmodelId;
    
    String changeType;
    String name;
    String qualifiedName;
    String type;
    String sourceCodeBefore;
    String sourceCodeAfter;
    boolean isTest;
    
//    Set<String> afferentClassesBeforeIndices;
//    Set<String> afferentClassesAfterIndices;
//    Set<String> efferentClassesBeforeIndices;
//    Set<String> efferentClassesAfterIndices;
    
    Set<Str_CodeElement> afferentClassesBefore;
    Set<Str_CodeElement> afferentClassesAfter;
    Set<Str_CodeElement> efferentClassesBefore;
    Set<Str_CodeElement> efferentClassesAfter;
    
    Set<Str_FieldChange> fieldChanges;
    Set<Str_MethodChange> methodChanges;
}
