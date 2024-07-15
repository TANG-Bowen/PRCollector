package jwrmodel;

import java.util.ArrayList;

public class Str_FilesChanged {
	

	ArrayList<Str_DiffFileUnit> str_diffFileUnits;
	String srcBeforeDirName;
	String srcAfterDirName;
	boolean hasJavaSrcFile;
	String instId;
	
	Str_CodeElement str_codeElement;
	
    public Str_FilesChanged()
    {
    	
    }


	public ArrayList<Str_DiffFileUnit> getStr_diffFileUnits() {
		return str_diffFileUnits;
	}

	public void setStr_diffFileUnits(ArrayList<Str_DiffFileUnit> str_diffFileUnits) {
		this.str_diffFileUnits = str_diffFileUnits;
	}

	public String getSrcBeforeDirName() {
		return srcBeforeDirName;
	}

	public void setSrcBeforeDirName(String srcBeforeDirName) {
		this.srcBeforeDirName = srcBeforeDirName;
	}

	public String getSrcAfterDirName() {
		return srcAfterDirName;
	}

	public void setSrcAfterDirName(String srcAfterDirName) {
		this.srcAfterDirName = srcAfterDirName;
	}

	public boolean isHasJavaSrcFile() {
		return hasJavaSrcFile;
	}

	public void setHasJavaSrcFile(boolean hasJavaSrcFile) {
		this.hasJavaSrcFile = hasJavaSrcFile;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public Str_CodeElement getStr_codeElement() {
		return str_codeElement;
	}

	public void setStr_codeElement(Str_CodeElement str_codeElement) {
		this.str_codeElement = str_codeElement;
	}
    
    
}
