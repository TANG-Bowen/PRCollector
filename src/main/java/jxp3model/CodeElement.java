package jxp3model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import prmodel.DiffFileUnit;
import prmodel.FilesChanged;
import prmodel.PullRequest;

public class CodeElement {
	
	PullRequest pr;
	FilesChanged flcg;
	Jxp3ModelBuilder jxpm;
	ArrayList<ChangedProjectUnit> cgProjectUnits;
	ArrayList<ChangedFileUnit> cgFileUnits;
	LinkedHashSet<String> pjNameIndex;
	String instId="";
	
	public CodeElement(FilesChanged flcg, PullRequest pr, Jxp3ModelBuilder jxpm)
	{
		this.pr = pr;
		this.flcg = flcg;
		this.jxpm = jxpm;
		cgProjectUnits = new ArrayList<>();
		cgFileUnits = new ArrayList<>();
		pjNameIndex = new LinkedHashSet<>();
	}
	
	public CodeElement(FilesChanged flcg, PullRequest pr)
	{
		this.pr = pr;
		this.flcg =flcg;
		cgProjectUnits = new ArrayList<>();
		cgFileUnits = new ArrayList<>();
		pjNameIndex = new LinkedHashSet<>();
	}
	
	void build()
	{
		this.buildCgProjectUnits();
	}
	
	void printDiffUnitPath()
	{
		if(this.flcg!=null)
		{
			if(!this.flcg.getDiffFileUnits().isEmpty())
			{
				System.out.println("Diff files total number : "+this.flcg.getDiffFileUnits().size());
				for(int i = 0; i < this.flcg.getDiffFileUnits().size(); i++)
				{
					DiffFileUnit dfui = this.flcg.getDiffFileUnits().get(i);
					System.out.println("Diff file unit "+ i +"  file type : "+ dfui.getFileType());
				}
			}
		}
	}
	
