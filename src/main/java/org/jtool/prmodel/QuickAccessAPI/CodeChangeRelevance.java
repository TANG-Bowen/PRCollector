package org.jtool.prmodel.QuickAccessAPI;

import java.util.ArrayList;
import java.util.List;

import org.jtool.jxp3model.ClassChange;
import org.jtool.jxp3model.FieldChange;
import org.jtool.jxp3model.FileChange;
import org.jtool.jxp3model.MethodChange;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.Participant;
import org.jtool.prmodel.PullRequest;

public class CodeChangeRelevance {
    
    public static List<ClassChange> changedClasses(PullRequest pullRequest) {
        return changedClasses(pullRequest.getCommits());
    }
    
    public static List<ClassChange> changedClassesByCommit(PullRequest pullRequest, String commitSha) {
        List<Commit> commits = new ArrayList<>();
        Commit commit = pullRequest.getCommit(commitSha);
        if (commit != null) {
            commits.add(commit);
        } else {
            return new ArrayList<>();
        }
        return changedClasses(commits);
    }
    
    public static List<ClassChange> changedClassesByCommiter(PullRequest pullRequest, Participant committer) {
        List<Commit> commits = new ArrayList<>();
        for (Commit commit : commits) {
            if (commit.getCommitter().equals(committer)) {
                commits.add(commit);
            }
        }
        return changedClasses(commits);
    }
    
    private static List<ClassChange> changedClasses(List<Commit> commits) {
        List<ClassChange> classes = new ArrayList<>();
        for (Commit commit : commits) {
            for (FileChange fchange : commit.getCodeChange().getFileChanges()) {
                for (ClassChange cchange : fchange.getClassChanges()) {
                    classes.add(cchange);
                }
            }
        }
        return classes;
    }
    
    public static List<MethodChange> changedMethods(PullRequest pullRequest) {
        return changedMethods(changedClasses(pullRequest));
    }
    
    public static List<MethodChange> changedMethodsByCommit(PullRequest pullRequest, String commitSha) {
        return changedMethods(changedClassesByCommit(pullRequest, commitSha));
    }
    
    public static List<MethodChange> changedMethodsByCommiter(PullRequest pullRequest, Participant committer) {
        return changedMethods(changedClassesByCommiter(pullRequest, committer));
    }
    
    private static List<MethodChange> changedMethods(List<ClassChange> cchanges) {
        List<MethodChange> methods = new ArrayList<>();
        for (ClassChange cchange : cchanges) {
            for (MethodChange mchange : cchange.getMethodChanges()) {
                methods.add(mchange);
            }
        }
        return methods;
    }
    
    public static List<FieldChange> changedFields(PullRequest pullRequest) {
        return changedFields(changedClasses(pullRequest));
    }
    
    public static List<FieldChange> changeddFieldsByCommit(PullRequest pullRequest, String commitSha) {
        return changedFields(changedClassesByCommit(pullRequest, commitSha));
    }
    
    public static List<FieldChange> changedFieldsByCommiter(PullRequest pullRequest, Participant committer) {
        return changedFields(changedClassesByCommiter(pullRequest, committer));
    }
    
    private static List<FieldChange> changedFields(List<ClassChange> cchanges) {
        List<FieldChange> fields = new ArrayList<>();
        for (ClassChange cchange : cchanges) {
            for (FieldChange fchange : cchange.getFieldChanges()) {
                fields.add(fchange);
            }
        }
        return fields;
    }
}
