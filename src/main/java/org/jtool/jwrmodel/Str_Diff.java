package org.jtool.jwrmodel;

import java.util.List;

public class Str_Diff {
    
    String prmodelId;
    
    String sourceCodePathBefore;
    String sourceCodePathAfter;
    String sourceCodeDirNameBefore;
    String sourceCodeDirNameAfter;
    boolean hasJavaFile;
    
    List<Str_DiffFile> diffFiles;
}
