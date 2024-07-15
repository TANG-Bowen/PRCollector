package QuickAccessAPI;

import java.util.ArrayList;

import prmodel.PullRequest;

public class DescriptionRelation {
	
	PullRequest pr;
	
	DescriptionRelation(PullRequest pr)
	{
		this.pr = pr;
	}

	public int complexity_title()
	{
		String[] words = this.pr.getTitle().split("\\s+");
		return words.length;
		
	}
	
	public int complexity_description()
	{
		String[] words = this.pr.getDcpt().getBody().split("\\s+");
		return words.length;
	}
	
	public ArrayList<String> mentionInDescription()
	{
		ArrayList<String> ss = new ArrayList<>();
		if(!this.pr.getDcpt().getMde().getMentionStrings().isEmpty())
		{
			ss = this.pr.getDcpt().getMde().getMentionStrings();
			return ss;
		}else {
			return ss;
		}
	}
	
	
}
