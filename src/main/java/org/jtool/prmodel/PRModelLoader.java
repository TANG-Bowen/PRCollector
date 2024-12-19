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
        jsonFileReader.read();
        prmodel.addAllPullRequests(jsonFileReader.getPullRequests());
        prmodel.addAllDataLosses(jsonFileReader.getDataLosses());
        return prmodel;
    }
}
