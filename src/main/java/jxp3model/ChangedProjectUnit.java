package jxp3model;

import java.util.HashSet;

import org.jtool.tinyjxplatform.builder.ModelBuilder;
import org.jtool.tinyjxplatform.srcmodel.JavaFile;
import org.jtool.tinyjxplatform.srcmodel.JavaProject;

import prmodel.DiffFileUnit;


public class ChangedProjectUnit {
	
	CodeElement cde;
	String instId="";
	String inputPathBefore="";
	String inputNameBefore="";
	String inputPathAfter="";
	String inputNameAfter="";
	
	JavaProject jpjBefore;
	JavaProject jpjAfter;
	ModelBuilder mbBefore;
	ModelBuilder mbAfter;
	
	String type="";//[add, delete, change]
	String name="";
	
	HashSet<ChangedFileUnit> cgFileUnits;
	
	public ChangedProjectUnit(CodeElement cde)
	{
		this.cde = cde;
		mbBefore = new ModelBuilder();
		mbAfter = new ModelBuilder();
		mbBefore.setVerbose(false);
		mbAfter.setVerbose(false);
		cgFileUnits = new HashSet<>();
	}
	
	void build()
	{
		  this.buildMdBuilder();
		  if(!cde.jxpm.prm.exceptionDone)
		  {
		     this.buildCgFiles();
		     this.buildName();
		  }
		  
		
	}
	void unBuild()
	{
		this.jpjBefore=null;
		this.jpjAfter=null;
		this.mbBefore=null;
		this.mbAfter=null;
		if(!this.cgFileUnits.isEmpty())
		{
			for(ChangedFileUnit cgfui : this.cgFileUnits)
			{
				cgfui.unBuild();
			}
		}
	}
	
	void buildMdBuilder()
	{
		try {
		  jpjBefore = mbBefore.build(inputNameBefore, inputPathBefore);
		  jpjAfter = mbAfter.build(inputNameAfter, inputPathAfter);
		  System.out.println("jxp3 builder builded ");
		  System.out.println("inputNameBefore : " + this.inputNameBefore);
		  System.out.println("inputPathBefore : "+ this.inputPathBefore);
		  System.out.println("inputNameAfter : " + this.inputNameAfter);
		  System.out.println("inputPathAfter : "+ this.inputPathAfter);
		}catch(Exception e)
		{
			this.cde.jxpm.prm.recordException(e);
			
		}
	}
	
	void buildName()
	{
		if(this.type.equals("delete"))
		{
			this.name = this.inputNameBefore;
		}else if(this.type.equals("add"))
		{
			this.name = this.inputNameAfter;
		}else if(this.type.equals("change"))
		{
			this.name = this.inputNameAfter;
		}
	}
	
	void buildCgFiles()
	{
		int CgFilesCounter = -1;
		if(this.type.equals("delete"))
		{
			HashSet<JavaFile> fs = new HashSet<>();
			fs.addAll(this.jpjBefore.getFiles());
			if(fs.isEmpty()==false)
			{
				for(JavaFile jf :fs)
				{
					ChangedFileUnit cgflu = new ChangedFileUnit();
					CgFilesCounter ++;
					cgflu.instId = "changedFileUnit"+CgFilesCounter;
					cgflu.jfBefore = jf;
					cgflu.type="delete";
					cgflu.build();
					this.cgFileUnits.add(cgflu);
				}
			}
		}else if(this.type.equals("add"))
		{
			HashSet<JavaFile> fs = new HashSet<>();
			fs.addAll(this.jpjAfter.getFiles());
			if(fs.isEmpty()==false)
			{
				for(JavaFile jf :fs)
				{
					ChangedFileUnit cgflu = new ChangedFileUnit();
					CgFilesCounter ++;
					cgflu.instId = "changedFileUnit"+CgFilesCounter;
					cgflu.jfAfter = jf;
					cgflu.type="add";
					cgflu.build();
					this.cgFileUnits.add(cgflu);
				}
			}
		}else if(this.type.equals("change"))
		{
			HashSet<JavaFile> fsb = new HashSet<>();
			fsb.addAll(this.jpjBefore.getFiles());
			
			HashSet<JavaFile> fsa = new HashSet<>();
			fsa.addAll(this.jpjAfter.getFiles());
			
			if(this.cde.flcg.getDiffFileUnits().isEmpty()==false)
			{
				for(DiffFileUnit dfu : this.cde.flcg.getDiffFileUnits())
				{
					if(dfu.getFileType().equals("delete"))
					{	
						JavaFile jfi = checkinFiles(dfu.getAbsolutePathBefore(),fsb);
						if(jfi!=null)
						{
							ChangedFileUnit cgflu = new ChangedFileUnit();
							CgFilesCounter ++;
							cgflu.instId = "changedFileUnit"+CgFilesCounter;
							cgflu.jfBefore = jfi;
							cgflu.type = "delete";
							cgflu.build();
							this.cgFileUnits.add(cgflu);
						}
					}else if(dfu.getFileType().equals("add"))
					{
						JavaFile jfi = checkinFiles(dfu.getAbsolutePathAfter(),fsa);
						if(jfi!=null)
						{
							ChangedFileUnit cgflu = new ChangedFileUnit();
							CgFilesCounter ++;
							cgflu.instId = "changedFileUnit"+CgFilesCounter;
							cgflu.jfAfter = jfi;
							cgflu.type = "add";
							cgflu.build();
							this.cgFileUnits.add(cgflu);
						}
					}else if(dfu.getFileType().equals("change"))
					{
						JavaFile jfib = checkinFiles(dfu.getAbsolutePathBefore(),fsb);
						JavaFile jfia = checkinFiles(dfu.getAbsolutePathAfter(),fsa);
						if(jfib!=null && jfia!=null)
						{
							ChangedFileUnit cgflu = new ChangedFileUnit();
							CgFilesCounter ++;
							cgflu.instId = "changedFileUnit"+CgFilesCounter;
							cgflu.jfBefore = jfib;
							cgflu.jfAfter = jfia;
							cgflu.type = "change";
							cgflu.build();
							this.cgFileUnits.add(cgflu);
						}else if(jfib!=null && jfia==null){
							
							System.out.println("jfib : "+jfib.getPath());
							System.out.println("jfia : ");
						}else if(jfib==null && jfia!=null)
						{
							System.out.println("jfib : ");
							System.out.println("jfia : "+jfia.getPath());
						}
					}
				}
			}else {
				System.out.println("DiffFileUnits is empty!! ");
			}
			
		}
	}
	
