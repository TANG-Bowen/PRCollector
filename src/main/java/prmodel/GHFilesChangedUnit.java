package prmodel;

public class GHFilesChangedUnit {
	
	String filePath ="";
	String fileContent="";
	
	GHFilesChangedUnit(String filePath, String fileContent)
	{
		this.filePath = filePath;
		this.fileContent = fileContent;
	}

	public String getFilepath() {
		return filePath;
	}

	public void setFilepath(String filepath) {
		this.filePath = filepath;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	
	

}
