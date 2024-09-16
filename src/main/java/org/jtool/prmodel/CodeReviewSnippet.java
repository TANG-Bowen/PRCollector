package org.jtool.prmodel;

import java.util.List;
import java.util.ArrayList;

public class CodeReviewSnippet extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String diffHunk;
    
    /* -------- Attributes -------- */
    
    private Conversation conversation;
    private List<ReviewComment> reviewComments = new ArrayList<>();
    
    public CodeReviewSnippet(PullRequest pullRequest, String diffHunk) {
        super(pullRequest);
        this.diffHunk = diffHunk;
    }
    
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
    
    public void addReviewComment(ReviewComment comment) {
        reviewComments.add(comment);
    }
    
    public void print() {
        String prefix = "CodeReview ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + diffHunk);
        System.out.println(prefix + " reviewComments : " + toPRElemList(reviewComments));
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public String getDiffHunk() {
        return diffHunk;
    }
    
    public Conversation getConversation() {
        return conversation;
    }
    
    public List<ReviewComment> getReviewComments() {
        return reviewComments;
    }
}
