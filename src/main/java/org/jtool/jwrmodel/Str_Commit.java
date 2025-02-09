/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.jwrmodel;

import java.util.List;

public class Str_Commit {
    
    String prmodelId;
    
    String sha;
    String shortSha;
    String date;
    String type;
    String message;
    
    String committerId;
    Str_CodeChange codeChange;
    List<Str_CIStatus> ciStatus;
}
