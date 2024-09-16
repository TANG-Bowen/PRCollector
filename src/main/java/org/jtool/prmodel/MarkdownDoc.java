package org.jtool.prmodel;

import java.util.ArrayList;
import java.util.List;

public class MarkdownDoc extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final List<String> headingStrings = new ArrayList<>();   //# 
    private final List<String> boldStrings = new ArrayList<>();      //**
    private final List<String> italicStrings = new ArrayList<>();    //_
    private final List<String> quoteStrings = new ArrayList<>();     //>
    private final List<String> linkStrings = new ArrayList<>();      //[]()
    private final List<String> codeStrings = new ArrayList<>();      //`
    private final List<String> codeBlockStrings = new ArrayList<>(); //```
    private final List<String> mentionStrings = new ArrayList<>();   //@
    private final List<String> textStrings = new ArrayList<>();
    
    private final List<String> pullLinkStrings = new ArrayList<>();  //gh-num
    private final List<String> issueLinkStrings = new ArrayList<>(); //#num
    private final List<String> textLinkStrings = new ArrayList<>();
    
    private final List<String> htmlComments = new ArrayList<>();     // <!-- ...-->
    
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
    
    private void print(String prefix, List<String> strings, String type) {
        for (String s : strings) {
            System.out.println("  " + type + " : " + s);
        }
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public List<String> getHeadingStrings() {
        return headingStrings;
    }
    
    public List<String> getBoldStrings() {
        return boldStrings;
    }
    
    public List<String> getItalicStrings() {
        return italicStrings;
    }
    
    public List<String> getQuoteStrings() {
        return quoteStrings;
    }
    
    public List<String> getLinkStrings() {
        return linkStrings;
    }
    
    public List<String> getCodeStrings() {
        return codeStrings;
    }
    
    public List<String> getCodeBlockStrings() {
        return codeBlockStrings;
    }
    
    public List<String> getTextStrings() {
        return textStrings;
    }
    
    public List<String> getMentionStrings() {
        return mentionStrings;
    }
    
    public List<String> getTextLinkStrings() {
        return textLinkStrings;
    }
    
    public List<String> getPullLinkStrings() {
        return pullLinkStrings;
    }
    
    public List<String> getIssueLinkStrings() {
        return issueLinkStrings;
    }
    
    public List<String> getHtmlComments() {
        return htmlComments;
    }
}
