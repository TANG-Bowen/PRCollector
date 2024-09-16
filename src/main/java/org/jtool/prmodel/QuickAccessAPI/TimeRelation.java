package org.jtool.prmodel.QuickAccessAPI;

import org.jtool.prmodel.CIStatus;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.PRAction;
import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.PRModelDate;
import org.jtool.prmodel.Comment;
import org.jtool.prmodel.ReviewComment;
import org.jtool.prmodel.Event;
import org.jtool.prmodel.Review;

public class TimeRelation {
    
    public static long lifetime_msec(PullRequest pullRequest) {
        return pullRequest.getEndDate().from(pullRequest.getCreateDate());
    }
    
    public static long mergetime_msec(PullRequest pullRequest) {
        if (pullRequest.isMerged() && !pullRequest.isStandardMerged()) {
            for (Event event: pullRequest.getConversation().getEvents()) {
                if (event.getBody().equals("closed")) {
                    return event.getDate().from(pullRequest.getCreateDate());
                }
            }
        } else if (pullRequest.isStandardMerged()) {
            return TimeRelation.lifetime_msec(pullRequest);
        }
        return 0;
    }
    
    public static long firstComment_response(PullRequest pullRequest) {
        for (PRAction action : pullRequest.getConversation().getTimeLine()) {
            if (action.getActionType().equals(Comment.class.getName())) {
                PRModelDate actionDate = action.getDate();
                return actionDate.from(pullRequest.getCreateDate());
            }
        }
        return 0;
    }
    
    public static long firstReviewComment_response(PullRequest pullRequest) {
        for (PRAction action : pullRequest.getConversation().getTimeLine()) {
            if (action.getActionType().equals(ReviewComment.class.getName())) {
                PRModelDate actionDate = action.getDate();
                return actionDate.from(pullRequest.getCreateDate());
            }
        }
        return 0;
    }
    
    public static long firstEvent_response(PullRequest pullRequest) {
        for (PRAction action : pullRequest.getConversation().getTimeLine()) {
            if (action.getActionType().equals(Event.class.getName())) {
                PRModelDate actionDate = action.getDate();
                return actionDate.from(pullRequest.getCreateDate());
            }
        }
        return 0;
    }
    
    public static long firstReview_response(PullRequest pullRequest) {
        for (PRAction action : pullRequest.getConversation().getTimeLine()) {
            if (action.getActionType().equals(Review.class.getName())) {
                PRModelDate actionDate = action.getDate();
                return actionDate.from(pullRequest.getCreateDate());
            }
        }
        return 0;
    }
    
    public static long total_ci_latency(PullRequest pullRequest) {
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
