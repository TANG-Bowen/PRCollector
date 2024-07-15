package jwrmodel;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Str_CodeElement {
	
	
	ArrayList<Str_ChangedProjectUnit> cgProjectUnits;
	LinkedHashSet<String> pjNameIndex;
	String instId;
	
	public Str_CodeElement()
	{
		
	}

	public ArrayList<Str_ChangedProjectUnit> getCgProjectUnits() {
		return cgProjectUnits;
	}

	public void setCgProjectUnits(ArrayList<Str_ChangedProjectUnit> cgProjectUnits) {
		this.cgProjectUnits = cgProjectUnits;
	}

	public LinkedHashSet<String> getPjNameIndex() {
		return pjNameIndex;
	}

	public void setPjNameIndex(LinkedHashSet<String> pjNameIndex) {
		this.pjNameIndex = pjNameIndex;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}
	
	
	

}
