package org.jtool.prmodel;

import java.util.Set;
import java.util.HashSet;

public class PRModel {
    
    private Set<String> repositoryNames = new HashSet<>();
    
    private Set<PullRequest> pullRequests = new HashSet<>();
    private Set<DataLoss> dataLosses = new HashSet<>();
    
    public PRModel() {
    }
    
    public void addPullRequest(PullRequest pullRequest) {
        pullRequests.add(pullRequest);
        repositoryNames.add(pullRequest.getRepositoryName());
    }
    
    public void addAllPullRequests(Set<PullRequest> pullRequests) {
        this.pullRequests.addAll(pullRequests);
        this.pullRequests.forEach(pr -> repositoryNames.add(pr.getRepositoryName()));
    }
    
    public void addDataLoss(DataLoss dataLoss)
    {
    	dataLosses.add(dataLoss);
    	repositoryNames.add(dataLoss.getRepositoryName());
    }
    
    public void addAllDataLosses(Set<DataLoss> dataLosses)
    {
    	this.dataLosses.addAll(dataLosses);
    	this.dataLosses.forEach(dl -> repositoryNames.add(dl.getRepositoryName()));
    }
    
    public Set<String> getRepositoryNames() {
        return repositoryNames;
    }
    
    public Set<PullRequest> getPullRequests() {
        return pullRequests;
    }
    
    public Set<DataLoss> getDataLosses(){
    	return dataLosses;
    }
}
