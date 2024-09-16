package org.jtool.prmodel;

import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

public class JsonTest {
    
    public static void main(String[] args) {
//      Gson gson = new GsonBuilder()
//              .registerTypeAdapter(Date.class, new CustomDateDeserializer())
//              .create();
        
        Gson gson = new Gson();
        String json = "{ \"createDate\": \"Jan 9, 2022, 11:44:07 PM\" }"; //copy
        //String json = "{ \"createDate\": \"Jan 9, 2022, 11:44:07 PM\" }";
        //String json = "{ \"createDate\": \"Jan 9, 2022, 11:44:07 PM\" }";
        //String json = "{ \"createDate\": \"Jan 9, 2022, 11:44:07 PM\" }"; //keyboard
        //String json = "{ \"createDate\": \"May 3, 2024, 5:40:05 PM\" }"; 
        //String json = "{ \"createDate\": \"May 3, 2024, 5:40:05 PM\" }"; 
        
        json = json.replace('\u00A0', ' ').replaceAll("\\p{Zs}", " ");
        
        MyObject obj = gson.fromJson(json, MyObject.class);
        
        System.out.println(obj);
    }
}
