package prmodel;

import java.util.Date;

import org.kohsuke.github.GHPullRequestReviewComment;

public class ReviewComment {
	
	Conversation conv;
	GHPullRequestReviewComment ghReviewComment;
	Participant commenter;
	Date commentDate;
	String body="";
	CodeReviewSnippet clr;
	MarkdownElement mde;
	String instId="";
	
	public ReviewComment(Conversation conv)
	{
		this.conv = conv;
	}
	
	void printReviewComment()
	{
		System.out.println();
		System.out.println("ReviewComment info : ");
		System.out.println("ReviewComment instId : "+this.instId);
		System.out.println("ReviewComment commenter : "+this.commenter.loginName);
		System.out.println("ReviewComment date : "+this.commentDate);
		System.out.println("ReviewComment body : "+this.body);
		System.out.println("ReviewComment codeLineReview instId : "+this.clr.instId);
		System.out.println("ReviewComment markdown : ");
		this.mde.printElements();
	}

	public Conversation getConv() {
		return conv;
	}

	public void setConv(Conversation conv) {
		this.conv = conv;
	}

	public GHPullRequestReviewComment getGhReviewComment() {
		return ghReviewComment;
	}

	public void setGhReviewComment(GHPullRequestReviewComment ghReviewComment) {
		this.ghReviewComment = ghReviewComment;
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

	public CodeReviewSnippet getClr() {
		return clr;
	}

	public void setClr(CodeReviewSnippet clr) {
		this.clr = clr;
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
