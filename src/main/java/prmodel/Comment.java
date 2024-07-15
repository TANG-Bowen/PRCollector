package prmodel;

import java.util.Date;

import org.kohsuke.github.GHIssueComment;

public class Comment {
	
	Conversation conv;
	GHIssueComment ghComment;
	Participant commenter;
	Date commentDate;
	String body="";
	MarkdownElement mde;
	
	String instId="";
	
	public Comment(Conversation conv)
	{
		this.conv = conv;
	}
	
	void printComment()
	{
		System.out.println();
		System.out.println("Comment info : ");
		System.out.println("Comment instId : "+ this.instId);
		System.out.println("Comment commenter : "+this.commenter.loginName);
		System.out.println("Comment date : "+this.commentDate);
		System.out.println("Comment body : "+this.body);
		System.out.println("Comment markdown : ");
		this.mde.printElements();
	}

	GHIssueComment getGHComment()
	{
		return this.ghComment;
	}

	public Conversation getConv() {
		return conv;
	}

	public void setConv(Conversation conv) {
		this.conv = conv;
	}

	public GHIssueComment getGhComment() {
		return ghComment;
	}

	public void setGhComment(GHIssueComment ghComment) {
		this.ghComment = ghComment;
	}

	public Participant getCommenter() {
		return commenter;
	}

	public void setCommenter(Participant commenter) {
		this.commenter = commenter;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public MarkdownElement getMde() {
		return mde;
	}

	public void setMde(MarkdownElement mde) {
		this.mde = mde;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}	
	
	
	
	
	
	
	

}
