package com.dtsworkshop.flextools.builder.processors;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.definitions.IClass;
import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.definitions.IMetaAttributes;
import com.adobe.flexbuilder.codemodel.internal.tree.ExpressionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.IdentifierNode;
import com.adobe.flexbuilder.codemodel.internal.tree.MemberedNode;
import com.adobe.flexbuilder.codemodel.internal.tree.NodeBase;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.TypeReference;

public class ProcessorHelper {

	public static String getQualifiedName(ExpressionNode identNode) {
		IDefinition def = identNode.getDefinition();
		String qualifiedName = "";
		if(def != null) {
			qualifiedName = def.getQualifiedName();
			
		}
		return qualifiedName;
	}

	public static void addImport(BuildStateType buildState, String qualifiedName) {
		TypeReference newImport = buildState.addNewImportReference();
		newImport.setQualifiedName(qualifiedName);
		String localName = getLocalName(qualifiedName);
		newImport.setShortName(localName);
	}

	public static String getLocalName(String qualifiedName) {
		String localName = "";
		int lastDot = qualifiedName.lastIndexOf(".");
		if(lastDot != -1) {
			localName = qualifiedName.substring(lastDot + 1);
		}
		return localName;
	}
	
//	public static String getReferenceClassName(MemberedNode clazzNode) {
//		clazzNode.get
//	}

}
