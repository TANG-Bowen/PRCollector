package org.jtool.prmodel.builder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.jtool.jxp3model.ClassChange;
import org.jtool.jxp3model.FieldChange;
import org.jtool.jxp3model.FileChange;
import org.jtool.jxp3model.MethodChange;
import org.jtool.jxp3model.ProjectChange;
import org.jtool.jxp3model.CodeElement;

import org.jtool.prmodel.PRElement;
import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.CodeChange;
import org.jtool.prmodel.Commit;
import org.jtool.prmodel.DiffFile;
import org.jtool.srcmodel.JavaProject;
import org.jtool.srcmodel.JavaFile;
import org.jtool.srcmodel.JavaClass;
import org.jtool.srcmodel.JavaField;
import org.jtool.srcmodel.JavaMethod;

public class CodeChangetBuilder {
    
    private final PullRequest pullRequest;
    private final File pullRequestDir;
    
    private Map<String, ProjectInfo> existingProjects = new HashMap<>();
    
    private Map<ClassChange, JavaClass> classMapBefore = new HashMap<>();
    private Map<ClassChange, JavaClass> classMapAfter= new HashMap<>();
    private Map<FieldChange, JavaField> fieldMapBefore = new HashMap<>();
    private Map<FieldChange, JavaField> fieldMapAfter = new HashMap<>();
    private Map<MethodChange, JavaMethod> methodMapBefore = new HashMap<>();
    private Map<MethodChange, JavaMethod> methodMapAfter = new HashMap<>();
    
    private Map<JavaClass, CodeElement> classElemMapBefore = new HashMap<>();
    private Map<JavaClass, CodeElement> classElemMapAfter = new HashMap<>();
    private Map<JavaMethod, CodeElement> methodElemMapBefore = new HashMap<>();
    private Map<JavaMethod, CodeElement> methodElemMapAfter = new HashMap<>();
    
    public CodeChangetBuilder(PullRequest pullRequest, File pullRequestDir) {
        this.pullRequest = pullRequest;
        this.pullRequestDir = pullRequestDir;
    }
    
    public void build() throws IOException, CommitMissingException {
        for (Commit commit : pullRequest.getTragetCommits()) {
            String dirNameBefore = PRElement.BEFORE + "_" + commit.getShortSha();
            String pathBefore = pullRequestDir.getAbsolutePath() + File.separator + dirNameBefore;
            String dirNameAfter = PRElement.AFTER + "_" + commit.getShortSha();
            String pathAfter = pullRequestDir.getAbsolutePath() + File.separator + dirNameAfter;
            
            File dirBefore = new File(pathBefore);
            File dirAfter = new File(pathAfter);
            
            CodeChange codeChange = commit.getCodeChange();
            if (codeChange != null) {
                buildProjectChanges(codeChange, dirBefore.getAbsolutePath(), dirAfter.getAbsolutePath());
                codeChange.setFileChanges();
                
                setReferenceRelation(codeChange);
                setTest(codeChange);
            }
        }
    }
    
    private void buildProjectChanges(CodeChange codeChange, String basePathBefore, String basePathAfter) {
        for (DiffFile diffFile : codeChange.getDiffFiles()) {
            String projectName = "";
            String projectPath = "";
            String pathBefore = "";
            String pathAfter = "";
            
            if (containsPath(diffFile.getPath(), "src" + File.separator)) {
                String[] dirs = diffFile.getPath().split("src" + File.separator);
                projectName = dirs[0];
                projectPath = dirs[0] + "src";
                
                pathBefore = basePathBefore + File.separator + projectPath;
                pathAfter = basePathAfter + File.separator + projectPath;
                
            } else if (containsPath(diffFile.getPath(), "test")) {
                String[] dirs = diffFile.getPath().split("test" + File.separator);
                projectName = dirs[0];
                projectPath = dirs[0] + "test";
                
                pathBefore = basePathBefore + File.separator + projectPath;
                pathAfter = basePathAfter + File.separator + projectPath;
            }
            
            buildProjectChange(codeChange, diffFile.getChangeType(), projectName, projectPath, pathBefore, pathAfter);
        }
    }
    
