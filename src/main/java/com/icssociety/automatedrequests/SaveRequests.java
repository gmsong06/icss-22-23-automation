package com.icssociety.automatedrequests;

import java.math.BigInteger;
import java.util.*;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarHeader;

public class SaveRequests {
    public static void save(Har har) {
    	//saves all responses + requests and their headers in the data base for a given HAR
    	List<HarEntry> entries = har.getLog().getEntries();
    	for(int i = 0; i < entries.size(); i++) {
			HarEntry entry = entries.get(i);
			
			Request request = new Request(entry, "Ann", 0);
			try {
				request.save();
			} catch(Exception e) {
				System.out.println("Error occurred saving request");
				continue;
			}
			
			
			List<HarHeader> requestHeaders = entry.getRequest().getHeaders();
			for(HarHeader h : requestHeaders) {
				RequestHeader requestHeader = new RequestHeader(h, ((BigInteger) request.getId()).intValue());
				try {
				requestHeader.save();
			} catch(Exception e) {
				System.out.println("Error occurred saving request header");
			}
			
			}
			
			List<HarHeader> responseHeaders = entry.getResponse().getHeaders();
			for(HarHeader h : responseHeaders) {
				ResponseHeader responseHeader = new ResponseHeader(h, ((BigInteger) request.getId()).intValue());
				try {
				responseHeader.save();
			} catch(Exception e) {
				System.out.println("Error occurred saving response header");
			}
			
			}
			
		}
    }
}