package jprTool;

import QuickAccessAPI.QkaApi;
import prmodel.Participant;

public class TestForQkaa {
	
	QkaApi qaa;
	String testCmtSha1="fd16ae78424bfce31439cc0fc67005cf7d14dba3";
	TestForQkaa(QkaApi qaa)
	{
		this.qaa = qaa;
	}
	
	void printTest()
	{
		this.printCommitR();
		this.printConversationR();
		this.printDiscriptionR();
		this.printEventR();
		this.printParticipantR();
		this.printTimeR();
		
	}
	
	void printCommitR()
	{
		System.out.println();
		System.out.println("CommitR : ");
		System.out.println("Num_Commits : "+qaa.getCmtr().Num_commits());
		System.out.println("Src_churn : "+qaa.getCmtr().Src_churn());
		System.out.println("Src_churn_commit : "+ qaa.getCmtr().Src_churn(testCmtSha1));
		System.out.println("Test_churn : "+qaa.getCmtr().test_churn());
		System.out.println("Test_churn_commit : "+qaa.getCmtr().test_churn(testCmtSha1));
		System.out.println("Files_added : "+qaa.getCmtr().files_added());
		System.out.println("Files_added_commit : "+qaa.getCmtr().files_added(testCmtSha1));
		System.out.println("Files_delete : "+qaa.getCmtr().files_deleted());
		System.out.println("Files_delete_commit : "+qaa.getCmtr().files_deleted(testCmtSha1));
		System.out.println("Files_modified : "+qaa.getCmtr().files_modified());
		System.out.println("Files_modified_commit : "+qaa.getCmtr().files_modified(testCmtSha1));
		System.out.println("Files_changed : "+qaa.getCmtr().files_changed());
		System.out.println("Files_changed_commit : "+qaa.getCmtr().files_changed(testCmtSha1));
		System.out.println("Src_files : "+qaa.getCmtr().src_files());
		System.out.println("Src_files_commit : "+qaa.getCmtr().src_files(testCmtSha1));
		System.out.println("Doc_files : "+qaa.getCmtr().doc_files());
		System.out.println("Doc_files_commit : "+qaa.getCmtr().doc_files(testCmtSha1));
		System.out.println("Other_files : "+qaa.getCmtr().other_files());
		System.out.println("Other_files_commit : "+qaa.getCmtr().other_files(testCmtSha1));
		System.out.println("Test_inclusion : "+qaa.getCmtr().test_inclusion());
		System.out.println("Test_inclusion_commit : "+qaa.getCmtr().test_inclusion(testCmtSha1));
		System.out.println("CI_failures : "+qaa.getCmtr().CIfailures());				
	}
	
	void printConversationR()
	{
		System.out.println();
		System.out.println("ConversationR : ");
		System.out.println("Num_commit_comment : "+qaa.getConvr().num_commit_comments());
		System.out.println("Num_issue_comment : "+qaa.getConvr().num_issue_comments());
		System.out.println("Num_comment : "+qaa.getConvr().num_comments());
		System.out.println("CommentsHaveMentions : "+qaa.getConvr().commentsHaveMentions().size());
		System.out.println("ReviewCommentsHaveMentions : "+qaa.getConvr().reviewCommentsHaveMentions().size());
		System.out.println("ReviewEventsHaveMentions : "+qaa.getConvr().reviewEventsHaveMentions().size());
		
	}
	
	void printDiscriptionR()
	{
		System.out.println();
		System.out.println("DiscriptionR : ");
		System.out.println("Complexity_title : "+qaa.getDscrpr().complexity_title());
		System.out.println("Complexity_description : "+qaa.getDscrpr().complexity_description());
		System.out.println("MentionInDescription : "+qaa.getDscrpr().mentionInDescription().size());
	}
	
	void printEventR()
	{
		System.out.println();
		System.out.println("EventR : ");
	    for(Participant parti : qaa.getPr().getParticipants())
	    {
	    	System.out.println("Prior_interaction : "+qaa.getEvnr().prior_interaction(parti));
	    }
		
	}
	
	void printParticipantR()
	{
		System.out.println();
		System.out.println("ParticipantR : ");
		System.out.println("Num_participants : "+qaa.getPartr().num_participants());
		System.out.println("SocialDistance : "+qaa.getPartr().social_distance());
	}
	
	void printTimeR()
	{
		System.out.println();
		System.out.println("TimeR : ");
		System.out.println("Lifetime_minites : "+qaa.getTmr().lifetime_msec());
		System.out.println("Mergetime_minites : "+qaa.getTmr().mergetime_msec());
		System.out.println("FirstComment_response : "+qaa.getTmr().firstComment_response());
		System.out.println("FirstReviewComment_response : "+qaa.getTmr().firstReviewComment_response());
		System.out.println("FirstEvent_response : "+qaa.getTmr().firstEvent_response());
		System.out.println("FirstReviewEvent_response : "+qaa.getTmr().firstReviewEvent_response());
		System.out.println("TotalCI_latency : "+qaa.getTmr().totalCI_Latency());
	}

}
