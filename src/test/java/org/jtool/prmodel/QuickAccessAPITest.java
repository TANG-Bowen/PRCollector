package org.jtool.prmodel;

import org.jtool.prmodel.QuickAccessAPI.*;

public class QuickAccessAPITest {
    
    private static final String testCommitSha1 = "fd16ae78424bfce31439cc0fc67005cf7d14dba3";
    
    private PullRequest pullRequest;
    
    QuickAccessAPITest(PullRequest pullRequest) {
        this.pullRequest = pullRequest;
    }
    
    public void print() {
        printParticipantRelevance();
        printDiscriptionRelevance();
        printConversationRelevance();
        printCommitRelevance();
        printCodeChangeRelevance();
        printTimeRelevance();
    }
    
    public void printParticipantRelevance() {
        System.out.println();
        System.out.println("Participant : ");
        System.out.println("Num of participants : " + ParticipantRelevance.numParticipants(pullRequest));
        System.out.println("Social distance exists : " + ParticipantRelevance.socialDistanceExists(pullRequest));
        System.out.println("Author : "                 + ParticipantRelevance.getAuthor(pullRequest));
        System.out.println("Num of reviewers : "       + ParticipantRelevance.getReviewers(pullRequest).size());
    }
    
    public void printDiscriptionRelevance() {
        System.out.println();
        System.out.println("Discription : ");
        System.out.println("Num of complex title : "           + DescriptionRelevance.numComplexTitle(pullRequest));
        System.out.println("Num of complex description : "     + DescriptionRelevance.numComplexDescription(pullRequest));
        System.out.println("Num of mentions in description : " + DescriptionRelevance.getMentionsInDescription(pullRequest).size());
    }
    
    public void printConversationRelevance() {
        System.out.println();
        System.out.println("Conversation : ");
        System.out.println("Num of issue comments : "                + ConversationRelevance.numIssueComments(pullRequest));
        System.out.println("Num of review comments : "               + ConversationRelevance.numReviewComments(pullRequest));
        System.out.println("Num of comments : "                      + ConversationRelevance.numComments(pullRequest));
        System.out.println("Num of issue comments with mentions : "  + ConversationRelevance.getIssueCommentsWithMentions(pullRequest).size());
        System.out.println("Num of review comments with mentions : " + ConversationRelevance.getReviewCommentsWithMentions(pullRequest).size());
        System.out.println("Num of review events with mentions : "   + ConversationRelevance.getReviewEventsWithMentions(pullRequest).size());
        System.out.println("Mention exists : "                       + ConversationRelevance.mentionExists(pullRequest));
        
        for (Participant pa : pullRequest.getParticipants()) {
            System.out.println("Num of issue events by participant : "    + ConversationRelevance.getIssueEventsByParticipant(pullRequest, pa).size());
            System.out.println("Num of review events by participant : "   + ConversationRelevance.getReviewEventsByParticipant(pullRequest, pa).size());
            System.out.println("Num of issue comments by participant : "  + ConversationRelevance.getIssueCommentsByParticipant(pullRequest, pa).size());
            System.out.println("Num of review comments by participant : " + ConversationRelevance.getReviewCommentsByParticipant(pullRequest, pa).size());
            System.out.println("Num of prior interactions : "             + ConversationRelevance.numPriorInteractionsByParticipant(pullRequest, pa));
        }
    }
    
    public void printCommitRelevance() {
        System.out.println();
        System.out.println("Commit : ");
        System.out.println("Num of commits : "                   + CommitRelevance.numCommits(pullRequest));
        System.out.println("Num of src churns : "                + CommitRelevance.numSrcChurns(pullRequest));
        System.out.println("Num of src churns for commit : "     + CommitRelevance.numSrcChurns(pullRequest, testCommitSha1));
        System.out.println("Num of test churns : "               + CommitRelevance.numTestChurns(pullRequest));
        System.out.println("Num of test churns for commit : "    + CommitRelevance.numTestChurns(pullRequest, testCommitSha1));
        System.out.println("Num of files added : "               + CommitRelevance.numFilesAdded(pullRequest));
        System.out.println("Num of files added for commit : "    + CommitRelevance.numFilesAdded(pullRequest, testCommitSha1));
        System.out.println("Num of files deleted : "             + CommitRelevance.numFilesDeleted(pullRequest));
        System.out.println("Num of files deleted for commit : "  + CommitRelevance.numFilesDeleted(pullRequest, testCommitSha1));
        System.out.println("Num of files revised : "             + CommitRelevance.numFilesRevised(pullRequest));
        System.out.println("Num of files revised for commit : "  + CommitRelevance.numFilesRevised(pullRequest, testCommitSha1));
        System.out.println("Num of files modified : "            + CommitRelevance.numFilesModified(pullRequest));
        System.out.println("Num of files modified for commit : " + CommitRelevance.numFilesModified(pullRequest, testCommitSha1));
        System.out.println("Num of src files : "                 + CommitRelevance.numSrcFiles(pullRequest));
        System.out.println("Num of src files for commit : "      + CommitRelevance.numSrcFiles(pullRequest, testCommitSha1));
        System.out.println("Num of doc files : "                 + CommitRelevance.numDocFiles(pullRequest));
        System.out.println("Num of doc files for commit : "      + CommitRelevance.numDocFiles(pullRequest, testCommitSha1));
        System.out.println("Num of other_files : "               + CommitRelevance.numOtherFiles(pullRequest));
        System.out.println("Num of other_files_commit : "        + CommitRelevance.numOtherFiles(pullRequest, testCommitSha1));
        System.out.println("test included : "                    + CommitRelevance.testIncluded(pullRequest));
        System.out.println("test included for commit : "         + CommitRelevance.testIncluded(pullRequest, testCommitSha1));
        System.out.println("CI_failures : "                      + CommitRelevance.numCIFailures(pullRequest));                

    }
    
    public void printCodeChangeRelevance() {
        System.out.println("Num of changed classes : "           + CodeChangeRelevance.changedClasses(pullRequest).size());
        System.out.println("Num of changed methods : "           + CodeChangeRelevance.changedMethods(pullRequest).size());
        System.out.println("Num of changed fields  : "           + CodeChangeRelevance.changedFields(pullRequest).size());
    }
    
    public void printTimeRelevance() {
        System.out.println();
        System.out.println("TimeR : ");
        System.out.println("Milli seconds for lifetime: "                       + TimeRelevance.lifetime_ms(pullRequest));
        System.out.println("Milli seconds for mergetime : "                     + TimeRelevance.mergetime_ms(pullRequest));
        System.out.println("Milli seconds for first issue comment response : "  + TimeRelevance.firstIssueCommentResponse_ms(pullRequest));
        System.out.println("Milli seconds for first review comment response : " + TimeRelevance.firstReviewCommentResponse_ms(pullRequest));
        System.out.println("Milli seconds for First iisue event response : "    + TimeRelevance.firstIssueEventResponse_ms(pullRequest));
        System.out.println("Milli seconds for first review event response : "   + TimeRelevance.firstReviewEventResponse_ms(pullRequest));
        System.out.println("Milli seconds for total CI latency : "              + TimeRelevance.totalCILatency_ms(pullRequest));
    }
}
