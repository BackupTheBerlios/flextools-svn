/**
 * 
 */
package com.dtsworkshop.flextools.builder;

import com.adobe.flexbuilder.codemodel.internal.tree.ClassNode;
import com.adobe.flexbuilder.codemodel.internal.tree.FunctionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.builder.processors.DefaultNodeProcessor;
import com.dtsworkshop.flextools.model.ClassStateType;
import com.dtsworkshop.flextools.model.FunctionNodeType;
import com.dtsworkshop.flextools.model.NodeType;

public class ClassProcessor extends DefaultNodeProcessor {

	public ClassProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public NodeType getNode(NodeBase node, NodeType parentType) {
		// TODO Auto-generated method stub
		ClassStateType type = (ClassStateType) super.getNode(node, parentType);
		ClassNode classNode = (ClassNode)node;
		type.setBaseClassName(classNode.getBaseClassName());
		type.setName(classNode.getName());
		type.setQualifiedName(classNode.getQualifiedName());
		FunctionNodeType functionNode = type.addNewConstructor();
		//type.setConstructor(classNode.getConstructorNode().getQualifiedName());
		FunctionNode constructorNode = classNode.getConstructorNode(); 
		functionNode.setStartPos(constructorNode.getStart());
		functionNode.setEndPos(constructorNode.getEnd());
		
		return type;
	}
	
}