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
<<<<<<< HEAD
    
    Str_Description description;
    Str_HTMLDescription htmlDescription;
    Str_Conversation conversation;
    List<Str_Commit> commits;   
=======
    Str_Conversation conversation;
    List<Str_Commit> commits;
    Str_Description description;
    Str_HTMLDescription htmlDescription;
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
    Str_FilesChanged filesChanged;
    Set<Str_Label> addedLabels;
    Set<Str_Label> removedLabels;
    Set<Str_Label> finalLabels;
}
