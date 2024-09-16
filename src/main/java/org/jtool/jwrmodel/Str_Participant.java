package org.jtool.jwrmodel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Str_Participant {
    
    String prmodelId;
    
    long userId;
    String login;
    String name;
    String location;
    String role;
    List<String> followers;
    List<String> follows;
    Set<String> actionRecord = new HashSet<>();
}
