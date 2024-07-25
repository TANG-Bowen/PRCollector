package prmodel;

public class CollectMisson {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String repoName_boot="spring-projects/spring-boot";
		String repoName_gravitino="apache/gravitino";
		String repoName_kafka="apache/kafka";
		String repoName_dubbo="apache/dubbo";
		String psnToken_0806="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";
		String psnToken_0930="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";
		String psnToken_0930_2="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";
		String psnToken_tf_1004="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";
		String filePath="/Users/touhakubun/PRDataset-re";
		String fromDate="2022-01-01";
		String toDate="2022-11-23";//2024-01-01
		
		PRModelBuilderBundle mdlBuilderBundle  = new PRModelBuilderBundle(psnToken_tf_1004, repoName_dubbo);
		mdlBuilderBundle.setRootSrcPath(filePath);
		mdlBuilderBundle.searchByCreated(fromDate, toDate);
		mdlBuilderBundle.searchByIsClosed();
		mdlBuilderBundle.downloadChangedFiles(0, 40);
		mdlBuilderBundle.downloadCommitNum(0, 20);
		mdlBuilderBundle.writeFile(true);
		mdlBuilderBundle.deleteSrcFile(true);
		mdlBuilderBundle.freeMemory(true);
		mdlBuilderBundle.build();
		
		System.out.println("Collect mission is over");
	}

}
