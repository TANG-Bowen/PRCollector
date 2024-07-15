package jwrmodel;

import java.util.HashSet;

public class Str_ChangedProjectUnit {
	
	//cde
	String instId="";
	String inputPathBefore="";
	String inputNameBefore="";
	String inputPathAfter="";
	String inputNameAfter="";
	String type="";
	String name="";
	HashSet<Str_ChangedFileUnit> cgFileUnits;
	
	public Str_ChangedProjectUnit()
	{
		
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

	public HashSet<Str_ChangedFileUnit> getCgFileUnits() {
		return cgFileUnits;
	}

	public void setCgFileUnits(HashSet<Str_ChangedFileUnit> cgFileUnits) {
		this.cgFileUnits = cgFileUnits;
	}
	
	

}
