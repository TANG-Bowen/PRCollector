package org.jtool.prmodel.QuickAccessAPI;

import java.util.ArrayList;
import java.util.List;

import org.jtool.jxp3model.ClassChange;
import org.jtool.jxp3model.FieldChange;
import org.jtool.jxp3model.FileChange;
import org.jtool.jxp3model.MethodChange;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.PullRequest;

public class CodeChangeFeature {
    
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
        Commit commit = CommitFeature.getCommit(pullRequest, commitSha);
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
        for (ClassChange cchange : CodeChangeFeature.changedClasses(pullRequest)) {
            for (MethodChange mchange : cchange.getMethodChanges()) {
                methods.add(mchange);
            }
        }
        return methods;
    }
    
    public static List<MethodChange> changedMethods(PullRequest pullRequest, String commitSha) {
        List<MethodChange> methods = new ArrayList<>();
        for (ClassChange cchange : CodeChangeFeature.changedClasses(pullRequest, commitSha)) {
            for (MethodChange mchange : cchange.getMethodChanges()) {
                methods.add(mchange);
            }
        }
        return methods;
    }
    
    public static List<FieldChange> changedFields(PullRequest pullRequest) {
        List<FieldChange> fields = new ArrayList<>();
        for (ClassChange cchange : CodeChangeFeature.changedClasses(pullRequest)) {
            for (FieldChange fchange : cchange.getFieldChanges()) {
                fields.add(fchange);
            }
        }
        return fields;
    }
    
    public static List<FieldChange> changedFields(PullRequest pullRequest, String commitSha) {
        List<FieldChange> fields = new ArrayList<>();
        for (ClassChange cchange : CodeChangeFeature.changedClasses(pullRequest, commitSha)) {
            for (FieldChange fchange : cchange.getFieldChanges()) {
                fields.add(fchange);
            }
        }
        return fields;
    }
}
