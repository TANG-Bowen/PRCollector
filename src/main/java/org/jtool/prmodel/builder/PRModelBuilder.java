package org.jtool.prmodel.builder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import org.jtool.prmodel.Label;
import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.PRModelBundle;

public class PRModelBuilder {
    
    private final String repositoryName;
    private final String rootSrcPath;
    
    private final String ghToken;
    private final int pullRequestNumber;
    
    private final int changedFilesMin;
    private final int changedFilesMax;
    private final int commitMin;
    private final int commitMax;
    private final List<String> bannedLabels;
    
    private final boolean writeErrorLog;
    
    private PullRequest pullRequest;
    private File pullRequestDir;
    
    public PRModelBuilder(PRModelBundle bundle, String ghToken, int pullRequestNumber, File pullRequestDir) {
        this.repositoryName = bundle.getRepositoryName();
        this.rootSrcPath = bundle.getRootSrcPath();
        this.ghToken = ghToken;
        this.pullRequestNumber = pullRequestNumber;
        this.pullRequestDir = pullRequestDir;
        
        changedFilesMin = bundle.getDownloadChangedFilesNumMin();
        changedFilesMax = bundle.getDownloadChangedFilesNumMax();
        commitMin = bundle.getDownloadCommitsNumMin();
        commitMax = bundle.getDownloadCommitsNumMax();
        bannedLabels = bundle.getBannedLabels();
        
        writeErrorLog = bundle.writeErrorLog();
    }
    
    public PullRequest getPullRequest() {
        return pullRequest;
    }
    
    public boolean build() {
        GitHub github = null;
        GHRepository repository = null;
        GHPullRequest ghPullRequest = null;
        
        try {
            github = new GitHubBuilder().withOAuthToken(ghToken).build();
            repository = github.getRepository(repositoryName);
            ghPullRequest = repository.getPullRequest(pullRequestNumber);
        } catch (IOException e) {
            return false;
        }
        
        try {
            if (!alreadyBuilt(repository, ghPullRequest)) {
                boolean downloadingChangedFilesOk = checkDownloadingChangedFiles(ghPullRequest);
                if (!downloadingChangedFilesOk) {
                    return false;
                }
                
                boolean downloadinCommitOk = checkDownloadingCommits(ghPullRequest);
                if (!downloadinCommitOk) {
                    return false;
                }
                
                PullRequestBuilder pullRequestBuilder = new PullRequestBuilder(ghPullRequest);
                pullRequest = pullRequestBuilder.build();
                System.out.println("Built PullRequest element");
                
                ParticipantBuilder participantBuilder = new ParticipantBuilder(pullRequest, ghPullRequest);
                participantBuilder.build();
                System.out.println("built Participant elements");
                
                ConversationBuilder conversationBuilder = new ConversationBuilder(pullRequest, ghPullRequest);
                conversationBuilder.build();
                System.out.println("built Conversation elements");
                
                DescriptionBuilder descriptionBuilder = new DescriptionBuilder(pullRequest, ghPullRequest);
                descriptionBuilder.build();
                System.out.println("Built Description element");
                
                HTMLDescriptionBuilder htmlDescriptionBuilder = new HTMLDescriptionBuilder(pullRequest, ghPullRequest);
                htmlDescriptionBuilder.build();
                System.out.println("Built HTMLDescription element");
                
                MarkdownDocBuilder markdownDocBuilder = new MarkdownDocBuilder(pullRequest, github);
                markdownDocBuilder.build();
                System.out.println("built MarkdownDoc element");
                
                LabelBuilder labelBuilder = new LabelBuilder(pullRequest, ghPullRequest, repository);
                labelBuilder.build(conversationBuilder.eventMap);
                System.out.println("built Label elements");
                
                CommitBuilder commitBuilder = new CommitBuilder(pullRequest, ghPullRequest);
                commitBuilder.build();
                System.out.println("built Commit elements");
                
                if (pullRequest.isSourceCodeRetrievable()) {
                    boolean noBannedLabel = checkBannedLabels(pullRequest);
                    if (noBannedLabel) {
                        
                        DiffBuilder diffBuilder = new DiffBuilder(pullRequest, pullRequestDir);
                        diffBuilder.build();
                        System.out.println("built Diff element");
                        
                        CodeChangetBuilder codeChangetBuilder = new CodeChangetBuilder(pullRequest);
                        codeChangetBuilder.build();
                        System.out.println("built CodeChange elements");
                        
                        diffBuilder.setTestForDiffFiles();
                        
                        FilesChangedInfoBuilder filesChangedInfoBuilder =
                                new FilesChangedInfoBuilder(pullRequest, ghPullRequest, repository);
                        filesChangedInfoBuilder.build();
                        System.out.println("built FilesChangedInfo element");
                    }
                }
                return true;
            } else {
                System.out.println("Already built : " +
                        repository.getName() + "  ---  " + ghPullRequest.getNumber());
                return false;
            }
        } catch (IOException | CommitMissingException e) {
            recordException(e, repository, ghPullRequest);
            return false;
        }
    }
    
    private boolean checkDownloadingChangedFiles(GHPullRequest ghPullRequest) throws IOException {
        int changedFilesSize = ghPullRequest.listFiles().toList().size();
        return changedFilesMin <= changedFilesSize && changedFilesSize <= changedFilesMax;
    }
    
    private boolean checkDownloadingCommits(GHPullRequest ghPullRequest) throws IOException {
        int commitSize = ghPullRequest.listCommits().toList().size();
        return commitMin <= commitSize && commitSize <= commitMax;
    }
    
    private boolean checkBannedLabels(PullRequest pullRequest) {
        for (String bannedlabel : bannedLabels) {
            for (Label label : pullRequest.getFinalLabels()) {
                if (bannedlabel.equals(label.getName())) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean alreadyBuilt(GHRepository repository, GHPullRequest ghPullRequest) {
        String repeatedPath = rootSrcPath + File.separator + "PRCollector" +
                File.separator + repository.getName() + File.separator + ghPullRequest.getNumber();
        File f = new File(repeatedPath);
        return f.exists();
    }
    
    private void recordException(Exception ex, GHRepository repository, GHPullRequest ghPullRequest) {
        if (writeErrorLog) {
            String filePath = rootSrcPath + File.separator + "PRCollector" +
                    File.separator + repository.getName() + File.separator + "error.txt";
            File file = new File(filePath);
            System.out.println("Write log to " + file.getAbsolutePath());
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                ex.printStackTrace(writer);
                writer.println("------------------------------------------------------" +
                        repository.getName()+" : " + ghPullRequest.getNumber());
                writer.flush();
            } catch (IOException e) {
                System.err.println("Failed to write log");
            }
            
            System.out.println("Wrote error log");
        } else {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