    private boolean containsPath(String pathName, String target) {
        Path path = Paths.get(pathName);
        for (Path p : path) {
            if (p.toString().equals(target)) {
                return true;
            }
        }
        return false;
    }
    
    private void buildProjectChange(CodeChange codeChange, String changeType,
            String projectName, String projectPath, String pathBefore, String pathAfter) {
        ProjectInfo projectInfo = existingProjects.get(projectPath);
        if (projectInfo == null) {
            ProjectChange projectChange = new ProjectChange(pullRequest, projectName, projectPath);
            codeChange.getProjectChanges().add(projectChange);
            projectChange.setCodeChange(codeChange);
            
            JavaProject projectBefore = null;
            JavaProject projectAfter = null;
            if (changeType == PRElement.DELETE || changeType == PRElement.REVISE) {
                TinyModelBuilder modelBuilderBefore = new TinyModelBuilder();
                modelBuilderBefore.setVerbose(false);
                projectBefore = modelBuilderBefore.buildSingle(projectName, pathBefore);
            }
            if (changeType == PRElement.ADD || changeType == PRElement.REVISE) {
                TinyModelBuilder modelBuilderAfter = new TinyModelBuilder();
                modelBuilderAfter.setVerbose(false);
                projectAfter = modelBuilderAfter.buildSingle(projectName, pathAfter);
            }
            
            existingProjects.put(projectPath, new ProjectInfo(projectChange, projectBefore, projectAfter));
        }
        
        buildFileChanges(codeChange, projectInfo.projectChange, changeType,
                projectInfo.projectBefore, projectInfo.projectAfter);
    }
    
    private void buildFileChanges(CodeChange codeChange, ProjectChange projectChange, String changeType,
            JavaProject projectBefore, JavaProject projectAfter) {
        if (changeType == PRElement.DELETE) {
            for (JavaFile jfile : projectBefore.getFiles()) {
                FileChange fileChange = createFileDeleted(codeChange, jfile);
                projectChange.getFileChanges().add(fileChange);
            }
            
        } else if (changeType == PRElement.ADD) {
            for (JavaFile jfile : projectAfter.getFiles()) {
                FileChange fileChange = createFileAdded(codeChange, jfile);
                projectChange.getFileChanges().add(fileChange);
            }
            
        } else if (changeType == PRElement.REVISE) {
            Set<JavaFile> jfilesBefore = new HashSet<>(projectBefore.getFiles());
            Set<JavaFile> jfilesAfter = new HashSet<>(projectAfter.getFiles());
            
            for (DiffFile diffFile : codeChange.getDiffFiles()) {
                if (diffFile.getChangeType() == PRElement.DELETE) {
                    JavaFile jfile = getJavaFile(diffFile.getPath(), jfilesBefore);
                    if (jfile != null) {
                        FileChange fileChange = createFileDeleted(codeChange, jfile);
                        projectChange.getFileChanges().add(fileChange);
                    }
                } else if (diffFile.getChangeType() == PRElement.ADD) {
                    JavaFile jfile = getJavaFile(diffFile.getPath(), jfilesAfter);
                    if (jfile != null) {
                        FileChange fileChange = createFileAdded(codeChange, jfile);
                        projectChange.getFileChanges().add(fileChange);
                    }
                } else if (diffFile.getChangeType() == PRElement.REVISE) {
                    JavaFile jfileBefore = getJavaFile(diffFile.getPath(), jfilesBefore);
                    JavaFile jfileAfter = getJavaFile(diffFile.getPath(), jfilesAfter);
                    
                    if (jfileBefore != null && jfileAfter != null) {
                        FileChange fileChange = createFileChanged(codeChange, jfileBefore, jfileAfter);
                        projectChange.getFileChanges().add(fileChange);
                        
                    } else if (jfileBefore != null && jfileAfter == null) {
                        System.out.println("JavaFile Before : "+ jfileBefore.getPath());
                        System.out.println("JavaFile After : null");
                    } else if (jfileBefore == null && jfileAfter != null) {
                        System.out.println("JavaFile Before : null");
                        System.out.println("JavaFile After : "+ jfileAfter.getPath());
                    }
                }
            }
        }
    }
    
