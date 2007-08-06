package com.dtsworkshop.flextools.builder.processors;

import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.internal.tree.FullNameNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.NodeType;

public class FullNameNodeProcessor extends DefaultNodeProcessor {

	public FullNameNodeProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public NodeType getNode(NodeBase node, NodeType parentType, BuildStateType buildState) {
		// TODO Auto-generated method stub
		NodeType nameNode = super.getNode(node, parentType, buildState);
		FullNameNode name = (FullNameNode)node;
		IDefinition [] typeDefs = name.getDefinitions();
		String nameString = name.getNameString();
		
		return nameNode;
	}

}