	JavaFile checkinFiles(String path , HashSet<JavaFile> jfs)
	{
		if(jfs.isEmpty()==false)
		{
			for(JavaFile jf : jfs)
			{
				if(jf.getPath().equals(path))
				{
					return jf;
				}
			}
		}
		
		return null;
	}
	
	void printCgPjUnit()
	{
		System.out.println();
		System.out.println("---ChangedProjectUnit----------------");
		System.out.println("   CgpjUnit instId : "+this.instId);
		System.out.println("   CgpjUnit Name : "+this.name);
		System.out.println("   CgpjUnit Type : "+this.type);
		System.out.println("   CgpjUnit InputPathBefore : "+this.inputPathBefore);
		System.out.println("   CgpjUnit InputNameBefore : "+this.inputNameBefore);
		System.out.println("   CgpjUnit InputPathAfter : "+this.inputPathAfter);
		System.out.println("   CgpjUnit InputNameAfter : "+this.inputNameAfter);
		for(ChangedFileUnit cgflui : this.cgFileUnits)
		{
			cgflui.printCgFlUnit();
		}
		if(this.cgFileUnits.isEmpty())
		{
			System.out.println("No cgFiles in cgProjectUnit !! ");
		}
		System.out.println("---========Project End=========----------------");
	}

	public CodeElement getCde() {
		return cde;
	}

	public void setCde(CodeElement cde) {
		this.cde = cde;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getInputPathBefore() {
		return inputPathBefore;
	}

	public void setInputPathBefore(String inputPathBefore) {
		this.inputPathBefore = inputPathBefore;
	}

	public String getInputNameBefore() {
		return inputNameBefore;
	}

	public void setInputNameBefore(String inputNameBefore) {
		this.inputNameBefore = inputNameBefore;
	}

	public String getInputPathAfter() {
		return inputPathAfter;
	}

	public void setInputPathAfter(String inputPathAfter) {
		this.inputPathAfter = inputPathAfter;
	}

	public String getInputNameAfter() {
		return inputNameAfter;
	}

	public void setInputNameAfter(String inputNameAfter) {
		this.inputNameAfter = inputNameAfter;
	}

	public JavaProject getJpjBefore() {
		return jpjBefore;
	}

	public void setJpjBefore(JavaProject jpjBefore) {
		this.jpjBefore = jpjBefore;
	}

	public JavaProject getJpjAfter() {
		return jpjAfter;
	}

	public void setJpjAfter(JavaProject jpjAfter) {
		this.jpjAfter = jpjAfter;
	}

	public ModelBuilder getMbBefore() {
		return mbBefore;
	}

	public void setMbBefore(ModelBuilder mbBefore) {
		this.mbBefore = mbBefore;
	}

	public ModelBuilder getMbAfter() {
		return mbAfter;
	}

	public void setMbAfter(ModelBuilder mbAfter) {
		this.mbAfter = mbAfter;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashSet<ChangedFileUnit> getCgFileUnits() {
		return cgFileUnits;
	}

	public void setCgFileUnits(HashSet<ChangedFileUnit> cgFileUnits) {
		this.cgFileUnits = cgFileUnits;
	}
	
	
	

}
