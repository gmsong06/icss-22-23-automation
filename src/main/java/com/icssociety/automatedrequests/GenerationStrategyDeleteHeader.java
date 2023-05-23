package com.icssociety.automatedrequests;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.http.HttpHeaders;

public class GenerationStrategyDeleteHeader extends GenerationStrategy {

	@Override
	public List<HttpHeaders> modifyHeaders(Request request) {
		List<RequestHeader> headers = RequestHeader.find("request_id = ?", request.getId());
		
		List<HttpHeaders> list_headers = new ArrayList<>();
		
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
			
			list_headers.add(new_headers);
			
		}
		
		return list_headers; 
	}
	
	public GenerationStrategyDeleteHeader() {
		super();
	}

}
