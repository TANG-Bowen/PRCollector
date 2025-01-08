package org.jtool.prmodel.builder;

import java.io.File;
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

public class CodeChangeBuilder {
    
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
    
    public CodeChangeBuilder(PullRequest pullRequest, File pullRequestDir) {
        this.pullRequest = pullRequest;
        this.pullRequestDir = pullRequestDir;
    }
    
    public void build() {
        for (Commit commit : pullRequest.getTargetCommits()) {
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
                setTestForClasses(codeChange);
            }
            existingProjects.clear();
            classMapBefore.clear();
            classMapAfter.clear();
            fieldMapBefore.clear();
            fieldMapAfter.clear();
            methodMapBefore.clear();
            methodMapAfter.clear();
            classElemMapBefore.clear();
            classElemMapAfter.clear();
            methodElemMapBefore.clear();
            methodElemMapAfter.clear();
        }
    }
    
    public void buildProjectChanges(CodeChange codeChange, String basePathBefore, String basePathAfter) {
        for (DiffFile diffFile : codeChange.getDiffFiles()) {
            String projectName = "";
            String projectPath = "";
            String pathBefore = "";
            String pathAfter = "";
            
            if (containsPath(diffFile.getPath(), "src") && diffFile.isJavaFile()) {
                String[] dirs = diffFile.getPath().split("src" + File.separator);
                projectName = dirs[0];
                projectPath = dirs[0] + "src";
                
                pathBefore = basePathBefore + File.separator + projectPath;
                pathAfter = basePathAfter + File.separator + projectPath;
                
            } else if (containsPath(diffFile.getPath(), "test") && diffFile.isJavaFile()) {
                String[] dirs = diffFile.getPath().split("test" + File.separator);
                projectName = dirs[0];
                projectPath = dirs[0] + "test";
                
                pathBefore = basePathBefore + File.separator + projectPath;
                pathAfter = basePathAfter + File.separator + projectPath;
            }
            
            if (projectPath != "") {
                File fb = new File(pathBefore);
                File fa = new File(pathAfter);
                if (fb.exists() && !fa.exists()) {
                    System.out.println("Project build(delete)   " + fb.getAbsolutePath());
                    buildProjectChange(codeChange, PRElement.DELETE,
                            projectName, projectPath, pathBefore, pathAfter);
                } else if (!fb.exists() && fa.exists()) {
                    System.out.println("Project build(add)   " + fa.getAbsolutePath());
                    buildProjectChange(codeChange, PRElement.ADD,
                            projectName, projectPath, pathBefore, pathAfter);
                } else if (fb.exists() && fa.exists()) {
                    System.out.println("Project build(revise-before)   " + fb.getAbsolutePath() );
                    System.out.println("Project build(revise-after)   " + fa.getAbsolutePath() );
                    buildProjectChange(codeChange, PRElement.REVISE,
                            projectName, projectPath, pathBefore, pathAfter);
                }else {
                    System.out.println("No input path for code change building !");
                }
            }
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
        projectInfo = existingProjects.get(projectPath);
        buildFileChanges(codeChange, projectInfo.projectChange, changeType,
                projectInfo.projectBefore, projectInfo.projectAfter);
    }
    
    private void buildFileChanges(CodeChange codeChange, ProjectChange projectChange, String changeType,
            JavaProject projectBefore, JavaProject projectAfter) {
        if (changeType == PRElement.DELETE) {
        	Set<JavaFile> jfilesBefore = new HashSet<>(projectBefore.getFiles());
        	for(DiffFile diffFile : codeChange.getDiffFiles()) {
        		if(diffFile.getChangeType() == PRElement.DELETE && diffFile.isJavaFile()) {
        			JavaFile jfile = getJavaFile(diffFile.getPath(), jfilesBefore);
        			if(jfile!=null && !inBuiltFiles(jfile,projectChange)) {
        				FileChange fileChange = createFileDeleted(codeChange, jfile);
                        projectChange.getFileChanges().add(fileChange);                       
                        fileChange.setTest(containsTestMethod(jfile));
        			}
        		}
        	}  
        } else if (changeType == PRElement.ADD) {
        	Set<JavaFile> jfilesAfter = new HashSet<>(projectAfter.getFiles());
        	for(DiffFile diffFile : codeChange.getDiffFiles()) {
        		if(diffFile.getChangeType() == PRElement.ADD && diffFile.isJavaFile()) {
        			JavaFile jfile = getJavaFile(diffFile.getPath(), jfilesAfter);
        			if(jfile!=null && !inBuiltFiles(jfile,projectChange)) {
        				FileChange fileChange = createFileDeleted(codeChange, jfile);
                        projectChange.getFileChanges().add(fileChange);                       
                        fileChange.setTest(containsTestMethod(jfile));
        			}
        		}
        	} 
           
        } else if (changeType == PRElement.REVISE) {
            Set<JavaFile> jfilesBefore = new HashSet<>(projectBefore.getFiles());
            Set<JavaFile> jfilesAfter = new HashSet<>(projectAfter.getFiles());
            
            for (DiffFile diffFile : codeChange.getDiffFiles()) {
                if (diffFile.getChangeType() == PRElement.DELETE && diffFile.isJavaFile()) {
                    JavaFile jfile = getJavaFile(diffFile.getPath(), jfilesBefore);
                    if (jfile != null && !inBuiltFiles(jfile, projectChange)) {
                        FileChange fileChange = createFileDeleted(codeChange, jfile);
                        projectChange.getFileChanges().add(fileChange);
                        
                        fileChange.setTest(containsTestMethod(jfile));
                    }
                } else if (diffFile.getChangeType() == PRElement.ADD && diffFile.isJavaFile()) {
                    JavaFile jfile = getJavaFile(diffFile.getPath(), jfilesAfter);
                    if (jfile != null && !inBuiltFiles(jfile, projectChange)) {
                        FileChange fileChange = createFileAdded(codeChange, jfile);
                        projectChange.getFileChanges().add(fileChange);
                        
                        fileChange.setTest(containsTestMethod(jfile));
                    }
                } else if (diffFile.getChangeType() == PRElement.REVISE && diffFile.isJavaFile()) {
                    JavaFile jfileBefore = getJavaFile(diffFile.getPath(), jfilesBefore);
                    JavaFile jfileAfter = getJavaFile(diffFile.getPath(), jfilesAfter);
                    
                    if (jfileBefore != null && jfileAfter != null &&
                            !inBuiltFiles(jfileBefore, projectChange) && !inBuiltFiles(jfileAfter, projectChange)) {
                        FileChange fileChange = createFileChanged(codeChange, jfileBefore, jfileAfter);
                        projectChange.getFileChanges().add(fileChange);
                        
                        fileChange.setTest(containsTestMethod(jfileBefore) || containsTestMethod(jfileAfter));
                    }
                }
            }
        }
    }
    
    static boolean containsTestMethod(JavaFile jfile) {
        for (JavaClass jclass : jfile.getClasses()) {
            boolean containsTestMethod = jclass.getMethods().stream().anyMatch(m -> isTestMethod(m));
            if (containsTestMethod) {
                return true;
            }
        }
        return false;
    }
    
    private boolean inBuiltFiles(JavaFile jfile, ProjectChange projectChange) {
        return projectChange.getFileChanges().stream()
                            .map(c -> c.getPath())
                            .anyMatch(p -> p.equals(jfile.getPath()));
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
        String type ="";
        if(jclass.getDeclaringClass()!=null){
          type = jclass.getDeclaringClass().getQualifiedName().fqn();
        }
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
        String type="";
        if(jclass.getDeclaringClass()!=null) {       
          type = jclass.getDeclaringClass().getQualifiedName().fqn();
        }
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
        String type ="";
        if(jclassAfter.getDeclaringClass()!=null) {
         type = jclassAfter.getDeclaringClass().getQualifiedName().fqn();
        }
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
        Set<JavaMethod> afterMethods = new HashSet<>(jclassAfter.getMethods());
        
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
        return jfiles.stream().filter(f -> f.getPath().contains(path)).findFirst().orElse(null);
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
        String sourceCodeBefore = "";
        String sourceCodeAfter = jmethod.getSource();
        
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
        
        MethodChange methodChange = new MethodChange(pullRequest, PRElement.REVISE,
                name, qualifiedName, type, sourceCodeBefore, sourceCodeAfter);
        methodChange.setTest(isTestMethod(jmethodBefore) || isTestMethod(jmethodAfter));
        methodMapBefore.put(methodChange, jmethodBefore);
        methodElemMapBefore.put(jmethodBefore, methodChange.getCodeElementBefore());
        methodMapAfter.put(methodChange, jmethodAfter);
        methodElemMapAfter.put(jmethodAfter, methodChange.getCodeElementAfter());
        
        return methodChange;
    }
    
    static boolean isTestMethod(JavaMethod jmethod) {
        JavaClass jclass = jmethod.getDeclaringClass().getSuperClass();
        if (jclass != null) {
            if (jclass.getQualifiedName().fqn().equals("junit.framework.TestCase")) {
                return true;
            }
        }
        
        String anno = getAnnotation(jmethod);
        return "Test".equals(anno);
    }
    
    static String getAnnotation(JavaMethod jmethod) {
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
    
    public void setReferenceRelation(CodeChange codeChange) {
        for (FileChange fileChange : codeChange.getFileChanges()) {
            for (ClassChange classChange : fileChange.getClassChanges()) {
                JavaClass jclassBefore = classMapBefore.get(classChange);
                if (jclassBefore != null) {
                    Set<JavaClass> classes = jclassBefore.getAfferentClasses();
                   // Set<CodeElement> elems = getClassElements(classes, classElemMapBefore);
                    Set<CodeElement> elems = getClassElements(classes, PRElement.BEFORE);
                    classChange.getAfferentClassesBefore().addAll(elems);
                }
                
                JavaClass jclassAfter = classMapAfter.get(classChange);
                if (jclassAfter != null) {
                    Set<JavaClass> classes = jclassAfter.getAfferentClasses();
                    //Set<CodeElement> elems = getClassElements(classes, classElemMapAfter);
                    Set<CodeElement> elems = getClassElements(classes, PRElement.AFTER);
                    classChange.getAfferentClassesAfter().addAll(elems);
                }
                
                for (FieldChange fieldChange : classChange.getFieldChanges()) {
                    JavaField jfieldBefore = fieldMapBefore.get(fieldChange);
                    if (jfieldBefore != null) {
                        Set<JavaMethod> callingMethods = jfieldBefore.getAccessingMethods();
                        //Set<CodeElement> callingElems = getMethodElements(callingMethods, methodElemMapBefore);
                        Set<CodeElement> callingElems = getMethodElements(callingMethods, PRElement.BEFORE);
                        fieldChange.getAccessingMethodsBefore().addAll(callingElems);
                        
                        Set<JavaMethod> calledMethods = jfieldBefore.getCalledMethods();
                        //Set<CodeElement> calledElems = getMethodElements(calledMethods, methodElemMapBefore);
                        Set<CodeElement> calledElems = getMethodElements(calledMethods, PRElement.BEFORE);
                        fieldChange.getCalledMethodsBefore().addAll(calledElems);
                    }
                    
                    JavaField jfieldAfter = fieldMapAfter.get(fieldChange);
                    if (jfieldAfter != null) {
                        Set<JavaMethod> callingMethods = jfieldAfter.getAccessingMethods();
                        //Set<CodeElement> callingElems = getMethodElements(callingMethods, methodElemMapAfter);
                        Set<CodeElement> callingElems = getMethodElements(callingMethods, PRElement.AFTER);
                        fieldChange.getAccessingMethodsAfter().addAll(callingElems);
                        
                        Set<JavaMethod> calledMethods = jfieldAfter.getCalledMethods();
                      //Set<CodeElement> calledElems = getMethodElements(callingMethods, methodElemMapAfter);
                        Set<CodeElement> calledElems = getMethodElements(calledMethods, PRElement.AFTER);
                        fieldChange.getCalledMethodsAfter().addAll(calledElems);
                    }
                }
                
                for (MethodChange methodChange : classChange.getMethodChanges()) {
                    JavaMethod jmethodBefore = methodMapBefore.get(methodChange);
                    if (jmethodBefore != null) {
                        Set<JavaMethod> callingMethods = jmethodBefore.getCallingMethods();
                       // Set<CodeElement> callingElems = getMethodElements(callingMethods, methodElemMapBefore);
                        Set<CodeElement> callingElems = getMethodElements(callingMethods, PRElement.BEFORE);
                        methodChange.getCallingMethodsBefore().addAll(callingElems);
                        
                        Set<JavaMethod> calledMethods = jmethodBefore.getCalledMethods();
                       // Set<CodeElement> calledElems = getMethodElements(calledMethods, methodElemMapBefore);
                        Set<CodeElement> calledElems = getMethodElements(calledMethods, PRElement.BEFORE);
                        methodChange.getCalledMethodsBefore().addAll(calledElems);
                    }
                    
                    JavaMethod jmethodAfter = methodMapAfter.get(methodChange);
                    if (jmethodAfter != null) {
                        Set<JavaMethod> callingMethods = jmethodAfter.getCallingMethods();
                        //Set<CodeElement> callingElems = getMethodElements(callingMethods, methodElemMapAfter);
                        Set<CodeElement> callingElems = getMethodElements(callingMethods, PRElement.AFTER);
                        methodChange.getCallingMethodsAfter().addAll(callingElems);
                        
                        Set<JavaMethod> calledMethods = jmethodAfter.getCalledMethods();
                        //Set<CodeElement> calledElems = getMethodElements(calledMethods, methodElemMapAfter);
                        Set<CodeElement> calledElems = getMethodElements(calledMethods, PRElement.AFTER);
                        methodChange.getCalledMethodsAfter().addAll(calledElems);
                    }
                }
            }
        }
    }
    
    @SuppressWarnings("unused")
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
    
    private Set<CodeElement> getClassElements(Set<JavaClass> classes, String stage) {
        Set<CodeElement> elems = new HashSet<>();
        for(JavaClass jclass : classes)
        {
            CodeElement codeElem = new CodeElement(pullRequest, stage, jclass.getQualifiedName().fqn(), jclass.getSource());
            elems.add(codeElem);
        }
        return elems;
    }
    
    @SuppressWarnings("unused")
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
    
    private Set<CodeElement> getMethodElements(Set<JavaMethod> methods, String stage) {
        Set<CodeElement> elems = new HashSet<>();
        for(JavaMethod jmethod : methods)
        {
            CodeElement codeElem = new CodeElement(pullRequest,stage,jmethod.getQualifiedName().fqn(), jmethod.getSource());
            
            elems.add(codeElem);
        }
        return elems;
    }
    
    public void setTestForClasses(CodeChange codeChange) {
        for (FileChange fileChange : codeChange.getFileChanges()) {
            for (ClassChange classChange : fileChange.getClassChanges()) {
                boolean isTest = classChange.getMethodChanges().stream().anyMatch(m -> m.isTest());
                classChange.setTest(isTest);
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
