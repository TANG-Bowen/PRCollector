package org.jtool.prmodel.builder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kohsuke.github.GitHub;

import com.vladsch.flexmark.ast.BlockQuote;
import com.vladsch.flexmark.ast.Code;
import com.vladsch.flexmark.ast.Emphasis;
import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.HtmlCommentBlock;
import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.ast.StrongEmphasis;
import com.vladsch.flexmark.ast.Text;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.MarkdownDoc;
import org.jtool.prmodel.MarkdownDocContent;
import org.jtool.prmodel.IssueComment;
import org.jtool.prmodel.ReviewComment;
import org.jtool.prmodel.ReviewEvent;

public class MarkdownDocBuilder {
    
    private final PullRequest pullRequest;
    private final GitHub github;
    
    MarkdownDocBuilder(PullRequest pullRequest, GitHub github) {
        this.pullRequest = pullRequest;
        this.github = github;
    }
    
    public void build() {
        MarkdownDoc doc = new MarkdownDoc(pullRequest);
        MutableDataSet doptions = new MutableDataSet();
        Parser pparser = Parser.builder(doptions).build();
        
        Document pdocument = pparser.parse(pullRequest.getDescription().getBody());
        traverse(doc, pdocument);
        
        buildMentionUsersInEachDoc(doc);
        
        buildIssueLinksAndPullLinks(pullRequest.getDescription().getBody(), doc);
        pullRequest.getDescription().setMardownDoc(doc);
        
        for (IssueComment comment : pullRequest.getConversation().getIssueComments()) {
            MarkdownDoc doc2 = new MarkdownDoc(pullRequest);
            MutableDataSet options = new MutableDataSet();
            Parser parser = Parser.builder(options).build();
            
            Document document = parser.parse(comment.getBody());
            traverse(doc2, document);
            
            buildMentionUsersInEachDoc(doc2);
            buildIssueLinksAndPullLinks(comment.getBody(), doc2);
            comment.setMarkdownDoc(doc2);
        }
        
        for (ReviewComment comment : pullRequest.getConversation().getReviewComments()) {
            MarkdownDoc doc2 = new MarkdownDoc(pullRequest);
            MutableDataSet options = new MutableDataSet();
            Parser parser = Parser.builder(options).build();
            
            Document document = parser.parse(comment.getBody());
            traverse(doc2, document);
            
            buildMentionUsersInEachDoc(doc2);
            buildIssueLinksAndPullLinks(comment.getBody(), doc2);
            comment.setMarkdownDoc(doc2);
        }
        
        for (ReviewEvent review : pullRequest.getConversation().getReviewEvents()){
            MarkdownDoc doc2 = new MarkdownDoc(pullRequest);
            MutableDataSet options = new MutableDataSet();
            Parser parser = Parser.builder(options).build();
            
            Document document = parser.parse(review.getBody());
            traverse(doc2, document);
            
            buildMentionUsersInEachDoc(doc2);
            buildIssueLinksAndPullLinks(review.getBody(), doc2);
            review.setMarkdownDoc(doc2);
        }
    }
    
