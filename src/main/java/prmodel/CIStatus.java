package prmodel;

import java.util.Date;

public class CIStatus {
	
	String context="";
	String state="";
	String description="";
	String targetUrl="";
	Date createDate;
	Date updateDate;
	Commit cmit;

	public CIStatus(Commit cmt)
	{
		this.cmit = cmt;
	}

	void printCIStatus()
	{
		System.out.println();
		System.out.println("CIStatus info : ");
		System.out.println(" Ci context : "+this.context);
		System.out.println(" Ci state : "+this.state);
		System.out.println(" Ci description : "+ this.description);
		System.out.println(" Ci target Url : "+this.targetUrl);
		System.out.println(" Ci createDate : "+this.createDate);
		System.out.println(" Ci updateDate : "+this.updateDate);
		System.out.println(" Ci commitSha : "+this.cmit.getSha());
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	public Commit getCmt() {
		return cmit;
	}

	public void setCmt(Commit cmt) {
		this.cmit = cmt;
	}
	
}
