/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.prmodel;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHPullRequest;
//import org.kohsuke.github.GHPullRequestQueryBuilder;
import org.kohsuke.github.GHPullRequestSearchBuilder;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

import org.jtool.jwrmodel.JsonFileWriter;
import org.jtool.prmodel.builder.PRModelBuilder;

public class PRModelBundle {
    
    private final String psnToken;
    private final String repoName;
    private final String rootPath;
    
    private final int[] pullRequestNumbers;
    
    private int changedFilesMin = 0;
    private int changedFilesMax = Integer.MAX_VALUE;
    private int commitsMin = 0;
    private int commitsMax = Integer.MAX_VALUE;
    
    private boolean deleteSourceFile = false;
    private boolean writeErrorLog = false;
    private boolean writeOnly = false;
    private boolean changedCodeBuild = true;
    
    private File repoDir;
    
    private GitHub github = null;
    private GHRepository repository;
    //private GHPullRequestQueryBuilder prQuery;
    private GHPullRequestSearchBuilder prSearch;
    
    public PRModelBundle(String psnToken, String repoName, String rootPath, int[] pullRequestNumbers) {
        this.psnToken = psnToken;
        this.repoName = repoName;
        this.rootPath = rootPath;
        this.pullRequestNumbers = pullRequestNumbers.clone();
        
        try {
            this.github = GitHub.connectUsingOAuth(psnToken);
            this.repository = this.github.getRepository(repoName);
            //this.prQuery = this.repository.queryPullRequests();
            this.prSearch = this.repository.searchPullRequests();
        } catch (IOException e) {
            System.out.println("Please check the repository name in GitHub: " + repoName);
        }
    }
    
    public PRModelBundle(String psnToken, String repoName, String rootPath, int pullRequestNumber) {
        this(psnToken, repoName, rootPath, new int[]{ pullRequestNumber });
    }
    
    public PRModelBundle(String psnToken, String repoName, String rootPath) {
        this(psnToken, repoName, rootPath, new int[0]);
    }
    
    public PRModel build() {
        PRModel prmodel = new PRModel();
        if (pullRequestNumbers.length > 0) {
            build(prmodel, pullRequestNumbers);
        } else {
            build(prmodel);
        }
        return prmodel;
    }
    
    private String getPullRequestPath(int pullRequestNumber) {
        return repoDir.getPath() + File.separator + pullRequestNumber;
    }
    
