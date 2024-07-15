package prmodel;

import java.util.ArrayList;
import java.util.Date;

public class Commit {
	
	PullRequest pr;
	String sha="";
	String shortSha="";
	Participant author;
	Date commitDate;
	String message="";
	String commitType="";
	String instId ="";
	
	FilesChanged flcg;
	
	ArrayList<CIStatus> cis;
	
	public Commit(PullRequest pr)
	{
		this.pr = pr;
		cis = new ArrayList<>();
	}
	

	void printCommit()
	{
		System.out.println();
		System.out.println("Commit instId : "+this.instId);
		System.out.println("Commit sha : "+this.sha);
		System.out.println("Commit shortSha : "+this.shortSha);
		System.out.println("Commit author : "+ this.author.loginName);
		System.out.println("Commit date : "+ this.commitDate);
		System.out.println("Commit message : "+this.message);
		System.out.println("Commit commitType : "+this.commitType);
		flcg.printFilesChanged();
		System.out.println(" CI info : ");
		if(!this.cis.isEmpty())
		{
			for(CIStatus cisi : this.cis)
			{
				cisi.printCIStatus();
			}
		}
	}

	public String getCommitType() {
		return commitType;
	}


	public void setCommitType(String commitType) {
		this.commitType = commitType;
	}


	public PullRequest getPr() {
		return pr;
	}

	public void setPr(PullRequest pr) {
		this.pr = pr;
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

	public Participant getAuthor() {
		return author;
	}

	public void setAuthor(Participant author) {
		this.author = author;
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

	public FilesChanged getFlcg() {
		return flcg;
	}

	public void setFlcg(FilesChanged flcg) {
		this.flcg = flcg;
	}
	
	public ArrayList<CIStatus> getCis() {
		return cis;
	}

	public void setCis(ArrayList<CIStatus> cis) {
		this.cis = cis;
	}
	
	
	

}