    private FileChange createFileDeleted(CodeChange codeChange, JavaFile jfile) {
        String name = jfile.getName();
        String path = jfile.getPath();
        String sourceCodeBefore = jfile.getSource();
        String sourceCodeAfter = "";
        
        FileChange fileChange = new FileChange(pullRequest, PRElement.DELETE,
                name, path, sourceCodeBefore, sourceCodeAfter);
        
        for (JavaClass jclass : jfile.getClasses()) {
            ClassChange classChange = createClassDeleted(codeChange, jclass);
            
            fileChange.getClassChanges().add(classChange);
            classChange.setFileChange(fileChange);
        }
        
        return fileChange;
    }
    
    private FileChange createFileAdded(CodeChange codeChange, JavaFile jfile) {
        String name = jfile.getName();
        String path = jfile.getPath();
        String sourceCodeBefore = "";
        String sourceCodeAfter = jfile.getSource();
        
        FileChange fileChange = new FileChange(pullRequest, PRElement.ADD,
                name, path, sourceCodeBefore, sourceCodeAfter);
        
        for (JavaClass jclass : jfile.getClasses()) {
            ClassChange classChange = createClassAdded(codeChange, jclass);
            
            fileChange.getClassChanges().add(classChange);
            classChange.setFileChange(fileChange);
        }
        
        return fileChange;
    }
    
    private FileChange createFileChanged(CodeChange codeChange, JavaFile jfileBefore, JavaFile jfileAfter) {
        String name = jfileAfter.getName();
        String path = jfileAfter.getPath();;
        String sourceCodeBefore = jfileBefore.getSource();
        String sourceCodeAfter = jfileAfter.getSource();
        
        FileChange fileChange = new FileChange(pullRequest, PRElement.REVISE,
                name, path, sourceCodeBefore, sourceCodeAfter);
        
        Set<JavaClass> beforeClasses = new HashSet<>(jfileBefore.getClasses());
        Set<JavaClass> afterClasses = new HashSet<>(jfileAfter.getClasses());
        
        if (beforeClasses.size() > 0 || afterClasses.size() > 0) {
            for (JavaClass jclass : beforeClasses) {
                if (!afterClasses.contains(jclass)) {
                    ClassChange classChange = createClassDeleted(codeChange, jclass);
                    
                    fileChange.getClassChanges().add(classChange);
                    classChange.setFileChange(fileChange);
                }
            }
            for (JavaClass jclass : afterClasses) {
                if (!beforeClasses.contains(jclass)) {
                    ClassChange classChange = createClassAdded(codeChange, jclass);
                    
                    fileChange.getClassChanges().add(classChange);
                    classChange.setFileChange(fileChange);
                }
            }
            for (JavaClass jclassBefore : beforeClasses) {
                for (JavaClass jclassAfter : afterClasses) {
                    if (jclassBefore.equals(jclassAfter) &&
                            !jclassBefore.getSource().equals(jclassAfter.getSource())) {
                        ClassChange classChange = createClassChanged(codeChange, jclassBefore, jclassAfter);
                        
                        fileChange.getClassChanges().add(classChange);
                        classChange.setFileChange(fileChange);
                    }
                }
            }
        }
        return fileChange;
    }
    
    private ClassChange createClassDeleted(CodeChange codeChange, JavaClass jclass) {
        String name = jclass.getName();
        String qualifiedName = jclass.getQualifiedName().fqn();
        String type = jclass.getDeclaringClass().getQualifiedName().fqn();
        String sourceCodeBefore = jclass.getSource();
        String sourceCodeAfter = "";
        
        ClassChange classChange = new ClassChange(pullRequest, PRElement.DELETE,
                name, qualifiedName, type, sourceCodeBefore, sourceCodeAfter);
        classMapBefore.put(classChange, jclass);
        classElemMapBefore.put(jclass, classChange.getCodeElementBefore());
        
        for (JavaField jfield : jclass.getFields()) {
            FieldChange fieldChange = createFieldDeleted(jfield);
            
            classChange.getFieldChanges().add(fieldChange);
            fieldChange.setClassChange(classChange);
        }
        for (JavaMethod jmethod : jclass.getMethods()) {
            MethodChange methodChange = createMethodDeleted(jmethod);
            
            classChange.getMethodChanges().add(methodChange);
            methodChange.setClassChange(classChange);
        }
        
        return classChange;
    }
    
