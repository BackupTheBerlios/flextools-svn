package com.dtsworkshop.flextools.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceHelper {

	public static String streamToString(InputStream fileInputStream) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
		
		String input = "";
		while((input = reader.readLine()) != null) {
			builder.append(input);
			builder.append("\n\r");
		}
		reader.close();
		return builder.toString();
	}

}
