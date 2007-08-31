package com.dtsworkshop.flextools.flexbuilder.model;

import org.eclipse.core.resources.IFile;

import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.dtsworkshop.flextools.codemodel.ITypeInfo;

public class FlexBuilderTypeInfo implements ITypeInfo {
	private IDefinition sourceDef;
	private IFile enclosingFile;
	
	public FlexBuilderTypeInfo(IDefinition sourceDef, IFile enclosingFile) {
		this.sourceDef = sourceDef;
		this.enclosingFile = enclosingFile;
	}
	
	public String getQualifiedName() {
		// TODO Auto-generated method stub
		return this.sourceDef.getQualifiedName();
	}

	public String getShortName() {
		// TODO Auto-generated method stub
		return this.sourceDef.getShortName();
	}

	public Object getAdapter(Class adapter) {
		if(adapter == IFile.class) {
			return enclosingFile;
		}
		return null;
	}

}
