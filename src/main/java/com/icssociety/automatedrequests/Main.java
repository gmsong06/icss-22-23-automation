package com.icssociety.automatedrequests;

import com.icssociety.automatedrequests.ApiRequest;

import org.javalite.activejdbc.*;

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.*;

public class Main {
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://100.26.10.112:3306/ICSSData";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "P6iHQb^a6*p";
    
	public static void main(String[] args) throws HarReaderException {
		Base.open(DRIVER, URL, USERNAME, PASSWORD);

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
		
		
		Base.close();
		
	}
}
