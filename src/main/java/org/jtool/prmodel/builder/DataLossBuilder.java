package org.jtool.prmodel.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jtool.prmodel.DataLoss;
import org.jtool.prmodel.PRModelDate;
import org.jtool.prmodel.PullRequest;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHIssueEvent;
import org.kohsuke.github.GHPullRequest;

public class DataLossBuilder {
	
	private  GHPullRequest ghPullRequest;
	private  String exceptionOutput;
	private  PullRequest pullRequest;
	
	DataLossBuilder(GHPullRequest ghPullRequest, String exceptionOutput)
	{
		this.ghPullRequest = ghPullRequest;
		this.exceptionOutput = exceptionOutput;
	}
	
	DataLossBuilder(PullRequest pullRequest, String exceptionOutput)
	{
		this.pullRequest = pullRequest;
		this.exceptionOutput = exceptionOutput;
	}
	
	DataLoss build()
	{		
		String dataLossType = buildDataLossType();
		DataLoss dataLoss = new DataLoss(dataLossType, exceptionOutput, pullRequest.getId(), pullRequest.getTitle(), pullRequest.getRepositoryName(), 
				pullRequest.getState(), pullRequest.getCreateDate(), pullRequest.getEndDate(), pullRequest.getMergeBranch(), pullRequest.getHeadBranch(),
				pullRequest.getPageUrl(), pullRequest.getRepositorySrcDLUrl(), pullRequest.getHeadRepositorySrcDLUrl(), pullRequest.isMerged(), pullRequest.isStandardMerged(),
				pullRequest.isSourceCodeRetrievable());
		
		return dataLoss;
	}
	
	String buildDataLossType()
	{
		String outputFirstLine = extractFirstLine(this.exceptionOutput);
		String regex = "\"(.*?)\"";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(outputFirstLine);
		List<String> variables = new ArrayList<>();
		while(matcher.find())
		{
			variables.add(matcher.group(1));
		}
		if(outputFirstLine.contains("java.lang.NullPointerException")
				&& isGHUser(variables) )
		{
			return "User Information loss";
		}else if(outputFirstLine.equals("org.jtool.prmodel.builder.CommitMissingException: Git checkout error"))
		{
			return "Commit loss";
		}else {
			return "Entity loss";
		}
	}
	
	String extractFirstLine(String exceptionOutput)
	{
		String[]lines = exceptionOutput.split("\\R");
		if(lines[0]!=null)
		{
		  return lines[0];
		}
		return null;
	}
	
	boolean isGHUser(List<String> variables)
	{
		for(String var : variables)
		{
			if(var.equals("org.kohsuke.github.GHUser.getLogin()") || var.contains("GHUser"))
			{
				return true;
			}
		}
		return false;
	}

}
