package org.jtool.prmodel;

import org.jtool.prmodel.QuickAccessAPI.*;

public class QuickAccessAPITest {
    
    private static final String testCommitSha1 = "fd16ae78424bfce31439cc0fc67005cf7d14dba3";
    
    private PullRequest pullRequest;
    
    QuickAccessAPITest(PullRequest pullRequest) {
        this.pullRequest = pullRequest;
    }
    
    public void print() {
        printCommitFeature();
        printCommitFeature();
        printConversationFeature();
        printDiscriptionFeature();
        printEventFeature();
        printParticipantFeature();
        printTimeFeature();
    }
    
    
    public void printCommitFeature() {
        System.out.println();
        System.out.println("Commit : ");
        System.out.println("Num of commits : "                   + CommitFeature.numCommits(pullRequest));
        System.out.println("Num of src churns : "                + CommitFeature.numSrcChurns(pullRequest));
        System.out.println("Num of src churns for commit : "     + CommitFeature.numSrcChurns(pullRequest, testCommitSha1));
        System.out.println("Num of test churns : "               + CommitFeature.numTestChurns(pullRequest));
        System.out.println("Num of test churns for commit : "    + CommitFeature.numTestChurns(pullRequest, testCommitSha1));
        System.out.println("Num of files added : "               + CommitFeature.numFilesAdded(pullRequest));
        System.out.println("Num of files added for commit : "    + CommitFeature.numFilesAdded(pullRequest, testCommitSha1));
        System.out.println("Num of files deleted : "             + CommitFeature.numFilesDeleted(pullRequest));
        System.out.println("Num of files deleted for commit : "  + CommitFeature.numFilesDeleted(pullRequest, testCommitSha1));
        System.out.println("Num of files revised : "             + CommitFeature.numFilesRevised(pullRequest));
        System.out.println("Num of files revised for commit : "  + CommitFeature.numFilesRevised(pullRequest, testCommitSha1));
        System.out.println("Num of files modified : "            + CommitFeature.numFilesModified(pullRequest));
        System.out.println("Num of files modified for commit : " + CommitFeature.numFilesModified(pullRequest, testCommitSha1));
        System.out.println("Num of src files : "                 + CommitFeature.numSrcFiles(pullRequest));
        System.out.println("Num of src files for commit : "      + CommitFeature.numSrcFiles(pullRequest, testCommitSha1));
        System.out.println("Num of doc files : "                 + CommitFeature.numDocFiles(pullRequest));
        System.out.println("Num of doc files for commit : "      + CommitFeature.numDocFiles(pullRequest, testCommitSha1));
        System.out.println("Num of other_files : "               + CommitFeature.numOtherFiles(pullRequest));
        System.out.println("Num of other_files_commit : "        + CommitFeature.numOtherFiles(pullRequest, testCommitSha1));
        System.out.println("test included : "                    + CommitFeature.testIncluded(pullRequest));
        System.out.println("test included for commit : "         + CommitFeature.testIncluded(pullRequest, testCommitSha1));
        System.out.println("CI_failures : "                      + CommitFeature.numCIFailures(pullRequest));                
    }
    
    public void printConversationFeature() {
        System.out.println();
        System.out.println("Conversation : ");
        System.out.println("Num of issue comments : "                + ConversationFeature.numIssueComments(pullRequest));
        System.out.println("Num of review comments : "               + ConversationFeature.numReviewComments(pullRequest));
        System.out.println("Num of comments : "                      + ConversationFeature.numComments(pullRequest));
        System.out.println("Num of issue comments with mentions : "  + ConversationFeature.getIssueCommentsWithMentions(pullRequest).size());
        System.out.println("Num of review comments with mentions : " + ConversationFeature.getReviewCommentsWithMentions(pullRequest).size());
        System.out.println("Num of review events with mentions : "   + ConversationFeature.getReviewEventsWithMentions(pullRequest).size());
        System.out.println("Mention exists : "                       + ConversationFeature.mentionExists(pullRequest));
        for (Participant pa : pullRequest.getParticipants()) {
            System.out.println("Num of issue events by participant : "    + ConversationFeature.getIssueEventsByParticipant(pullRequest, pa).size());
            System.out.println("Num of review events by participant : "   + ConversationFeature.getReviewEventsByParticipant(pullRequest, pa).size());
            System.out.println("Num of issue comments by participant : "  + ConversationFeature.getIssueCommentsByParticipant(pullRequest, pa).size());
            System.out.println("Num of review comments by participant : " + ConversationFeature.getReviewCommentsByParticipant(pullRequest, pa).size());
        }
        
    }
    
    public void printDiscriptionFeature() {
        System.out.println();
        System.out.println("Discription : ");
        System.out.println("Num of complex title : "           + DescriptionFeature.numComplexTitle(pullRequest));
        System.out.println("Num of complex description : "     + DescriptionFeature.numComplexDescription(pullRequest));
        System.out.println("Num of mentions in description : " + DescriptionFeature.getMentionsInDescription(pullRequest).size());
    }
    
    public void printEventFeature() {
        System.out.println();
        System.out.println("Event : ");
        for (Participant pa : pullRequest.getParticipants()) {
            System.out.println("Num of prior interactions : " + EventFeature.numPriorInteractions(pullRequest, pa));
        }
    }
    
    public void printParticipantFeature() {
        System.out.println();
        System.out.println("Participant : ");
        System.out.println("Num of participants : " + ParticipantFeature.numParticipants(pullRequest));
        System.out.println("Social distance exists : " + ParticipantFeature.socialDistanceExists(pullRequest));
        System.out.println("Author : "                 + ParticipantFeature.getAuthor(pullRequest));
        System.out.println("Num of reviewers : "       + ParticipantFeature.getReviewers(pullRequest).size());
    }
    
    public void printTimeFeature() {
        System.out.println();
        System.out.println("TimeR : ");
        System.out.println("Milli seconds for lifetime: "                       + TimeFeature.lifetime_ms(pullRequest));
        System.out.println("Milli seconds for mergetime : "                     + TimeFeature.mergetime_ms(pullRequest));
        System.out.println("Milli seconds for first issue comment response : "  + TimeFeature.firstIssueCommentResponse_ms(pullRequest));
        System.out.println("Milli seconds for first review comment response : " + TimeFeature.firstReviewCommentResponse_ms(pullRequest));
        System.out.println("Milli seconds for First iisue event response : "    + TimeFeature.firstIssueEventResponse_ms(pullRequest));
        System.out.println("Milli seconds for first review event response : "   + TimeFeature.firstReviewEventResponse_ms(pullRequest));
        System.out.println("Milli seconds for total CI latency : "              + TimeFeature.totalCILatency_ms(pullRequest));
    }
}
