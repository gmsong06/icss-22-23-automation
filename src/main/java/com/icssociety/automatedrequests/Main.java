package com.icssociety.automatedrequests;

import org.javalite.activejdbc.*;

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
		DBConnection.open();
		
		// removeAllRequests(); // only call if you want to remove all the rows in the table
		
		HarReader harReader = new HarReader();
		Har har = harReader.readFromFile(new File("./data/Extempore.har"));

		SaveRequests.save(har);
		SetRequestHeader.save(har);
		SetResponseHeaders.save(har);
		
		DBConnection.close();
		
	}
	
	// removes all the rows in the requests table in the database
	public static void removeAllRequests() {
		Request.deleteAll();
		Base.exec("ALTER TABLE requests AUTO_INCREMENT = 1");
	}
}
