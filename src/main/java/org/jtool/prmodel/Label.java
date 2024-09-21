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
    
    public String getName() {
        return name;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getDescription() {
        return description;
    }
    
    public IssueEvent getIssueEvent() {
        return issueEvent;
    }
}
