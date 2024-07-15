package QuickAccessAPI;

import java.io.File;
import prmodel.CIStatus;
import prmodel.Commit;
import prmodel.DiffFileUnit;
import prmodel.PullRequest;

public class CommitRelation {
	
	PullRequest pr;
	
	CommitRelation(PullRequest pr)
	{
		this.pr = pr;
	}
	
	public int Num_commits()
	{
		return pr.getCommits().size();
	}
	
	public int Src_churn()
	{
		int lines=0;
		if(!this.pr.getFlcg().getDiffFileUnits().isEmpty())
		{
			for(DiffFileUnit dfui : this.pr.getFlcg().getDiffFileUnits())
			{
				lines += dfui.getDiffLineUnits().size();
			}
			return lines;
		}else {
			System.out.println("No DiffFile in the PR");
			return 0;
		}
	}
	
	public int Src_churn(String commitSha)
	{
		int lines = 0;
		if(!pr.getCommits().isEmpty())
		{
			for(Commit cmiti : pr.getCommits())
			{
				if(cmiti.getSha().equals(commitSha))
				{
					if(!cmiti.getFlcg().getDiffFileUnits().isEmpty())
					{
						for(DiffFileUnit dfui : cmiti.getFlcg().getDiffFileUnits())
						{
							lines += dfui.getDiffLineUnits().size(); 
						}
						return lines;
					}else {
						System.out.println("No line changed in the commit ");
						return 0;
					}
				}
			}
			System.out.println("No such commitSha in the PR ");
			return 0;
		}else {
			return 0;
		}
	}
	
	public int test_churn()
	{
		int lines = 0;
		if(!this.pr.getFlcg().getDiffFileUnits().isEmpty())
		{
			for(DiffFileUnit dfui : this.pr.getFlcg().getDiffFileUnits())
			{
				if(dfui.isTest())
				{
					lines += dfui.getDiffLineUnits().size();
				}
			}
			return lines;
		}
		System.out.println("No commits in the PR");
		return 0;
	}
	
	public int test_churn(String commitSha)
	{
		int lines =0;
		if(!this.pr.getCommits().isEmpty())
		{
			for(Commit cmiti : this.pr.getCommits())
			{
				if(cmiti.getSha().equals(commitSha))
				{
					if(!cmiti.getFlcg().getDiffFileUnits().isEmpty())
					{
						for(DiffFileUnit dfui : cmiti.getFlcg().getDiffFileUnits())
						{
							if(dfui.isTest())
							{
								lines += dfui.getDiffLineUnits().size();
							}
						}
						return lines;
					}
				}
			}
			System.out.println("No such commitSha in the PR");
			return 0;
		}
		System.out.println("No commits in the PR");
		return 0;
	}
	
	public int files_added()
	{
		int files =0;
		if(!this.pr.getFlcg().getDiffFileUnits().isEmpty())
		{
			for(DiffFileUnit dfui : this.pr.getFlcg().getDiffFileUnits())
			{
				if(dfui.getFileType().equals("add"))
				{
					files ++;
				}
			}
			return files;
		}
		System.out.println("No file changed in the PR");
		return 0;
	}
	
	public int files_added(String commitSha)
	{
		int files = 0;
		if(!this.pr.getCommits().isEmpty())
		{
			for(Commit cmiti : this.pr.getCommits())
			{
				if(cmiti.getSha().equals(commitSha))
				{
					if(!cmiti.getFlcg().getDiffFileUnits().isEmpty())
					{
						for(DiffFileUnit dfui : cmiti.getFlcg().getDiffFileUnits())
						{
							if(dfui.getFileType().equals("add"))
							{
								files++;
							}
						}
						return files;
					}
				}
				
			}
			System.out.println("No such commit in the PR");
			return 0;
		}
		System.out.println("No commits in the PR");
		return 0;
	}
	
