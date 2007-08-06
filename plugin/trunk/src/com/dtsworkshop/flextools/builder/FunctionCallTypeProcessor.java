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
package com.dtsworkshop.flextools.builder;

import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.definitions.IFunction;
import com.adobe.flexbuilder.codemodel.internal.tree.ExpressionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.FunctionCallNode;
import com.adobe.flexbuilder.codemodel.internal.tree.KeywordNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.builder.processors.DefaultNodeProcessor;
import com.dtsworkshop.flextools.builder.processors.ProcessorHelper;
import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.FunctionCallType;


public class FunctionCallTypeProcessor extends DefaultNodeProcessor {

	public FunctionCallTypeProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BuildReference getNode(NodeBase node, BuildReference parentType, BuildStateType buildState) {
		// TODO Auto-generated method stub
		FunctionCallType type = (FunctionCallType)super.getNode(node, parentType, buildState);
		FunctionCallNode callNode = (FunctionCallNode)node;
		KeywordNode newKeyword = callNode.getNewKeywordNode();
		
		type.setIsConstructorCall(newKeyword != null);
		//IFunction functionDef = (IFunction)callNode.getNameNode();
		
		type.setName(ProcessorHelper.getQualifiedName(callNode.getNameNode()));
		IDefinition def = callNode.getDefinition();
		
		System.out.println(String.format("Def for function call %s", type.getName()));
		if(def != null) {
			System.out.println(def.getClass().getCanonicalName());
		}
		else {
			
		}
			
		return type;
	}

}
