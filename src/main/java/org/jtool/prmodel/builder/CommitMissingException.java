/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.prmodel.builder;

public class CommitMissingException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public CommitMissingException(String message) {
        super(message);
    }
}
