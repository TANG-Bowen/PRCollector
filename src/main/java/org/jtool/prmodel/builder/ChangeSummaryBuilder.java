/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

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
import org.jtool.prmodel.ChangeSummary;
import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.PRModelBundle;
import org.jtool.jxp3model.FileChange;

public class ChangeSummaryBuilder {
    
	private List<Exception> exceptions = new ArrayList<>();
    private final PullRequest pullRequest;
    private final File pullRequestDir;
    
    private final GHPullRequest ghPullRequest;
    private final GHRepository repository;
    
    private final boolean changedCodeBuild;
    
    ChangeSummaryBuilder(PullRequest pullRequest, File pullRequestDir,
            GHPullRequest ghPullRequest, GHRepository repository, boolean changedCodeBuild) {
        this.pullRequest = pullRequest;
        this.ghPullRequest = ghPullRequest;
        this.repository = repository;
        this.changedCodeBuild = changedCodeBuild;
        this.pullRequestDir = pullRequestDir;
    }
    
    void build() {
        Commit firstCommit;
        Commit lastCommit;
        if (pullRequest.getTargetCommits().size() > 1) {
            List<Commit> commits = pullRequest.getTargetCommits();
            firstCommit = commits.get(0);
            lastCommit = commits.get(commits.size()-1);
            	
        } else if (pullRequest.getTargetCommits().size() == 1) {
            firstCommit = pullRequest.getTargetCommits().get(0);
            lastCommit = firstCommit;
        } else {
            return;
        }
        
        boolean hasJavaFile = false;
        if (firstCommit.getCodeChange() != null && lastCommit.getCodeChange() != null) {
            hasJavaFile = firstCommit.getCodeChange().hasJavaFile() || lastCommit.getCodeChange().hasJavaFile();
        }
        ChangeSummary changeSummary = new ChangeSummary(pullRequest, hasJavaFile);
        pullRequest.setChangeSummary(changeSummary);
        
        String dirNameBefore = PRElement.BEFORE + "_" + firstCommit.getShortSha();
        String pathBefore = pullRequestDir.getAbsolutePath() + File.separator + dirNameBefore;
        String dirNameAfter = PRElement.AFTER + "_" + lastCommit.getShortSha();
        String pathAfter = pullRequestDir.getAbsolutePath() + File.separator + dirNameAfter;
        
        File dirBefore = PRModelBundle.getDir(pathBefore);
        File dirAfter = PRModelBundle.getDir(pathAfter);
        
        try {
            CodeChange codeChange = new CodeChange(pullRequest);
            commandGitDiff(codeChange, dirBefore.getAbsolutePath(), dirAfter.getAbsolutePath());
            List<String> ghChangedFilePaths = collectGHChangedFiles();
            for (DiffFile diffFile : codeChange.getDiffFiles()) {
                if (isIn(diffFile, ghChangedFilePaths)) {
                    changeSummary.getDiffFiles().add(diffFile);
                    if (diffFile.isJavaFile()) {
                        diffFile.setTest(isTest(firstCommit, diffFile) || isTest(lastCommit, diffFile));
                    }
                }
            }
			if (changedCodeBuild) {
				CodeChangeBuilder codeChangeBuilder = new CodeChangeBuilder(pullRequest, pullRequestDir);
				codeChangeBuilder.buildProjectChanges(codeChange, pathBefore, pathAfter);
				codeChange.setFileChanges();
				codeChangeBuilder.setReferenceRelation(codeChange);
				codeChangeBuilder.setTestForClasses(codeChange);

				for (FileChange fileChange : codeChange.getFileChanges()) {
					changeSummary.getFileChanges().add(fileChange);
				}
			}
        } catch (Exception e) {
        	this.exceptions.add(e);
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
    }
    
    private List<String> collectGHChangedFiles() throws Exception {
        List<String> ghChangedFilePaths = new ArrayList<>();
        
        for (GHPullRequestFileDetail gfFileDetail : ghPullRequest.listFiles().toList()) {
            if (!gfFileDetail.getStatus().equals("removed")) {
                GHContent content = repository.getFileContent(gfFileDetail.getFilename(),
                        ghPullRequest.getHead().getSha());
                
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(content.read()))) {
                    String line = "";
                    StringBuilder text = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        text.append(line);
                    }
                    ghChangedFilePaths.add(content.getPath());
                } catch (Exception e) {
                	this.exceptions.add(e);
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
                            ghChangedFilePaths.add(removedPath);
                        }
                    }
                }
            }
        }
        return ghChangedFilePaths;
    }
    
    private boolean isIn(DiffFile diffFile, List<String> ghChangedFilePaths) {
        for (String path : ghChangedFilePaths) {
            if (diffFile.getChangeType() == PRElement.ADD || diffFile.getChangeType() == PRElement.REVISE) {
                if (path.equals(diffFile.getPath())) {
                    return true;
                }
            } else if (diffFile.getChangeType() == PRElement.DELETE) {
                if (path.equals(diffFile.getPath())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    List<Exception> getExceptions() {
        return exceptions;
    }
}
