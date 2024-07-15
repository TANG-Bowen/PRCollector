package prmodel;

public class Description {
	
	String instId="";
	String body="";
	PullRequest pr;
	MarkdownElement mde;
	
	public Description(PullRequest pr)
	{
		this.pr = pr;
	}
	
	void printDescription()
	{
		System.out.println();
		System.out.println("Description info : ");
		System.out.println(this.body);
		this.mde.printElements();
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

	public PullRequest getPr() {
		return pr;
	}

	public void setPr(PullRequest pr) {
		this.pr = pr;
	}

	public MarkdownElement getMde() {
		return mde;
	}

	public void setMde(MarkdownElement mde) {
		this.mde = mde;
	}

	
}
