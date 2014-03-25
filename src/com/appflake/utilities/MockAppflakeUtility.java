package com.appflake.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class MockAppflakeUtility {
	
	private static final String CONTENT_TYPE = "application/json";

	private static int responseCode = -1;
	private static JSONObject jsonResponse = null;
	private static HttpURLConnection connection = null;
	
	public static void processGetRequest(String spec, String param, int value) {
		try {
			URL url = new URL(spec + "/" + value);
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-Type", CONTENT_TYPE);
			connection.connect();

			responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream iStream = connection.getInputStream();
				Reader reader = new InputStreamReader(iStream);

				// Print Individual keys
				int contentLength = (int) connection.getContentLengthLong();
				char[] array = new char[contentLength];
				reader.read(array);
				String responseData = new String(array);
				jsonResponse = new JSONObject(responseData);
				int id = jsonResponse.getInt("id");
				String title = jsonResponse.getString("title");
				System.out.println("Id: " + id);
				System.out.println("Title: " + title);

			} else {
				System.out.println("Error: " + connection.getResponseMessage());
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
	}
}
