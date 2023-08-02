package com.icssociety.automatedrequests;

import org.javalite.activejdbc.*;

import java.util.*;
import java.io.*;
 

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;

public class Main {
	
	static HashMap<String, GenerationStrategy> staticGenerationStrategies = new HashMap<>();
	static HashMap<Integer, Integer> statusCodes = new HashMap<>();
	static private int totalSensitiveData = 0;
	static private int totalSimilarData = 0;
	static private int totalSuccessfulCalls = 0;
	static private int totalUnsuccessfulCalls = 0;
	static private int totalModifiedCalls = 0;
	static private int totalUnmodifiedCalls = 0;
	
	public static void main(String[] args) throws HarReaderException {
		DBConnection.open();
		
		removeAllRequests();
		removeAllRequestHeaders();
		removeAllResponseHeaders();
		
		File dir = new File("./data");
		String[] files = dir.list();
		files[0]="Extempore.har";

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("./data/data.txt"));

		for(String f : files){
			if(!f.endsWith(".har")) continue;
		
			HarReader harReader = new HarReader();
			Har har = harReader.readFromFile(new File("./data/" + f));

			SaveRequests.save(har);

			putStrategies();

			System.out.println("STARTED MODIFYING " + f);
			for(String str: staticGenerationStrategies.keySet()) {
				GenerationStrategy strategy = staticGenerationStrategies.get(str);
				for(int i = 1; i < Base.count("requests") + 1; i++) {
					Request req = Request.findById(i);
					if(Integer.valueOf(req.getIsGenerated().toString()) == 0) {
						strategy.sendModifiedRequest(req);
						for(int j : strategy.getStatusCodes().keySet()){
							if(statusCodes.containsKey(j)) statusCodes.replace(j, statusCodes.get(j), statusCodes.get(j)+strategy.getStatusCodes().get(j));
							else statusCodes.put(j, strategy.getStatusCodes().get(j));
						}
					}
				}

				writeData(strategy, f, writer);
				addDataTotals(strategy);
			}
			
			System.out.println("STOPPED MODIFYING " + f);
		} 
			
		writeFinalData(writer);

		} catch (IOException io) {

		}
		
		DBConnection.close();
		
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

	public static void putStrategies(){
		staticGenerationStrategies.put("DELETE_HEADER", new GenerationStrategyDeleteHeader());
		staticGenerationStrategies.put("ADD_STRING", new GenerationStrategyAddString());
		staticGenerationStrategies.put("ITERATE_URL", new GenerationStrategyIterateUrlNumbers());
		staticGenerationStrategies.put("ITERATE_USER_ID", new GenerationStrategyIterateUserId());
		staticGenerationStrategies.put("APPEND_BODY", new GenerationStrategyAppendJSON());
		staticGenerationStrategies.put("DELETE_BODY", new GenerationStrategyDeleteBody());
		staticGenerationStrategies.put("ADD_RANDOM_STRING", new GenerationStrategyAddRandomString());
	}

	public static void writeData(GenerationStrategy strategy, String f, BufferedWriter writer){
		try{
			writer.write(strategy.getStrategyDescription() + " from app " + f.substring(0, f.indexOf(".har")) + " generated ");
			writer.write(strategy.getUnsuccessfulRequests() + " unsuccessful requests and ");
			writer.write(strategy.getSuccessfulRequests() + " successful requests with ");
			writer.write(strategy.getModifiedResponses() + " modified responses and ");
			writer.write(strategy.getUnmodifiedResponses() + " unmodified responses\n");
			writer.write("Success Rate: " + 100 * strategy.getSuccessfulRequests()/(strategy.getSuccessfulRequests() + strategy.getUnsuccessfulRequests()) + "%\n");
			if(strategy.getSuccessfulRequests() > 0) {
				writer.write("Modification Rate: " + 100 * strategy.getModifiedResponses()/(strategy.getModifiedResponses() + strategy.getUnmodifiedResponses()) + "%\n");
			}
			writer.write("Number of calls including sensitive data: " + strategy.getSensitiveData()); 
			writer.write("\nNumber of calls including 90% similar data: " + strategy.getSimilarData()); 
			writer.write("\n\n");
			// writer.close();
		} catch (Exception e){
			System.out.println("Error occured in file writing");
		}
	}

	public static void addDataTotals(GenerationStrategy strategy){
		totalSensitiveData+=strategy.getSensitiveData();
		totalSimilarData+=strategy.getSimilarData();
		totalSuccessfulCalls+=strategy.getSuccessfulRequests();
		totalUnsuccessfulCalls+=strategy.getUnsuccessfulRequests();
		totalModifiedCalls+=strategy.getModifiedResponses();
		totalUnmodifiedCalls+=strategy.getUnmodifiedResponses();
	}

	public static void writeFinalData(BufferedWriter writer){
		try{
			// BufferedWriter writer = new BufferedWriter(new FileWriter("./data/data.txt"));

			writer.write("\n\nStatus Code : # of occurances\n");
			for(int i : statusCodes.keySet())
				writer.write(Integer.toString(i) + " : " + Integer.toString(statusCodes.get(i)) + "\n");
			writer.write("\nTotal number of successful calls: " + totalSuccessfulCalls);
			writer.write("\nTotal number of unsuccessful calls: " + totalUnsuccessfulCalls);
			writer.write("\nTotal number of modified calls: " + totalModifiedCalls);
			writer.write("\nTotal number of unmodified calls: " + totalUnmodifiedCalls);
			writer.write("\nTotal number of sensitive calls: " + totalSensitiveData);
			writer.write("\nTotal number of similar calls: " + totalSimilarData);

			writer.close();

		}catch(Exception e){
			System.out.println("An error occured in final file writing");
		}
	}
	
}
