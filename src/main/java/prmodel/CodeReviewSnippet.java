package prmodel;

import java.util.ArrayList;
import java.util.HashSet;

public class CodeReviewSnippet {
	
	Conversation conv;
    HashSet<Participant> participants;
    ArrayList<ReviewComment> reviewComments;
    String diff="";
    String instId="";
    
    public CodeReviewSnippet(Conversation conv)
    {
    	this.conv = conv;
    	participants = new HashSet<>();
    	reviewComments = new ArrayList<>();
    }
    
    void printCodeLineReview()
    {
    	System.out.println();
    	System.out.println("CodeLineReview info : ");
    	System.out.println("CodeLineReview instId : "+this.instId);
    	for(Participant prti : this.participants)
    	{
    		System.out.println(" Participants(cdlr) : "+prti.loginName);
    	}
    	
    	System.out.println();
    	System.out.println(" Diff(cdlr) : ");
    	System.out.println(" "+this.diff);
    	System.out.println();
    	System.out.println(" ReviewComment(cdlr) : ");
    	for(ReviewComment rcti : this.reviewComments)
    	{
    		System.out.println("  "+ rcti.commenter.loginName+" "+rcti.commentDate);
    		System.out.println("  "+ rcti.body);
    	}
    	
    }

	public Conversation getConv() {
		return conv;
	}

	public void setConv(Conversation conv) {
		this.conv = conv;
	}

	public HashSet<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(HashSet<Participant> participants) {
		this.participants = participants;
	}

	public ArrayList<ReviewComment> getReviewComments() {
		return reviewComments;
	}

	public void setReviewComments(ArrayList<ReviewComment> reviewComments) {
		this.reviewComments = reviewComments;
	}

	public String getDiff() {
		return diff;
	}

	public void setDiff(String diff) {
		this.diff = diff;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}
    
    
    
   

}
