package org.jtool.jwrmodel;

import java.util.Set;

public class Str_FileChange {
    
    String prmodelId;
    
    String changeType;
    String name;
    String path;
    String sourceCodeBefore;
    String sourceCodeAfter;
    boolean isTest;
    
    Set<Str_ClassChange> classChanges;
}
