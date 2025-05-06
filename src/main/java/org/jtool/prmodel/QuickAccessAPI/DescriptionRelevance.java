/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.prmodel.QuickAccessAPI;

import java.util.List;
import java.util.stream.Collectors;

import org.jtool.prmodel.PullRequest;

public class DescriptionRelevance {
    
    /**
     * Returns the number of total words in a pull-request's title.
     * @param pullRequest a pull-request
     * @return the number of the words in the title
     */
    public static int numComplexTitle(PullRequest pullRequest) {
        String title = pullRequest.getTitle();
        String[] titleWords = title.split("\\s+");
        return titleWords.length;
    }
    
    /**
     * Returns the number of total words in a pull-request's body.
     * @param pullRequest a pull-request
     * @return the number of the words in the description
     */
    public static int numComplexBody(PullRequest pullRequest) {
        String body = pullRequest.getDescription().getBody();
        String[] bodyWords = body.split("\\s+");
        return bodyWords.length;
    }
    
    /**
     * Returns the number of total words in a pull-request's title and body.
     * @param pullRequest a pull-request
     * @return the number of the words in the description
     */
    public static int numComplexDescription(PullRequest pullRequest) {
        String title = pullRequest.getTitle();
        String body = pullRequest.getDescription().getBody();
        if (title == null) {
            title = "";
        }
        if (body == null) {
            body = "";
        }
        String[] titleWords = title.split("\\s+");
        String[] bodyWords = body.split("\\s+");
        return titleWords.length + bodyWords.length;
    }
    
    /**
     * Returns the collection of mentions in a pull-request's description.
     * @param pullRequest a pull-request
     * @return a List of mention Strings
     */
    public static List<String> getMentionsInDescription(PullRequest pullRequest) {
        return pullRequest.getDescription().getMarkdownDoc().getMentionStrings().stream()
                .map(c -> c.getText()).collect(Collectors.toList());
    }
}