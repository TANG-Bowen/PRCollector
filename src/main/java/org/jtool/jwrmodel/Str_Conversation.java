package org.jtool.jwrmodel;

import java.util.LinkedHashSet;

public class Str_Conversation {
    
    String prmodelId;
    
    LinkedHashSet<Str_IssueEvent> issueEvents;
    LinkedHashSet<Str_IssueComment> issueComments;
    LinkedHashSet<Str_ReviewEvent> reviewEvents;
    LinkedHashSet<Str_ReviewComment> reviewComments;
    LinkedHashSet<Str_CodeReviewSnippet> codeReviews;
    
    LinkedHashSet<String> timeLineIds;
}
