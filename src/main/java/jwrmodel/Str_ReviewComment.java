package jwrmodel;

import java.util.Date;

public class Str_ReviewComment {
	
	String participantInstId;
	Date commentDate;
	String body;
	String codeLineReviewInstId;
	Str_MarkdownElement str_markdownElement;
	String instId;
	
	public Str_ReviewComment()
	{
		
	}

	public String getParticipantInstId() {
		return participantInstId;
	}

	public void setParticipantInstId(String participantInstId) {
		this.participantInstId = participantInstId;
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
	
    

	public String getCodeLineReviewInstId() {
		return codeLineReviewInstId;
	}

	public void setCodeLineReviewInstId(String codeLineReviewInstId) {
		this.codeLineReviewInstId = codeLineReviewInstId;
	}

	public Str_MarkdownElement getStr_markdownElement() {
		return str_markdownElement;
	}

	public void setStr_markdownElement(Str_MarkdownElement str_markdownElement) {
		this.str_markdownElement = str_markdownElement;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}
	
	

}
