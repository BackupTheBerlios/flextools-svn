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
package com.dtsworkshop.flextools.flexbuilder.builder;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.definitions.IClass;
import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.definitions.IMetaAttributes;
import com.adobe.flexbuilder.codemodel.internal.tree.ExpressionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.IdentifierNode;
import com.adobe.flexbuilder.codemodel.internal.tree.MemberedNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.TypeReference;

public class ProcessorHelper {

	public static String getQualifiedName(ExpressionNode identNode) {
		IDefinition def = identNode.getDefinition();
		String qualifiedName = "";
		if(def != null) {
			qualifiedName = def.getQualifiedName();
			
		}
		return qualifiedName;
	}

	public static void addImport(BuildStateType buildState, String qualifiedName) {
		TypeReference newImport = buildState.addNewImportReference();
		newImport.setQualifiedName(qualifiedName);
		String localName = getLocalName(qualifiedName);
		newImport.setShortName(localName);
	}

	public static String getLocalName(String qualifiedName) {
		String localName = "";
		int lastDot = qualifiedName.lastIndexOf(".");
		if(lastDot != -1) {
			localName = qualifiedName.substring(lastDot + 1);
		}
		else {
			return qualifiedName;
		}
		return localName;
	}
	
//	public static String getReferenceClassName(MemberedNode clazzNode) {
//		clazzNode.get
//	}

}
