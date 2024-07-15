package jxp3model;

import java.util.HashSet;

import org.jtool.tinyjxplatform.srcmodel.JavaClass;
import org.jtool.tinyjxplatform.srcmodel.JavaFile;

public class ChangedFileUnit {
	
	String instId = "";
	String name="";
	String type="";//[add, delete, change]
	JavaFile jfBefore;
	JavaFile jfAfter;
	String sourceBefore="";
	String sourceAfter="";
	String pathBefore="";
	String pathAfter="";
	boolean isTest=false;
	
	HashSet<ChangedClassUnit> cgClassUnits;
	
	public ChangedFileUnit()
	{
		cgClassUnits = new HashSet<>();
	}
	
	void build()
	{
		this.buildName();
		this.buildIsTest();
		this.buildPath();
		this.buildSource();
		this.buildCgClasses();
	}
	
	void buildName()
	{
		if(this.type.equals("delete"))
		{
			this.name = this.jfBefore.getName();
		}else if(this.type.equals("add"))
		{
			this.name = this.jfAfter.getName();
		}else if(this.type.equals("change"))
		{
			this.name = this.jfAfter.getName();
		}
	}
	
	void buildIsTest()
	{
		if(this.type.equals("delete"))
		{
			if(this.jfBefore.getSource().contains("@Test"))
			{
				this.isTest = true;
			}
			
		}else if(this.type.equals("add"))
		{
			if(this.jfAfter.getSource().contains("@Test"))
			{
				this.isTest = true;
			}
		}else if(this.type.equals("change"))
		{
			if(this.jfBefore.getSource().contains("@Test") || this.jfAfter.getSource().contains("@Test"))
			{
				this.isTest = true;
			}
		}
	}
	
	void buildPath()
	{
		if(this.type.equals("delete"))
		{
			this.pathBefore = this.jfBefore.getPath();
		}else if(this.type.equals("add"))
		{
			this.pathAfter = this.jfAfter.getPath();
		}else if(this.type.equals("change"))
		{
			this.pathBefore = this.jfBefore.getPath();
			this.pathAfter = this.jfAfter.getPath();
		}
	}
	
	void buildSource()
	{
		if(this.type.equals("delete"))
		{
			this.sourceBefore = this.jfBefore.getSource();
		}else if(this.type.equals("add"))
		{
			this.sourceAfter = this.jfAfter.getSource();
		}else if(this.type.equals("change"))
		{
			this.sourceBefore = this.jfBefore.getSource();
			this.sourceAfter = this.jfAfter.getSource();
		}
	}
	
