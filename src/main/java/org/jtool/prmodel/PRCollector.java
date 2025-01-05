package org.jtool.prmodel;

import java.io.File;

import com.moandjiezana.toml.Toml;

public class PRCollector {
    
    private static final String TOML_FILENAME = "./prcollector-config.toml";
    
    public static void main(String[] args) {
        String filename;
        if (args.length == 1) {
            if (args[0].equals("-h")) {
                System.out.println("Usage: java -cp PRCollector-0.0.1.jar " +
                                   "org.jtool.prmodel.PRCollector [toml_file]");
                return;
            } else {
                filename = args[0];
            }
        } else {
            filename = TOML_FILENAME;
        }
        
        PRModelBundle bundle = getModelBundle(filename);
        
        if (bundle != null) {
            PRModel prmodel = bundle.build();
            
            prmodel.getPullRequests().forEach(pr -> pr.print());
            prmodel.getDeficientPullRequests().forEach(pr -> pr.print());
            
            prmodel.getPullRequests().forEach(pr -> pr.print());
            prmodel.getDeficientPullRequests().forEach(pr -> pr.print());
        }
    }
    
    private static PRModelBundle getModelBundle(String tomlFilePath) {
        File file = new File(tomlFilePath);
        if (!file.exists() || !file.getName().endsWith(".toml")) {
            System.out.println("Not found configuration file in toml: " + tomlFilePath);
            return null;
        }
        
        Toml toml = new Toml().read(file);
        Config config = toml.to(Config.class);
        
        if (!config.checkEssential()) {
            return null;
        }
        
        PRModelBundle bundle;
        if (config.prNumber > 0) {
            bundle = new PRModelBundle(config.psnToken, config.repoName, config.rootPath, config.prNumber);
        } else if (config.prNumbers != null) {
            bundle = new PRModelBundle(config.psnToken, config.repoName, config.rootPath, config.prNumbers);
        } else {
            bundle = new PRModelBundle(config.psnToken, config.repoName, config.rootPath);
        }
        
        bundle.deleteSourceFile(config.deleteSrcFile);
        bundle.writeErrorLog(config.writeErrorLog);
        
        if (config.prNumber > 0 || config.prNumbers != null) {
            return bundle;
        }
        
        if (config.searchByWhen != null) {
            if (config.searchByWhen.equals("Created") && config.checkTime()) {
                bundle.searchByCreated(config.fromDate, config.toDate);
            } else if (config.searchByWhen.equals("Closed") && config.checkTime()) {
                bundle.searchByClosed(config.fromDate, config.toDate);
            } else if (config.searchByWhen.equals("Merged") && config.checkTime()) {
                bundle.searchByMerged(config.fromDate, config.toDate);
            } else if (config.searchByWhen.equals("Updated") && config.checkTime()) {
                bundle.searchByUpdated(config.fromDate, config.toDate);
            }
        }
        
        int changedFilesMin = 0;
        int changedFilesMax = Integer.MAX_VALUE;
        if (config.minNumOfChangedFiles > 0) {
            changedFilesMin = config.minNumOfChangedFiles;
        }
        if (config.maxNumOfChangedFiles > 0) {
            changedFilesMax = config.maxNumOfChangedFiles;
        }
        bundle.downloadChangedFileNum(changedFilesMin, changedFilesMax);
        
        int commitsMin = 0;
        int commitsMax = Integer.MAX_VALUE;
        if (config.minNumOfCommits > 0) {
            commitsMin = config.minNumOfCommits;
        }
        if (config.maxNumOfCommits > 0) {
            commitsMax = config.maxNumOfCommits;
        }
        bundle.downloadCommitNum(commitsMin, commitsMax);
        
        if (config.searchByIsOpen) {
            bundle.searchByIsOpen();
        }
        if (config.searchByIsClosed) {
            bundle.searchByIsClosed();
        }
        if (config.searchByIsDraft) {
            bundle.searchByIsDraft();
        }
        if (config.searchByIsMerged) {
            bundle.searchByIsMerged();
        }
        
        if (config.searchByAssignedUser != null) {
            bundle.searchByAssignedUser(config.searchByAssignedUser);
        }
        if (config.searchByAuthor != null) {
            bundle.searchByAuthor(config.searchByAuthor);
        }
        if (config.searchByBaseBranch != null) {
            bundle.searchByBaseBranch(config.searchByBaseBranch);
        }
        if (config.searchByHeadBranch != null) {
            bundle.searchByHeadBranch(config.searchByHeadBranch);
        }
        
        return bundle;
    }
    
    class Config {
        String psnToken;
        String repoName;
        String rootPath;
        
        int prNumber;
        int[] prNumbers;
        
        String fromDate;
        String toDate;
        String searchByWhen;
        
        int minNumOfChangedFiles;
        int maxNumOfChangedFiles;
        int minNumOfCommits;
        int maxNumOfCommits;
        
        boolean searchByIsOpen;
        boolean searchByIsClosed;
        boolean searchByIsDraft;
        boolean searchByIsMerged;
        
        String searchByAssignedUser;
        String searchByAuthor;
        String searchByBaseBranch;
        String searchByHeadBranch;
        
        boolean deleteSrcFile;
        boolean writeErrorLog;
        
        boolean checkEssential() {
            boolean result = psnToken != null && repoName != null && rootPath != null;
            if (!result) {
                System.out.println("Must set psnToken, repoName, and rootPath");
            }
            return result;
        }
        
        boolean checkTime() {
            boolean result = fromDate != null && toDate != null;
            if (!result) {
                System.out.println("Must set fromDate and toDate when using searchByWhen");
            }
            return result;
        }
    }
}