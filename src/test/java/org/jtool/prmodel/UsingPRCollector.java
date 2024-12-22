package org.jtool.prmodel;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class UsingPRCollector {
    
    private final static String GITHUB_TOKEN = "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";
    private final static String ROOT_SOURCE_PATH = "/Users/tangbowen/PRDataset-sceu";
    private final static String REPOSITORY_NAME = "spring-projects/spring-boot";
    
    private final static String MODEL_STORAGE = ROOT_SOURCE_PATH + '/' + "PRCollector";
    
    private Set<PullRequest> pullRequests = null;
    
    public UsingPRCollector() {
    }
    
    public static void main(String[] args) {
        UsingPRCollector usage = new UsingPRCollector();
        usage.buildPRModel(GITHUB_TOKEN, REPOSITORY_NAME, ROOT_SOURCE_PATH);
        usage.loadPRModel(MODEL_STORAGE);
        
        usage.execute();
    }
    
    private void buildPRModel(String ghToken, String repositoryName, String rootSrcPath) {
        PRModelBundle bundle = new PRModelBundle(ghToken, repositoryName, rootSrcPath);
        bundle.writeFile(true);
        bundle.deleteSourceFile(true);
        
        bundle.searchByIsClosed();
        bundle.searchByCreated("2024-04-15", "2024-04-18");
        //bundle.searchByAuthor("snicoll");
        
        bundle.build();
    }
    
    private void loadPRModel(String filepath) {
        if (pullRequests != null) {
            return;
        }
        
        PRModelLoader loader = new PRModelLoader(filepath);
        PRModel prmodel = loader.load();
        
        pullRequests = prmodel.getPullRequests();
    }
    
    private void execute() {
        for (PullRequest pullRequest : pullRequests) {
            System.out.println("PR " + pullRequest.getId() + "   " + pullRequest.getTitle());
            
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
        }
    }
    
    /*********************************************************
     * Conversation Relevant
     *********************************************************/
    
    public int numCommitComments(PullRequest pullRequest) {
        Set<ReviewComment> comments = pullRequest.getConversation().getReviewComments();
        return comments.size();
    }
    
    public int numIssueComments(PullRequest pullRequest) {
        Set<IssueComment> comments = pullRequest.getConversation().getIssueComments();
        return comments.size();
    }
    
    public int numComments(PullRequest pullRequest) {
        return numCommitComments(pullRequest) + numIssueComments(pullRequest);
    }
    
    public int numPriorInteractions(PullRequest pullRequest) {
        Participant author = null;
        for (Participant participant : pullRequest.getParticipants()) {
            if (participant.getRole().equals("Author")) {
                author = participant;
                break;
            }
        }
        if (author == null) {
            return 0;
        }
        
        int num = 0;
        for (Participant pa : pullRequest.getParticipants()) {
            if (pa.getLogin().equals(author.getLogin())) {
                for (IssueEvent event : pullRequest.getConversation().getIssueEvents()) {
                    if (event.getParticipant().getLogin().equals(author.getLogin())) {
                        num++;
                    }
                }
            }
        }
        return num;
    }
    
    public boolean mentionExists(PullRequest pullRequest) {
        boolean hasMention = false;
        for (IssueComment comment : pullRequest.getConversation().getIssueComments()) {
            if (!comment.getMarkdownDoc().getMentionStrings().isEmpty()) {
                hasMention = true;
                break;
            }
        }
        
        for (ReviewComment comment : pullRequest.getConversation().getReviewComments()) {
            if (!comment.getMarkdownDoc().getMentionStrings().isEmpty()) {
                hasMention = true;
                break;
            }
        }
        
        for (ReviewEvent review : pullRequest.getConversation().getReviewEvents()) {
            if (!review.getMarkdownDoc().getMentionStrings().isEmpty()) {
                hasMention = true;
                break;
            }
        }
        return hasMention;
    }
    
    public int numComplexDescription(PullRequest pullRequest) {
        String title = pullRequest.getTitle();
        if(pullRequest.getDescription().getBody()!=null)
        {
        String body = pullRequest.getDescription().getBody();        
        String[] titleWords = title.split("\\s+");
        String[] bodyWords = body.split("\\s+");
        return titleWords.length + bodyWords.length;
        }else {
        	return -1;
        }
    }
    
    /*********************************************************
     * Commit Relevant
     *********************************************************/
    
    public int numCommits(PullRequest pullRequest) {
        List<Commit> commits = pullRequest.getCommits();
        return commits.size();
    }
    
    public int numSrcChurns(PullRequest pullRequest) {
        int churns = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            churns = churns + dfile.getDiffLines().size();
        }
        return churns;
    }
    
    public int numTestChurns(PullRequest pullRequest) {
        int churns = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (dfile.isTest()) {
                churns = churns + dfile.getDiffLines().size();
            }
        }
        return churns;
    }
    
    public int numSrcFiles(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (dfile.isJavaFile()) {
                files++;
            }
        }
        return files;
    }
    
    public int numFilesAdded(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.ADD)) {
                files++;
            }
        }
        return files;
    }
    
    public int numFilesDeleted(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.DELETE)) {
                files++;
            }
        }
        return files;
    }
    
    public int numFilesRevised(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.REVISE)) {
                files++;
            }
        }
        return files;
    }
    
    public int numFilesModified(PullRequest pullRequest) {
        return pullRequest.getFilesChanged().getDiffFiles().size();
    }
    
    public int numDocFiles(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (isDocExtention(dfile)) {
                files++;
            }
        }
        return files;
    }
    
    public int numOtherFiles(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (!dfile.isJavaFile() && !isDocExtention(dfile)) {
                files++;
            }
        }
        return files;
    }
    
    private boolean isDocExtention(DiffFile dfile) {
        String path = dfile.getPath();
        return path.endsWith(".md") || path.endsWith(".html") || path.contains(".adoc");
    }
    
    public boolean testIncluded(PullRequest pullRequest) {
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (dfile.isTest()) {
                return true;
            }
        }
        return false;
    }
    
    public int numCIFailures(PullRequest pullRequest) {
        int failures = 0;
        for (Commit commit : pullRequest.getCommits()) {
            for (CIStatus status : commit.getCIStatus()) {
                if (!status.getState().equals("SUCCESS")) {
                    failures++;
                }
            }
        }
        return failures;
    }
    
    /*********************************************************
     * Participant Relevant
     *********************************************************/
    
    public int numParticipants(PullRequest pullRequest) {
        Set<Participant> participants = pullRequest.getParticipants();
        return participants.size();
    }
    
    public boolean socialDistanceExists(PullRequest pullRequest) {
        Participant submitter = null;
        Participant closeMember = null;
        for (Participant pa : pullRequest.getParticipants()) {
            if (pa.getRole().equals("Author")) {
                submitter = pa;
                break;
            }
        }
        
        for (IssueEvent event : pullRequest.getConversation().getIssueEvents()) {
            if (event.getBody().equals("closed")) {
                closeMember = event.getParticipant();
                break;
            }
        }
        
        if (submitter != null && closeMember != null) {
            return followRelation(submitter, closeMember) == 1 || followRelation(submitter, closeMember) == 2;
        }
        return false;
    }
    
    private int followRelation(Participant participant1, Participant participant2) {
        boolean isFollower1 = false;
        boolean isFollower2 = false;
        for (String follower : participant1.getFollowers()) {
            if (participant2.getLogin().equals(follower)) {
                isFollower1 = true;
                break;
            }
        }
        
        for (String follower : participant2.getFollowers()) {
            if (participant1.getLogin().equals(follower)) {
                isFollower2 = true;
                break;
            }
        }
        
        if (isFollower1 && isFollower2) {
            return 2;
        } else if (isFollower1 && !isFollower2) {
            return -1;
        } else if (!isFollower1 && isFollower2) {
            return 1;
        } else {
            return 0;
        }
    }
    
    /*********************************************************
     * Time Relevant
     *********************************************************/
    
    public long lifetimeMinutes(PullRequest pullRequest) {
        long timeDiff = pullRequest.getEndDate().from(pullRequest.getCreateDate());
        return TimeUnit.MILLISECONDS.toMinutes(timeDiff);
    }
    
    public long mergetimeMinutes(PullRequest pullRequest) {
        if (pullRequest.isMerged() && !pullRequest.isStandardMerged()) {
            for (IssueEvent event: pullRequest.getConversation().getIssueEvents()) {
                if (event.getBody().equals("closed")) {
                    long timeDiff = event.getDate().from(pullRequest.getCreateDate());
                    return TimeUnit.MILLISECONDS.toMinutes(timeDiff);
                }
            }
        } else if (pullRequest.isStandardMerged()) {
            return lifetimeMinutes(pullRequest);
        }
        return -1;
    }
    
    public long totalCILatencyMinutes(PullRequest pullRequest) {
        int commitSize = pullRequest.getCommits().size();
        if (commitSize > 0) {
           Commit lastCommit = pullRequest.getCommits().get(commitSize - 1);
           int ciSize = lastCommit.getCIStatus().size();
           if (ciSize > 0) {
               CIStatus status = lastCommit.getCIStatus().get(ciSize - 1);
               long timeDiff = status.getUpdateDate().from(pullRequest.getCreateDate());
               return TimeUnit.MILLISECONDS.toMinutes(timeDiff);
           }
        }
        return -1;
    }
}
