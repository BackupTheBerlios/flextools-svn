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


import com.adobe.flexbuilder.codemodel.definitions.IVariable;
import com.adobe.flexbuilder.codemodel.internal.tree.ExpressionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.FullNameNode;
import com.adobe.flexbuilder.codemodel.internal.tree.IdentifierNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.adobe.flexbuilder.codemodel.internal.tree.VariableNode;
import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.NodeType;

public class VariableProcessor extends DefaultNodeProcessor {

	@SuppressWarnings("restriction")
	@Override
	public BuildReference getNode(NodeBase node, BuildReference parentType, BuildStateType buildState) {
		// TODO Auto-generated method stub
		NodeType type= (NodeType)super.getNode(node, parentType, buildState);
		IVariable variable = (IVariable)node;
//		String content = type.getContents();
//		content += String.format(
//			" and variable type '%s'",
//			variable.getVariableType()
//		);
//		type.setContents(content);
//		type.addNewStopNode().setContents(content);
		IVariable var = (IVariable)node;
//		System.out.println(String.format(
//			"Variable name %s, type: %s, classification: %s",
//			var.getName(), var.getVariableType(), var.getVariableClassification().toString()
//		));
//		System.out.println(String.format(
//			"Variable package name: %s, qualified name: %s, is implicit? %s",
//			var.getPackageName(), var.getQualifiedName(),
//			(var.isImplicit()) ? "yes" : "no"
//		));
		VariableNode varNode = (VariableNode)var;
		ExpressionNode typeNode = (ExpressionNode)varNode.getTypeNode();
		if(typeNode instanceof IdentifierNode) {
			IdentifierNode identNode = (IdentifierNode)typeNode;
			String identText = identNode.getText();
			String nameString = identNode.getNameString();
//			System.out.println(String.format(
//					"Variable has ident node of %s %s"
//				, identText, nameString
//			));
		}
		return type;
	}

	public VariableProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
		// TODO Auto-generated constructor stub
	}

}
