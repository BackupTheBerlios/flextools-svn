package com.dtsworkshop.flextools.builder.processors;


import com.adobe.flexbuilder.codemodel.definitions.IVariable;
import com.adobe.flexbuilder.codemodel.internal.tree.ExpressionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.FullNameNode;
import com.adobe.flexbuilder.codemodel.internal.tree.IdentifierNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.adobe.flexbuilder.codemodel.internal.tree.VariableNode;
import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.NodeType;

public class VariableProcessor extends DefaultNodeProcessor {

	@Override
	public BuildReference getNode(NodeBase node, BuildReference parentType, BuildStateType buildState) {
		// TODO Auto-generated method stub
		NodeType type= (NodeType)super.getNode(node, parentType, buildState);
		IVariable variable = (IVariable)node;
		String content = type.getContents();
		content += String.format(
			" and variable type '%s'",
			variable.getVariableType()
		);
		type.setContents(content);
		type.addNewStopNode().setContents(content);
		IVariable var = (IVariable)node;
		System.out.println(String.format(
			"Variable name %s, type: %s, classification: %s",
			var.getName(), var.getVariableType(), var.getVariableClassification().toString()
		));
		System.out.println(String.format(
			"Variable package name: %s, qualified name: %s, is implicit? %s",
			var.getPackageName(), var.getQualifiedName(),
			(var.isImplicit()) ? "yes" : "no"
		));
		VariableNode varNode = (VariableNode)var;
		ExpressionNode typeNode = (ExpressionNode)varNode.getTypeNode();
		if(typeNode instanceof IdentifierNode) {
			IdentifierNode identNode = (IdentifierNode)typeNode;
			String identText = identNode.getText();
			String nameString = identNode.getNameString();
			System.out.println(String.format(
					"Variable has ident node of %s %s"
				, identText, nameString
			));
		}
		return type;
	}

	public VariableProcessor(Class modelNode, Class xmlNode) {
		super(modelNode, xmlNode);
		// TODO Auto-generated constructor stub
	}

}
