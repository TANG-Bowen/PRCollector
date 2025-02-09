/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

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
    
    /**
     * Returns IssueEvents inside.
     * @return a LinkedHashSet of IssueEvents
     */
    public LinkedHashSet<IssueEvent> getIssueEvents() {
        return issueEvents;
    }
    
    /**
     * Returns IssueComments inside.
     * @return a LinkedHashSet of IssueComments
     */
    public LinkedHashSet<IssueComment> getIssueComments() {
        return issuecomments;
    }
    
    /**
     * Returns ReviewEvents inside.
     * @return a LinkedHashSet of ReviewEvents
     */
    public LinkedHashSet<ReviewEvent> getReviewEvents() {
        return reviewEvents;
    }
    
    /**
     * Returns ReviewComments inside.
     * @return a LinkedHashSet of ReviewComments
     */
    public LinkedHashSet<ReviewComment> getReviewComments() {
        return reviewComments;
    }
    
    /**
     * Returns CodeReviews inside.
     * @return a LinkedHashSet of CodeReviews
     */
    public LinkedHashSet<CodeReviewSnippet> getCodeReviews() {
        return codeReviews;
    }
    
    /**
     * Returns Timeline inside.
     * @return a LinkedHashSet of Actions
     */
    public LinkedHashSet<Action> getTimeLine() {
        return timeLine;
    }
}
