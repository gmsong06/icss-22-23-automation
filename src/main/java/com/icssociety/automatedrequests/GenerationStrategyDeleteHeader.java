package com.icssociety.automatedrequests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.http.HttpHeaders;

public class GenerationStrategyDeleteHeader extends GenerationStrategy {

	
	//generation strategy which removes each header, returning a list of headers, each entry having one header removed from the original one
	@Override
	public Map<HttpHeaders, String> modifyHeaders(Request request) {
		List<RequestHeader> headers = RequestHeader.find("request_id = ?", request.getId());
		
		Map<HttpHeaders, String> list_headers = new HashMap<>();
		
		//loop through all the headers
		for(int i = 0; i < headers.size(); i++) {
			
			//create a temp copy list of the headers in the request, then remove one of those headers
			List<RequestHeader> temp = RequestHeader.find("request_id = ?", request.getId());
			temp.remove(i);
			
			//create an HttpHeaders, then set each of the header to be equal to the modified headers list (which is stored in temp)
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

	public String getStrategyDescription () {
		return "Removing a header at a time from request";
	}
	
	public GenerationStrategyDeleteHeader() {
		super();
	}

}
