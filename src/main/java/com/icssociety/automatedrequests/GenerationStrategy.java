package com.icssociety.automatedrequests;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import net.ricecode.similarity.LevenshteinDistanceStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;

import java.util.regex.*;

public abstract class GenerationStrategy {

	public abstract String getStrategyDescription();
	private int successfulRequests = 0;
	private int unsuccessfulRequests = 0;
	private int modifiedResponses = 0;
	private int unmodifiedResponses = 0;
	private int numSensitiveData = 0;
	private int numSimilarData = 0;
	private int numFlaggedData = 0;
	private int numErrorData = 0;
	private int numCodeData = 0;
	private HashMap<Integer, Integer> statusCodes = new HashMap<>();

	
	public List<String> modifyUrl(Request request) {
		List<String> unmodifiedUrl = new ArrayList<>();
		unmodifiedUrl.add(request.getUrl().toString());
		return unmodifiedUrl;
	}

	public Map<HttpHeaders, String> modifyHeaders(Request request) {
		Map<HttpHeaders, String> unmodifiedHeaders = new HashMap<>();
		List<RequestHeader> headers = RequestHeader.find("request_id = ?", request.getId());
		HttpHeaders httpHeaders = new HttpHeaders();
		for(RequestHeader h : headers) {
			String name = (String) h.getName();
			if(name.equalsIgnoreCase("content-length")) {
				httpHeaders.setContentLength(Long.parseLong((String) h.getValue()));
			} else {
				httpHeaders.set(name, (Object) h.getValue());
			}
		}
		unmodifiedHeaders.put(httpHeaders, "Did not modify headers");
		return unmodifiedHeaders;
	}

	public Map<String, String> modifyBody(Request request) {
		Map<String, String> unmodifiedBody = new HashMap<>();
		unmodifiedBody.put(request.getRequestBody().toString(), "Did not modify body");
		return unmodifiedBody;
	}
	
	public void sendModifiedRequest(Request request, BufferedWriter flagWriter, BufferedWriter errorWriter) {

		//setting up + building HTTP Requests
		HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	
		try {
			for(String url: modifyUrl(request)) {
				HttpRequest sent_request = HTTP_TRANSPORT.createRequestFactory()
       				.buildRequest(
       						(String) request.getMethod(), 
       						new GenericUrl(url), 
       						null
       				);

       		
			Map<HttpHeaders, String> modified_headers = modifyHeaders(request);
			Map<String, String> modified_bodies = modifyBody(request);
       		List<HttpHeaders> new_headers = new ArrayList<>(modified_headers.keySet());

       		//loop through each modified header
       		for(int i = 0; i < new_headers.size(); i++) {
				for(String body: modified_bodies.keySet()) {
					Request new_request = new Request();
       			
					HttpResponse response = sent_request.setHeaders(new_headers.get(i)).executeAsync().get();
					
		
					new_request.setResponseStatus(response.getStatusCode());
					if(statusCodes.containsKey(200)) statusCodes.replace(200, statusCodes.get(200), statusCodes.get(200)+1);
					else statusCodes.put(200, 0);

					String res = response.parseAsString();
					new_request.setIsGenerated(1);
					
					if(!res.equals(request.getResponseBody().toString())) {

						if((int) response.getStatusCode() < 400) {
							new_request.setIsGenerated(3);
							modifiedResponses ++;
						
						} else {
							new_request.setIsGenerated(2);
						}
					} else unmodifiedResponses++;

					new_request.setUrl(url);
					new_request.setFirstRecorded("timmy");
					new_request.setRequestBody(body);
					new_request.setBodyModification(modified_bodies.get(body));
					
					new_request.setResponseType(response.getContentType());
					new_request.setResponseBody(res);
					
					new_request.setMethod(request.getMethod().toString());
					new_request.setModification(modified_headers.get(new_headers.get(i)));
					
					//if the modified response body contains sensitive data using regexs


					if(returnsCode(res)) {
						System.out.println("code");
						errorWriter.write("Code returned:" + "\n");
						errorWriter.write(modified_headers.get(new_headers.get(i)) + "\n");
						errorWriter.write(modified_bodies.get(body) + "\n");
						errorWriter.write("\n");
						numCodeData++;
					}
					 else if(checkLength(res)) {
						System.out.println("error, response too long to parse");
						errorWriter.write("Error parsing data:" + "\n");
						errorWriter.write(modified_headers.get(new_headers.get(i)) + "\n");
						errorWriter.write(modified_bodies.get(body) + "\n");
						errorWriter.write(res + "\n");
						errorWriter.write("\n");
						numErrorData++;
					}  else {
						if(containsData(res)) {
						numSensitiveData++;
						//flag dissimilar and sensitive data
						if(!is90PercentSimilar(request.getResponseBody().toString(), res)) {
							new_request.setIsGenerated(-1);
							flagWriter.write(modified_headers.get(new_headers.get(i)) + "\n");
							flagWriter.write(modified_bodies.get(body) + "\n");
							flagWriter.write("\n");
							numFlaggedData++;
						}
						}
						//if the unmodified request response body is similar to the modified request response body
						if(is90PercentSimilar(request.getResponseBody().toString(), res)) numSimilarData++;
					}

					response.disconnect();
					successfulRequests++;
					new_request.save(); // decide if we want this
				}
       		}
			}

			} catch(ExecutionException e) {
				Throwable cause = e.getCause();
    
   				if (cause instanceof HttpResponseException) {
       				int responseCode = ((HttpResponseException) cause).getStatusCode();
					
					if(!statusCodes.containsKey(responseCode)) statusCodes.put(responseCode, 1);
					else statusCodes.replace(responseCode, statusCodes.get(responseCode), statusCodes.get(responseCode)+1);
				}
				unsuccessfulRequests++;
			} catch(Exception e){
				unsuccessfulRequests++;
			}
			
		
		// System.out.println(this.getStrategyDescription() + " generated: ");
		// System.out.print(unsuccessfulRequests + " unsuccessful requests and ");
		// System.out.print(successfulRequests + "successful requests with ");
		// System.out.print(modifiedResponses + "modified responses and ");
		// System.out.print(unmodifiedResponses + "unmodified responses");
	}

