package org.jtool.prmodel;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommitStatus;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHIssueEvent;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestCommitDetail;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHPullRequestReviewComment;
import org.kohsuke.github.GHPullRequestReview;
import org.kohsuke.github.GHPullRequestSearchBuilder;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.GHUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class UsingGitHubAPI {
    
    private final static String GITHUB_TOKEN = "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";
    private final static String REPOSITORY_NAME = "spring-projects/spring-boot";
    
    private GitHub github;
    private GHRepository repository;
    
    private List<GHPullRequest> pullRequests = null;
    
    public UsingGitHubAPI() {
    }
    
    public static void main(String[] args) {
        UsingGitHubAPI usage = new UsingGitHubAPI();
        usage.buildPRModel(GITHUB_TOKEN, REPOSITORY_NAME);
        
        usage.execute();
    }
    
    public void buildPRModel(String ghToken, String repositoryName) {
        try {
            github = new GitHubBuilder().withOAuthToken(GITHUB_TOKEN).build();
            repository = github.getRepository(REPOSITORY_NAME);
            GHPullRequestSearchBuilder prSearch = repository.searchPullRequests();
            
            prSearch.isClosed();
            LocalDate from = LocalDate.parse("2024-04-15");
            LocalDate to = LocalDate.parse("2024-04-18");
            prSearch.created(from, to);
//            GHUser user = this.github.getUser("snicoll");
//            prSearch.author(user);
            
            List<GHPullRequest> prs = new ArrayList<>();
            for (GHPullRequest ghpr : prSearch.list()) {
                GHPullRequest pr = repository.getPullRequest(ghpr.getNumber());
                prs.add(pr);
            }
            pullRequests = getPullRequestList(prs);
        } catch (IOException e) {
            pullRequests = new ArrayList<>();
        }
    }
    
    private List<GHPullRequest> getPullRequestList(List<GHPullRequest> prs) {
        List<GHPullRequest> sortedList = prs.stream()
            .sorted((e1, e2) -> e2.getNumber() - e1.getNumber())
            .collect(Collectors.toList());
            return sortedList;
    }
    
    private void execute() {
        for (GHPullRequest pullRequest : pullRequests) {
            System.out.println("PR " + pullRequest.getNumber() + "   " + pullRequest.getTitle());
            
            try {
                System.out.println("numCommitComments " + numCommitComments(pullRequest));
                System.out.println("numIssueComments " + numIssueComments(pullRequest));
                System.out.println("numComments " + numComments(pullRequest));
                System.out.println("numPriorInteractions " + numPriorInteractions(pullRequest));
                System.out.println("mentionExists " + mentionExists(pullRequest));
                System.out.println("numComplexDescription " + numComplexDescription(pullRequest));
                
            System.out.println("numCommits " + numCommits(pullRequest));
            System.out.println("numSrcChurns " + numSrcChurns(pullRequest));
            System.out.println("numTestChurns " + numTestChurns(pullRequest));
            System.out.println("numSrcFiles " + numSrcFiles(pullRequest));
            System.out.println("numFilesAdded " + numFilesAdded(pullRequest));
            System.out.println("numFilesDeleted " + numFilesDeleted(pullRequest));
            System.out.println("numFilesRevised " + numFilesRevised(pullRequest));
            System.out.println("numFilesModified " + numFilesModified(pullRequest));
            System.out.println("numDocFiles " + numDocFiles(pullRequest));
            System.out.println("numOtherFiles " + numOtherFiles(pullRequest));
            System.out.println("testIncluded " + testIncluded(pullRequest));
            System.out.println("numCIFailures " + numCIFailures(pullRequest));
            
            System.out.println("numParticipants: " + numParticipants(pullRequest));
            System.out.println("socialDistanceExists: " + socialDistanceExists(pullRequest));
            
            System.out.println("lifetimeMinutes " + lifetimeMinutes(pullRequest));
            System.out.println("mergetimeMinutes " + mergetimeMinutes(pullRequest));
            System.out.println("totalCILatencyMinutes " + totalCILatencyMinutes(pullRequest));
            
            } catch (IOException e) { /* empty */ }
        }
    }
    
    /*********************************************************
     * Conversation Relevant
     *********************************************************/
    
    public int numCommitComments(GHPullRequest pullRequest) throws IOException {
        Set<GHPullRequestReviewComment> comments = pullRequest.listReviewComments().toSet();
        return comments.size();
    }
    
    public int numIssueComments(GHPullRequest pullRequest) throws IOException {
        Set<GHIssueComment> comments = pullRequest.listComments().toSet();
        return comments.size();
    }
    
    public int numComments(GHPullRequest pullRequest) throws IOException {
        return numCommitComments(pullRequest) + numIssueComments(pullRequest);
    }
    
    public int numPriorInteractions(GHPullRequest pullRequest) throws IOException {
        int num = 0;
        for (GHIssueEvent evi : pullRequest.listEvents()) {
            if (evi.getActor().getId() == pullRequest.getUser().getId()) {
                num++;
            }
        }
        return num;
    }
    
    public boolean mentionExists(GHPullRequest pullRequest) throws IOException {
        boolean hasMention = false;
        for (GHIssueComment comment : pullRequest.listComments().toList()) {
            if (comment.getBody().contains("@")) {
                hasMention = true;
                break;
            }
        }
        for (GHPullRequestReviewComment comment : pullRequest.listReviewComments()) {
            if (comment.getBody().contains("@")) {
                hasMention = true;
                break;
            }
        }
        for (GHPullRequestReview review  : pullRequest.listReviews()) {
            if (review.getBody().contains("@")) {
                hasMention = true;
                break;
            }
        }
        return hasMention;
    }
    
    public int numComplexDescription(GHPullRequest pullRequest) {
        String title = pullRequest.getTitle();
        if(pullRequest.getBody()!=null)
        {
        String body = pullRequest.getBody();
        String[] titleWords = title.split("\\s+");
        String[] bodyWords = body.split("\\s+");
        return titleWords.length + bodyWords.length;
        }else {
        	return -1;
        }
    }
    
    /*********************************************************
     * Commit Relevant
     * @throws IOException 
     *********************************************************/
    
    public int numCommits(GHPullRequest pullRequest) throws IOException {
        List<GHPullRequestCommitDetail> commits = pullRequest.listCommits().toList();
        return commits.size();
    }
    
    public int numSrcChurns(GHPullRequest pullRequest) throws IOException {
        int churns = 0;
        for (GHPullRequestFileDetail fileDetail : pullRequest.listFiles().toList()) {
            churns = churns + fileDetail.getChanges();
        }
        return churns;
    }
    
    public int numTestChurns(GHPullRequest pullRequest) throws IOException {
        int churns = 0;
        for (GHPullRequestFileDetail fileDetail : pullRequest.listFiles().toList()) {
            if (isTest(pullRequest, fileDetail)) {
                churns = churns + fileDetail.getChanges();
            }
        }
        return churns;
    }
    
    private boolean isTest(GHPullRequest pullRequest, GHPullRequestFileDetail fileDetail) throws IOException {
        if (!fileDetail.getFilename().endsWith(".java")) {
            return false;
        }
        GHContent content = repository.getFileContent(fileDetail.getFilename(), pullRequest.getHead().getSha());
        BufferedReader reader = new BufferedReader(new InputStreamReader(content.read()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("@Test")) {
                return true;
            }
        }
        return false;
    }
    
    public int numSrcFiles(GHPullRequest pullRequest) throws IOException {
        int files = 0;
        for (GHPullRequestFileDetail fileDetail : pullRequest.listFiles().toList()) {
            if (fileDetail.getFilename().endsWith(".java")) {
                files++;
            }
        }
        return files;
    }
    
    public int numFilesAdded(GHPullRequest pullRequest) throws IOException {
        int files = 0;
        for (GHPullRequestFileDetail fileDetail : pullRequest.listFiles().toList()) {
            if (pullRequest.getAdditions() == getLines(pullRequest, fileDetail)) {
                files++;
            }
        }
        return files;
    }
    
    public int numFilesDeleted(GHPullRequest pullRequest) throws IOException {
        int files = 0;
        for (GHPullRequestFileDetail fileDetail : pullRequest.listFiles().toList()) {
            if (pullRequest.getDeletions() == getLines(pullRequest, fileDetail)) {
                files++;
            }
        }
        return files;
    }
    
    public int numFilesRevised(GHPullRequest pullRequest) throws IOException {
        int files = 0;
        for (GHPullRequestFileDetail fileDetail : pullRequest.listFiles().toList()) {
            int lines = getLines(pullRequest, fileDetail);
            if (fileDetail.getAdditions() < lines && fileDetail.getDeletions() < lines) {
                files++;
            }
        }
        return files;
    }
    
    public int numFilesModified(GHPullRequest pullRequest) throws IOException {
        int files = 0;
        for (GHPullRequestFileDetail fileDetail : pullRequest.listFiles().toList()) {
            int lines = getLines(pullRequest, fileDetail);
            if (fileDetail.getAdditions() == lines) {
                files++;
            }
            if (fileDetail.getDeletions() == lines) {
                files++;
            }
            if (fileDetail.getAdditions() < lines && fileDetail.getDeletions() < lines) {
                files++;
            }
        }
        return files;
    }
    
    private int getLines(GHPullRequest pullRequest, GHPullRequestFileDetail fileDetail) throws IOException {
        int lines = 0;
        GHContent content = repository.getFileContent(fileDetail.getFilename(), pullRequest.getHead().getSha());
        BufferedReader reader = new BufferedReader(new InputStreamReader(content.read()));
        while (reader.readLine() != null) {
            lines++;
        }
        return lines;
    }
    
    public int numDocFiles(GHPullRequest pullRequest) throws IOException {
        int files = 0;
        for (GHPullRequestFileDetail file : pullRequest.listFiles().toList()) {
            if (isDocExtention(file)) {
                files++;
            }
        }
        return files;
    }
    
    public int numOtherFiles(GHPullRequest pullRequest) throws IOException {
        int files = 0;
        for (GHPullRequestFileDetail file : pullRequest.listFiles().toList()) {
            if (!file.getFilename().endsWith(".java") && !isDocExtention(file)) {
                files++;
            }
        }
        return files;
    }
    
    private boolean isDocExtention(GHPullRequestFileDetail file) {
        String path = file.getFilename();
        return path.endsWith(".md") || path.endsWith(".html") || path.contains(".adoc");
    }
    
    public boolean testIncluded(GHPullRequest pullRequest) throws IOException {
        for (GHPullRequestFileDetail file : pullRequest.listFiles().toList()) {
            if (isTest(pullRequest, file)) {
                return true;
            }
        }
        return false;
    }
    
    public int numCIFailures(GHPullRequest pullRequest) throws IOException {
        int failures = 0;
        for (GHPullRequestCommitDetail commitDetail : pullRequest.listCommits()) {
            GHCommit commit = repository.getCommit(commitDetail.getSha());
            for (GHCommitStatus status : commit.listStatuses()) {
                if (!status.getState().toString().equals("SUCCESS")) {
                    failures++;
                }
            }
        }
        return failures;
    }
    
    /*********************************************************
     * Participant Relevant
     *********************************************************/
    
    public int numParticipants(GHPullRequest pullRequest) throws IOException {
        Set<GHUser> participants = new HashSet<>();
        participants.add(pullRequest.getUser());
        for (GHIssueComment isci : pullRequest.listComments()) {
            participants.add(isci.getUser());
        }
        for (GHPullRequestReviewComment rci : pullRequest.listReviewComments()) {
            participants.add(rci.getUser());
        }
        for (GHIssueEvent evi : pullRequest.listEvents()) {
            participants.add(evi.getActor());
        }
        for (GHPullRequestReview rvi : pullRequest.listReviews()) {
            participants.add(rvi.getUser());
        }
        return participants.size();
    }
    
    public boolean socialDistanceExists(GHPullRequest pullRequest) throws IOException {
        GHUser submitter = pullRequest.getUser();
        GHUser closeMember = null;
        for (GHIssueEvent event : pullRequest.listEvents()) {
            if (event.getEvent().equals("closed")) {
                closeMember = event.getActor();
            }
        }
        for (GHUser follower : submitter.getFollows()) {
            if (follower.getId() == closeMember.getId()) {
                return true;
            }
        }
        return false;
    }
    
    /*********************************************************
     * Time Relevant
     *********************************************************/
    
    public long lifetimeMinutes(GHPullRequest pullRequest) throws IOException {
        long timeDiff = pullRequest.getClosedAt().getTime() - pullRequest.getCreatedAt().getTime();
        return TimeUnit.MILLISECONDS.toMinutes(timeDiff);
    }
    
    public long mergetimeMinutes(GHPullRequest pullRequest) throws IOException {
        if (pullRequest.isMerged()) {
            long timeDiff = pullRequest.getMergedAt().getTime() - pullRequest.getCreatedAt().getTime();
            return TimeUnit.MILLISECONDS.toMinutes(timeDiff);
        }
        return -1;
    }
    
    public long totalCILatencyMinutes(GHPullRequest pullRequest) throws IOException {
        List<GHPullRequestCommitDetail> commits = pullRequest.listCommits().toList();
        int commitSize = commits.size();
        if (commitSize > 0) {
            GHPullRequestCommitDetail lastCommitDetail = commits.get(commitSize - 1);
            GHCommit lastCommit = repository.getCommit(lastCommitDetail.getSha());
            
            Date latest = null;
            for (GHCommitStatus status : lastCommit.listStatuses()) {
                if (latest == null) {
                    latest = status.getCreatedAt();
                } else if (status.getUpdatedAt().compareTo(latest) > 0) {
                    latest = status.getCreatedAt();
                }
            }
            long timeDiff = latest.getTime() - pullRequest.getCreatedAt().getTime();
            return TimeUnit.MILLISECONDS.toMinutes(timeDiff);
        }
        return -1;
    }
}
