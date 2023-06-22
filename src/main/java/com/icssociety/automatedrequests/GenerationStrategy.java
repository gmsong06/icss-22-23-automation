package com.icssociety.automatedrequests;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class GenerationStrategy {
	
	public List<String> modifyUrl(Request request) {
		List<String> unmodifiedUrl = new ArrayList<>();
		unmodifiedUrl.add(request.getUrl().toString());
		return unmodifiedUrl;
	}

	public Map<HttpHeaders, String> modifyHeaders(Request request) {
		Map<HttpHeaders, String> unmodifiedHeaders = new HashMap<>();
		List<RequestHeader> headers = RequestHeader.find("request_id = ?", request.getId());
		HttpHeaders httpHeaders = new HttpHeaders();
		for(RequestHeader h : headers) {
			String name = (String) h.getName();
			if(name.equalsIgnoreCase("content-length")) {
				httpHeaders.setContentLength(Long.parseLong((String) h.getValue()));
			} else {
				httpHeaders.set(name, (Object) h.getValue());
			}
		}
		unmodifiedHeaders.put(httpHeaders, "Did not modify headers");
		return unmodifiedHeaders;
	}

	public Map<String, String> modifyBody(Request request) {
		Map<String, String> unmodifiedBody = new HashMap<>();
		unmodifiedBody.put(request.getRequestBody().toString(), "Did not modify body");
		return unmodifiedBody;
	}
	
	public void sendModifiedRequest(Request request) {
		//setting up + building HTTP Requests
		HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	
		try {
			for(String url: modifyUrl(request)) {
				HttpRequest sent_request = HTTP_TRANSPORT.createRequestFactory()
       				.buildRequest(
       						(String) request.getMethod(), 
       						new GenericUrl(url), 
       						null
       				);
	
       		
			Map<HttpHeaders, String> modified_headers = modifyHeaders(request);
			Map<String, String> modified_bodies = modifyBody(request);
       		List<HttpHeaders> new_headers = new ArrayList<>(modified_headers.keySet());

       		//loop through each modified header
       		for(int i = 0; i < new_headers.size(); i++) {
				for(String body: modified_bodies.keySet()) {
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

					new_request.setUrl(url);
					new_request.setFirstRecorded("timmy");
					new_request.setRequestBody(body);
					new_request.setBodyModification(modified_bodies.get(body));
					
					new_request.setResponseType(response.getContentType());
					new_request.setResponseBody(res);
					
					new_request.setMethod(request.getMethod().toString());
					new_request.setModification(modified_headers.get(new_headers.get(i)));
					
					response.disconnect();
					
					new_request.save(); // decide if we want this
				}
       		}
			}
			
		} catch(Exception e) {
			System.out.println(e.toString());
		}
	
	}
}
