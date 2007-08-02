package com.dtsworkshop.flextools.builder.processors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.model.NodeType;

public class DefaultNodeProcessor extends NodeProcessor {

	public DefaultNodeProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
	}

	@Override
	public NodeType getNode(NodeBase node, NodeType parentType) {
		String nodeName = node.getNodeType();
		nodeName = nodeName.substring(0, 1).toUpperCase() + nodeName.substring(1, nodeName.length());
		String methodName = "addNew" + nodeName;
		NodeType newInstance = createNode(parentType, methodName);
		newInstance.setStartPos(node.getStart());
		newInstance.setEndPos(node.getEnd());
		return newInstance;
	}

	private NodeType createNode(NodeType parentType, String methodName){
		NodeType newInstance = null;
		
		try {
			Method addMethod = parentType.getClass().getMethod(methodName);
			newInstance = (NodeType)addMethod.invoke(parentType, new Object[0]);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newInstance;
	}

}
