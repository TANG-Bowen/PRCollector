package prmodel;

import java.util.ArrayList;
import java.util.HashSet;
import org.kohsuke.github.GHUser;

public class Participant {
	
	String instId = "";
	GHUser user;
	String loginName="";
	String name="";
	long id;
	String role="";
	String location="";
	HashSet<String> record;//record of action
	ArrayList<String> followers;
	ArrayList<String> followings;
	
	PullRequest pr;
	
	
	public Participant(PullRequest pr)
	{
		record = new HashSet<>();
		followers = new ArrayList<>();
		followings = new ArrayList<>();
		this.pr = pr;
	}
	
	void printParticipant()
	{
		System.out.println();
		System.out.println("Participant info : ");
		System.out.println("Participant instId : "+this.instId);
		System.out.println("Participant loginName : "+this.loginName);
		System.out.println("Participant name : "+this.name);
		System.out.println("Participant id : "+this.id);
		System.out.println("Participant location : "+this.location);
		System.out.println("Participant role : "+this.role);
		System.out.println("Participant followers size : "+this.followers.size());
		System.out.println("Participant followings size : "+this.followings.size());
		for(String recordi : this.record)
		{
			System.out.println(" recordi : "+recordi);
		}
	}
	
	GHUser getGhUser()
	{
		return this.user;
	}
	
	PullRequest getPR()
	{
		return this.pr;
	}
	
	//API Outside

	
	public String getInstId() {
		return instId;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}

	public String getRole() {
		return role;
	}

	public String getLocation() {
		return location;
	}

	public HashSet<String> getRecord() {
		return record;
	}

	public ArrayList<String> getFollowers() {
		return followers;
	}

	public ArrayList<String> getFollowings() {
		return followings;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public GHUser getUser() {
		return user;
	}

	public void setUser(GHUser user) {
		this.user = user;
	}

	public PullRequest getPr() {
		return pr;
	}

	public void setPr(PullRequest pr) {
		this.pr = pr;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setRecord(HashSet<String> record) {
		this.record = record;
	}

	public void setFollowers(ArrayList<String> followers) {
		this.followers = followers;
	}

	public void setFollowings(ArrayList<String> followings) {
		this.followings = followings;
	}
	

}
