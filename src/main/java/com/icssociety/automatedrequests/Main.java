package com.icssociety.automatedrequests;

import org.javalite.activejdbc.*;

import org.json.JSONObject;//for sending requests

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;//likewise until here

import java.util.*;
import java.io.*;
 

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;

import java.util.*;
import java.io.*;

public class Main {
    
	public static void main(String[] args) throws HarReaderException {
		DBConnection.open(); // opens the connection to the database
		
		// removeAllRequests(); // WARNING only call if you want to remove all the rows in the table
		
		HarReader harReader = new HarReader(); // creates an instance of HarReader, the module we use to read .har data
		Har har = harReader.readFromFile(new File("./data/Extempore.har")); // har stores the .har data by using the harReader created above
		
		for(int i = 0; i < har.getLog().getEntries().size(); i++) { // loops through all the entries of the .har file
			HarEntry entry = har.getLog().getEntries().get(i);
			String method = entry.getRequest().getMethod().toString(); // request method
			String url = entry.getRequest().getUrl().toString(); // request url
			String req_body = ""; // request body if it exists
			if(entry.getRequest().getPostData().getText() != null) { // checks if there is a request body
				req_body = entry.getRequest().getPostData().getText().toString(); 
			}
			
			int res_status = entry.getResponse().getStatus(); // response status
			
			String res_type = "";
			String res_body = "";
			
			if(entry.getResponse().getContent().getMimeType() != null) { // checks if there is a response type
				res_type = entry.getResponse().getContent().getMimeType().toString(); 
			}
			
			if(entry.getResponse().getContent().getText() != null) { //checks if there is a response body
				res_body = entry.getResponse().getContent().getText().toString();		
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

		SetResponseHeaders.save(har);
		
		HttpRequest req = null;
		
		req = Unirest.get("api.genshin.dev/artifacts/adventurer");//random url
		System.out.println(req.getUrl());

		try {
			
			  HttpResponse<JsonNode> boom = req.asJson();
			 
			  System.out.println(boom);
				JsonNode node = boom.getBody();
				System.out.println(node);
			} catch (UnirestException e) {
				System.out.println("request error occurred: " + e);
			}
		
		DBConnection.close(); // closes the connection to the database
		SetRequestHeader.save();
		
	}
	
	// removes all the rows in the requests table in the database
	public static void removeAllRequests() {
		Request.deleteAll();
		Base.exec("ALTER TABLE requests AUTO_INCREMENT = 1");
	}
}
