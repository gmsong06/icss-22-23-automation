package com.icssociety.automatedrequests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenerationStrategyIterateUrlNumbers extends GenerationStrategy {

     @Override
    public List<String> modifyUrl(Request request) {
        List<String> modified_urls = new ArrayList<>();
        String currentUrl = request.getUrl().toString();
        int paramBeginIndex = 0;
        while(currentUrl.indexOf("=", paramBeginIndex) > -1) {
            paramBeginIndex = currentUrl.indexOf("=", paramBeginIndex);
            int paramEndIndex = currentUrl.indexOf("&", paramBeginIndex);
            if(paramEndIndex <= -1) paramEndIndex = currentUrl.length();
            String param = currentUrl.substring(paramBeginIndex + 1, paramEndIndex);
               
            Set<String> modified_params = new HashSet<>();
            for(int i = 0; i < param.length(); i++) {
                String a = param.substring(0, i);
                String b = param.substring(i+1, param.length());
                char c = param.charAt(i);
                if(Character.isDigit(c)) {
                    modified_params.add(a + Math.abs((Character.getNumericValue(c)+1)) + b);
                    modified_params.add(a + Math.abs((Character.getNumericValue(c)-1)) + b);
                    modified_params.add(a + Math.abs((Character.getNumericValue(c)+5)) + b);
                    modified_params.add(a + Math.abs((Character.getNumericValue(c)-5)) + b);
                } else if(Character.isLetter(c)) {
                    modified_params.add(a + (char)(c+1) + b);
                    modified_params.add(a + (char)(c-1) + b);
                    modified_params.add(a + (char)(c+10) + b);
                    modified_params.add(a + (char)(c-10) + b);
                }
            }

            String a = currentUrl.substring(0, paramBeginIndex);
            String b = currentUrl.substring(Math.min(paramEndIndex + 1, currentUrl.length()), currentUrl.length());
            for(String s: modified_params) {
                modified_urls.add(a + s + b);
            }

            paramBeginIndex = paramEndIndex;
        }
        
        return modified_urls;
    }
}