package org.jtool.prmodel.builder;

import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

import org.kohsuke.github.GHIssueEvent;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;

import org.jtool.prmodel.IssueEvent;
import org.jtool.prmodel.Label;
import org.jtool.prmodel.PullRequest;

public class LabelBuilder {
    
    private final PullRequest pullRequest;
    private final GHPullRequest ghPullRequest;
    private final GHRepository repository;
    
    LabelBuilder(PullRequest pullRequest, GHPullRequest ghPullRequest, GHRepository repository) {
        this.pullRequest = pullRequest;
        this.ghPullRequest = ghPullRequest;
        this.repository = repository;
    }
    
    void build(Map<Long, IssueEvent> eventMap) throws IOException {
        if (eventMap == null) {
            return;
        }
        
        Set<GHLabel> repoLabels = new HashSet<>(repository.listLabels().toSet());
        
        for (GHIssueEvent ghEvent : ghPullRequest.listEvents().toList()) {
            String eventType = ghEvent.getEvent();
            
            if (eventType.equals("labeled")) {
                GHLabel ghLabel = ghEvent.getLabel();
                String name = ghLabel.getName();
                String color = ghLabel.getColor();
                GHLabel repoLabel = getGHLabel(repoLabels, name, color);
                String description = repoLabel.getDescription();
                
                Label label = new Label(pullRequest, name, color, description);
<<<<<<< HEAD
                long ghId = ghEvent.getId();
                IssueEvent issueEvent = eventMap.get(ghId);
                label.setIssueEvent(issueEvent);
                pullRequest.getAddedLabels().add(label);
     
=======
                pullRequest.getAddedLabels().add(label);
                
                long ghId = repoLabel.getId();
                IssueEvent issueEvent = eventMap.get(ghId);
                label.setIssueEvent(issueEvent);
                
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
            } else if (eventType.equals("unlabeled")) {
                GHLabel ghLabel = ghEvent.getLabel();
                String name = ghLabel.getName();
                String color = ghLabel.getColor();
                GHLabel repoLabel = getGHLabel(repoLabels, name, color);
                String description = repoLabel.getDescription();
                
                Label label = new Label(pullRequest, name, color, description);
<<<<<<< HEAD
                long ghId = ghEvent.getId();
                IssueEvent issueEvent = eventMap.get(ghId);
                label.setIssueEvent(issueEvent);
                pullRequest.getRemovedLabels().add(label);
                
                
=======
                pullRequest.getRemovedLabels().add(label);
                
                long ghId = repoLabel.getId();
                IssueEvent issueEvent = eventMap.get(ghId);
                label.setIssueEvent(issueEvent);
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
            }
        }
        
        for (Label label : pullRequest.getAddedLabels()) {
            if (!isIn(label, pullRequest.getRemovedLabels())) {
                pullRequest.getFinalLabels().add(label);
            }
        }
    }
    
    private GHLabel getGHLabel(Set<GHLabel> repoLabels, String name, String color) {
        for (GHLabel ghLabel : repoLabels) {
            if (ghLabel.getName().equals(name) && ghLabel.getColor().equals(color)) {
                return ghLabel;
            }
        }
        return null;
    }
    
    private boolean isIn(Label label, Set<Label> labels) {
        for (Label la : labels) {
            if (la.getName().equals(label.getName()) && la.getColor().equals(label.getColor())) {
                return true;
            }
        }
        return false;
    }
}
