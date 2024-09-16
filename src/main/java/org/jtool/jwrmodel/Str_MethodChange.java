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
}
