package com.icssociety.automatedrequests;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.http.HttpHeaders;

public class GenerationStrategyDeleteHeader extends GenerationStrategy {

	
	//generation strategy which removes each header, returning a list of headers, each entry having one header removed from the original one
	@Override
	public List<HttpHeaders> runStrategy(Request request) {
		//get a list of all the headers
		List<RequestHeader> headers = RequestHeader.find("request_id = ?", request.getId());
		//make a list of http headers, which modified headers will be added to
		List<HttpHeaders> list_headers = new ArrayList<>();
		
		//loop through all the headers
		for(int i = 0; i < headers.size(); i++) {
			
			//create a temp copy list of the headers in the request, then remove one of those headers
			List<RequestHeader> temp = RequestHeader.find("request_id = ?", request.getId());
			temp.remove(i);
			
			//create an HttpHeaders, then set each of the header to be equal to the modified headers list (which is stored in temp)
			HttpHeaders new_headers = new HttpHeaders();
			for(RequestHeader h : temp) {
				new_headers.set((String) h.getName(), (String) h.getValue());
			}
			
			//add the new headers to the final list
			list_headers.add(new_headers);
		}
		
		return list_headers; 
	}
	
	public GenerationStrategyDeleteHeader() {
		super();
	}

}
