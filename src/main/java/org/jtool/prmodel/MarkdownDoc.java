package org.jtool.prmodel;

import java.util.ArrayList;
import java.util.List;

public class MarkdownDoc extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final List<MarkdownDocContent> headingStrings = new ArrayList<>();   //# 
    private final List<MarkdownDocContent> boldStrings = new ArrayList<>();      //**
    private final List<MarkdownDocContent> italicStrings = new ArrayList<>();    //_
    private final List<MarkdownDocContent> quoteStrings = new ArrayList<>();     //>
    private final List<MarkdownDocContent> linkStrings = new ArrayList<>();      //[]()
    private final List<MarkdownDocContent> codeStrings = new ArrayList<>();      //`
    private final List<MarkdownDocContent> codeBlockStrings = new ArrayList<>(); //```
    private final List<MarkdownDocContent> mentionStrings = new ArrayList<>();   //@
    private final List<MarkdownDocContent> textStrings = new ArrayList<>();
    
    private final List<MarkdownDocContent> pullLinkStrings = new ArrayList<>();  //gh-num
    private final List<MarkdownDocContent> issueLinkStrings = new ArrayList<>(); //#num
    private final List<MarkdownDocContent> textLinkStrings = new ArrayList<>();
    
    private final List<MarkdownDocContent> htmlComments = new ArrayList<>();     // <!-- ...-->
    
    /* -------- Attributes -------- */
    
    public MarkdownDoc(PullRequest pullRequest) {
        super(pullRequest);
    }
    
    public void print() {
        String prefix = "MarkdownDoc ";
        System.out.println();
        System.out.println(prefix + super.toString());
        print(prefix, headingStrings, "Heading");
        print(prefix, boldStrings, "Bold");
        print(prefix, italicStrings, "Italic");
        print(prefix, linkStrings, "Link");
        print(prefix, quoteStrings, "Quote");
        print(prefix, codeStrings, "Code");
        print(prefix, codeBlockStrings, "CodeBlock");
        print(prefix, textStrings, "Text");
        print(prefix, mentionStrings, "Mention");
        print(prefix, issueLinkStrings, "IssueLink");
        print(prefix, pullLinkStrings, "PullLink");
        print(prefix, htmlComments, "HtmlComment");
    }
    
    private void print(String prefix, List<MarkdownDocContent> texts, String type) {
        for (MarkdownDocContent text : texts) {
            System.out.println("  " + type + " : " + text.toString());
        }
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public List<MarkdownDocContent> getHeadingStrings() {
        return headingStrings;
    }
    
    public List<MarkdownDocContent> getBoldStrings() {
        return boldStrings;
    }
    
    public List<MarkdownDocContent> getItalicStrings() {
        return italicStrings;
    }
    
    public List<MarkdownDocContent> getQuoteStrings() {
        return quoteStrings;
    }
    
    public List<MarkdownDocContent> getLinkStrings() {
        return linkStrings;
    }
    
    public List<MarkdownDocContent> getCodeStrings() {
        return codeStrings;
    }
    
    public List<MarkdownDocContent> getCodeBlockStrings() {
        return codeBlockStrings;
    }
    
    public List<MarkdownDocContent> getTextStrings() {
        return textStrings;
    }
    
    public List<MarkdownDocContent> getMentionStrings() {
        return mentionStrings;
    }
    
    public List<MarkdownDocContent> getTextLinkStrings() {
        return textLinkStrings;
    }
    
    public List<MarkdownDocContent> getPullLinkStrings() {
        return pullLinkStrings;
    }
    
    public List<MarkdownDocContent> getIssueLinkStrings() {
        return issueLinkStrings;
    }
    
    public List<MarkdownDocContent> getHtmlComments() {
        return htmlComments;
    }
}
