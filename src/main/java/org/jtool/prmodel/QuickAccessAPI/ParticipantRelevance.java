package org.jtool.prmodel.QuickAccessAPI;

import java.util.List;
import java.util.stream.Collectors;

import org.jtool.prmodel.IssueEvent;
import org.jtool.prmodel.Participant;
import org.jtool.prmodel.PullRequest;

public class ParticipantRelevance {
    
    public static int numParticipants(PullRequest pullRequest) {
        return pullRequest.getParticipants().size();
    }
    
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
    
    public static Participant getAuthor(PullRequest pullRequest) {
        return pullRequest.getParticipants().stream()
                .filter(pa -> pa.getRole().equals("Author")).findFirst().orElse(null);
    }
    
    public static List<Participant> getReviewers(PullRequest pullRequest) {
        return pullRequest.getParticipants().stream()
                .filter(pa -> pa.getRole().equals("Reviewer")).collect(Collectors.toList());
    }
}
