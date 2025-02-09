/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.jwrmodel;

import java.util.LinkedHashSet;

public class Str_Conversation {
    
    String prmodelId;
    
    LinkedHashSet<Str_IssueComment> issueComments;
    LinkedHashSet<Str_ReviewComment> reviewComments;
    LinkedHashSet<Str_CodeReviewSnippet> codeReviews;
    LinkedHashSet<Str_IssueEvent> issueEvents;
    LinkedHashSet<Str_ReviewEvent> reviewEvents;
    LinkedHashSet<String> timeLineIds;
}
