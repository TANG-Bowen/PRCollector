# PRCollector
A tool platform for pull request collection on GitHub with a simple model. 

Fetch data :  
* Attributes relative a pull request(Basic information, review Comment, file diff...).
* The markdown elements edited by participants in the PR's conversation.
* The modified code elements parsed by AST parser(in current version, only supporting Java code).

Product :
* A generated Json file to record the PR model.
* File directories which help build a simple dataset for collected PRs.

Additional tools : 
* A Json file reader helps to restore a Json file to Java instances as a PR model.
* Quick Access API provides a fast access to those information which has been proved to be useful to acceptence of a PR

## Requirement
* JDK 11

## Installation
The jar file will be builded under the path of ~/PRCollector/target

## Usage
Before starting a collection of PRs on GitHub, a personal access token is needed.

A collection mission can be described as follows: 
```java
    String psnToken ="your token";
    String repoName ="author/repository name";
    String filePath ="/Users/yourAimPath";//a path of a directory which you want to set the root path of dataset
    String fromDate ="2022-01-01";
    String toDate ="2024-01-01";

    PRModelBundle bundle = new PRModelBundle(psnToken, repoName, filePath);
    bundle.searchByCreated(fromDate, toDate);
    bundle.searchByIsClosed();
    bundle.downloadChangedFileNum(0 , 40);//set upper limit and lower limit for the number of changed files
    bundle.downloadCommitNum(0 , 20);//set upper limit and lower limit for the number of commits
    bundle.writeFile(true);//set whether output Json files or not
    bundle.deleteSrcFile(true);//set whether delete the source files after the collection or not
    bundle.build();
```

 The tool platform also provides a Json file reader to return the PR model to Java instance :
 ```java
    String path = "/Users/yourAimPath";// the path of root directory which may be same as the set root directory of built dataset
    PRModelLoader loader = new PRModelLoader(path);
    PRModel prmodel = loader.load();
    Set<PullRequest> prs = promodel.getPullRequests();
    System.out.println("Output head: ");
    for(PullRequest pri : prs){
       System.out.println("PR :" + pri.getTitle() + "  createDate: " + pr.getCreateDate());
    }
    System.out.println("total "+pullRequest.size() + " prs");
    
      
 ```

 There are APIs provided by the tool platform, that can access the useful attributes for acceptence of the pull request. A sample for getting social distance(Whether the contributor follows the user who closes the PR) as follows : 
  ```java
       for(PullRequest pri : prs){
           System.out.println("PR " + pri.getId() + "  " + pri.getTitle() + "  Social_instance with platform : " + ParticipantRelevance.socialDistanceExists(pri));
       }     
  ```
