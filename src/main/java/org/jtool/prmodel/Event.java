package org.jtool.prmodel;

public class Event extends PRAction {
    
    /* -------- Attributes -------- */
    
    // PRModelDate date;
    // String body;
    
    /* -------- Attributes -------- */
    
    public Event(PullRequest pullRequest, PRModelDate date, String body) {
        super(pullRequest, date, body);
    }
    
    public void print() {
        super.print("Event ");
    }
}
