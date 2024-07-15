package prmodel;

import java.util.Date;

public class PRAction {
	
	Conversation conv;
	String body="";
	Participant executor;
	Date actionDate;
	String type="";// [Comment, ReviewComment, Event, ReviewEvent]
	String instId="";
	
	public PRAction(Conversation conv)
	{
		this.conv = conv;
	}
	
	void printPRAction()
	{
		System.out.println();
		System.out.println("PRAction info : ");
		System.out.println("PRAction instId : ");
		System.out.println(" "+this.executor.loginName + "  "+ this.actionDate +"  "+this.type );
		System.out.println();
		System.out.println(this.body);
	}

	public Conversation getConv() {
		return conv;
	}

	public void setConv(Conversation conv) {
		this.conv = conv;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Participant getExecutor() {
		return executor;
	}

	public void setExecutor(Participant executor) {
		this.executor = executor;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}
	
	
	

}
