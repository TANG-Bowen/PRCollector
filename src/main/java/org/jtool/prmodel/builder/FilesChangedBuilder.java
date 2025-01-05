package org.jtool.prmodel.builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestCommitDetail;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHRepository;

import org.jtool.prmodel.CodeChange;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.DiffFile;
import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.FilesChanged;
import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.PRModelBundle;
import org.jtool.jxp3model.FileChange;

public class FilesChangedBuilder {
    
    private final PullRequest pullRequest;
    private final File pullRequestDir;
    
    private final GHPullRequest ghPullRequest;
    private final GHRepository repository;
    
    FilesChangedBuilder(PullRequest pullRequest, File pullRequestDir,
            GHPullRequest ghPullRequest, GHRepository repository) {
        this.pullRequest = pullRequest;
        this.ghPullRequest = ghPullRequest;
        this.repository = repository;
        
        this.pullRequestDir = pullRequestDir;
    }
    
    void build() {
        Commit firstCommit;
        Commit lastCommit;
        if (pullRequest.getCommits().size() > 1) {
            List<Commit> commits = pullRequest.getTargetCommits();
            firstCommit = commits.get(0);
            lastCommit = commits.get(commits.size() - 1);
        } else if (pullRequest.getCommits().size() == 1) {
            firstCommit = pullRequest.getCommits().get(0);
            lastCommit = firstCommit;
        } else {
            return;
        }
        
        boolean hasJavaFile = firstCommit.getCodeChange().hasJavaFile() || lastCommit.getCodeChange().hasJavaFile();
        
        FilesChanged filesChanged = new FilesChanged(pullRequest, hasJavaFile);
        pullRequest.setFilesChanged(filesChanged);
        
        String dirNameBefore = PRElement.BEFORE + "_" + firstCommit.getShortSha();
        String pathBefore = pullRequestDir.getAbsolutePath() + File.separator + dirNameBefore;
        String dirNameAfter = PRElement.AFTER + "_" + lastCommit.getShortSha();
        String pathAfter = pullRequestDir.getAbsolutePath() + File.separator + dirNameAfter;
        
        File dirBefore = PRModelBundle.getDir(pathBefore);
        File dirAfter = PRModelBundle.getDir(pathAfter);
        
        try {
            CodeChange codeChange = new CodeChange(pullRequest);
            commandGitDiff(codeChange, dirBefore.getAbsolutePath(), dirAfter.getAbsolutePath());
            List<GHFile> ghChangedFiles = collectGHChangedFiles();
            
            for (DiffFile diffFile : codeChange.getDiffFiles()) {
                if (isIn(diffFile, ghChangedFiles)) {
                    filesChanged.getDiffFiles().add(diffFile);
                    if (diffFile.isJavaFile()) {
                        diffFile.setTest(isTest(firstCommit, diffFile) || isTest(lastCommit, diffFile));
                    }
                }
            }
            
        } catch (CommitMissingException | IOException e) {
            /* empty */
        }
    }
    
    private boolean isTest(Commit commit, DiffFile diffFile) {
        CodeChange codeChange = commit.getCodeChange();
        for (DiffFile df : codeChange.getDiffFiles()) {
            if (df.getPath().equals(diffFile.getPath())) {
                return df.isTest();
            }
        }
        return false;
    }
    
    private void commandGitDiff(CodeChange codeChange, String basePathBefore, String basePathAfter)
            throws CommitMissingException, IOException {
        String workingDir = pullRequestDir.getAbsolutePath();
        
        String C_cd_working = "cd " + workingDir;
        String C_gitDiff = "git diff " + basePathBefore + " " + basePathAfter;
        
        String diffCommand =
                C_cd_working + " ; " +
                C_gitDiff;
        
        String diffOutput = DiffBuilder.executeDiff(diffCommand);
        DiffBuilder.buildDiffFiles(pullRequest, codeChange, diffOutput, basePathBefore, basePathAfter);
        
        for (DiffFile diffFile : codeChange.getDiffFiles()) {
            if (diffFile.isJavaFile()) {
                for (FileChange fileChange : codeChange.getFileChanges()) {
                    if (diffFile.getChangeType() == fileChange.getChangeType()) {
                        if (fileChange.getPath().contains(diffFile.getPath())) {
                            diffFile.setTest(fileChange.isTest());
                        }
                    }
                }
            }
        }
    }
    
    private List<GHFile> collectGHChangedFiles() throws IOException {
        List<GHFile> ghChangedFiles = new ArrayList<>();
        
        for (GHPullRequestFileDetail gfFileDetail : ghPullRequest.listFiles().toList()) {
            if (!gfFileDetail.getStatus().equals("removed")) {
                GHContent content = repository.getFileContent(gfFileDetail.getFilename(),
                        ghPullRequest.getHead().getSha());
                
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(content.read(), "UTF-8"))) {
                    String line = "";
                    StringBuilder text = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        text.append(line);
                    }
                    
                    GHFile ghFile = new GHFile(content.getPath(), text.toString());
                    ghChangedFiles.add(ghFile);
                } catch (IOException e) {
                    /* empty */
                }
            } else if (gfFileDetail.getStatus().equals("removed")) {
                for (GHPullRequestCommitDetail ghCommitDetail : ghPullRequest.listCommits()) {
                    GHCommit ghcmti = repository.getCommit(ghCommitDetail.getSha());
                    for (GHCommit.File file : ghcmti.listFiles()) {
                        if (file.getFileName().equals(gfFileDetail.getFilename()) && file.getStatus().equals("removed")) {
                            String removedPath = file.getFileName();
                            String removedContent = file.getPatch();
                            if (removedContent == null) {
                                removedContent = "";
                            }
                            
                            GHFile ghFile = new GHFile(removedPath, removedContent);
                            ghChangedFiles.add(ghFile);
                        }
                    }
                }
            }
        }
        return ghChangedFiles;
    }
    
    private boolean isIn(DiffFile diffFile, List<GHFile> ghChangedFiles) {
        for (GHFile ghFile : ghChangedFiles) {
            if (diffFile.getChangeType() == PRElement.ADD || diffFile.getChangeType() == PRElement.REVISE) {
                if (ghFile.path.equals(diffFile.getPath()) &&
                        ghFile.content.equals(diffFile.getSourceCodeAfter())) {
                    return true;
                }
            } else if (diffFile.getChangeType() == PRElement.DELETE) {
                if (ghFile.path.equals(diffFile.getPath()) &&
                        ghFile.content.contains(diffFile.getBodyDel())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private class GHFile {
        
        final String path;
        final String content;
        
        GHFile(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}
