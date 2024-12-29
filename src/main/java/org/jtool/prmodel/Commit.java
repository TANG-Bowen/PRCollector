package org.jtool.prmodel;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

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
            PRModelDate date, String message, String type) {
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
    
    public String getSha() {
        return sha;
    }
    
    public String getShortSha() {
        return shortSha;
    }
    
    public PRModelDate getDate() {
        return date;
    }
    
    public String getType() {
        return type;
    }
    
    public String getMessage() {
        return message;
    }
    
    public PullRequest getPullRequest() {
        return pullRequest;
    }
    
    public Participant getCommitter() {
        return committer;
    }
    
    public CodeChange getCodeChange() {
        return codeChange;
    }
    
    public List<CIStatus> getCIStatus() {
        return ciStatus;
    }
}
