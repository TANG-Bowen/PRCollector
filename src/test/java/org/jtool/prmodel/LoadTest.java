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
        
//        String filePath2 = "/Users/tangbowen/PRDataset-te/PRCollector/spring-boot";
//        PRModelLoader loader2 = new PRModelLoader(filePath2);
//        
//        PRModel prmodel2 = loader2.load();
//        Set<PullRequest> pullRequests2 = prmodel2.getPullRequests();
//        if (pullRequests2.size() == 1) {
//            PullRequest pr2 = pullRequests2.iterator().next();
//            System.out.println(" PR : " + pr2.getTitle() + "   createDate : " + pr2.getCreateDate());
//            pr2.print();
//        }
    }
}
