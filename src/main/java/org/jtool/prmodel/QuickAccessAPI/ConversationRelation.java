package org.jtool.prmodel.QuickAccessAPI;

import java.util.ArrayList;
import java.util.List;

import org.jtool.prmodel.IssueComment;
import org.jtool.prmodel.IssueEvent;
import org.jtool.prmodel.Participant;
import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.ReviewComment;
import org.jtool.prmodel.ReviewEvent;

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
        List<ReviewComment> comments = new ArrayList<>();
        for (ReviewComment comment : pullRequest.getConversation().getReviewComments()) {
            if (!comment.getMarkdownDoc().getMentionStrings().isEmpty()) {
                comments.add(comment);
            }
        }
        return comments;
    }
    
    public static List<ReviewEvent> reviewEventsHaveMentions(PullRequest pullRequest) {
        List<ReviewEvent> events = new ArrayList<>();
        for (ReviewEvent event : pullRequest.getConversation().getReviewEvents()) {
            if (!event.getMarkdownDoc().getMentionStrings().isEmpty()) {
                events.add(event);
            }
        }
        return events;
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
        List<ReviewComment> comments = new ArrayList<>();
        for (ReviewComment comment: pullRequest.getConversation().getReviewComments()) {
            if (comment.getParticipant().getPRModelId().equals(participant.getPRModelId())) {
                comments.add(comment);
            }
        }
        return comments;
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
    
    public static List<ReviewEvent> getReviewEventsByParticipant(PullRequest pullRequest, Participant participant) {
        List<ReviewEvent> events = new ArrayList<>();
        for (ReviewEvent event : pullRequest.getConversation().getReviewEvents()) {
            if (event.getParticipant().getPRModelId().equals(participant.getPRModelId())) {
                events.add(event);
            }
        }
        return events;
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
        
        for (ReviewEvent review : pullRequest.getConversation().getReviewEvents()) {
            if (!review.getMarkdownDoc().getMentionStrings().isEmpty()) {
                hasMention = true;
                break;
            }
        }
        return hasMention;
    }
}
