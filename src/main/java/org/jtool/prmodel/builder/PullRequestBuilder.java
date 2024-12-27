package org.jtool.prmodel.builder;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHIssueEvent;
import org.kohsuke.github.GHPullRequest;

import org.jtool.prmodel.PRModelDate;
import org.jtool.prmodel.PullRequest;

public class PullRequestBuilder {
    
    private List<Exception> exceptions = new ArrayList<>();
    
    private final GHPullRequest ghPullRequest;
    
    PullRequestBuilder(GHPullRequest ghPullRequest) {
        this.ghPullRequest = ghPullRequest;
    }
    
    PullRequest build() {
        int number = ghPullRequest.getNumber();
        String title = ghPullRequest.getTitle();
        String repositoryName = ghPullRequest.getRepository().getName();
        String state = ghPullRequest.getState().name();
        String id = repositoryName + "#" + number;
        
        PRModelDate createDate;
        try {
            createDate = new PRModelDate(ghPullRequest.getCreatedAt());
        } catch (IOException e) {
            createDate = new PRModelDate(ghPullRequest.getClosedAt());
            exceptions.add(e);
        }
        
        PRModelDate endDate = new PRModelDate(ghPullRequest.getClosedAt());
        
        String mergeBranch = ghPullRequest.getBase().getRef();
        String headBranch = ghPullRequest.getHead().getRef();
        String pageUrl = ghPullRequest.getHtmlUrl().toString();
        String repositorySrcDLUrl = ghPullRequest.getRepository().getHttpTransportUrl();
        
        List<String> repositoryBranches;
        String headRepositorySrcDLUrl;
        List<String> headRepositoryBranches;
        boolean sourceCodeRetrievable;
        try {
            Map<String, GHBranch> repoBranches = ghPullRequest.getRepository().getBranches();
            repositoryBranches = new ArrayList<>(repoBranches.keySet());
            
            if (ghPullRequest.getHead().getRepository() != null) {
                headRepositorySrcDLUrl = ghPullRequest.getHead().getRepository().getHttpTransportUrl();
                
                Map<String, GHBranch> headRepoBranches = ghPullRequest.getHead().getRepository().getBranches();
                headRepositoryBranches = new ArrayList<>(headRepoBranches.keySet());
                
                sourceCodeRetrievable = headRepositoryBranches.contains(headBranch);
            } else {
                headRepositorySrcDLUrl = PRModelBuilder.UNKNOWN_SYMBOL;
                headRepositoryBranches = new ArrayList<>(Arrays.asList(PRModelBuilder.UNKNOWN_SYMBOL));
                
                sourceCodeRetrievable = false;
                exceptions.add(new Exception("Not found repository"));
            }
        } catch (IOException e) {
            repositoryBranches = new ArrayList<>();
            headRepositorySrcDLUrl = PRModelBuilder.UNKNOWN_SYMBOL;
            headRepositoryBranches = new ArrayList<>(Arrays.asList(PRModelBuilder.UNKNOWN_SYMBOL));
            
            sourceCodeRetrievable = false;
            exceptions.add(e);
        }
        
        boolean isMerged = false;
        boolean isStandardMerged = false;
        try {
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
        } catch(IOException e) {
            isMerged = false;
            isStandardMerged = false;
        }
        
        PullRequest pullRequest = new PullRequest(id, title, repositoryName, state,
                createDate, endDate,
                mergeBranch, headBranch, pageUrl,
                repositorySrcDLUrl, headRepositorySrcDLUrl,
                isMerged, isStandardMerged, 
                repositoryBranches, headRepositoryBranches);
        pullRequest.setSourceCodeRetrievable(sourceCodeRetrievable);
        return pullRequest;
    }
    
    List<Exception> getExceptions() {
        return exceptions;
    }
}
