package jxp3model;

import java.util.HashSet;
import java.util.Set;

import org.jtool.tinyjxplatform.srcmodel.JavaMethod;

public class ChangedMethodUnit {
	
	String instId ="";
	String name="";
	String qualifiedName="";
	String type = "";
	String returnType="";
	String signatureBefore="";
	String signatureAfter="";
	String sourceBefore="";
	String sourceAfter="";
	
	JavaMethod jmtBefore;
	JavaMethod jmtAfter;
	boolean isTest=false;
	
	HashSet<JavaMethod> callingMethodsBefore;
	HashSet<JavaMethod> callingMethodsAfter;
	HashSet<JavaMethod> calledMethodsBefore;
	HashSet<JavaMethod> calledMethodsAfter;
	
	HashSet<String> callingMethodSourcesBefore;
	HashSet<String> callingMethodSourcesAfter;
	HashSet<String> calledMethodSourcesBefore;
	HashSet<String> calledMethodSourcesAfter;
	
	public ChangedMethodUnit()
	{
		callingMethodsBefore = new HashSet<>();
		callingMethodsAfter = new HashSet<>();
		calledMethodsBefore = new HashSet<>();
		calledMethodsAfter = new HashSet<>();
		callingMethodSourcesBefore = new HashSet<>();
		callingMethodSourcesAfter = new HashSet<>();
		calledMethodSourcesBefore = new HashSet<>();
		calledMethodSourcesAfter = new HashSet<>();
		
	}
	
	void build()
	{
		this.buildName();
		this.buildSignature();
		this.buildSource();
		this.buildIsTest();
		this.buildCallingAndCalled();
	}
	
	void buildName()
	{
		if(this.type.equals("delete"))
		{
			this.name = this.jmtBefore.getName();
			this.qualifiedName = this.jmtBefore.getQualifiedName().toString();
		}else if(this.type.equals("add"))
		{
			this.name = this.jmtAfter.getName();
			this.qualifiedName = this.jmtAfter.getQualifiedName().toString();
		}else if(this.type.equals("change"))
		{
			this.name = this.jmtAfter.getName();
			this.qualifiedName = this.jmtAfter.getQualifiedName().toString();
		}
	}
	
	void buildSignature()
	{
		if(this.type.equals("delete"))
		{
			this.signatureBefore = this.jmtBefore.getSignature();
			this.returnType = this.jmtBefore.getReturnType();
		}else if(this.type.equals("add"))
		{
			this.signatureAfter = this.jmtAfter.getSignature();
			this.returnType = this.jmtAfter.getReturnType();
		}else if(this.type.equals("change"))
		{
			this.signatureBefore = this.jmtBefore.getSignature();
			this.signatureAfter = this.jmtAfter.getSignature();
			this.returnType = this.jmtAfter.getReturnType();
		}
	}
	
	void buildSource()
	{
		if(this.type.equals("delete"))
		{
			this.sourceBefore = this.jmtBefore.getSource();
		}else if(this.type.equals("add"))
		{
			this.sourceAfter = this.jmtAfter.getSource();
		}else if(this.type.equals("change"))
		{
			this.sourceBefore = this.jmtBefore.getSource();
			this.sourceAfter = this.jmtAfter.getSource();
		}
	}
	
	void buildIsTest()
	{
		if(this.type.equals("delete"))
		{
			if(this.sourceBefore.contains("@Test"))
			{
				this.isTest = true;
			}
		}else if(this.type.equals("add"))
		{
			if(this.sourceAfter.contains("@Test"))
			{
				this.isTest = true;
			}
		}else if(this.type.equals("change"))
		{
			if(this.sourceBefore.contains("@Test") || this.sourceAfter.contains("@Test"))
			{
				this.isTest = true;
			}
		}
	}
	
	void buildCallingAndCalled()
	{
		if(this.type.equals("delete"))
		{
			this.callingMethodsBefore.addAll(this.jmtBefore.getCallingMethodsInProject());
			this.calledMethodsBefore.addAll(this.jmtBefore.getCalledMethodsInProject());
		}else if(this.type.equals("add"))
		{
			this.callingMethodsAfter.addAll(this.jmtAfter.getCallingMethodsInProject());
			this.calledMethodsAfter.addAll(this.jmtAfter.getCalledMethodsInProject());
		}else if(this.type.equals("change"))
		{
			this.callingMethodsBefore.addAll(this.jmtBefore.getCallingMethodsInProject());
			this.calledMethodsBefore.addAll(this.jmtBefore.getCalledMethodsInProject());
			this.callingMethodsAfter.addAll(this.jmtAfter.getCallingMethodsInProject());
			this.calledMethodsAfter.addAll(this.jmtAfter.getCalledMethodsInProject());
		}
	}
	
