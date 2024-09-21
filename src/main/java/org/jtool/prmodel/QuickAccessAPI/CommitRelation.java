package org.jtool.prmodel.QuickAccessAPI;

import java.util.List;
import java.io.File;

import org.jtool.prmodel.CIStatus;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.DiffFile;
import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.PullRequest;

public class CommitRelation {
    
    /**
     * Returns commits in a pull-request.
     * @param pullRequest a pull-request
     * @return the collection of the commits
     */
    public static List<Commit> getCommits(PullRequest pullRequest) {
        return pullRequest.getCommits();
    }
    
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
    public static int num_commits(PullRequest pullRequest) {
        return pullRequest.getCommits().size();
    }
    
    /**
     * Returns the number of churns in a pull-request.
     * @param pullRequest a pull-request
     * @return the number of the churns
     */
    public static int src_churn(PullRequest pullRequest) {
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
    public static int src_churn(PullRequest pullRequest, String commitSha) {
        int churns = 0;
        Commit commit = CommitRelation.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getDiff().getDiffFiles()) {
            churns = churns + dfile.getDiffLines().size();
        }
        return churns;
    }
    
    /**
     * Returns the number of test-code related churns in a pull-request.
     * @param pullRequest a pull-request
     * @return the number of the churns
     */
    public static int test_churn(PullRequest pullRequest) {
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
    public static int test_churn(PullRequest pullRequest, String commitSha) {
        int churns = 0;
        Commit commit = CommitRelation.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getDiff().getDiffFiles()) {
            if (dfile.isTest()) {
                churns = churns + dfile.getDiffLines().size();
            }
        }
        return churns;
    }
    
    public static int files_added(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.ADD)) {
                files++;
            }
        }
        return files;
    }
    
    public static int files_added(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = CommitRelation.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getDiff().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.ADD)) {
                files++;
            }
        }
        return files;
    }
    
    public static int files_deleted(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.DELETE)) {
                files++;
            }
        }
        return files;
    }
    
    public static int files_deleted(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = CommitRelation.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getDiff().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.DELETE)) {
                files++;
            }
        }
        return files;
    }
    
    public static int files_modified(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.CHANGE)) {
                files++;
            }
        }
        return files;
    }
    
    public static int files_modified(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = CommitRelation.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getDiff().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.CHANGE)) {
                files++;
            }
        }
        return files;
    }
    
    public static int files_changed(PullRequest pullRequest) {
        return pullRequest.getFilesChanged().getDiffFiles().size();
    }
    
    public static int files_changed(PullRequest pullRequest, String commitSha) {
        Commit commit = CommitRelation.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        return commit.getDiff().getDiffFiles().size();
    }
    
    public static int src_files(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (dfile.isJavaFile()) {
                files++;
            }
        }
        return files;
    }
    
    public static int src_files(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = CommitRelation.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getDiff().getDiffFiles()) {
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
    
    public static int doc_files(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (checkFilenameExtention(dfile)) {
                files++;
            }
        }
        return files;
    }
    
    public static int doc_files(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = CommitRelation.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getDiff().getDiffFiles()) {
            if (checkFilenameExtention(dfile)) {
                files++;
            }
        }
        return files;
    }
    
    public static int other_files(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            if (!dfile.isJavaFile() && !checkFilenameExtention(dfile)) {
                files++;
            }
        }
        return files;
    }
    
    public static int other_files(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = CommitRelation.getCommit(pullRequest, commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getDiff().getDiffFiles()) {
            if (!dfile.isJavaFile() && !checkFilenameExtention(dfile)) {
                files++;
            }
        }
        return files;
    }
    
    public static boolean test_inclusion(PullRequest pullRequest) {
        return test_churn(pullRequest) > 0;
    }
    
    public static boolean test_inclusion(PullRequest pullRequest, String commitSha) {
        return test_churn(pullRequest, commitSha) > 0;
    }
    
    public static int ci_failures(PullRequest pullRequest) {
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
