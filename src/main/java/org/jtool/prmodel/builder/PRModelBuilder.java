package org.jtool.prmodel.builder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import org.jtool.prmodel.DeficientPullRequest;
import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.PRModelBundle;

public class PRModelBuilder {
    
    public static final String UNKNOWN_SYMBOL = "!";
    
    private final String psnToken;
    private final String repoName;
    
    private final int changedFilesMin;
    private final int changedFilesMax;
    private final int commitMin;
    private final int commitMax;
    
    private final boolean writeErrorLog;
    
    private final int pullRequestNumber;
    private final File pullRequestDir;
    
    private PullRequest pullRequest = null;
    private DeficientPullRequest deficientPullRequest = null;
    
    public PRModelBuilder(PRModelBundle bundle, String psnToken, String repoName,
            int pullRequestNumber, File pullRequestDir) {
        this.psnToken = psnToken;
        this.repoName = repoName;
        
        this.changedFilesMin = bundle.getDownloadChangedFilesNumMin();
        this.changedFilesMax = bundle.getDownloadChangedFilesNumMax();
        this.commitMin = bundle.getDownloadCommitsNumMin();
        this.commitMax = bundle.getDownloadCommitsNumMax();
        
        this.writeErrorLog = bundle.writeErrorLog();
        
        this.pullRequestNumber = pullRequestNumber;
        this.pullRequestDir = pullRequestDir;
    }
    
    public PullRequest getPullRequest() {
        return pullRequest;
    }
    
    public DeficientPullRequest getDeficientPullRequest() {
        return deficientPullRequest;
    }
    
    public boolean build() {
        GitHub github = null;
        GHRepository repository = null;
        GHPullRequest ghPullRequest = null;
        
        try {
            github = new GitHubBuilder().withOAuthToken(psnToken).build();
            repository = github.getRepository(repoName);
            ghPullRequest = repository.getPullRequest(pullRequestNumber);
        } catch (IOException e) {
            return false;
        }
        
        try {
            boolean downloadingChangedFilesOk = checkDownloadingChangedFiles(ghPullRequest);
            if (!downloadingChangedFilesOk) {
                return false;
            }
            
            boolean downloadinCommitOk = checkDownloadingCommits(ghPullRequest);
            if (!downloadinCommitOk) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        
        List<Exception> exceptions = new ArrayList<>();
        
        PullRequestBuilder pullRequestBuilder = new PullRequestBuilder(ghPullRequest);
        pullRequest = pullRequestBuilder.build();
        exceptions.addAll(pullRequestBuilder.getExceptions());
        System.out.println("Built PullRequest element " + pullRequest.getId());
        
        ParticipantBuilder participantBuilder = new ParticipantBuilder(pullRequest, ghPullRequest);
        participantBuilder.build();
        exceptions.addAll(participantBuilder.getExceptions());
        System.out.println("Built Participant elements");
        
        ConversationBuilder conversationBuilder = new ConversationBuilder(pullRequest, ghPullRequest);
        conversationBuilder.build();
        exceptions.addAll(conversationBuilder.getExceptions());
        System.out.println("Built Conversation elements");
        
        DescriptionBuilder descriptionBuilder = new DescriptionBuilder(pullRequest, ghPullRequest);
        descriptionBuilder.build();
        System.out.println("Built Description element");
        
        HTMLDescriptionBuilder htmlDescriptionBuilder = new HTMLDescriptionBuilder(pullRequest, ghPullRequest);
        htmlDescriptionBuilder.build();
        exceptions.addAll(htmlDescriptionBuilder.getExceptions());
        System.out.println("Built HTMLDescription element");
        
        MarkdownDocBuilder markdownDocBuilder = new MarkdownDocBuilder(pullRequest, github);
        markdownDocBuilder.build();
        System.out.println("Built MarkdownDoc element");
        
        LabelBuilder labelBuilder = new LabelBuilder(pullRequest, ghPullRequest, repository);
        labelBuilder.build(conversationBuilder.getEventMap());
        exceptions.addAll(labelBuilder.getExceptions());
        System.out.println("Built Label elements");
        
        CommitBuilder commitBuilder = new CommitBuilder(pullRequest, ghPullRequest);
        commitBuilder.build();
        exceptions.addAll(commitBuilder.getExceptions());
        System.out.println("Built Commit elements");
        
        if (pullRequest.isSourceCodeRetrievable()) {
            DiffBuilder diffBuilder = new DiffBuilder(pullRequest, pullRequestDir);
            diffBuilder.build();
            exceptions.addAll(diffBuilder.getExceptions());
            System.out.println("Built Diff element");
            
            CodeChangeBuilder codeChangetBuilder = new CodeChangeBuilder(
                    pullRequest, pullRequestDir);
            codeChangetBuilder.build();
            System.out.println("Built CodeChange elements");
            
            diffBuilder.setTestForDiffFiles();
            
            FilesChangedBuilder filesChangedBuilder = new FilesChangedBuilder(
                    pullRequest, pullRequestDir, ghPullRequest, repository);
            filesChangedBuilder.build();
            System.out.println("Built FilesChanged element");
        }
        
        recordExceptions(exceptions, repository, ghPullRequest);
        
        if (exceptions.isEmpty()) {
            deficientPullRequest = null;
            return true;
        } else {
            deficientPullRequest = new DeficientPullRequest(pullRequest, getExceptionString(exceptions));
            pullRequest = null;
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
    
    private void recordExceptions(List<Exception> exceptions, GHRepository repository, GHPullRequest ghPullRequest) {
        if (writeErrorLog) {
            File file = new File(repoName + File.separator + "error.txt");
            System.out.println("Write log to " + file.getAbsolutePath());
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                for (Exception e : exceptions) {
                    writer.write(e.getMessage());
                    e.printStackTrace(writer);
                    writer.println("------------------------------------------------------" +
                            repository.getName() + " : " + pullRequestNumber);
                    writer.flush();
                }
            } catch (IOException e) {
                System.err.println("Failed to write log");
            }
            System.out.println("Wrote error log");
        } else {
            for (Exception e : exceptions) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    private String getExceptionString(List<Exception> exceptions) {
        StringBuilder buf = new StringBuilder();
        for (Exception e : exceptions) {
            buf.append(e.getMessage());
        }
        return buf.toString();
    }
}
