/**
*	Copyright (C) Oliver B. Tupman, 2007.
*	
*	This file is part of the Flex Tools Project.
*	
*	The Flex Tools Project is free software; you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation; either version 3 of the License, or
*	(at your option) any later version.
*	
*	The Flex Tools Project is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*	GNU General Public License for more details.
*	
*	You should have received a copy of the GNU General Public License
*	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.dtsworkshop.flextools.flexbuilder.builder;

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
