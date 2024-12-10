package org.jtool.prmodel;

import org.jtool.jwrmodel.JsonFileReader;

public class PRModelLoader {
    
    private final String filePath;
    
    public PRModelLoader(String filePath) {
        this.filePath = filePath;
    }
    
    public PRModel load() {
        PRModel prmodel = new PRModel();
        
        JsonFileReader jsonFileReader = new JsonFileReader(filePath);
<<<<<<< HEAD
        jsonFileReader.read();
=======
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
        prmodel.addAllPullRequests(jsonFileReader.getPullRequests());
        return prmodel;
    }
}
