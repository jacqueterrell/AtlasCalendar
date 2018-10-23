package com.team.mamba.atlascalendar.data.model.api.googlePlaces.request;

import java.util.LinkedHashMap;
import java.util.Map;

public class AutoCompleteRequest {

    private String input;
    private static final String TYPES = "establishment";
    private static final String KEY = "AIzaSyAyEa35coS21US4r-pJj-0wN68q0fEFy6Y";



    public AutoCompleteRequest(String input){

        this.input = input;
    }


    public Map<String,String> getRequestMap(){

        Map<String,String> requestMap = new LinkedHashMap<>();

        requestMap.put("input", input);
        requestMap.put("types",TYPES);
        requestMap.put("key", KEY);

        return requestMap;
    }

}
