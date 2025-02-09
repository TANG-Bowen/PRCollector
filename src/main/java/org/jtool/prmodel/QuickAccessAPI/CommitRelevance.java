/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.prmodel.QuickAccessAPI;

import java.io.File;

import org.jtool.prmodel.CIStatus;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.DiffFile;
import org.jtool.prmodel.DiffLine;
import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.PullRequest;

public class CommitRelevance {
    
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
        for (DiffFile dfile : pullRequest.getChangeSummary().getDiffFiles()) {
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
        Commit commit = pullRequest.getCommit(commitSha);
        if (commit == null) {
            return 0;
        }
        
        for (DiffFile dfile : commit.getCodeChange().getDiffFiles()) {
            churns = churns + dfile.getDiffLines().size();
        }
        return churns;
    }
    
    /**
     * Returns the number of added churns in a pull-request.
     * @param pullRequest a pull-request
     * @return the number of the churns
     */
    public static int numSrcAddChurns(PullRequest pullRequest) {
    	int churns = 0;
    	for(DiffFile dfile : pullRequest.getChangeSummary().getDiffFiles()) {
    		for(DiffLine dline : dfile.getDiffLines()) {
    			if(dline.getChangeType().equals(PRElement.ADD)) {
    				churns++;
    			}
    		}
    	}
    	return churns;
    }
    
    
    /**
     * Returns the number of added churns in a commit having the sha number.
     * @param pullRequest a pull-request
     * @param commitSha the sha number of a commit
     * @return the number of the churns
     */
    public static int numSrcAddChurns(PullRequest pullRequest, String commitSha) {
    	int churns = 0;
    	Commit commit = pullRequest.getCommit(commitSha);
    	if(commit == null) {
    		return 0;
    	}
    	for(DiffFile dfile : commit.getCodeChange().getDiffFiles()) {
    		for(DiffLine dline : dfile.getDiffLines()) {
    			if(dline.getChangeType().equals(PRElement.ADD)) {
    				churns++;
    			}
    		}
    	}
    	return churns;
    }
    
    /**
     * Returns the number of deleted churns in a pull-request.
     * @param pullRequest a pull-request
     * @return the number of the churns
     */
    public static int numSrcDeletedChurns(PullRequest pullRequest) {
    	int churns = 0;
    	for(DiffFile dfile : pullRequest.getChangeSummary().getDiffFiles()) {
    		for(DiffLine dline : dfile.getDiffLines()) {
    			if(dline.getChangeType().equals(PRElement.DELETE)) {
    				churns++;
    			}
    		}
    	}
    	return churns;
    }
    
    /**
     * Returns the number of deleted churns in a commit having the sha number.
     * @param pullRequest a pull-request
     * @param commitSha the sha number of a commit
     * @return the number of the churns
     */
    public static int numSrcDeleteChurns(PullRequest pullRequest, String commitSha) {
        int churns = 0;
        Commit commit = pullRequest.getCommit(commitSha);
        if(commit == null) {
            return 0;
        }
        for(DiffFile dfile : commit.getCodeChange().getDiffFiles()) {
            for(DiffLine dline : dfile.getDiffLines()) {
                if(dline.getChangeType().equals(PRElement.DELETE)) {
                    churns++;
                }
            }
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
        for (DiffFile dfile : pullRequest.getChangeSummary().getDiffFiles()) {
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
        Commit commit = pullRequest.getCommit(commitSha);
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
    
    /**
     * Returns the number of added files in a pull-request.
     * @param pullRequest a pull-request
     * @return the number of the files
     */
    public static int numFilesAdded(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getChangeSummary().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.ADD)) {
                files++;
            }
        }
        return files;
    }
    
