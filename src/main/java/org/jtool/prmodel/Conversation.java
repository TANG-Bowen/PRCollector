package org.jtool.prmodel;

import java.util.LinkedHashSet;

public class Conversation extends PRElement {
    
    /* -------- Attributes -------- */
    
    /* -------- Attributes -------- */
    
    private LinkedHashSet<IssueEvent> issueEvents = new LinkedHashSet<>();
    private LinkedHashSet<IssueComment> issuecomments = new LinkedHashSet<>();
    private LinkedHashSet<ReviewEvent> reviewEvents = new LinkedHashSet<>();
    private LinkedHashSet<ReviewComment> reviewComments = new LinkedHashSet<>();
    private LinkedHashSet<CodeReviewSnippet> codeReviews = new LinkedHashSet<>();
    private LinkedHashSet<Action> timeLine = new LinkedHashSet<>();
    
    public Conversation(PullRequest pullRequest) {
        super(pullRequest);
    }
    
    public void print() {
        String prefix = "Conversation ";
        System.out.println();
        System.out.println(prefix + super.toString());
        issueEvents.forEach(e -> e.print());
        issuecomments.forEach(e -> e.print());
        reviewEvents.forEach(e -> e.print());
        reviewComments.forEach(e -> e.print());
        codeReviews.forEach(e -> e.print());
        
        timeLine.forEach(e -> e.print("Action "));
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public LinkedHashSet<IssueEvent> getIssueEvents() {
        return issueEvents;
    }
    
    
    public LinkedHashSet<IssueComment> getIssueComments() {
        return issuecomments;
    }
    
    public LinkedHashSet<ReviewEvent> getReviewEvents() {
        return reviewEvents;
    }
    
    public LinkedHashSet<ReviewComment> getReviewComments() {
        return reviewComments;
    }
    
    public LinkedHashSet<CodeReviewSnippet> getCodeReviews() {
        return codeReviews;
    }
    
    public LinkedHashSet<Action> getTimeLine() {
        return timeLine;
    }
}
