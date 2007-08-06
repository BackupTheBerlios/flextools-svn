/**
 * 
 */
package com.dtsworkshop.flextools.builder.processors;

import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.NodeType;

public abstract class NodeProcessor {
	public Class modelNode;
	public Class xmlNode;
	
	public abstract NodeType getNode(NodeBase node, NodeType parentType, BuildStateType buildState);
	
	public NodeProcessor(Class modelNode, Class xmlNode) {
		this.modelNode = modelNode;
		this.xmlNode = xmlNode;
	}
	
}