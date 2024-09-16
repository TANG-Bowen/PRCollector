package org.jtool.prmodel.builder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kohsuke.github.GHUser;
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
import com.vladsch.flexmark.util.sequence.BasedSequence;

import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.MarkdownDoc;
import org.jtool.prmodel.Comment;
import org.jtool.prmodel.ReviewComment;
import org.jtool.prmodel.Review;

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
        Parser dparser = Parser.builder(doptions).build();
        
        Document ddocument = dparser.parse(pullRequest.getDescription().getBody());
        traverse(doc, ddocument);
        
        buildMentionUsersInEachDoc(doc);
        
        buildIssueLinksAndPullLinks(pullRequest.getDescription().getBody(), doc);
        pullRequest.getDescription().setMardownDoc(doc);
        
        for (Comment comment : pullRequest.getConversation().getComments()) {
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
        
        for (Review review : pullRequest.getConversation().getReviews()){
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
        for (Node child = node.getFirstChild();child!=null;child = child.getNext()) {
            if (child instanceof Heading) {
                Node headingContent = child.getFirstChild();
                if (headingContent != null) {
                    doc.getHeadingStrings().add(headingContent.getChars().toString());
                }
            } else if (child instanceof Emphasis) {
                Node emphasisContent = child.getFirstChild();
                if (emphasisContent != null) {
                    doc.getItalicStrings().add(emphasisContent.getChars().toString());
                }
            } else if (child instanceof StrongEmphasis) {
                Node strongEmphasisContent = child.getFirstChild();
                if (strongEmphasisContent != null) {
                    doc.getBoldStrings().add(strongEmphasisContent.getChars().toString());
                }
            } else if (child instanceof BlockQuote) {
                Node quoteContent = child.getFirstChild();
                if (quoteContent != null) {
                    doc.getQuoteStrings().add(quoteContent.getChars().toString());
                }
            } else if(child instanceof Link) {
                Link link = (Link)child;
                BasedSequence linkText = link.getText();
                BasedSequence linkUrl = link.getUrl();
                String addString = linkText + "->" + linkUrl;
                doc.getLinkStrings().add(addString);
            } else if (child instanceof Code) {
                Node codeContent = child.getFirstChild();
                if (codeContent != null) {
                    doc.getCodeStrings().add(codeContent.getChars().toString());
                }
            } else if (child instanceof FencedCodeBlock) {
                Node codeBlockContent = child.getFirstChild();
                if (codeBlockContent != null) {
                    doc.getCodeBlockStrings().add(codeBlockContent.getChars().toString());
                }
            } else if(child instanceof Text) {
                String text = child.getChars().unescape();
                doc.getTextStrings().add(text);
                traverse(doc, child);
            } else if (child instanceof HtmlCommentBlock) {
                HtmlCommentBlock htcmt = (HtmlCommentBlock) child;
                doc.getHtmlComments().add(htcmt.getChars().toString());
                
            } else {
                traverse(doc, child);
            }
        }
    }
    
    @SuppressWarnings("unused")
    private void addTextLink(String text, MarkdownDoc doc) {
        Pattern pattern = Pattern.compile("\\bhttps?:\\/\\/[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String textLink = matcher.group();
            doc.getTextLinkStrings().add(textLink);
            String[] ss = textLink.split("/");
        }
    }
    
    @SuppressWarnings("unused")
    private void buildMentionUsersInEachDoc(MarkdownDoc doc) {
        boolean checkUser = true;
        for (String user : pullRequest.getHtmlDescription().getMentionUsers()) {
            for (String text : doc.getTextStrings()) {
                if (text.contains(user)) {
                    String[] email = user.split("@");
                    String name = email[email.length - 1];
                    try {
                        GHUser ghUser = github.getUser(name);
                    } catch (IOException e) {
                        checkUser = false;
                    }
                    if (checkUser) {
                        doc.getMentionStrings().add(name);
                    }
                }
            }
        }
    }
    
    @SuppressWarnings("unused")
    private void buildIssueLinksAndPullLinks(String text, MarkdownDoc doc) {
        if (text != null) {
            Pattern issuePattern = Pattern.compile("#\\d+");
            Matcher issueMatch = issuePattern.matcher(text);
            while (issueMatch.find()) {
                doc.getIssueLinkStrings().add(issueMatch.group());
            }
            Pattern linkPattern = Pattern.compile("gh-\\d+");
            Matcher linkMatch = linkPattern.matcher(text);
            while (linkMatch.find()) {
                doc.getPullLinkStrings().add(linkMatch.group());
            }
        }
    }
}
