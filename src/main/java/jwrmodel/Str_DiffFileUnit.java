package jwrmodel;

import java.util.ArrayList;

public class Str_DiffFileUnit {
	
	String absolutePathBefore="";
	String absolutePathAfter="";
	String relativePath="";
	String diffBodyAll="";
	String diffBodyAdd="";
	String diffBodyDelete="";
	String fileType ="";//add, delete, change
	boolean isJavaSrcFile=false;
	boolean isTest=false;//build after jxp3Module building
	
	ArrayList<Str_DiffLineUnit> diffLineUnits;
	
	public Str_DiffFileUnit()
	{
		
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

	public ArrayList<Str_DiffLineUnit> getDiffLineUnits() {
		return diffLineUnits;
	}

	public void setDiffLineUnits(ArrayList<Str_DiffLineUnit> diffLineUnits) {
		this.diffLineUnits = diffLineUnits;
	}
	
	

}
