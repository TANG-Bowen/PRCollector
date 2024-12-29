package org.jtool.prmodel.QuickAccessAPI;

import java.util.ArrayList;
import java.util.List;

import org.jtool.prmodel.IssueComment;
import org.jtool.prmodel.IssueEvent;
import org.jtool.prmodel.Participant;
import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.ReviewComment;
import org.jtool.prmodel.ReviewEvent;

public class ConversationRelevance {
    
    public static int numIssueComments(PullRequest pullRequest) {
        return pullRequest.getConversation().getIssueComments().size();
    }
    
    public static int numReviewComments(PullRequest pullRequest) {
        return pullRequest.getConversation().getReviewComments().size();
    }
    
    public static int numComments(PullRequest pullRequest) {
        return numReviewComments(pullRequest) + numIssueComments(pullRequest);
    }
    
    public static List<IssueComment> getIssueCommentsWithMentions(PullRequest pullRequest) {
        List<IssueComment> comments = new ArrayList<>();
        for (IssueComment comment : pullRequest.getConversation().getIssueComments()) {
            if (!comment.getMarkdownDoc().getMentionStrings().isEmpty()) {
                comments.add(comment);
            }
        }
        return comments;
    }
    
    public static List<ReviewComment> getReviewCommentsWithMentions(PullRequest pullRequest) {
        List<ReviewComment> comments = new ArrayList<>();
        for (ReviewComment comment : pullRequest.getConversation().getReviewComments()) {
            if (!comment.getMarkdownDoc().getMentionStrings().isEmpty()) {
                comments.add(comment);
            }
        }
        return comments;
    }
    
    public static boolean mentionExists(PullRequest pullRequest) {
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
    
    public static List<IssueEvent> getIssueEventsByParticipant(PullRequest pullRequest, Participant participant) {
        List<IssueEvent> events = new ArrayList<>();
        for (IssueEvent event : pullRequest.getConversation().getIssueEvents()) {
            if (event.getParticipant().getPRModelId().equals(participant.getPRModelId())) {
                events.add(event);
            }
        }
        return events;
    }
    
    public static List<ReviewEvent> getReviewEventsWithMentions(PullRequest pullRequest) {
        List<ReviewEvent> events = new ArrayList<>();
        for (ReviewEvent event : pullRequest.getConversation().getReviewEvents()) {
            if (!event.getMarkdownDoc().getMentionStrings().isEmpty()) {
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
    
    public static List<IssueComment> getIssueCommentsByParticipant(PullRequest pullRequest, Participant participant) {
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
    
    public static int numPriorInteractionsByParticipant(PullRequest pullRequest, Participant participant) {
        int num = 0;
        for (Participant pa : pullRequest.getParticipants()) {
            if (pa.getLogin().equals(participant.getLogin())) {
                for (IssueEvent event : pullRequest.getConversation().getIssueEvents()) {
                    if (event.getParticipant().getLogin().equals(participant.getLogin())) {
                        num++;
                    }
                }
            }
        }
        return num;
    }
    
    public static List<IssueEvent> getIssueEventsByParticipant(PullRequest pullRequest, Participant participant) {
        List<IssueEvent> events = new ArrayList<>();
        for (IssueEvent event : pullRequest.getConversation().getIssueEvents()) {
            if (event.getParticipant().equals(participant)) {
                events.add(event);
            }
        }
        return events;
    }
    
    public static List<ReviewEvent> getReviewEventsWithMentions(PullRequest pullRequest) {
        List<ReviewEvent> events = new ArrayList<>();
        for (ReviewEvent event : pullRequest.getConversation().getReviewEvents()) {
            if (!event.getMarkdownDoc().getMentionStrings().isEmpty()) {
                events.add(event);
            }
        }
        return events;
    }
    
    public static List<ReviewEvent> getReviewEventsByParticipant(PullRequest pullRequest, Participant participant) {
        List<ReviewEvent> events = new ArrayList<>();
        for (ReviewEvent event : pullRequest.getConversation().getReviewEvents()) {
            if (event.getParticipant().equals(participant)) {
                events.add(event);
            }
        }
        return events;
    }
    
    public static List<IssueComment> getIssueCommentsByParticipant(PullRequest pullRequest, Participant participant) {
        List<IssueComment> comments = new ArrayList<>();
        for (IssueComment comment : pullRequest.getConversation().getIssueComments()) {
            if (comment.getParticipant().equals(participant)) {
                comments.add(comment);
            }
        }
        return comments;
    }
    
    public static List<ReviewComment> getReviewCommentsByParticipant(PullRequest pullRequest, Participant participant) {
        List<ReviewComment> comments = new ArrayList<>();
        for (ReviewComment comment: pullRequest.getConversation().getReviewComments()) {
            if (comment.getParticipant().equals(participant)) {
                comments.add(comment);
            }
        }
        return comments;
    }
    
    public static int numPriorInteractionsByParticipant(PullRequest pullRequest, Participant participant) {
        int num = 0;
        for (Participant pa : pullRequest.getParticipants()) {
            if (pa.getLogin().equals(participant.getLogin())) {
                for (IssueEvent event : pullRequest.getConversation().getIssueEvents()) {
                    if (event.getParticipant().getLogin().equals(participant.getLogin())) {
                        num++;
                    }
                }
            }
        }
        return num;
    }
}
