package com.icssociety.automatedrequests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.http.HttpHeaders;

public class GenerationStrategyAddRandomString extends GenerationStrategy {

	@Override
	public Map<String, String> modifyBody(Request request) {
		Map<String, String> modifiedBody = new HashMap<>();
		String body = (String) request.getRequestBody();
		
		int additionalStringLength = 100;
		body += HelperUtils.generateRandomString(additionalStringLength); // Hard coded string length, may change in future
		
		modifiedBody.put(body, "appended random string of size " + additionalStringLength + " from request id " + request.getId().toString());
		
		return modifiedBody;
	}
	
	public GenerationStrategyAddRandomString() {
		super();
	}

}
