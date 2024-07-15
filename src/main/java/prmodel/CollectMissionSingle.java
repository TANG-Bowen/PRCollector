package prmodel;

import java.io.IOException;

public class CollectMissionSingle {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String repoName_boot="spring-projects/spring-boot";
		String repoName_cassandra="apache/cassandra";
		String repoName_dubbo="apache/dubbo";
		String repoName_kafka="apache/kafka";
		String psnToken="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";
		String psnToken_tf_1004="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";
		String filePath="/Users/touhakubun/PRDataset-te";
		
		//PRModelBuilder mdlBuilder = new PRModelBuilder(psnToken_tf_1004, repoName_cassandra, 3008);
		PRModelBuilder mdlBuilder = new PRModelBuilder(psnToken_tf_1004, repoName_dubbo, 11542);
		//PRModelBuilder mdlBuilder = new PRModelBuilder(psnToken_tf_1004, repoName_dubbo, 14312);
		mdlBuilder.setRootSrcPath(filePath);
		mdlBuilder.writeFile(true);
		mdlBuilder.deleteSrcFile(true);
		mdlBuilder.downloadFilterChangedFiles(0, 40);
		mdlBuilder.setWriteErrorLogs(false);
		try {
			mdlBuilder.build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
