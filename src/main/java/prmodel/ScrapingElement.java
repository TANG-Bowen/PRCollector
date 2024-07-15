package prmodel;

import java.util.HashSet;

public class ScrapingElement {
	
	PullRequest pr;
	String descriptionScrp="";
	HashSet<String> mentionUsers;//name with @
	String instId="";
	
	ScrapingElement(PullRequest pr)
	{
		this.pr = pr;
		mentionUsers = new HashSet<>();
	}
	
	void printScrapingElement()
	{
		System.out.println();
		System.out.println("DescriptionScrp info");
		System.out.println("DescriptionScrp instId : "+this.instId);
		System.out.println("DescriptionScrp : "+this.descriptionScrp);
		System.out.println("MentionUsers : ");
		if(this.mentionUsers.isEmpty()==false)
		{
			for(String menti : this.mentionUsers)
			{
				System.out.println(" MentionUser : "+ menti);
			}
		}
	}

	public PullRequest getPr() {
		return pr;
	}

	public void setPr(PullRequest pr) {
		this.pr = pr;
	}

	public String getDescriptionScrp() {
		return descriptionScrp;
	}

	public void setDescriptionScrp(String descriptionScrp) {
		this.descriptionScrp = descriptionScrp;
	}

	public HashSet<String> getMentionUsers() {
		return mentionUsers;
	}

	public void setMentionUsers(HashSet<String> mentionUsers) {
		this.mentionUsers = mentionUsers;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}
	

}