    /**
     * Returns the number of added files in a commit having the sha number.
     * @param pullRequest a pull-request
     * @param commitSha the sha number of a commit
     * @return the number of the files
     */
    public static int numFilesAdded(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = pullRequest.getCommit(commitSha);
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
    
    /**
     * Returns the number of deleted files in a pull-request.
     * @param pullRequest a pull-request
     * @return the number of the files
     */
    public static int numFilesDeleted(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getChangeSummary().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.DELETE)) {
                files++;
            }
        }
        return files;
    }
    
    /**
     * Returns the number of deleted files in a commit having the sha number.
     * @param pullRequest a pull-request
     * @param commitSha the sha number of a commit
     * @return the number of the files
     */
    public static int numFilesDeleted(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = pullRequest.getCommit(commitSha);
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
    
    /**
     * Returns the number of revised files in a pull-request.
     * @param pullRequest a pull-request
     * @return the number of the files
     */
    public static int numFilesRevised(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getChangeSummary().getDiffFiles()) {
            if (dfile.getChangeType().equals(PRElement.REVISE)) {
                files++;
            }
        }
        return files;
    }
    
    /**
     * Returns the number of revised files in a commit having the sha number.
     * @param pullRequest a pull-request
     * @param commitSha the sha number of a commit
     * @return the number of the files
     */
    public static int numFilesRevised(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = pullRequest.getCommit(commitSha);
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
    
    /**
     * Returns the total number of modified files in a pull-request.
     * @param pullRequest a pull-request
     * @return the number of the files
     */
    public static int numFilesModified(PullRequest pullRequest) {
        return pullRequest.getChangeSummary().getDiffFiles().size();
    }
    
    /**
     * Returns the total number of modified files in a commit having the sha number.
     * @param pullRequest a pull-request
     * @param commitSha the sha number of a commit
     * @return the number of the files
     */
    public static int numFilesModified(PullRequest pullRequest, String commitSha) {
        Commit commit = pullRequest.getCommit(commitSha);
        if (commit == null) {
            return 0;
        }
        return commit.getCodeChange().getDiffFiles().size();
    }
    
    /**
     * Returns the total number of java source files in a pull-request.
     * @param pullRequest a pull-request
     * @return the number of the files
     */
    public static int numSrcFiles(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getChangeSummary().getDiffFiles()) {
            if (dfile.isJavaFile()) {
                files++;
            }
        }
        return files;
    }
    
    /**
     * Returns the total number of java source files in a commit having the sha number.
     * @param pullRequest a pull-request
     * @param commitSha the sha number of a commit
     * @return the number of the files
     */
    public static int numSrcFiles(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = pullRequest.getCommit(commitSha);
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
        String[] names = dfile.getPath().split(File.separator);
        String ext = names[names.length - 1];
        return ext.contains(".md") || ext.contains(".html") || ext.contains(".adoc");
    }
    
    /**
     * Returns the total number of document files in a pull-request.
     * @param pullRequest a pull-request
     * @return the number of the files
     */
    public static int numDocFiles(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getChangeSummary().getDiffFiles()) {
            if (checkFilenameExtention(dfile)) {
                files++;
            }
        }
        return files;
    }
    
    /**
     * Returns the total number of document files in a commit having the sha number.
     * @param pullRequest a pull-request
     * @param commitSha the sha number of a commit
     * @return the number of the files
     */
    public static int numDocFiles(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = pullRequest.getCommit(commitSha);
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
    
    /**
     * Returns the total number of neither java source files nor document files in a pull-request.
     * @param pullRequest a pull-request
     * @return the number of the files
     */
    public static int numOtherFiles(PullRequest pullRequest) {
        int files = 0;
        for (DiffFile dfile : pullRequest.getChangeSummary().getDiffFiles()) {
            if (!dfile.isJavaFile() && !checkFilenameExtention(dfile)) {
                files++;
            }
        }
        return files;
    }
    
    /**
     * Returns the total number of neither java source files nor document files in a commit having the sha number.
     * @param pullRequest a pull-request
     * @param commitSha the sha number of a commit
     * @return the number of the files
     */
    public static int numOtherFiles(PullRequest pullRequest, String commitSha) {
        int files = 0;
        Commit commit = pullRequest.getCommit(commitSha);
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
    
    /**
     * Returns if at least one test churn included in this pull request.
     * @param pullRequest a pull-request
     * @return true if at least one test churn exists
     */
    public static boolean testIncluded(PullRequest pullRequest) {
        return numTestChurns(pullRequest) > 0;
    }
    
    /**
     * Returns if at least one test churn included in a commit having the sha number.
     * @param pullRequest a pull-request
     * @param commitSha the sha number of a commit
     * @return true if at least one test churn exists
     */
    public static boolean testIncluded(PullRequest pullRequest, String commitSha) {
        return numTestChurns(pullRequest, commitSha) > 0;
    }
    
    /**
     * Returns the number of CI failures in a pull-request.
     * @param pullRequest a pull-request
     * @return the number of the CI failures
     */
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
