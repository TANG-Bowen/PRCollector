package org.jtool.prmodel;

import java.util.Set;

public class LoadSingleTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filePath = "/Users/tangbowen/PRDataset-te/PRCollector/arrow";
        PRModelLoader loader = new PRModelLoader(filePath);
        
        PRModel prmodel = loader.load();
        Set<PullRequest> pullRequests = prmodel.getPullRequests();
        
        System.out.println("Output head : ");
        for (PullRequest pr : pullRequests) {
            if(pr.getId().equals("arrow#12640"))
            {
            	pr.print();
            }
        }
        Set<DataLoss> dataLosses = prmodel.getDataLosses();
        for(DataLoss dl : dataLosses)
        {
        	dl.print();
        }
       

	}

}