	public int files_deleted()
	{
		int files =0;
		if(!this.pr.getFlcg().getDiffFileUnits().isEmpty())
		{
			for(DiffFileUnit dfui : this.pr.getFlcg().getDiffFileUnits())
			{
				if(dfui.getFileType().equals("delete"))
				{
					files ++;
				}
			}
			return files;
		}
		System.out.println("No file changed in the PR");
		return 0;
	}
	
	public int files_deleted(String commitSha)
	{
		int files = 0;
		if(!this.pr.getCommits().isEmpty())
		{
			for(Commit cmiti : this.pr.getCommits())
			{
				if(cmiti.getSha().equals(commitSha))
				{
					if(!cmiti.getFlcg().getDiffFileUnits().isEmpty())
					{
						for(DiffFileUnit dfui : cmiti.getFlcg().getDiffFileUnits())
						{
							if(dfui.getFileType().equals("delete"))
							{
								files++;
							}
						}
						return files;
					}
				}
				
			}
			System.out.println("No such commit in the PR");
			return 0;
		}
		System.out.println("No commits in the PR");
		return 0;
	}
	
	public int files_modified()
	{
		int files =0;
		if(!this.pr.getFlcg().getDiffFileUnits().isEmpty())
		{
			for(DiffFileUnit dfui : this.pr.getFlcg().getDiffFileUnits())
			{
				if(dfui.getFileType().equals("change"))
				{
					files ++;
				}
			}
			return files;
		}
		System.out.println("No file changed in the PR");
		return 0;
	}
	
	public int files_modified(String commitSha)
	{
		int files = 0;
		if(!this.pr.getCommits().isEmpty())
		{
			for(Commit cmiti : this.pr.getCommits())
			{
				if(cmiti.getSha().equals(commitSha))
				{
					if(!cmiti.getFlcg().getDiffFileUnits().isEmpty())
					{
						for(DiffFileUnit dfui : cmiti.getFlcg().getDiffFileUnits())
						{
							if(dfui.getFileType().equals("change"))
							{
								files++;
							}
						}
						return files;
					}
				}
				
			}
			System.out.println("No such commit in the PR");
			return 0;
		}
		System.out.println("No commits in the PR");
		return 0;
	}
	
	public int files_changed()
	{
		int files =0;
		if(!this.pr.getFlcg().getDiffFileUnits().isEmpty())
		{
			files = this.pr.getFlcg().getDiffFileUnits().size();
			return files;
		}
		System.out.println("No file changed in the PR");
		return 0;
	}
	
	public int files_changed(String commitSha)
	{
		int files = 0;
		if(!this.pr.getCommits().isEmpty())
		{
			for(Commit cmiti : this.pr.getCommits())
			{
				if(cmiti.getSha().equals(commitSha))
				{
					if(!cmiti.getFlcg().getDiffFileUnits().isEmpty())
					{
						files = cmiti.getFlcg().getDiffFileUnits().size();
						return files;
					}
				}
				
			}
			System.out.println("No such commit in the PR");
			return 0;
		}
		System.out.println("No commits in the PR");
		return 0;
	}
	
	public int src_files()
	{
		int files =0;
		if(!this.pr.getFlcg().getDiffFileUnits().isEmpty())
		{
			for(DiffFileUnit dfui : this.pr.getFlcg().getDiffFileUnits())
			{	
				if(dfui.isJavaSrcFile())
				{
					files ++;
				}					
			}
			return files;
		}
		System.out.println("No file changed in the PR");
		return 0;
	}
	
	public int src_files(String commitSha)
	{
		int files = 0;
		if(!this.pr.getCommits().isEmpty())
		{
			for(Commit cmiti : this.pr.getCommits())
			{
				if(cmiti.getSha().equals(commitSha))
				{
					if(!cmiti.getFlcg().getDiffFileUnits().isEmpty())
					{
						for(DiffFileUnit dfui : cmiti.getFlcg().getDiffFileUnits())
						{
								if(dfui.isJavaSrcFile())
								{
                                   files++;
								}
						}
						return files;
					}
				}
				
			}
			System.out.println("No such commit in the PR");
			return 0;
		}
		System.out.println("No commits in the PR");
		return 0;
	}
	
