package com.icssociety.automatedrequests;

import java.util.HashMap;
import java.util.Map;

import org.json.*;

public class GenerationStrategyAppendJSON extends GenerationStrategy {
    @Override
    public Map<String, String> modifyBody(Request request) {
        Map<String, String> modified_bodies = new HashMap<>();
        if(request.getMethod().toString().equals("POST")) {
            try {
            JSONObject body = new JSONObject(request.getRequestBody().toString());
            JSONObject appended_body = body.put("apple", "banana");
            modified_bodies.put(appended_body.toString(), "appended pair apple: banana to JSON body" + " from request id " + request.getId().toString());
            return modified_bodies;
            
        } catch(JSONException e) {
        }

        }
        
        return modified_bodies;
    }

}
