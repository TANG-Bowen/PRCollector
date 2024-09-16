package org.jtool.prmodel;

public class BuildTest2 {
    
    public static void main(String[] args) {
        String psntoken = "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";
        String repoName_boot = "spring-projects/spring-boot";
        String filePath = "/Users/tangbowen";
        
        PRModelBundle bundle = new PRModelBundle(psntoken, repoName_boot, filePath);
        
        bundle.searchByAuthor("snicoll");
        bundle.searchByIsClosed();
        bundle.searchByCreated("2024-04-01", "2024-05-20");
        bundle.writeFile(true);
        bundle.deleteSourceFile(true);
        
        bundle.build();
        
        System.out.println("Collection was over");
    }
}
