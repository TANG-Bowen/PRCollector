package org.jtool.prmodel.builder;

import java.io.IOException;

import org.kohsuke.github.GHPullRequest;

import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.Description;

public class DescriptionBuilder {
    
    private final PullRequest pullRequest;
    private final GHPullRequest ghPullRequest;
    
    DescriptionBuilder(PullRequest pullRequest, GHPullRequest ghPullRequest) {
        this.pullRequest = pullRequest;
        this.ghPullRequest = ghPullRequest;
    }
    
    void build() throws IOException {
        String body = ghPullRequest.getBody();
        Description description = new Description(pullRequest, body);
        
        pullRequest.setDescription(description);
    }
}
