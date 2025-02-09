/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.prmodel.QuickAccessAPI;

import java.util.List;
import java.util.ArrayList;

import org.jtool.prmodel.IssueComment;
import org.jtool.prmodel.IssueEvent;
import org.jtool.prmodel.Participant;
import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.ReviewComment;
import org.jtool.prmodel.ReviewEvent;

public class ConversationRelevance {
    
    /**
     * Returns the number of issue comments in a pull-request's conversation.
     * @param pullRequest a pull-request
     * @return the number of the IssueComments
     */
    public static int numIssueComments(PullRequest pullRequest) {
        return pullRequest.getConversation().getIssueComments().size();
    }
    
    /**
     * Returns the number of review comments in a pull-request's conversation.
     * @param pullRequest a pull-request
     * @return the number of the ReviewComments
     */
    public static int numReviewComments(PullRequest pullRequest) {
        return pullRequest.getConversation().getReviewComments().size();
    }
    
    /**
     * Returns the number of issue comments and review comments in a pull-request's conversation.
     * @param pullRequest a pull-request
     * @return the number of IssueComments and ReviewComments
     */
    public static int numComments(PullRequest pullRequest) {
        return numReviewComments(pullRequest) + numIssueComments(pullRequest);
    }
    
    /**
     * Returns the collection of issue comments with at least one mention in a pull-request's conversation.
     * @param pullRequest a pull-request
     * @return a List of IssueComments
     */
    public static List<IssueComment> getIssueCommentsWithMentions(PullRequest pullRequest) {
        List<IssueComment> comments = new ArrayList<>();
        for (IssueComment comment : pullRequest.getConversation().getIssueComments()) {
            if (!comment.getMarkdownDoc().getMentionStrings().isEmpty()) {
                comments.add(comment);
            }
        }
        return comments;
    }
    
    /**
     * Returns the collection of review comments with at least one mention in a pull-request's conversation.
     * @param pullRequest a pull-request
     * @return a List of ReviewComments
     */
    public static List<ReviewComment> getReviewCommentsWithMentions(PullRequest pullRequest) {
        List<ReviewComment> comments = new ArrayList<>();
        for (ReviewComment comment : pullRequest.getConversation().getReviewComments()) {
            if (!comment.getMarkdownDoc().getMentionStrings().isEmpty()) {
                comments.add(comment);
            }
        }
        return comments;
    }
    
    /**
     * Returns if at least one mention in a pull-request's conversation.
     * @param pullRequest a pull-request
     * @return true if at least one mention exits
     */
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
    
    /**
     * Returns issue events with a actor in a pull request's conversation.
     * @param pullRequest a pull-request
     * @param participant the Participant who as a actor
     * @return a List of IssueEvents
     */
    public static List<IssueEvent> getIssueEventsByParticipant(PullRequest pullRequest, Participant participant) {
        List<IssueEvent> events = new ArrayList<>();
        for (IssueEvent event : pullRequest.getConversation().getIssueEvents()) {
            if (event.getParticipant().equals(participant)) {
                events.add(event);
            }
        }
        return events;
    }
    
    /**
     * Returns if at least one mention in review events.
     * @param pullRequest a pull-request
     * @return true if at least one mention exits
     */
    public static List<ReviewEvent> getReviewEventsWithMentions(PullRequest pullRequest) {
        List<ReviewEvent> events = new ArrayList<>();
        for (ReviewEvent event : pullRequest.getConversation().getReviewEvents()) {
            if (!event.getMarkdownDoc().getMentionStrings().isEmpty()) {
                events.add(event);
            }
        }
        return events;
    }
    
    /**
     * Returns review events with a actor in a pull request's conversation.
     * @param pullRequest a pull-request
     * @param participant the Participant who as a actor
     * @return a List of ReviewEvents
     */
    public static List<ReviewEvent> getReviewEventsByParticipant(PullRequest pullRequest, Participant participant) {
        List<ReviewEvent> events = new ArrayList<>();
        for (ReviewEvent event : pullRequest.getConversation().getReviewEvents()) {
            if (event.getParticipant().equals(participant)) {
                events.add(event);
            }
        }
        return events;
    }
    
    /**
     * Returns issue comments with a actor in a pull request's conversation.
     * @param pullRequest a pull-request
     * @param participant the Participant who as a actor
     * @return a List of IssueComments
     */
    public static List<IssueComment> getIssueCommentsByParticipant(PullRequest pullRequest, Participant participant) {
        List<IssueComment> comments = new ArrayList<>();
        for (IssueComment comment : pullRequest.getConversation().getIssueComments()) {
            if (comment.getParticipant().equals(participant)) {
                comments.add(comment);
            }
        }
        return comments;
    }
    
    /**
     * Returns review comments with a actor in a pull request's conversation.
     * @param pullRequest a pull-request
     * @param participant the Participant who as a actor
     * @return a List of ReviewComments
     */
    public static List<ReviewComment> getReviewCommentsByParticipant(PullRequest pullRequest, Participant participant) {
        List<ReviewComment> comments = new ArrayList<>();
        for (ReviewComment comment: pullRequest.getConversation().getReviewComments()) {
            if (comment.getParticipant().equals(participant)) {
                comments.add(comment);
            }
        }
        return comments;
    }
    
    /**
     * Returns number of issue events which a participant has joined in a pull request.
     * @param pullRequest a pull-request
     * @param participant the Participant who as a actor
     * @return number of IssueEvents
     */
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
