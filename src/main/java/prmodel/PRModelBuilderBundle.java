package prmodel;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestQueryBuilder;
import org.kohsuke.github.GHPullRequestSearchBuilder;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

public class PRModelBuilderBundle {
	
	String token="";
	String repoName="";// String/String
	GitHub github;
	GHRepository repo;
	GHPullRequestQueryBuilder prQuery;
	GHPullRequestSearchBuilder prSearch;
	
	ArrayList<PRModelBuilder> prmdlBuilders;
	ArrayList<PullRequest> prs;
	String rootSrcPath="";
	int changedFilesMin=0;
	int changedFilesMax=-9999;
	int commitMin=0;
	int commitMax=-9999;
	boolean writeFile=false;
	boolean deleteSrcFile = false;
	boolean freeMemory=false;
	ArrayList<String> downloadBannedLabels;
	
	public PRModelBuilderBundle(String token , String repoName)
	{
		this.token = token;
		this.repoName = repoName;
		this.prmdlBuilders = new ArrayList<>();
		this.prs = new ArrayList<>();
		this.downloadBannedLabels = new ArrayList<>();
		try {
			this.github = GitHub.connectUsingOAuth(token);
			this.repo = this.github.getRepository(repoName);
			//this.prQuery = this.repo.queryPullRequests();
			this.prSearch = this.repo.searchPullRequests();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void build()
	{
		PagedIterable<GHPullRequest> prss = this.prSearch.list();
		try {	
			   if(!prss.toList().isEmpty())
			   {
				for(GHPullRequest ghpri : prss)
				{
					PRModelBuilder mdlBuilderi = new PRModelBuilder(this.token,this.repoName,ghpri.getNumber());
					mdlBuilderi.setRootSrcPath(this.getRootSrcPath());
					mdlBuilderi.downloadFilterChangedFiles(changedFilesMin, changedFilesMax);
					mdlBuilderi.downloadFilterCommitNum(commitMin, commitMax);
					mdlBuilderi.downloadBannedLabels(downloadBannedLabels);
					mdlBuilderi.writeFile(writeFile);
					mdlBuilderi.deleteSrcFile(deleteSrcFile);
					mdlBuilderi.build();
					if(this.freeMemory)
					{
						mdlBuilderi=null;
					}else {
					PullRequest pri =mdlBuilderi.getPr();
					this.prmdlBuilders.add(mdlBuilderi);
					this.prs.add(pri);
					}
				}
			   }else {
				   System.out.println("No pr select in builder");
			   }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void searchByAssignedUser(String assigneeName)
	{
		try {
			GHUser assignee = this.github.getUser(assigneeName);
			this.prSearch.assigned(assignee);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void searchByAuthor(String authorName)
	{
		try {
			GHUser author = this.github.getUser(authorName);
			this.prSearch.author(author);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void searchByBaseBranch(String branchName)
	{
		try {
			GHBranch branch = this.repo.getBranch(branchName);
			this.prSearch.base(branch);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void searchByClosed(String closed)
	{
		LocalDate closedDate = LocalDate.parse(closed);
		this.prSearch.closed(closedDate);
	}
	
	public void searchByClosed(String from, String to)
	{
		LocalDate fromDate = LocalDate.parse(from);
		LocalDate toDate = LocalDate.parse(to);
		this.prSearch.closed(fromDate, toDate);
		
	}
	
	public void searchByClosedAfter(String closed, boolean inclusive)
	{
		LocalDate closedDate = LocalDate.parse(closed);
		this.prSearch.closedAfter(closedDate, inclusive);
	}
	
	public void searchByClosedBefore(String closed, boolean inclusive)
	{
		LocalDate closedDate = LocalDate.parse(closed);
		this.prSearch.closedBefore(closedDate, inclusive);
	}
	
	public void searchByCommit(String sha)
	{
		this.prSearch.commit(sha);
	}
	
	public void searchByCreated(String created)
	{
		LocalDate createdDate = LocalDate.parse(created);
		this.prSearch.created(createdDate);
	}
	
	public void searchByCreated(String from, String to)
	{
		LocalDate fromDate = LocalDate.parse(from);
		LocalDate toDate = LocalDate.parse(to);
		this.prSearch.created(fromDate, toDate);
	}
	
	public void searchByCreatedAfter(String created, boolean inclusive)
	{
		LocalDate createdDate = LocalDate.parse(created);
		this.prSearch.createdAfter(createdDate, inclusive);
	}
	
	public void searchByCreatedBefore(String created, boolean inclusive)
	{
		LocalDate createDate = LocalDate.parse(created);
		this.prSearch.createdBefore(createDate, inclusive);
	}
	
	public void searchByHeadBranch(String branchName)
	{
		try {
			GHBranch branch = this.repo.getBranch(branchName);
			this.prSearch.head(branch);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void searchByInLabels(ArrayList<String> labels)
	{
		this.prSearch.inLabels(labels);
	}
	
	public void searchByLabel(String label)
	{
		this.prSearch.label(label);
	}

	public void searchByIsClosed()
	{
		this.prSearch.isClosed();
	}
	
	public void searchByIsDraft()
	{
		this.prSearch.isDraft();
	}
	
	public void searchByIsMerged()
	{
		this.prSearch.isMerged();
	}
	
	public void searchByIsOpen()
	{
		this.prSearch.isOpen();
	}
	
	public void searchByMentions(String userName)
	{
		try {
			GHUser mentionUser = this.github.getUser(userName);
			this.prSearch.mentions(mentionUser);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void searchByMerged(String merged)
	{
		LocalDate mergedDate = LocalDate.parse(merged);
		this.prSearch.merged(mergedDate);
	}
	
	public void searchByMerged(String from, String to)
	{
		LocalDate fromDate = LocalDate.parse(from);
		LocalDate toDate = LocalDate.parse(to);
		this.prSearch.merged(fromDate, toDate);
	}
	
	public void searchByMergedAfter(String merged, boolean inclusive)
	{
		LocalDate mergedDate = LocalDate.parse(merged);
		this.prSearch.mergedAfter(mergedDate, inclusive);
	}
	
	public void searchByMergedBefore(String merged, boolean inclusive)
	{
		LocalDate mergedDate = LocalDate.parse(merged);
		this.prSearch.mergedBefore(mergedDate, inclusive);
	}
	
	public void searchByTitleLike(String title)
	{
		this.prSearch.titleLike(title);
	}
	
	public void searchByUpdated(String updated)
	{
		LocalDate updatedDate = LocalDate.parse(updated);
		this.prSearch.updated(updatedDate);
	}
	
	public void searchByUpdated(String from, String to)
	{
		LocalDate fromDate = LocalDate.parse(from);
		LocalDate toDate = LocalDate.parse(to);
		this.prSearch.updated(fromDate, toDate);
	}
	
	public void searchByUpdatedAfter(String updated, boolean inclusive)
	{
		LocalDate updateDate = LocalDate.parse(updated);
		this.prSearch.updatedAfter(updateDate, inclusive);
	}
	
	public void searchByUpdateBefore(String updated, boolean inclusive)
	{
		LocalDate updateDate = LocalDate.parse(updated);
		this.prSearch.updatedBefore(updateDate, inclusive);
	}
	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public GitHub getGithub() {
		return github;
	}

	public void setGithub(GitHub github) {
		this.github = github;
	}

	public GHRepository getRepo() {
		return repo;
	}

	public void setRepo(GHRepository repo) {
		this.repo = repo;
	}

	public GHPullRequestQueryBuilder getPrQuery() {
		return prQuery;
	}

	public void setPrQuery(GHPullRequestQueryBuilder prQuery) {
		this.prQuery = prQuery;
	}

	public GHPullRequestSearchBuilder getPrSearch() {
		return prSearch;
	}

	public void setPrSearch(GHPullRequestSearchBuilder prSearch) {
		this.prSearch = prSearch;
	}

	public ArrayList<PRModelBuilder> getPrmdlBuilders() {
		return prmdlBuilders;
	}

	public void setPrmdlBuilders(ArrayList<PRModelBuilder> prmdlBuilders) {
		this.prmdlBuilders = prmdlBuilders;
	}

	public ArrayList<PullRequest> getPrs() {
		return prs;
	}

	public void setPrs(ArrayList<PullRequest> prs) {
		this.prs = prs;
	}

	public String getRootSrcPath() {
		return rootSrcPath;
	}

	public void setRootSrcPath(String rootSrcPath) {
		this.rootSrcPath = rootSrcPath;
	}
	
	public void writeFile(boolean writeFile)
	{
		this.writeFile = writeFile;
	}
	
	public void deleteSrcFile(boolean deleteSrcFile)
	{
		this.deleteSrcFile = deleteSrcFile;
	}
	
	public void freeMemory(boolean freeMemory)
	{
		this.freeMemory=freeMemory;
	}
	
	public void downloadChangedFiles(int min , int max)
	{
		this.changedFilesMin= min;
		this.changedFilesMax= max;
	}
	
	public void downloadCommitNum(int min , int max)
	{
		this.commitMin= min;
		this.commitMax= max;
	}
	
	public void downloadBannedLabels(ArrayList<String> labels)
	{
		this.downloadBannedLabels = labels;
	}
	
}
