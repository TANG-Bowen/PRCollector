package jwrmodel;

import java.util.Date;

public class Str_CIStatus {
	
	String context;
	String state;
	String description;
	String targetUrl;
	Date createDate;
	Date updateDate;
	String commitInstId;
	

	Str_CIStatus()
	{
		
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
	
	public String getCommitInstId() {
		return commitInstId;
	}

	public void setCommitInstId(String commitInstId) {
		this.commitInstId = commitInstId;
	}

}
