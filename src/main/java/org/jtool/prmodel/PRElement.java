package org.jtool.prmodel;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

public class PRElement {
    
    public static final String DELETE = "delete";
    public static final String ADD = "add";
    public static final String REVISE = "revise";
    public static final String NO_CHANGE = "";
    
    public static final String BEFORE = "before";
    public static final String AFTER = "after";
    
    private static long idNum = 1;
    
    protected String prmodelId;
    
    protected final PullRequest pullRequest;
    
    protected PRElement(PullRequest pullRequest) {
        this.pullRequest = pullRequest;
        
        this.prmodelId = this.getClass().getName() + String.valueOf(idNum);
        idNum++;
    }
    
    public void setPrmodelId(String prmodelId) {
        this.prmodelId = prmodelId;
    }
    
    public static String toPRElemList(List<? extends PRElement> elems) {
        StringBuilder s = new StringBuilder();
        if (elems.isEmpty()) {
            return "[ ]";
        }
        for (PRElement e : elems) {
            s.append(e.getPRModelId() + " ");
        }
        return "[ " + s.toString() + "]";
    }
    
    public static String toPRElemList(Set<? extends PRElement> elems) {
        return toPRElemList(new ArrayList<>(elems));
    }
    
    public static String toStringList(List<String> elems) {
        StringBuilder s = new StringBuilder();
        if (elems.isEmpty()) {
            return "[ ]";
        }
        for (String e : elems) {
            s.append(e + " ");
        }
        return "[ " + s.toString() + "]";
    }
    
    public static String toStringList(Set<String> elems) {
        return toStringList(new ArrayList<>(elems));
    }
    
    public static String toSortedPRElemList(Set<? extends PRElement> elems) {
        List<PRElement> list = new ArrayList<>(elems);
        List<PRElement> sortedList = list.stream()
                                         .sorted((e1, e2) -> e1.prmodelId.compareTo(e2.prmodelId))
                                         .collect(Collectors.toList());
        return toPRElemList(sortedList);
    }
    
    public static String toSortedStringList(Set<String> elems) {
        List<String> list = new ArrayList<>(elems);
        List<String> sortedList = list.stream()
                                      .sorted((e1, e2) -> e1.compareTo(e2))
                                      .collect(Collectors.toList());
        return toStringList(sortedList);
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PRElement) ? equals((PRElement)obj) : false;
    }
    
    public boolean equals(PRElement element) {
        return element != null && (this == element || this.prmodelId.equals(element.prmodelId));
    }
    
    @Override
    public int hashCode() {
        return prmodelId.hashCode();
    }
    
    @Override
    public String toString() {
        return "prmodelId : " + prmodelId;
    }
    
    /* ------------------------------------
     * API
     --------------------------------------*/
    
    public PullRequest getPullRequest() {
        return pullRequest;
    }
    
    public String getPRModelId() {
        return prmodelId;
    }
}
