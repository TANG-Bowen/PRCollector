package jwrmodel;

import java.util.ArrayList;
import java.util.HashSet;

public class Str_CodeLineReview {
	
	HashSet<String> participantInstIds;
	ArrayList<String> reviewCommentInstIds;
	String diff;
	String instId;
	
	Str_CodeLineReview()
	{
		
	}

	public HashSet<String> getParticipantInstIds() {
		return participantInstIds;
	}

	public void setParticipantInstIds(HashSet<String> participantInstIds) {
		this.participantInstIds = participantInstIds;
	}

	public ArrayList<String> getReviewCommentInstIds() {
		return reviewCommentInstIds;
	}

	public void setReviewCommentInstIds(ArrayList<String> reviewCommentInstIds) {
		this.reviewCommentInstIds = reviewCommentInstIds;
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
