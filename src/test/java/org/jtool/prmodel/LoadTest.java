package org.jtool.prmodel;

import java.util.Set;

public class LoadTest {
    
    public static void main(String[] args) {
        String filePath = args[0]; // Path where data file is stored
        
        test(filePath);
        testSingle(filePath);
    }
    
    public static void test(String filePath) {
        PRModelLoader loader = new PRModelLoader(filePath);
        PRModel prmodel = loader.load();
        Set<PullRequest> pullRequests = prmodel.getPullRequests();
        
        System.out.println("Output head : ");
        for (PullRequest pr : pullRequests) {
            System.out.println(" PR : " + pr.getTitle()+"   createDate : " + pr.getCreateDate());
        }
        System.out.println("total " + pullRequests.size() + " prs");
    }
    
    private static void testSingle(String filePath) {
        PRModelLoader loader = new PRModelLoader(filePath);
        PRModel prmodel = loader.load();
        
        System.out.println("Output head : ");
        for (PullRequest pr : prmodel.getPullRequests()) {
            if (pr.getId().equals("presto#18407")) {
                pr.print();
            }
        }
        
        for (DeficientPullRequest pr : prmodel.getDeficientPullRequests()) {
            if( pr.getId().equals("presto#18407")) {
                pr.print();
            }
        }
    }
}
