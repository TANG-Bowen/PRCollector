package org.jtool.prmodel.builder;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommitStatus;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestCommitDetail;

import org.jtool.prmodel.CIStatus;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.PRModelDate;
import org.jtool.prmodel.Participant;
import org.jtool.prmodel.PullRequest;

public class CommitBuilder {
    
    private final PullRequest pullRequest;
    private final GHPullRequest ghPullRequest;
    
    CommitBuilder(PullRequest pullRequest, GHPullRequest ghPullRequest) {
        this.pullRequest = pullRequest;
        this.ghPullRequest = ghPullRequest;
    }
    
    void build() throws IOException {
        List<Commit> commits = new ArrayList<>();
        
        for (GHPullRequestCommitDetail ghCommitDetail : ghPullRequest.listCommits()) {
            GHCommit ghCommit = ghPullRequest.getRepository().getCommit(ghCommitDetail.getSha());
            
            String sha = ghCommitDetail.getSha();
            String shortSha = ghCommitDetail.getSha().substring(0, 6);
            PRModelDate date = new PRModelDate(ghCommit.getCommitDate());
            String message = ghCommit.getCommitShortInfo().getMessage();
            String type = getCommitType(ghCommit, message);
            
            Commit commit = new Commit(pullRequest, sha, shortSha, date, message, type);
            commits.add(commit);
            
            Participant commiter = ParticipantBuilder.existsParticipant(pullRequest, ghCommit.getAuthor().getId());
            if (commiter == null) {
                commiter = ParticipantBuilder.createParticipant(pullRequest, ghCommit.getAuthor(), "Commiter");
            }
            commit.setCommiter(commiter);
            
            List<CIStatus> statusList = new ArrayList<>();
            for (GHCommitStatus ghStatus : ghCommit.listStatuses().toList()) {
                String context = ghStatus.getContext();
                String state = ghStatus.getState().toString();
                String description = ghStatus.getDescription();
                String targetUrl = ghStatus.getTargetUrl();
                PRModelDate createDate = new PRModelDate(ghStatus.getCreatedAt());
                PRModelDate updateDate = new PRModelDate(ghStatus.getUpdatedAt());
                
                CIStatus ciStatus = new CIStatus(pullRequest, context, state, description, targetUrl,
                        createDate, updateDate);
                ciStatus.setCommit(commit);
                
                statusList.add(ciStatus);
            }
            
            List<CIStatus> sortedStatusList = statusList.stream()
                    .sorted((s1, s2) -> s1.getUpdateDate().compareTo(s2.getUpdateDate())).collect(Collectors.toList());
            commit.getCIStatus().addAll(sortedStatusList);
        }
        List<Commit> sortedCimmits = commits.stream()
                .sorted((c1, c2) -> c1.getDate().compareTo(c2.getDate())).collect(Collectors.toList());
        pullRequest.getCommits().addAll(sortedCimmits);
    }
    
    private String getCommitType(GHCommit ghCommit, String message) throws IOException {
        List<GHCommit> parents = ghCommit.getParents();
        int parentCount = parents.size();
        if (parentCount == 0) {
            return "initial";
        } else if (parentCount > 1 || message.contains("Merge") || message.contains("merge")) { 
            return "merge";
        } else if (message.startsWith("Revert")) {
            return "revert";
        } else if (message.startsWith("fixup!")) {
            return "fixup";
        } else if (message.startsWith("squash!")) {
            return "squash";
        } else if (message.trim().isEmpty()) {
            return "empty";
        } else {
            return "regular";
        }
    }
}
