package org.jtool.prmodel.QuickAccessAPI;

import java.util.Set;
import java.util.stream.Collectors;

import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.PRModel;

public class PullRequestFeature {
    
    /**
     * Returns all pull-requests having the repository name in a pull-request model.
     * @param prmodel a pull-request model 
     * @param repositoryName the name of a repository
     * @return the collection of the pull-requests
     */
    public static Set<PullRequest> getPullRequests(PRModel prmodel, String repositoryName) {
        return prmodel.getPullRequests().stream()
                .filter(pr -> pr.getRepositoryName().equals(repositoryName)).collect(Collectors.toSet());
    }
}