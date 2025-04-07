/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.prmodel.QuickAccessAPI;

import org.jtool.prmodel.CIStatus;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.Action;
import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.PRModelDate;
import org.jtool.prmodel.IssueComment;
import org.jtool.prmodel.ReviewComment;
import org.jtool.prmodel.IssueEvent;
import org.jtool.prmodel.ReviewEvent;

public class TimeRelevance {
    
    /**
     * Returns time between created date to closed date of a pull request.
     * @param pullRequest a pull-request
     * @return a time in ms 
     */
    public static long lifetime_ms(PullRequest pullRequest) {
        return pullRequest.getEndDate().from(pullRequest.getCreateDate());
    }
    
    /**
     * Returns time between created date to merged date of a pull request.
     * @param pullRequest a pull-request
     * @return a time in ms 
     */
    public static long mergetime_ms(PullRequest pullRequest) {
        if (pullRequest.isMerged() && !pullRequest.isStandardMerged()) {
            for (IssueEvent event: pullRequest.getConversation().getIssueEvents()) {
                if (event.getBody().equals("closed")) {
                    return event.getDate().from(pullRequest.getCreateDate());
                }
            }
        } else if (pullRequest.isStandardMerged()) {
            return lifetime_ms(pullRequest);
        }
        return 0;
    }
    
    /**
     * Returns time between created date to the date of first issue event by reviewer of a pull request.
     * @param pullRequest a pull-request
     * @return a time in ms 
     */
    public static long firstIssueEventResponse_ms(PullRequest pullRequest) {
        for (Action action : pullRequest.getConversation().getTimeLine()) {
            String actionType = action.getActionType();
            if ((actionType.equals(IssueEvent.class.getName()) || actionType.equals("Event")) &&
                    action.getParticipant().getRole().equals("Reviewer")) {
                PRModelDate actionDate = action.getDate();
                return actionDate.from(pullRequest.getCreateDate());
            }
        }
        return 0;
    }
    
    /**
     * Returns time between created date to the date of first review event by reviewer of a pull request.
     * @param pullRequest a pull-request
     * @return a time in ms 
     */
    public static long firstReviewEventResponse_ms(PullRequest pullRequest) {
        for (Action action : pullRequest.getConversation().getTimeLine()) {
            String actionType = action.getActionType();
            if (actionType.equals(ReviewEvent.class.getName()) && action.getParticipant().getRole().equals("Reviewer")) {
                PRModelDate actionDate = action.getDate();
                return actionDate.from(pullRequest.getCreateDate());
            }
        }
        return 0;
    }
    
    /**
     * Returns time between created date to the date of first issue comment by reviewer of a pull request.
     * @param pullRequest a pull-request
     * @return a time in ms 
     */
    public static long firstIssueCommentResponse_ms(PullRequest pullRequest) {
        for (Action action : pullRequest.getConversation().getTimeLine()) {
            String actionType = action.getActionType();
            if ((actionType.equals(IssueComment.class.getName()) || actionType.equals("Comment"))
                    && action.getParticipant().getRole().equals("Reviewer")) {
                PRModelDate actionDate = action.getDate();
                return actionDate.from(pullRequest.getCreateDate());
            }
        }
        return 0;
    }
    
    /**
     * Returns time between created date to the date of first review comment by reviewer of a pull request.
     * @param pullRequest a pull-request
     * @return a time in ms 
     */
    public static long firstReviewCommentResponse_ms(PullRequest pullRequest) {
        for (Action action : pullRequest.getConversation().getTimeLine()) {
            String actionType = action.getActionType();
            if (actionType.equals(ReviewComment.class.getName()) && action.getParticipant().getRole().equals("Reviewer")) {
                PRModelDate actionDate = action.getDate();
                return actionDate.from(pullRequest.getCreateDate());
            }
        }
        return 0;
    }
    
    /**
     * Returns time between created date to the final CI result updated date of a pull request.
     * @param pullRequest a pull-request
     * @return a time in ms 
     */
    public static long totalCILatency_ms(PullRequest pullRequest) {
        int commitSize = pullRequest.getCommits().size();
        if (commitSize > 0) {
           Commit lastCommit = pullRequest.getCommits().get(commitSize - 1);
           int ciSize = lastCommit.getCIStatus().size();
           if (ciSize > 0) {
               CIStatus lastCi = lastCommit.getCIStatus().get(ciSize - 1);
               return lastCi.getUpdateDate().from(pullRequest.getCreateDate());
           }
        }
        return 0;
    }
}
