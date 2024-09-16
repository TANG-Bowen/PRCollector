package org.jtool.prmodel.builder;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHIssueEvent;
import org.kohsuke.github.GHPullRequest;

import org.jtool.prmodel.PRModelDate;
import org.jtool.prmodel.PullRequest;

public class PullRequestBuilder {
    
    private final GHPullRequest ghPullRequest;
    
    PullRequestBuilder(GHPullRequest ghPullRequest) {
        this.ghPullRequest = ghPullRequest;
    }
    
    PullRequest build() throws IOException {
        int id = ghPullRequest.getNumber();
        String title = ghPullRequest.getTitle();
        String repositoryName = ghPullRequest.getRepository().getName();
        String state = ghPullRequest.getState().name();
        
        PRModelDate createDate = new PRModelDate(ghPullRequest.getCreatedAt());
        PRModelDate endDate = new PRModelDate(ghPullRequest.getClosedAt());
        
        String mergeBranch = ghPullRequest.getBase().getRef();
        String headBranch = ghPullRequest.getHead().getRef();
        String pageUrl = ghPullRequest.getHtmlUrl().toString();
        String repositorySrcDLUrl = ghPullRequest.getRepository().getHttpTransportUrl();
        
        Map<String, GHBranch> repoBranches = ghPullRequest.getRepository().getBranches();
        List<String> repositoryBranches = new ArrayList<>(repoBranches.keySet());
        String headRepositorySrcDLUrl;
        List<String> headRepositoryBranches;
        if (ghPullRequest.getHead().getRepository() != null) {
            headRepositorySrcDLUrl = ghPullRequest.getHead().getRepository().getHttpTransportUrl();
            
            Map<String, GHBranch> headRepoBranches = ghPullRequest.getHead().getRepository().getBranches();
            headRepositoryBranches = new ArrayList<>(headRepoBranches.keySet());
        } else {
            headRepositorySrcDLUrl = "";
            headRepositoryBranches = new ArrayList<>();
        }
        
        boolean sourceCodeRetrievable = headRepositoryBranches.contains(headBranch);
        
        boolean isMerged = false;
        boolean isStandardMerged = false;
        if (state.equals("CLOSED")) {
            if (ghPullRequest.isMerged()) {
                isMerged = true;
                isStandardMerged = true;
            } else {
                GHIssueEvent closedEvent = null;
                for (GHIssueEvent event : ghPullRequest.listEvents().toList()) {
                    if (event.getEvent().equals("closed")) {
                        closedEvent = event;
                    }
                }
                if (closedEvent != null) {
                    if (closedEvent.getCommitId() != null) {
                        isMerged = true;
                        isStandardMerged = false;
                    } else {
                        isMerged = false;
                        isStandardMerged = false;
                    }
                }
            }
        }
        
        PullRequest pullRequest = new PullRequest(id, title, repositoryName, state,
                createDate, endDate,
                mergeBranch, headBranch, pageUrl, repositorySrcDLUrl, headRepositorySrcDLUrl,
                isMerged, isStandardMerged, sourceCodeRetrievable, 
                repositoryBranches, headRepositoryBranches);
        return pullRequest;
    }
}