    private void traverse(MarkdownDoc doc, Node node) {
        for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
            if (child instanceof Heading) {
                Node headingNode = child.getFirstChild();
                if (headingNode != null) {
                    doc.getHeadingStrings().add(createMarkdownDocContent(headingNode));
                }
            } else if (child instanceof Emphasis) {
                Node emphasisNode = child.getFirstChild();
                if (emphasisNode != null) {
                    doc.getItalicStrings().add(createMarkdownDocContent(emphasisNode));
                }
            } else if (child instanceof StrongEmphasis) {
                Node strongEmphasisNode = child.getFirstChild();
                if (strongEmphasisNode != null) {
                    doc.getBoldStrings().add(createMarkdownDocContent(strongEmphasisNode));
                }
            } else if (child instanceof BlockQuote) {
                Node quoteNode = child.getFirstChild();
                if (quoteNode != null) {
                    doc.getQuoteStrings().add(createMarkdownDocContent(quoteNode));
                }
            } else if(child instanceof Link) {
                Link link = (Link)child;
                doc.getLinkStrings().add(createMarkdownDocContent(link));
            } else if (child instanceof Code) {
                Node codeNode = child.getFirstChild();
                if (codeNode != null) {
                    doc.getCodeStrings().add(createMarkdownDocContent(codeNode));
                }
            } else if (child instanceof FencedCodeBlock) {
                Node codeBlockNode = child.getFirstChild();
                if (codeBlockNode != null) {
                    doc.getCodeBlockStrings().add(createMarkdownDocContent(codeBlockNode));
                }
            } else if (child instanceof Text) {
                Text text = (Text)child;
                doc.getTextStrings().add(createMarkdownDocContent(text));
                traverse(doc, child);
            } else if (child instanceof HtmlCommentBlock) {
                HtmlCommentBlock htmlCommentBlock = (HtmlCommentBlock)child;
                doc.getHtmlComments().add(createMarkdownDocContent(htmlCommentBlock));
            } else {
                traverse(doc, child);
            }
        }
    }
    
    private MarkdownDocContent createMarkdownDocContent(Node node) {
        return new MarkdownDocContent(node.getChars().toString(),
<<<<<<< HEAD
                node.getChars().getStartOffset(), node.getChars().getEndOffset(),
                node.getStartOffset(), node.getEndOffset());
=======
                node.getChars().getStartOffset(), node.getChars().getStartOffset(),
                node.getStartOffset(), node.getStartOffset());
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
    }
    
    private MarkdownDocContent createMarkdownDocContent(Text text) {
        return new MarkdownDocContent(text.getChars().unescape(),
<<<<<<< HEAD
                text.getChars().getStartOffset(), text.getChars().getEndOffset(),
                text.getStartOffset(), text.getEndOffset());
=======
                text.getChars().getStartOffset(), text.getChars().getStartOffset(),
                text.getStartOffset(), text.getStartOffset());
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
    }
    
    private MarkdownDocContent createMarkdownDocContent(Link link) {
        String linkText = link.getText().toString();
        String linkUrl = link.getUrl().toString();
        String text = linkText + "->" + linkUrl;
        return new MarkdownDocContent(text,
<<<<<<< HEAD
                link.getChars().getStartOffset(), link.getChars().getEndOffset(),
                link.getStartOffset(), link.getEndOffset());
=======
                link.getChars().getStartOffset(), link.getChars().getStartOffset(),
                link.getStartOffset(), link.getStartOffset());
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
    }
    
    private void buildMentionUsersInEachDoc(MarkdownDoc doc) {
        for (String user : pullRequest.getHtmlDescription().getMentionUsers()) {
            for (MarkdownDocContent content : doc.getTextStrings()) {
                if (content.getText().contains(user)) {
                    String[] words = user.split("@");
                    String name = words[words.length - 1];
                    try {
                        github.getUser(name); // Check the name
                        
                        MarkdownDocContent nameContent = new MarkdownDocContent(name,
<<<<<<< HEAD
                                content.getStartOffset(), content.getEndOffset(),
                                content.getNodeStartOffset(), content.getNodeEndOffset());
=======
                                content.getStartOffset(), content.getStartOffset(),
                                content.getNodeStartOffset(), content.getNodeStartOffset());
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
                        doc.getMentionStrings().add(nameContent);
                    } catch (IOException e) {
                        /* empty */
                    }
                }
            }
        }
    }
    
    private void buildIssueLinksAndPullLinks(String text, MarkdownDoc doc) {
        if (text != null) {
            Pattern issuePattern = Pattern.compile("#\\d+");
            Matcher issueMatch = issuePattern.matcher(text);
            while (issueMatch.find()) {
                int start = issueMatch.start();
<<<<<<< HEAD
                int end = issueMatch.end();
=======
                int end = issueMatch.start();
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
                MarkdownDocContent issueContent = new MarkdownDocContent(issueMatch.group(),
                        start, end, start, end);
                doc.getIssueLinkStrings().add(issueContent);
            }
            
            Pattern pullPattern = Pattern.compile("gh-\\d+");
            Matcher pullMatch = pullPattern.matcher(text);
            while (pullMatch.find()) {
                int start = pullMatch.start();
<<<<<<< HEAD
                int end = pullMatch.end();
=======
                int end = pullMatch.start();
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
                MarkdownDocContent pullContent = new MarkdownDocContent(pullMatch.group(),
                        start, end, start, end);
                doc.getPullLinkStrings().add(pullContent);
            }
        }
    }
}
