package com.dtsworkshop.flextools.builder.processors;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.definitions.IClass;
import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.definitions.IMetaAttributes;
import com.adobe.flexbuilder.codemodel.internal.tree.ExpressionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.IdentifierNode;
import com.adobe.flexbuilder.codemodel.internal.tree.MemberedNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;

public class ProcessorHelper {

	public static String getQualifiedName(ExpressionNode identNode) {
		IDefinition def = identNode.getDefinition();
		String qualifiedName = "";
		if(def != null) {
			qualifiedName = def.getQualifiedName();
			
		}
		return qualifiedName;
	}
	
//	public static String getReferenceClassName(MemberedNode clazzNode) {
//		clazzNode.get
//	}

}
