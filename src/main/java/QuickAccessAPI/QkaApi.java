package QuickAccessAPI;

import prmodel.PullRequest;

public class QkaApi {
	
	PullRequest pr;
	CommitRelation cmtr;
	ConversationRelation convr;
	DescriptionRelation dscrpr;
	EventRelation evnr;
	ParticipantRelation partr;
	TimeRelation tmr;
	
	
	public QkaApi(PullRequest pr)
	{
		this.pr = pr;
		cmtr = new CommitRelation(this.pr);
		convr = new ConversationRelation(this.pr);
		dscrpr = new DescriptionRelation(this.pr);
		evnr = new EventRelation(this.pr);
		partr = new ParticipantRelation(this.pr);
		tmr = new TimeRelation(this.pr);
	}


	public PullRequest getPr() {
		return pr;
	}


	public void setPr(PullRequest pr) {
		this.pr = pr;
	}


	public CommitRelation getCmtr() {
		return cmtr;
	}


	public void setCmtr(CommitRelation cmtr) {
		this.cmtr = cmtr;
	}


	public ConversationRelation getConvr() {
		return convr;
	}


	public void setConvr(ConversationRelation convr) {
		this.convr = convr;
	}


	public DescriptionRelation getDscrpr() {
		return dscrpr;
	}


	public void setDscrpr(DescriptionRelation dscrpr) {
		this.dscrpr = dscrpr;
	}


	public EventRelation getEvnr() {
		return evnr;
	}


	public void setEvnr(EventRelation evnr) {
		this.evnr = evnr;
	}


	public ParticipantRelation getPartr() {
		return partr;
	}


	public void setPartr(ParticipantRelation partr) {
		this.partr = partr;
	}


	public TimeRelation getTmr() {
		return tmr;
	}


	public void setTmr(TimeRelation tmr) {
		this.tmr = tmr;
	}
	
	
	

}
