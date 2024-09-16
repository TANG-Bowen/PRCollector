package org.jtool.jwrmodel;

import java.util.Set;

public class Str_FileChange {
    
    String prmodelId;
    
    String changeType;
    String name;
    String pathBefore;
    String pathAfter;
    String sourceCodeBefore;
    String sourceCodeAfter;
    boolean isTest;
    
    Set<Str_ClassChange> classChanges;
}
