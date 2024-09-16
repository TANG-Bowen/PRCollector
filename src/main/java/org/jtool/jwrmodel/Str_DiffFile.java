package org.jtool.jwrmodel;

import java.util.List;

public class Str_DiffFile {
    
    String prmodelId;
    
    String pathBefore;
    String pathAfter;
    String relativePath;
    String changeType;
    String bodyAll;
    String bodyAdd;
    String bodyDel;
    String sourceCodeBefore;
    String sourceCodeAfter;
    boolean isJavaFile;
    boolean isTest;
    
    List<Str_DiffLine> diffLines;
}
