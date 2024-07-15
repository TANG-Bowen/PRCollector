package jxp3model;

import java.util.HashSet;

import org.jtool.tinyjxplatform.srcmodel.JavaField;
import org.jtool.tinyjxplatform.srcmodel.JavaMethod;

public class ChangedFieldUnit {
	
	String instId="";
	String name="";
	String qualifiedName="";
	String type ="";//[add, delete, change]
	String returnType="";
	String sourceBefore="";
	String sourceAfter = "";
	JavaField jfdBefore;
	JavaField jfdAfter;
	boolean isTest=false;
	HashSet<JavaMethod> accessMethodsBefore;
	HashSet<JavaMethod> accessMethodsAfter;
	
	HashSet<String> accessMethodSourcesBefore;
	HashSet<String> accessMethodSourcesAfter;
	
	public ChangedFieldUnit()
	{
		accessMethodsBefore = new HashSet<>();
		accessMethodsAfter = new HashSet<>();
		accessMethodSourcesBefore = new HashSet<>();
		accessMethodSourcesAfter = new HashSet<>();
	}
	
	void build()
	{
		this.buildName();
		this.buildReturnType();
		this.buildSource();
		this.buildAccessMethod();
	}
	
	void buildName()
	{
		if(this.type.equals("delete"))
		{
			this.name = this.jfdBefore.getName();
			this.qualifiedName = this.jfdBefore.getQualifiedName().toString();
		}else if(this.type.equals("add"))
		{
			this.name = this.jfdAfter.getName();
			this.qualifiedName = this.jfdAfter.getQualifiedName().toString();
		}else if(this.type.equals("change"))
		{
			this.name = this.jfdAfter.getName();
			this.qualifiedName = this.jfdAfter.getQualifiedName().toString();
		}
	}
	
	void buildReturnType()
	{
		if(this.type.equals("delete"))
		{
			this.returnType = this.jfdBefore.getType();
		}else if(this.type.equals("add"))
		{
			this.returnType = this.jfdAfter.getType();
		}else if(this.type.equals("change"))
		{
			this.returnType = this.jfdAfter.getType();
		}
	}
	
	void buildSource()
	{
		if(this.type.equals("delete"))
		{
			this.sourceBefore = this.jfdBefore.getSource();
		}else if(this.type.equals("add"))
		{
			this.sourceAfter = this.jfdAfter.getSource();
		}else if(this.type.equals("change"))
		{
			this.sourceBefore = this.jfdBefore.getSource();
			this.sourceAfter = this.jfdAfter.getSource();
		}
	}
	
	void buildAccessMethod()
	{
		if(this.type.equals("delete"))
		{
			this.accessMethodsBefore.addAll(this.jfdBefore.getAccessingMethodsInProject());
		}else if(this.type.equals("add"))
		{
			this.accessMethodsAfter.addAll(this.jfdAfter.getAccessingMethodsInProject());
		}else if(this.type.equals("change"))
		{
			this.accessMethodsBefore.addAll(this.jfdBefore.getAccessingMethodsInProject());
			this.accessMethodsAfter.addAll(this.jfdAfter.getAccessingMethodsInProject());
		}
	}
	
	void printCgFieldUnit()
	{
		System.out.println();
		System.out.println("---ChangedFieldUnit----------------");
		System.out.println("   cgFdUnit instId : "+ this.instId);
		System.out.println("   cgFdUnit Name : "+ this.name);
		System.out.println("   cgFdUnit QName : "+ this.qualifiedName);
		System.out.println("   cgFdUnit Type : "+ this.type);
		System.out.println("   cgFdUnit ReturnType : "+ this.returnType);
		System.out.println("   cgFdUnit SourceBefore : "+ this.sourceBefore);
		System.out.println("   cgFdUnit SourceAfter : "+ this.sourceAfter);
		System.out.println("   cgFdUnit IsTest : "+ this.isTest);
		for(JavaMethod jmti : this.accessMethodsBefore)
		{
			System.out.println("   cgFdUnit AccessMethodBefore : "+ jmti.getQualifiedName());
		}
		
		for(JavaMethod jmti : this.accessMethodsAfter)
		{
			System.out.println("   cgFdUnit AccessMethodAfter : "+ jmti.getQualifiedName());
		}
		System.out.println("---========Field End=========----------------");
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

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
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

	public JavaField getJfdBefore() {
		return jfdBefore;
	}

	public void setJfdBefore(JavaField jfdBefore) {
		this.jfdBefore = jfdBefore;
	}

	public JavaField getJfdAfter() {
		return jfdAfter;
	}

	public void setJfdAfter(JavaField jfdAfter) {
		this.jfdAfter = jfdAfter;
	}

	public boolean isTest() {
		return isTest;
	}

	public void setTest(boolean isTest) {
		this.isTest = isTest;
	}

	public HashSet<JavaMethod> getAccessMethodsBefore() {
		return accessMethodsBefore;
	}

	public void setAccessMethodsBefore(HashSet<JavaMethod> accessMethodsBefore) {
		this.accessMethodsBefore = accessMethodsBefore;
	}

	public HashSet<JavaMethod> getAccessMethodsAfter() {
		return accessMethodsAfter;
	}

	public void setAccessMethodsAfter(HashSet<JavaMethod> accessMethodsAfter) {
		this.accessMethodsAfter = accessMethodsAfter;
	}

	public HashSet<String> getAccessMethodSourcesBefore() {
		return accessMethodSourcesBefore;
	}

	public void setAccessMethodSourcesBefore(HashSet<String> accessMethodSourcesBefore) {
		this.accessMethodSourcesBefore = accessMethodSourcesBefore;
	}

	public HashSet<String> getAccessMethodSourcesAfter() {
		return accessMethodSourcesAfter;
	}

	public void setAccessMethodSourcesAfter(HashSet<String> accessMethodSourcesAfter) {
		this.accessMethodSourcesAfter = accessMethodSourcesAfter;
	}
	
	
	

}
