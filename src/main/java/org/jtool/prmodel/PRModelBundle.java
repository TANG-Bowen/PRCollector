package org.jtool.prmodel;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

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
    
    private final String ghToken;
    private final String repositoryName;
    private final String rootSrcPath;
    
    private final int pullRequestNumber;
    
    private GitHub github = null;
    private GHRepository repository;
    //private GHPullRequestQueryBuilder prQuery;
    private GHPullRequestSearchBuilder prSearch;
    
    private int changedFilesMin = 0;
    private int changedFilesMax = Integer.MAX_VALUE;
    private int commitMin = 0;
    private int commitMax = Integer.MAX_VALUE;
    
    private List<String> bannedLabels = new ArrayList<>();
    
    private boolean writeErrorLog = false;
    
    private boolean writeFile = false;
    private boolean deleteSourceFile = false;
    
    public PRModelBundle(String ghToken, String repositoryName, String rootSrcPath, int pullRequestNumber) {
        this.ghToken = ghToken;
        this.repositoryName = repositoryName;
        this.rootSrcPath = rootSrcPath;
        this.pullRequestNumber = pullRequestNumber;
        
        try {
            this.github = GitHub.connectUsingOAuth(ghToken);
            this.repository = this.github.getRepository(repositoryName);
            //this.prQuery = this.repository.queryPullRequests();
            this.prSearch = this.repository.searchPullRequests();
        } catch (IOException e) {
            /* empty */
        }
    }
    
    public PRModelBundle(String ghToken, String repositoryName, String rootSrcPath) {
        this(ghToken, repositoryName, rootSrcPath, -1);
    }
    
    public PRModel build() {
        PRModel prmodel = new PRModel();
            if (pullRequestNumber >= 0) {
                buildSingle(prmodel, pullRequestNumber);
            } else {
                build(prmodel);
            }
       
        return prmodel;
    }
    
    private String getPullRequestPath(int pullRequestNumber) {
        String rootPath = rootSrcPath + File.separator + "PRCollector";
        File rootDir = getDir(rootPath);
        String repoPath = rootDir.getAbsolutePath() + File.separator + repository.getName();
        File repositoryDir = getDir(repoPath);
        String prPath = repositoryDir.getAbsolutePath() + File.separator + pullRequestNumber;
        return prPath;
    }
    
    private boolean alreadyBuilt(String path) {
        File file = new File(path);
        return file.exists();
    }
    
    private void build(PRModel prmodel) {
        PagedIterable<GHPullRequest> ghPullRequests = prSearch.list();
        for (GHPullRequest ghPullRequest : ghPullRequests) {
            String pullRequestPath = getPullRequestPath(ghPullRequest.getNumber());
            if (!alreadyBuilt(pullRequestPath)) {
                File pullRequestDir = getDir(pullRequestPath);
                PRModelBuilder builder = new PRModelBuilder(this, ghToken, ghPullRequest.getNumber(), pullRequestDir);
                boolean result = builder.build();
                if (result) {
                    PullRequest pullRequest = builder.getPullRequest();
                    if (pullRequest != null) {
                        prmodel.addPullRequest(pullRequest);
                        writePRModelToFile(pullRequest, pullRequestDir);
                    }
                } else {
                    JsonFileWriter.deleteFiles(pullRequestDir.getAbsolutePath(), false);
                    System.out.println("Delete files after error log : " + pullRequestDir.getAbsolutePath()); 
                    
                    DeficientPullRequest pullRequest = builder.getDeficientPullRequest();
                    if (pullRequest != null) {
                        prmodel.addDeficientPullRequest(pullRequest);
                        writeDataLossToFile(pullRequest, pullRequestDir);
                    }
                }
                builder = null;
            } else {
                System.out.println("Already built : " + repository.getName() + "  ---  " + ghPullRequest.getNumber());
            }
        }
    }
    
    private void buildSingle(PRModel prmodel, int pullRequestNumber) {
        String pullRequestPath = getPullRequestPath(pullRequestNumber);
        if (!alreadyBuilt(pullRequestPath)) {
            File pullRequestDir = getDir(pullRequestPath);
            PRModelBuilder builder = new PRModelBuilder(this, ghToken, pullRequestNumber, pullRequestDir);
            
            boolean result = builder.build();
            if (result) {
                PullRequest pullRequest = builder.getPullRequest();
                if (pullRequest != null) {
                    prmodel.addPullRequest(pullRequest);
                    writePRModelToFile(pullRequest, pullRequestDir);
                }
            } else {
                JsonFileWriter.deleteFiles(pullRequestDir.getAbsolutePath(),false);
                System.out.println("Delete files after error log : " + pullRequestDir.getAbsolutePath()); 
                
                DeficientPullRequest pullRequest = builder.getDeficientPullRequest();
                if (pullRequest != null) {
                    prmodel.addDeficientPullRequest(pullRequest);
                    writeDataLossToFile(pullRequest, pullRequestDir);
                }
            }
            builder = null;
        } else {
            System.out.println("Already built : " + repository.getName() + "  ---  " + pullRequestNumber);
        }
    }
    
    private void writePRModelToFile(PullRequest pullRequest, File pullRequestDir) {
        String jsonPath = pullRequestDir + File.separator
                        + pullRequest.getRepositoryName() + "_" + pullRequest.getId() + "_str.json";
        JsonFileWriter jfwriter = new JsonFileWriter(pullRequest, jsonPath);
        
        if (writeFile) {
            jfwriter.writePRModel();
            
            if (deleteSourceFile) {
                JsonFileWriter.deleteGitSourceFile(pullRequest, pullRequestDir);
            }
        } else {
            if (deleteSourceFile) {
                JsonFileWriter.deleteGitSourceFile(pullRequest, pullRequestDir);
                System.out.println("delete source files after error logs");
            }
            JsonFileWriter.deleteFiles(pullRequestDir.getAbsolutePath(), true);
            System.out.println("delete retained source files under the pr dir ");
        }
    }
    
    private void writeDataLossToFile(DeficientPullRequest pullRequest, File pullRequestDir) {
        String jsonPath = pullRequestDir + File.separator + pullRequest.getRepositoryName() + "_" + pullRequest.getId()+"_loss.json";
        JsonFileWriter jfwriter = new JsonFileWriter(pullRequest, jsonPath);
        jfwriter.writePRModelWithDataLoss();
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
    
    /**
     * Set upper limit number and lower limit number of changed files.
     */
    public void downloadChangedFileNum(int min , int max) {
        this.changedFilesMin = min;
        this.changedFilesMax = max;
    }
    
    /**
     * Set upper limit number and lower limit number of commits.
     */
    public void downloadCommitNum(int min , int max) {
        this.commitMin = min;
        this.commitMax = max;
    }
    
    /**
     * Set ban label.
     */
    public void setBannedLabels(List<String> labels) {
        bannedLabels = labels;
    }
    
    /**
     * Set write file flag.
     */
    public void writeFile(boolean bool) {
        this.writeFile = bool;
    }
    
    /**
     * Set download file flag.
     */
    public void deleteSourceFile(boolean bool) {
        this.deleteSourceFile = bool;
    }
    
    /**
     * Set write error log file flag.
     */
    public void setWriteErrorLog(boolean bool) {
        this.writeErrorLog = bool;
    }
    
    public String getRepositoryName() {
        return repositoryName;
    }
    
    public String getRootSrcPath() {
        return rootSrcPath;
    }
    
    public int getDownloadChangedFilesNumMin() {
        return changedFilesMin;
    }
    
    public int getDownloadChangedFilesNumMax() {
        return changedFilesMax;
    }
    
    public int getDownloadCommitsNumMin() {
        return commitMin;
    }
    
    public int getDownloadCommitsNumMax() {
        return commitMax;
    }
    
    public List<String> getBannedLabels() {
        return bannedLabels;
    }
    
    public boolean writeErrorLog() {
        return writeErrorLog;
    }
    
    public boolean writeFile() {
        return writeFile;
    }
    
    public boolean deleteSourceFile() {
        return deleteSourceFile;
    }
    
    /**
     * Set search pull request by assigned user's login name.
     */
    public void searchByAssignedUser(String userName) {
        try {
            GHUser assignee = this.github.getUser(userName);
            prSearch.assigned(assignee);
        } catch (IOException e) {
            System.err.println("Invalid user name: " + userName);
        }
    }
    
    /**
     * Set search pull request by assigned user's login name.
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
     * Set search pull request by name of merge branch.
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
    public void searchByHeadBranch(String branchName){
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
    public void searchByInLabels(ArrayList<String> labels) {
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
    public void searchByMentions(String userName){
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
