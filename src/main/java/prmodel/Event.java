package prmodel;

import java.util.Date;

import org.kohsuke.github.GHIssueEvent;

public class Event {
	
	Conversation conv;
	GHIssueEvent ghEvent;
	Participant actor;
	Date eventDate;
	String body="";
	String instId = "";
	
	public Event(Conversation conv)
	{
		this.conv = conv;
	}
	
	public void printEvent()
	{
		System.out.println();
		System.out.println("Event info : ");
		System.out.println("Event instId : "+this.instId);
		System.out.println("Event actor : "+this.actor.loginName);
		System.out.println("Event date : "+this.eventDate);
		System.out.println("Event body : "+this.body);
	}

	public Conversation getConv() {
		return conv;
	}

	public void setConv(Conversation conv) {
		this.conv = conv;
	}

	public GHIssueEvent getGhEvent() {
		return ghEvent;
	}

	public void setGhEvent(GHIssueEvent ghEvent) {
		this.ghEvent = ghEvent;
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

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}
	
	

}
