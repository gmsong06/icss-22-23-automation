package com.icssociety.automatedrequests;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.util.concurrent.ExecutionError;

public abstract class GenerationStrategy {
	
	public abstract List<HttpHeaders> runStrategy(Request request);
	
	public void sendModifiedRequest(Request request) {
		//setting up + building HTTP Requests
		HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		
		try { 
       		HttpRequest sent_request = HTTP_TRANSPORT.createRequestFactory()
       				.buildRequest(
       						(String) request.getMethod(), 
       						new GenericUrl((String) request.getUrl()), 
       						null
       				);
	
       		
       		//Gets a list of modified headers which were passed by the generation strategy used.
       		List<HttpHeaders> new_headers = runStrategy(request);

       		//loop through each modified header
       		for(int i = 0; i < new_headers.size(); i++) {
       			
       			//create a new HTTP request and get a HTTP response using the modified headers
       			Request new_request = new Request(); 
       			HttpResponse response = sent_request.setHeaders(new_headers.get(i)).executeAsync().get(); 
       			
       			//update the status code of the modified HTTP request
    			new_request.setResponseStatus(response.getStatusCode());
    			
    			//Parse a string response from the modified HTTP request
    			String res = response.parseAsString();
    			
    			//Set the generated status of the request to 1 (generated)
    			new_request.setIsGenerated(1);
    			
    			//if the string response body is different from the response as string, and the response code is not a 400, 
    			//		set generation code to 3 (success) or 2 (failure)
    			if(!res.equals(request.getResponseBody().toString())) {
    				if((int) response.getStatusCode() < 400) {
    					new_request.setIsGenerated(3);
    				} else {
    					new_request.setIsGenerated(2);
    				}
    			}
    				
    			//Update the new request to include all the same parameters of the old request, modifying some ("timmy")
    			new_request.setUrl(request.getUrl().toString());
    			new_request.setFirstRecorded("timmy");
    			new_request.setRequestBody(request.getRequestBody().toString());
    			
    			new_request.setResponseType(response.getContentType());
    			new_request.setResponseBody(res);
    			
    			new_request.setMethod(request.getMethod().toString());
    			new_request.setModification("modified header number " + i + " from request id " + request.getId().toString());
    			
    			response.disconnect();
    			
    			//saving the new request in the data base
    			new_request.save(); // decide if we want this
       		}
			
			
		} catch(Exception e) {
			System.out.println(e.toString());
		}
	
	}
}
