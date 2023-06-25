package com.icssociety.automatedrequests;

import org.javalite.activejdbc.*;

import java.util.*;
import java.io.*;
 

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;

public class Main {
	
	static HashMap<String, GenerationStrategy> staticGenerationStrategies = new HashMap<>();
	
	public static void main(String[] args) throws HarReaderException {
		DBConnection.open();
		
		removeAllRequests();
		removeAllRequestHeaders();
		removeAllResponseHeaders();
		
		HarReader harReader = new HarReader();
		Har har = harReader.readFromFile(new File("./data/Extempore.har"));

		SaveRequests.save(har);
		
		staticGenerationStrategies.put("DELETE_HEADER", new GenerationStrategyDeleteHeader());
		staticGenerationStrategies.put("ADD_STRING", new GenerationStrategyAddString());
		staticGenerationStrategies.put("ITERATE_URL", new GenerationStrategyIterateUrlNumbers());
		staticGenerationStrategies.put("ITERATE_USER_ID", new GenerationStrategyIterateUserId());
		staticGenerationStrategies.put("APPEND_BODY", new GenerationStrategyAppendJSON());
		staticGenerationStrategies.put("DELETE_BODY", new GenerationStrategyDeleteBody());
		staticGenerationStrategies.put("ADD_RANDOM_STRING", new GenerationStrategyAddRandomString());
		
		System.out.println("STARTED MODIFYING");
		for(int i = 1; i < Base.count("requests") + 1; i++) {
			Request req = Request.findById(i);
				if(Integer.valueOf(req.getIsGenerated().toString()) == 0) {
					generateModifiedRequests(req, i);
				}
		 }
		System.out.println("STOPPED MODIFYING");
		
		DBConnection.close();
		
	}
	
	public static void generateModifiedRequests(Request request, int id) {
		for(String i : staticGenerationStrategies.keySet()) {
			GenerationStrategy strategy = staticGenerationStrategies.get(i);
			
			strategy.sendModifiedRequest(request);
			
		}

	}
	public static void removeAllRequests() {
		Base.exec("DELETE FROM requests");
		Base.exec("ALTER TABLE requests AUTO_INCREMENT = 1");
	}
	
	public static void removeAllRequestHeaders() {
		Base.exec("DELETE FROM request_headers");
		Base.exec("ALTER TABLE request_headers AUTO_INCREMENT = 1");
	}
	
	public static void removeAllResponseHeaders() {
		Base.exec("DELETE FROM response_headers");
		Base.exec("ALTER TABLE response_headers AUTO_INCREMENT = 1");
	}
	
}
