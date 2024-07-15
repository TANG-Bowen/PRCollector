package prmodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class PullRequest {
	
	String title="";
	int id=-99;
	String state="";//CLOSED, OPEN
	String repositoryName="";
	Date createDate;
	Date endDate;
	boolean isMerged = false;
	boolean isStandardMerged = false;//new
	boolean srcRetrievable = false;//new
	String mergeBranch="";
	String headBranch="";
	String prPageUrl="";
	String repositorySrcDLUrl="";
	String headRepositorySrcDLUrl="";
	List<String> repositoryBranches;
	List<String> headRepositoryBranches;
	String htmlDescription="";
	
	Description dcpt;
	
	HashSet<Participant> participants;
	
	ScrapingElement scrpe;
	
	Conversation conv;
	
	ArrayList<Commit> commits;
	
	FilesChanged flcg;
	
	HashSet<PRLabel> finalLabels;
	HashSet<PRLabel> addedLabels;
	HashSet<PRLabel> removedLabels;

	
	public PullRequest()
	{
		this.repositoryBranches = new ArrayList<>();
		this.headRepositoryBranches = new ArrayList<>();
		this.participants = new HashSet<>();
		this.commits = new ArrayList<>();
		this.finalLabels = new HashSet<>();
		this.addedLabels = new HashSet<>();
		this.removedLabels = new HashSet<>();	
	}
	
	
	
	public void printPR()
	{
		this.printPRBase();
		this.printScrapingElement();
		this.printParticipants();
		this.printConversation();
		this.printCommit();
		this.printFilesChangedPR();
		this.printLabels();
	}
	
	void printPRBase()
	{
		System.out.println();
		System.out.println("PR Title : "+ this.title);
		System.out.println("PR Id : "+ this.id);
		System.out.println("PR State : "+this.state);
		System.out.println("PR RepositoryName : "+this.repositoryName);
		System.out.println("PR CreateDate : "+this.createDate);
		System.out.println("PR EndDate : "+ this.endDate);
		System.out.println("PR isMerged : "+this.isMerged);
		System.out.println("PR isStandardMerged : "+this.isStandardMerged);
		System.out.println("PR SrcRetrievable : "+this.srcRetrievable);
		System.out.println("PR MergeBranch : "+this.mergeBranch);
		System.out.println("PR HeadBranch : "+this.headBranch);
		System.out.println("PR PrPageUrl : "+this.prPageUrl);
		System.out.println("PR RepositorySrcDLUrl : "+this.repositorySrcDLUrl);
		System.out.println("PR HeadRepositorySrcDLUrl : "+this.headRepositorySrcDLUrl);
		this.dcpt.printDescription();
	}
	
	void printScrapingElement()
	{
		if(this.scrpe!=null)
		{
		  this.scrpe.printScrapingElement();
		}
	}
	
	void printParticipants()
	{
		System.out.println();
		System.out.println("Participants info : ");
		for(Participant prti : this.participants)
		{
			prti.printParticipant();
		}
	}
	
	void printConversation()
	{
		System.out.println();
		System.out.println("Conversation info : ");
		this.conv.printConversation();
		
	}
	
	void printCommit()
	{
		System.out.println();
		System.out.println("Commit info : ");
		for(Commit cmiti : this.commits)
		{
			cmiti.printCommit();
		}
	}
	
	void printFilesChangedPR()
	{
		System.out.println();
		System.out.println("FilesChangedPR info : ");
		this.flcg.printFilesChanged();
	}
	
	void printLabels()
	{
		System.out.println();
		System.out.println("Label info : ");
		System.out.println(" Added Label : ");
		for(PRLabel prlbi : this.addedLabels)
		{
			prlbi.printLabel();
		}
		System.out.println(" Removed Label : ");
		for(PRLabel prlbi : this.removedLabels)
		{
			prlbi.printLabel();
		}
		System.out.println(" Final Label : ");
		for(PRLabel prlbi : this.finalLabels)
		{
			prlbi.printLabel();
		}
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public String getRepositoryName() {
		return repositoryName;
	}



	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}



	public Date getCreateDate() {
		return createDate;
	}



	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public Date getEndDate() {
		return endDate;
	}



	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}



	public boolean isMerged() {
		return isMerged;
	}



	public void setMerged(boolean isMerged) {
		this.isMerged = isMerged;
	}



	public boolean isStandardMerged() {
		return isStandardMerged;
	}



	public void setStandardMerged(boolean isStandardMerged) {
		this.isStandardMerged = isStandardMerged;
	}



	public boolean isSrcRetrievable() {
		return srcRetrievable;
	}



	public void setSrcRetrievable(boolean srcRetrievable) {
		this.srcRetrievable = srcRetrievable;
	}



	public String getMergeBranch() {
		return mergeBranch;
	}



	public void setMergeBranch(String mergeBranch) {
		this.mergeBranch = mergeBranch;
	}



	public String getHeadBranch() {
		return headBranch;
	}



	public void setHeadBranch(String headBranch) {
		this.headBranch = headBranch;
	}



	public String getPrPageUrl() {
		return prPageUrl;
	}



	public void setPrPageUrl(String prPageUrl) {
		this.prPageUrl = prPageUrl;
	}



	public String getRepositorySrcDLUrl() {
		return repositorySrcDLUrl;
	}



	public void setRepositorySrcDLUrl(String repositorySrcDLUrl) {
		this.repositorySrcDLUrl = repositorySrcDLUrl;
	}



	public String getHeadRepositorySrcDLUrl() {
		return headRepositorySrcDLUrl;
	}



	public void setHeadRepositorySrcDLUrl(String headRepositorySrcDLUrl) {
		this.headRepositorySrcDLUrl = headRepositorySrcDLUrl;
	}



	public List<String> getRepositoryBranches() {
		return repositoryBranches;
	}



	public void setRepositoryBranches(List<String> repositoryBranches) {
		this.repositoryBranches = repositoryBranches;
	}



	public List<String> getHeadRepositoryBranches() {
		return headRepositoryBranches;
	}



	public void setHeadRepositoryBranches(List<String> headRepositoryBranches) {
		this.headRepositoryBranches = headRepositoryBranches;
	}



	public Description getDcpt() {
		return dcpt;
	}



	public void setDcpt(Description dcpt) {
		this.dcpt = dcpt;
	}



	public HashSet<Participant> getParticipants() {
		return participants;
	}



	public void setParticipants(HashSet<Participant> participants) {
		this.participants = participants;
	}



	public ScrapingElement getScrpe() {
		return scrpe;
	}



	public void setScrpe(ScrapingElement scrpe) {
		this.scrpe = scrpe;
	}



	public Conversation getConv() {
		return conv;
	}



	public void setConv(Conversation conv) {
		this.conv = conv;
	}



	public ArrayList<Commit> getCommits() {
		return commits;
	}



	public void setCommits(ArrayList<Commit> commits) {
		this.commits = commits;
	}



	public FilesChanged getFlcg() {
		return flcg;
	}



	public void setFlcg(FilesChanged flcg) {
		this.flcg = flcg;
	}



	public HashSet<PRLabel> getFinalLabels() {
		return finalLabels;
	}



	public void setFinalLabels(HashSet<PRLabel> finalLabels) {
		this.finalLabels = finalLabels;
	}



	public HashSet<PRLabel> getAddedLabels() {
		return addedLabels;
	}



	public void setAddedLabels(HashSet<PRLabel> addedLabels) {
		this.addedLabels = addedLabels;
	}



	public HashSet<PRLabel> getRemovedLabels() {
		return removedLabels;
	}



	public void setRemovedLabels(HashSet<PRLabel> removedLabels) {
		this.removedLabels = removedLabels;
	}



	public String getHtmlDescription() {
		return htmlDescription;
	}



	public void setHtmlDescription(String htmlDescription) {
		this.htmlDescription = htmlDescription;
	}
	
	
	
	
	

}