    private ClassChange createClassAdded(CodeChange codeChange, JavaClass jclass) {
        String name = jclass.getName();
        String qualifiedName = jclass.getQualifiedName().fqn();
        String type = jclass.getDeclaringClass().getQualifiedName().fqn();
        String sourceCodeBefore = "";
        String sourceCodeAfter =  jclass.getSource();
        
        ClassChange classChange = new ClassChange(pullRequest, PRElement.ADD,
                name, qualifiedName, type, sourceCodeBefore, sourceCodeAfter);
        classMapAfter.put(classChange, jclass);
        classElemMapAfter.put(jclass, classChange.getCodeElementAfter());
        
        for (JavaField jfield : jclass.getFields()) {
            FieldChange fieldChange = createFieldAdded(jfield);
            
            classChange.getFieldChanges().add(fieldChange);
            fieldChange.setClassChange(classChange);
        }
        for (JavaMethod jmethod : jclass.getMethods()) {
            MethodChange methodChange = createMethodAdded(jmethod);
            
            classChange.getMethodChanges().add(methodChange);
            methodChange.setClassChange(classChange);
        }
        
        return classChange;
    }
    
    private ClassChange createClassChanged(CodeChange codeChange, JavaClass jclassBefore, JavaClass jclassAfter) {
        String name = jclassAfter.getName();
        String qualifiedName = jclassAfter.getQualifiedName().fqn();
        String type = jclassAfter.getDeclaringClass().getQualifiedName().fqn();
        String sourceCodeBefore = jclassBefore.getSource();
        String sourceCodeAfter =  jclassAfter.getSource();
        
        ClassChange classChange = new ClassChange(pullRequest, PRElement.REVISE,
                name, qualifiedName, type, sourceCodeBefore, sourceCodeAfter);
        classMapBefore.put(classChange, jclassBefore);
        classElemMapBefore.put(jclassBefore, classChange.getCodeElementBefore());
        classMapAfter.put(classChange, jclassAfter);
        classElemMapAfter.put(jclassAfter, classChange.getCodeElementAfter());
        
        Set<JavaField> beforeFields = new HashSet<>(jclassBefore.getFields());
        Set<JavaField> afterFields = new HashSet<>(jclassAfter.getFields());
        
        if (beforeFields.size() > 0 || afterFields.size() > 0) {
            for (JavaField jfield : beforeFields) {
                if (!afterFields.contains(jfield)) {
                    FieldChange fieldChange = createFieldDeleted(jfield);
                    
                    classChange.getFieldChanges().add(fieldChange);
                    fieldChange.setClassChange(classChange);
                }
            }
            for (JavaField jfield : afterFields) {
                if (!beforeFields.contains(jfield)) {
                    FieldChange fieldChange = createFieldAdded(jfield);
                    
                    classChange.getFieldChanges().add(fieldChange);
                    fieldChange.setClassChange(classChange);
                }
            }
            
            for (JavaField jfieldBefore : beforeFields) {
                for (JavaField jfieldAfter : afterFields) {
                    if (jfieldBefore.equals(jfieldAfter) &&
                            !jfieldBefore.getSource().equals(jfieldAfter.getSource())) {
                        FieldChange fieldChange = createFieldChanged(jfieldBefore, jfieldAfter);
                        
                        classChange.getFieldChanges().add(fieldChange);
                        fieldChange.setClassChange(classChange);
                    }
                }
            }
        }
        
        Set<JavaMethod> beforeMethods = new HashSet<>(jclassBefore.getMethods());
        Set<JavaMethod> afterMethods = new HashSet<>(jclassBefore.getMethods());
        
        if (beforeMethods.size() > 0 || afterMethods.size() > 0) {
            for (JavaMethod jmethod : beforeMethods) {
                if (!afterMethods.contains(jmethod)) {
                    MethodChange methodChange = createMethodDeleted(jmethod);
                    
                    classChange.getMethodChanges().add(methodChange);
                    methodChange.setClassChange(classChange);
                }
            }
            for (JavaMethod jmethod : afterMethods) {
                if (!beforeMethods.contains(jmethod)) {
                    MethodChange methodChange = createMethodAdded(jmethod);
                    
                    classChange.getMethodChanges().add(methodChange);
                    methodChange.setClassChange(classChange);
                }
            }
            for (JavaMethod jmethodBefore : beforeMethods) {
                for (JavaMethod jmethodAfter : afterMethods) {
                    if (jmethodBefore.equals(jmethodAfter) &&
                            !jmethodBefore.getSource().equals(jmethodAfter.getSource())) {
                        MethodChange methodChange = createMethodChanged(jmethodBefore, jmethodAfter);
                        
                        classChange.getMethodChanges().add(methodChange);
                        methodChange.setClassChange(classChange);
                    }
                }
            }
        }
        return classChange;
    }
    
