package com.icssociety.automatedrequests;
import java.math.BigInteger;
import java.util.List;

public class GenerationStrategy {
	public static void deleteHeader(Request request, int id) { // removes one header
		List<RequestHeader> headers = RequestHeader.find("request_id = ?", request.getId());
		for(int i = 0; i < headers.size(); i++) {
			List<RequestHeader> temp = headers;
			int headerIndex = i;
			
			temp.remove(headerIndex);
			
			request.setIsGenerated(1);
			request.setId(null);
			request.setModification("removed header number " + (headerIndex + 1) + " from the request with id of " + id);
			request.save();
			
			for(RequestHeader h : temp) {
				h.setRequestId(id);
				h.setId(null);
				h.save();
			}
		}
	}
}
