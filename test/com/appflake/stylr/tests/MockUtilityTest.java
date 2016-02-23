package com.appflake.stylr.tests;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MockUtilityTest {

	private static final String SPEC_USER = "http://stylr.apiary-mock.com/user.json";
	private static final String PARAM = "email";
	private static final String VAL = "ankitbaderiya@gmail.com";
	private static final String CHARSET = "UTF-8";
	private static final String CONTENT_TYPE = "application/json";
	private static final String REQUEST_TYPE = "POST";

	private static int responseCode = -1;
	private static JSONObject jsonResponse = null;
	private static HttpURLConnection connection = null;
	private URL url = null;
	private OutputStream oStream = null;
	private Writer writer = null;
	private JSONObject jsonObject = null;

	@Before
	public void setUp() throws Exception {
		url = new URL(SPEC_USER);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(REQUEST_TYPE);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", CONTENT_TYPE);
		connection.connect();
	}

	@After
	public void tearDown() throws Exception {
		writer.close();
		connection.disconnect();
	}

	@Test
	public void testProcessRequestStringStringString() throws JSONException,
			IOException {
		jsonObject = new JSONObject();
		jsonObject.put(PARAM, VAL);

		oStream = connection.getOutputStream();
		writer = new OutputStreamWriter(oStream);
		writer.write(URLEncoder.encode(jsonObject.toString(), CHARSET));
		writer.flush();

		responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			InputStream iStream = connection.getInputStream();
			Reader reader = new InputStreamReader(iStream);

			int contentLength = (int) connection.getContentLengthLong();
			char[] array = new char[contentLength];
			reader.read(array);
			String responseData = new String(array);
			jsonResponse = new JSONObject(responseData);

			String name = jsonResponse.getString("name");

			Assert.assertNotNull(name);
			Assert.assertEquals(name, "Ankit Baderiya");
		}
	}
}
