package com.icssociety.automatedrequests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.http.HttpHeaders;

public class GenerationStrategyAddRandomString extends GenerationStrategy {

	@Override
	public Map<String, String> modifyBody(Request request) {
		Map<String, String> modified_bodies = new HashMap<>();
		String body = (String) request.getRequestBody();
	}
	
	public GenerationStrategyAddRandomString() {
		super();
	}

}
