package com.dtsworkshop.flextools.builder.processors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.NodeType;

public class DefaultNodeProcessor extends NodeProcessor {

	public DefaultNodeProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
	}

	@Override
	public BuildReference getNode(NodeBase node, BuildReference parentType, BuildStateType buildState) {
		String nodeName = node.getNodeType();
		
		nodeName = nodeName.substring(0, 1).toUpperCase() + nodeName.substring(1, nodeName.length());
		String methodName = "addNew" + nodeName;
		BuildReference newInstance = createNode(parentType, methodName);
		newInstance.setStartPos(node.getStart());
		newInstance.setEndPos(node.getEnd());
		return newInstance;
	}

	private BuildReference createNode(BuildReference parentType, String methodName){
		BuildReference newInstance = null;
		
		try {
			Method addMethod = parentType.getClass().getMethod(methodName);
			newInstance = (BuildReference)addMethod.invoke(parentType, new Object[0]);
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
