package com.icssociety.automatedrequests;

import java.util.HashMap;
import java.util.Map;

public class GenerationStrategyDeleteBody extends GenerationStrategy {
    @Override
    public Map<String, String> modifyBody(Request request) {
        Map<String, String> modifiedBody = new HashMap<>();
        modifiedBody.put("", "deleted JSON Body from request id " + request.getId().toString());
        return modifiedBody;
    }
    
    public GenerationStrategyDeleteBody() {
		super();
	}
}