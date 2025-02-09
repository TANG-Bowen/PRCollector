/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.jwrmodel;

import java.util.List;
import java.util.Set;

public class Str_CodeChange {
    
    String prmodelId;
    
    boolean hasJavaFile;
    
    List<Str_DiffFile> diffFiles;
    Set<Str_ProjectChange> projectChanges;
}