    public static File getDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }
    
    public static String getRelativePath(String absolutePath, String basePath) {
        String[] paths = absolutePath.split(basePath + File.separator);
        if (paths.length > 1) {
            return paths[1];
        }
        return "";
    }
    
    private boolean alreadyBuilt(String path) {
        File file = new File(path);
        return file.exists();
    }
    
    private void build(PRModel prmodel, int[] pullRequestNumbers) {
        System.out.println("PRCollector start");
        
        String datasetPath = rootPath + File.separator + "PRCollector";
        File datasetDir = getDir(datasetPath);
        String repoPath = datasetDir.getAbsolutePath() + File.separator + repository.getName();
        repoDir = getDir(repoPath);
        
        for (int index = 0; index < pullRequestNumbers.length; index++) {
            int number = pullRequestNumbers[index];
            String prPath = getPullRequestPath(number);
            if (!alreadyBuilt(prPath)) {
                File prDir = getDir(prPath);
                
                PRModelBuilder builder = new PRModelBuilder(this, psnToken, repoName, number, prDir);
                boolean result = builder.build();
                
                if (result) {
                    PullRequest pullRequest = builder.getPullRequest();
                    if (pullRequest != null) {
                        writePRModelToFile(pullRequest, prDir);
                        
                        if (writeOnly) {
                            pullRequest = null;
                        } else {
                            prmodel.addPullRequest(pullRequest);
                        }
                    }
                } else {
                    DeficientPullRequest pullRequest = builder.getDeficientPullRequest();
                    if (pullRequest != null) {
                        writeDataLossToFile(pullRequest, prDir);
                        
                        if (writeOnly) {
                            pullRequest = null;
                        } else {
                            prmodel.addDeficientPullRequest(pullRequest);
                        }
                    }
                }
                builder = null;
            } else {
                System.out.println("Already built : " + repository.getName() + "  ---  " + number);
            }
        }
        
        System.out.println("Finish");
    }
    
    private void build(PRModel prmodel) {
        try {
            PagedIterable<GHPullRequest> ghPullRequests = prSearch.list();
            int[] numbers = new int[ghPullRequests.toList().size()];
            int index = 0;
            for (GHPullRequest ghPullRequest : ghPullRequests) {
                numbers[index] = ghPullRequest.getNumber();
                index++;
            }
            build(prmodel, numbers);
        } catch (IOException e) {
            System.out.println("Not ontained pull request numbers");
        }
    }
    
    private void writePRModelToFile(PullRequest pullRequest, File pullRequestDir) {
        String jsonPath = pullRequestDir + File.separator
                        + pullRequest.getRepositoryName() + "_" + pullRequest.getId() + "_str.json";
        JsonFileWriter jfwriter = new JsonFileWriter(pullRequest, jsonPath);
        
        jfwriter.writePRModel();
        deleteGitSourceFile(pullRequest, pullRequestDir);
    }
    
    private void writeDataLossToFile(DeficientPullRequest pullRequest, File pullRequestDir) {
        String jsonPath = pullRequestDir + File.separator
                        + pullRequest.getRepositoryName() + "_" + pullRequest.getId() + "_loss.json";
        JsonFileWriter jfwriter = new JsonFileWriter(pullRequest, jsonPath);
        
        jfwriter.writePRModelWithDataLoss();
        deleteGitSourceFile(pullRequest, pullRequestDir);
    }
    
    private void deleteGitSourceFile(PullRequest pullRequest, File pullRequestDir) {
        if (!deleteSourceFile) {
            return;
        }
        
        String pathBase = pullRequestDir.getAbsolutePath() + File.separator +"BaseSource";
        try {
            deleteDirectory(pathBase);
        } catch (IOException e) {
            System.out.println("Not found base code directory to be deleted! " + pullRequestDir);
        }
        
        for (Commit commit : pullRequest.getTargetCommits()) {
            String dirNameBefore = PRElement.BEFORE + "_" + commit.getShortSha();
            String pathBefore = pullRequestDir.getAbsolutePath() + File.separator + dirNameBefore; 
            String dirNameAfter = PRElement.AFTER + "_" + commit.getShortSha();
            String pathAfter = pullRequestDir.getAbsolutePath() + File.separator + dirNameAfter;
            
            try {
                deleteDirectory(pathBefore);
                deleteDirectory(pathAfter);
            } catch (IOException e) {
                System.out.println("Not found directory to be deleted! " + pullRequestDir);
            }
        }
    }
    
    private void deleteDirectory(String path) throws IOException {
        Path dir = Paths.get(path);
        if (Files.exists(dir)) {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                
                @Override
                 public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                 public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }
    
    /**
     * Set upper limit number and lower limit number of changed files.
     * @param min the lower limit number
     * @param max the upper limit number
     */
    public void downloadChangedFileNum(int min , int max) {
        this.changedFilesMin = min;
        this.changedFilesMax = max;
    }
    
    /**
     * Set upper limit number and lower limit number of commits.
     * @param min the lower limit number
     * @param max the upper limit number
     */
    public void downloadCommitNum(int min , int max) {
        this.commitsMin = min;
        this.commitsMax = max;
    }
    
    /**
     * Set a flag whether download files will be deleted.
     * @param bool <code>true</code> if deleting is desired, otherwise <code<false</code>
     */
    public void deleteSourceFile(boolean bool) {
        this.deleteSourceFile = bool;
    }
    
    /**
     * Set a flag whether an error log be written.
     * @param bool <code>true</code> if writing is desired, otherwise <code<false</code>
     */
    public void writeErrorLog(boolean bool) {
        this.writeErrorLog = bool;
    }
    
    /**
     * Set a flag whether pull request data will not be saved to memory.
     * @param bool <code>true</code> if not-saving is desired, otherwise <code<false</code>
     */
    public void writeOnly(boolean bool) {
        this.writeOnly = bool;
    }
    
    /**
     * Set a flag whether code element data will not be saved to json file.
     * @param bool <code>true</code> if not-saving is desired, otherwise <code<false</code>
     */
    public void changedCodeBuild(boolean bool) {
    	this.changedCodeBuild = bool;
    }
    
    public String getRepositoryName() {
        return repoName;
    }
    
    public String getRootPath() {
        return rootPath;
    }
    
    public int getDownloadChangedFilesNumMin() {
        return changedFilesMin;
    }
    
    public int getDownloadChangedFilesNumMax() {
        return changedFilesMax;
    }
    
    public int getDownloadCommitsNumMin() {
        return commitsMin;
    }
    
    public int getDownloadCommitsNumMax() {
        return commitsMax;
    }
    
    
    public File getRepoDir() {
        return repoDir;
    }
    
    public boolean writeErrorLog() {
        return writeErrorLog;
    }
    
    public boolean writeOnly() {
        return writeOnly;
    }
    
    public boolean changedCodeBuild() {
    	return changedCodeBuild;
    }
    
    /**
     * Set search pull requests by the assigned user's login name.
     */
    public void searchByAssignedUser(String userName) {
        try {
            GHUser assignee = github.getUser(userName);
            prSearch.assigned(assignee);
        } catch (IOException e) {
            System.err.println("Invalid user name: " + userName);
        }
    }
    
    /**
     * Set search pull requests by the author's login name.
     */
    public void searchByAuthor(String userName) {
        try {
            GHUser author = github.getUser(userName);
            prSearch.author(author);
        } catch (IOException e) {
            System.err.println("Invalid user name: " + userName);
        }
    }
    
    /**
     * Set search pull requests by the name of merge branch.
     */
    public void searchByBaseBranch(String branchName) {
        try {
            GHBranch branch = repository.getBranch(branchName);
            prSearch.base(branch);
        } catch (IOException e) {
            System.err.println("Invalid branch name: " + branchName);
        }
    }
    
    /**
     * Set search pull request by closed date.
     */
    public void searchByClosed(String closed) {
        LocalDate closedDate = LocalDate.parse(closed);
        prSearch.closed(closedDate);
    }
    
    /**
     * Set search pull request by closed date period.
     */
    public void searchByClosed(String from, String to) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        prSearch.closed(fromDate, toDate);
    }
    
    /**
     * Set search pull request by after closed date with inclusive flag.
     */
    public void searchByClosedAfter(String closed, boolean inclusive) {
        LocalDate closedDate = LocalDate.parse(closed);
        prSearch.closedAfter(closedDate, inclusive);
    }
    
    /**
     * Set search pull request by before closed date with inclusive flag.
     */
    public void searchByClosedBefore(String closed, boolean inclusive) {
        LocalDate closedDate = LocalDate.parse(closed);
        prSearch.closedBefore(closedDate, inclusive);
    }
    
    /**
     * Set search pull request by commit full sha.
     */
    public void searchByCommit(String sha) {
        prSearch.commit(sha);
    }
    
    /**
     * Set search pull request by created date.
     */
    public void searchByCreated(String created) {
        LocalDate createdDate = LocalDate.parse(created);
        prSearch.created(createdDate);
    }
    
    /**
     * Set search pull request by created date period.
     */
    public void searchByCreated(String from, String to) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        prSearch.created(fromDate, toDate);
    }
    
    /**
     * Set search pull request by after created date with inclusive flag.
     */
    public void searchByCreatedAfter(String created, boolean inclusive) {
        LocalDate createdDate = LocalDate.parse(created);
        prSearch.createdAfter(createdDate, inclusive);
    }
    
    /**
     * Set search pull request by before created date with inclusive flag.
     */
    public void searchByCreatedBefore(String created, boolean inclusive) {
        LocalDate createDate = LocalDate.parse(created);
        prSearch.createdBefore(createDate, inclusive);
    }
    
    /**
     * Set search pull request by head branch name.
     */
    public void searchByHeadBranch(String branchName) {
        try {
            GHBranch branch = repository.getBranch(branchName);
            this.prSearch.head(branch);
        } catch (IOException e) {
            System.err.println("Invalid branch name: " + branchName);
        }
    }
    
    /**
     * Set search pull request by labels.
     */
    public void searchByInLabels(List<String> labels) {
        prSearch.inLabels(labels);
    }
    
    /**
     * Set search pull request by containing a label.
     */
    public void searchByLabel(String label) {
        prSearch.label(label);
    }
    
    /**
     * Set search pull request by if state is closed.
     */
    public void searchByIsClosed() {
        prSearch.isClosed();
    }
    
    /**
     * Set search pull request by if it is a draft.
     */
    public void searchByIsDraft() {
        prSearch.isDraft();
    }
    
    /**
     * Set search pull request by if state is merged.
     */
    public void searchByIsMerged() {
        prSearch.isMerged();
    }
    
    /**
     * Set search pull request by if state is opened.
     */
    public void searchByIsOpen() {
        prSearch.isOpen();
    }
    
    /**
     * Set search pull request by mentioning a user name.
     */
    public void searchByMentions(String userName) {
        try {
            GHUser mentionUser = this.github.getUser(userName);
            prSearch.mentions(mentionUser);
        } catch (IOException e) {
            System.err.println("Invalid user name: " + userName);
        }
    }
    
    /**
     * Set search pull request by merged date.
     */
    public void searchByMerged(String merged) {
        LocalDate mergedDate = LocalDate.parse(merged);
        prSearch.merged(mergedDate);
    }
    
    /**
     * Set search pull request by merged date period.
     */
    public void searchByMerged(String from, String to) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        prSearch.merged(fromDate, toDate);
    }
    
    /**
     * Set search pull request by after merged date with inclusive flag.
     */
    public void searchByMergedAfter(String merged, boolean inclusive) {
        LocalDate mergedDate = LocalDate.parse(merged);
        prSearch.mergedAfter(mergedDate, inclusive);
    }
    
    /**
     * Set search pull request by before merged date with inclusive flag.
     */
    public void searchByMergedBefore(String merged, boolean inclusive) {
        LocalDate mergedDate = LocalDate.parse(merged);
        prSearch.mergedBefore(mergedDate, inclusive);
    }
    
    /**
     * Set search pull request by a title String.
     */
    public void searchByTitleLike(String title) {
        prSearch.titleLike(title);
    }
    
    /**
     * Set search pull request by updated date.
     */
    public void searchByUpdated(String updated) {
        LocalDate updatedDate = LocalDate.parse(updated);
        prSearch.updated(updatedDate);
    }
    
    /**
     * Set search pull request by updated date period.
     */
    public void searchByUpdated(String from, String to) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        prSearch.updated(fromDate, toDate);
    }
    
    /**
     * Set search pull request by after updated date with inclusive flag.
     */
    public void searchByUpdatedAfter(String updated, boolean inclusive) {
        LocalDate updateDate = LocalDate.parse(updated);
        prSearch.updatedAfter(updateDate, inclusive);
    }
    
    /**
     * Set search pull request by before updated date with inclusive flag.
     */
    public void searchByUpdateBefore(String updated, boolean inclusive) {
        LocalDate updateDate = LocalDate.parse(updated);
        prSearch.updatedBefore(updateDate, inclusive);
    }
}
