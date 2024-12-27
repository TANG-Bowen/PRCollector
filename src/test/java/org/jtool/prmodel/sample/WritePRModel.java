package org.jtool.prmodel.sample;

import java.util.Set;

import org.jtool.prmodel.PRModel;
import org.jtool.prmodel.PRModelBundle;
import org.jtool.prmodel.PullRequest;

public class WritePRModel {
    
    private final static String GITHUB_TOKEN = "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";
    private final static String ROOT_SOURCE_PATH = "/Users/tangbowen/PRDataset-sce";
    private final static String REPOSITORY_NAME = "spring-projects/spring-boot";
    
    WritePRModel() {
    }
    
    public static void main(String[] args) {
        WritePRModel writer = new WritePRModel();
        Set<PullRequest> pullRequests = writer.getPRs(GITHUB_TOKEN, REPOSITORY_NAME, ROOT_SOURCE_PATH);
        pullRequests.forEach(pr -> pr.print());
    }
    
    public Set<PullRequest> getPRs(String ghToken, String repositoryName, String rootSrcPath) {
        PRModelBundle bundle = new PRModelBundle(ghToken, repositoryName, rootSrcPath);
        bundle.writeFile(true);
        
        bundle.searchByIsClosed();
        bundle.searchByCreated("2024-04-01", "2024-05-20");
        
        PRModel prmodel = bundle.build();
        return prmodel.getPullRequests();
    }
}
