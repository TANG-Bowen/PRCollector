package jwrmodel;

import java.util.LinkedHashSet;

public class Str_Conversation {
	
    String instId;
    LinkedHashSet<Str_Comment> comments;
    LinkedHashSet<Str_ReviewComment> reviewComments;
    LinkedHashSet<Str_Event> events;
    LinkedHashSet<Str_ReviewEvent> reviewEvents;
    LinkedHashSet<Str_CodeLineReview> codeLineReviews;
    LinkedHashSet<Str_PRAction> timeLine;
    
    public Str_Conversation()
    {
    	
    }

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public LinkedHashSet<Str_Comment> getComments() {
		return comments;
	}

	public void setComments(LinkedHashSet<Str_Comment> comments) {
		this.comments = comments;
	}

	public LinkedHashSet<Str_ReviewComment> getReviewComments() {
		return reviewComments;
	}

	public void setReviewComments(LinkedHashSet<Str_ReviewComment> reviewComments) {
		this.reviewComments = reviewComments;
	}

	public LinkedHashSet<Str_Event> getEvents() {
		return events;
	}

	public void setEvents(LinkedHashSet<Str_Event> events) {
		this.events = events;
	}

	public LinkedHashSet<Str_ReviewEvent> getReviewEvents() {
		return reviewEvents;
	}

	public void setReviewEvents(LinkedHashSet<Str_ReviewEvent> reviewEvents) {
		this.reviewEvents = reviewEvents;
	}

	public LinkedHashSet<Str_CodeLineReview> getCodeLineReviews() {
		return codeLineReviews;
	}

	public void setCodeLineReviews(LinkedHashSet<Str_CodeLineReview> codeLineReviews) {
		this.codeLineReviews = codeLineReviews;
	}

	public LinkedHashSet<Str_PRAction> getTimeLine() {
		return timeLine;
	}

	public void setTimeLine(LinkedHashSet<Str_PRAction> timeLine) {
		this.timeLine = timeLine;
	}
    
    
    
    

}
