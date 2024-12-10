package org.jtool.prmodel;

public class BuildTest {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        String repoName_boot = "spring-projects/spring-boot";
        String repoName_gravitino = "apache/gravitino";
        String repoName_kafka = "apache/kafka";
        String repoName_dubbo = "apache/dubbo";
<<<<<<< HEAD
               
        String psnToken_ac_0109 = "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";
        
        String filePath = "/Users/tangbowen/PRDataset-te";
        
        String fromDate = "2024-11-08";
        String toDate = "2024-11-11"; //2024-01-01 
        
        PRModelBundle bundle = new PRModelBundle(psnToken_ac_0109, repoName_boot, filePath);
=======
        
        String psnToken_0806 = "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";
        String psnToken_0930 = "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";
        String psnToken_0930_2="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";
        String psnToken_tf_1004="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";
        
        String filePath = "/Users/touhakubun/PRDataset-re";
        
        String fromDate = "2022-01-01";
        String toDate = "2022-11-23"; //2024-01-01 
        
        PRModelBundle bundle = new PRModelBundle(psnToken_tf_1004, repoName_dubbo, filePath);
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
        
        bundle.searchByCreated(fromDate, toDate);
        bundle.searchByIsClosed();
        bundle.downloadChangedFileNum(0, 40);
        bundle.downloadCommitNum(0, 20);
        bundle.writeFile(true);
        bundle.deleteSourceFile(true);
        
        bundle.build();
        
        System.out.println("Collection was over");
    }
}
