/**
*	Copyright (C) Oliver B. Tupman, 2007.
*	
*	This file is part of the Flex Tools Project.
*	
*	The Flex Tools Project is free software; you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation; either version 3 of the License, or
*	(at your option) any later version.
*	
*	The Flex Tools Project is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*	GNU General Public License for more details.
*	
*	You should have received a copy of the GNU General Public License
*	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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
