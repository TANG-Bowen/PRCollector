package org.jtool.prmodel.builder;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jtool.prmodel.DeficientPullRequest;
import org.jtool.prmodel.PullRequest;

public class DeficientPullRequestBuilder {
    
    private String exceptionOutput;
    private PullRequest pullRequest;
    
    DeficientPullRequestBuilder(String exceptionOutput) {
        this.exceptionOutput = exceptionOutput;
    }
    
    DeficientPullRequestBuilder(PullRequest pullRequest, String exceptionOutput)
    {
        this.pullRequest = pullRequest;
        this.exceptionOutput = exceptionOutput;
    }
    
    DeficientPullRequest build() {
        String dataLossType = buildDataLossType();
        DeficientPullRequest deficientPullRequest = new DeficientPullRequest(dataLossType, exceptionOutput,
                pullRequest.getId(), pullRequest.getTitle(), pullRequest.getRepositoryName(), 
                pullRequest.getState(), pullRequest.getCreateDate(), pullRequest.getEndDate(),
                pullRequest.getMergeBranch(), pullRequest.getHeadBranch(),
                pullRequest.getPageUrl(), pullRequest.getRepositorySrcDLUrl(), pullRequest.getHeadRepositorySrcDLUrl(),
                pullRequest.isMerged(), pullRequest.isStandardMerged(), pullRequest.isSourceCodeRetrievable());
        return deficientPullRequest;
    }
    
    private String buildDataLossType() {
        String outputFirstLine = extractFirstLine(this.exceptionOutput);
        String regex = "\"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(outputFirstLine);
        List<String> variables = new ArrayList<>();
        while(matcher.find()) {
            variables.add(matcher.group(1));
        }
        
        if (outputFirstLine.contains("java.lang.NullPointerException") && isGHUser(variables)) {
            return "User Information loss";
        } else if (outputFirstLine.equals("org.jtool.prmodel.builder.CommitMissingException: Git checkout error")) {
            return "Commit loss";
        } else {
            return "Entity loss";
        }
    }
    
    private String extractFirstLine(String exceptionOutput) {
        String[] lines = exceptionOutput.split("\\R");
        if (lines[0] != null) {
            return lines[0];
        }
        return null;
    }
    
    private boolean isGHUser(List<String> variables) {
        return variables.stream()
                        .anyMatch(v -> v.equals("org.kohsuke.github.GHUser.getLogin()") || v.contains("GHUser"));
    }
}
