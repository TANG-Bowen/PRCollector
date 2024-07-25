package jwrmodel;

import java.util.ArrayList;
import java.util.Date;

public class Str_Commit {
	
	String sha;
	String shortSha;
	String authorInstId;
	Date commitDate;
	String message;
	String commitType;
	String instId;
	Str_FilesChanged filesChanged;
	ArrayList<Str_CIStatus> cis;
	

	public Str_Commit()
	{
		
	}

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public String getShortSha() {
		return shortSha;
	}

	public void setShortSha(String shortSha) {
		this.shortSha = shortSha;
	}

	public String getAuthorInstId() {
		return authorInstId;
	}

	public void setAuthorInstId(String authorInstId) {
		this.authorInstId = authorInstId;
	}

	public Date getCommitDate() {
		return commitDate;
	}

	public void setCommitDate(Date commitDate) {
		this.commitDate = commitDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public Str_FilesChanged getFilesChanged() {
		return filesChanged;
	}

	public void setFilesChanged(Str_FilesChanged filesChanged) {
		this.filesChanged = filesChanged;
	}
	
	public ArrayList<Str_CIStatus> getCis() {
		return cis;
	}

	public void setCis(ArrayList<Str_CIStatus> cis) {
		this.cis = cis;
	}

	public String getCommitType() {
		return commitType;
	}

	public void setCommitType(String commitType) {
		this.commitType = commitType;
	}
	
    
}
