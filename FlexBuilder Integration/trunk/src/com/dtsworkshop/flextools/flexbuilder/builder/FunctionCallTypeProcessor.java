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
import com.adobe.flexbuilder.codemodel.definitions.IFunction;
import com.adobe.flexbuilder.codemodel.internal.tree.ClassNode;
import com.adobe.flexbuilder.codemodel.internal.tree.ExpressionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.FunctionCallNode;
import com.adobe.flexbuilder.codemodel.internal.tree.KeywordNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
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
		boolean isConstructorCall = callNode.getNewKeywordNode() != null;
		type.setIsConstructorCall(isConstructorCall);
		//IFunction functionDef = (IFunction)callNode.getNameNode();
		
		type.setName(FbCodeModelHelper.getQualifiedName(callNode.getNameNode()));
		IDefinition def = callNode.getDefinition();
		
		if(def != null) {
			//System.out.println(def.getClass().getCanonicalName());
		}
		else {
			//System.out.println("No def");
		}
		CallType typeOfCall = CallType.Normal;
		
		if(def instanceof ClassNode) {
			
			boolean isMethodSameAsClassName = def.getName().equals(ProcessorHelper.getLocalName(type.getName()));
			if(isConstructorCall) {
				typeOfCall = CallType.Constructor;
				type.setType(FunctionCallType.Type.CONSTRUCTOR);
			}
			else if(isMethodSameAsClassName && !isConstructorCall) {
				typeOfCall = CallType.Cast;
				type.setType(FunctionCallType.Type.CAST);
			}
			else {
				typeOfCall = CallType.Static;
				type.setType(FunctionCallType.Type.STATIC);
			}
		}
		else {
			type.setType(FunctionCallType.Type.NORMAL);
		}
		
		//type.setType(FunctionCallType.Type.CAST)
			
		return type;
	}
	
	private enum CallType {
			Static (1)
			,Constructor(2) 
			,Normal(4)
			,Cast(3);
			
		private final int callValue;
		private CallType(int value) {
			this.callValue = value;
		}
		public int getCallValue() {
			return this.callValue;
		}
		
	}

}
