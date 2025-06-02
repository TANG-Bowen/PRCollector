/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.prmodel;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Commit extends PRElement {
    
    /* -------- Attributes -------- */
    
    private final String sha;
    private final String shortSha;
    private final PRModelDate date;
    private final String type;
    private final String message;
    
    /* -------- Attributes -------- */
    
    private Participant committer;
    private CodeChange codeChange;
    private List<CIStatus> ciStatus = new ArrayList<>();
    
    public Commit(PullRequest pullRequest, String sha, String shortSha,
            PRModelDate date, String type, String message) {
        super(pullRequest);
        this.sha = sha;
        this.shortSha = shortSha;
        this.date = date;
        this.type = type;
        this.message = message;
    }
    
    public void setCommiter(Participant committer) {
        this.committer = committer;
    }
    
    public void setCodeChange(CodeChange codeChange) {
        this.codeChange = codeChange;
    }
    
    void sortCIStatus1() {
        List<CIStatus> sortedStatusList = ciStatus.stream()
            .sorted((s1, s2) -> s1.getUpdateDate().compareTo(s2.getUpdateDate()))
            .collect(Collectors.toList());
        ciStatus = sortedStatusList;
    }
    
    public void print() {
        String prefix = "Commit ";
        System.out.println();
        System.out.println(prefix + super.toString());
        System.out.println(prefix + "sha : " + sha);
        System.out.println(prefix + "shortSha : " + shortSha);
        System.out.println(prefix + "committer : "+ committer.getLogin());
        System.out.println(prefix + "date : "+ date.toString());
        System.out.println(prefix + "type : " + type);
        System.out.println(prefix + "message : " + message);
        System.out.println(prefix + "ciStatus : " + toPRElemList(ciStatus));
        if(this.codeChange!=null) {
        codeChange.print();
        }
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    /**
     * Returns full sha of this commit.
     * @return sha String
     */
    public String getSha() {
        return sha;
    }
    
    /**
     * Returns short sha of this commit.
     * @return short sha String
     */
    public String getShortSha() {
        return shortSha;
    }
    
    /**
     * Returns upload date of this commit.
     * @return a PRModelDate
     */
    public PRModelDate getDate() {
        return date;
    }
    
    /**
     * Returns type of this commit.
     * @return type String
     */
    public String getType() {
        return type;
    }
    
    /**
     * Returns message of this commit.
     * @return message String
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Returns the pull request contains this commit.
     * @return a PullRequest
     */
    public PullRequest getPullRequest() {
        return pullRequest;
    }
    
    /**
     * Returns the user who upload this commit.
     * @return a Participant
     */
    public Participant getCommitter() {
        return committer;
    }
    
    /**
     * Returns CodeChange in this commit.
     * @return a CodeChange
     */
    public CodeChange getCodeChange() {
        return codeChange;
    }
    
    /**
     * Returns CI results this commit.
     * @return a List of CIStatuses
     */
    public List<CIStatus> getCIStatus() {
        return ciStatus;
    }
}
