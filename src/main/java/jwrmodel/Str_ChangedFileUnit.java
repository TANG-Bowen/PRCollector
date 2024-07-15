package jwrmodel;

import java.util.HashSet;

public class Str_ChangedFileUnit {

	String instId = "";
	String name="";
	String type="";
	String sourceBefore="";
	String sourceAfter="";
	String pathBefore="";
	String pathAfter="";
	boolean isTest;
	
	HashSet<Str_ChangedClassUnit> cgClassUnits;
	
	public Str_ChangedFileUnit()
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

	public HashSet<Str_ChangedClassUnit> getCgClassUnits() {
		return cgClassUnits;
	}

	public void setCgClassUnits(HashSet<Str_ChangedClassUnit> cgClassUnits) {
		this.cgClassUnits = cgClassUnits;
	}
	
	
	
}
