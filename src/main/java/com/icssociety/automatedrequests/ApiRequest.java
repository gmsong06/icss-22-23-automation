package com.icssociety.automatedrequests;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("api_requests")
public class ApiRequest extends Model {

    public void setMethod(String method) {
        setString("method", method);
    }

    public String getMethod() {
        return getString("method");
    }

    public void setUrl(String url) {
        setString("url", url);
    }

    public String getUrl() {
        return getString("url");
    }

    public void setRequestHeaders(String requestHeaders) {
        setString("request_headers", requestHeaders);
    }

    public String getRequestHeaders() {
        return getString("request_headers");
    }

    public void setResponseStatus(int responseStatus) {
        setInteger("response_status", responseStatus);
    }

    public int getResponseStatus() {
        return getInteger("response_status");
    }

    public void setResponseHeaders(String responseHeaders) {
        setString("response_headers", responseHeaders);
    }

    public String getResponseHeaders() {
        return getString("response_headers");
    }

    public void setResponseBody(String responseBody) {
        setString("response_body", responseBody);
    }

    public String getResponseBody() {
        return getString("response_body");
    }
}
