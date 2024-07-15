package jwrmodel;

import java.util.HashSet;

public class Str_ChangedFieldUnit {
	
	String instId="";
	String name="";
	String qualifiedName="";
	String type ="";
	String returnType="";
	String sourceBefore="";
	String sourceAfter = "";
	boolean isTest;
	
	HashSet<String> accessMethodsBefore;
	HashSet<String> accessMethodsAfter;
	
	public Str_ChangedFieldUnit()
	{
		
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

	public boolean isTest() {
		return isTest;
	}

	public void setTest(boolean isTest) {
		this.isTest = isTest;
	}

	public HashSet<String> getAccessMethodsBefore() {
		return accessMethodsBefore;
	}

	public void setAccessMethodsBefore(HashSet<String> accessMethodsBefore) {
		this.accessMethodsBefore = accessMethodsBefore;
	}

	public HashSet<String> getAccessMethodsAfter() {
		return accessMethodsAfter;
	}

	public void setAccessMethodsAfter(HashSet<String> accessMethodsAfter) {
		this.accessMethodsAfter = accessMethodsAfter;
	}
	
	
	

}
