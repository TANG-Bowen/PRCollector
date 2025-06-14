/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

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
    
    private List<Exception> exceptions = new ArrayList<>();
    
    private final PullRequest pullRequest;
    private final File pullRequestDir;
    
    DiffBuilder(PullRequest pullRequest, File pullRequestDir) {
        this.pullRequest = pullRequest;
        this.pullRequestDir = pullRequestDir;
    }
    
    void build() {
        String workingPath = pullRequestDir.getAbsolutePath();
        String basePath = pullRequestDir.getAbsolutePath() + File.separator + "BaseSource";
        File dirBase = PRModelBundle.getDir(basePath);
        
        String C_cd_working = "cd " + workingPath;
        String C_cd_base = "cd " + basePath;
        String C_gitClone_base = "git clone " + pullRequest.getHeadRepositorySrcDLUrl() + " " + basePath; 
        String C_gitCheckoutBranch = "git checkout " + pullRequest.getHeadBranch();
        
        String cloneCommand = 
                C_cd_working + " ; " +
                C_gitClone_base + " ; " +
                C_cd_base + " ; " + 
                C_gitCheckoutBranch + " ; ";
        
        try {
            downloadSourceCode(cloneCommand);
            System.out.println("Download Ok!");
        } catch (CommitMissingException | IOException e) {
            pullRequest.setCommitRetrievable(false);
            exceptions.add(e);
        }
        
        for (Commit commit : pullRequest.getTargetCommits()) {
            String dirNameBefore = PRElement.BEFORE + "_" + commit.getShortSha();
            String pathBefore = pullRequestDir.getAbsolutePath() + File.separator + dirNameBefore;
            String dirNameAfter = PRElement.AFTER + "_" + commit.getShortSha();
            String pathAfter = pullRequestDir.getAbsolutePath() + File.separator + dirNameAfter;
            
            File dirBefore = PRModelBundle.getDir(pathBefore);
            File dirAfter = PRModelBundle.getDir(pathAfter);
            
            CodeChange codeChange = new CodeChange(pullRequest);
            codeChange.setCommit(commit);
            commit.setCodeChange(codeChange);
            
            try {
                build(commit, codeChange,
                        dirBefore.getAbsolutePath(), dirAfter.getAbsolutePath(), dirBase.getAbsolutePath());
                
                boolean hasJavaFile = codeChange.getDiffFiles().stream().anyMatch(f -> f.isJavaFile());
                codeChange.hasJavaFile(hasJavaFile);
            } catch (CommitMissingException | IOException e) {
                pullRequest.setCommitRetrievable(false);
                exceptions.add(e);
            }
        }
    }
    
    void build(Commit commit, CodeChange codeChange,
            String commitPathBefore, String commitPathAfter, String basePath)
            throws CommitMissingException, IOException {
        
        String C_cd_before  = "cd " + commitPathBefore;
        String C_cd_after   = "cd " + commitPathAfter;
        
        String C_gitCopy_before = "cp -R " +
                basePath + File.separator + "." + " " + commitPathBefore + File.separator;
        String C_gitCopy_after  = "cp -R " +
                basePath + File.separator + "." + " " + commitPathAfter + File.separator;
        
        String C_gitCheckoutCommit = "git checkout " + commit.getSha();
        
        String C_gitCheckoutPreviousCommit      = "git checkout " + "HEAD~" + " --";
        
        String C_gitDiff = "git diff " + commitPathBefore + " " + commitPathAfter;
        
        String copyCommand = 
                C_gitCopy_before + " ; " +
                C_gitCopy_after + " ; " ;
        
        String checkCommitCommand =
                C_cd_after          + " ; " +
                C_gitCheckoutCommit + " ; " +
                C_cd_before         + " ; " +
                C_gitCheckoutCommit + " ; " +
                C_gitCheckoutPreviousCommit;
        
        String diffCommand = C_gitDiff;
        
        copySourceCode(copyCommand);
        checkCommit(checkCommitCommand);
        
        String diffOutput = executeDiff(diffCommand);
        buildDiffFiles(pullRequest, codeChange, diffOutput, commitPathBefore, commitPathAfter);
    }
    
    void setTestForDiffFiles() {
        for (Commit commit : pullRequest.getTargetCommits()) {
            CodeChange codeChange = commit.getCodeChange();
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
    }
    
    List<Exception> getExceptions() {
        return exceptions;
    }
    
    static void downloadSourceCode(String command) throws CommitMissingException, IOException {
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
    
    static void copySourceCode(String command) throws CommitMissingException, IOException {
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
            throw new CommitMissingException("Copy error");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
    
    static void checkCommit(String commandChangeToCommit) throws CommitMissingException, IOException {
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
    
    static String executeDiff(String commandDiff) throws CommitMissingException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", commandDiff);
        
        BufferedReader reader = null;
        try {
            //processBuilder.inheritIO();
            Process process = processBuilder.start();
            StringBuilder diffOutput = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                //System.out.println("      " + line + "****************");
                
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
    
    static void buildDiffFiles(PullRequest pr, CodeChange codeChange, String diffOutput,
            String basePathBefore, String basePathAfter) {
        String[] diffs = diffOutput.split("diff --git ");
        if (diffs.length  > 1) {
            for (int i = 1; i < diffs.length; i++) {
                String diffUnit = diffs[i];
                String[] lines = diffUnit.split("\n");
                
                if (lines.length > 0 && lines[0] != null && !lines[0].contains("/.git/") && !lines[0].contains(".DS_Store")) {
                    String absolutePathBefore = "";
                    String absolutePathAfter = "";
                    
                    List<DiffLine> diffLines = new ArrayList<>();
                    
                    int plusCount = Integer.MAX_VALUE;
                    for (int j = 0; j < lines.length; j++) {
                        String[] line = lines[j].split(" "); 
                        if (line.length == 2 && line[0].equals("---") &&
                           !line[1].equals("/dev/null") && j < plusCount) {
                            String[] deleteLine = lines[j].split("---\\sa");
							if (deleteLine.length == 1) {
								deleteLine = lines[j].split("---\\s\"a");
							}
                            absolutePathBefore = deleteLine[1];
                        } else if (line.length == 2 && line[0].equals("+++") && j < plusCount) {
                            if (!line[1].equals("/dev/null")) {
								String[] addLine = lines[j].split("\\+\\+\\+\\sb");
								if (addLine.length == 1) {
									addLine = lines[j].split("\\+\\+\\+\\s\"b");
								}
                                absolutePathAfter = addLine[1];
                            }
                            plusCount = j;
                            
                        } else if (j > plusCount) {
			    if(lines[j].length()>0){
                            char firstC = lines[j].charAt(0);
                            if (firstC == '-') {
                                String text = lines[j].substring(1);
                                
                                DiffLine diffLine = new DiffLine(pr, PRElement.DELETE, text);
                                diffLines.add(diffLine);
                            } else if (firstC == '+') {
                                String text = lines[j].substring(1);
                                
                                DiffLine diffLine = new DiffLine(pr, PRElement.ADD, text);
                                diffLines.add(diffLine);
                            }
			    }
                        }
                    }
                    
                    String path = getRelativePath(absolutePathBefore, absolutePathAfter,
                            basePathBefore, basePathAfter);
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
                    
                    boolean hasJavaFile = path.endsWith(".java");
                    
                    DiffFile diffFile = new DiffFile(pr, changeType, path, bodyAll, bodyAdd, bodyDel,
                            sourceCodeBefore, sourceCodeAfter, hasJavaFile);
                    codeChange.getDiffFiles().add(diffFile);
                    
                    diffFile.getDiffLines().addAll(diffLines);
                    diffLines.forEach(d -> d.setDiffFile(diffFile));
                }
            }
        }
    }
    
    private static String getRelativePath(String pathBefore, String pathAfter,
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
    
    private static String getDiffBodyAll(List<DiffLine> diffLines) {
        StringBuilder text = new StringBuilder();
        for (DiffLine diffLine : diffLines) {
            text.append(diffLine.getText());
        }
        return text.toString();
    }
    
    private static String getDiffBodyAll(List<DiffLine> diffLines, String changeType) {
        StringBuilder text = new StringBuilder();
        for (DiffLine diffLine : diffLines) {
            if (diffLine.getChangeType() == changeType) {
                text.append(diffLine.getText());
            }
        }
        return text.toString();
    }
    
    private static String getChangeType(String absolutePathBefore, String absolutePathAfter) {
        if (absolutePathBefore.equals("") && !absolutePathAfter.equals("")) {
            return PRElement.ADD;
        } else if (!absolutePathBefore.equals("") && absolutePathAfter.equals("")) {
            return PRElement.DELETE;
        } else if (!absolutePathBefore.equals("") && !absolutePathAfter.equals("")) {
            return PRElement.REVISE;
        }
        return PRElement.NO_CHANGE;
    }
    
    private static String getCode(String absolutePath) {
        File file = new File(absolutePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line="";
            StringBuilder code = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                code.append(line);
            }
            return code.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
