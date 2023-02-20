package com.icssociety.automatedrequests;

import java.util.*;

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarHeader;

public class SetResponseHeaders {
    public static void save(Har har) {
        for(int i = 0; i < har.getLog().getEntries().size(); i++) { // loops through all the entries of the .har file
			String req_id = har.getLog().getEntries().get(i).getRequest().getId().toString(); // request id
			List<HarHeader> headers = har.getLog().getEntries().get(i).getResponse().getHeaders(); // list of response headers

			ResponseHeader response_header = new ResponseHeader(); // creates an instance of the ResponseHeader model

			for(HarHeader h: headers) {
				response_header.set("request_id", req_id);
				response_header.set("name", h.getName());
				response_header.set("value", h.getValue());
				response_header.saveIt(); // saves it to the database
			}
			
		}
    }
}