package main;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIHandler {
	public OkHttpClient client = new OkHttpClient();
	public String apiURL, authKey;
	
	/**
	 * Handles API calls and other functions
	 * @param apiURL The URL of the API
	 * @param authKey The authentication key parameter to be passed
	 */
	public APIHandler(String apiURL, String authKey) {
		this.apiURL = apiURL;
		this.authKey = authKey;
	}
	
	/**
	 * Returns info at requested part of site
	 * @param url Direction to the part of the API that should be accessed
	 * @return The data if found
	 */
	public String getInfo (String url) {
		Request request = new Request.Builder()
	      .url(apiURL + url + authKey)
	      .build();
			Response response;
		
		try {
			response = client.newCall(request).execute();
			return response.body().string();
		} catch (IOException e) {e.printStackTrace();return e.getStackTrace().toString();}
	}
}
