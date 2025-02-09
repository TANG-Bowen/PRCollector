/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.prmodel.builder;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.kohsuke.github.GHIssueEvent;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;

import org.jtool.prmodel.IssueEvent;
import org.jtool.prmodel.Label;
import org.jtool.prmodel.PullRequest;

public class LabelBuilder {
    
    private List<Exception> exceptions = new ArrayList<>();
    
    private final PullRequest pullRequest;
    private final GHPullRequest ghPullRequest;
    private final GHRepository repository;
    
    LabelBuilder(PullRequest pullRequest, GHPullRequest ghPullRequest, GHRepository repository) {
        this.pullRequest = pullRequest;
        this.ghPullRequest = ghPullRequest;
        this.repository = repository;
    }
    
    void build(Map<Long, IssueEvent> eventMap) {
        if (eventMap == null) {
            return;
        }
        
        try {
            Set<GHLabel> repoLabels = new HashSet<>(repository.listLabels().toSet());
            
            for (GHIssueEvent ghEvent : ghPullRequest.listEvents().toList()) {
                GHLabel ghLabel = ghEvent.getLabel();
                if (ghLabel == null) {
                    continue;
                }
                
                String name = ghLabel.getName();
                String color = ghLabel.getColor();
                GHLabel repoLabel = getGHLabel(repoLabels, name, color);
                if (repoLabel == null) {
                    continue;
                }
                
                String description = repoLabel.getDescription();
                Label label = new Label(pullRequest, name, color, description);
                long ghId = ghEvent.getId();
                IssueEvent issueEvent = eventMap.get(ghId);
                label.setIssueEvent(issueEvent);
                
                String eventType = ghEvent.getEvent();
                if (eventType.equals("labeled")) {
                    pullRequest.getAddedLabels().add(label);
                } else if (eventType.equals("unlabeled")) {
                    pullRequest.getRemovedLabels().add(label);
                }
            }
            
            for (Label label : pullRequest.getAddedLabels()) {
                if (!isIn(label, pullRequest.getRemovedLabels())) {
                    pullRequest.getFinalLabels().add(label);
                }
            }
        } catch (Exception e) {
            exceptions.add(e);
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
    
    List<Exception> getExceptions() {
        return exceptions;
    }
}
