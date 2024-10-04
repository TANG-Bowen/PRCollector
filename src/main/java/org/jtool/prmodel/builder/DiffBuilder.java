package org.jtool.prmodel.builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

import org.jtool.jxp3model.FileChange;
import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.CodeChange;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.DiffFile;
import org.jtool.prmodel.DiffLine;
import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.PRModelBundle;

public class DiffBuilder {
    
    private final PullRequest pullRequest;
    private final File pullRequestDir;
    
    DiffBuilder(PullRequest pullRequest, File pullRequestDir) {
        this.pullRequest = pullRequest;
        this.pullRequestDir = pullRequestDir;
    }
    
    void build() throws CommitMissingException, IOException {
        for (Commit commit : pullRequest.getTragetCommits()) {
            String dirNameBefore = PRElement.BEFORE + "_" + commit.getShortSha();
            String pathBefore = pullRequestDir.getAbsolutePath() + File.separator + dirNameBefore; 
            String dirNameAfter = PRElement.AFTER + "_" + commit.getShortSha();
            String pathAfter = pullRequestDir.getAbsolutePath() + File.separator + dirNameAfter;
            
            File dirBefore = PRModelBundle.getDir(pathBefore);
            File dirAfter = PRModelBundle.getDir(pathAfter);
            
            CodeChange codeChange = new CodeChange(pullRequest);
            codeChange.setCommit(commit);
            commit.setCodeChange(codeChange);
            
            commandGit(commit, codeChange, dirBefore.getAbsolutePath(), dirAfter.getAbsolutePath());
            
            boolean hasJavaFile = codeChange.getDiffFiles().stream().anyMatch(f -> f.isJavaFile());
            codeChange.hasJavaFile(hasJavaFile);
        }
    }
    
    private void commandGit(Commit commit, CodeChange codeChange, String pathBefore, String pathAfter)
            throws CommitMissingException, IOException {
        String workingDir = pullRequestDir.getAbsolutePath();
        
        String C_cd_working = "cd " + workingDir;
        String C_cd_before  = "cd " + pathBefore;
        String C_cd_after   = "cd " + pathAfter;
        
        String C_gitClone_before = "git clone " + pullRequest.getHeadRepositorySrcDLUrl() + " " + pathBefore;
        String C_gitClone_after  = "git clone " + pullRequest.getHeadRepositorySrcDLUrl() + " " + pathAfter;
        
        String C_gitCheckoutBranch = "git checkout " + pullRequest.getHeadBranch();
        String C_gitCheckoutCommit = "git checkout " + commit.getSha();
        
        String C_gitCheckoutPreviousCommit      = "git checkout " + "HEAD~" + " --";
        //String C_gitCheckoutPreviousMergeCommit = "git checkout " + "HEAD~1^2 --";
        
        String C_gitDiff = "git diff " + pathBefore + " " + pathAfter;
        
        String downloadCommand =
                C_cd_working        + " ; " +
                C_gitClone_after    + " ; " +
                C_cd_after          + " ; " +
                C_gitCheckoutBranch + " ; " +
                C_gitCheckoutCommit + " ; " +
                C_cd_working        + " ; " +
                C_gitClone_before   + " ; " +
                C_cd_before         + " ; " +
                C_gitCheckoutBranch + " ; " +
                C_gitCheckoutCommit + " ; " +
                C_gitCheckoutPreviousCommit;
        
        String checkCommitCommand =
                C_cd_working        + " ; " +
                C_cd_after          + " ; " +
                C_gitCheckoutCommit + " ; " +
                C_cd_working        + " ; " +
                C_cd_before         + " ; " +
                C_gitCheckoutCommit + " ; " +
                C_gitCheckoutPreviousCommit;
        
        String diffCommand =
                C_cd_working + " ; " +
                C_gitDiff;
        
        downloadSourceCode(downloadCommand);
        System.out.println("Download Ok!");
        
        checkCommit(checkCommitCommand);
        
        String diffOutput = executeDiff(diffCommand);
        
        buildDiffFiles(codeChange, diffOutput, pathBefore, pathAfter);
    }
    
