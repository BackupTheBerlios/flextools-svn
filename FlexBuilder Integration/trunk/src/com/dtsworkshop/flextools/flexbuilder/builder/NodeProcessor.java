/**
 * 
 */
package com.dtsworkshop.flextools.flexbuilder.builder;

import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.BuildStateType;

public abstract class NodeProcessor {
	public Class modelNode;
	public Class xmlNode;
	
	public abstract BuildReference getNode(NodeBase node, BuildReference parentType, BuildStateType buildState);
	
	public NodeProcessor(Class modelNode, Class xmlNode) {
		this.modelNode = modelNode;
		this.xmlNode = xmlNode;
	}
	
}