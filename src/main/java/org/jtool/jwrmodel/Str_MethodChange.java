/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

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
    
    Set<Str_CodeElement> callingMethodsBefore;
    Set<Str_CodeElement> callingMethodsAfter;
    Set<Str_CodeElement> calledMethodsBefore;
    Set<Str_CodeElement> calledMethodsAfter;
}
