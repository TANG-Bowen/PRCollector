/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.prmodel;

public class Label extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String name;
    private final String color;
    private final String description;
    
    /* -------- Attributes -------- */
    
    private IssueEvent issueEvent;
    
    public Label(PullRequest pullRequest, String name, String color, String description) {
        super(pullRequest);
        this.name = name;
        this.color = color;
        this.description = description;
    }
    
    public void setIssueEvent(IssueEvent issueEvent) {
        this.issueEvent = issueEvent;
    }
    
    public void print() {
        String prefix = "Label ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "name : " + name);
        System.out.println(prefix + "color : " + color);
        System.out.println(prefix + "description : " + description);
        issueEvent.print();
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    /**
     * Returns label name.
     * @return name String
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns label color code.
     * @return color code String
     */
    public String getColor() {
        return color;
    }
    
    /**
     * Returns label description body.
     * @return description String
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Returns add color issue event.
     * @return the IssueEvent
     */
    public IssueEvent getIssueEvent() {
        return issueEvent;
    }
}