	void buildCgProjectUnits()
	{
		int cgProjectUnitCounter = -1;
		try {
			if(this.flcg!=null)
			{
		for(int i=0; i<this.flcg.getDiffFileUnits().size();i++)
		{
			DiffFileUnit dfui = this.flcg.getDiffFileUnits().get(i);
			String inputPathBefore="";
			String inputNameBefore="";
			String inputPathAfter="";
			String inputNameAfter="";
			if(dfui.isJavaSrcFile() == true && dfui.getFileType().equals("delete"))
			{
				if(this.containsDir(dfui.getAbsolutePathBefore(), "src"))
				{
					String ssb[] = dfui.getAbsolutePathBefore().split(File.separator + "src");
					inputPathBefore = ssb[0] + File.separator + "src";
					String lssb[] = ssb[0].split(this.flcg.getSrcBeforeDirName() + File.separator);
					if(lssb.length==1)
					{
					   inputNameBefore = lssb[0];
					}else {
					   inputNameBefore = lssb[1];	
					}
					String ssa[] = inputPathBefore.split(this.flcg.getSrcBeforeDirName());
					inputPathAfter = ssa[0] + this.flcg.getSrcAfterDirName() + ssa[1];
					inputNameAfter = inputNameBefore;
				}else if(!this.containsDir(dfui.getAbsolutePathBefore(), "src") && this.containsDir(dfui.getAbsolutePathBefore(), "test"))
				{
					String ssb[] = dfui.getAbsolutePathBefore().split(File.separator + "test");
					inputPathBefore = ssb[0] + File.separator + "test";
					String lssb[] = ssb[0].split(this.flcg.getSrcBeforeDirName() + File.separator);
					if(lssb.length==1)
					{
					   inputNameBefore = lssb[0];
					}else {
					   inputNameBefore = lssb[1];	
					}
					String ssa[] = inputPathBefore.split(this.flcg.getSrcBeforeDirName());
					inputPathAfter = ssa[0] + this.flcg.getSrcAfterDirName() + ssa[1];
					inputNameAfter = inputNameBefore;
				}
				File f = new File(inputPathAfter);
				if(this.pjNameIndex.contains(inputNameBefore) == false && f.exists()==false)
				{
					ChangedProjectUnit cgpju = new ChangedProjectUnit(this);
					cgProjectUnitCounter ++ ;
					cgpju.instId = "changedProjectUnit"+cgProjectUnitCounter;
					cgpju.inputNameBefore = inputNameBefore;
					cgpju.inputPathBefore = inputPathBefore;
					cgpju.type = "delete";
					cgpju.build();
					this.cgProjectUnits.add(cgpju);
					this.pjNameIndex.add(inputNameBefore);
				}else if(this.pjNameIndex.contains(inputNameBefore) == false && f.exists()==true)
				{
					ChangedProjectUnit cgpju = new ChangedProjectUnit(this);
					cgProjectUnitCounter ++ ;
					cgpju.instId = "changedProjectUnit"+cgProjectUnitCounter;
					cgpju.inputNameBefore = inputNameBefore;
					cgpju.inputPathBefore = inputPathBefore;
					cgpju.inputNameAfter = inputNameAfter;
					cgpju.inputPathAfter = inputPathAfter;
					cgpju.type = "change";
					cgpju.build();
					this.cgProjectUnits.add(cgpju);
					this.pjNameIndex.add(inputNameBefore);
				}
				
			}else if(dfui.isJavaSrcFile() == true && dfui.getFileType().equals("add"))
			{
				if(this.containsDir(dfui.getAbsolutePathAfter(), "src"))
				{
					String ssa[] = dfui.getAbsolutePathAfter().split(File.separator + "src");
					inputPathAfter = ssa[0] + File.separator + "src";
					String lssa[] = ssa[0].split(this.flcg.getSrcAfterDirName() + File.separator);
					if(lssa.length==1)
					{
					   inputNameAfter = lssa[0];
					}else {
					   inputNameAfter = lssa[1];	
					}
					String ssb[] = inputPathAfter.split(this.flcg.getSrcAfterDirName());
					inputPathBefore = ssb[0] + this.flcg.getSrcBeforeDirName() + ssb[1];
					inputNameBefore = inputNameAfter;
				}else if(!this.containsDir(dfui.getAbsolutePathAfter(), "src") && this.containsDir(dfui.getAbsolutePathAfter(), "test"))
				{
					String ssa[] = dfui.getAbsolutePathAfter().split(File.separator + "test");
					inputPathAfter = ssa[0] + File.separator + "test";
					String lssa[] = ssa[0].split(this.flcg.getSrcAfterDirName() + File.separator);
					if(lssa.length==1)
					{
					   inputNameAfter = lssa[0];
					}else {
					   inputNameAfter = lssa[1];	
					}
					String ssb[] = inputPathAfter.split(this.flcg.getSrcAfterDirName());
					inputPathBefore = ssb[0] + this.flcg.getSrcBeforeDirName() + ssb[1];
					inputNameBefore = inputNameAfter;
				}
				File f = new File(inputPathBefore);
				
				if(this.pjNameIndex.contains(inputNameAfter) == false && f.exists()==false)
				{
					ChangedProjectUnit cgpju = new ChangedProjectUnit(this);
					cgProjectUnitCounter ++ ;
					cgpju.instId = "changedProjectUnit"+cgProjectUnitCounter;
					cgpju.inputNameAfter = inputNameAfter;
					cgpju.inputPathAfter = inputPathAfter;
					cgpju.type = "add";
					cgpju.build();
					this.cgProjectUnits.add(cgpju);
					this.pjNameIndex.add(inputNameAfter);
				}else if(this.pjNameIndex.contains(inputNameAfter) == false && f.exists()==true)
				{
					ChangedProjectUnit cgpju = new ChangedProjectUnit(this);
					cgProjectUnitCounter ++ ;
					cgpju.instId = "changedProjectUnit"+cgProjectUnitCounter;
					cgpju.inputNameBefore = inputNameBefore;
					cgpju.inputPathBefore = inputPathBefore;
					cgpju.inputNameAfter = inputNameAfter;
					cgpju.inputPathAfter = inputPathAfter;
					cgpju.type = "change";
					cgpju.build();
					this.cgProjectUnits.add(cgpju);
					this.pjNameIndex.add(inputNameAfter);
				}
				
			}else if(dfui.isJavaSrcFile() == true && dfui.getFileType().equals("change"))
			{
				if(this.containsDir(dfui.getAbsolutePathBefore(), "src") && this.containsDir(dfui.getAbsolutePathAfter(), "src"))
				{
					String ssb[] = dfui.getAbsolutePathBefore().split(File.separator + "src");
					inputPathBefore = ssb[0] + File.separator + "src";
					String lssb[] = ssb[0].split(this.flcg.getSrcBeforeDirName() + File.separator);
					if(lssb.length==1)
					{
					   inputNameBefore = lssb[0];
					}else {
					   inputNameBefore = lssb[1];	
					}

					String ssa[] = dfui.getAbsolutePathAfter().split(File.separator + "src");
					inputPathAfter = ssa[0] + File.separator + "src";
					String lssa[] = ssa[0].split(this.flcg.getSrcAfterDirName() + File.separator);
					if(lssa.length==1)
					{
					   inputNameAfter = lssa[0];
					}else {
					   inputNameAfter = lssa[1];	
					}
				}else if(!this.containsDir(dfui.getAbsolutePathBefore(), "src") && !this.containsDir(dfui.getAbsolutePathAfter(), "src") && this.containsDir(dfui.getAbsolutePathBefore(), "test") && this.containsDir(dfui.getAbsolutePathAfter(), "test"))
				{
					String ssb[] = dfui.getAbsolutePathBefore().split(File.separator + "test");
					inputPathBefore = ssb[0] + File.separator + "test";
					String lssb[] = ssb[0].split(this.flcg.getSrcBeforeDirName() + File.separator);
					if(lssb.length==1)
					{
					   inputNameBefore = lssb[0];
					}else {
					   inputNameBefore = lssb[1];	
					}

					String ssa[] = dfui.getAbsolutePathAfter().split(File.separator + "test");
					inputPathAfter = ssa[0] + File.separator + "test";
					String lssa[] = ssa[0].split(this.flcg.getSrcAfterDirName() + File.separator);
					if(lssa.length==1)
					{
					   inputNameAfter = lssa[0];
					}else {
					   inputNameAfter = lssa[1];	
					}
				}
				if(this.pjNameIndex.contains(inputNameBefore)==false && this.pjNameIndex.contains(inputNameAfter)==false)
				{
					ChangedProjectUnit cgpju = new ChangedProjectUnit(this);
					cgProjectUnitCounter ++ ;
					cgpju.instId = "changedProjectUnit"+cgProjectUnitCounter;
					cgpju.inputNameBefore = inputNameBefore;
					cgpju.inputPathBefore = inputPathBefore;
					cgpju.inputNameAfter = inputNameAfter;
					cgpju.inputPathAfter = inputPathAfter;
					cgpju.type = "change";
					cgpju.build();
					this.cgProjectUnits.add(cgpju);
					this.pjNameIndex.add(inputNameAfter);
				}
				
			}
		}
			}
		}catch(Exception e)
		{
			this.jxpm.prm.recordException(e);
		}
		
	}
	
