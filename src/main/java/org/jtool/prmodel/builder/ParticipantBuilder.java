package org.jtool.prmodel.builder;

import java.io.IOException;
import java.util.List;
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
    
    private final PullRequest pullRequest;
    private final GHPullRequest ghPullRequest;
    
    ParticipantBuilder(PullRequest pullRequest, GHPullRequest ghPullRequest) {
        this.pullRequest = pullRequest;
        this.ghPullRequest = ghPullRequest;
    }
    
    void build() throws IOException {
        GHUser author = ghPullRequest.getUser();
        Participant participant = createParticipant(pullRequest, author, "Author");
        participant.getActionRecord().add("Creation");
        
        for (GHIssueComment comment : ghPullRequest.getComments()) {
            GHUser user = comment.getUser();
            Participant reviewer = checkAndCreateReviewer(pullRequest, user, "Reviewer");
            reviewer.getActionRecord().add("Comment");
        }
        
        for (GHPullRequestReviewComment comment : ghPullRequest.listReviewComments()) {
            GHUser user = comment.getUser();
            Participant reviewer = checkAndCreateReviewer(pullRequest, user, "Reviewer");
            reviewer.getActionRecord().add("ReviewComment");
        }
        
        for (GHIssueEvent event : ghPullRequest.listEvents()) {
            GHUser user = event.getActor();
            Participant reviewer = checkAndCreateReviewer(pullRequest, user, "Reviewer");
            reviewer.getActionRecord().add("Event");
        }
        
        for (GHPullRequestReview review : ghPullRequest.listReviews()) {
            GHUser user = review.getUser();
            Participant reviewer = checkAndCreateReviewer(pullRequest, user, "Reviewer");
            reviewer.getActionRecord().add("Review");
        }
        
        PagedIterable<GHPullRequestCommitDetail> ghCommits = ghPullRequest.listCommits();
        for (GHPullRequestCommitDetail commit : ghCommits) {
            GHCommit ghCommit = ghPullRequest.getRepository().getCommit(commit.getSha());
            GHUser user = ghCommit.getAuthor();
            Participant commiter = checkAndCreateReviewer(pullRequest, user, "Author");
            commiter.getActionRecord().add("Commit");
        }
    }
    
    static Participant createParticipant(PullRequest pullRequest, GHUser ghUser, String role) throws IOException {
        long userId = ghUser.getId();
        String login = ghUser.getLogin();
        String name = ghUser.getName();
        String location = ghUser.getLocation();
        List<String> followers = ghUser.getFollowers().stream().map(u -> u.getLogin()).collect(Collectors.toList());
        List<String> follows = ghUser.getFollows().stream().map(u -> u.getLogin()).collect(Collectors.toList());
        
        Participant participant = new Participant(pullRequest, userId, login, name, location, role,
                followers, follows);
        pullRequest.addParticipant(participant);
        return participant;
    }
    
    static Participant checkAndCreateReviewer(PullRequest pullRequest, GHUser ghUser, String role) throws IOException {
        Participant participant = existsParticipant(pullRequest, ghUser);
        if (participant == null) {
            participant = createParticipant(pullRequest, ghUser, role);
        }
        return participant;
    }
    
    static Participant existsParticipant(PullRequest pullRequest, GHUser ghUser) {
        return pullRequest.getParticipants().stream()
                          .filter(p -> p.getLogin().equals(ghUser.getLogin()))
                          .findFirst().orElse(null);
    }
    
    static Participant existsParticipant(PullRequest pullRequest, long userId) {
        return pullRequest.getParticipants().stream()
                          .filter(p -> p.getUserId() == userId)
                          .findFirst().orElse(null);
    }
}
