/**
 * 
 */
package com.dtsworkshop.flextools.flexbuilder.builder;

import java.util.logging.Logger;

import com.adobe.flexbuilder.codemodel.definitions.IASScope;
import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.internal.tree.BlockNode;
import com.adobe.flexbuilder.codemodel.internal.tree.ClassNode;
import com.adobe.flexbuilder.codemodel.internal.tree.ContainerNode;
import com.adobe.flexbuilder.codemodel.internal.tree.ExpressionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.FunctionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.IdentifierNode;
import com.adobe.flexbuilder.codemodel.internal.tree.KeywordNode;
import com.adobe.flexbuilder.codemodel.internal.tree.MemberedNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.adobe.flexbuilder.codemodel.internal.tree.TransparentContainerNode;
import com.adobe.flexbuilder.codemodel.internal.tree.VariableNode;
import com.adobe.flexbuilder.codemodel.tree.IASNode;
import com.adobe.flexbuilder.codemodel.tree.IExpressionNode;
import com.adobe.flexbuilder.codemodel.tree.IScopedNode;
import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.ClassInterfaceReference;
import com.dtsworkshop.flextools.model.ClassStateType;
import com.dtsworkshop.flextools.model.FunctionNodeType;

public class ClassProcessor extends DefaultNodeProcessor {
	private Logger log = Logger.getLogger(ClassProcessor.class.getName());
	
	public ClassProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
		// TODO Auto-generated constructor stub
	}
/*
 - BlockNode
  * Import Node
  - ModifiersNode
  - ClassNode
  
 - ClassNode
 - ImportNode
 */
	@Override
	public BuildReference getNode(NodeBase node, BuildReference parentType, BuildStateType buildState) {
		// TODO Auto-generated method stub
		ClassStateType type = (ClassStateType) super.getNode(node, parentType, buildState);
		
		log.info("Processing class.");
		ClassNode classNode = (ClassNode)node;
		IScopedNode scopedNode = node.getScopeNode();
		
		ExpressionNode baseClassNode = classNode.getBaseClassNode();
		type.setBaseClassName(classNode.getBaseClassName());
		type.setName(classNode.getName());
		type.setQualifiedName(classNode.getQualifiedName());
		FunctionNodeType functionNode = type.addNewConstructor();
		//type.setConstructor(classNode.getConstructorNode().getQualifiedName());
		FunctionNode constructorNode = classNode.getConstructorNode(); 
		functionNode.setStartPos(constructorNode.getStart());
		functionNode.setEndPos(constructorNode.getEnd());
		VariableNode superNode = classNode.getSuperNode();
		if(superNode != null) {
			if(superNode.getStart() != -1) {
				log.info("Supernode has a start.");
			}
		}
		KeywordNode extendsNode = classNode.getExtendsKeywordNode();
		if(extendsNode != null) {
			if(extendsNode.getStart() != -1) {
				log.info("Extends node has a start");
			}
		}
		if(baseClassNode != null) {
			if(baseClassNode.getDefinition() != null) {
				log.info("Processing base class.");
				baseClassNode.getScopeNode();
				BlockNode contents = classNode.getContents();
				ContainerNode contNode;
				
				ClassInterfaceReference baseRef = type.addNewExtends();
				addClassInterfaceRef(baseRef, baseClassNode, baseClassNode.getDefinition());
				
			}
		}
		
		//int baseClassStart = baseClassNode.getStart();
		addInterfaces(type, classNode);
		//addDeriviationNode(type, classNode);
		return type;
	}
	
	private void addDeriviationNode(ClassStateType typeNode, IASNode sourceNode) {
		IASNode extendsNode = sourceNode.getChild(1);
		if(sourceNode instanceof ClassNode) {
			((ClassNode)sourceNode).getScopeNode();
		}
		if(extendsNode instanceof IdentifierNode) {
			((IdentifierNode)extendsNode).getScopeNode();
			IExpressionNode expNode = (IExpressionNode)extendsNode;
			boolean isValid = expNode.getDefinition() != null;
			if(isValid) {
				String text = ((IdentifierNode)expNode).getText();
				
				String [] attributes = expNode.getAdaptableAttributes();
				ClassInterfaceReference ref = typeNode.addNewExtends();
				addClassInterfaceRef(ref, expNode, expNode.getDefinition());
			}
		}
		System.out.println("Extends node");
	}
	
	private void addInterfaces(ClassStateType typeNode, IASNode sourceNode) {
		IASNode interfaceNode = null;
		for(IASNode nodeChild : sourceNode.getChildren()) {
			if(nodeChild instanceof TransparentContainerNode) {
				interfaceNode = nodeChild;
				break;
			}
		}
		if(interfaceNode != null) {
			for(IASNode node : interfaceNode.getChildren()) {
				if(!(node instanceof IExpressionNode)) {
					continue;
				}
				IExpressionNode expNode = (IExpressionNode)node;
				IDefinition nodeDef = expNode.getDefinition();
				if(nodeDef != null) {
					log.info("Processing interface " + nodeDef.getName());
					ClassInterfaceReference ref = typeNode.addNewImplements();
					addClassInterfaceRef(ref, node, nodeDef);
				}
				
			}
		}
	}

	private void addClassInterfaceRef(ClassInterfaceReference ref, IASNode documentNode, IDefinition definition) {
		if(definition == null) {
			log.info("ClassProcessor.addClassInterfaceRef:Definition is null.");
			return;
		}
		if(documentNode.getStart() == -1) {
			
			log.info(String.format("ClassProcessor.addClassInterfaceRef: Reference %s has no start", definition.getName()));
		}
		ref.setStartPos(documentNode.getStart());
		ref.setEndPos(documentNode.getEnd());
		ref.setShortName(definition.getName());
		ref.setQualifiedName(definition.getQualifiedName());
//		System.out.println(ref.xmlText());
	}
	
	/*
	 public class MultipleInterfaceClass implements SimpleInterface, DerivingInterface, IList
	  - Ident node: class name
	  - KeywordNode: implements
	  - TransparentContainerNode
	    * IdentifierNode: interfaces
	  - BlockNode (class contents)
	 * 
	 public class ImplementingSimpleInterface implements SimpleInterface
	  - Ident node: class name
	  - Ident node: empty, start & end = -1
	  - TransparentContainerNode: 
	   * interface implementations
	  - BlockNode (class contents)
	 *
	 public class SimpleClass
	  - Ident node: class name
	  - Ident node: empty, start & end = -1
	  - BlockNode (contents)
	 * 
	 public class ImplementingDerivingInterface extends SimpleClass implements DerivingInterface
	 class node children:
	 - IdentifierNode: class name
	 - Ident node: extending
	 - TransparentContainerNode
	 	* Ident node: interface implementations 
	 - BlockNode (class contents?)
	 */
	
	
}