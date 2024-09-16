package org.jtool.prmodel;

public class Label extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String name;
    private final String color;
    private final String description;
    
    /* -------- Attributes -------- */
    
    private Event event;
    
    public Label(PullRequest pullRequest, String name, String color, String description) {
        super(pullRequest);
        this.name = name;
        this.color = color;
        this.description = description;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }
    
    public void print() {
        String prefix = "Label ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "name : " + name);
        System.out.println(prefix + "color : " + color);
        System.out.println(prefix + "description : " + description);
        event.print();
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
    
    public Event getEvent() {
        return event;
    }
}
