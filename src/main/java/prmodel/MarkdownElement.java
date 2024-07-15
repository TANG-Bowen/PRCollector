package prmodel;

import java.util.ArrayList;

public class MarkdownElement {
	
	ArrayList<String> headingStrings;//# 
	ArrayList<String> boldStrings;//**
	ArrayList<String> italicStrings;//_
	ArrayList<String> quoteStrings;//>
	ArrayList<String> linkStrings;//[]()
	ArrayList<String> codeStrings;//`
	ArrayList<String> codeBlockStrings;//```
	ArrayList<String> textStrings;
	ArrayList<String> mentionStrings;//@
	
	ArrayList<String> textLinkStrings;
	ArrayList<String> pullLinkStrings;//gh-num
	ArrayList<String> issueLinkStrings;//#num
	
	ArrayList<String> htmlComments;//<!-- ...-->
	
	public MarkdownElement()
	{
		headingStrings = new ArrayList<>();
		boldStrings = new ArrayList<>();
		italicStrings = new ArrayList<>();
		quoteStrings = new ArrayList<>();
		linkStrings = new ArrayList<>();
		codeStrings = new ArrayList<>();
		codeBlockStrings = new ArrayList<>();
		textStrings = new ArrayList<>();
		mentionStrings = new ArrayList<>();
		textLinkStrings = new ArrayList<>();
		pullLinkStrings = new ArrayList<>();
		issueLinkStrings = new ArrayList<>();
		htmlComments = new ArrayList<>();
		
	}
	
	void printElements()
	{
		System.out.println();
		System.out.println("MarkdownElement info : ");
		this.printElementUnit(headingStrings, "Heading");
		this.printElementUnit(boldStrings, "Bold");
		this.printElementUnit(italicStrings, "Italic");
		this.printElementUnit(linkStrings, "Link");
		this.printElementUnit(quoteStrings, "Quote");
		this.printElementUnit(codeStrings, "Code");
		this.printElementUnit(codeBlockStrings, "CodeBlock");
		this.printElementUnit(textStrings, "Text");
		this.printElementUnit(mentionStrings, "Mention");
		this.printElementUnit(issueLinkStrings, "IssueLink");
		this.printElementUnit(pullLinkStrings, "PullLink");
		this.printElementUnit(htmlComments, "HtmlComment");
	}
	
	void printElementUnit(ArrayList<String> strings, String type)
	{
		if(!strings.isEmpty())
		{
		for(int i=0;i<strings.size();i++)
		{
			String stri = strings.get(i);
			System.out.println(type+" : "+stri);
		}
		}
	}

	public ArrayList<String> getHeadingStrings() {
		return headingStrings;
	}

	public void setHeadingStrings(ArrayList<String> headingStrings) {
		this.headingStrings = headingStrings;
	}

	public ArrayList<String> getBoldStrings() {
		return boldStrings;
	}

	public void setBoldStrings(ArrayList<String> boldStrings) {
		this.boldStrings = boldStrings;
	}

	public ArrayList<String> getItalicStrings() {
		return italicStrings;
	}

	public void setItalicStrings(ArrayList<String> italicStrings) {
		this.italicStrings = italicStrings;
	}

	public ArrayList<String> getQuoteStrings() {
		return quoteStrings;
	}

	public void setQuoteStrings(ArrayList<String> quoteStrings) {
		this.quoteStrings = quoteStrings;
	}

	public ArrayList<String> getLinkStrings() {
		return linkStrings;
	}

	public void setLinkStrings(ArrayList<String> linkStrings) {
		this.linkStrings = linkStrings;
	}

	public ArrayList<String> getCodeStrings() {
		return codeStrings;
	}

	public void setCodeStrings(ArrayList<String> codeStrings) {
		this.codeStrings = codeStrings;
	}

	public ArrayList<String> getCodeBlockStrings() {
		return codeBlockStrings;
	}

	public void setCodeBlockStrings(ArrayList<String> codeBlockStrings) {
		this.codeBlockStrings = codeBlockStrings;
	}

	public ArrayList<String> getTextStrings() {
		return textStrings;
	}

	public void setTextStrings(ArrayList<String> textStrings) {
		this.textStrings = textStrings;
	}

	public ArrayList<String> getMentionStrings() {
		return mentionStrings;
	}

	public void setMentionStrings(ArrayList<String> mentionStrings) {
		this.mentionStrings = mentionStrings;
	}

	public ArrayList<String> getTextLinkStrings() {
		return textLinkStrings;
	}

	public void setTextLinkStrings(ArrayList<String> textLinkStrings) {
		this.textLinkStrings = textLinkStrings;
	}

	public ArrayList<String> getPullLinkStrings() {
		return pullLinkStrings;
	}

	public void setPullLinkStrings(ArrayList<String> pullLinkStrings) {
		this.pullLinkStrings = pullLinkStrings;
	}

	public ArrayList<String> getIssueLinkStrings() {
		return issueLinkStrings;
	}

	public void setIssueLinkStrings(ArrayList<String> issueLinkStrings) {
		this.issueLinkStrings = issueLinkStrings;
	}

	public ArrayList<String> getHtmlComments() {
		return htmlComments;
	}

	public void setHtmlComments(ArrayList<String> htmlComments) {
		this.htmlComments = htmlComments;
	}
	
	
	
}
