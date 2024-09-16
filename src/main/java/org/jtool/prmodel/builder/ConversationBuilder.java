package org.jtool.prmodel.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHIssueEvent;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestReview;
import org.kohsuke.github.GHPullRequestReviewComment;

import org.jtool.prmodel.CodeReviewSnippet;
import org.jtool.prmodel.Comment;
import org.jtool.prmodel.Conversation;
import org.jtool.prmodel.Event;
import org.jtool.prmodel.PRAction;
import org.jtool.prmodel.PRModelDate;
import org.jtool.prmodel.Participant;
import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.Review;
import org.jtool.prmodel.ReviewComment;

public class ConversationBuilder {
    
    private final PullRequest pullRequest;
    private final GHPullRequest ghPullRequest;
    
    Map<Long, Event> eventMap = null;
    
    ConversationBuilder(PullRequest pullRequest, GHPullRequest ghPullRequest) {
        this.pullRequest = pullRequest;
        this.ghPullRequest = ghPullRequest;
    }
    
    void build() throws IOException {
        Conversation conversation = new Conversation(pullRequest);
        pullRequest.setConversation(conversation);
        
        buildComment(conversation);
        buildReviewComment(conversation);
        buildEvent(conversation);
        buildReview(conversation);
        buildTimeLine(conversation);
        
        buildCodeReviewSnippet(conversation);
        addCodeReviewSnippet(conversation);
        
    }
    
    private Participant getParticipant(String login) {
        return pullRequest.getParticipants().stream()
                .filter(p -> p.getLogin().equals(login))
                .findFirst().orElse(null);
    }
    
    private void buildComment(Conversation conversation) throws IOException {
        for (GHIssueComment ghComment : ghPullRequest.listComments()) {
            PRModelDate date = new PRModelDate(ghComment.getCreatedAt());
            String body = ghComment.getBody();
            
            Comment comment = new Comment(pullRequest, date, body);
            conversation.getComments().add(comment);
            
            comment.setConversation(conversation);
            comment.setParticipant(getParticipant(ghComment.getUser().getLogin()));
        }
    }
    
    private void buildReviewComment(Conversation conversation) throws IOException {
        for (GHPullRequestReviewComment ghComment : ghPullRequest.listReviewComments()) {
            PRModelDate date = new PRModelDate(ghComment.getCreatedAt());
            String body = ghComment.getBody();
            
            ReviewComment comment = new ReviewComment(pullRequest, date, body);
            conversation.getReviewComments().add(comment);
            
            comment.setConversation(conversation);
            comment.setParticipant(getParticipant(ghComment.getUser().getLogin()));
        }
    }
    
    private void buildEvent(Conversation conversation) throws IOException {
        eventMap = new HashMap<>();
        
        for (GHIssueEvent ghEvent : ghPullRequest.listEvents()) {
            PRModelDate date = new PRModelDate(ghEvent.getCreatedAt());
            String body = ghEvent.getEvent();
            
            Event event = new Event(pullRequest, date, body);
            conversation.getEvents().add(event);
            
            event.setConversation(conversation);
            event.setParticipant(getParticipant(ghEvent.getActor().getLogin()));
            
            long ghId = ghEvent.getId();
            eventMap.put(ghId, event);
        }
    }
    
    private void buildReview(Conversation conversation) throws IOException {
        for (GHPullRequestReview ghReview : ghPullRequest.listReviews()) {
            PRModelDate date = new PRModelDate(ghReview.getCreatedAt());
            String body = ghReview.getBody();
            
            Review review = new Review(pullRequest, date, body);
            conversation.getReviews().add(review);
            
            review.setConversation(conversation);
            review.setParticipant(getParticipant(ghReview.getUser().getLogin()));
        }
    }
    
    private void buildTimeLine(Conversation conversation) {
        List<PRAction> actions = new ArrayList<>();
        conversation.getComments().forEach(c -> actions.add(c));
        conversation.getReviewComments().forEach(c -> actions.add(c));
        conversation.getEvents().forEach(e -> actions.add(e));
        conversation.getReviews().forEach(r -> actions.add(r));
        
        List<PRAction> sortedActions = actions.stream()
                                              .sorted((a1, a2) -> a1.getDate().compareTo(a2.getDate()))
                                              .collect(Collectors.toList());
        conversation.getTimeLine().addAll(sortedActions);
    }
    
    private void buildCodeReviewSnippet(Conversation conversation) throws IOException {
        for (GHPullRequestReviewComment ghComment : ghPullRequest.listReviewComments()) {
            String diffHunk = ghComment.getDiffHunk();
            
            if (conversation.getCodeReviews().stream().anyMatch(s -> s.getDiffHunk().equals(diffHunk))) {
                continue;
            }
            
            CodeReviewSnippet snippet = new CodeReviewSnippet(pullRequest, diffHunk);
            conversation.getCodeReviews().add(snippet);
            
            snippet.setConversation(conversation);
            
            List<GHPullRequestReviewComment> ghComments = new ArrayList<>();
            for (GHPullRequestReviewComment ghComment2 : ghPullRequest.listReviewComments()) {
                if (ghComment.getDiffHunk().equals(ghComment2.getDiffHunk())) {
                    if (!ghComments.stream().anyMatch(c -> c.getBody().equals(ghComment2.getBody()))) {
                        ghComments.add(ghComment2);
                    }
                }
            }
            
            Collections.sort(ghComments, new Comparator<GHPullRequestReviewComment>() {
                @Override
                public int compare(GHPullRequestReviewComment c1 , GHPullRequestReviewComment c2) {
                    try {
                        return c1.getCreatedAt().compareTo(c2.getCreatedAt());
                    } catch (IOException e) {
                        return -33;
                    }
                }
            });
            
            for (GHPullRequestReviewComment ghc : ghComments) {
                ReviewComment comment = conversation.getReviewComments().stream()
                        .filter(c -> c.getBody().equals(ghc.getBody())).findFirst().orElse(null);
                if (comment != null) {
                    snippet.getReviewComments().add(comment);
                    comment.setCodeReviewSnippet(snippet);
                }
            }
        }
    }
    
    private void addCodeReviewSnippet(Conversation conversation) {
        for (ReviewComment comment : conversation.getReviewComments()) {
            for (CodeReviewSnippet snippet : conversation.getCodeReviews()) {
                for (ReviewComment comment2 : snippet.getReviewComments()) {
                    if (comment.getBody().equals(comment2.getBody())) {
                        comment.setCodeReviewSnippet(snippet);
                    }
                }
            }
        }
    }
}
