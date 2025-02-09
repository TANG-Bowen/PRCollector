/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.prmodel;

public class Action extends PRElement {
    
    /* -------- Attributes -------- */
    
    protected final PRModelDate date;
    protected final String body;
    
    /* -------- Attributes -------- */
    
    protected Conversation conversation;
    protected Participant participant;
    
    protected Action(PullRequest pullRequest, PRModelDate date, String body) {
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
    /**
     * Returns the create date of a action.
     * @return the date instance of PRModelDate
     */ 
    public PRModelDate getDate() {
        return date;
    }
    
    /**
     * Returns the body of a action edited by the actor.
     * @return the body
     */
    public String getBody() {
        return body;
    }
    
    /**
     * Returns the conversation covers this action.
     * @return the conversation
     */ 
    public Conversation getConversation() {
        return conversation;
    }
    
    /**
     * Returns the actor of this action.
     * @return the actor
     */
    public Participant getParticipant() {
        return participant;
    }
    
    /**
     * Returns the type of this action.
     * @return the type
     */
    public String getActionType() {
        return this.getClass().getName();
    }
}
