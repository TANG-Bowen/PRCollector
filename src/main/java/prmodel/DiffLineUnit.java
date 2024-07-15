package prmodel;

public class DiffLineUnit{
	
	String text="";
	String type="";
	
	public DiffLineUnit()
	{
		
	}
	
	void printDiffLineUnit()
	{
		System.out.println();
		System.out.println("  DiffLine : "+this.type + " : "+this.text);
		System.out.println("<----------------->");
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

		

}
