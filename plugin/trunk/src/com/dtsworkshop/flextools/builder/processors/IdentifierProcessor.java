package com.dtsworkshop.flextools.builder.processors;

import java.util.ArrayList;

import antlr.Token;

import com.adobe.flexbuilder.codemodel.definitions.ASDefinitionCache;
import com.adobe.flexbuilder.codemodel.definitions.ASDefinitionFilter;
import com.adobe.flexbuilder.codemodel.definitions.IASScope;
import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.internal.testing.IAdaptableNode;
import com.adobe.flexbuilder.codemodel.internal.tree.IdentifierNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.adobe.flexbuilder.codemodel.tree.IASNode;
import com.adobe.flexbuilder.codemodel.tree.IScopedNode;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.NodeType;

public class IdentifierProcessor extends DefaultNodeProcessor {

	public IdentifierProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public NodeType getNode(NodeBase node, NodeType parentType, BuildStateType buildState) {
		// TODO Auto-generated method stub
		IdentifierNode identNode = (IdentifierNode)node;
		
		NodeType identifierType = super.getNode(node, parentType, buildState);
		String qualifiedName = ProcessorHelper.getQualifiedName(identNode);
		
		String content = "";
		
		content = String.format(
			"qualified name: %s, package name: %s",
			qualifiedName, identNode.getPackageName()
		);
		
		identifierType.setContents(content);
		return identifierType;
	}

}
