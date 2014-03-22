package com.appflake.stylr;

import com.appflake.stylr.utilities.MockUtility;

/**
 * Main launcher for the Mock API
 * 
 * @author Ankit Baderiya
 *
 */
public class Launcher {

	private static final String SPEC_USER = "http://stylr.apiary-mock.com/user.json";
	private static final String SPEC_CLOTHING = "http://stylr.apiary-mock.com/clothing/recommendations.json";
	private static final String PARAM1 = "email";
	private static final String PARAM2 = "user_id";
	private static final String VAL1 = "silent.dream@gmail.com";
	private static final int VAL2 = 100;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		MockUtility.processRequest(SPEC_USER, PARAM1, VAL1);
		MockUtility.processRequest(SPEC_CLOTHING, PARAM2, VAL2);
	}
}
