package org.jtool.prmodel.QuickAccessAPI;

import java.io.File;

import org.jtool.prmodel.CIStatus;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.DiffFile;
import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.PullRequest;

public class CommitFeature {
    
    /**
     * Returns the commit having the sha number in a pull-request.
     * @param pullRequest a pull-request
     * @param commitSha the sha number of a commit
     * @return the found commit
     */
    public static Commit getCommit(PullRequest pullRequest, String commitSha) {
        return pullRequest.getCommits().stream().filter(c -> c.getSha().equals(commitSha)).findFirst().orElse(null);
    }
    
    /**
     * Returns the number of commits in a pull-request.
     * @param pullRequest a pull-request
     * @return the number of the commits
     */
    public static int numCommits(PullRequest pullRequest) {
        return pullRequest.getCommits().size();
    }
    
    /**
     * Returns the number of churns in a pull-request.
     * @param pullRequest a pull-request
     * @return the number of the churns
     */
    public static int numSrcChurns(PullRequest pullRequest) {
        int churns = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            churns = churns + dfile.getDiffLines().size();
        }
        return churns;
    }
    
    /**
     * Returns the number of churns in a commit having the sha number.
     * @param pullRequest a pull-request
     * @param commitSha the sha number of a commit
     * @return the number of the churns
     */
    public static int numSrcChurns(PullRequest pullRequest, String commitSha) {
        int churns = 0;
        Commit commit = CommitFeature.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getCodeChange().getDiffFiles()) {
            churns = churns + dfile.getDiffLines().size();
        }
        return churns;
    }
    
    /**
     * Returns the number of test-code related churns in a pull-request.
     * @param pullRequest a pull-request
     * @return the number of the churns
     */
    public static int numTestChurns(PullRequest pullRequest) {
        int churns = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (dfile.isTest()) {
                churns = churns + dfile.getDiffLines().size();
            }
        }
        return churns;
    }
    
    /**
     * Returns the number of test-code related churns in a commit having the sha number.
     * @param pullRequest a pull-request
     * @param commitSha the sha number of a commit
     * @return the number of the churns
     */
    public static int numTestChurns(PullRequest pullRequest, String commitSha) {
        int churns = 0;
        Commit commit = CommitFeature.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getCodeChange().getDiffFiles()) {
            if (dfile.isTest()) {
                churns = churns + dfile.getDiffLines().size();
            }
        }
        return churns;
    }
    
    public static int numFilesAdded(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.ADD)) {
                files++;
            }
        }
        return files;
    }
    
    public static int numFilesAdded(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = CommitFeature.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getCodeChange().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.ADD)) {
                files++;
            }
        }
        return files;
    }
    
    public static int numFilesDeleted(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.DELETE)) {
                files++;
            }
        }
        return files;
    }
    
    public static int numFilesDeleted(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = CommitFeature.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getCodeChange().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.DELETE)) {
                files++;
            }
        }
        return files;
    }
    
    public static int numFilesRevised(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.REVISE)) {
                files++;
            }
        }
        return files;
    }
    
    public static int numFilesRevised(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = CommitFeature.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getCodeChange().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.REVISE)) {
                files++;
            }
        }
        return files;
    }
    
    public static int numFilesModified(PullRequest pullRequest) {
        return pullRequest.getFilesChanged().getDiffFiles().size();
    }
    
    public static int numFilesModified(PullRequest pullRequest, String commitSha) {
        Commit commit = CommitFeature.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        return commit.getCodeChange().getDiffFiles().size();
    }
    
    public static int numSrcFiles(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (dfile.isJavaFile()) {
                files++;
            }
        }
        return files;
    }
    
    public static int numSrcFiles(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = CommitFeature.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getCodeChange().getDiffFiles()) {
            if (dfile.isJavaFile()) {
                files++;
            }
        }
        return files;
    }
    
    private static boolean checkFilenameExtention(DiffFile dfile) {
        String[] names = dfile.getRelativePath().split(File.separator);
        String ext = names[names.length - 1];
        return ext.contains(".md") || ext.contains(".html") || ext.contains(".adoc");
    }
    
    public static int numDocFiles(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (checkFilenameExtention(dfile)) {
                files++;
            }
        }
        return files;
    }
    
    public static int numDocFiles(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = CommitFeature.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getCodeChange().getDiffFiles()) {
            if (checkFilenameExtention(dfile)) {
                files++;
            }
        }
        return files;
    }
    
    public static int numOtherFiles(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (!dfile.isJavaFile() && !checkFilenameExtention(dfile)) {
                files++;
            }
        }
        return files;
    }
    
    public static int numOtherFiles(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = CommitFeature.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getCodeChange().getDiffFiles()) {
            if (!dfile.isJavaFile() && !checkFilenameExtention(dfile)) {
                files++;
            }
        }
        return files;
    }
    
    public static boolean testIncluded(PullRequest pullRequest) {
        return numTestChurns(pullRequest) > 0;
    }
    
    public static boolean testIncluded(PullRequest pullRequest, String commitSha) {
        return numTestChurns(pullRequest, commitSha) > 0;
    }
    
    public static int numCIFailures(PullRequest pullRequest) {
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
}
