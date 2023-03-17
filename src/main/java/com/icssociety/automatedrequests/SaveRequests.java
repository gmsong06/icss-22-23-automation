package com.icssociety.automatedrequests;

import java.math.BigInteger;
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
    	for(int i = 0; i < entries.size(); i++) {
			HarEntry entry = entries.get(i);
			
			Request request = new Request(entry, "Ann");
			request.save();
			
			List<HarHeader> requestHeaders = entry.getRequest().getHeaders();
			for(HarHeader h : requestHeaders) {
				RequestHeader requestHeader = new RequestHeader(h, ((BigInteger) request.getId()).intValue());
				requestHeader.save();
			}
			
			List<HarHeader> responseHeaders = entry.getResponse().getHeaders();
			for(HarHeader h : responseHeaders) {
				ResponseHeader responseHeader = new ResponseHeader(h, ((BigInteger) request.getId()).intValue());
				responseHeader.save();
			}
			
		}
    }
}