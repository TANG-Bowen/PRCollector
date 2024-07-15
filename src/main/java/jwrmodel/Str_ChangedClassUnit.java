package jwrmodel;

import java.util.HashSet;

public class Str_ChangedClassUnit {
	
	String instId="";
	String name="";
	String qualifiedName="";
	String type="";
	String sourceBefore="";
	String sourceAfter="";
	boolean isTest;
	
	HashSet<Str_ChangedMethodUnit> cgMethodUnits;
	HashSet<Str_ChangedFieldUnit> cgFieldUnits;
	
	public Str_ChangedClassUnit()
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

	public HashSet<Str_ChangedMethodUnit> getCgMethodUnits() {
		return cgMethodUnits;
	}

	public void setCgMethodUnits(HashSet<Str_ChangedMethodUnit> cgMethodUnits) {
		this.cgMethodUnits = cgMethodUnits;
	}

	public HashSet<Str_ChangedFieldUnit> getCgFieldUnits() {
		return cgFieldUnits;
	}

	public void setCgFieldUnits(HashSet<Str_ChangedFieldUnit> cgFieldUnits) {
		this.cgFieldUnits = cgFieldUnits;
	}
	
	
	

}
