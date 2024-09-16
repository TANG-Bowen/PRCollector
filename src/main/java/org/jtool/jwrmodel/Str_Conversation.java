package org.jtool.jwrmodel;

import java.util.LinkedHashSet;

public class Str_Conversation {
    
    String prmodelId;
    
    LinkedHashSet<Str_Comment> comments;
    LinkedHashSet<Str_ReviewComment> reviewComments;
    LinkedHashSet<Str_Event> events;
    LinkedHashSet<Str_Review> reviews;
    LinkedHashSet<Str_CodeReviewSnippet> codeReviews;
    
    LinkedHashSet<String> timeLineIds;
}
