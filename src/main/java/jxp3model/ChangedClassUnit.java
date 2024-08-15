package jxp3model;

import java.util.HashSet;

import org.jtool.tinyjxplatform.srcmodel.JavaClass;
import org.jtool.tinyjxplatform.srcmodel.JavaField;
import org.jtool.tinyjxplatform.srcmodel.JavaMethod;

public class ChangedClassUnit {
	
	String instId="";
	String name="";
	String qualifiedName="";
	String type="";
	String sourceBefore="";
	String sourceAfter="";
	JavaClass jclsBefore;
	JavaClass jclsAfter;
	boolean isTest=false;
	
	HashSet<ChangedMethodUnit> cgMethodUnits;
	HashSet<ChangedFieldUnit> cgFieldUnits;
	
	public ChangedClassUnit()
	{
		cgMethodUnits = new HashSet<>();
		cgFieldUnits = new HashSet<>();
	}
	
	void build()
	{
		this.buildName();
		this.buildIsTest();
		this.buildSource();
		this.buildCgMethods();
		this.buildCgFields();
	}
	
	void unBuild()
	{
		this.jclsBefore= null;
		this.jclsAfter= null;
		if(!this.cgMethodUnits.isEmpty())
		{
			for(ChangedMethodUnit cgmui : this.cgMethodUnits)
			{
				cgmui.unBuild();
			}
		}
		if(!this.cgFieldUnits.isEmpty())
		{
			for(ChangedFieldUnit cgfdi : this.cgFieldUnits)
			{
				cgfdi.unBuild();
			}
		}
	}
	
	void buildName()
	{
		if(this.type.equals("delete"))
		{
			this.name = this.jclsBefore.getName();
			this.qualifiedName = this.jclsBefore.getQualifiedName().toString();
			
		}else if(this.type.equals("add"))
		{
			this.name = this.jclsAfter.getName();
			this.qualifiedName = this.jclsAfter.getQualifiedName().toString();
			
		}else if(this.type.equals("change"))
		{
			this.name = this.jclsAfter.getName();
			this.qualifiedName = this.jclsAfter.getQualifiedName().toString();
		}
		
	}
	
	void buildIsTest()
	{
		if(this.type.equals("delete"))
		{
			if(this.jclsBefore.getSource().contains("@Test"))
			{
				this.isTest = true;
			}
		}else if(this.type.equals("add"))
		{
			if(this.jclsAfter.getSource().contains("@Test"))
			{
				this.isTest = true;
			}
		}else if(this.type.equals("change"))
		{
			if(this.jclsAfter.getSource().contains("@Test") || this.jclsAfter.getSource().contains("@Test"))
			{
				this.isTest = true;
			}
		}
	}
	