    private JavaFile getJavaFile(String path , Set<JavaFile> jfiles) {
        return jfiles.stream().filter(f -> f.getPath().equals(path)).findFirst().orElse(null);
    }
    
    private FieldChange createFieldDeleted(JavaField jfield) {
        String name = jfield.getName();
        String qualifiedName = jfield.getQualifiedName().fqn();
        String type = jfield.getType();
        String sourceCodeBefore = jfield.getSource();
        String sourceCodeAfter = "";
        
        FieldChange fieldChange = new FieldChange(pullRequest, PRElement.DELETE,
                name, qualifiedName, type, sourceCodeBefore, sourceCodeAfter);
        fieldMapBefore.put(fieldChange, jfield);
        
        return fieldChange;
    }
    
    private FieldChange createFieldAdded(JavaField jfield) {
        String name = jfield.getName();
        String qualifiedName = jfield.getQualifiedName().fqn();
        String type = jfield.getType();
        String sourceCodeBefore = "";
        String sourceCodeAfter = jfield.getSource();
        
        FieldChange fieldChange = new FieldChange(pullRequest, PRElement.ADD,
                name, qualifiedName, type, sourceCodeBefore, sourceCodeAfter);
        fieldMapAfter.put(fieldChange, jfield);
        
        return fieldChange;
    }
    
    private FieldChange createFieldChanged(JavaField jfieldBefore, JavaField jfieldAfter) {
        String name = jfieldAfter.getName();
        String qualifiedName = jfieldAfter.getQualifiedName().fqn();
        String type = jfieldAfter.getType();
        String sourceCodeBefore =  jfieldBefore.getSource();
        String sourceCodeAfter = jfieldAfter.getSource();
        
        FieldChange fieldChange = new FieldChange(pullRequest, PRElement.REVISE,
                name, qualifiedName, type, sourceCodeBefore, sourceCodeAfter);
        fieldMapBefore.put(fieldChange, jfieldBefore);
        fieldMapAfter.put(fieldChange, jfieldAfter);
        
        return fieldChange;
    }
    
    private MethodChange createMethodDeleted(JavaMethod jmethod) {
        String name = jmethod.getName();
        String qualifiedName = jmethod.getQualifiedName().fqn();
        String type = jmethod.getReturnType();
        String sourceCodeBefore = jmethod.getSource();
        String sourceCodeAfter = "";
        
        MethodChange methodChange = new MethodChange(pullRequest, PRElement.DELETE,
                name, qualifiedName, type, sourceCodeBefore, sourceCodeAfter);
        methodChange.setTest(isTestMethod(jmethod));
        methodMapBefore.put(methodChange, jmethod);
        methodElemMapBefore.put(jmethod, methodChange.getCodeElementBefore());
        
        return methodChange;
    }
    
