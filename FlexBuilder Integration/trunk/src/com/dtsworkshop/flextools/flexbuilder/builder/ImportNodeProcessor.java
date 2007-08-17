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

import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.internal.tree.ClassNode;
import com.adobe.flexbuilder.codemodel.internal.tree.ExpressionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.ImportNode;
import com.adobe.flexbuilder.codemodel.internal.tree.InterfaceNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.ImportNodeType;
import com.dtsworkshop.flextools.builder.processors.ProcessorHelper;

public class ImportNodeProcessor extends DefaultNodeProcessor {

	public ImportNodeProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
	}

	@Override
	public BuildReference getNode(NodeBase node, BuildReference parentType, BuildStateType buildState) {
		ImportNodeType createdNode = (ImportNodeType)super.getNode(node, parentType, buildState);
		
		ImportNode importNode = (ImportNode)node;
		String packageName = importNode.getPackageName();
		ExpressionNode targetPNode = importNode.getTargetPackageNode();
		IDefinition [] defs = targetPNode.getDefinitions();
		//TODO: Using the definition obtained above we can find the target class and get all other info from that
		//TODO: Establish caching for above, too
		//NOTE: No definitions if it's an SWC-imported class
		//TODO: Add import node stuff to build state
//		System.out.println(String.format(
//			"Got imported package '%s'", packageName
//		));
		node.getScopeNode();
		if(defs.length == 1) {
			if(defs[0] instanceof ClassNode) {
				ClassNode importedClass = (ClassNode)defs[0];
				importedClass.getScopeNode();
				String qualifiedName = importedClass.getQualifiedName();
				ProcessorHelper.addImport(buildState, qualifiedName);
				createdNode.setLocalName(ProcessorHelper.getLocalName(qualifiedName));
				createdNode.setQualifiedName(qualifiedName);
			}
			else if(defs[0] instanceof InterfaceNode) {
				InterfaceNode intNode = (InterfaceNode)defs[0];
				intNode.getScopeNode();
				ProcessorHelper.addImport(buildState, intNode.getQualifiedName());
				createdNode.setLocalName("unknown - need to implement interface handling!");
			}
		}
		return createdNode;
	}

}