	boolean containsDir(String path, String target)
	{
		Path pa = Paths.get(path);
		for(Path pi : pa)
		{
			if(pi.toString().equals(target))
			{
				return true;
			}
		}
		return false;
		
	}
	
	public void printCodeElement()
	{
		System.out.println();
		System.out.println("---CodeElement between "+this.flcg.getSrcBeforeDirName()+" and "+this.flcg.getSrcAfterDirName()+"----------------");
		System.out.println("pr title : "+this.pr.getTitle());
		System.out.println("cde instId : "+this.instId);
		System.out.println("flcg srcBeforeDirName : "+this.flcg.getSrcBeforeDirName());
		System.out.println("flcg srcAfterDirName : "+this.flcg.getSrcAfterDirName());
		for(ChangedProjectUnit cgpjui : this.cgProjectUnits)
		{
			cgpjui.printCgPjUnit();
		}
		System.out.println("---=================---------------=====================---------------");
	}
	
	public void printPRCodeElement()
	{
		System.out.println();
		System.out.println("cde instId : "+this.instId);
		if(!this.cgFileUnits.isEmpty())
		{
			for(ChangedFileUnit cgfui : this.cgFileUnits)
			{
				cgfui.printCgFlUnit();
			}
			System.out.println(" Total changefileUnits : "+this.cgFileUnits.size());
		}
	}

	public PullRequest getPr() {
		return pr;
	}

	public void setPr(PullRequest pr) {
		this.pr = pr;
	}

	public FilesChanged getFlcg() {
		return flcg;
	}

	public void setFlcg(FilesChanged flcg) {
		this.flcg = flcg;
	}

	public ArrayList<ChangedProjectUnit> getCgProjectUnits() {
		return cgProjectUnits;
	}

	public void setCgProjectUnits(ArrayList<ChangedProjectUnit> cgProjectUnits) {
		this.cgProjectUnits = cgProjectUnits;
	}

	public LinkedHashSet<String> getPjNameIndex() {
		return pjNameIndex;
	}

	public void setPjNameIndex(LinkedHashSet<String> pjNameIndex) {
		this.pjNameIndex = pjNameIndex;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public Jxp3ModelBuilder getJxpm() {
		return jxpm;
	}

	public void setJxpm(Jxp3ModelBuilder jxpm) {
		this.jxpm = jxpm;
	}

	public ArrayList<ChangedFileUnit> getCgFileUnits() {
		return cgFileUnits;
	}

	public void setCgFileUnits(ArrayList<ChangedFileUnit> cgFileUnits) {
		this.cgFileUnits = cgFileUnits;
	}
	
	
	
	

}
