package org.jtool.jxp3model;

import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.PullRequest;

public abstract class CommonChange extends PRElement {
    
    /* -------- Attributes -------- */
    
    protected final String changeType;
    protected final String name;
    protected final String qualifiedName;
    protected final String type;
    protected final CodeElement before;
    protected final CodeElement after;
    protected boolean isTest = false;
    
    /* -------- Attributes -------- */
    
    private FileChange fileChange;
    
    protected CommonChange(PullRequest pullRequest, String changeType,
            String name, String qualifiedName, String type,
            String sourceCodeBefore, String sourceCodeAfter) {
        super(pullRequest);
        this.changeType = changeType;
        this.name = name;
        this.qualifiedName = qualifiedName;
        this.type = type;
        
        this.before = new CodeElement(pullRequest,PRElement.BEFORE, qualifiedName, sourceCodeBefore);
        this.after = new CodeElement(pullRequest, PRElement.AFTER, qualifiedName, sourceCodeAfter);
    }
    
    public void setFileChange(FileChange fileChange) {
        this.fileChange = fileChange;
    }
    
    public void setTest(boolean bool) {
        this.isTest = bool;
    }
    
    protected void print(String prefix) {
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "changeType : " + changeType);
        System.out.println(prefix + "name : " + name);
        System.out.println(prefix + "qualifiedName : " + qualifiedName);
        System.out.println(prefix + "type : " + type);
        System.out.println(prefix + "isTest : " + isTest);
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ProjectChange) ? equals((CommonChange)obj) : false;
    }
    
    public boolean equals(CommonChange change) {
        return change != null && (this == change || qualifiedName.equals(change.qualifiedName));
    }
    
    @Override
    public int hashCode() {
        return qualifiedName.hashCode();
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public String getChangeType() {
        return changeType;
    }
    
    public String getName() {
        return name;
    }
    
    public String getQualifiedName() {
        return qualifiedName;
    }
    
    public String getType() {
        return type;
    }
    
    public CodeElement getCodeElementBefore() {
        return before;
    }
    
    public CodeElement getCodeElementAfter() {
        return after;
    }
    
    public String getQualifiedNameBefore() {
        return before.getQualifiedName();
    }
    
    public String getQualifiedNameAfter(){
        return after.getQualifiedName();
    }
    
    public String getSourceCodeBefore() {
        return before.getSourceCode();
    }
    
    public String getSourceCodeAfter() {
        return after.getSourceCode();
    }
    
    public boolean isTest() {
        return isTest;
    }
    
    public FileChange getFileChange() {
        return fileChange;
    }
}
