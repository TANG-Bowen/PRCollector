package org.jtool.prmodel;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Participant extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final long userId;
    private final String login;
    private final String name;
    private final String location;
    private final String role;
    private final List<String> followers;
    private final List<String> follows;
    private final Set<String> actionRecord = new HashSet<>();
    
    /* -------- Attributes -------- */
    
    public Participant(PullRequest pullRequest, long userId, String login, String name, String location, String role,
            List<String> followers, List<String> follows) {
        super(pullRequest);
        this.userId = userId;
        this.login = login;
        this.name = name;
        this.location = location;
        this.role = role;
        this.followers = followers;
        this.follows = follows;
    }
    
    public void print() {
        String prefix = "Participant";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "userId : " + userId);
        System.out.println(prefix + "login : " + login);
        System.out.println(prefix + "name : " + name);
        System.out.println(prefix + "location : " + location);
        System.out.println(prefix + "role : " + role);
        System.out.println(prefix + "followers : " + followers.size());
        System.out.println(prefix + "follows : " + follows.size());
        System.out.println(prefix + "actionRecord : " + toStringList(actionRecord));
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public long getUserId() {
        return userId;
    }
    
    public String getLogin() {
        return login;
    }
    
    public String getName() {
        return name;
    }
    
    public String getLocation() {
        return location;
    }
    
    public String getRole() {
        return role;
    }
    
    public List<String> getFollowers() {
        return followers;
    }
    
    public List<String> getFollows() {
        return follows;
    }
    
    public Set<String> getActionRecord() {
        return actionRecord;
    }
}
