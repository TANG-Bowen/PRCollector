/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

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
    
    /**
     * Returns heading strings in this Markdown document.
     * @return a List of MarkdownDocContents for heading Strings
     */
    public List<MarkdownDocContent> getHeadingStrings() {
        return headingStrings;
    }
    
    /**
     * Returns bold strings in this Markdown document.
     * @return a List of MarkdownDocContents for bold Strings
     */
    public List<MarkdownDocContent> getBoldStrings() {
        return boldStrings;
    }
    
    /**
     * Returns italic strings in this Markdown document.
     * @return a List of MarkdownDocContents for italic Strings
     */
    public List<MarkdownDocContent> getItalicStrings() {
        return italicStrings;
    }
    
    /**
     * Returns quote strings in this Markdown document.
     * @return a List of MarkdownDocContents for quote Strings
     */
    public List<MarkdownDocContent> getQuoteStrings() {
        return quoteStrings;
    }
    
    /**
     * Returns link strings in this Markdown document.
     * @return a List of MarkdownDocContents for link Strings
     */
    public List<MarkdownDocContent> getLinkStrings() {
        return linkStrings;
    }
    
    /**
     * Returns code strings in this Markdown document.
     * @return a List of MarkdownDocContents for code Strings
     */
    public List<MarkdownDocContent> getCodeStrings() {
        return codeStrings;
    }
    
    /**
     * Returns code block strings in this Markdown document.
     * @return a List of MarkdownDocContents for code block Strings
     */
    public List<MarkdownDocContent> getCodeBlockStrings() {
        return codeBlockStrings;
    }
    
    /**
     * Returns text strings in this Markdown document.
     * @return a List of MarkdownDocContents for text Strings
     */
    public List<MarkdownDocContent> getTextStrings() {
        return textStrings;
    }
    
    /**
     * Returns mention strings in this Markdown document.
     * @return a List of MarkdownDocContents for mention Strings
     */
    public List<MarkdownDocContent> getMentionStrings() {
        return mentionStrings;
    }
    
    /**
     * Returns text link strings in this Markdown document.
     * @return a List of MarkdownDocContents for text link Strings
     */
    public List<MarkdownDocContent> getTextLinkStrings() {
        return textLinkStrings;
    }
    
    /**
     * Returns pull link strings in this Markdown document.
     * @return a List of MarkdownDocContents for pull link Strings
     */
    public List<MarkdownDocContent> getPullLinkStrings() {
        return pullLinkStrings;
    }
    
    /**
     * Returns issue link strings in this Markdown document.
     * @return a List of MarkdownDocContents for issue link Strings
     */
    public List<MarkdownDocContent> getIssueLinkStrings() {
        return issueLinkStrings;
    }
    
    /**
     * Returns html comment strings in this Markdown document.
     * @return a List of MarkdownDocContents for html comment Strings
     */
    public List<MarkdownDocContent> getHtmlComments() {
        return htmlComments;
    }
}
