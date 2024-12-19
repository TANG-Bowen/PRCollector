package org.jtool.prmodel;

public class BuildSingleTest {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        String repoName_boot = "spring-projects/spring-boot";
        String repoName_cassandra = "apache/cassandra";
        String repoName_dubbo = "apache/dubbo";
        String repoName_kafka = "apache/kafka";
        String repoName_arrow = "apache/arrow";
       
        String psnToken_ac_0109 = "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";
        
        String filePath = "/Users/tangbowen/PRDataset-te";
        
        PRModelBundle bundle = new PRModelBundle(psnToken_ac_0109, repoName_arrow, filePath, 12640);
        //PRModelBundle bundle = new PRModelBundle(psnToken_tf_1004, repoName_cassandra, filePath, 3008);
        //PRModelBundle bundle = new PRModelBundle(psnToken_tf_1004, repoName_dubbo, filePath, 9846);
        
        bundle.writeFile(true);
        bundle.deleteSourceFile(true);
        bundle.downloadChangedFileNum(0, 40);
        bundle.downloadCommitNum(0, 20);
        bundle.setWriteErrorLog(true);
        
        bundle.build();
        
        System.out.println("Collection was over");
        for(PullRequest pri : bundle.getPullRequests())
        {
        	pri.print();
        }
        
        for(DataLoss dli : bundle.getDataLosses())
        {
        	dli.print();
        }
    }
}
