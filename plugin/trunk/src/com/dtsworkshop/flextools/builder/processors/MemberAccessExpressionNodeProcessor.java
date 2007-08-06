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
package com.dtsworkshop.flextools.builder.processors;

import com.adobe.flexbuilder.codemodel.definitions.IPackage;
import com.adobe.flexbuilder.codemodel.internal.tree.MemberAccessExpressionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.NodeType;

public class MemberAccessExpressionNodeProcessor extends DefaultNodeProcessor {

	public MemberAccessExpressionNodeProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BuildReference getNode(NodeBase node, BuildReference parentType, BuildStateType buildState) {
		// TODO Auto-generated method stub
		NodeType accessNode = (NodeType)super.getNode(node, parentType, buildState);
		
		MemberAccessExpressionNode docNode = (MemberAccessExpressionNode)node;
		IPackage fred;

		String memberName = docNode.getNameString();
		
		return accessNode;
	}

}
