package org.jtool.prmodel;

import java.util.Set;

public class GetPRModel {
    
    private final static String GITHUB_TOKEN = "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";
    private final static String ROOT_SOURCE_PATH = "/Users/tangbowen/PRDataset-sce";
    private final static String REPOSITORY_NAME = "spring-projects/spring-boot";
    
    GetPRModel() {
    }
    
    public static void main(String[] args) {
        GetPRModel getPRModel = new GetPRModel();
        Set<PullRequest> pullRequests = getPRModel.getPRs(GITHUB_TOKEN, REPOSITORY_NAME, ROOT_SOURCE_PATH);
        pullRequests.forEach(pr -> pr.print());
    }
    
    public Set<PullRequest> getPRs(String ghToken, String repositoryName, String rootSrcPath) {
        PRModelBundle bundle = new PRModelBundle(ghToken, repositoryName, rootSrcPath);
        bundle.writeFile(true);
        
        bundle.searchByIsClosed();
        bundle.searchByCreated("2024-04-01", "2024-04-03");
        bundle.setWriteErrorLog(true);
        PRModel prmodel = bundle.build();
        return prmodel.getPullRequests();
    }
}
