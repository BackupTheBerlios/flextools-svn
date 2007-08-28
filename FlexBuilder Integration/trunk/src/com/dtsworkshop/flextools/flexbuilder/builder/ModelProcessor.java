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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import sun.reflect.generics.tree.ClassTypeSignature;

import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.internal.tree.*;
import com.adobe.flexbuilder.codemodel.tree.IASNode;
import com.adobe.flexbuilder.codemodel.tree.IFileNode;
import com.dtsworkshop.flextools.FlexToolsLog;
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

/**
 * A processor to process the Flex Builder state model.
 * Depends on various bits internal to Flex.
 * 
 * TODO: Try to remove as many of the internal references as possible
 * 
 * @author otupman
 *
 */
@SuppressWarnings("restriction")
public class ModelProcessor {
	private static Logger log = Logger.getLogger(ModelProcessor.class);
	
	private String getFileContents(IFile file) {
		
		try {
			InputStream fileInputStream = file.getContents();

			return ResourceHelper.streamToString(fileInputStream);
		} catch (CoreException e) {
			e.printStackTrace();
			FlexToolsLog.logError(e);
		} catch (IOException e) {
			e.printStackTrace();
			FlexToolsLog.logError(e);
		}
		return "";
	}

	/**
	 * Gets the state document for the source file and it's corresponding
	 * root node.
	 * 
	 * @param startNode Root node for the file
	 * @param sourceFile The source file 
	 * @return The constructed state document
	 */
	public BuildStateDocument getStateDocument(IFileNode startNode, IFile sourceFile) {
		log.debug("Getting state document for " + sourceFile.getName());
		XmlOptions options = new XmlOptions();
		options.setSavePrettyPrint();
		options.setSavePrettyPrintIndent(2);
		BuildStateDocument doc = BuildStateDocument.Factory.newInstance(options);
		IDefinition [] defs = startNode.getAllTopLevelDefinitions(false, true);
		buildStateDocument(startNode, sourceFile, doc);
		return doc;
	}
	
	private void buildStateDocument(IFileNode startNode, IFile sourceFile, BuildStateDocument doc) {
		log.debug("Building state document for " + sourceFile.getName());
		BuildStateType buildType = doc.addNewBuildState();
		buildType.setFile(sourceFile.getProjectRelativePath().toString());
		buildType.setProject(sourceFile.getProject().getName());
		NodeType fileNode = buildType.addNewFileNode();
		String fileData = getFileContents(sourceFile);
		processNode(startNode, fileNode, fileData, buildType);
	}
	
	
	private void processNode(IASNode node, BuildReference parentXmlNode, String textData, BuildStateType buildState) {
		IASNode [] children = node.getChildren();
		for(IASNode child : children) {
			if(child.getStart() == child.getEnd()) {
				log.warn(String.format("Found a node of type %s whose start & end are the same [start: %d]", child.getNodeType(), child.getStart()));
			//	continue;
			}
			NodeProcessor processor = getProcessor(child.getClass());
			processor.setFileData(textData);
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
	
	/**
	 * List of processors that will process nodes for us.
	 */
	private static List<NodeProcessor> processors;
	/**
	 * Map between source class name and the node processor.
	 * Generated from the processors list.
	 */
	private static Map<Class, NodeProcessor> processorMap;
	
	
	/** Default fallback processor for any node types not handled by custom processors */
	private static NodeProcessor defaultProcessor;
	
	static {
		processors = new ArrayList<NodeProcessor>(20);
		//TODO: Determine whether the following mappings can use the interfaces
		processors.add(new ClassProcessor(ClassNode.class, ClassStateType.class));
		processors.add(new FunctionCallTypeProcessor(FunctionCallNode.class, FunctionCallType.class));
		processors.add(new IdentifierProcessor(IdentifierNode.class, IdentifierNodeType.class));
		processors.add(new FunctionProcessor(FunctionNode.class, FunctionNodeType.class));
		processors.add(new VariableProcessor(VariableNode.class, NodeType.class));
		processors.add(new FullNameNodeProcessor(FullNameNode.class, NodeType.class));
		processors.add(new ImportNodeProcessor(ImportNode.class, ImportNodeType.class));
		processors.add(new MemberAccessExpressionNodeProcessor(MemberAccessExpressionNode.class, NodeType.class));
		processors.add(new AttributeNodeProcessor(AttributeNode.class, NodeType.class));
		defaultProcessor = new DefaultNodeProcessor(NodeBase.class, NodeType.class);
		
		
		processorMap = new HashMap<Class, NodeProcessor>(processors.size());
		for(NodeProcessor currentProcessor : processors) {
			processorMap.put(currentProcessor.modelNode, currentProcessor);
			processorMap.put(currentProcessor.xmlNode, currentProcessor);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
