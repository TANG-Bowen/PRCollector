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

public class TimeFeature {
    
    public static long lifetime_ms(PullRequest pullRequest) {
        return pullRequest.getEndDate().from(pullRequest.getCreateDate());
    }
    
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
    
    public static long firstIssueEventResponse_ms(PullRequest pullRequest) {
        for (Action action : pullRequest.getConversation().getTimeLine()) {
            if (action.getActionType().equals(IssueEvent.class.getName())) {
                PRModelDate actionDate = action.getDate();
                return actionDate.from(pullRequest.getCreateDate());
            }
        }
        return 0;
    }
    
    public static long firstReviewEventResponse_ms(PullRequest pullRequest) {
        for (Action action : pullRequest.getConversation().getTimeLine()) {
            if (action.getActionType().equals(ReviewEvent.class.getName())) {
                PRModelDate actionDate = action.getDate();
                return actionDate.from(pullRequest.getCreateDate());
            }
        }
        return 0;
    }
    
    public static long firstIssueCommentResponse_ms(PullRequest pullRequest) {
        for (Action action : pullRequest.getConversation().getTimeLine()) {
            if (action.getActionType().equals(IssueComment.class.getName())) {
                PRModelDate actionDate = action.getDate();
                return actionDate.from(pullRequest.getCreateDate());
            }
        }
        return 0;
    }
    
    public static long firstReviewCommentResponse_ms(PullRequest pullRequest) {
        for (Action action : pullRequest.getConversation().getTimeLine()) {
            if (action.getActionType().equals(ReviewComment.class.getName())) {
                PRModelDate actionDate = action.getDate();
                return actionDate.from(pullRequest.getCreateDate());
            }
        }
        return 0;
    }
    
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
