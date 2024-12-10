package org.jtool.jxp3model;

import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.PullRequest;

public class CodeElement extends PRElement {
    
    private final String stage;
    private final String qualifiedName;
    private final String sourceCode;
    
    public CodeElement(PullRequest pullrequest, String stage, String qualifiedName, String sourceCode) {
    	super (pullrequest);
        this.stage = stage;
        this.qualifiedName = qualifiedName;
        this.sourceCode = sourceCode;
    }
    
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof CodeElement) ? equals((CodeElement)obj) : false;
    }
    
    public boolean equals(CodeElement elem) {
        return elem != null && (this == elem || getIndex().equals(elem.getIndex()));
    }
    
    @Override
    public int hashCode() {
        return getIndex().hashCode();
    }
    
    public String getIndex() {
        return stage + "::" + qualifiedName;
    }
    
    public String getStage() {
        return stage;
    }
    
    public String getQualifiedName() {
        return qualifiedName;
    }
    
    public String getSourceCode() {
        return sourceCode;
    }
}
