package com.scout24.thread;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceValidation implements Callable<Map<String, Integer>> {

	private Logger logger = LoggerFactory.getLogger(ResourceValidation.class);
	
    private List<String> resourceList;
    private Map<String, Integer> resourceValidationMap = new HashMap<>();

    public ResourceValidation(List<String> resourceList) {
        this.resourceList = resourceList;
    }

    @Override
    public Map<String, Integer> call() {
        for (String resource: resourceList) {
            int respCode;
            try {
                respCode = getResponseCode(resource);
                resourceValidationMap.put(resource, respCode);
            } catch (MalformedURLException e) {
            	logger.error("get protocol other than HTTP(s) "+resource);
            } catch (IOException e) {
            	logger.error("error while connect to URL provided "+resource+ "  "+e.getCause());
            }
        }
        return resourceValidationMap;
    }

    public static int getResponseCode(String resource) throws IOException {

        URL url = new URL(resource);

        if (url.openConnection() instanceof HttpURLConnection) {
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setInstanceFollowRedirects(false);
            HttpURLConnection.setFollowRedirects(false);
            connection.connect();
            return connection.getResponseCode();
        }
        return -1;
    }

}
