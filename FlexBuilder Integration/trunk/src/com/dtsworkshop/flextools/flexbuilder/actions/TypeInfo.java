package com.dtsworkshop.flextools.flexbuilder.actions;

import org.eclipse.core.resources.IFile;

/**
 * Simple value class representing some information about a type.
 * Provided is the qualified name of the class, as well as a
 * reference to the file that contains the  type.
 * 
 * @author otupman
 *
 */
public class TypeInfo {
	
	private String qualifiedName;
	private IFile typeFile;
	/**
	 * The full name (qualified name) of the type
	 */
	public String getQualifiedName() {
		return qualifiedName;
	}
	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}
	
	/**
	 * Reference to the file that contains the type
	 */
	public IFile getTypeFile() {
		return typeFile;
	}
	public void setTypeFile(IFile typeFile) {
		this.typeFile = typeFile;
	}
	
	
}