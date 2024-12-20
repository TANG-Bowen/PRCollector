package org.jtool.prmodel.builder;

import org.jtool.jxplatform.builder.ModelBuilder;
import org.jtool.srcmodel.JavaProject;

public class TinyModelBuilder extends ModelBuilder {
    
    public TinyModelBuilder() {
        builderImpl = new TinyModelBuilderImpl();
    }
    
    @Override
    public TinyModelBuilderImpl getModelBuilderImpl() {
        return (TinyModelBuilderImpl)builderImpl;
    }
    
    public void setVerbose(boolean verbose) {
        getModelBuilderImpl().setVerbose(verbose);
    }
    
    public JavaProject buildSingle(String name, String srcpath) {
        JavaProject jproject = getModelBuilderImpl().buildSingle(this, name, srcpath);
        return jproject;
    }
    
    public JavaProject buildSingle(String name, String[] srcpath) {
        JavaProject jproject = getModelBuilderImpl().buildSingle(this, name, srcpath);
        return jproject;
    }
}
