package org.jtool.prmodel.builder;

import org.jtool.jxplatform.project.ModelBuilderImpl;
import org.jtool.srcmodel.JavaProject;

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
}
