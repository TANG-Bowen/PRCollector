package org.jtool.prmodel;

import java.util.Set;
import java.util.HashSet;

public class HTMLDescription extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String body;
    
    /* -------- Attributes -------- */
    
    private Set<String> mentionUsers = new HashSet<>(); // name with @
    
    public HTMLDescription(PullRequest pullRequest, String body) {
        super(pullRequest);
        this.body = body;
    }
    
    public void print() {
        String prefix = "HTMLDescription ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "mentionUsers : "+ toSortedStringList(mentionUsers));
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    /**
     * Returns HTML description body.
     * @return body String
     */
    public String getBody() {
        return body;
    }
    
    /**
     * Returns users who are mentioned by markdown element.
     * @return a Set of login name Strings
     */
    public Set<String> getMentionUsers() {
        return mentionUsers;
    }
}
