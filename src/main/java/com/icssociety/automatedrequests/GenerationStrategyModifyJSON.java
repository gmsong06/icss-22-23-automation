package com.icssociety.automatedrequests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.http.HttpHeaders;
import org.json.*;

public class GenerationStrategyModifyJSON extends GenerationStrategy {
    @Override
    public Map<String, String> modifyBody(Request request) {
        if(request.getMethod().toString().equals("POST")) {
           
        } else {
            return super.modifyBody(request);
        }
        return super.modifyBody(request);
    }

}
