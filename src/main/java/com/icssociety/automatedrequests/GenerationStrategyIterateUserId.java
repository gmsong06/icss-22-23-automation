package com.icssociety.automatedrequests;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.http.HttpHeaders;

public class GenerationStrategyIterateUserId extends GenerationStrategy {

	
	//generation strategy which removes each header, returning a list of headers, each entry having one header removed from the original one
	@Override
	public List<HttpHeaders> modifyHeaders(Request request) {
		//get a list of all the headers
		List<RequestHeader> headers = RequestHeader.find("request_id = ?", request.getId());
		//make a list of http headers, which modified headers will be added to
		List<HttpHeaders> list_headers = new ArrayList<>();
		
		//loop through all the headers
		for(RequestHeader tempHeader : headers) { //loops through each header in request
			System.out.println(tempHeader.getName().toString() + " " + tempHeader.getId().toString());
			if(tempHeader.getName().toString().equalsIgnoreCase("id") || tempHeader.getName().toString().equalsIgnoreCase("userid")) { //checks if header name is id or userid
				
				char[] idHeader = tempHeader.getValue().toString().toCharArray(); //converts the header to a char array of the values in the id
				int idLen = idHeader.length;
				
				for(int j = -2; j < 2; j++) { //changes the last value of the id using acsii, goes down and up 10
					if(j == 0) continue;
					HttpHeaders new_headers = new HttpHeaders();
					idHeader[idLen-1] = (char)(idHeader[idLen-1] + j);
					String s = new String(idHeader);
					
					for(RequestHeader h : headers) {
						new_headers.set((String) h.getName(), (Object) h.getValue());
					}
					
					new_headers.set((String) tempHeader.getName(), (Object)s);
					list_headers.add(new_headers);
				}
			}	
		
		}
		return list_headers;	

	}
	
	public GenerationStrategyIterateUserId() {
		super();
	}

}
