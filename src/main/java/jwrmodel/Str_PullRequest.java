package jwrmodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class Str_PullRequest {
	
	public String title;
	public int id;
	public String state;
	public String repositoryName;
	public Date createDate;
	public Date endDate;
	public boolean isMerged;
	public boolean isStandardMerged;
	public boolean srcRetrievable;
	public String mergeBranch;
	public String headBranch;
	public String prPageUrl;
	public String repositorySrcDLUrl;
	public String headRepositorySrcDLUrl;
	public List<String> repositoryBranches;
	public List<String> headRepositoryBranches;
	
	public Str_Description str_description;
	public String htmlDescription;
	public HashSet<Str_Participant> str_participants;
	public Str_Conversation str_conversation;
	public ArrayList<Str_Commit> str_commits;
	public Str_FilesChanged str_filesChanged;
	
	public HashSet<Str_PRLabel> str_finalLabels;
	public HashSet<Str_PRLabel> str_addedLabels;
	public HashSet<Str_PRLabel> str_removedLabels;
	
	public Str_PullRequest()
	{
		
	}
	
	
	

}
