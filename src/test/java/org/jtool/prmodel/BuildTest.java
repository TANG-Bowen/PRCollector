package org.jtool.prmodel;

public class BuildTest {
    
    static String repoName_springboot = "spring-projects/spring-boot";
    static String repoName_cassandra = "apache/cassandra";
    static String repoName_presto = "prestodb/presto";
    static String repoName_dubbo = "apache/dubbo";
    static String repoName_kafka = "apache/kafka";
    static String repoName_arrow = "apache/arrow";
    
    public static void main(String[] args) {
        if (args.length >= 2) {
            String ghToken = args[0];  // GitHub token
            String filePath = args[1]; // Path where data file will be stored
            
            if (args.length == 2) {
                test1(ghToken, filePath);
                test2(ghToken, filePath);
                
                testSingle1(ghToken, filePath);
                testSingle2(ghToken, filePath);
                testSingle3(ghToken, filePath);
            } else if (args.length == 4) {
                testSingle(ghToken, filePath, args[2], Integer.parseInt(args[3]));
            }
        } else {
            System.out.println("Parameters: ghToken filePath [ repositoryName number ]");
        }
    }
    
    private static void testSingle(String ghToken, String filePath, String repositoryName, int number) {
        PRModelBundle bundle = new PRModelBundle(ghToken, repositoryName, filePath, number);
        
        bundle.writeFile(true);
        bundle.deleteSourceFile(true);
        bundle.downloadChangedFileNum(0, 40);
        bundle.downloadCommitNum(0, 20);
        bundle.setWriteErrorLog(true);
        
        PRModel prmodel = bundle.build();
        
        System.out.println("Finish");
        
        prmodel.getPullRequests().forEach(pr -> pr.print());
        prmodel.getDeficientPullRequests().forEach(pr -> pr.print());
    }
    
    private static void test1(String ghToken, String filePath) {
        PRModelBundle bundle = new PRModelBundle(ghToken, repoName_springboot, filePath);
        
        String fromDate = "2024-11-08";
        String toDate = "2024-11-11"; //2024-01-01 
        bundle.searchByCreated(fromDate, toDate);
        bundle.searchByIsClosed();
        bundle.downloadChangedFileNum(0, 40);
        bundle.downloadCommitNum(0, 20);
        bundle.writeFile(true);
        bundle.deleteSourceFile(true);
        
        PRModel prmodel = bundle.build();
        
        prmodel.getPullRequests().forEach(pr -> pr.print());
        prmodel.getDeficientPullRequests().forEach(pr -> pr.print());
        
        System.out.println("Finish");
        
        prmodel.getPullRequests().forEach(pr -> pr.print());
        prmodel.getDeficientPullRequests().forEach(pr -> pr.print());
    }
    
    private static void test2(String ghToken, String filePath) {
        PRModelBundle bundle = new PRModelBundle(ghToken, repoName_springboot, filePath);
        
        bundle.searchByAuthor("snicoll");
        bundle.searchByIsClosed();
        bundle.searchByCreated("2024-04-01", "2024-05-20");
        bundle.writeFile(true);
        bundle.deleteSourceFile(true);
        
        PRModel prmodel = bundle.build();
        
        prmodel.getPullRequests().forEach(pr -> pr.print());
        prmodel.getDeficientPullRequests().forEach(pr -> pr.print());
        
        System.out.println("Finish");
        
        prmodel.getPullRequests().forEach(pr -> pr.print());
        prmodel.getDeficientPullRequests().forEach(pr -> pr.print());
    }
    
    private static void testSingle1(String ghToken, String filePath) {
        PRModelBundle bundle = new PRModelBundle(ghToken, repoName_springboot, filePath, 40393);
        
        bundle.writeFile(true);
        bundle.deleteSourceFile(true);
        bundle.downloadChangedFileNum(0, 40);
        bundle.downloadCommitNum(0, 20);
        bundle.setWriteErrorLog(true);
        
        PRModel prmodel = bundle.build();
        
        System.out.println("Finish");
        
        prmodel.getPullRequests().forEach(pr -> pr.print());
        prmodel.getDeficientPullRequests().forEach(pr -> pr.print());
    }
    
    private static void testSingle2(String ghToken, String filePath) {
        PRModelBundle bundle = new PRModelBundle(ghToken, repoName_cassandra, filePath, 3008);
        // PRModelBundle bundle = new PRModelBundle(ghToken, repoName_dubbo, filePath, 9846);
        
        bundle.writeFile(true);
        bundle.deleteSourceFile(true);
        bundle.downloadChangedFileNum(0, 40);
        bundle.downloadCommitNum(0, 20);
        bundle.setWriteErrorLog(true);
        
        PRModel prmodel = bundle.build();
        
        System.out.println("Finish");
        
        prmodel.getPullRequests().forEach(pr -> pr.print());
        prmodel.getDeficientPullRequests().forEach(pr -> pr.print());
    }
    
    private static void testSingle3(String ghToken, String filePath) {
        PRModelBundle bundle = new PRModelBundle(ghToken, repoName_dubbo, filePath, 9846);
        
        bundle.writeFile(true);
        bundle.deleteSourceFile(true);
        bundle.downloadChangedFileNum(0, 40);
        bundle.downloadCommitNum(0, 20);
        bundle.setWriteErrorLog(true);
        
        PRModel prmodel = bundle.build();
        
        System.out.println("Finish");
        
        prmodel.getPullRequests().forEach(pr -> pr.print());
        prmodel.getDeficientPullRequests().forEach(pr -> pr.print());
    }
}
