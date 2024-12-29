package org.jtool.jwrmodel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.google.gson.Gson;

import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.DeficientPullRequest;
import org.jtool.prmodel.PRElement;

public class JsonFileWriter {
    
    private PullRequest pullRequest;
    private Str_PullRequest strPullRequest;
    
    private DeficientPullRequest deficientPullRequest;
    private Str_DeficientPullRequest strDeficientPullRequest;
    
    private  StringConverter strBuilder;
    
    private  File outputFile;
    
    public JsonFileWriter(PullRequest pullRequest, String outputFilePath) {
        this.pullRequest = pullRequest;
        this.outputFile = new File(outputFilePath);
        
        this.strBuilder = new StringConverter();
        this.strPullRequest = strBuilder.buildPullRequest(pullRequest);
    }
    
    public JsonFileWriter(DeficientPullRequest deficientPullRequest, String outputFilePath) {
        this.deficientPullRequest = deficientPullRequest;
        this.outputFile = new File(outputFilePath);
        
        this.strBuilder = new StringConverter();
        this.strDeficientPullRequest = strBuilder.buildDeficientPullRequest(deficientPullRequest);
    }
    
    public void writePRModel() {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(strPullRequest);
        
        try (FileWriter writer = new FileWriter(outputFile, false)) {
            writer.write(jsonStr);
            System.out.println("Succeeded to write PR "+ pullRequest.getId() + " into a json file !");
        } catch (IOException e) {
            System.err.println("Could not write " + outputFile);
        }
    }
    
    public void writePRModelWithDataLoss() {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(strDeficientPullRequest);
        
        try(FileWriter writer = new FileWriter(outputFile, false)){
            writer.write(jsonStr);
            System.out.println("Succeeded to write Deficient PR "+ deficientPullRequest.getId() + " into a json file !");
        }catch(IOException e) {
            System.err.println("Could not write " + outputFile);
        }
    }
    
    public static void deleteGitSourceFile(PullRequest pullRequest, File pullRequestDir) {
        for (Commit commit : pullRequest.getTragetCommits()) {
            String dirNameBefore = PRElement.BEFORE + "_" + commit.getShortSha();
            String pathBefore = pullRequestDir.getAbsolutePath() + File.separator + dirNameBefore; 
            String dirNameAfter = PRElement.AFTER + "_" + commit.getShortSha();
            String pathAfter = pullRequestDir.getAbsolutePath() + File.separator + dirNameAfter;
            
            deleteFiles(pathBefore, true);
            deleteFiles(pathAfter, true);
        }
    }
    
    public static void deleteFiles(File file) {
        Path path = Paths.get(file.getAbsolutePath());
        //deleteFiles(path);
        deleteDir(path);
        boolean flag;
        if(file.listFiles().length==0)
        {
        	flag = true;
        }else {
        	flag = false;
        }
        System.out.println("delete task finished : "+flag);
    }
    
    public static void deleteFiles(String pathStr, boolean deleteCurrentDir) {
        File currentDir = new File(pathStr);
        if (currentDir.exists() && currentDir.isDirectory()) {
            //File[] files = currentDir.listFiles();
            deleteSubdirectories(currentDir);
            
            if(deleteCurrentDir) {
                currentDir.delete();
            }
        } else {
            System.out.println("Not valid directory to delete! ");
        }
    }
    
    static void deleteSubdirectories(File sourceDir) {
        File[] files = sourceDir.listFiles();
        if (files!=null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        } else {
            sourceDir.delete();
        }
    }
    
    static void deleteDirectory(File sourceDir) {
        File[] files = sourceDir.listFiles();
        if (files !=null) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                } else if(file.isDirectory()) {
                    deleteDirectory(file);
                }
            }
        }
        sourceDir.delete();
    }
    
    public static void deleteDir(Path dir) {
        try {
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
        } catch(IOException e) {
            System.out.println(e);
        }
    }
}
