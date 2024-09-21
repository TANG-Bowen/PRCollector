package org.jtool.jwrmodel;

import java.util.List;
import java.util.Set;

public class Str_PullRequest {
    
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
    boolean sourceCodeRetrievable;
    
    List<String> repositoryBranches;
    List<String> headRepositoryBranches;
    
    Set<Str_Participant> participants;
    Str_Conversation conversation;
    List<Str_Commit> commits;
    Str_Description description;
    Str_HTMLDescription htmlDescription;
    Str_AllFilesChanged allFilesChanged;
    Set<Str_Label> addedLabels;
    Set<Str_Label> removedLabels;
    Set<Str_Label> finalLabels;
}
