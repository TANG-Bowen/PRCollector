package org.jtool.jxp3model;

public class CodeElement {
    
    private final String stage;
    private final String qualifiedName;
    private final String sourceCode;
    
    public CodeElement(String stage, String qualifiedName, String sourceCode) {
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
