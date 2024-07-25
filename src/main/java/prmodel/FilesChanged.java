package prmodel;

import java.io.File;
import java.util.ArrayList;

import jxp3model.CodeElement;

public class FilesChanged {
	
	PullRequest pr;
	Commit cmit;
	ArrayList<DiffFileUnit> diffFileUnits;
	File srcAfterDir;
	File srcBeforeDir;
	String srcBeforeDirName="";
	String srcAfterDirName="";
	boolean hasJavaSrcFile = false;
	String instId ="";
	
	CodeElement cde;
	
	public FilesChanged(PullRequest pr)
	{
		diffFileUnits = new ArrayList<>();
		this.pr =pr;
	}

	void printFilesChanged()
	{
		System.out.println();
		System.out.println("FilesChanged instId: "+this.instId);
	
		System.out.println(" srcBeforeDirName : "+ this.srcBeforeDirName);	
		System.out.println(" srcAfterDirName : "+ this.srcAfterDirName);
		System.out.println(" hasJavaSrc : "+ this.hasJavaSrcFile);
		if(this.diffFileUnits.isEmpty()==false)
		{
			for(DiffFileUnit dfui : this.diffFileUnits)
			{
				dfui.printDiffFileUnit();
			}
		}
		System.out.println(" codeElement : ");
		this.cde.printCodeElement();
	  
	}
	
	void printPrFilesChanged()
	{
		System.out.println();
		System.out.println("PrFilesChanged instId: "+this.instId);
		System.out.println(" hasJavaSrc : "+ this.hasJavaSrcFile);
		if(this.diffFileUnits.isEmpty()==false)
		{
			for(DiffFileUnit dfui : this.diffFileUnits)
			{
				dfui.printDiffFileUnit();
			}
			System.out.println(" Total DifffileUnits : "+this.diffFileUnits.size());
		}
		this.cde.printPRCodeElement();
	}

	public PullRequest getPr() {
		return pr;
	}

	public void setPr(PullRequest pr) {
		this.pr = pr;
	}

	public Commit getCmit() {
		return cmit;
	}

	public void setCmit(Commit cmit) {
		this.cmit = cmit;
	}

	public ArrayList<DiffFileUnit> getDiffFileUnits() {
		return diffFileUnits;
	}

	public void setDiffFileUnits(ArrayList<DiffFileUnit> diffFileUnits) {
		this.diffFileUnits = diffFileUnits;
	}

	public File getSrcAfterDir() {
		return srcAfterDir;
	}

	public void setSrcAfterDir(File srcAfterDir) {
		this.srcAfterDir = srcAfterDir;
	}

	public File getSrcBeforeDir() {
		return srcBeforeDir;
	}

	public void setSrcBeforeDir(File srcBeforeDir) {
		this.srcBeforeDir = srcBeforeDir;
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

	public CodeElement getCde() {
		return cde;
	}

	public void setCde(CodeElement cde) {
		this.cde = cde;
	}
	
    
}
