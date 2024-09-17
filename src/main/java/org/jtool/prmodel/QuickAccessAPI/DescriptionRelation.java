package org.jtool.prmodel.QuickAccessAPI;

import java.util.List;
import java.util.stream.Collectors;

import org.jtool.prmodel.PullRequest;

public class DescriptionRelation {
    
    public static int complexity_title(PullRequest pullRequest) {
        String[] words = pullRequest.getTitle().split("\\s+");
        return words.length;
    }
    
    public static int complexity_description(PullRequest pullRequest) {
        String[] words = pullRequest.getDescription().getBody().split("\\s+");
        return words.length;
    }
    
    public static List<String> mentionInDescription(PullRequest pullRequest) {
        return pullRequest.getDescription().getMarkdownDoc().getMentionStrings().stream()
                .map(c -> c.getText()).collect(Collectors.toList());
    }
}
