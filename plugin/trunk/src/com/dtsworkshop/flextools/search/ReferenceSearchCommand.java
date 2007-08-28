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
import org.eclipse.core.runtime.Assert;

import com.dtsworkshop.flextools.model.IdentifierNodeType;

/**
 * 
 * 
 * @author otupman
 *
 */
public class ReferenceSearchCommand extends AbstractSearchCommand implements ISearchCommand {

	public String getSearchText(SearchInfo info) {
		return String.format(
			"%s " +
			"$this//mod:IdentifierNode | $this//mod:ClassReference",
			getSearcher().getNamespaceDecl()
		);
	}

	public void processResult(SearchInfo info, XmlObject result) {
		Assert.isTrue(result instanceof IdentifierNodeType);
		IdentifierNodeType node = (IdentifierNodeType)result;
		String comparisonText = (info.isSearchExact) ? node.getQualifiedName() : node.getName();
		if(info.isMatch(comparisonText)) {
			info.addReference(
					node, 
					"Identifier reference " + comparisonText
			);
		}
	}
	
}