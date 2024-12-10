package org.jtool.jwrmodel;

import java.util.Set;

public class Str_MethodChange {
    
    String prmodelId;
    
    String changeType;
    String name;
    String qualifiedName;
    String type;
    String sourceCodeBefore;
    String sourceCodeAfter;
    boolean isTest;
    
    Set<String> callingMethodsBeforeIndices;
    Set<String> callingMethodsAfterIndices;
    Set<String> calledMethodsBeforeIndices;
    Set<String> calledMethodsAfterIndices;
<<<<<<< HEAD
    
    Set<Str_CodeElement> callingMethodsBefore;
    Set<Str_CodeElement> callingMethodsAfter;
    Set<Str_CodeElement> calledMethodsBefore;
    Set<Str_CodeElement> calledMethodsAfter;
=======
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
}