	void printCgMdUnit()
	{
		System.out.println();
		System.out.println("---ChangedMethodUnit----------------");
		System.out.println("   CgMdUnit instId : "+this.instId);
		System.out.println("   CgMdUnit Name : "+this.name);
		System.out.println("   CgMdUnit QName : "+this.qualifiedName);
		System.out.println("   CgMdUnit Type : "+this.type);
		System.out.println("   CgMdUnit ReturnType : "+this.returnType);
		System.out.println("   CgMdUnit SignatureBefore : "+this.signatureBefore);
		System.out.println("   CgMdUnit SignatureAfter : "+this.signatureAfter);
		System.out.println("   CgMdUnit SourceBefore : "+this.sourceBefore);
		System.out.println("   CgMdUnit SourceAfter : "+this.sourceAfter);
		System.out.println("   CgMdUnit IsTest : "+this.isTest);
		
		for(JavaMethod jmti : this.calledMethodsBefore)
		{
			System.out.println("   CgMdUnit CalledMethodBefore : "+jmti.getQualifiedName());
		}
		
		for(JavaMethod jmti : this.callingMethodsBefore)
		{
			System.out.println("   CgMdUnit CallingMethodBefore : "+jmti.getQualifiedName());
		}
		
		for(JavaMethod jmti : this.calledMethodsAfter)
		{
			System.out.println("   CgMdUnit CalledMethodAfter : "+jmti.getQualifiedName());
		}
		
		for(JavaMethod jmti : this.callingMethodsAfter)
		{
			System.out.println("   CgMdUnit CallingMethodAfter : "+jmti.getQualifiedName());
		}
		System.out.println("---=========Method Unit========----------------");
		
	}
	
	public void callBuildTestOutput(Set<JavaMethod> callAimMethods, String buildName)
	{
		if(!callAimMethods.isEmpty())
		{
			for(JavaMethod jmi : callAimMethods)
			{
				System.out.println("BuildTestMethod : ");
				System.out.println("MethodName : "+ jmi.getQualifiedName());
				System.out.println("MethodSource :"+ jmi.getSource());
			}
			
		}else {
			System.out.println("CALL BUILD TEST OUT IS EMPTY!  " +buildName);
		}
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

	public String getSignatureBefore() {
		return signatureBefore;
	}

	public void setSignatureBefore(String signatureBefore) {
		this.signatureBefore = signatureBefore;
	}

	public String getSignatureAfter() {
		return signatureAfter;
	}

	public void setSignatureAfter(String signatureAfter) {
		this.signatureAfter = signatureAfter;
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

	public JavaMethod getJmtBefore() {
		return jmtBefore;
	}

	public void setJmtBefore(JavaMethod jmtBefore) {
		this.jmtBefore = jmtBefore;
	}

	public JavaMethod getJmtAfter() {
		return jmtAfter;
	}

	public void setJmtAfter(JavaMethod jmtAfter) {
		this.jmtAfter = jmtAfter;
	}

	public boolean isTest() {
		return isTest;
	}

	public void setTest(boolean isTest) {
		this.isTest = isTest;
	}

	public HashSet<JavaMethod> getCallingMethodsBefore() {
		return callingMethodsBefore;
	}

	public void setCallingMethodsBefore(HashSet<JavaMethod> callingMethodsBefore) {
		this.callingMethodsBefore = callingMethodsBefore;
	}

	public HashSet<JavaMethod> getCallingMethodsAfter() {
		return callingMethodsAfter;
	}

	public void setCallingMethodsAfter(HashSet<JavaMethod> callingMethodsAfter) {
		this.callingMethodsAfter = callingMethodsAfter;
	}

	public HashSet<JavaMethod> getCalledMethodsBefore() {
		return calledMethodsBefore;
	}

	public void setCalledMethodsBefore(HashSet<JavaMethod> calledMethodsBefore) {
		this.calledMethodsBefore = calledMethodsBefore;
	}

	public HashSet<JavaMethod> getCalledMethodsAfter() {
		return calledMethodsAfter;
	}

	public void setCalledMethodsAfter(HashSet<JavaMethod> calledMethodsAfter) {
		this.calledMethodsAfter = calledMethodsAfter;
	}

	public HashSet<String> getCallingMethodSourcesBefore() {
		return callingMethodSourcesBefore;
	}

	public void setCallingMethodSourcesBefore(HashSet<String> callingMethodSourcesBefore) {
		this.callingMethodSourcesBefore = callingMethodSourcesBefore;
	}

	public HashSet<String> getCallingMethodSourcesAfter() {
		return callingMethodSourcesAfter;
	}

	public void setCallingMethodSourcesAfter(HashSet<String> callingMethodSourcesAfter) {
		this.callingMethodSourcesAfter = callingMethodSourcesAfter;
	}

	public HashSet<String> getCalledMethodSourcesBefore() {
		return calledMethodSourcesBefore;
	}

	public void setCalledMethodSourcesBefore(HashSet<String> calledMethodSourcesBefore) {
		this.calledMethodSourcesBefore = calledMethodSourcesBefore;
	}

	public HashSet<String> getCalledMethodSourcesAfter() {
		return calledMethodSourcesAfter;
	}

	public void setCalledMethodSourcesAfter(HashSet<String> calledMethodSourcesAfter) {
		this.calledMethodSourcesAfter = calledMethodSourcesAfter;
	}
	
	
	
	

}
