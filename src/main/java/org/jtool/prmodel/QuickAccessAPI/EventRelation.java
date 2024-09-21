package org.jtool.prmodel.QuickAccessAPI;

import org.jtool.prmodel.IssueEvent;
import org.jtool.prmodel.Participant;
import org.jtool.prmodel.PullRequest;

public class EventRelation {
    
    public static int prior_interaction(PullRequest pullRequest, Participant participant) {
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
