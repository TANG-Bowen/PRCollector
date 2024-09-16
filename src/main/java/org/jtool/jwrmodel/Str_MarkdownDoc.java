package org.jtool.jwrmodel;

import java.util.ArrayList;
import java.util.List;

public class Str_MarkdownDoc {
    
    String prmodelId;
    
    List<String> headingStrings;
    List<String> boldStrings;
    List<String> italicStrings;
    List<String> quoteStrings;
    List<String> linkStrings;
    List<String> codeStrings;
    List<String> codeBlockStrings;
    List<String> mentionStrings;
    List<String> textStrings;
    
    List<String> pullLinkStrings;
    List<String> issueLinkStrings;
    List<String> textLinkStrings;
    
    List<String> htmlComments = new ArrayList<>();
}
