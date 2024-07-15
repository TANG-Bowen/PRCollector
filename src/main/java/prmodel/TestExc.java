package prmodel;

import java.io.IOException;
import jprTool.JsonFileWriter;

public class TestExc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String psntoken ="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";
		String repoName_boot ="spring-projects/spring-boot";

		
		PRModelBuilder mdlBuilder = new PRModelBuilder(psntoken, repoName_boot, 38847);
		
		
		try {
			mdlBuilder.setRootSrcPath("/Users/tangbowen");
			mdlBuilder.writeFile(true);
			mdlBuilder.deleteSrcFile(true);
			mdlBuilder.build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PullRequest pr = mdlBuilder.getPr();
		//pr.printPR();
//		JsonFileWriter jfWriter = new JsonFileWriter(mdlBuilder);
//        jfWriter.makeFile();
//        jfWriter.deleteGitSrc();
        
//        PRModelBuilderBundle mdlBuilderBundle  = new PRModelBuilderBundle(psntoken, repoName_boot);
//        mdlBuilderBundle.setRootSrcPath("/Users/tangbowen");
//        mdlBuilderBundle.searchByAuthor("snicoll");
//        mdlBuilderBundle.searchByIsClosed();
//        mdlBuilderBundle.searchByCreated("2024-04-01", "2024-05-20");
//        mdlBuilderBundle.writeFile(true);
//        mdlBuilderBundle.deleteSrcFile(true);
//        mdlBuilderBundle.freeMemory(true);
//        mdlBuilderBundle.build();
//        JsonFileWriter jfw = new JsonFileWriter(mdlBuilderBundle);
//        jfw.makeFiles();
//        jfw.deleteGitSrc();
//        if(!mdlBuilderBundle.getPrmdlBuilders().isEmpty())
//        {
//        	for(PRModelBuilder mdlbi : mdlBuilderBundle.getPrmdlBuilders())
//        	{
//        		JsonFileWriter jfWriteri = new JsonFileWriter(mdlbi);
//        		jfWriteri.makeFile();
//        		jfWriteri.deleteGitSrc();
//        	}
//        }else {
//        	System.out.println("No pr select");
//        }
	}

}
