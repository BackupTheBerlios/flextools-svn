package com.dtsworkshop.flextools.builder.processors;

import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.definitions.IMetaAttributes;
import com.adobe.flexbuilder.codemodel.internal.tree.ExpressionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.IdentifierNode;

public class ProcessorHelper {

	public static String getQualifiedName(ExpressionNode identNode) {
		IDefinition def = identNode.getDefinition();
		String qualifiedName = "";
		if(def != null) {
			qualifiedName = def.getQualifiedName();
			
		}
		return qualifiedName;
	}
	
	

}
