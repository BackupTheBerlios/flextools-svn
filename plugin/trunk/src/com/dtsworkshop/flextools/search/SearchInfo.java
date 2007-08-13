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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.BuildStateDocument;

/**
 * Defines the search information/parameters.
 * 
 * 
 * @author Oliver Tupman
 *
 */
public class SearchInfo {

	/**
	 * @param searcher
	 */
	SearchInfo(ClassSearcher searcher) {
		this.searcher = searcher;
	}

	public ClassSearcher searcher;
	public BuildStateDocument document;
	public IProject project;
	public IFile containingFile;
	public boolean isCaseSensitive;
	public boolean isSearchExact = false;
	
	private List<Integer> uniques = new ArrayList<Integer>(100);
	
	
	/**
	 * Convenience method to create a reference from the supplied build
	 * state match.
	 * 
	 * @param sourceRef The build match/reference that should be given to the user
	 * @return The constructed search reference.
	 */
	public SearchReference createReference(BuildReference sourceRef) {
		return searcher.referenceFromNodeType(project, containingFile, sourceRef);
	}
	
	private boolean isPositionInRef(BuildReference ref, int position) {
		return position >= ref.getStartPos() && position <= ref.getEndPos(); 
	}
	
	private boolean isContainedInOtherMatch(BuildReference searchRef) {
		for(BuildReference currentRef : searcher.storedMatches) {
			if(isPositionInRef(currentRef, searchRef.getStartPos())
			   || isPositionInRef(currentRef, searchRef.getEndPos())) {
				return true;
			}
		}
		return false;
	}
	
	
	
	public SearchReference addReference(BuildReference sourceRef, String description) {
		SearchReference newRef = createReference(sourceRef);
		newRef.setDescription(description);
		//matches.add(newRef);
		int code = String.format("%d - %d", sourceRef.getStartPos(), sourceRef.getEndPos()).hashCode();
		if(uniques.contains(code)) {
			return newRef;
		}
		
		boolean isContainedMatch = isContainedInOtherMatch(sourceRef);
		if(!isContainedMatch) {
			searcher.matches.add(newRef);
			uniques.add(code);
			searcher.storedMatches.add(sourceRef);
		}
		
		return newRef;
	}
	
	/**
	 * Determine whether the text provided is a match against the search text.
	 * The match will occur exact or in-exact depending on the search options
	 * provided by the client code.
	 * 
	 * 
	 * @param textToMatch The text to compare with the search text
	 * @return True - is a match; false otherwise.
	 */
	public boolean isMatch(String textToMatch) {
		if(searcher.exactMatch) {
			return isExactMatch(textToMatch);
		}
		else {
			return containsText(textToMatch);
		}
	}

	/**
	 * Determines whether the text provided contains the text to search for.
	 * Case sensitivity is determined by the options provided by the client
	 * code.
	 * 
	 * @param textToMatch The text to look for a match
	 * @return True - the text is contained in the parameter; false otherwise
	 */
	public boolean containsText(String textToMatch) {
		if(textToMatch == null) {
			return false;
		}
		if(isCaseSensitive) {
			return textToMatch.contains(searcher.searchText);
		}
		else {
			boolean result = textToMatch.toLowerCase().contains(searcher.searchText.toLowerCase());
			return result;
		}
	}

	/**
	 * Determines whether the text provided is an exact match with the search
	 * text.
	 * 
	 * @param textToMatch The text to try and match with.
	 * @return True - the text matches the search text; false otherwise
	 */
	public boolean isExactMatch(String textToMatch) {
		if(textToMatch == null) {
			return false;
		}
		if(isCaseSensitive) {
			return searcher.searchText.equals(textToMatch);
		}
		else {
			return searcher.searchText.compareToIgnoreCase(textToMatch) == 0;
		}
	}
}