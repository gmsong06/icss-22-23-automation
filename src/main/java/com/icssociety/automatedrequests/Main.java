package com.icssociety.automatedrequests;

import org.javalite.activejdbc.*;

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;

import java.util.*;
import java.io.*;

public class Main {
    
	public static void main(String[] args) throws HarReaderException {
		DBConnection.open(); // opens the connection to the database
		
		removeAllRequests(); // WARNING only call if you want to remove all the rows in the table
		
		HarReader harReader = new HarReader(); // creates an instance of HarReader, the module we use to read .har data
		Har har = harReader.readFromFile(new File("./data/your_file_name.har")); // har stores the .har data by using the harReader created above
		
		for(int i = 0; i < har.getLog().getEntries().size(); i++) { // loops through all the entries of the .har file
			String method = har.getLog().getEntries().get(i).getRequest().getMethod().toString(); // request method
			String url = har.getLog().getEntries().get(i).getRequest().getUrl().toString(); // request url
			String req_body = ""; // request body if it exists
			if(har.getLog().getEntries().get(i).getRequest().getPostData().getText() != null) { // checks if there is a request body
				req_body = har.getLog().getEntries().get(i).getRequest().getPostData().getText().toString(); 
			}
			
			int res_status = har.getLog().getEntries().get(i).getResponse().getStatus(); // response status
			
			String res_type = "";
			String res_body = "";
			
			if(har.getLog().getEntries().get(i).getResponse().getContent().getMimeType() != null) { // checks if there is a response type
				res_type = har.getLog().getEntries().get(i).getResponse().getContent().getMimeType().toString(); 
			}
			
			if(har.getLog().getEntries().get(i).getResponse().getContent().getText() != null) { //checks if there is a response body
				res_body = har.getLog().getEntries().get(i).getResponse().getContent().getText().toString();		
			}
			String first_recorded = "enter_name"; // who first recorded this data
			
			Request request = new Request(); // creates an instance of the Request model
			
			// populates the request object with all the necessary info
			request.set("method", method);
			request.set("url", url);
			request.set("request_body", req_body);
			request.set("response_status", res_status);
			request.set("response_type", res_type);
			request.set("response_body", res_body);
			request.set("first_recorded", first_recorded);
			
			request.saveIt(); // saves it to the database
		}

		for(int i = 0; i < har.getLog().getEntries().size(); i++) { // loops through all the entries of the .har file
			String req_id = har.getLog().getEntries().get(i).getRequest().getId().toString(); // request id
			List<HarHeader> headers = har.getLog().getEntries().get(i).getResponse().getHeaders(); // list of response headers

			ResponseHeader response_header = new ResponseHeader(); // creates an instance of the ResponseHeader model

			for(HarHeader h: headers) {
				response_header.set("id", id);
				response_header.set("request_id", req_id);
				response_header.set("name", h.getName());
				response_header.set("value", h.getValue());
				response_header.saveIt(); // saves it to the database
			}
			
		}
		
		DBConnection.close(); // closes the connection to the database
		
	}
	
	// removes all the rows in the requests table in the database
	public static void removeAllRequests() {
		Request.deleteAll();
		Base.exec("ALTER TABLE requests AUTO_INCREMENT = 1");
	}
}
