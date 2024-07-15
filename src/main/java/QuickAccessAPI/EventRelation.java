package QuickAccessAPI;

import prmodel.Event;
import prmodel.Participant;
import prmodel.PullRequest;

public class EventRelation {
	
	PullRequest pr;
	
	EventRelation(PullRequest pr)
	{
		this.pr = pr;
	}

	public int prior_interaction(Participant pra)
	{
		int num=0;
		if(!this.pr.getParticipants().isEmpty())
		{
			for(Participant pri : this.pr.getParticipants())
			{
				if(pri.getLoginName().equals(pra.getLoginName()))
				{
					if(!this.pr.getConv().getEvents().isEmpty())
					{
						for(Event evi : this.pr.getConv().getEvents())
						{
							if(evi.getActor().getLoginName().equals(pra.getLoginName()))
							{
								num++;
							}
						}
						return num;
					}
					System.out.println("No events the pariticipant joined in ");
					return 0;
				}
			}
			System.out.println("No events the pariticipant joined in ");
			return 0;
		}
		System.out.println("No participants in the pr ");
		return 0;
	}
}
