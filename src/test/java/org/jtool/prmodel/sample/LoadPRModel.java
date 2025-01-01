package org.jtool.prmodel.sample;

import java.util.Set;
import java.util.HashSet;

import org.jtool.prmodel.PRModel;
import org.jtool.prmodel.PRModelLoader;
import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.DeficientPullRequest;

public class LoadPRModel {
    
    private final static String ROOT_SOURCE_PATH = "/Users/tangbowen/PRDataset-te";
    private final static String REPOSITORY_NAME = "spring-boot";
    
    public static void main(String[] args) {
        String dataPath = ROOT_SOURCE_PATH + "/PRCollector/" + REPOSITORY_NAME;
        
        LoadPRModel loader = new LoadPRModel();
        Set<PullRequest> pullRequests = loader.getPRs(dataPath);
        pullRequests.forEach(pr -> pr.print());
    }
    
    public Set<PullRequest> getPRs(String dataPath) {
        PRModelLoader loader = new PRModelLoader(dataPath);
        
        PRModel prmodel = loader.load();
        return prmodel.getPullRequests();
    }
    
    public Set<PullRequest> getUserInformationLossPRs(PRModel prmodel) {
        Set<PullRequest> pullRequests = new HashSet<>();
        for (DeficientPullRequest pr : prmodel.getDeficientPullRequests()) {
            if (!pr.isParticipantRetrievable()) {
                pullRequests.add(pr);
            }
        }
        return pullRequests;
    }
    
    public Set<PullRequest> getCommentLossPRs(PRModel prmodel) {
        Set<PullRequest> pullRequests = new HashSet<>();
        for (DeficientPullRequest pr : prmodel.getDeficientPullRequests()) {
            if (!pr.isIssueCommentRetrievable()) {
                pullRequests.add(pr);
            }
        }
        return pullRequests;
    }
    
    public Set<PullRequest> getReviewCommentLossPRs(PRModel prmodel) {
        Set<PullRequest> pullRequests = new HashSet<>();
        for (DeficientPullRequest pr : prmodel.getDeficientPullRequests()) {
            if (!pr.isReviewCommentRetrievable()) {
                pullRequests.add(pr);
            }
        }
        return pullRequests;
    }
    
    public Set<PullRequest> getEventLossPRs(PRModel prmodel) {
        Set<PullRequest> pullRequests = new HashSet<>();
        for (DeficientPullRequest pr : prmodel.getDeficientPullRequests()) {
            if (!pr.isEventRetrievable()) {
                pullRequests.add(pr);
            }
        }
        return pullRequests;
    }
    
    public Set<PullRequest> getReviewEventLossPRs(PRModel prmodel) {
        Set<PullRequest> pullRequests = new HashSet<>();
        for (DeficientPullRequest pr : prmodel.getDeficientPullRequests()) {
            if (!pr.isReviewEventRetrievable()) {
                pullRequests.add(pr);
            }
        }
        return pullRequests;
    }
    
    public Set<PullRequest> getCommitLossPRs(PRModel prmodel) {
        Set<PullRequest> pullRequests = new HashSet<>();
        for (DeficientPullRequest pr : prmodel.getDeficientPullRequests()) {
            if (!pr.isCommitRetrievable()) {
                pullRequests.add(pr);
            }
        }
        return pullRequests;
    }
    
    public Set<PullRequest> getSourceCodeLossPRs(PRModel prmodel) {
        Set<PullRequest> pullRequests = new HashSet<>();
        for (PullRequest pr : prmodel.getPullRequests()) {
            if (!pr.isSourceCodeRetrievable()) {
                pullRequests.add(pr);
            }
        }
        return pullRequests;
    }
    
    public Set<PullRequest> getNonCategorizedLossPRs(PRModel prmodel) {
        Set<PullRequest> pullRequests = new HashSet<>();
        for (DeficientPullRequest pr : prmodel.getDeficientPullRequests()) {
            if (pr.nonCategorized()) {
                pullRequests.add(pr);
            }
        }
        return pullRequests;
    }
}
