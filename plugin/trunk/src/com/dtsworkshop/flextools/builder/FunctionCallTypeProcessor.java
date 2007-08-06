package com.dtsworkshop.flextools.builder;

import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.definitions.IFunction;
import com.adobe.flexbuilder.codemodel.internal.tree.ExpressionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.FunctionCallNode;
import com.adobe.flexbuilder.codemodel.internal.tree.KeywordNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.builder.processors.DefaultNodeProcessor;
import com.dtsworkshop.flextools.builder.processors.ProcessorHelper;
import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.FunctionCallType;


public class FunctionCallTypeProcessor extends DefaultNodeProcessor {

	public FunctionCallTypeProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BuildReference getNode(NodeBase node, BuildReference parentType, BuildStateType buildState) {
		// TODO Auto-generated method stub
		FunctionCallType type = (FunctionCallType)super.getNode(node, parentType, buildState);
		FunctionCallNode callNode = (FunctionCallNode)node;
		KeywordNode newKeyword = callNode.getNewKeywordNode();
		
		type.setIsConstructorCall(newKeyword != null);
		//IFunction functionDef = (IFunction)callNode.getNameNode();
		
		type.setName(ProcessorHelper.getQualifiedName(callNode.getNameNode()));
		IDefinition def = callNode.getDefinition();
		
		System.out.println(String.format("Def for function call %s", type.getName()));
		if(def != null) {
			System.out.println(def.getClass().getCanonicalName());
		}
		else {
			
		}
			
		return type;
	}

}
