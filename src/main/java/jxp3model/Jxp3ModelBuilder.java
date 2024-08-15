package jxp3model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import prmodel.Commit;
import prmodel.PRModelBuilder;
import prmodel.PullRequest;

public class Jxp3ModelBuilder {
	
	PRModelBuilder prm;
	PullRequest pr;

	
	public Jxp3ModelBuilder(PRModelBuilder prm)
	{
		this.prm = prm;
		this.pr = prm.getPr();

	}
	
	public void build()
	{
		this.buildCommitCodeElement();
		this.buildPRCodeElement();
	}
	
	void buildCommitCodeElement()
	{
		int commitCodeElementId = -1;
		try {
		for(Commit cmiti : this.pr.getCommits())
		{
			if(!cmiti.getCommitType().equals("merge") && !cmiti.getCommitType().equals("initial"))
			{
			CodeElement cde = new CodeElement(cmiti.getFlcg(), pr, this);
			commitCodeElementId ++;
			cde.instId = "commitCodeElement"+commitCodeElementId;
			cde.build();
			cmiti.getFlcg().setCde(cde);
			cde.unBuild();
			}
		}
		}catch(Exception e)
		{
			this.prm.recordException(e);
		}
	}
	
	void buildPRCodeElement()
	{
		if(pr.getFlcg()!=null)
		{
		  CodeElement cde = new CodeElement(pr.getFlcg(),pr, this);
		  cde.instId = "prCodeElement0";
		  cde.build();
		  pr.getFlcg().setCde(cde);
		}
		
	}
	
	

}