	void buildSource()
	{
		if(this.type.equals("delete"))
		{
			this.sourceBefore = this.jclsBefore.getSource();
		}else if(this.type.equals("add"))
		{
			this.sourceAfter = this.jclsAfter.getSource();
		}else if(this.type.equals("change"))
		{
			this.sourceBefore = this.jclsBefore.getSource();
			this.sourceAfter = this.jclsAfter.getSource();
		}
	}
	void buildCgMethods()
	{
		int CgMethodsCounter = -1;
		if(this.type.equals("delete"))
		{
			if(this.jclsBefore.getMethods().isEmpty()==false)
			{
				for(JavaMethod jm : this.jclsBefore.getMethods())
				{
					ChangedMethodUnit cgmu = new ChangedMethodUnit();
					CgMethodsCounter++;
					cgmu.instId = "changedMethodUnit"+CgMethodsCounter;
					cgmu.jmtBefore = jm;
					cgmu.type = "delete";
					cgmu.build();
					this.cgMethodUnits.add(cgmu);
				}
			}
			
		}else if(this.type.equals("add"))
		{
			if(this.jclsAfter.getMethods().isEmpty()==false)
			{
			  for(JavaMethod jm : this.jclsAfter.getMethods())
			  {
				ChangedMethodUnit cgmu = new ChangedMethodUnit();
				CgMethodsCounter++;
				cgmu.instId = "changedMethodUnit"+CgMethodsCounter;
				cgmu.jmtAfter = jm;
				cgmu.type = "add";
				cgmu.build();
				this.cgMethodUnits.add(cgmu);
			  }
			}
			
		}else if(this.type.equals("change"))
		{
			HashSet<JavaMethod> msb = new HashSet<>();
			HashSet<JavaMethod> msa = new HashSet<>();
			msb.addAll(this.jclsBefore.getMethods());
			msa.addAll(this.jclsAfter.getMethods());
			
			if(msb.isEmpty()==false || msa.isEmpty() == false)
			{
				for(JavaMethod jm : msb)
				{
					if(this.checkinMethods(jm, msa) == false)
					{
						ChangedMethodUnit cgmu = new ChangedMethodUnit();
						CgMethodsCounter++;
						cgmu.instId = "changedMethodUnit"+CgMethodsCounter;
						cgmu.jmtBefore = jm;
						cgmu.type = "delete";
						cgmu.build();
						this.cgMethodUnits.add(cgmu);
					}
				}
				
				for(JavaMethod jm :msa)
				{
					if(this.checkinMethods(jm, msb)== false)
					{
						ChangedMethodUnit cgmu = new ChangedMethodUnit();
						CgMethodsCounter++;
						cgmu.instId = "changedMethodUnit"+CgMethodsCounter;
						cgmu.jmtAfter = jm;
						cgmu.type = "add";
						cgmu.build();
						this.cgMethodUnits.add(cgmu);
					}
				}
				
				for(JavaMethod jmb : msb)
				{
					for(JavaMethod jma : msa)
					{
						if(jmb.getQualifiedName().toString().equals(jma.getQualifiedName().toString()) && jmb.getSignature().equals(jma.getSignature()) && jmb.getReturnType().equals(jma.getReturnType()) )
						{
							if(jmb.getSource().equals(jma.getSource()) == false)
							{
							  ChangedMethodUnit cgmu = new ChangedMethodUnit();
							  CgMethodsCounter++;
							  cgmu.instId = "changedMethodUnit"+CgMethodsCounter;
							  cgmu.jmtBefore = jmb;
							  cgmu.jmtAfter = jma;
							  cgmu.type = "change";
							  cgmu.build();
							  this.cgMethodUnits.add(cgmu);
							}
						}
					}
				}
				
			}
			
		}
	}
	
	boolean checkinMethods(JavaMethod jm , HashSet<JavaMethod> jms)
	{
		for(JavaMethod jmi : jms)
		{
			if(jmi.getQualifiedName().toString().equals(jm.getQualifiedName().toString())  && jmi.getSignature().equals(jm.getSignature()) && jmi.getReturnType().equals(jm.getReturnType()) )
			{
				return true;
			}
		}
		return false;
	}

	void buildCgFields()
	{
		int CgFieldsCounter = -1 ;
		if(this.type.equals("delete"))
		{
			if(this.jclsBefore.getFields().isEmpty()==false)
			{
				for(JavaField jfd : this.jclsBefore.getFields())
				{
					ChangedFieldUnit cgfdu = new ChangedFieldUnit();
					CgFieldsCounter ++ ;
					cgfdu.instId = "changedFieldUnit"+CgFieldsCounter;
					cgfdu.jfdBefore = jfd;
					cgfdu.type = "delete";
					if(this.isTest==true)
					{
						cgfdu.isTest = true;
					}
					cgfdu.build();
					this.cgFieldUnits.add(cgfdu);
				}
			}
		}else if(this.type.equals("add"))
		{
			if(this.jclsAfter.getFields().isEmpty()==false)
			{
				for(JavaField jfd :this.jclsAfter.getFields())
				{
					ChangedFieldUnit cgfdu = new ChangedFieldUnit();
					CgFieldsCounter ++ ;
					cgfdu.instId = "changedFieldUnit"+CgFieldsCounter;
					cgfdu.jfdAfter = jfd;
					cgfdu.type = "add";
					if(this.isTest==true)
					{
						cgfdu.isTest = true;
					}
					cgfdu.build();
					this.cgFieldUnits.add(cgfdu);
				}
			}
		}else if(this.type.equals("change"))
		{
			HashSet<JavaField> jfdsb = new HashSet<>();
			HashSet<JavaField> jfdsa = new HashSet<>();
			jfdsb.addAll(this.jclsBefore.getFields());
			jfdsa.addAll(this.jclsAfter.getFields());
			
			if(jfdsb.isEmpty()==false || jfdsa.isEmpty()==false)
			{
				for(JavaField jfd : jfdsb)
				{
					if(this.checkinFields(jfd, jfdsa)==false)
					{
						ChangedFieldUnit cgfdu = new ChangedFieldUnit();
						CgFieldsCounter ++ ;
						cgfdu.instId = "changedFieldUnit"+CgFieldsCounter;
						cgfdu.jfdBefore = jfd;
						cgfdu.type = "delete";
						if(this.isTest==true)
						{
							cgfdu.isTest = true;
						}
						cgfdu.build();
						this.cgFieldUnits.add(cgfdu);
					}
				}
				
				for(JavaField jfd : jfdsa)
				{
					if(this.checkinFields(jfd, jfdsb)== false)
					{
						ChangedFieldUnit cgfdu = new ChangedFieldUnit();
						CgFieldsCounter ++ ;
						cgfdu.instId = "changedFieldUnit"+CgFieldsCounter;
						cgfdu.jfdAfter = jfd;
						cgfdu.type = "add";
						if(this.isTest==true)
						{
							cgfdu.isTest = true;
						}
						cgfdu.build();
						this.cgFieldUnits.add(cgfdu);
					}
				}
				
				for(JavaField jfdb : jfdsb)
				{
					for(JavaField jfda : jfdsa)
					{
						if(jfdb.getQualifiedName().toString().equals(jfda.getQualifiedName().toString()) && jfdb.getType().equals(jfda.getType()) && jfdb.getSource().equals(jfda.getSource())==false )
						{
							ChangedFieldUnit cgfdu = new ChangedFieldUnit();
							CgFieldsCounter ++ ;
							cgfdu.instId = "changedFieldUnit"+CgFieldsCounter;
							cgfdu.jfdBefore = jfdb;
							cgfdu.jfdAfter = jfda;
							cgfdu.type = "change";
							if(this.isTest==true)
							{
								cgfdu.isTest = true;
							}
							cgfdu.build();
							this.cgFieldUnits.add(cgfdu);
						}
					}
				}
				
			}
			
		}
	}
	
