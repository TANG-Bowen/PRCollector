# PRCollector
A tool platform for pull request collection on GitHub with a simple model. 

Fetch data :  
* Attributes relative a pull request(Basic information, ReviewComment, File diff...).
* The markdown elements edited by participants in the PR's discussion.
* The modified code elements parsed by AST parser(in current version, only supporting Java code).

Product :
* A generated Json file to record the PR model.
* File directoriees help build a simple dataset for collected PRs.

Additional tools : 
* A Json file reader helps to restore a Json file to Java instances of the PR model.
* Quick Access API provides a fast access to those information which has been proved to be useful to acceptence of a PR

## Requirement
* JDK 11

## Installation
The jar file is under the path of ~/PRCollector/target

## Usage
Before starting a collection of PRs on GitHub, a personal access token is needed.

A collection mission can be described as follows: 
```java
    String psnToken ="your token";
		String repoName ="author/repository name";
    String filePath ="/Users/yourAimPath";//a path of a directory which you want to set the root path of dataset
    String fromDate ="2022-01-01";
    String toDate ="2024-01-01";

    PRModelBuilderBundle prmbb = new PRModelBuilderBundle(psnToken, repoName);
    prmbb.setRootSrcPath(filePath);
    prmbb.searchByCreated(fromDate, toDate);
    prmbb.searchByIsClosed();
    prmbb.downloadChangedFiles(0 , 40);//set upper and lower limits for the number of changed files
    prmbb.downloadCommitNum(0 , 20);//set upper and lower limits for the number of commits
    prmbb.writeFile(true);//set whether output Json files or not
    prmbb.deleteSrcFile(true);//set whether delete the source files after the collection or not
    prmbb.freeMemory(true);//when the large number of PRs in collection mission, you may need this to free up memory space
    prmbb.build();
```

 The tool platform also provides a Json file reader to return the PR model to Java instance :
 ```java
    String path = "/Users/yourAimPath";// the path of root directory which may be same as the set root directory of built dataset
    ArrayList<PullRequest> prs = new ArrayList<>();
    JsonFileReader jfr = new JsonFileReader(path);
    prs.addAll(jfr.getPRs();
    if(!prs.isEmpty()){
       for(PullRequest pri : prs){
       System.out.println("PR " + pri.getId() + " " + pri.getTitle() + " " + pri.getState() + " " + pri.getCreateDate());
       }
    }
      
 ```

 There are APIs provided by the tool platform, that can access the useful attributes for acceptence of the pull request. A sample for getting social distance(Whether the contributor follows the user who closes the PR) as follows : 
  ```java
    if(!prs.isEmpty()){
       for(PullRequest pri : prs){
           QkaApi qka = new QkaApi(pri);
           System.out.println("PR " + pri.getId() + "  " + pri.getTitle() + "  Social_instance with platform : " + qka.getPartr().social_distance());
       }
    }   
  ```
