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

import com.dtsworkshop.flextools.model.ClassStateType;


public class ClassDelcarationSearchCommand extends AbstractSearchCommand implements ISearchCommand {

	public String getSearchText(SearchInfo info) {
		// TODO Auto-generated method stub
		return String.format(
				"%s" +
				"$this//mod:ClassNode",
				getSearcher().getNamespaceDecl()
		);
	}

	public void processResult(SearchInfo info, XmlObject result) {
		if(!(result instanceof ClassStateType)) {
			return;
		}
		ClassStateType type = (ClassStateType)result;
		Object test = type.getDomNode().getOwnerDocument();
		if(info.isExactMatch(type.getName())) {
			SearchReference newRef = info.createReference(type);
			newRef.setDescription("Class delcaration " + type.getName());
//			getSearcher().getMatches().add(newRef);
		}
	}
	
}