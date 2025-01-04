package org.jtool.prmodel.QuickAccessAPI;

import java.util.List;
import java.util.stream.Collectors;

import org.jtool.prmodel.IssueEvent;
import org.jtool.prmodel.Participant;
import org.jtool.prmodel.PullRequest;

public class ParticipantRelevance {
    
	/**
     * Returns number of participants in a pull request.
     * @param pullRequest a pull-request
     * @param participant the Participant who as a actor
     * @return number of Participants
     */
    public static int numParticipants(PullRequest pullRequest) {
        return pullRequest.getParticipants().size();
    }
    
    /**
     * Returns number of state represent follow relationship between two participants in a pull request.
     * @param participant1 the Participant
     * @param participant2 the Participant 
     * @return number of state: 2: participant1 and participant2 follows to each other; 1: participant2 is participant1's follower; -1: participant1 is participant2's follower
     */
    private static int followRelation(Participant participant1, Participant participant2) {
        boolean isFollower1 = false;
        boolean isFollower2 = false;
        for (String follower : participant1.getFollowers()) {
            if (participant2.getLogin().equals(follower)) {
                isFollower1 = true;
                break;
            }
        }
        
        for (String follower : participant2.getFollowers()) {
            if (participant1.getLogin().equals(follower)) {
                isFollower2 = true;
                break;
            }
        }
        
        if (isFollower1 && isFollower2) {
            return 2;
        } else if (isFollower1 && !isFollower2) {
            return -1;
        } else if (!isFollower1 && isFollower2) {
            return 1;
        } else {
            return 0;
        }
    }
    
    /**
     * Returns if author is a follower to the reviewer who closed a pull request.
     * @param pullRequest a pull-request
     * @return true if author is the reviewer's follower
     */
    public static boolean socialDistanceExists(PullRequest pullRequest) {
        Participant submitter = null;
        Participant closeMember = null;
        for (Participant pa : pullRequest.getParticipants()) {
            if (pa.getRole().equals("Author")) {
                submitter = pa;
                break;
            }
        }
        
        for (IssueEvent event : pullRequest.getConversation().getIssueEvents()) {
            if (event.getBody().equals("closed")) {
                closeMember = event.getParticipant();
                break;
            }
        }
        
        if (submitter != null && closeMember != null) {
            return followRelation(submitter, closeMember) == 1 || followRelation(submitter, closeMember) == 2;
        }
        return false;
    }
    
    /**
     * Returns author of a pull request.
     * @param pullRequest a pull-request
     * @return a Participant
     */
    public static Participant getAuthor(PullRequest pullRequest) {
        return pullRequest.getParticipants().stream()
                .filter(pa -> pa.getRole().equals("Author")).findFirst().orElse(null);
    }
    
    /**
     * Returns reviewers of a pull request.
     * @param pullRequest a pull-request
     * @return a List of Participants
     */
    public static List<Participant> getReviewers(PullRequest pullRequest) {
        return pullRequest.getParticipants().stream()
                .filter(pa -> pa.getRole().equals("Reviewer")).collect(Collectors.toList());
    }
}
