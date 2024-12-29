package org.jtool.prmodel.QuickAccessAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jtool.jxp3model.ClassChange;
import org.jtool.jxp3model.FieldChange;
import org.jtool.jxp3model.FileChange;
import org.jtool.jxp3model.MethodChange;
import org.jtool.prmodel.CIStatus;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.DiffFile;
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
    
    public static int numFilesModified(PullRequest pullRequest) {
        return pullRequest.getFilesChanged().getDiffFiles().size();
    }
    
    public static int numFilesModified(PullRequest pullRequest, String commitSha) {
        Commit commit = pullRequest.getCommit(commitSha);
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
    
    public static List<ClassChange> changedClasses(PullRequest pullRequest) {
        List<ClassChange> classes = new ArrayList<>();
        for (Commit commit : pullRequest.getCommits()) {
            for (FileChange fchange : commit.getCodeChange().getFileChanges()) {
                for (ClassChange cchange : fchange.getClassChanges()) {
                    classes.add(cchange);
                }
            }
        }
        return classes;
    }
    
    public static List<ClassChange> changedClasses(PullRequest pullRequest, String commitSha) {
        List<ClassChange> classes = new ArrayList<>();
        Commit commit = pullRequest.getCommit(commitSha);
        if (commit == null) {
            return classes;
        }
        
        for (FileChange fchange : commit.getCodeChange().getFileChanges()) {
            for (ClassChange cchange : fchange.getClassChanges()) {
                classes.add(cchange);
            }
        }
        return classes;
    }
    
    public static List<MethodChange> changedMethods(PullRequest pullRequest) {
        List<MethodChange> methods = new ArrayList<>();
        for (ClassChange cchange : changedClasses(pullRequest)) {
            for (MethodChange mchange : cchange.getMethodChanges()) {
                methods.add(mchange);
            }
        }
        return methods;
    }
    
    public static List<MethodChange> changedMethods(PullRequest pullRequest, String commitSha) {
        List<MethodChange> methods = new ArrayList<>();
        for (ClassChange cchange : changedClasses(pullRequest, commitSha)) {
            for (MethodChange mchange : cchange.getMethodChanges()) {
                methods.add(mchange);
            }
        }
        return methods;
    }
    
    public static List<FieldChange> changedFields(PullRequest pullRequest) {
        List<FieldChange> fields = new ArrayList<>();
        for (ClassChange cchange : changedClasses(pullRequest)) {
            for (FieldChange fchange : cchange.getFieldChanges()) {
                fields.add(fchange);
            }
        }
        return fields;
    }
    
    public static List<FieldChange> changedFields(PullRequest pullRequest, String commitSha) {
        List<FieldChange> fields = new ArrayList<>();
        for (ClassChange cchange : changedClasses(pullRequest, commitSha)) {
            for (FieldChange fchange : cchange.getFieldChanges()) {
                fields.add(fchange);
            }
        }
        return fields;
    }
}
