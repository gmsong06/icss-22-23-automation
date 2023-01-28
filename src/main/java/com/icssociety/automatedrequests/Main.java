package com.icssociety.automatedrequests;

import com.icssociety.automatedrequests.Request;

import org.javalite.activejdbc.*;

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.*;
import java.io.*;

public class Main {
    
	public static void main(String[] args) throws HarReaderException {
		DBConnection.open();
		
		removeAllRequests(); // only call if you want to erase the entire table
		HarReader harReader = new HarReader();
		Har har = harReader.readFromFile(new File("extempore.har"));
		
		for(int i = 0; i < har.getLog().getEntries().size(); i++) {
			String method = har.getLog().getEntries().get(i).getRequest().getMethod().toString();
			String url = har.getLog().getEntries().get(i).getRequest().getUrl().toString();
			String req_body = "";
			if(har.getLog().getEntries().get(i).getRequest().getPostData().getText() != null) {
				req_body = har.getLog().getEntries().get(i).getRequest().getPostData().getText().toString();
			}
			
			int res_status = har.getLog().getEntries().get(i).getResponse().getStatus();
			
			String res_type = "";
			String res_body = "";
			
			if(har.getLog().getEntries().get(i).getResponse().getContent().getMimeType() != null) {
				res_type = har.getLog().getEntries().get(i).getResponse().getContent().getMimeType().toString();
			}
			
			if(har.getLog().getEntries().get(i).getResponse().getContent().getText() != null) {
				res_body = har.getLog().getEntries().get(i).getResponse().getContent().getText().toString();		
			}
			String first_recorded = "Tiffany";
			
			Request request = new Request();
			request.set("method", method);
			request.set("url", url);
			request.set("request_body", req_body);
			request.set("response_status", res_status);
			request.set("response_type", res_type);
			request.set("response_body", res_body);
			request.set("first_recorded", first_recorded);
			
			request.saveIt();
		}
		
		DBConnection.close();
		
	}
	
	public static void removeAllRequests() {
		Request.deleteAll();
		Base.exec("ALTER TABLE requests AUTO_INCREMENT = 1");
	}
}
