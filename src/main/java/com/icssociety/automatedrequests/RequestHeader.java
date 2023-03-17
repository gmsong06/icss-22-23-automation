package com.icssociety.automatedrequests;

import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarHeader;

@Table("request_headers")
public class RequestHeader extends Model { // the RequestHeader model that is stored in the "request_headers" table
	HarEntry har;
	public RequestHeader() {}
	public RequestHeader(HarHeader header, int requestId) {
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
