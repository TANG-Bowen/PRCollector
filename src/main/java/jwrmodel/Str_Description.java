package jwrmodel;

public class Str_Description {
	
	String instId;
	String body;
	Str_MarkdownElement smde;
	
	public Str_Description()
	{
		
	}
	
	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Str_MarkdownElement getSmde() {
		return smde;
	}

	public void setSmde(Str_MarkdownElement smde) {
		this.smde = smde;
	}

}
