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
package com.dtsworkshop.flextools.search;

import org.apache.xmlbeans.XmlObject;

/**
 * Defines a command that can take part in the search. Essentially
 * an implementor should construct the appropriate xpath query
 * and will then be called with the results.
 * 
 * @author Oliver Tupman
 *
 */
public interface ISearchCommand {
	/** 
	 * Gets the XPath command to execute upon the build state.
	 * 
	 * @param info The SearchInfo that defines the general parameters
	 * @return The constructed xpath
	 */
	String getSearchText(SearchInfo info);
	/**
	 * Process a result from the xpath query
	 * 
	 * @param info The search information/parameters
	 * @param result The result found
	 */
	void processResult(SearchInfo info, XmlObject result);
	
	ClassSearcher getSearcher();

	void setSearcher(ClassSearcher searcher);
	
}