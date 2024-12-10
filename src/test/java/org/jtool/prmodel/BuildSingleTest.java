package org.jtool.prmodel;

public class BuildSingleTest {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        String repoName_boot = "spring-projects/spring-boot";
        String repoName_cassandra = "apache/cassandra";
        String repoName_dubbo = "apache/dubbo";
        String repoName_kafka = "apache/kafka";
<<<<<<< HEAD
        String repoName_arrow = "apache/arrow";
       
        String psnToken_ac_0109 = "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";
        
        String filePath = "/Users/tangbowen/PRDataset-te";
        
        PRModelBundle bundle = new PRModelBundle(psnToken_ac_0109, repoName_arrow, filePath, 13275);
=======
        
        String psnToken = "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";
        String psnToken_tf_1004 = "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";
        
        String filePath = "/Users/tangbowen/PRDataset-te";
        
        PRModelBundle bundle = new PRModelBundle(psnToken_tf_1004, repoName_boot, filePath, 40617);
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
        //PRModelBundle bundle = new PRModelBundle(psnToken_tf_1004, repoName_cassandra, filePath, 3008);
        //PRModelBundle bundle = new PRModelBundle(psnToken_tf_1004, repoName_dubbo, filePath, 9846);
        
        bundle.writeFile(true);
<<<<<<< HEAD
        bundle.deleteSourceFile(true);
        bundle.downloadChangedFileNum(0, 40);
        bundle.downloadCommitNum(0, 20);
        bundle.setWriteErrorLog(true);
=======
        bundle.deleteSourceFile(false);
        bundle.downloadChangedFileNum(0, 40);
        bundle.downloadCommitNum(0, 20);
        //bundle.setWriteErrorLog(false);
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
        
        bundle.build();
        
        System.out.println("Collection was over");
<<<<<<< HEAD
//        for(PullRequest pri : bundle.getPullRequests())
//        {
//        	pri.print();
//        }
=======
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
    }
}
