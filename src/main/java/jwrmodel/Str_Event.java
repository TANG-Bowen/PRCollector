package jwrmodel;

import java.util.Date;

public class Str_Event {
	
	String actorInstId;
	Date commentDate;
	String body;
	String instId;
	
	public Str_Event()
	{
		
	}

	public String getActorInstId() {
		return actorInstId;
	}

	public void setActorInstId(String actorInstId) {
		this.actorInstId = actorInstId;
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

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}
	
	

}