	public int getSuccessfulRequests() {
		return successfulRequests;
	}

	public int getUnsuccessfulRequests() {
		return unsuccessfulRequests;
	}

	public int getModifiedResponses() {
		return modifiedResponses;
	}

	public int getUnmodifiedResponses() {
		return unmodifiedResponses;
	}

	public int getSensitiveData(){
		return numSensitiveData;
	}

	public int getSimilarData(){
		return numSimilarData;
	}

	public int getFlaggedData() {
		return numFlaggedData;
	}

	public int getErrorData() {
		return numErrorData;
	}

	public int getCodeData() {
		return numCodeData;
	}

	public HashMap<Integer,Integer> getStatusCodes(){
		return statusCodes;
	}

	public static boolean returnsCode(String input) {
		String cssRegex = "\\b(css|border|color|font)\\b";
		String htmlRegex = "\\b(<body>|<main>|</svg>|/>)\\b";
		String javaScriptRegex = "\\b(void|console|function|return|var|request|\u00FF)\\b";
		String combinedRegex = cssRegex + "|" + htmlRegex + "|" + javaScriptRegex;
		Pattern pattern = Pattern.compile(combinedRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
	}

	public static boolean checkLength(String input) {
		return input.length() > 100000;
	}

	public static boolean containsData(String input) {
        String phoneRegex = "\\b\\d{3}[-.]?\\d{3}[-.]?\\d{4}\\b";
        String emailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
        String addressRegex = "\\b\\d+\\s+([A-Za-z]+\\s+){1,5}(Avenue|St|Street|Rd|Road|Lane|Blvd|Boulevard)\\b";
        String ssnRegex = "\\b\\d{3}-\\d{2}-\\d{4}\\b";
        String nameRegex = "\\b[A-Z][a-z]+\\s[A-Z][a-z]+\\b";
		String infoRegex = "\\b(username|role|position|userid|id|name|firstName|lastName)\\b";

        String combinedRegex = phoneRegex + "|" + emailRegex + "|" + addressRegex + "|" + ssnRegex + "|" + nameRegex + "|" + infoRegex;
        Pattern pattern = Pattern.compile(combinedRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

	public static boolean is90PercentSimilar(String str1, String str2) {
		SimilarityStrategy strategy = new LevenshteinDistanceStrategy();
		StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
		System.out.println(str1.length());
		System.out.println(str2.length());
		return service.score(str1, str2) >= 0.9;
		// int len1 = str1.length();
        // int len2 = str2.length();

        // if (len1 > len2) {
        //     String temp = str1;
        //     str1 = str2;
        //     str2 = temp;
        //     int tempLen = len1;
        //     len1 = len2;
        //     len2 = tempLen;
        // }

        // int[] prevRow = new int[len1 + 1];
        // int[] currRow = new int[len1 + 1];

        // for (int i = 0; i <= len1; i++) {
        //     prevRow[i] = i;
        // }

        // for (int j = 1; j <= len2; j++) {
        //     currRow[0] = j;
        //     for (int i = 1; i <= len1; i++) {
        //         int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;
        //         int deletion = prevRow[i] + 1;
        //         int insertion = currRow[i - 1] + 1;
        //         int substitution = prevRow[i - 1] + cost;
        //         currRow[i] = Math.min(Math.min(deletion, insertion), substitution);
        //     }
        //     int[] temp = prevRow;
        //     prevRow = currRow;
        //     currRow = temp;
        // }

        // int distance = prevRow[len1];
        // double similarity = 1.0 - (double) distance / Math.max(len1, len2);
        // return similarity >= 0.9;
	}
}
