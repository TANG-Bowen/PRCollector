package org.jtool.prmodel;

import java.util.LinkedHashSet;

public class Conversation extends PRElement {
    
    /* -------- Attributes -------- */
    
    /* -------- Attributes -------- */
    
    private LinkedHashSet<IssueComment> issuecomments = new LinkedHashSet<>();
    private LinkedHashSet<IssueEvent> issueEvents = new LinkedHashSet<>();
    private LinkedHashSet<Review> reviews = new LinkedHashSet<>();
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
        issuecomments.forEach(e -> e.print());
        reviewComments.forEach(e -> e.print());
        issueEvents.forEach(e -> e.print());
        reviews.forEach(e -> e.print());
        codeReviews.forEach(e -> e.print());
        timeLine.forEach(e -> e.print("Action "));
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public LinkedHashSet<IssueComment> getIssueComments() {
        return issuecomments;
    }
    
    public LinkedHashSet<ReviewComment> getReviewComments() {
        return reviewComments;
    }
    
    public LinkedHashSet<IssueEvent> getIssueEvents() {
        return issueEvents;
    }
    
    public LinkedHashSet<Review> getReviews() {
        return reviews;
    }
    
    public LinkedHashSet<CodeReviewSnippet> getCodeReviews() {
        return codeReviews;
    }
    
    public LinkedHashSet<Action> getTimeLine() {
        return timeLine;
    }
}
