/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.jwrmodel;

import java.util.Set;

public class Str_ProjectChange {
    
    String prmodelId;
    
    String name;
    String path;
    
    Set<Str_FileChange> fileChanges;
}
