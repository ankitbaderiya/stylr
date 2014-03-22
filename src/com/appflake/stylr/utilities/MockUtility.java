package com.appflake.stylr.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A utility class to send an HTTP POST request and handle a JSON response
 * 
 * @author Ankit Baderiya
 *
 */
public class MockUtility {

	private static final String CHARSET = "UTF-8";
	private static final String CONTENT_TYPE = "application/json";
	private static final String REQUEST_TYPE = "POST";

	private static int responseCode = -1;
	private static JSONObject jsonResponse = null;
	private static HttpURLConnection connection = null;

	private MockUtility() {
	}

	public static void processRequest(String spec, String param, String value) {
		try {
			setUpConnection(spec);

			JSONObject jsonObject = new JSONObject();
			jsonObject.put(param, value);

			postSend(jsonObject);

			responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream iStream = connection.getInputStream();
				Reader reader = new InputStreamReader(iStream);

				// Print String response
				// processResponse(reader);

				// Print Individual keys
				processUserKeyResponses(reader);

			} else {
				System.out.println("Error: " + connection.getResponseMessage());
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	public static void processRequest(String spec, String param, int value) {
		try {
			setUpConnection(spec);

			JSONObject jsonObject = new JSONObject();
			jsonObject.put(param, value);

			postSend(jsonObject);

			responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream iStream = connection.getInputStream();
				Reader reader = new InputStreamReader(iStream);

				// Print String response
				// processResponse(reader);

				// Print Individual keys
				processClothingKeyResponses(reader);

			} else {
				System.out.println("Error: " + connection.getResponseMessage());
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	private static void closeConnection() {
		connection.disconnect();
	}

	private static void processUserKeyResponses(Reader reader) throws IOException, JSONException {
		int contentLength = (int) connection.getContentLengthLong();
		char[] array = new char[contentLength];
		reader.read(array);
		String responseData = new String(array);
		jsonResponse = new JSONObject(responseData);
		String uid = jsonResponse.getString("uid");
		String name = jsonResponse.getString("name");
		String email = jsonResponse.getString("email");
		String provider = jsonResponse.getString("provider");
		int first_login = jsonResponse.getInt("first_login");
		System.out.println("Uid: " + uid);
		System.out.println("Name: " + name);
		System.out.println("Email: " + email);
		System.out.println("Provider: " + provider);
		System.out.println("First_login: " + first_login);
	}

	private static void processClothingKeyResponses(Reader reader) throws IOException, JSONException {
		int contentLength = (int) connection.getContentLengthLong();
		char[] array = new char[contentLength];
		reader.read(array);
		String responseData = new String(array);
		jsonResponse = new JSONObject(responseData);

		String name = jsonResponse.getString("name");
		String image_url = jsonResponse.getString("image_url");
		String artist_url = jsonResponse.getString("artist_url");
		String artist = jsonResponse.getString("artist");
		JSONArray tags = jsonResponse.getJSONArray("tags");
		int price = jsonResponse.getInt("price");
		int comment_counter = jsonResponse.getInt("comment_counter");
		int heart_counter = jsonResponse.getInt("heart_counter");
		int hotness_counter = jsonResponse.getInt("hotness_counter");

		System.out.println("name: " + name);
		System.out.println("image_url: " + image_url);
		System.out.println("artist_url: " + artist_url);
		System.out.println("artist: " + artist);
		System.out.println("tags: ");
		for (int i = 0; i < tags.length(); i++) {
			System.out.println(tags.getString(i) + ",");
		}
		System.out.println("price: " + price);
		System.out.println("comment_counter: " + comment_counter);
		System.out.println("heart_counter: " + heart_counter);
		System.out.println("hotness_counter: " + hotness_counter);
	}

	@SuppressWarnings("unused")
	private static void processResponse(Reader reader) throws IOException {
		BufferedReader in = new BufferedReader(reader);
		String line = "";
		StringBuffer response = new StringBuffer();
		while ((line = in.readLine()) != null) {
			response.append(line);
		}
		System.out.println(response.toString());
	}

	private static void postSend(JSONObject jsonObject) throws IOException, UnsupportedEncodingException {
		Writer writer = null;
		try {
			OutputStream oStream = connection.getOutputStream();
			writer = new OutputStreamWriter(oStream);
			writer.write(URLEncoder.encode(jsonObject.toString(), CHARSET));
			writer.flush();
		} finally {
			writer.close();
		}
	}

	private static void setUpConnection(String spec) throws MalformedURLException, IOException, ProtocolException {
		URL url = new URL(spec);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(REQUEST_TYPE);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", CONTENT_TYPE);
		connection.connect();
	}
}
