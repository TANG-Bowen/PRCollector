package prmodel;

import org.kohsuke.github.GHIssueEvent;

public class PRLabel {
	
	PullRequest pr;
	String name = "";
	String color= "";
	Long ghid;
	String description ="";
	GHIssueEvent ghEvent;
	Event event;
	String instId="";
	
	public PRLabel(PullRequest pr)
	{
	   this.pr = pr;	
	}
	
	void printLabel()
	{
		System.out.println();
		System.out.println("  Label instId : "+this.instId);
		System.out.println("  Label Name : "+ this.name);
		System.out.println("  Label color : "+ this.color);
		System.out.println("  Label ghid : "+ this.ghid);
		System.out.println("  Label description : "+ this.description);
		System.out.println("  Label event : " + this.event.body);
	}

	public PullRequest getPr() {
		return pr;
	}

	public void setPr(PullRequest pr) {
		this.pr = pr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Long getGhid() {
		return ghid;
	}

	public void setGhid(Long ghid) {
		this.ghid = ghid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public GHIssueEvent getGhEvent() {
		return ghEvent;
	}

	public void setGhEvent(GHIssueEvent ghEvent) {
		this.ghEvent = ghEvent;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}
	
	
	
	

}
