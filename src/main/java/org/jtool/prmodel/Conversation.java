package org.jtool.prmodel;

import java.util.LinkedHashSet;

public class Conversation extends PRElement {
    
    /* -------- Attributes -------- */
    
    /* -------- Attributes -------- */
    
    private LinkedHashSet<Comment> comments = new LinkedHashSet<>();
    private LinkedHashSet<ReviewComment> reviewComments = new LinkedHashSet<>();
    private LinkedHashSet<Event> events = new LinkedHashSet<>();
    private LinkedHashSet<Review> reviews = new LinkedHashSet<>();
    private LinkedHashSet<CodeReviewSnippet> codeReviews = new LinkedHashSet<>();
    private LinkedHashSet<PRAction> timeLine = new LinkedHashSet<>();
    
    public Conversation(PullRequest pullRequest) {
        super(pullRequest);
    }
    
    public void print() {
        String prefix = "Conversation ";
        System.out.println();
        System.out.println(prefix + super.toString());
        comments.forEach(e -> e.print());
        reviewComments.forEach(e -> e.print());
        events.forEach(e -> e.print());
        reviews.forEach(e -> e.print());
        codeReviews.forEach(e -> e.print());
        timeLine.forEach(e -> e.print("PRAction "));
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public LinkedHashSet<Comment> getComments() {
        return comments;
    }
    
    public LinkedHashSet<ReviewComment> getReviewComments() {
        return reviewComments;
    }
    
    public LinkedHashSet<Event> getEvents() {
        return events;
    }
    
    public LinkedHashSet<Review> getReviews() {
        return reviews;
    }
    
    public LinkedHashSet<CodeReviewSnippet> getCodeReviews() {
        return codeReviews;
    }
    
    public LinkedHashSet<PRAction> getTimeLine() {
        return timeLine;
    }
}
