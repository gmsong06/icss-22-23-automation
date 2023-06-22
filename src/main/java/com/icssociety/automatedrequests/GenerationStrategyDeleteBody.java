package com.icssociety.automatedrequests;

import java.util.HashMap;
import java.util.Map;

public class GenerationStrategyDeleteBody extends GenerationStrategy {
    @Override
    public Map<String, String> modifyBody(Request request) {
        Map<String, String> modified_bodies = new HashMap<>();
        modified_bodies.put("", "deleted JSON Body" + " from request id " + request.getId().toString());
        return modified_bodies;
    }

}