	void buildCgClasses()
	{
		int CgClassesCounter = -1;
		if(this.type.equals("delete"))
		{
			if(this.jfBefore.getClasses().isEmpty()==false)
			{
				for(JavaClass jcli : this.jfBefore.getClasses())
				{
					ChangedClassUnit cgclu = new ChangedClassUnit();
					CgClassesCounter ++;
					cgclu.instId = "changedClassUnit"+CgClassesCounter;
					cgclu.jclsBefore = jcli;
					cgclu.type = "delete";
					cgclu.build();
					this.cgClassUnits.add(cgclu);
				}
				
			}
		}else if(this.type.equals("add"))
		{
			if(this.jfAfter.getClasses().isEmpty()==false)
			{
				for(JavaClass jcli : this.jfAfter.getClasses())
				{
					ChangedClassUnit cgclu = new ChangedClassUnit();
					CgClassesCounter ++;
					cgclu.instId = "changedClassUnit"+CgClassesCounter;
					cgclu.jclsAfter = jcli;
					cgclu.type = "add";
					cgclu.build();
					this.cgClassUnits.add(cgclu);
				}
			}
		}else if(this.type.equals("change"))
		{
			HashSet<JavaClass> jclsb = new HashSet<>();
			jclsb.addAll(this.jfBefore.getClasses());
			HashSet<JavaClass> jclsa = new HashSet<>();
			jclsa.addAll(this.jfAfter.getClasses());
			
			if(jclsb.isEmpty()==false || jclsa.isEmpty()==false)
			{
				for(JavaClass jcl : jclsb)
				{
					if(this.checkinClasses(jcl, jclsa)==false)
					{
						ChangedClassUnit cgclu = new ChangedClassUnit();
						CgClassesCounter ++;
						cgclu.instId = "changedClassUnit"+CgClassesCounter;
						cgclu.jclsBefore = jcl;
						cgclu.type = "delete";
						cgclu.build();
						this.cgClassUnits.add(cgclu);
					}
				}
				
				for(JavaClass jcl : jclsa)
				{
					if(this.checkinClasses(jcl, jclsb)==false)
					{
						ChangedClassUnit cgclu = new ChangedClassUnit();
						CgClassesCounter ++;
						cgclu.instId = "changedClassUnit"+CgClassesCounter;
						cgclu.jclsAfter = jcl;
						cgclu.type = "add";
						cgclu.build();
						this.cgClassUnits.add(cgclu);
					}
				}
				
				for(JavaClass jclb :jclsb)
				{
					for(JavaClass jcla : jclsa)
					{
						if(jclb.getQualifiedName().toString().equals(jcla.getQualifiedName().toString()) && jclb.getSource().equals(jcla.getSource())==false )
						{
							ChangedClassUnit cgclu = new ChangedClassUnit();
							CgClassesCounter ++;
							cgclu.instId = "changedClassUnit"+CgClassesCounter;
							cgclu.jclsBefore = jclb;
							cgclu.jclsAfter = jcla;
							cgclu.type = "change";
							cgclu.build();
							this.cgClassUnits.add(cgclu);
						}
					}
				}
			}
		}
	}
	
	boolean checkinClasses(JavaClass jcl , HashSet<JavaClass> jcls)
	{
		for(JavaClass jcli : jcls)
		{
			if(jcli.getQualifiedName().toString().equals(jcl.getQualifiedName().toString()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	void printCgFlUnit()
	{
		System.out.println();
		System.out.println("---ChangedFileUnit----------------");
		System.out.println("   CgflUnit instId :"+this.instId);
		System.out.println("   CgflUnit Name : "+this.name);
		System.out.println("   CgflUnit Type : "+this.type);
		System.out.println("   CgflUnit SourceBefore : "+this.sourceBefore);
		System.out.println("   CgflUnit SourceAfter : "+this.sourceAfter);
		System.out.println("   CgflUnit PathBefore : "+this.pathBefore);
		System.out.println("   CgflUnit PathAfter : "+this.pathAfter);
		System.out.println("   CgflUnit IsTest : "+this.isTest);
		for(ChangedClassUnit cgclsui : this.cgClassUnits )
		{
			cgclsui.printCgClsUnit();
		}
		System.out.println("---=========File End========----------------");
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JavaFile getJfBefore() {
		return jfBefore;
	}

	public void setJfBefore(JavaFile jfBefore) {
		this.jfBefore = jfBefore;
	}

	public JavaFile getJfAfter() {
		return jfAfter;
	}

	public void setJfAfter(JavaFile jfAfter) {
		this.jfAfter = jfAfter;
	}

	public String getSourceBefore() {
		return sourceBefore;
	}

	public void setSourceBefore(String sourceBefore) {
		this.sourceBefore = sourceBefore;
	}

	public String getSourceAfter() {
		return sourceAfter;
	}

	public void setSourceAfter(String sourceAfter) {
		this.sourceAfter = sourceAfter;
	}

	public String getPathBefore() {
		return pathBefore;
	}

	public void setPathBefore(String pathBefore) {
		this.pathBefore = pathBefore;
	}

	public String getPathAfter() {
		return pathAfter;
	}

	public void setPathAfter(String pathAfter) {
		this.pathAfter = pathAfter;
	}

	public boolean isTest() {
		return isTest;
	}

	public void setTest(boolean isTest) {
		this.isTest = isTest;
	}

	public HashSet<ChangedClassUnit> getCgClassUnits() {
		return cgClassUnits;
	}

	public void setCgClassUnits(HashSet<ChangedClassUnit> cgClassUnits) {
		this.cgClassUnits = cgClassUnits;
	}
	
	
	

}
