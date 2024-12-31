package org.jtool.prmodel;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.List;

public class PRModel {
    
    private Set<String> repositoryNames = new HashSet<>();
    
    private Set<PullRequest> pullRequests = new HashSet<>();
    private Set<DeficientPullRequest> deficientPullRequests = new HashSet<>();
    
    public PRModel() {
    }
    
    public void addPullRequest(PullRequest pullRequests) {
        this.pullRequests.add(pullRequests);
        this.repositoryNames.add(pullRequests.getRepositoryName());
    }
    
    public void addAllPullRequests(Set<PullRequest> pullRequests) {
        this.pullRequests.addAll(pullRequests);
        pullRequests.forEach(pr -> this.repositoryNames.add(pr.getRepositoryName()));
    }
    
    public void addDeficientPullRequest(DeficientPullRequest pullRequests) {
        this.deficientPullRequests.add(pullRequests);
        this.repositoryNames.add(pullRequests.getRepositoryName());
    }
    
    public void addAllDeficientPullRequests(Set<DeficientPullRequest> pullRequests) {
        this.deficientPullRequests.addAll(pullRequests);
        pullRequests.forEach(dl -> this.repositoryNames.add(dl.getRepositoryName()));
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    /**
     * Return the names of all repositories.
     * @return the collection of the repository names
     */
    public Set<String> getRepositoryNames() {
        return repositoryNames;
    }
    
    /**
     * Returns all pull-requests in a pull-request model.
     * @return the collection of the pull-requests
     */
    public Set<PullRequest> getPullRequests() {
        return pullRequests;
    }
    
    /**
     * Returns all pull-requests in a pull-request model.
     * @return the collection of the pull-requests in the pull-request number's order
     */
    public List<PullRequest> getPullRequestList() {
        return pullRequests.stream()
                           .sorted((pr1, pr2) -> pr1.getId().compareTo(pr2.getId()))
                           .collect(Collectors.toList());
    }
    
    /**
     * Returns all deficient pull-requests in a pull-request model.
     * @return the collection of the pull-requests in the pull-request number's order
     */
    public Set<DeficientPullRequest> getDeficientPullRequests(){
        return deficientPullRequests;
    }
    
    /**
     * Returns all deficient pull-requests in a pull-request model.
     * @return the collection of the pull-requests in the pull-request number's order
     */
    public List<DeficientPullRequest> getDeficientPullRequestList(){
        return deficientPullRequests.stream()
                                    .sorted((pr1, pr2) -> pr1.getId().compareTo(pr2.getId()))
                                    .collect(Collectors.toList());
    }
    
    /**
     * Returns all pull-requests having the repository name in a pull-request model.
     * @param prmodel a pull-request model 
     * @param repositoryName the name of a repository
     * @return the collection of the pull-requests
     */
    public Set<PullRequest> getPullRequests(PRModel prmodel, String repositoryName) {
        return prmodel.getPullRequests().stream()
                .filter(pr -> pr.getRepositoryName().equals(repositoryName)).collect(Collectors.toSet());
    }
    
    /**
     * Returns all pull-requests having the repository name in a pull-request model.
     * @param prmodel a pull-request model 
     * @param repositoryName the name of a repository
     * @return the collection of the pull-requestsitoryName
     * @return the collection of the pull-requests in the pull-request number's order
     */
    public List<PullRequest> getPullRequestList(PRModel prmodel, String repositoryName) {
        return getPullRequestList(prmodel, repositoryName)
                .stream()
                .sorted((pr1, pr2) -> pr1.getId().compareTo(pr2.getId()))
                .collect(Collectors.toList());
    }
}
