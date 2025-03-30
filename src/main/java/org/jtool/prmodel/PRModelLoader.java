/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.prmodel;

import org.jtool.jwrmodel.JsonFileReader;

public class PRModelLoader {
    
    private final String filePath;
    
    public PRModelLoader(String filePath) {
        this.filePath = filePath;
    }
    
    public PRModel load() {
        PRModel prmodel = new PRModel();
        
        JsonFileReader jsonFileReader = new JsonFileReader(this, filePath);
        jsonFileReader.read();
        
        prmodel.addAllPullRequests(jsonFileReader.getPullRequests());
        prmodel.addAllDeficientPullRequests(jsonFileReader.getDeficientPullRequests());
        return prmodel;
    }
    
    public PRModel load(int num) {
        PRModel prmodel = new PRModel();
        
        JsonFileReader jsonFileReader = new JsonFileReader(this, filePath);
        jsonFileReader.read(num);
        
        prmodel.addAllPullRequests(jsonFileReader.getPullRequests());
        prmodel.addAllDeficientPullRequests(jsonFileReader.getDeficientPullRequests());
        return prmodel;
    }
    
    public void loadOnly() {
        JsonFileReader jsonFileReader = new JsonFileReader(this, filePath);
        jsonFileReader.hookedRead();
    }
    
    public void actionPerformed(PullRequest pullRequest) {
        // Invoked per loading each pull request when using loadOnly()
    }
    
    public void actionPerformed(DeficientPullRequest pullRequest) {
        // Invoked per loading each pull request when using loadOnly()
    }
}
