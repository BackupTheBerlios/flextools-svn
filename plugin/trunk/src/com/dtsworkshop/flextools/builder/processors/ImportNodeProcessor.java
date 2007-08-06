package com.dtsworkshop.flextools.builder.processors;

import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.internal.tree.ClassNode;
import com.adobe.flexbuilder.codemodel.internal.tree.ExpressionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.ImportNode;
import com.adobe.flexbuilder.codemodel.internal.tree.InterfaceNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.ImportNodeType;

public class ImportNodeProcessor extends DefaultNodeProcessor {

	public ImportNodeProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
	}

	@Override
	public BuildReference getNode(NodeBase node, BuildReference parentType, BuildStateType buildState) {
		ImportNodeType createdNode = (ImportNodeType)super.getNode(node, parentType, buildState);
		
		ImportNode importNode = (ImportNode)node;
		String packageName = importNode.getPackageName();
		ExpressionNode targetPNode = importNode.getTargetPackageNode();
		IDefinition [] defs = targetPNode.getDefinitions();
		//TODO: Using the definition obtained above we can find the target class and get all other info from that
		//TODO: Establish caching for above, too
		//NOTE: No definitions if it's an SWC-imported class
		//TODO: Add import node stuff to build state
		System.out.println(String.format(
			"Got imported package '%s'", packageName
		));
		if(defs.length == 1) {
			if(defs[0] instanceof ClassNode) {
			ClassNode importedClass = (ClassNode)defs[0];
				String qualifiedName = importedClass.getQualifiedName();
				ProcessorHelper.addImport(buildState, qualifiedName);
				createdNode.setLocalName(ProcessorHelper.getLocalName(qualifiedName));
				createdNode.setQualifiedName(qualifiedName);
			}
			else if(defs[0] instanceof InterfaceNode) {
				InterfaceNode intNode = (InterfaceNode)defs[0];
				ProcessorHelper.addImport(buildState, intNode.getQualifiedName());
				createdNode.setLocalName("unknown - need to implement interface handling!");
			}
		}
		return createdNode;
	}

}
