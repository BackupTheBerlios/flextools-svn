package com.dtsworkshop.flextools.flexbuilder.builder;

import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.internal.tree.ExpressionNode;

public class FbCodeModelHelper {
	public static String getQualifiedName(ExpressionNode identNode) {
		IDefinition def = identNode.getDefinition();
		String qualifiedName = "";
		if(def != null) {
			qualifiedName = def.getQualifiedName();
			
		}
		return qualifiedName;
	}

}
