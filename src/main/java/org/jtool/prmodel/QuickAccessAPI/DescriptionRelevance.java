package org.jtool.prmodel.QuickAccessAPI;

import java.util.List;
import java.util.stream.Collectors;

import org.jtool.prmodel.PullRequest;

public class DescriptionRelevance {
    
    public static int numComplexTitle(PullRequest pullRequest) {
        String[] words = pullRequest.getTitle().split("\\s+");
        return words.length;
    }
    
    public static int numComplexDescription(PullRequest pullRequest) {
        String[] words = pullRequest.getDescription().getBody().split("\\s+");
        return words.length;
    }
    
    public static List<String> getMentionsInDescription(PullRequest pullRequest) {
        return pullRequest.getDescription().getMarkdownDoc().getMentionStrings().stream()
                .map(c -> c.getText()).collect(Collectors.toList());
    }
}