	public int doc_files()
	{
		int files =0;
		if(!this.pr.getFlcg().getDiffFileUnits().isEmpty())
		{
			for(DiffFileUnit dfui : this.pr.getFlcg().getDiffFileUnits())
			{	
				String[] ss = dfui.getRelativePath().split(File.separator);
				int l = ss.length;
				if(ss[l-1].contains(".md") || ss[l-1].contains(".html") || ss[l-1].contains(".adoc"))
                {
	               files++;
                }
			}
			return files;
		}
		System.out.println("No files changed in the PR");
		return 0;
	}
	
	public int doc_files(String commitSha)
	{
		int files = 0;
		if(!this.pr.getCommits().isEmpty())
		{
			for(Commit cmiti : this.pr.getCommits())
			{
				if(cmiti.getSha().equals(commitSha))
				{
					if(!cmiti.getFlcg().getDiffFileUnits().isEmpty())
					{
						for(DiffFileUnit dfui : cmiti.getFlcg().getDiffFileUnits())
						{
							String[] ss = dfui.getRelativePath().split(File.separator);
							int l = ss.length;
							if(ss[l-1].contains(".md") || ss[l-1].contains(".html") || ss[l-1].contains(".adoc"))
			                {
				               files++;
			                }	
						}
						return files;
					}
				}
				
			}
			System.out.println("No such commit in the PR");
			return 0;
		}
		System.out.println("No commits in the PR");
		return 0;
		
	}
	
	public int other_files()
	{
		int files =0;
		if(!this.pr.getFlcg().getDiffFileUnits().isEmpty())
		{
			for(DiffFileUnit dfui : this.pr.getFlcg().getDiffFileUnits())
			{
				if(!dfui.isJavaSrcFile())
				{
					String[] ss = dfui.getRelativePath().split(File.separator);
					int l = ss.length;
					if(!ss[l-1].contains(".md") && !ss[l-1].contains(".html") && !ss[l-1].contains(".adoc"))
	                {
		               files++;
	                }	
				}
			}
			return files;
		}
		System.out.println("No files changed in the PR");
		return 0;
	}
	
	public int other_files(String commitSha)
	{
		int files = 0;
		if(!this.pr.getCommits().isEmpty())
		{
			for(Commit cmiti : this.pr.getCommits())
			{
				if(cmiti.getSha().equals(commitSha))
				{
					if(!cmiti.getFlcg().getDiffFileUnits().isEmpty())
					{
						for(DiffFileUnit dfui : cmiti.getFlcg().getDiffFileUnits())
						{
							if(!dfui.isJavaSrcFile())
							{
							   String[] ss = dfui.getRelativePath().split(File.separator);
							   int l = ss.length;
							   if(!ss[l-1].contains(".md") && !ss[l-1].contains(".html") && !ss[l-1].contains(".adoc"))
			                   {
				                   files++;
			                   }	
							}
						}
						return files;
					}
				}
				
			}
			System.out.println("No such commit in the PR");
			return 0;
		}
		System.out.println("No commits in the PR");
		return 0;
		
	}
	
	public boolean test_inclusion()
	{
		if(this.test_churn()!=0)
		{
			return true;
		}else {
			return false;
		}
	}
	
	public boolean test_inclusion(String commitSha)
	{
		if(this.test_churn(commitSha)!=0)
		{
			return true;
		}else {
			return false;
		}
	}
	
	public int CIfailures()
	{
		int count =0;
		if(!this.pr.getCommits().isEmpty())
		{
			for(Commit cmiti : this.pr.getCommits())
			{
				if(!cmiti.getCis().isEmpty())
				{
					for(CIStatus cisi : cmiti.getCis())
					{
						if(!cisi.getState().equals("SUCCESS"))
						{
							count++;
						}
					}
				}
			}
			return count;
		}
		System.out.println("No commits in the PR");
		return 0;
	}
	
	
	
	
	
	

}
