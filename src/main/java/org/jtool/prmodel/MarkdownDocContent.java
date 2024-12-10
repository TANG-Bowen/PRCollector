package org.jtool.prmodel;

public class MarkdownDocContent {
    
    private final String text;
    private final int startOffset;
    private final int endOffset;
    private final int nodeStartOffset;
    private final int nodeEndOffset;
    
    public MarkdownDocContent(String text,
            int startOffset, int endOffset, int nodeStartOffset, int nodeEndOffset) {
        this.text = text;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
<<<<<<< HEAD
        this.nodeStartOffset = nodeStartOffset;
        this.nodeEndOffset = nodeEndOffset;
=======
        this.nodeStartOffset = startOffset;
        this.nodeEndOffset = endOffset;
>>>>>>> ce71da35411010c508025a48f729e2039d8b6792
    }
    
    public String getText() {
        return text;
    }
    public int getStartOffset() {
        return startOffset;
    }
    
    public int getEndOffset() {
        return endOffset;
    }
    
    public int getNodeStartOffset() {
        return nodeStartOffset;
    }
    
    public int getNodeEndOffset() {
        return nodeEndOffset;
    }
    
    @Override
    public String toString() {
        return text + "[" + startOffset + "-" + endOffset + "]";
    }
}
