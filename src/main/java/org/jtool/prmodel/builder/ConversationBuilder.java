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
import org.jtool.prmodel.IssueComment;
import org.jtool.prmodel.Conversation;
import org.jtool.prmodel.IssueEvent;
import org.jtool.prmodel.Action;
import org.jtool.prmodel.PRModelDate;
import org.jtool.prmodel.Participant;
import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.ReviewEvent;
import org.jtool.prmodel.ReviewComment;

public class ConversationBuilder {
    
    private List<Exception> exceptions = new ArrayList<>();
    
    private final PullRequest pullRequest;
    private final GHPullRequest ghPullRequest;
    
    private Map<Long, IssueEvent> eventMap = null;
    
    ConversationBuilder(PullRequest pullRequest, GHPullRequest ghPullRequest) {
        this.pullRequest = pullRequest;
        this.ghPullRequest = ghPullRequest;
    }
    
    void build() {
        Conversation conversation = new Conversation(pullRequest);
        pullRequest.setConversation(conversation);
        
        buildIssueEvent(conversation);
        buildIssueComment(conversation);
        buildReviewEvent(conversation);
        buildReviewComment(conversation);
        
        buildTimeLine(conversation);
        
        buildCodeReviewSnippet(conversation);
        addCodeReviewSnippet(conversation);
    }
    
    private Participant getParticipant(String login) {
        return pullRequest.getParticipants().stream()
                .filter(p -> p.getLogin().equals(login))
                .findFirst().orElse(null);
    }
    
    private void buildIssueEvent(Conversation conversation) {
        eventMap = new HashMap<>();
        try {
            for (GHIssueEvent ghEvent : ghPullRequest.listEvents()) {
                PRModelDate date = new PRModelDate(ghEvent.getCreatedAt());
                String body = ghEvent.getEvent();
                
                IssueEvent event = new IssueEvent(pullRequest, date, body);
                conversation.getIssueEvents().add(event);
                
                event.setConversation(conversation);
                event.setParticipant(getParticipant(ghEvent.getActor().getLogin()));
                
                long ghId = ghEvent.getId();
                eventMap.put(ghId, event);
            }
        } catch (IOException e) {
            pullRequest.setEventRetrievable(false);
            exceptions.add(e);
        }
    }
    
    private void buildIssueComment(Conversation conversation) {
        try {
            for (GHIssueComment ghComment : ghPullRequest.listComments()) {
                try {
                    PRModelDate date = new PRModelDate(ghComment.getCreatedAt());
                    String body = ghComment.getBody();
                    
                    IssueComment comment = new IssueComment(pullRequest, date, body);
                    conversation.getIssueComments().add(comment);
                    
                    comment.setConversation(conversation);
                    comment.setParticipant(getParticipant(ghComment.getUser().getLogin()));
                } catch (IOException e) {
                    pullRequest.setCommentRetrievable(false);
                    exceptions.add(e);
                }
            }
        } catch (IOException e) {
            pullRequest.setCommentRetrievable(false);
            exceptions.add(e);
        }
    }
    
    private void buildReviewEvent(Conversation conversation) {
        for (GHPullRequestReview ghReview : ghPullRequest.listReviews()) {
            try {
                PRModelDate date = new PRModelDate(ghReview.getCreatedAt());
                String body = ghReview.getBody();
                
                ReviewEvent review = new ReviewEvent(pullRequest, date, body);
                conversation.getReviewEvents().add(review);
                
                review.setConversation(conversation);
                review.setParticipant(getParticipant(ghReview.getUser().getLogin()));
            } catch (IOException e) {
                pullRequest.setReviewEventRetrievable(false);
                exceptions.add(e);
            }
        }
    }
    
    private void buildReviewComment(Conversation conversation) {
        try {
            for (GHPullRequestReviewComment ghComment : ghPullRequest.listReviewComments()) {
                try {
                    PRModelDate date = new PRModelDate(ghComment.getCreatedAt());
                    String body = ghComment.getBody();
                    
                    ReviewComment comment = new ReviewComment(pullRequest, date, body);
                    conversation.getReviewComments().add(comment);
                    
                    comment.setConversation(conversation);
                    comment.setParticipant(getParticipant(ghComment.getUser().getLogin()));
                } catch (IOException e) {
                    pullRequest.setReviewCommentRetrievable(false);
                    exceptions.add(e);
                }
            }
        } catch (IOException e) {
            pullRequest.setReviewCommentRetrievable(false);
            exceptions.add(e);
        }
    }
    
    private void buildTimeLine(Conversation conversation) {
        List<Action> actions = new ArrayList<>();
        conversation.getIssueEvents().forEach(e -> actions.add(e));
        conversation.getIssueComments().forEach(c -> actions.add(c));
        conversation.getReviewEvents().forEach(r -> actions.add(r));
        conversation.getReviewComments().forEach(c -> actions.add(c));
        
        List<Action> sortedActions = actions.stream()
                                              .sorted((a1, a2) -> a1.getDate().compareTo(a2.getDate()))
                                              .collect(Collectors.toList());
        conversation.getTimeLine().addAll(sortedActions);
    }
    
    private void buildCodeReviewSnippet(Conversation conversation) {
        try {
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
        } catch (IOException e) {
            /* empty */
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
    
    public Map<Long, IssueEvent> getEventMap() {
        return eventMap;
    }
    
    List<Exception> getExceptions() {
        return exceptions;
    }
}
