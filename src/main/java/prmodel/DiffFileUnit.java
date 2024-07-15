package prmodel;

import java.util.ArrayList;

public class DiffFileUnit {
	
	String absolutePathBefore="";
	String absolutePathAfter="";
	String relativePath="";
	String diffBodyAll="";
	String diffBodyAdd="";
	String diffBodyDelete="";
	String fileType ="";//add, delete, change
	boolean isJavaSrcFile=false;
	boolean isTest=false;//build after jxp3Module building
	
	ArrayList<DiffLineUnit> diffLineUnits;
	
	public DiffFileUnit()
	{
		diffLineUnits = new ArrayList<>();
	}
	
	void printDiffFileUnit()
	{
		System.out.println();
		System.out.println(" DFU absolutePathBefore : " + this.absolutePathBefore);
		System.out.println(" DFU absolutePathAfter : " + this.absolutePathAfter);
		System.out.println(" DFU relativePath : " + this.relativePath);
		System.out.println(" DFU fileType : " + this.fileType);
		System.out.println(" DFU isJavaSrcFile : " + this.isJavaSrcFile);
		System.out.println(" DFU isTest : " + this.isTest);
		System.out.println(" DFU diffBodyAll : "+ this.diffBodyAll);
		System.out.println(" DFU diffBodyAdd(+++) : "+ this.diffBodyAdd);
		System.out.println(" DFU diffBodyDelete(---) : " + this.diffBodyDelete );
		for(DiffLineUnit dflui : this.diffLineUnits)
		{
			dflui.printDiffLineUnit();
		}
	}

	public String getAbsolutePathBefore() {
		return absolutePathBefore;
	}

	public void setAbsolutePathBefore(String absolutePathBefore) {
		this.absolutePathBefore = absolutePathBefore;
	}

	public String getAbsolutePathAfter() {
		return absolutePathAfter;
	}

	public void setAbsolutePathAfter(String absolutePathAfter) {
		this.absolutePathAfter = absolutePathAfter;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getDiffBodyAll() {
		return diffBodyAll;
	}

	public void setDiffBodyAll(String diffBodyAll) {
		this.diffBodyAll = diffBodyAll;
	}

	public String getDiffBodyAdd() {
		return diffBodyAdd;
	}

	public void setDiffBodyAdd(String diffBodyAdd) {
		this.diffBodyAdd = diffBodyAdd;
	}

	public String getDiffBodyDelete() {
		return diffBodyDelete;
	}

	public void setDiffBodyDelete(String diffBodyDelete) {
		this.diffBodyDelete = diffBodyDelete;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public boolean isJavaSrcFile() {
		return isJavaSrcFile;
	}

	public void setJavaSrcFile(boolean isJavaSrcFile) {
		this.isJavaSrcFile = isJavaSrcFile;
	}

	public boolean isTest() {
		return isTest;
	}

	public void setTest(boolean isTest) {
		this.isTest = isTest;
	}

	public ArrayList<DiffLineUnit> getDiffLineUnits() {
		return diffLineUnits;
	}

	public void setDiffLineUnits(ArrayList<DiffLineUnit> diffLineUnits) {
		this.diffLineUnits = diffLineUnits;
	}
	
	
	
}
