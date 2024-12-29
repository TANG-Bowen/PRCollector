package org.jtool.prmodel.builder;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.kohsuke.github.GHPullRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.HTMLDescription;

public class HTMLDescriptionBuilder {
    
    private List<Exception> exceptions = new ArrayList<>();
    
    private final PullRequest pullRequest;
    private final GHPullRequest ghPullRequest;
    
    HTMLDescriptionBuilder(PullRequest pullRequest, GHPullRequest ghPullRequest) {
        this.pullRequest = pullRequest;
        this.ghPullRequest = ghPullRequest;
    }
    
    void build() {
        try {
            org.jsoup.nodes.Document hdoc = Jsoup.connect(ghPullRequest.getHtmlUrl().toString()).get();
            Elements comments = hdoc.getElementsByClass("comment-body");
            Element comment = comments.get(0);
            String body = comment.text();
            
            HTMLDescription description = new HTMLDescription(pullRequest, body);
            
            Elements mentions = hdoc.getElementsByClass("user-mention notranslate");
            mentions.stream().map(m -> m.text()).forEach(u -> description.getMentionUsers().add(u));
            
            pullRequest.setHTMLDescription(description);
        } catch (IOException e) {
            exceptions.add(e);
        }
    }
    
    List<Exception> getExceptions() {
        return exceptions;
    }
}
