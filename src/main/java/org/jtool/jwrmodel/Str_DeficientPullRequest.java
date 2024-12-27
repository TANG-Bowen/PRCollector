package org.jtool.jwrmodel;

import java.util.List;
import java.util.Set;

public class Str_DeficientPullRequest {
    
    String prmodelId;
    
    String id;
    String title;
    String repositoryName;
    String state;
    
    String createDate;
    String endDate;
    
    String mergeBranch;
    String headBranch;
    String pageUrl;
    String repositorySrcDLUrl;
    String headRepositorySrcDLUrl;
    
    boolean isMerged;
    boolean isStandardMerged;
    
    List<String> repositoryBranches;
    List<String> headRepositoryBranches;
    
    boolean participantRetrievable;
    boolean commentRetrievable;
    boolean reviewCommentRetrievable;
    boolean eventRetrievable;
    boolean reviewEventRetrievable;
    boolean commitRetrievable;
    boolean sourceCodeRetrievable;
    
    Set<Str_Participant> participants;
    
    Str_Description description;
    Str_HTMLDescription htmlDescription;
    Str_Conversation conversation;
    List<Str_Commit> commits;   
    Str_FilesChanged filesChanged;
    Set<Str_Label> addedLabels;
    Set<Str_Label> removedLabels;
    Set<Str_Label> finalLabels;
    
    String exceptionOutput;
}