    private MethodChange createMethodAdded(JavaMethod jmethod) {
        String name = jmethod.getName();
        String qualifiedName = jmethod.getQualifiedName().fqn();
        String type = jmethod.getReturnType();
        String sourceCodeBefore = jmethod.getSource();
        String sourceCodeAfter = "";
        
        MethodChange methodChange = new MethodChange(pullRequest, PRElement.ADD,
                name, qualifiedName, type, sourceCodeBefore, sourceCodeAfter);
        methodChange.setTest(isTestMethod(jmethod));
        methodMapAfter.put(methodChange, jmethod);
        methodElemMapAfter.put(jmethod, methodChange.getCodeElementAfter());
        
        return methodChange;
    }
    
    private MethodChange createMethodChanged(JavaMethod jmethodBefore, JavaMethod jmethodAfter) {
        String name = jmethodAfter.getName();
        String qualifiedName = jmethodAfter.getQualifiedName().fqn();
        String type = jmethodAfter.getReturnType();
        String sourceCodeBefore = jmethodBefore.getSource();
        String sourceCodeAfter = jmethodAfter.getSource();
        
        MethodChange methodChange = new MethodChange(pullRequest, PRElement.ADD,
                name, qualifiedName, type, sourceCodeBefore, sourceCodeAfter);
        methodChange.setTest(isTestMethod(jmethodBefore) || isTestMethod(jmethodAfter));
        methodMapBefore.put(methodChange, jmethodBefore);
        methodElemMapBefore.put(jmethodBefore, methodChange.getCodeElementBefore());
        methodMapAfter.put(methodChange, jmethodAfter);
        methodElemMapAfter.put(jmethodAfter, methodChange.getCodeElementAfter());
        
        return methodChange;
    }
    
    private boolean isTestMethod(JavaMethod jmethod) {
        JavaClass jclass = jmethod.getDeclaringClass().getSuperClass();
        if (jclass != null) {
            if (jclass.getQualifiedName().fqn().equals("junit.framework.TestCase")) {
                return true;
            }
        }
        
        String anno = getAnnotation(jmethod);
        return "Test".equals(anno);
    }
    
    private String getAnnotation(JavaMethod jmethod) {
        ASTNode node = jmethod.getASTNode();
        if (node instanceof MethodDeclaration) {
            MethodDeclaration methodDecl = (MethodDeclaration)node;
            for (Object obj : methodDecl.modifiers()) {
                IExtendedModifier mod = (IExtendedModifier)obj;
                if (mod.isAnnotation()) {
                    Annotation anno = (Annotation)mod;
                    Name name = anno.getTypeName();
                    return name.getFullyQualifiedName();
                }
            }
        }
        return "";
    }
    