	boolean checkinFields(JavaField jfd , HashSet<JavaField> jfds)
	{
		for(JavaField jfdi : jfds)
		{
			if(jfdi.getQualifiedName().toString().equals(jfd.getQualifiedName().toString()) && jfdi.getType().equals(jfd.getType()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	void printCgClsUnit()
	{
		System.out.println();
		System.out.println("---ChangedClassUnit----------------");
		System.out.println("   CgClsUnit instId : "+this.instId);
		System.out.println("   CgClsUnit Name : "+this.name);
		System.out.println("   CgClsUnit QName : "+this.qualifiedName);
		System.out.println("   CgClsUnit Type : "+this.type);
		System.out.println("   CgClsUnit SourceBefore : "+this.sourceBefore);
		System.out.println("   CgClsUnit SourceAfter : "+this.sourceAfter);
		System.out.println("   CgClsUnit IsTest : "+this.isTest);
		
		if(!this.cgMethodUnits.isEmpty())
		{
		    for(ChangedMethodUnit cgmti : this.cgMethodUnits)
		   {
			  cgmti.printCgMdUnit();
		   }
		}else {
			System.out.println("No ChangedMethodUnits !");
		}
		
		if(!this.cgFieldUnits.isEmpty())
		{
		    for(ChangedFieldUnit cgfdi : this.cgFieldUnits)
		   {
			  cgfdi.printCgFieldUnit();
		   }
		}else {
			System.out.println("No ChangedFieldUnits !");
		}
		
		
		
		System.out.println("---=========Class End========----------------");
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

	public String getQualifiedName() {
		return qualifiedName;
	}

	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public JavaClass getJclsBefore() {
		return jclsBefore;
	}

	public void setJclsBefore(JavaClass jclsBefore) {
		this.jclsBefore = jclsBefore;
	}

	public JavaClass getJclsAfter() {
		return jclsAfter;
	}

	public void setJclsAfter(JavaClass jclsAfter) {
		this.jclsAfter = jclsAfter;
	}

	public boolean isTest() {
		return isTest;
	}

	public void setTest(boolean isTest) {
		this.isTest = isTest;
	}

	public HashSet<ChangedMethodUnit> getCgMethodUnits() {
		return cgMethodUnits;
	}

	public void setCgMethodUnits(HashSet<ChangedMethodUnit> cgMethodUnits) {
		this.cgMethodUnits = cgMethodUnits;
	}

	public HashSet<ChangedFieldUnit> getCgFieldUnits() {
		return cgFieldUnits;
	}

	public void setCgFieldUnits(HashSet<ChangedFieldUnit> cgFieldUnits) {
		this.cgFieldUnits = cgFieldUnits;
	}
	
	
	
	
}
