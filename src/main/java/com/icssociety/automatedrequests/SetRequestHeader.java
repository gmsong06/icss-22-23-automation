package com.icssociety.automatedrequests;

import org.javalite.activejdbc.*;

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarHeader;

import java.util.*;
import java.io.*;

public class SetRequestHeader {
    
	public static void save() throws HarReaderException {
		DBConnection.open(); // opens the connection to the database
		
		// removeAllRequests(); // WARNING only call if you want to remove all the rows in the table
		
		HarReader harReader = new HarReader(); // creates an instance of HarReader, the module we use to read .har data
		Har har = harReader.readFromFile(new File("./data/your_file_name.har")); // har stores the .har data by using the harReader created above
		List<HarEntry> entries = har.getLog().getEntries();
		for(int i = 0; i < entries.size(); i++) { // loops through all the entries of the .har file
			List<HarHeader> headers = entries.get(i).getRequest().getHeaders();
			for (int j = 0; j < headers.size(); j++) {
				
				String name = headers.get(j).getName().toString();
				String value = headers.get(j).getValue().toString();
								
				RequestHeader header = new RequestHeader(); // creates an instance of the Request model
				
				// // populates the request object with all the necessary info
				header.set("value", value);
				header.set("name", name);
				
				header.saveIt(); // saves it to the database
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
