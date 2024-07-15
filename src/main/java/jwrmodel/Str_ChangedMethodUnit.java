package jwrmodel;

import java.util.HashSet;

public class Str_ChangedMethodUnit {
	
	String instId ="";
	String name="";
	String qualifiedName="";
	String type = "";
	String returnType="";
	String signatureBefore="";
	String signatureAfter="";
	String sourceBefore="";
	String sourceAfter="";
	boolean isTest;
	
	HashSet<String> CallingMethodsBefore;
	HashSet<String> CallingMethodsAfter;
	HashSet<String> CalledMethodsBefore;
	HashSet<String> CalledMethodsAfter;
	
	HashSet<String> callingMethodSourcesBefore;
	HashSet<String> callingMethodsSourcesAfter;
	HashSet<String> calledMethodSourcesBefore;
	HashSet<String> calledMethodSourcesAfter;
	
	public Str_ChangedMethodUnit()
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

	public boolean isTest() {
		return isTest;
	}

	public void setTest(boolean isTest) {
		this.isTest = isTest;
	}

	public HashSet<String> getCallingMethodsBefore() {
		return CallingMethodsBefore;
	}

	public void setCallingMethodsBefore(HashSet<String> callingMethodsBefore) {
		CallingMethodsBefore = callingMethodsBefore;
	}

	public HashSet<String> getCallingMethodsAfter() {
		return CallingMethodsAfter;
	}

	public void setCallingMethodsAfter(HashSet<String> callingMethodsAfter) {
		CallingMethodsAfter = callingMethodsAfter;
	}

	public HashSet<String> getCalledMethodsBefore() {
		return CalledMethodsBefore;
	}

	public void setCalledMethodsBefore(HashSet<String> calledMethodsBefore) {
		CalledMethodsBefore = calledMethodsBefore;
	}

	public HashSet<String> getCalledMethodsAfter() {
		return CalledMethodsAfter;
	}

	public void setCalledMethodsAfter(HashSet<String> calledMethodsAfter) {
		CalledMethodsAfter = calledMethodsAfter;
	}

	public HashSet<String> getCallingMethodSourcesBefore() {
		return callingMethodSourcesBefore;
	}

	public void setCallingMethodSourcesBefore(HashSet<String> callingMethodSourcesBefore) {
		this.callingMethodSourcesBefore = callingMethodSourcesBefore;
	}

	public HashSet<String> getCallingMethodsSourcesAfter() {
		return callingMethodsSourcesAfter;
	}

	public void setCallingMethodsSourcesAfter(HashSet<String> callingMethodsSourcesAfter) {
		this.callingMethodsSourcesAfter = callingMethodsSourcesAfter;
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
