package org.jtool.prmodel.builder;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHIssueEvent;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestCommitDetail;
import org.kohsuke.github.GHPullRequestReview;
import org.kohsuke.github.GHPullRequestReviewComment;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.PagedIterable;

import org.jtool.prmodel.Participant;
import org.jtool.prmodel.PullRequest;

public class ParticipantBuilder {
    
    private List<Exception> exceptions = new ArrayList<>();
    
    private final PullRequest pullRequest;
    private final GHPullRequest ghPullRequest;
    
    ParticipantBuilder(PullRequest pullRequest, GHPullRequest ghPullRequest) {
        this.pullRequest = pullRequest;
        this.ghPullRequest = ghPullRequest;
    }
    
    void build() {
        try {
            GHUser author = ghPullRequest.getUser();
            Participant participant = createParticipant(author, "Author");
            participant.getActionRecord().add("Creation");
        } catch (IOException e) {
            Participant participant = createUnknownParticipant("Author");
            participant.getActionRecord().add("Creation");
            exceptions.add(e);
        }
        
        try {
            for (GHIssueComment comment : ghPullRequest.getComments()) {
                try {
                    GHUser reviewer = comment.getUser();
                    Participant participant = checkAndCreateReviewer(reviewer, "Reviewer");
                    participant.getActionRecord().add("Comment");
                } catch (IOException e) {
                    Participant participant = checkAndCreateUnknownReviewer("Reviewer");
                    participant.getActionRecord().add("Comment");
                    exceptions.add(e);
                }
            }
        } catch (IOException e) {
            Participant participant = checkAndCreateUnknownReviewer("Reviewer");
            participant.getActionRecord().add("Comment");
            exceptions.add(e);
        }
        
        try {
            for (GHPullRequestReviewComment comment : ghPullRequest.listReviewComments()) {
                try {
                    GHUser reviewer = comment.getUser();
                    Participant participant = checkAndCreateReviewer(reviewer, "Reviewer");
                    participant.getActionRecord().add("ReviewComment");
                } catch (IOException e) {
                    Participant participant = checkAndCreateUnknownReviewer("Reviewer");
                    participant.getActionRecord().add("Comment");
                    exceptions.add(e);
                }
            }
        } catch (IOException e) {
            Participant participant = checkAndCreateUnknownReviewer("Reviewer");
            participant.getActionRecord().add("Comment");
            exceptions.add(e);
        }
        
        try {
            for (GHIssueEvent event : ghPullRequest.listEvents()) {
                GHUser reviewer = event.getActor();
                Participant participant = checkAndCreateReviewer(reviewer, "Reviewer");
                participant.getActionRecord().add("Event");
            }
        } catch (IOException e) {
            Participant participant = checkAndCreateUnknownReviewer("Reviewer");
            participant.getActionRecord().add("Comment");
            exceptions.add(e);
        }
        
        for (GHPullRequestReview review : ghPullRequest.listReviews()) {
            try {
                GHUser reviewer = review.getUser();
                Participant participant = checkAndCreateReviewer(reviewer, "Reviewer");
                participant.getActionRecord().add("Review");
            } catch (IOException e) {
                Participant participant = createUnknownParticipant("Reviewer");
                participant.getActionRecord().add("Comment");
                exceptions.add(e);
            }
        }
        
        PagedIterable<GHPullRequestCommitDetail> ghCommits = ghPullRequest.listCommits();
        for (GHPullRequestCommitDetail commit : ghCommits) {
            try {
                GHCommit ghCommit = ghPullRequest.getRepository().getCommit(commit.getSha());
                
                try {
                    GHUser commiter = ghCommit.getAuthor();
                    Participant participant = checkAndCreateReviewer(commiter, "Author");
                    participant.getActionRecord().add("Commit");
                } catch (IOException e) {
                    Participant participant = checkAndCreateUnknownReviewer("Author");
                    participant.getActionRecord().add("Comment");
                    exceptions.add(e);
                }
            } catch (IOException e) {
                Participant participant = checkAndCreateUnknownReviewer("Author");
                participant.getActionRecord().add("Comment");
                exceptions.add(e);
            }
        }
    }
    
    Participant createParticipant(GHUser ghUser, String role) {
        long userId = ghUser.getId();
        String login = ghUser.getLogin();
        String name;
        String location;
        List<String> followers;
        List<String> follows;
        try {
            name = ghUser.getName();
        } catch (IOException e) {
            name = PRModelBuilder.UNKNOWN_SYMBOL;
            pullRequest.setParticipantRetrievable(false);
            exceptions.add(e);
        }
        try {
            location = ghUser.getLocation();
        } catch (IOException e) {
            location = PRModelBuilder.UNKNOWN_SYMBOL;
            pullRequest.setParticipantRetrievable(false);
            exceptions.add(e);
        }
        try {
            followers = ghUser.getFollowers().stream().map(u -> u.getLogin()).collect(Collectors.toList());
        } catch (IOException e) {
            followers = new ArrayList<>(Arrays.asList(PRModelBuilder.UNKNOWN_SYMBOL));
            pullRequest.setParticipantRetrievable(false);
            exceptions.add(e);
        }
        try {
            follows = ghUser.getFollows().stream().map(u -> u.getLogin()).collect(Collectors.toList());
        } catch (IOException e) {
            follows = new ArrayList<>(Arrays.asList(PRModelBuilder.UNKNOWN_SYMBOL));
            pullRequest.setParticipantRetrievable(false);
            exceptions.add(e);
        }
        
        Participant participant = new Participant(pullRequest,
                userId, login, name, location, role, followers, follows);
        pullRequest.addParticipant(participant);
        return participant;
    }
    
    Participant createUnknownParticipant(String role) {
        long userId = -1;
        String login = PRModelBuilder.UNKNOWN_SYMBOL;
        String name = PRModelBuilder.UNKNOWN_SYMBOL;
        String location = PRModelBuilder.UNKNOWN_SYMBOL;
        List<String> followers = new ArrayList<>(Arrays.asList(PRModelBuilder.UNKNOWN_SYMBOL));
        List<String> follows = new ArrayList<>(Arrays.asList(PRModelBuilder.UNKNOWN_SYMBOL));
        pullRequest.setParticipantRetrievable(false);
        
        Participant participant = new Participant(pullRequest,
                userId, login, name, location, role, followers, follows);
        pullRequest.addParticipant(participant);
        return participant;
    }
    
    Participant checkAndCreateReviewer(GHUser ghUser, String role) {
        Participant participant = existsParticipant(ghUser);
        if (participant == null) {
            participant = createParticipant(ghUser, role);
        }
        return participant;
    }
    
    Participant checkAndCreateUnknownReviewer(String role) {
        Participant participant = existsParticipant(PRModelBuilder.UNKNOWN_SYMBOL);
        if (participant == null) {
            participant = createUnknownParticipant(role);
        }
        return participant;
    }
    
    Participant existsParticipant(GHUser ghUser) {
        return pullRequest.getParticipants().stream()
                          .filter(p -> p.getLogin().equals(ghUser.getLogin()))
                          .findFirst().orElse(null);
    }
    
    Participant existsParticipant(long userId) {
        return pullRequest.getParticipants().stream()
                          .filter(p -> p.getUserId() == userId)
                          .findFirst().orElse(null);
    }
    
    Participant existsParticipant(String name) {
        return pullRequest.getParticipants().stream()
                          .filter(p -> p.getName() == name)
                          .findFirst().orElse(null);
    }
    
    List<Exception> getExceptions() {
        return exceptions;
    }
}
