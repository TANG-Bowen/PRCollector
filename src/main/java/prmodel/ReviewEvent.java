package prmodel;

import java.util.Date;

import org.kohsuke.github.GHPullRequestReview;

public class ReviewEvent {
	
	Conversation conv;
	GHPullRequestReview ghReview;
	Participant actor;
	Date eventDate;
	String body="";
	MarkdownElement mde;
	String instId = "";
	
	public ReviewEvent(Conversation conv)
	{
		this.conv = conv;
	}
	
	void printReviewEvent()
	{
		System.out.println();
		System.out.println("ReviewEvent info : ");
		System.out.println("ReviewEvent instId : "+this.instId);
		System.out.println("ReviewEvent actor : "+this.actor.loginName);
		System.out.println("ReviewEvent date : "+this.eventDate);
		System.out.println("ReviewEvent body : "+this.body);
		System.out.println("ReviewEvent markdown : ");
		this.mde.printElements();
	}

	public Conversation getConv() {
		return conv;
	}

	public void setConv(Conversation conv) {
		this.conv = conv;
	}

	public GHPullRequestReview getGhReview() {
		return ghReview;
	}

	public void setGhReview(GHPullRequestReview ghReview) {
		this.ghReview = ghReview;
	}

	public Participant getActor() {
		return actor;
	}

	public void setActor(Participant actor) {
		this.actor = actor;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
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
