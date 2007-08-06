package com.dtsworkshop.flextools.builder.processors;

import com.adobe.flexbuilder.codemodel.definitions.IPackage;
import com.adobe.flexbuilder.codemodel.internal.tree.MemberAccessExpressionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.NodeType;

public class MemberAccessExpressionNodeProcessor extends DefaultNodeProcessor {

	public MemberAccessExpressionNodeProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public NodeType getNode(NodeBase node, NodeType parentType, BuildStateType buildState) {
		// TODO Auto-generated method stub
		NodeType accessNode = super.getNode(node, parentType, buildState);
		
		MemberAccessExpressionNode docNode = (MemberAccessExpressionNode)node;
		IPackage fred;

		String memberName = docNode.getNameString();
		
		return accessNode;
	}

}
