package org.jtool.jwrmodel;

import java.util.Set;

public class Str_ProjectChange {
    
    String prmodelId;
    
    String changeType;
    String nameBefore;
    String nameAfter;
    String pathBefore;
    String pathAfter;
    
    Set<Str_FileChange> fileChanges;
}
