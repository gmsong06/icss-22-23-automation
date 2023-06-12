package com.icssociety.automatedrequests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.http.HttpHeaders;

public class GenerationStrategyDeleteHeader extends GenerationStrategy {

	@Override
	public Map<HttpHeaders, String> modifyHeaders(Request request) {
		List<RequestHeader> headers = RequestHeader.find("request_id = ?", request.getId());
		
		Map<HttpHeaders, String> list_headers = new HashMap<>();
		
		for(int i = 0; i < headers.size(); i++) {
			List<RequestHeader> temp = RequestHeader.find("request_id = ?", request.getId());
			
			temp.remove(i);
			
			HttpHeaders new_headers = new HttpHeaders();
			
			for(RequestHeader h : temp) {
				String name = (String) h.getName();
				if(name.equalsIgnoreCase("content-length")) {
					new_headers.setContentLength(Long.parseLong((String) h.getValue()));
				} else {
					new_headers.set(name, (Object) h.getValue());
				}
			}
			
			list_headers.put(new_headers, "removed header number " + i + " from request id " + request.getId().toString());
			
		}
		
		return list_headers; 
	}
	
	public GenerationStrategyDeleteHeader() {
		super();
	}

}
