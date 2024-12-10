package org.jtool.prmodel;

public class BuildTest {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        String repoName_boot = "spring-projects/spring-boot";
        String repoName_gravitino = "apache/gravitino";
        String repoName_kafka = "apache/kafka";
        String repoName_dubbo = "apache/dubbo";
               
        String psnToken_ac_0109 = "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";
        
        String filePath = "/Users/tangbowen/PRDataset-te";
        
        String fromDate = "2024-11-08";
        String toDate = "2024-11-11"; //2024-01-01 
        
        PRModelBundle bundle = new PRModelBundle(psnToken_ac_0109, repoName_boot, filePath);
        
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
