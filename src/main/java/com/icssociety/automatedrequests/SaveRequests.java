package com.icssociety.automatedrequests;

import java.util.*;

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarHeader;
import de.sstoehr.harreader.model.HarRequest;
import de.sstoehr.harreader.model.HarResponse;

public class SaveRequests {
    public static void save(Har har) {
    	List<HarEntry> entries = har.getLog().getEntries();
    	for(int i = 0; i < entries.size(); i++) { // loops through all the entries of the .har file
			HarEntry entry = entries.get(i);
			
			HarRequest request = entry.getRequest();
			HarResponse response = entry.getResponse();
			
			String method = request.getMethod().toString(); // request method
			String url = request.getUrl().toString(); // request url
			String req_body = ""; // request body if it exists
			
			if(request.getPostData().getText() != null) { // checks if there is a request body
				req_body = request.getPostData().getText().toString(); 
			}
			
			int res_status = response.getStatus(); // response status
			
			String res_type = "";
			String res_body = "";
			
			if(response.getContent().getMimeType() != null) { // checks if there is a response type
				res_type = response.getContent().getMimeType().toString(); 
			}
			
			if(response.getContent().getText() != null) { //checks if there is a response body
				res_body = response.getContent().getText().toString();		
			}
			String first_recorded = "enter_name"; // who first recorded this data
			
			Request newRequest = new Request(); // creates an instance of the Request model
			
			// populates the request object with all the necessary info
			newRequest.set("method", method);
			newRequest.set("url", url);
			newRequest.set("request_body", req_body);
			newRequest.set("response_status", res_status);
			newRequest.set("response_type", res_type);
			newRequest.set("response_body", res_body);
			newRequest.set("first_recorded", first_recorded);
			
			newRequest.saveIt(); // saves it to the database
		}
    }
}