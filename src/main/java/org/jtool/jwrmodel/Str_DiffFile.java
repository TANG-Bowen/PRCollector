/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.jwrmodel;

import java.util.List;

public class Str_DiffFile {
    
    String prmodelId;
    
    String changeType;
    String path;
    String bodyAll;
    String bodyAdd;
    String bodyDel;
    String sourceCodeBefore;
    String sourceCodeAfter;
    boolean isJavaFile;
    boolean isTest;
    
    List<Str_DiffLine> diffLines;
}
