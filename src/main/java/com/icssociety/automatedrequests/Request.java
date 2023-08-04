package com.icssociety.automatedrequests;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarRequest;
import de.sstoehr.harreader.model.HarResponse;

@Table("requests")
public class Request extends Model { // the Request model that is stored in the "requests" table
	
	//contains a har file, a request, a response, and who recorded it. 
	//likewise, has string entries for url, method, response and request body and type, generation status, modification status. 
	//		Has getters and setters for all 
	HarEntry har;
	HarRequest request;
	HarResponse response;
	String firstRecorded;
	public Request() {}
	public Request(HarEntry har, String firstRecorded, int isGenerated) {
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
		this.setIsGenerated(isGenerated);
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
	
	public void setIsGenerated(int isGenerated) {
		this.set("is_generated", isGenerated);
	}
	
	public void setModification(String modification) {
		this.set("modification", modification);
	}

	public void setUrlModification(String modification) {
		this.set("url_modification", modification);
	}

	public void setBodyModification(String modification) {
		this.set("body_modification", modification);
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
	
	public Object getIsGenerated() {
		return this.get("is_generated");
	}
	
	public Object getModification() {
		return this.get("modification");
	}

	public Object getUrlModification() {
		return this.get("url_modification");
	}

	public Object getBodyModification() {
		return this.get("body_modification");
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
