package org.jtool.prmodel.builder;

import org.jtool.jxplatform.project.ModelBuilderImpl;
import org.jtool.srcmodel.JavaProject;
import org.jtool.srcmodel.JavaFile;
import org.jtool.srcmodel.JavaClass;
import org.jtool.srcmodel.internal.JavaASTVisitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

class TinyModelBuilderImpl extends ModelBuilderImpl {
    
    TinyModelBuilderImpl() {
    }
    
    void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    
    JavaProject buildSingle(TinyModelBuilder modelBuilder, String name, String srcpath) {
        String[] srcpaths = new String[1];
        srcpaths[0] = srcpath;
        return buildSingle(modelBuilder, name, srcpaths);
    }
    
    JavaProject buildSingle(TinyModelBuilder modelBuilder, String name, String[] srcpath) {
        JavaProject jproject = createProject(modelBuilder, name, srcpath[0], srcpath);
        build(jproject);
        return jproject;
    }
    
    private JavaProject createProject(TinyModelBuilder modelBuilder, String name, String path, String[] srcpath) {
        JavaProject jproject = new JavaProject(name, path, path);
        jproject.setModelBuilder(modelBuilder);
        jproject.setSourceBinaryPaths(srcpath, srcpath);
        jproject.setClassPath(srcpath);
        return jproject;
    }
    
    @Override
    public void collectInfo(JavaProject jproject, List<JavaClass> classes) {
        int count = 0;
        for (JavaClass jclass : classes) {
            jproject.collectInfo(jclass);
            
            count++;
            logger.recordLog("-Built " + jclass.getQualifiedName() + " (" + count + "/" + classes.size() + ")");
        }
    }
    

    @Override
    public void parseFile(JavaProject jproject, List<File> sourceFiles) {
        List<FileContent> fileContents = new ArrayList<>();
        for (File file : sourceFiles) {
            try {
                String path = file.getCanonicalPath();
                String source = read(file);
                if (!source.startsWith("#")) {
                    FileContent fileContent = new FileContent(path, source);
                    fileContents.add(fileContent);
                }
            } catch (IOException e) {
                printError("Cannot read file " + file.getPath());
            }
        }
        parse(jproject, fileContents);
    }
    
    private String read(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            char[] buf = new char[128];
            int count = 0;
            while ((count = reader.read(buf)) != -1) {
                String data = String.valueOf(buf, 0, count);
                content.append(data);
                buf = new char[1024];
            }
        }
        return content.toString();
    }
    
    private void parse(JavaProject jproject, List<FileContent> fileContents) {
        if (fileContents.size() > 0) {
            String[] paths = new String[fileContents.size()];
            String[] encodings = new String[fileContents.size()];
            Map<String, String> sources = new HashMap<>();
            Map<String, String> charsets = new HashMap<>();
            
            int count = 0;
            for (FileContent fileContent : fileContents) {
                String path = fileContent.getPath();
                String source = fileContent.getSource();
                String charset = StandardCharsets.UTF_8.name();
                paths[count] = path;
                encodings[count] = charset;
                sources.put(path, source);
                charsets.put(path, charset);
                count++;
            }
            
            parse(jproject, paths, encodings, sources, charsets);
        } else {
            printError("Found no Java source files in " + jproject.getPath());
        }
    }
    
    private void parse(JavaProject jproject, String[] paths, String[] encodings,
            Map<String, String> sources, Map<String, String> charsets) {
        final int size = paths.length;
        
        FileASTRequestor requestor = new FileASTRequestor() {
            private int count = 0;
            
            @Override
            public void acceptAST(String filepath, CompilationUnit cu) {
                JavaFile jfile = new JavaFile(cu, filepath, sources.get(filepath), charsets.get(filepath), jproject);
                JavaASTVisitor visitor = new JavaASTVisitor(jfile);
                cu.accept(visitor);
                visitor.terminate();
                
                count++;
                logger.recordLog("-Parsed " +
                        filepath.substring(jproject.getPath().length() + 1) + " (" + count + "/" + size + ")");
            }
        };
        
        try {
            ASTParser parser = getParser(jproject);
            parser.setEnvironment(jproject.getClassPath(), jproject.getSourcePath(), null, true);
            parser.createASTs(paths, encodings, new String[]{ }, requestor, null);
        } catch (IllegalArgumentException | IllegalStateException e) {
            /* Skip parsing */
        }
    }
    
    private class FileContent {
        
        private String path;
        private String source;
        
        FileContent(String path, String source) {
            this.path = path;
            this.source = source;
        }
        
        String getPath() {
            return path;
        }
        
        String getSource() {
            return source;
        }
    }
}
