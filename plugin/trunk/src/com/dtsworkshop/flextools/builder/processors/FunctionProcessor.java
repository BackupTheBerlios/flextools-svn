package com.dtsworkshop.flextools.builder.processors;

import com.adobe.flexbuilder.codemodel.definitions.IFunction;
import com.adobe.flexbuilder.codemodel.definitions.IVariable;
import com.adobe.flexbuilder.codemodel.definitions.IFunction.FunctionClassification;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.FunctionNodeType;
import com.dtsworkshop.flextools.model.NodeType;
import com.dtsworkshop.flextools.model.SimpleVariableType;

public class FunctionProcessor extends DefaultNodeProcessor {

	public FunctionProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public NodeType getNode(NodeBase node, NodeType parentType, BuildStateType buildState) {
		// TODO Auto-generated method stub
		IFunction functionNode = (IFunction)node;
		IVariable [] args =functionNode.getArguments();
		String name = functionNode.getName();
		String returnType = functionNode.getReturnType();
		FunctionClassification classification = functionNode.getFunctionClassification();
		FunctionNodeType type = (FunctionNodeType)super.getNode(node, parentType, buildState); 
		
		type.setShortName(name);
		type.setClassificationTypeName(classification.toString());
		type.setReturnType(returnType);
		
		for(IVariable arg : args) {
			SimpleVariableType var = type.addNewArgument();
			var.setClassification(arg.getVariableClassification().toString());
			var.setName(arg.getName());
			var.setTypeName(arg.getVariableType());
		}
		
		return type;
	}
	
}
