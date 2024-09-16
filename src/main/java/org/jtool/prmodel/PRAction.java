package org.jtool.prmodel;

public class PRAction extends PRElement {
    
    /* -------- Attributes -------- */
    
    protected final PRModelDate date;
    protected final String body;
    
    /* -------- Attributes -------- */
    
    protected Conversation conversation;
    protected Participant participant;
    
    protected PRAction(PullRequest pullRequest, PRModelDate date, String body) {
        super(pullRequest);
        this.date = date;
        this.body = body;
    }
    
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
    
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
    
    protected void print(String prefix) {
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "date : " + date.toString());
        System.out.println(prefix + "body : " + body);
        System.out.println(prefix + "participant : " + participant.getLogin());
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public PRModelDate getDate() {
        return date;
    }
    
    public String getBody() {
        return body;
    }
    
    public Conversation getConversation() {
        return conversation;
    }
    
    public Participant getParticipant() {
        return participant;
    }
    
    public String getActionType() {
        return this.getClass().getName();
    }
}
