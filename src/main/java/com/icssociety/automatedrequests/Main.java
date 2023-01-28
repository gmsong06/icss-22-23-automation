package com.icssociety.automatedrequests;

import com.icssociety.automatedrequests.ApiRequest;

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
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://100.26.10.112:3306/ICSSData";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "P6iHQb^a6*p";
    
	public static void main(String[] args) throws HarReaderException {
		Base.open(DRIVER, URL, USERNAME, PASSWORD);
		
		HarReader harReader = new HarReader();
		Har har = harReader.readFromFile(new File("extempore.har"));
		
		for(int i = 0; i < har.getLog().getEntries().size(); i++) {
			System.out.println("Request url: " + har.getLog().getEntries().get(i).getRequest().getUrl());
			System.out.println("Request method: " + har.getLog().getEntries().get(i).getRequest().getMethod());
			System.out.println("Request headers:");
			for(int j = 0; j < har.getLog().getEntries().get(i).getRequest().getHeaders().size(); j++) {
				System.out.println("name: " + har.getLog().getEntries().get(i).getRequest().getHeaders().get(j).getName());
				System.out.println("value: " + har.getLog().getEntries().get(i).getRequest().getHeaders().get(j).getValue());
				System.out.println();
			}
			System.out.println("---------------------------------");
		}
		
		/*
		// Create a new request object
		ApiRequest request = new ApiRequest();
		request.set("url", "https://test.com/api");
		request.set("method", "GET");
		request.set("request_headers", "{\"Content-Type\":\"application/json\"}");
		request.set("response_status", 200);
		request.set("response_headers", "{\"Content-Type\":\"application/json\"}");
		request.set("response_body", "{\"message\":\"Hello World\"}");

		// Save the request to the database
		request.saveIt();
		*/
		
		Base.close();
		
	}
}
