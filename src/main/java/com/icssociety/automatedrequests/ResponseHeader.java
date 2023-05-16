package com.icssociety.automatedrequests;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarHeader;

@Table("response_headers")
public class ResponseHeader extends Model { // the ResponseHeader model that is stored in the "response-headers" table
	//stores the headers of a specific response, has name, value, and id
	//		Has getters and setters for all of them
	
	HarEntry har;
	public ResponseHeader() {}
	public ResponseHeader(HarHeader header, int requestId) {
		String name = header.getName().toString();
		String value = header.getValue().toString();
		
		this.setName(name);
		this.setValue(value);
		this.setRequestId(requestId);
	}
	
	public void setName(String name) {
		this.set("name", name);
	}
	
	public void setValue(String value) {
		this.set("value", value);
	}
	
	public void setRequestId(int requestId) {
		this.set("request_id", requestId);
	}
	
	public Object getName() {
		return this.get("name");
	}
	
	public Object getValue() {
		return this.get("value");
	}
	
	public Object getRequestId() {
		return this.get("request_id");
	}
}
