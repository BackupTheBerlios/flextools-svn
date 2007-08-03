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
import com.dtsworkshop.flextools.model.BuildStateDocument;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.ClassStateType;
import com.dtsworkshop.flextools.model.FunctionCallType;
import com.dtsworkshop.flextools.model.FunctionNodeType;
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
		processNode(startNode, fileNode, fileData);
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
	
	private void processNode(IASNode node, NodeType parentXmlNode, String textData) {
		IASNode [] children = node.getChildren();
		for(IASNode child : children) {
			NodeProcessor processor = getProcessor(child.getClass());
			NodeType newXmlChild = processor.getNode((NodeBase)child, parentXmlNode);
			newXmlChild.setContents(getNodeContents(textData, child));
			if(child.getChildCount() > 0) {
				processNode(child, newXmlChild, textData);
				
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
		processors.add(new IdentifierProcessor(IdentifierNode.class, NodeType.class));
		processors.add(new FunctionProcessor(FunctionNode.class, FunctionNodeType.class));
		defaultProcessor = new DefaultNodeProcessor(NodeBase.class, NodeType.class);
		
		
		processorMap = new HashMap<Class, NodeProcessor>(processors.size());
		for(NodeProcessor currentProcessor : processors) {
			processorMap.put(currentProcessor.modelNode, currentProcessor);
			processorMap.put(currentProcessor.xmlNode, currentProcessor);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
