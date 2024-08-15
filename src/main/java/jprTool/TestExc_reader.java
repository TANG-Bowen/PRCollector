package jprTool;

import java.util.HashSet;

import QuickAccessAPI.QkaApi;
import prmodel.PullRequest;

public class TestExc_reader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PullRequest pr1 = new PullRequest();
		HashSet<PullRequest> prs = new HashSet<>();
		JsonFileReader jfreader = new JsonFileReader("/Users/tangbowen/PRDataset-re/PRCollector/dubbo");
		prs = jfreader.getPRs();
		 System.out.println("Output head : ");
        if(!prs.isEmpty())
        {
        	
        	for(PullRequest pri : prs)
        	{
        		System.out.println(" PR : "+pri.getTitle()+"   createDate : "+pri.getCreateDate());
        		
        	}
        }
        System.out.println("total "+prs.size()+" prs");
        
        
//        JsonFileReader jfreader1 = new JsonFileReader("/Users/tangbowen/PRDataset-re/PRCollector/dubbo/13351/dubbo_13351_str.json");
//        pr1 = jfreader1.getPR();
//        System.out.println(" PR : "+pr1.getTitle()+"   createDate : "+pr1.getCreateDate());
//        pr1.printPR();
	}

}
