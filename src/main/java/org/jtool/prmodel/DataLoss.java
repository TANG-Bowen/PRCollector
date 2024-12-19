package org.jtool.prmodel;

public class DataLoss {
	
	private final String lossType;
	private final String exceptionOutput;
	private final String id;
    private final String title;
    private final String repositoryName;
    private final String state;
    
    private final PRModelDate createDate;
    private final PRModelDate endDate;
    
    private final String mergeBranch;
    private final String headBranch;
    private final String pageUrl;
    private final String repositorySrcDLUrl;
    private final String headRepositorySrcDLUrl;
    
    private final boolean isMerged;
    private final boolean isStandardMerged;
    private final boolean sourceCodeRetrievable;
    
	public DataLoss(String lossType, String exceptionOutput, String id, String title, String repositoryName,
			String state, PRModelDate createDate, PRModelDate endDate, String mergeBranch, String headBranch,
			String pageUrl, String repositorySrcUrl, String headRepositorySrcUrl, boolean isMerged,
			boolean isStandardMerged, boolean sourceCodeRetrievable) {
		this.lossType = lossType;
		this.exceptionOutput = exceptionOutput;
		this.id = id;
		this.title = title;
		this.repositoryName = repositoryName;
		this.state = state;
		this.createDate = createDate;
		this.endDate = endDate;
		this.mergeBranch = mergeBranch;
		this.headBranch = headBranch;
		this.pageUrl = pageUrl;
		this.repositorySrcDLUrl = repositorySrcUrl;
		this.headRepositorySrcDLUrl = headRepositorySrcUrl;
		this.isMerged = isMerged;
		this.isStandardMerged = isStandardMerged;
		this.sourceCodeRetrievable = sourceCodeRetrievable;

	}
    
	public void print() {
		System.out.println();
		System.out.println("Data loss Pull Request : ");
		System.out.println("lossType : " + this.lossType);
		System.out.println("exceptionOutput : " + this.exceptionOutput);
		System.out.println("id : " + this.id);
		System.out.println("title : " + this.title);
		System.out.println("repositoryName : " + this.repositoryName);
		System.out.println("state : " + this.state);
		System.out.println("createDate : " + this.createDate.toString());
		System.out.println("endDate : " + this.endDate.toString());
		System.out.println("mergeBranch : " + this.mergeBranch);
		System.out.println("headBranch : " + this.headBranch);
		System.out.println("pageUrl : " + this.pageUrl);
		System.out.println("repositorySrcDLUrl : " + this.repositorySrcDLUrl);
		System.out.println("headRepositorySrcDLUrl : " + this.headRepositorySrcDLUrl);
		System.out.println("isMerged : " + this.isMerged);
		System.out.println("isStandardMeged : " + this.isStandardMerged);
		System.out.println("sourceCodeRetrievable : " + this.sourceCodeRetrievable);
	}

	public String getLossType() {
		return lossType;
	}

	public String getExceptionOutput() {
		return exceptionOutput;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public String getState() {
		return state;
	}

	public PRModelDate getCreateDate() {
		return createDate;
	}

	public PRModelDate getEndDate() {
		return endDate;
	}

	public String getMergeBranch() {
		return mergeBranch;
	}

	public String getHeadBranch() {
		return headBranch;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public String getRepositorySrcDLUrl() {
		return repositorySrcDLUrl;
	}

	public String getHeadRepositorySrcDLUrl() {
		return headRepositorySrcDLUrl;
	}

	public boolean isMerged() {
		return isMerged;
	}

	public boolean isStandardMerged() {
		return isStandardMerged;
	}

	public boolean isSourceCodeRetrievable() {
		return sourceCodeRetrievable;
	}
    
    

}
