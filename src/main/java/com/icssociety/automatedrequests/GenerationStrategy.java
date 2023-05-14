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
		HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		
		try {
       		HttpRequest sent_request = HTTP_TRANSPORT.createRequestFactory()
       				.buildRequest(
       						(String) request.getMethod(), 
       						new GenericUrl((String) request.getUrl()), 
       						null
       				);
	
       		
       		List<HttpHeaders> new_headers = runStrategy(request);

       		for(int i = 0; i < new_headers.size(); i++) {
       			Request new_request = new Request();
       			
       			HttpResponse response = sent_request.setHeaders(new_headers.get(i)).executeAsync().get();
       			
       
    			new_request.setResponseStatus(response.getStatusCode());
    			String res = response.parseAsString();
    							
    			new_request.setIsGenerated(1);
    			
    			if(!res.equals(request.getResponseBody().toString())) {
    				if((int) response.getStatusCode() < 400) {
    					new_request.setIsGenerated(3);
    				} else {
    					new_request.setIsGenerated(2);
    				}
    			}
    				
    			new_request.setUrl(request.getUrl().toString());
    			new_request.setFirstRecorded("timmy");
    			new_request.setRequestBody(request.getRequestBody().toString());
    			
    			new_request.setResponseType(response.getContentType());
    			new_request.setResponseBody(res);
    			
    			new_request.setMethod(request.getMethod().toString());
    			new_request.setModification("modified header number " + i + " from request id " + request.getId().toString());
    			
    			response.disconnect();
    			
    			new_request.save(); // decide if we want this
       		}
			
			
		} catch(Exception e) {
			System.out.println(e.toString());
		}
	
	}
}
