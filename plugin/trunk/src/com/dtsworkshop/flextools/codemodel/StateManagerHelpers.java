package com.dtsworkshop.flextools.codemodel;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

public class StateManagerHelpers {

	public static String getStateName(IResource resource) {
		return getStateName(resource.getProjectRelativePath());
	}
	
	public static String getStateName(IPath path) {
		return getStateName(path.toString());
	}
	
	public static String getStateName(String path) {
		return path.replace("/", ".");
	}
}
