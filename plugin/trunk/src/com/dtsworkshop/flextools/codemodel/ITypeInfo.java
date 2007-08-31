package com.dtsworkshop.flextools.codemodel;

import org.eclipse.core.runtime.IAdaptable;

/**
 * Describes information about a type.
 * 
 * @author otupman
 *
 */
public interface ITypeInfo extends IAdaptable{
	/**
	 * Gets the qualified (long) name for a type
	 * @return
	 */
	String getQualifiedName();
	/**
	 * Gets the short name for a type.
	 * For example: com.example.SomeClass would return the string SomeClass
	 * @return
	 */
	String getShortName();
}
