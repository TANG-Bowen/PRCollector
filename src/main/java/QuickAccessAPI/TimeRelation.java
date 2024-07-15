package QuickAccessAPI;

import java.util.Date;
import prmodel.CIStatus;
import prmodel.Commit;
import prmodel.Event;
import prmodel.PRAction;
import prmodel.PullRequest;

public class TimeRelation {
	
	PullRequest pr;

	TimeRelation(PullRequest pr)
	{
		this.pr = pr;
		
	}
	
	public long lifetime_msec()
	{
		long diff = pr.getEndDate().getTime() - pr.getCreateDate().getTime();
		return diff;
	}
	
	public long mergetime_msec()
	{
		if(pr.isMerged() && pr.isStandardMerged()==false )
		{		 
		  if(!pr.getConv().getEvents().isEmpty())
		  {
			for(Event evi : pr.getConv().getEvents())
			{
				if(evi.getBody().equals("closed"))
				{
					long diff = evi.getEventDate().getTime() - pr.getCreateDate().getTime();
					return diff;
				}
			}
			return 0;
		  }
		  return 0;
		
		}else if(pr.isStandardMerged()){
			return this.lifetime_msec();
		}else if(!pr.isMerged())
		{
			System.out.println("PR is not merged");
			return 0;
		}else {
			System.out.println("Not common sense");
			return 0;
		}
	}
	
	public long firstComment_response()
	{
		Date actionDate=null;
		if(!this.pr.getConv().getTimeLine().isEmpty())
		{
			for(PRAction prai : this.pr.getConv().getTimeLine())
			{
				if(prai.getType().equals("Comment"))
				{
					actionDate = prai.getActionDate();
					break;
				}
			}
		}
		if(actionDate!=null)
		{
			long diff = actionDate.getTime() - pr.getCreateDate().getTime();
		  return diff;
		}else {
			System.out.println("No comment in the PR");
			return 0;
		}
		
	}
	
	public long firstReviewComment_response()
	{
		Date actionDate=null;
		if(!this.pr.getConv().getTimeLine().isEmpty())
		{
			for(PRAction prai : this.pr.getConv().getTimeLine())
			{
				if(prai.getType().equals("ReviewComment"))
				{
					actionDate = prai.getActionDate();
					break;
				}
			}
		}
		if(actionDate!=null)
		{
			long diff = actionDate.getTime() - pr.getCreateDate().getTime();
		  return diff;
		}else {
			System.out.println("No reviewcomment in the PR");
			return 0;
		}
	}
	
	public long firstEvent_response()
	{
		Date actionDate=null;
		if(!this.pr.getConv().getTimeLine().isEmpty())
		{
			for(PRAction prai : this.pr.getConv().getTimeLine())
			{
				if(prai.getType().equals("Event"))
				{
					actionDate = prai.getActionDate();
					break;
				}
			}
		}
		if(actionDate!=null)
		{
			long diff = actionDate.getTime() - pr.getCreateDate().getTime();
		  return diff;
		}else {
			System.out.println("No event in the PR");
			return 0;
		}
	}
	
	public long firstReviewEvent_response()
	{
		Date actionDate=null;
		if(!this.pr.getConv().getTimeLine().isEmpty())
		{
			for(PRAction prai : this.pr.getConv().getTimeLine())
			{
				if(prai.getType().equals("ReviewEvent"))
				{
					actionDate = prai.getActionDate();
					break;
				}
			}
		}
		if(actionDate!=null)
		{
			long diff = actionDate.getTime() - pr.getCreateDate().getTime();
		  return diff;
		}else {
			System.out.println("No reviewevent in the PR");
			return 0;
		}
	}
	
	public long totalCI_Latency()
	{
		if(!this.pr.getCommits().isEmpty())
		{
		   int sz = this.pr.getCommits().size();
		   Commit lastCmit = this.pr.getCommits().get(sz-1);
		   if(!lastCmit.getCis().isEmpty())
		   {
			   int sz_ci = lastCmit.getCis().size();
			   CIStatus lastCi = lastCmit.getCis().get(sz_ci-1);
			   long diff = lastCi.getUpdateDate().getTime() - pr.getCreateDate().getTime();
			   return diff;
		   }
		   System.out.println("No ci in the last commit");
		   return 0;
		}
		System.out.println("No commit in the PR");
		return 0;
		
	}
	

}
