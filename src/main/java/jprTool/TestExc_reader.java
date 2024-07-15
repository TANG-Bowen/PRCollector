package jprTool;

import QuickAccessAPI.QkaApi;
import prmodel.PullRequest;

public class TestExc_reader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PullRequest pr1 = new PullRequest();
		JsonFileReader jfreader = new JsonFileReader("/Users/tangbowen/JPrTest/spring-boot/39813/spring-boot_39813_str.json");
		pr1 = jfreader.getPR();
    	//pr1.printPR();
		QkaApi qka = new QkaApi(pr1);
		TestForQkaa tqk = new TestForQkaa(qka);
		tqk.printTest();
//    	JsonFileReader jfreader1 = new JsonFileReader("/Users/tangbowen/JPrTest/spring-boot");
//    	if(!jfreader1.getPRs().isEmpty())
//    	{
//    		int counter=0;
//    		for(PullRequest pri : jfreader1.getPRs())
//    		{
//    			System.out.println();
//    			System.out.println("PRI Number : "+pri.getId());
//    			System.out.println("PRI Title : "+pri.getTitle());
//    			System.out.println("PRI CreateDate : "+pri.getCreateDate());
//    			//System.out.println("PRI Author : ");
//    			for(PRLabel prlbi : pri.getFinalLabels())
//    			{
//    				System.out.println(" PRI Label : "+prlbi.getName());
//    			}
//    			QkaApi qaa = new QkaApi(pri);
//    			TestForQkaa tfq = new TestForQkaa(qaa);
//    	        tfq.printTest();
//    			counter++;
//    		}
//    		System.out.println("total pr num is "+counter);
//    	}
    	
	}

}
