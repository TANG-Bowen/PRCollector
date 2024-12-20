package org.jtool.prmodel;

import java.util.Set;

public class LoadTest {
    
    public static void main(String[] args) {
        String filePath = "/Users/tangbowen/PRDataset-te/PRCollector/spring-boot";
        PRModelLoader loader = new PRModelLoader(filePath);
        
        PRModel prmodel = loader.load();
        Set<PullRequest> pullRequests = prmodel.getPullRequests();
        
        System.out.println("Output head : ");
        
        for (PullRequest pr : pullRequests) {
            System.out.println(" PR : " + pr.getTitle()+"   createDate : " + pr.getCreateDate());
        }
        System.out.println("total " + pullRequests.size() + " prs");
    }
}
