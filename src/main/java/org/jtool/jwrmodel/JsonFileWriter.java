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
    
    public void deleteGitSourceFile(PullRequest pullRequest) {
        for (Commit commit : pullRequest.getCommits()) {
            if (commit.getDiff() != null) {
                File fileBefore = new File(commit.getDiff().getSourceCodePathBefore());
                deleteGitSourceFiles(fileBefore);
                
                File fileAfter = new File(commit.getDiff().getSourceCodePathAfter());
                deleteGitSourceFiles(fileAfter);
            } else {
                System.out.println("No ned to delete Git source files for "+ commit.getSha());
            }
        }
    }
    
    private void deleteGitSourceFiles(File file) {
        Path path = Paths.get(file.getAbsolutePath());
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                
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
            System.out.println("Successed to delete Git Source files !");
            
        } catch (IOException e) {
            /* empty */
        }
    }
    
    public void deleteRetainPullRequestFiles(Path path) {
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