    private void setReferenceRelation(CodeChange codeChange) {
        for (FileChange fileChange : codeChange.getFileChanges()) {
            for (ClassChange classChange : fileChange.getClassChanges()) {
                JavaClass jclassBefore = classMapBefore.get(classChange);
                if (jclassBefore != null) {
                    Set<JavaClass> classes = jclassBefore.getAfferentClassesInProject();
                    Set<CodeElement> elems = getClassElements(classes, classElemMapBefore);
                    classChange.getAfferentClassesBefore().addAll(elems);
                }
                
                JavaClass jclassAfter = classMapAfter.get(classChange);
                if (jclassAfter != null) {
                    Set<JavaClass> classes = jclassAfter.getAfferentClassesInProject();
                    Set<CodeElement> elems = getClassElements(classes, classElemMapAfter);
                    classChange.getAfferentClassesAfter().addAll(elems);
                }
                
                for (FieldChange fieldChange : classChange.getFieldChanges()) {
                    JavaField jfieldBefore = fieldMapBefore.get(fieldChange);
                    if (jfieldBefore != null) {
                        Set<JavaMethod> callingMethods = jfieldBefore.getAccessingMethodsInProject();
                        Set<CodeElement> callingElems = getMethodElements(callingMethods, methodElemMapBefore);
                        fieldChange.getCallingMethodsBefore().addAll(callingElems);
                        
                        Set<JavaMethod> calledMethods = jfieldBefore.getCalledMethodsInProject();
                        Set<CodeElement> calledelems = getMethodElements(calledMethods, methodElemMapBefore);
                        fieldChange.getCalledMethodsBefore().addAll(calledelems);
                    }
                    
                    JavaField jfieldAfter = fieldMapAfter.get(fieldChange);
                    if (jfieldBefore != null) {
                        Set<JavaMethod> callingMethods = jfieldAfter.getAccessingMethodsInProject();
                        Set<CodeElement> callingElems = getMethodElements(callingMethods, methodElemMapAfter);
                        fieldChange.getCallingMethodsAfter().addAll(callingElems);
                        
                        Set<JavaMethod> calledMethods = jfieldAfter.getCalledMethodsInProject();
                        Set<CodeElement> calledelems = getMethodElements(calledMethods, methodElemMapAfter);
                        fieldChange.getCalledMethodsAfter().addAll(calledelems);
                    }
                }
                
                for (MethodChange methodChange : classChange.getMethodChanges()) {
                    JavaMethod jmethodBefore = methodMapBefore.get(methodChange);
                    if (jmethodBefore != null) {
                        Set<JavaMethod> callingMethods = jmethodBefore.getCallingMethodsInProject();
                        Set<CodeElement> callingElems = getMethodElements(callingMethods, methodElemMapBefore);
                        methodChange.getCallingMethodsBefore().addAll(callingElems);
                        
                        Set<JavaMethod> calledMethods = jmethodBefore.getCalledMethodsInProject();
                        Set<CodeElement> calledelems = getMethodElements(calledMethods, methodElemMapBefore);
                        methodChange.getCalledMethodsBefore().addAll(calledelems);
                    }
                    
                    JavaMethod jmethodAfter = methodMapAfter.get(methodChange);
                    if (jmethodAfter != null) {
                        Set<JavaMethod> callingMethods = jmethodAfter.getCallingMethodsInProject();
                        Set<CodeElement> callingElems = getMethodElements(callingMethods, methodElemMapAfter);
                        methodChange.getCallingMethodsAfter().addAll(callingElems);
                        
                        Set<JavaMethod> calledMethods = jmethodAfter.getCalledMethodsInProject();
                        Set<CodeElement> calledelems = getMethodElements(calledMethods, methodElemMapAfter);
                        methodChange.getCalledMethodsAfter().addAll(calledelems);
                    }
                }
            }
        }
    }
    
    private Set<CodeElement> getClassElements(Set<JavaClass> classes, Map<JavaClass, CodeElement> classElemMap) {
        Set<CodeElement> elems = new HashSet<>();
        for (JavaClass jclass : classes) {
            CodeElement codeElem = classElemMap.get(jclass);
            if (codeElem != null) {
                elems.add(codeElem);
            }
        }
        return elems;
    }
    
    private Set<CodeElement> getMethodElements(Set<JavaMethod> methods, Map<JavaMethod, CodeElement> methodElemMap) {
        Set<CodeElement> elems = new HashSet<>();
        for (JavaMethod jmethod : methods) {
            CodeElement codeElem = methodElemMap.get(jmethod);
            if (codeElem != null) {
                elems.add(codeElem);
            }
        }
        return elems;
    }
    
    private void setTest(CodeChange codeChange) {
        for (FileChange fileChange : codeChange.getFileChanges()) {
            fileChange.setTest(false);
            for (ClassChange classChange : fileChange.getClassChanges()) {
                boolean isTest = classChange.getMethodChanges().stream().anyMatch(m -> m.isTest());
                classChange.setTest(isTest);
                if (isTest) {
                    fileChange.setTest(true);
                }
            }
        }
    }
    
    private class ProjectInfo {
        ProjectChange projectChange;
        JavaProject projectBefore;
        JavaProject projectAfter;
        
        ProjectInfo(ProjectChange projectChange, JavaProject projectBefore, JavaProject projectAfter) {
            this.projectChange = projectChange;
            this.projectBefore =  projectBefore;
            this.projectAfter = projectAfter;
        }
    }
}
