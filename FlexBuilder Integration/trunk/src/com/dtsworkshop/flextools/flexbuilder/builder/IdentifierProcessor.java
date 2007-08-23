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

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

//import uk.co.badgersinfoil.metaas.ActionScriptFactory;
//import uk.co.badgersinfoil.metaas.ActionScriptParser;
//import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;

import antlr.Token;

import com.adobe.flexbuilder.codemodel.definitions.ASDefinitionCache;
import com.adobe.flexbuilder.codemodel.definitions.ASDefinitionFilter;
import com.adobe.flexbuilder.codemodel.definitions.IASScope;
import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.internal.testing.IAdaptableNode;
import com.adobe.flexbuilder.codemodel.internal.tree.BlockNode;
import com.adobe.flexbuilder.codemodel.internal.tree.ClassNode;
import com.adobe.flexbuilder.codemodel.internal.tree.FunctionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.IdentifierNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.adobe.flexbuilder.codemodel.internal.tree.VariableNode;
import com.adobe.flexbuilder.codemodel.tree.ASOffsetInformation;
import com.adobe.flexbuilder.codemodel.tree.IASNode;
import com.adobe.flexbuilder.codemodel.tree.IFileNode;
import com.adobe.flexbuilder.codemodel.tree.IScopedNode;
import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.IdentifierNodeType;
import com.dtsworkshop.flextools.builder.processors.ProcessorHelper;

public class IdentifierProcessor extends DefaultNodeProcessor {
	private static Logger log = Logger.getLogger(IdentifierProcessor.class);
	public IdentifierProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BuildReference getNode(NodeBase node, BuildReference parentType, BuildStateType buildState) {
		// TODO Auto-generated method stub
		IdentifierNode identNode = (IdentifierNode)node;
		
		IdentifierNodeType identifierType = (IdentifierNodeType)super.getNode(node, parentType, buildState);
		String qualifiedName = FbCodeModelHelper.getQualifiedName(identNode);
		if(node.getStart() == -1) {
			log.debug(String.format("Ident node %s has -1", identNode.getNameString()));
			IASNode parent = node;
			
			boolean isValidNode = false;
			while(!isValidNode) {
				parent = parent.getParent();
				isValidNode = parent.getStart() != -1;
			}
			ASOffsetInformation info = new ASOffsetInformation(parent.getEnd()+ 10, parent.getParent());
			IASNode containingNode = info.getContainingNode();
			if(parent instanceof ClassNode) {
				//ClassNode classNode = (ClassNode)parent;
				parent = parent.getParent();
			}
			else if(parent instanceof VariableNode) {
				VariableNode varNode = (VariableNode)parent;
			}
			else if(parent instanceof BlockNode) {
				BlockNode blockNode = (BlockNode)parent;
			}
			else if(parent instanceof FunctionNode) {
				FunctionNode funcNode = (FunctionNode)parent;
			}
			int validStart = parent.getStart();
			int validEnd = parent.getEnd();
			log.debug(String.format("Valid parent is from %d to %d, type is %s", validStart, validEnd, parent.getNodeType()));

			String parentData = getFileData().substring(validStart, validEnd); 
			
//			FileInputStream in = new FileInputStream("Test.as");
//			InputStreamReader reader = new InputStreamReader(in);
//			ActionScriptFactory fact = new ActionScriptFactory();
//			ActionScriptParser parser = fact.newParser();
//			ASCompilationUnit unit = parser.parse(reader);
//			ActionScriptFactory asFactory = new ActionScriptFactory();
//			ActionScriptParser parser = asFactory.newParser();
//			StringReader reader = new StringReader(parentData);
//			
//			try {
//				ASCompilationUnit parseResult = parser.parse(reader);
//				log.debug("asdf");
//			} catch (RuntimeException e) {
//				e.printStackTrace();
//			}
		}
		
//		String content = "";
//		
//		content = String.format(
//			"qualified name: %s, package name: %s",
//			qualifiedName, identNode.getPackageName()
//		);
//		
//		identifierType.setContents(content);
		identifierType.setQualifiedName(qualifiedName);
		identifierType.setName(ProcessorHelper.getLocalName(qualifiedName));
		return identifierType;
	}

}
