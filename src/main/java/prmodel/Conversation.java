package prmodel;

import java.util.LinkedHashSet;

public class Conversation {
	
	PullRequest pr;
	String instId="";
	LinkedHashSet<Comment> comments;
	LinkedHashSet<ReviewComment> reviewComments;
	LinkedHashSet<Event> events;
	LinkedHashSet<ReviewEvent> reviewEvents;
	
	LinkedHashSet<CodeReviewSnippet> codeLineReviews;
	LinkedHashSet<PRAction> timeLine;
	
	public Conversation(PullRequest pr)
	{
		this.pr = pr;
		this.comments = new LinkedHashSet<>();
		this.reviewComments = new LinkedHashSet<>();
		this.events = new LinkedHashSet<>();
		this.reviewEvents = new LinkedHashSet<>();
		this.codeLineReviews = new LinkedHashSet<>();
		this.timeLine = new LinkedHashSet<>();
	}
	
	
	
	public PullRequest getPr() {
		return pr;
	}



	public void setPr(PullRequest pr) {
		this.pr = pr;
	}



	public String getInstId() {
		return instId;
	}



	public void setInstId(String instId) {
		this.instId = instId;
	}



	public LinkedHashSet<Comment> getComments() {
		return comments;
	}



	public void setComments(LinkedHashSet<Comment> comments) {
		this.comments = comments;
	}



	public LinkedHashSet<ReviewComment> getReviewComments() {
		return reviewComments;
	}



	public void setReviewComments(LinkedHashSet<ReviewComment> reviewComments) {
		this.reviewComments = reviewComments;
	}



	public LinkedHashSet<Event> getEvents() {
		return events;
	}



	public void setEvents(LinkedHashSet<Event> events) {
		this.events = events;
	}



	public LinkedHashSet<ReviewEvent> getReviewEvents() {
		return reviewEvents;
	}



	public void setReviewEvents(LinkedHashSet<ReviewEvent> reviewEvents) {
		this.reviewEvents = reviewEvents;
	}



	public LinkedHashSet<CodeReviewSnippet> getCodeLineReviews() {
		return codeLineReviews;
	}



	public void setCodeLineReviews(LinkedHashSet<CodeReviewSnippet> codeLineReviews) {
		this.codeLineReviews = codeLineReviews;
	}



	public LinkedHashSet<PRAction> getTimeLine() {
		return timeLine;
	}



	public void setTimeLine(LinkedHashSet<PRAction> timeLine) {
		this.timeLine = timeLine;
	}



	void printConversation()
	{
		System.out.println();
		System.out.println("Conversation instId : "+this.instId);
		this.printComments();
		this.printReviewComments();
		this.printEvents();
		this.printReviewEvents();
		this.printCodeLineReviews();
		this.printTimeLine();
	}
	
	void printComments()
	{
		System.out.println();
		System.out.println("Comments : ");
		System.out.println();
		for(Comment cmti : this.comments)
		{
			cmti.printComment();
			System.out.println("Comment-----------------------------");
		}
	}
	
	void printReviewComments()
	{
		System.out.println("Review Comments : ");
		System.out.println();
		for(ReviewComment rcmti : this.reviewComments)
		{
			rcmti.printReviewComment();
			System.out.println("ReviewComment-----------------------------");
		}
	}
	
	void printEvents()
	{
		System.out.println("Events : ");
		System.out.println();
		for(Event ei : this.events)
		{
			ei.printEvent();
			System.out.println("Event-----------------------------");
		}
	}
	
	void printReviewEvents()
	{
		System.out.println("ReviewEvents : ");
		System.out.println();
		for(ReviewEvent rei : this.reviewEvents)
		{
			rei.printReviewEvent();
			System.out.println("ReviewEvent-----------------------------");
		}
	}
	
	void printCodeLineReviews()
	{
		System.out.println("CodeLineReviews : ");
		System.out.println();
		for(CodeReviewSnippet clri : this.codeLineReviews)
		{
			clri.printCodeLineReview();
			System.out.println("CodeLineReview-----------------------------");
		}
	}

	void printTimeLine()
	{
		System.out.println();
		System.out.println("TimeLine : ");
		System.out.println();
		for(PRAction ati : this.timeLine)
		{
			ati.printPRAction();
			System.out.println("PRAction-----------------------------------");
		}
	}
}