    private void downloadSourceCode(String command) throws CommitMissingException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        
        BufferedReader reader = null;
        try {
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            
            while ((line = reader.readLine()) != null) {
                System.out.println("      " + line + "****************");
            }
            process.waitFor();
        } catch (Exception e) {
            throw new CommitMissingException("Download error");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
    
    private void checkCommit(String commandChangeToCommit) throws CommitMissingException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", commandChangeToCommit);
        processBuilder.redirectErrorStream(true);
        
        BufferedReader reader = null;
        try {
            Process process = processBuilder.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("      " + line + "****************");
                
                if (line.contains("fatal: reference is not a tree:")) {
                    throw new CommitMissingException("Commit reference error");
                }
            }
            process.waitFor();
        } catch (Exception e) {
            throw new CommitMissingException("Git checkout error");
            
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
    
    private String executeDiff(String commandDiff) throws CommitMissingException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", commandDiff);
        
        BufferedReader reader = null;
        try {
            //processBuilder.inheritIO();
            Process process = processBuilder.start();
            StringBuilder diffOutput = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("      " + line + "****************");
                
                diffOutput.append(line).append("\n");
            }
            process.waitFor();
            return diffOutput.toString();
        } catch (Exception e) {
            throw new CommitMissingException("Git diff error");
            
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
    
    private void buildDiffFiles(CodeChange codeChange, String diffOutput, String topPathBefore, String topPathAfter) {
        String[] diffs = diffOutput.split("diff --git ");
        if (diffs.length  > 1) {
            for (int i = 1; i < diffs.length; i++) {
                String diffUnit= diffs[i];
                String[] lines = diffUnit.split("\n");
                
                if (lines.length > 0 && lines[0] != null && !lines[0].contains("/.git/")) {
                    String absolutePathBefore = "";
                    String absolutePathAfter = "";
                    
                    List<DiffLine> diffLines = new ArrayList<>();
                    
                    int plusCount = Integer.MAX_VALUE;
                    for (int j = 0; j < lines.length; j++) {
                        String[] line = lines[j].split(" "); 
                        if (line.length == 2 && line[0].equals("---") &&
                           !line[1].equals("/dev/null") && j < plusCount) {
                            String[] deleteLine = lines[j].split("--- a");
                            absolutePathBefore = deleteLine[1];
                        } else if (line.length == 2 && line[0].equals("+++") && j < plusCount) {
                            if (!line[1].equals("/dev/null")) {
                                String[] addLine = lines[j].split("\\+\\+\\+ b");
                                absolutePathAfter = addLine[1];
                            }
                            plusCount = j;
                            
                        } else if (j > plusCount) {
                            char firstC = lines[j].charAt(0);
                            if (firstC == '-') {
                                String text = lines[j].substring(1);
                                
                                DiffLine diffLine = new DiffLine(pullRequest, PRElement.DELETE, text);
                                diffLines.add(diffLine);
                            } else if (firstC == '+') {
                                String text = lines[j].substring(1);
                                
                                DiffLine diffLine = new DiffLine(pullRequest, PRElement.ADD, text);
                                diffLines.add(diffLine);
                            }
                        }
                    }
                    
                    String relativePath = getRelativePath(
                            absolutePathBefore, absolutePathAfter, topPathBefore, topPathAfter);
                    String changeType = getChangeType(absolutePathBefore, absolutePathAfter);
                    
                    String bodyAll = getDiffBodyAll(diffLines);
                    String bodyAdd = getDiffBodyAll(diffLines, PRElement.ADD);
                    String bodyDel = getDiffBodyAll(diffLines, PRElement.DELETE);
                    
                    String sourceCodeBefore = "";
                    String sourceCodeAfter = "";
                    if (changeType == PRElement.ADD) {
                        sourceCodeAfter = getCode(absolutePathAfter);
                    } else if (changeType == PRElement.DELETE) {
                        sourceCodeBefore = getCode(absolutePathBefore);
                    } else if (changeType == PRElement.REVISE) {
                        sourceCodeBefore = getCode(absolutePathBefore);
                        sourceCodeAfter = getCode(absolutePathAfter);
                    }
                    
                    boolean hasJavaFile = relativePath.endsWith(".java");
                    
                    DiffFile diffFile = new DiffFile(pullRequest, absolutePathBefore, absolutePathAfter,
                            relativePath, changeType, bodyAll, bodyAdd, bodyDel,
                            sourceCodeBefore, sourceCodeAfter, hasJavaFile);
                    diffFile.setCodeChange(codeChange);
                    codeChange.getDiffFiles().add(diffFile);
                    
                    diffFile.getDiffLines().addAll(diffLines);
                    diffLines.forEach(d -> d.setDiffFile(diffFile));
                }
            }
        }
    }
    
    private String getRelativePath(String pathBefore, String pathAfter,
            String topPathBefore, String topPathAfter) {
        if (!pathBefore.equals("")) {
            String[] paths = pathBefore.split(topPathBefore + File.separator);
            if (paths.length > 1) {
                return paths[1];
            }
        } else if (!pathAfter.equals("")) {
            String[] paths = pathAfter.split(topPathAfter + File.separator);
            if (paths.length > 1) {
                return paths[1];
            }
        }
        return "";
    }
    
    private String getDiffBodyAll(List<DiffLine> diffLines) {
        StringBuilder text = new StringBuilder();
        for (DiffLine diffLine : diffLines) {
            text.append(diffLine.getText());
        }
        return text.toString();
    }
    
    private String getDiffBodyAll(List<DiffLine> diffLines, String changeType) {
        StringBuilder text = new StringBuilder();
        for (DiffLine diffLine : diffLines) {
            if (diffLine.getChangeType() == changeType) {
                text.append(diffLine.getText());
            }
        }
        return text.toString();
    }
    
    private String getChangeType(String absolutePathBefore, String absolutePathAfter) {
        if (absolutePathBefore.equals("") && !absolutePathAfter.equals("")) {
            return PRElement.ADD;
        } else if (!absolutePathBefore.equals("") && absolutePathAfter.equals("")) {
            return PRElement.DELETE;
        } else if (!absolutePathBefore.equals("") && !absolutePathAfter.equals("")) {
            return PRElement.REVISE;
        }
        return PRElement.NO_CHANGE;
    }
    
    private String getCode(String absolutePath) {
        File file = new File(absolutePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String code = "";
            while ((line = reader.readLine()) != null) {
                code += line;
            }
            return code;
        } catch (Exception e) {
            return "";
        }
    }
    
    void setTestForDiffFiles() {
        for (Commit commit : pullRequest.getTragetCommits()) {
            CodeChange codeChange = commit.getCodeChange();
            for (DiffFile diffFile : codeChange.getDiffFiles()) {
                if (diffFile.isJavaFile()) {
                    for (FileChange fileChange : codeChange.getFileChanges()) {
                        if (diffFile.getChangeType() == fileChange.getChangeType()) {
                            if (diffFile.getChangeType() == PRElement.ADD) {
                                if (diffFile.getPathAfter().equals(fileChange.getPathAfter())) {
                                    diffFile.setTest(fileChange.isTest());
                                }
                            } else if (diffFile.getChangeType() == PRElement.DELETE) {
                                if (diffFile.getPathBefore().equals(fileChange.getPathBefore())) {
                                    diffFile.setTest(fileChange.isTest());
                                }
                            } else if (diffFile.getChangeType() == PRElement.REVISE) {
                                if (diffFile.getPathBefore().equals(fileChange.getPathBefore()) &&
                                    diffFile.getPathAfter().equals(fileChange.getPathAfter())) {
                                    diffFile.setTest(fileChange.isTest());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
