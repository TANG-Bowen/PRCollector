package org.jtool.prmodel.QuickAccessAPI;

import java.util.ArrayList;
import java.util.List;

import org.jtool.prmodel.IssueComment;
import org.jtool.prmodel.IssueEvent;
import org.jtool.prmodel.Participant;
import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.ReviewComment;
import org.jtool.prmodel.Review;

public class ConversationRelation {
    
    public static int num_commit_comments(PullRequest pullRequest) {
        return pullRequest.getConversation().getReviewComments().size();
    }
    
    public static int num_issue_comments(PullRequest pullRequest) {
        return pullRequest.getConversation().getIssueComments().size();
    }
    
    public static int num_comments(PullRequest pullRequest) {
        return pullRequest.getConversation().getReviewComments().size()
                + pullRequest.getConversation().getIssueComments().size();
    }
    
    public static List<IssueComment> commentsHaveMentions(PullRequest pullRequest) {
        List<IssueComment> comments = new ArrayList<>();
        for (IssueComment comment : pullRequest.getConversation().getIssueComments()) {
            if (!comment.getMarkdownDoc().getMentionStrings().isEmpty()) {
                comments.add(comment);
            }
        }
        return comments;
    }
    
    public static List<ReviewComment> reviewCommentsHaveMentions(PullRequest pullRequest) {
        List<ReviewComment> reviewcomments = new ArrayList<>();
        for (ReviewComment comment : pullRequest.getConversation().getReviewComments()) {
            if (!comment.getMarkdownDoc().getMentionStrings().isEmpty()) {
                reviewcomments.add(comment);
            }
        }
        return reviewcomments;
    }
    
    public static List<Review> reviewEventsHaveMentions(PullRequest pullRequest) {
        List<Review> reviews = new ArrayList<>();
        for (Review review : pullRequest.getConversation().getReviews()) {
            if (!review.getMarkdownDoc().getMentionStrings().isEmpty()) {
                reviews.add(review);
            }
        }
        return reviews;
    }
    
    public static List<IssueComment> getCommentsByParticipant(PullRequest pullRequest, Participant participant) {
        List<IssueComment> comments = new ArrayList<>();
        for (IssueComment comment : pullRequest.getConversation().getIssueComments()) {
            if (comment.getParticipant().getPRModelId().equals(participant.getPRModelId())) {
                comments.add(comment);
            }
        }
        return comments;
    }
    
    public static List<ReviewComment> getReviewCommentsByParticipant(PullRequest pullRequest, Participant participant) {
        List<ReviewComment> reviewComments = new ArrayList<>();
        for (ReviewComment comment: pullRequest.getConversation().getReviewComments()) {
            if (comment.getParticipant().getPRModelId().equals(participant.getPRModelId())) {
                reviewComments.add(comment);
            }
        }
        return reviewComments;
    }
    
    public List<IssueEvent> getEventsByParticipant(PullRequest pullRequest, Participant participant) {
        List<IssueEvent> events = new ArrayList<>();
        for (IssueEvent event : pullRequest.getConversation().getIssueEvents()) {
            if (event.getParticipant().getPRModelId().equals(participant.getPRModelId())) {
                events.add(event);
            }
        }
        return events;
    }
    
    public static List<Review> getReviewEventsByParticipant(PullRequest pullRequest, Participant participant) {
        List<Review> reviews = new ArrayList<>();
        for (Review review : pullRequest.getConversation().getReviews()) {
            if (review.getParticipant().getPRModelId().equals(participant.getPRModelId())) {
                reviews.add(review);
            }
        }
        return reviews;
    }
    
    public static boolean mentionExist(PullRequest pullRequest) {
        boolean hasMention = false;
        for (IssueComment comment : pullRequest.getConversation().getIssueComments()) {
            if (!comment.getMarkdownDoc().getMentionStrings().isEmpty()) {
                hasMention = true;
                break;
            }
        }
        
        for (ReviewComment comment : pullRequest.getConversation().getReviewComments()) {
            if (!comment.getMarkdownDoc().getMentionStrings().isEmpty()) {
                hasMention = true;
                break;
            }
        }
        
        for (Review review : pullRequest.getConversation().getReviews()) {
            if (!review.getMarkdownDoc().getMentionStrings().isEmpty()) {
                hasMention=true;
                break;
            }
        }
        return hasMention;
    }
}
