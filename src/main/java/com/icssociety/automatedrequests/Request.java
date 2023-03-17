package com.icssociety.automatedrequests;

import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarRequest;
import de.sstoehr.harreader.model.HarResponse;

@Table("requests")
public class Request extends Model { // the Request model that is stored in the "requests" table
	HarEntry har;
	HarRequest request;
	HarResponse response;
	String firstRecorded;
	public Request() {}
	public Request(HarEntry har, String firstRecorded) {
		this.har = har;
		this.request = har.getRequest();
		this.response = har.getResponse();
		
		String method = request.getMethod().toString();
		String url = request.getUrl().toString();
		String reqBody = "";
		
		if(this.checkRequestBody()) {
			reqBody = request.getPostData().getText().toString(); 
		}
		
		int resStatus = response.getStatus();
		String resType = "";
		String resBody = "";
		
		if(this.checkResponseType()) {
			resType = response.getContent().getMimeType().toString(); 
		}
		
		if(this.checkResponseBody()) {
			resBody = response.getContent().getText().toString();		
		}
		
		this.firstRecorded = firstRecorded;
		this.setMethod(method);
		this.setUrl(url);
		this.setRequestBody(reqBody);
		this.setResponseStatus(resStatus);
		this.setResponseType(resType);
		this.setResponseBody(resBody);
		this.setFirstRecorded(firstRecorded);
	}
	
	public void setMethod(String method) {
		this.set("method", method);
	}
	
	public void setUrl(String url) {
		this.set("url", url);
	}
	
	public void setRequestBody(String reqBody) {
		this.set("request_body", reqBody);
	}
	
	public void setResponseStatus(int resStatus) {
		this.set("response_status", resStatus);
	}
	
	public void setResponseType(String resType) {
		this.set("response_type", resType);
	}
	
	public void setResponseBody(String resBody) {
		this.set("response_body", resBody);
	}
	
	public void setFirstRecorded(String firstRecorded) {
		this.set("first_recorded", firstRecorded);
	}
	
	public Object getMethod() {
		return this.get("method");
	}
	
	public Object getUrl() {
		return this.get("url");
	}
	
	public Object getRequestBody() {
		return this.get("request_body");
	}
	
	public Object getResponseStatus() {
		return this.get("response_status");
	}
	
	public Object getResponseType() {
		return this.get("response_type");
	}
	
	public Object getResponseBody() {
		return this.get("response_body");
	}
	
	public Object getFirstRecorded() {
		return this.get("first_recorded");
	}
	
	public boolean checkRequestBody() {
		if(request.getPostData().getText() != null) return true;
		return false;
	}
	
	public boolean checkResponseType() {
		if(response.getContent().getMimeType() != null) return true;
		return false;
	}
	
	public boolean checkResponseBody() {
		if(response.getContent().getText() != null) return true;
		return false;
	}
}
