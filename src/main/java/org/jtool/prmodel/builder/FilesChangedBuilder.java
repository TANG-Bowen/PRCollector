package org.jtool.prmodel.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestCommitDetail;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHRepository;

import org.jtool.prmodel.Commit;
import org.jtool.prmodel.DiffFile;

import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.FilesChanged;
import org.jtool.prmodel.PRElement;
import org.jtool.jxp3model.FileChange;

public class FilesChangedBuilder {
    
    private final PullRequest pullRequest;
    private final GHPullRequest ghPullRequest;
    private final GHRepository repository;
    
    FilesChangedBuilder(PullRequest pullRequest, GHPullRequest ghPullRequest, GHRepository repository) {
        this.pullRequest = pullRequest;
        this.ghPullRequest = ghPullRequest;
        this.repository = repository;
    }
    
    void build() throws IOException {
        List<GHFile> ghChangedFiles = collectGHChangedFiles();
        
        if (pullRequest.getCommits().size() > 1) {
            List<Commit> commits = pullRequest.getTragetCommits();
            
            boolean hasJavaFile = commits.stream().anyMatch(c -> c.getCodeChange().hasJavaFile());
            
            FilesChanged filesChanged = new FilesChanged(pullRequest, hasJavaFile);
            pullRequest.setFilesChanged(filesChanged);
            
            for (Commit commit : commits) {
                for (DiffFile diffFile : commit.getCodeChange().getDiffFiles()) {
                    if (isIn(diffFile, ghChangedFiles)) {
                        filesChanged.getDiffFiles().add(diffFile);
                    }
                }
                
                for (FileChange fileChange : commit.getCodeChange().getFileChanges()) {
                    if (isIn(fileChange, ghChangedFiles) && !filesChanged.getFileChanges().contains(fileChange)) {
                        filesChanged.getFileChanges().add(fileChange);
                    }
                }
            }
            
            removeReplicatedDiffFiles(filesChanged);
            
        } else if (pullRequest.getCommits().size() == 1) {
            Commit commit = pullRequest.getCommits().get(0);
            
            FilesChanged filesChangedInfo = new FilesChanged(pullRequest, commit.getCodeChange().hasJavaFile());
            pullRequest.setFilesChanged(filesChangedInfo);
            
            for (DiffFile diffFile : commit.getCodeChange().getDiffFiles()) {
                filesChangedInfo.getDiffFiles().add(diffFile);
            }
            for (FileChange fileChange : commit.getCodeChange().getFileChanges()) {
                filesChangedInfo.getFileChanges().add(fileChange);
            }
            
            removeReplicatedDiffFiles(filesChangedInfo);
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
                String removedPath="";
                String removedContent="";
                
                for (GHPullRequestCommitDetail ghCommitDetail : ghPullRequest.listCommits()) {
                    GHCommit ghcmti = repository.getCommit(ghCommitDetail.getSha());
                    for (GHCommit.File file : ghcmti.listFiles()) {
                        if (file.getFileName().equals(gfFileDetail.getFilename()) && file.getStatus().equals("removed")) {
                            removedPath = file.getFileName();
                            removedContent = file.getPatch();
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
                if (ghFile.path.equals(diffFile.getRelativePath()) &&
                        ghFile.content.equals(diffFile.getSourceCodeAfter())) {
                    return true;
                }
            } else if (diffFile.getChangeType() == PRElement.DELETE) {
                if (ghFile.path.equals(diffFile.getRelativePath()) &&
                        ghFile.content.contains(diffFile.getBodyDel())) {
                    return true;
                }   
            }
        }
        return false;
    }
    
    private boolean isIn(FileChange fileChange, List<GHFile> ghChangedFiles) {
        if (fileChange.getChangeType() == PRElement.ADD || fileChange.getChangeType() == PRElement.REVISE) {
            String sourceCode  = fileChange.getSourceCodeAfter().replaceAll("\n", "");
            for (GHFile ghFile : ghChangedFiles) {
                if (fileChange.getPathAfter().contains(ghFile.path) &&
                        ghFile.content.equals(sourceCode)) {
                    return true;
                }
            }
        } else if (fileChange.getChangeType() == PRElement.DELETE) {
            String sourceCode = fileChange.getSourceCodeBefore().replaceAll("\n", "");
            for (GHFile ghFile : ghChangedFiles) {
                if (fileChange.getPathBefore().contains(ghFile.path) &&
                        ghFile.content.equals(sourceCode)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void removeReplicatedDiffFiles(FilesChanged filesChangedInfo) {
        if (filesChangedInfo.getDiffFiles().isEmpty()) {
            return;
        }
        
        Set<DiffFile> filesBefore = filesChangedInfo.getDiffFiles().stream()
                .filter(f -> f.getChangeType() == PRElement.ADD || f.getChangeType() == PRElement.REVISE)
                .collect(Collectors.toSet());
        
        Set<DiffFile> removedFiles = new HashSet<>();
        List<DiffFile> filesAfter = new ArrayList<>(filesBefore);
        for (DiffFile fileBefore : filesBefore) {
            for (DiffFile fileAfter : filesAfter) {
                if (fileBefore.getBodyAdd().contains(fileAfter.getBodyAdd()) &&
                   !fileBefore.getBodyAdd().equals(fileAfter.getBodyAdd())) {
                    
                    String[] addBefore = fileBefore.getBodyAdd().split(" ");
                    int lenBefore = addBefore.length;
                    String[] addAfter = fileAfter.getBodyAdd().split(" ");
                    int lenAfter = addAfter.length;
                    
                    if (lenAfter > lenBefore) {
                        for (DiffFile diffFile : filesChangedInfo.getDiffFiles()) {
                            if (diffFile.getRelativePath().equals(fileBefore.getRelativePath()) &&
                                diffFile.getChangeType() == fileBefore.getChangeType() &&
                                diffFile.getBodyAll().equals(fileBefore.getBodyAll())) {
                                removedFiles.add(diffFile);
                            }
                        }
                    } else if (lenAfter < lenBefore) {
                        for (DiffFile diffFile : filesChangedInfo.getDiffFiles()) {
                            if (diffFile.getRelativePath().equals(fileAfter.getRelativePath()) &&
                                diffFile.getChangeType() == fileAfter.getChangeType() &&
                                diffFile.getBodyAll().equals(fileAfter.getBodyAll())) {
                                removedFiles.add(diffFile);
                            }
                        }
                    }
                }
            }
        }
        filesChangedInfo.getDiffFiles().removeAll(removedFiles);
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
