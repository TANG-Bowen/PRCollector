package QuickAccessAPI;

import java.util.ArrayList;
import java.util.List;

import prmodel.Event;
import prmodel.Participant;
import prmodel.PullRequest;

public class ParticipantRelation {
	
	PullRequest pr;
	
	ParticipantRelation(PullRequest pr)
	{
		this.pr = pr;
	}
	
	public int num_participants()
	{
		return this.pr.getParticipants().size();
	}
	
	public int followRelation(Participant pra, Participant pro)
	{
		boolean oIsAFollower = false;
		boolean aIsOFollower = false;
		if(!pra.getFollowers().isEmpty())
		{
			for(String fleri : pra.getFollowers())
			{
				if(pro.getLoginName().equals(fleri))
				{
					oIsAFollower = true;
				}
			}
		}
		
		if(!pro.getFollowers().isEmpty())
		{
			for(String fleri : pro.getFollowers())
			{
				if(pra.getLoginName().equals(fleri))
				{
					aIsOFollower = true;
				}
			}
		}
		
		if(oIsAFollower == true && aIsOFollower==true)
		{
			return 2;
		}else if(oIsAFollower == true && aIsOFollower==false)
		{
			return -1;
		}else if(oIsAFollower == false && aIsOFollower==true)
		{
			return 1;
		}else {
			return 0;
		}
	}
	
	public boolean social_distance()
	{
		Participant submitter=null;
		Participant closeMember=null;
		if(!this.pr.getParticipants().isEmpty())
		{
			for(Participant prti : this.pr.getParticipants())
			{
				if(prti.getRole().equals("Author"))
				{
					submitter = prti;
				}
			}
		}
		
		if(!this.pr.getConv().getEvents().isEmpty())
		{
			for(Event evi : this.pr.getConv().getEvents())
			{
				if(evi.getBody().equals("closed"))
				{
					closeMember = evi.getActor();
				}
			}
		}
		
		if(submitter!=null && closeMember!=null)
		{
			if(this.followRelation(submitter, closeMember)==1 || this.followRelation(submitter, closeMember)==2)
			{
				return true;
			}else {
				return false;
			}
				
		}
		System.out.println("Null participant");
		return false;
	}
	
	public Participant getAuthor()
	{
		if(!this.pr.getParticipants().isEmpty())
		{
			for(Participant pari : this.pr.getParticipants())
			{
				if(pari.getRole().equals("Author"))
				{
					return pari;
				}
			}
		}
		return null;
	}
	
	public List<Participant> getReviewers()
	{
		List<Participant> reviewers = new ArrayList<>();
		if(!this.pr.getParticipants().isEmpty())
		{
			for(Participant pri : this.pr.getParticipants())
			{
				if(pri.getRole().equals("Reviewer"))
				{
					reviewers.add(pri);
				}
			}
		}
		return reviewers;
	}
	
	
	

}
