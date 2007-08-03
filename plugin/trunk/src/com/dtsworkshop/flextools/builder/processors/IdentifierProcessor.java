package com.dtsworkshop.flextools.builder.processors;

import java.util.ArrayList;

import antlr.Token;

import com.adobe.flexbuilder.codemodel.definitions.ASDefinitionCache;
import com.adobe.flexbuilder.codemodel.definitions.ASDefinitionFilter;
import com.adobe.flexbuilder.codemodel.definitions.IASScope;
import com.adobe.flexbuilder.codemodel.internal.testing.IAdaptableNode;
import com.adobe.flexbuilder.codemodel.internal.tree.IdentifierNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.adobe.flexbuilder.codemodel.tree.IASNode;
import com.adobe.flexbuilder.codemodel.tree.IScopedNode;
import com.dtsworkshop.flextools.model.NodeType;

public class IdentifierProcessor extends DefaultNodeProcessor {

	public IdentifierProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public NodeType getNode(NodeBase node, NodeType parentType) {
		// TODO Auto-generated method stub
		IdentifierNode identNode = (IdentifierNode)node;
		
		NodeType identifierType = super.getNode(node, parentType);
		String qualifiedName = ProcessorHelper.getQualifiedName(identNode);
		identifierType.setContents(qualifiedName);
		return identifierType;
	}

}
