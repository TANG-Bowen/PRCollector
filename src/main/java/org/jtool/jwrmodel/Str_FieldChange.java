/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.jwrmodel;

import java.util.Set;

public class Str_FieldChange {
    
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
    
    Set<Str_CodeElement> AccessingMethodsBefore;
    Set<Str_CodeElement> AccessingMethodsAfter;
    Set<Str_CodeElement> calledMethodsBefore;
    Set<Str_CodeElement> calledMethodsAfter;
}
