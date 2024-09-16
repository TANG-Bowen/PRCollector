package org.jtool.prmodel;

import org.jtool.prmodel.QuickAccessAPI.*;

public class QuickAccessAPITest {
    
    String testCmtSha1 = "fd16ae78424bfce31439cc0fc67005cf7d14dba3";
    
    QuickAccessAPITest() {
    }
    
    public void print(PullRequest pullRequest) {
        printCommitRelation(pullRequest);
        printConversationRelation(pullRequest);
        printDiscriptionRelation(pullRequest);
        printEventRelation(pullRequest);
        printParticipantRelation(pullRequest);
        printTimeRelation(pullRequest);
        
    }
    
    public void printCommitRelation(PullRequest pullRequest) {
        System.out.println();
        System.out.println("CommitR : ");
        System.out.println("Num_Commits : "           + CommitRelation.num_commits(pullRequest));
        System.out.println("Src_churn : "             + CommitRelation.src_churn(pullRequest));
        System.out.println("Src_churn_commit : "      + CommitRelation.src_churn(pullRequest, testCmtSha1));
        System.out.println("Test_churn : "            + CommitRelation.test_churn(pullRequest));
        System.out.println("Test_churn_commit : "     + CommitRelation.test_churn(pullRequest, testCmtSha1));
        System.out.println("Files_added : "           + CommitRelation.files_added(pullRequest));
        System.out.println("Files_added_commit : "    + CommitRelation.files_added(pullRequest, testCmtSha1));
        System.out.println("Files_delete : "          + CommitRelation.files_deleted(pullRequest));
        System.out.println("Files_delete_commit : "   + CommitRelation.files_deleted(pullRequest, testCmtSha1));
        System.out.println("Files_modified : "        + CommitRelation.files_modified(pullRequest));
        System.out.println("Files_modified_commit : " + CommitRelation.files_modified(pullRequest, testCmtSha1));
        System.out.println("Files_changed : "         + CommitRelation.files_changed(pullRequest));
        System.out.println("Files_changed_commit : "  + CommitRelation.files_changed(pullRequest, testCmtSha1));
        System.out.println("Src_files : "             + CommitRelation.src_files(pullRequest));
        System.out.println("Src_files_commit : "      + CommitRelation.src_files(pullRequest, testCmtSha1));
        System.out.println("Doc_files : "             + CommitRelation.doc_files(pullRequest));
        System.out.println("Doc_files_commit : "      + CommitRelation.doc_files(pullRequest, testCmtSha1));
        System.out.println("Other_files : "           + CommitRelation.other_files(pullRequest));
        System.out.println("Other_files_commit : "    + CommitRelation.other_files(pullRequest, testCmtSha1));
        System.out.println("Test_inclusion : "        + CommitRelation.test_inclusion(pullRequest));
        System.out.println("Test_inclusion_commit : " + CommitRelation.test_inclusion(pullRequest, testCmtSha1));
        System.out.println("CI_failures : "           + CommitRelation.ci_failures(pullRequest));                
    }
    
    public void printConversationRelation(PullRequest pullRequest) {
        System.out.println();
        System.out.println("ConversationR : ");
        System.out.println("Num_commit_comment : "         + ConversationRelation.num_commit_comments(pullRequest));
        System.out.println("Num_issue_comment : "          + ConversationRelation.num_issue_comments(pullRequest));
        System.out.println("Num_comment : "                + ConversationRelation.num_comments(pullRequest));
        System.out.println("CommentsHaveMentions : "       + ConversationRelation.commentsHaveMentions(pullRequest).size());
        System.out.println("ReviewCommentsHaveMentions : " + ConversationRelation.reviewCommentsHaveMentions(pullRequest).size());
        System.out.println("ReviewEventsHaveMentions : "   + ConversationRelation.reviewEventsHaveMentions(pullRequest).size());
        
    }
    
    public void printDiscriptionRelation(PullRequest pullRequest) {
        System.out.println();
        System.out.println("DiscriptionR : ");
        System.out.println("Complexity_title : "       + DescriptionRelation.complexity_title(pullRequest));
        System.out.println("Complexity_description : " + DescriptionRelation.complexity_description(pullRequest));
        System.out.println("MentionInDescription : "   + DescriptionRelation.mentionInDescription(pullRequest).size());
    }
    
    public void printEventRelation(PullRequest pullRequest) {
        System.out.println();
        System.out.println("EventR : ");
        for(Participant pa : pullRequest.getParticipants()) {
            System.out.println("Prior_interaction : " + EventRelation.prior_interaction(pullRequest, pa));
        }
    }
    
    public void printParticipantRelation(PullRequest pullRequest) {
        System.out.println();
        System.out.println("ParticipantR : ");
        System.out.println("Num_participants : " + ParticipantRelation.num_participants(pullRequest));
        System.out.println("SocialDistance : "   + ParticipantRelation.social_distance(pullRequest));
    }
    
    public void printTimeRelation(PullRequest pullRequest) {
        System.out.println();
        System.out.println("TimeR : ");
        System.out.println("Lifetime_minites : "            + TimeRelation.lifetime_msec(pullRequest));
        System.out.println("Mergetime_minites : "           + TimeRelation.mergetime_msec(pullRequest));
        System.out.println("FirstComment_response : "       + TimeRelation.firstComment_response(pullRequest));
        System.out.println("FirstReviewComment_response : " + TimeRelation.firstReviewComment_response(pullRequest));
        System.out.println("FirstEvent_response : "         + TimeRelation.firstEvent_response(pullRequest));
        System.out.println("FirstReviewEvent_response : "   + TimeRelation.firstReview_response(pullRequest));
        System.out.println("TotalCI_latency : "             + TimeRelation.total_ci_latency(pullRequest));
    }
}
