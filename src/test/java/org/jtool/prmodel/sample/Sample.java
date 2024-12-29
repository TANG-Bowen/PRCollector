package org.jtool.prmodel.sample;

import java.util.List;
import java.util.ArrayList;

import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.DiffFile;
import org.jtool.prmodel.Participant;
import org.jtool.prmodel.ReviewComment;
import org.jtool.prmodel.Action;
import org.jtool.jxp3model.FileChange;
import org.jtool.jxp3model.ClassChange;
import org.jtool.jxp3model.MethodChange;

public class Sample {
    
    public long numSrcChurns(PullRequest pullRequest) {
        int churns = 0;
        for (DiffFile dfile : pullRequest.getFilesChanged().getDiffFiles()) {
            churns = churns + dfile.getDiffLines().size();
        }
        return churns;
    }
    
    public long numSrcChurns(PullRequest pullRequest, String commitSha) {
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
    
    public Participant getAuthor(PullRequest pullRequest) {
        for (Participant participant : pullRequest.getParticipants()) {
            if (participant.getRole().equals("Author")) {
                return participant;
            }
        }
        return null;
    }
    
    public List<ReviewComment> getReviewCommentsWithMentions(PullRequest pullRequest) {
        List<ReviewComment> comments = new ArrayList<>();
        for (ReviewComment comment : pullRequest.getConversation().getReviewComments()) {
            if (!comment.getMarkdownDoc().getMentionStrings().isEmpty()) {
                comments.add(comment);
            }
        }
        return comments;
    }
    
    public List<ReviewComment> getReviewCommentsByAuthor(PullRequest pullRequest) {
        Participant participant = getAuthor(pullRequest);
        if (participant == null) {
            return new ArrayList<>();
        }
        List<ReviewComment> comments = new ArrayList<>();
        for (ReviewComment comment: pullRequest.getConversation().getReviewComments()) {
            if (comment.getParticipant().equals(participant)) {
                comments.add(comment);
            }
        }
        return comments;
    }
    
    public List<ClassChange> changedClasses(PullRequest pullRequest) {
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
    
    public List<MethodChange> changedMethods(PullRequest pullRequest) {
        List<MethodChange> methods = new ArrayList<>();
        for (ClassChange cchange : changedClasses(pullRequest)) {
            for (MethodChange mchange : cchange.getMethodChanges()) {
                methods.add(mchange);
            }
        }
        return methods;
    }
    
    public long firstReviewCommentResponse_ms(PullRequest pullRequest) {
        for (Action action : pullRequest.getConversation().getTimeLine()) {
            if (action.getActionType().equals("ReviewComment")) {
                return action.getDate().from(pullRequest.getCreateDate());
            }
        }
        return 0;
    }
}