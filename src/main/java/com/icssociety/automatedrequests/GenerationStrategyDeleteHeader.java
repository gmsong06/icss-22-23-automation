package com.icssociety.automatedrequests;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.http.HttpHeaders;

public class GenerationStrategyDeleteHeader extends GenerationStrategy {

	@Override
	public List<HttpHeaders> runStrategy(Request request) {
		List<RequestHeader> headers = RequestHeader.find("request_id = ?", request.getId());
		
		List<HttpHeaders> list_headers = new ArrayList<>();
		
		for(int i = 0; i < headers.size(); i++) {
			List<RequestHeader> temp = RequestHeader.find("request_id = ?", request.getId());
			
			temp.remove(i);
			
			HttpHeaders new_headers = new HttpHeaders();
			
			for(RequestHeader h : temp) {
				new_headers.set((String) h.getName(), (Object) h.getValue());
			}
			
			list_headers.add(new_headers);
			
		}
		
		return list_headers; 
	}
	
	public GenerationStrategyDeleteHeader() {
		super();
	}

}
