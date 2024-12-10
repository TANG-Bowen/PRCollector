package org.jtool.prmodel;

import java.util.Set;
import java.util.HashSet;

public class PRModel {
    
    private Set<String> repositoryNames = new HashSet<>();
    
    private Set<PullRequest> pullRequests = new HashSet<>();
    
    public PRModel() {
    }
    
    public void addPullRequest(PullRequest pullRequest) {
        pullRequests.add(pullRequest);
        repositoryNames.add(pullRequest.getRepositoryName());
    }
    
    public void addAllPullRequests(Set<PullRequest> pullRequests) {
<<<<<<< HEAD
        this.pullRequests.addAll(pullRequests);
        this.pullRequests.forEach(pr -> repositoryNames.add(pr.getRepositoryName()));
=======
        pullRequests.addAll(pullRequests);
        pullRequests.forEach(pr -> repositoryNames.add(pr.getRepositoryName()));
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
    }
    
    public Set<String> getRepositoryNames() {
        return repositoryNames;
    }
    
    public Set<PullRequest> getPullRequests() {
        return pullRequests;
    }
}
