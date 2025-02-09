/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.jwrmodel;

import java.util.List;

public class Str_ChangeSummary {
    
    String prmodelId;
    
    boolean hasJavaFile;
    List<Str_DiffFile> diffFiles;
    List<Str_FileChange> fileChanges;
}
