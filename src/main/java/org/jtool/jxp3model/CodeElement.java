package org.jtool.jxp3model;

<<<<<<< HEAD
import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.PullRequest;

public class CodeElement extends PRElement {
=======
public class CodeElement {
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
    
    private final String stage;
    private final String qualifiedName;
    private final String sourceCode;
    
<<<<<<< HEAD
    public CodeElement(PullRequest pullrequest, String stage, String qualifiedName, String sourceCode) {
    	super (pullrequest);
=======
    public CodeElement(String stage, String qualifiedName, String sourceCode) {
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
        this.stage = stage;
        this.qualifiedName = qualifiedName;
        this.sourceCode = sourceCode;
    }
    
<<<<<<< HEAD
    
=======
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
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
