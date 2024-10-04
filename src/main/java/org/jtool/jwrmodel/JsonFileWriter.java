package org.jtool.jwrmodel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.google.gson.Gson;

import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.PRModelBundle;

public class JsonFileWriter {
    
    private final PullRequest pullRequest;
    
    private final StringConverter strBuilder;
    private final Str_PullRequest strPullRequest;
    
    private final File outputFile;
    
    public JsonFileWriter(PullRequest pullRequest, String outputFilePath) {
        this.pullRequest = pullRequest;
        this.outputFile = new File(outputFilePath);
        
        this.strBuilder = new StringConverter(pullRequest);
        this.strPullRequest = strBuilder.buildPullRequest();
    }
    
    public void write() {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(strPullRequest);
        
        try (FileWriter writer = new FileWriter(outputFile, false)) {
            writer.write(jsonStr);
            System.out.println("Succeeded to write PR "+ pullRequest.getId() + " into a json file !");
            
        } catch (IOException e) {
            System.err.println("Could not write " + outputFile);
        }
    }
    
    public void deleteGitSourceFile(PullRequest pullRequest, File pullRequestDir) {
        for (Commit commit : pullRequest.getTragetCommits()) {
            String dirNameBefore = PRElement.BEFORE + "_" + commit.getShortSha();
            String pathBefore = pullRequestDir.getAbsolutePath() + File.separator + dirNameBefore; 
            String dirNameAfter = PRElement.AFTER + "_" + commit.getShortSha();
            String pathAfter = pullRequestDir.getAbsolutePath() + File.separator + dirNameAfter;
            
            File dirBefore = PRModelBundle.getDir(pathBefore);
            File dirAfter = PRModelBundle.getDir(pathAfter);
            
            deleteFiles(dirBefore);
            deleteFiles(dirAfter);
        }
    }
    
    private void deleteFiles(File file) {
        Path path = Paths.get(file.getAbsolutePath());
        deleteFiles(path);
    }
    
    public void deleteFiles(Path path) {
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                     if (!dir.equals(path)) {
                         Files.delete(dir);
                     } else {
                         try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                             for (Path entry : stream) {
                                 Files.delete(entry);
                             }
                         } catch (DirectoryNotEmptyException e) {
                             return FileVisitResult.CONTINUE;
                         }
                     }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            /* empty */
        }
    }
}
