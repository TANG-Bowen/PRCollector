package jwrmodel;

import java.util.ArrayList;
import java.util.HashSet;

public class Str_Participant {
	
	String instId;
	String loginName="";
	String name="";
	long id;
	String role="";
	String location="";
	HashSet<String> record;//record of action
	ArrayList<String> followers;
	ArrayList<String> followings;
	
	public Str_Participant()
	{
		
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public HashSet<String> getRecord() {
		return record;
	}

	public void setRecord(HashSet<String> record) {
		this.record = record;
	}

	public ArrayList<String> getFollowers() {
		return followers;
	}

	public void setFollowers(ArrayList<String> followers) {
		this.followers = followers;
	}

	public ArrayList<String> getFollowings() {
		return followings;
	}

	public void setFollowings(ArrayList<String> followings) {
		this.followings = followings;
	}
	
	

}
