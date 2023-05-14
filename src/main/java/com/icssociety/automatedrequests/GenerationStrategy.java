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

public class GenerationStrategy {
	public static void deleteHeader(Request request, int id) { // removes one header
		List<RequestHeader> headers = RequestHeader.find("request_id = ?", request.getId());
		for(int i = 0; i < headers.size(); i++) {
			List<RequestHeader> temp = headers;
			int headerIndex = i;
			
			temp.remove(headerIndex);
			request.setIsGenerated(1);
			request.setModification("removed header number " + (headerIndex + 1) + " from the request with id of " + id);

			System.out.println("SENDING MODIFIED REQUEST # " + i);
			sendModifiedRequest(request, temp);			
			System.out.println("COMPLETED MODIFIED REQUEST # " + i);			
			
			for(RequestHeader h : temp) {
				h.setRequestId(id);
				h.save();
			}
		}
	}

	private static void sendModifiedRequest(Request request, List<RequestHeader> modifiedHeaders) {
		HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		try {
       		HttpRequest new_request = HTTP_TRANSPORT.createRequestFactory()
       				.buildRequest(
       						(String) request.getMethod(), 
       						new GenericUrl((String) request.getUrl()), 
       						null
       				);
			
       		HttpHeaders new_headers = new HttpHeaders();
			for(RequestHeader h: modifiedHeaders) {
				new_headers.set((String) h.getName(), (String) h.getValue());
			}

			HttpResponse response = new_request.setHeaders(new_headers).executeAsync().get();
			request.setResponseStatus(response.getStatusCode());
			String res = response.parseAsString();

			if(!res.equals(request.getResponseBody().toString())) {
				if((int) response.getStatusCode() < 400) {
					request.setIsGenerated(3);
				} else {
					request.setIsGenerated(2);
				}
			}

			request.setResponseBody(res);
			response.disconnect();
			
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		request.save();
	}
}
