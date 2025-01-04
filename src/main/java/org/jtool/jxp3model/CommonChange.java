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
    
    /**
     * Returns change type of this CommonChange.
     * @return a changType String
     */
    public String getChangeType() {
        return changeType;
    }
    
    /**
     * Returns name of this CommonChange.
     * @return a name String
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns qualified name of this CommonChange.
     * @return a qualified name String
     */
    public String getQualifiedName() {
        return qualifiedName;
    }
    
    /**
     * Returns return type of this CommonChange.
     * @return a type String
     */
    public String getType() {
        return type;
    }
    
    /**
     * Returns CodeElement of this CommonChange in pre files.
     * @return a CodeElement
     */
    public CodeElement getCodeElementBefore() {
        return before;
    }
    
    /**
     * Returns CodeElement of this CommonChange in post files.
     * @return a CodeElement
     */
    public CodeElement getCodeElementAfter() {
        return after;
    }
    
    /**
     * Returns the qualified name of CodeElement in this CommonChange in pre files.
     * @return a qualified name String
     */
    public String getQualifiedNameBefore() {
        return before.getQualifiedName();
    }
    
    /**
     * Returns the qualified name of CodeElement in this CommonChange in post files.
     * @return a qualified name String
     */
    public String getQualifiedNameAfter(){
        return after.getQualifiedName();
    }
    
    /**
     * Returns the source code of CodeElement in this CommonChange in pre files.
     * @return a source code String
     */
    public String getSourceCodeBefore() {
        return before.getSourceCode();
    }
    
    /**
     * Returns the source code of CodeElement in this CommonChange in post files.
     * @return a source code String
     */
    public String getSourceCodeAfter() {
        return after.getSourceCode();
    }
    
    /**
     * Returns if this CommonChange relates to tests.
     * @return true if relation to test exists
     */
    public boolean isTest() {
        return isTest;
    }
    
    /**
     * Returns the FileChange which contains this CommonChange.
     * @return a FileChange
     */
    public FileChange getFileChange() {
        return fileChange;
    }
}
