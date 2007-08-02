package com.dtsworkshop.flextools.builder;

import com.adobe.flexbuilder.codemodel.internal.tree.ExpressionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.FunctionCallNode;
import com.adobe.flexbuilder.codemodel.internal.tree.KeywordNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.builder.processors.DefaultNodeProcessor;
import com.dtsworkshop.flextools.model.FunctionCallType;
import com.dtsworkshop.flextools.model.NodeType;


public class FunctionCallTypeProcessor extends DefaultNodeProcessor {

	public FunctionCallTypeProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public NodeType getNode(NodeBase node, NodeType parentType) {
		// TODO Auto-generated method stub
		FunctionCallType type = (FunctionCallType)super.getNode(node, parentType);
		FunctionCallNode callNode = (FunctionCallNode)node;
		KeywordNode newKeyword = callNode.getNewKeywordNode();
		
		type.setIsConstructorCall(newKeyword != null);
		type.setName((callNode.getNameNode().toString()));
		return type;
	}

}
