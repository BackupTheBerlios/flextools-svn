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
package com.dtsworkshop.flextools.builder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlOptions;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import sun.reflect.generics.tree.ClassTypeSignature;

import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.internal.tree.*;
import com.adobe.flexbuilder.codemodel.tree.IASNode;
import com.adobe.flexbuilder.codemodel.tree.IFileNode;
import com.dtsworkshop.flextools.builder.processors.*;
import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.BuildStateDocument;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.ClassStateType;
import com.dtsworkshop.flextools.model.FunctionCallType;
import com.dtsworkshop.flextools.model.FunctionNodeType;
import com.dtsworkshop.flextools.model.IdentifierNodeType;
import com.dtsworkshop.flextools.model.ImportNodeType;
import com.dtsworkshop.flextools.model.NodeType;
import com.dtsworkshop.flextools.utils.ResourceHelper;

public class ModelProcessor {

	private String getFileContents(IFile file) {
		
		try {
			InputStream fileInputStream = file.getContents();

			return ResourceHelper.streamToString(fileInputStream);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public String dumpNodes(IFileNode startNode, IFile sourceFile) {
		//NodeProcessor processor = getProcessor(startNode.class);
		XmlOptions options = new XmlOptions();
		options.setSavePrettyPrint();
		options.setSavePrettyPrintIndent(2);
		BuildStateDocument doc = BuildStateDocument.Factory.newInstance(options);		
		
		buildStateDocument(startNode, sourceFile, doc);	
		return doc.xmlText(options);
	}

	public BuildStateDocument getStateDocument(IFileNode startNode, IFile sourceFile) {
		XmlOptions options = new XmlOptions();
		options.setSavePrettyPrint();
		options.setSavePrettyPrintIndent(2);
		BuildStateDocument doc = BuildStateDocument.Factory.newInstance(options);
		buildStateDocument(startNode, sourceFile, doc);
		return doc;
	}
	
	private void buildStateDocument(IFileNode startNode, IFile sourceFile, BuildStateDocument doc) {
		BuildStateType buildType = doc.addNewBuildState();
		buildType.setFile(sourceFile.getProjectRelativePath().toString());
		buildType.setProject(sourceFile.getProject().getName());
		NodeType fileNode = buildType.addNewFileNode();
		String fileData = getFileContents(sourceFile);
		processNode(startNode, fileNode, fileData, buildType);
	}
	
	private String getNodeContents(String data, IASNode node) {
		String contents = "";
		int start = node.getStart();
		int end = node.getEnd();
		
		if(start == -1 || end == -1) {
			return "";
		}
		else if(start > end) {
			int tempEnd = end;
			end = start;
			start = tempEnd;
		}
		try {
			contents = data.substring(start, end);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return contents;
	}
	
	private void processNode(IASNode node, BuildReference parentXmlNode, String textData, BuildStateType buildState) {
		IASNode [] children = node.getChildren();
		for(IASNode child : children) {
			NodeProcessor processor = getProcessor(child.getClass());
			BuildReference newXmlChild = processor.getNode((NodeBase)child, parentXmlNode, buildState);
			//newXmlChild.setContents(getNodeContents(textData, child));
			if(child.getChildCount() > 0) {
				processNode(child, newXmlChild, textData, buildState);
				
			}
		}
	}
	
	private NodeProcessor getProcessor(Class nodeClass) {
		if(processorMap.containsKey(nodeClass)) {
			return processorMap.get(nodeClass);
		}
		return defaultProcessor;
	}
	
	private static List<NodeProcessor> processors;
	private static Map<Class, NodeProcessor> processorMap;
	
	private static NodeProcessor defaultProcessor;
	static {
		processors = new ArrayList<NodeProcessor>(20);
		processors.add(new ClassProcessor(ClassNode.class, ClassStateType.class));
		processors.add(new FunctionCallTypeProcessor(FunctionCallNode.class, FunctionCallType.class));
		processors.add(new IdentifierProcessor(IdentifierNode.class, IdentifierNodeType.class));
		processors.add(new FunctionProcessor(FunctionNode.class, FunctionNodeType.class));
		processors.add(new VariableProcessor(VariableNode.class, NodeType.class));
		processors.add(new FullNameNodeProcessor(FullNameNode.class, NodeType.class));
		processors.add(new ImportNodeProcessor(ImportNode.class, ImportNodeType.class));
		processors.add(new MemberAccessExpressionNodeProcessor(MemberAccessExpressionNode.class, NodeType.class));
		
		defaultProcessor = new DefaultNodeProcessor(NodeBase.class, NodeType.class);
		
		
		processorMap = new HashMap<Class, NodeProcessor>(processors.size());
		for(NodeProcessor currentProcessor : processors) {
			processorMap.put(currentProcessor.modelNode, currentProcessor);
			processorMap.put(currentProcessor.xmlNode, currentProcessor);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
