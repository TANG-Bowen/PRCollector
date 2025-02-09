/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.prmodel;

public class CIStatus extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String context;
    private final String state;
    private final String description;
    private final String targetUrl;
    private final PRModelDate createDate;
    private final PRModelDate updateDate;
    
    /* -------- Attributes -------- */
    
    Commit commit;
    
    public CIStatus(PullRequest pullRequest, String context, String state, String description, String targetUrl,
            PRModelDate createDate, PRModelDate updateDate) {
        super(pullRequest);
        this.context = context;
        this.state = state;
        this.description = description;
        this.targetUrl = targetUrl;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
    
    public void setCommit(Commit commit) {
        this.commit = commit;
    }
    
    public void print() {
        String prefix = "CIStatus ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "context : " + context);
        System.out.println(prefix + "state : " + state);
        System.out.println(prefix + "description : " + description);
        System.out.println(prefix + "targetUrl : " + targetUrl);
        System.out.println(prefix + "createDate : " + createDate.toString());
        System.out.println(prefix + "updateDate : " + updateDate.toString());
        System.out.println(prefix + "commit : " + commit.getSha());
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    /**
     * Returns the context of a CI, usually as the name.
     * @return the context
     */
    public String getContext() {
        return context;
    }
    
    /**
     * Returns the state of a CI, usually as the result.
     * @return the state
     */
    public String getState() {
        return state;
    }
    
    /**
     * Returns the description of a CI, usually as the response message.
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Returns the url of a CI.
     * @return the url
     */
    public String getTargetUrl() {
        return targetUrl;
    }
    
    /**
     * Returns create date of a CI.
     * @return create date
     */
    public PRModelDate getCreateDate() {
        return createDate;
    }
    
    /**
     * Returns the update date of a CI.
     * @return the update
     */
    public PRModelDate getUpdateDate() {
        return updateDate;
    }
    
    /**
     * Returns the aim commit.
     * @return the commit
     */
    public Commit getCommit() {
        return commit;
    }
}
