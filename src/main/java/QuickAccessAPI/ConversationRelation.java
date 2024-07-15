package QuickAccessAPI;

import java.util.ArrayList;
import java.util.List;

import prmodel.Comment;
import prmodel.Event;
import prmodel.Participant;
import prmodel.PullRequest;
import prmodel.ReviewComment;
import prmodel.ReviewEvent;

public class ConversationRelation {
	
	PullRequest pr;
	
	ConversationRelation(PullRequest pr)
	{
		this.pr = pr;
	}
	
	public int num_commit_comments()
	{
		if(!this.pr.getConv().getReviewComments().isEmpty())
		{
			return this.pr.getConv().getReviewComments().size();
		}else {
			return 0;
		}
	}
	
	public int num_issue_comments()
	{
		if(!this.pr.getConv().getComments().isEmpty())
		{
			return this.pr.getConv().getComments().size();
		}else {
			return 0;
		}
	}
	
	public int num_comments()
	{
		return this.pr.getConv().getReviewComments().size() + this.pr.getConv().getComments().size();
	}
	
	
	public ArrayList<Comment> commentsHaveMentions()
	{
		ArrayList<Comment> comments = new ArrayList<>();
		if(!this.pr.getConv().getComments().isEmpty())
		{
			for(Comment cmti : this.pr.getConv().getComments())
			{
				if(!cmti.getMde().getMentionStrings().isEmpty())
				{
					comments.add(cmti);
				}
					
			}
		}
		return comments;
	}
	
	public ArrayList<ReviewComment> reviewCommentsHaveMentions()
	{
		ArrayList<ReviewComment> reviewcomments = new ArrayList<>();
		if(!this.pr.getConv().getReviewComments().isEmpty())
		{
			for(ReviewComment rcmti : this.pr.getConv().getReviewComments())
			{
				if(!rcmti.getMde().getMentionStrings().isEmpty())
				{
					reviewcomments.add(rcmti);
				}
			}
		}
		return reviewcomments;
	}
	
	public ArrayList<ReviewEvent> reviewEventsHaveMentions()
	{
		ArrayList<ReviewEvent> reviewevents = new ArrayList<>();
		if(!this.pr.getConv().getReviewEvents().isEmpty())
		{
			for(ReviewEvent revi : this.pr.getConv().getReviewEvents())
			{
				if(!revi.getMde().getMentionStrings().isEmpty())
				{
					reviewevents.add(revi);
				}
			}
		}
		return reviewevents;
	}
	
	public List<Comment> getCommentsByParticipant(Participant pari)
	{
		List<Comment> comments = new ArrayList<>();
		if(!this.pr.getConv().getComments().isEmpty())
		{
			for(Comment cmti : this.pr.getConv().getComments())
			{
				if(cmti.getCommenter().getInstId().equals(pari.getInstId()))
				{
					comments.add(cmti);
				}
			}
		}
		return comments;
	}
	
	public List<ReviewComment> getReviewCommentsByParticipant(Participant pari)
	{
		List<ReviewComment> reviewComments = new ArrayList<>();
		if(!this.pr.getConv().getReviewComments().isEmpty())
		{
			for(ReviewComment rcmti : this.pr.getConv().getReviewComments())
			{
				if(rcmti.getCommenter().getInstId().equals(pari.getInstId()))
				{
					reviewComments.add(rcmti);
				}
			}
		}
		return reviewComments;
	}
	
	public List<Event> getEventsByParticipant(Participant pari)
	{
		List<Event> events = new ArrayList<>();
		if(!this.pr.getConv().getEvents().isEmpty())
		{
			for(Event evi : this.pr.getConv().getEvents())
			{
				if(evi.getActor().getInstId().equals(pari.getInstId()))
				{
					events.add(evi);
				}
			}
		}
		return events;
	}
	
	public List<ReviewEvent> getReviewEventsByParticipant(Participant pari)
	{
		List<ReviewEvent> reviewEvents = new ArrayList<>();
		if(!this.pr.getConv().getReviewEvents().isEmpty())
		{
			for(ReviewEvent revi : this.pr.getConv().getReviewEvents())
			{
				if(revi.getActor().getInstId().equals(pari.getInstId()))
				{
					reviewEvents.add(revi);
				}
			}
		}
		return reviewEvents;
	}
	
	public boolean mentionExist()
	{
		boolean hasMention=false;
		if(!this.pr.getConv().getComments().isEmpty())
		{
			for(Comment cmti : this.pr.getConv().getComments())
			{
				if(!cmti.getMde().getMentionStrings().isEmpty())
				{
					hasMention=true;
					break;
				}
			}
		}
		if(!this.pr.getConv().getReviewComments().isEmpty())
		{
			for(ReviewComment rcmti : this.pr.getConv().getReviewComments())
			{
				if(!rcmti.getMde().getMentionStrings().isEmpty())
				{
					hasMention=true;
					break;
				}
					
			}
		}
		if(!this.pr.getConv().getReviewEvents().isEmpty())
		{
			for(ReviewEvent revi : this.pr.getConv().getReviewEvents())
			{
				if(!revi.getMde().getMentionStrings().isEmpty())
				{
					hasMention=true;
					break;
				}
			}
		}
		return hasMention;
	}
	

}
