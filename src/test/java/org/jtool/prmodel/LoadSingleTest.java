package org.jtool.prmodel;

public class LoadSingleTest {

    public static void main(String[] args) {
        String filePath = "/Users/tangbowen/PRDataset-dle";
        PRModelLoader loader = new PRModelLoader(filePath);
        
        PRModel prmodel = loader.load();
        
        System.out.println("Output head : ");
        
        for (PullRequest pr : prmodel.getPullRequests()) {
            if (pr.getId().equals("presto#18407")) {
                pr.print();
            }
        }
        
        for (DeficientPullRequest pr : prmodel.getDeficientPullRequests()) {
        	if(pr.getId().equals("presto#18407")) {
            pr.print();
        	}
        }
    }
}
