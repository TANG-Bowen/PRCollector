/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.jwrmodel;

import java.util.ArrayList;
import java.util.List;

import org.jtool.prmodel.MarkdownDocContent;

public class Str_MarkdownDoc {
    
    String prmodelId;
    
    List<MarkdownDocContent> headingStrings;
    List<MarkdownDocContent> boldStrings;
    List<MarkdownDocContent> italicStrings;
    List<MarkdownDocContent> quoteStrings;
    List<MarkdownDocContent> linkStrings;
    List<MarkdownDocContent> codeStrings;
    List<MarkdownDocContent> codeBlockStrings;
    List<MarkdownDocContent> mentionStrings;
    List<MarkdownDocContent> textStrings;
    
    List<MarkdownDocContent> pullLinkStrings;
    List<MarkdownDocContent> issueLinkStrings;
    List<MarkdownDocContent> textLinkStrings;
    
    List<MarkdownDocContent> htmlComments = new ArrayList<>();
}
