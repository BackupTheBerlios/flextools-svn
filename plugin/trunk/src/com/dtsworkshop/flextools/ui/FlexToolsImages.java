package com.dtsworkshop.flextools.ui;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;

import com.dtsworkshop.flextools.Activator;

public class FlexToolsImages {
	
	private static final IPath ICONS_PATH = new Path("/icons");
	
	public static final String MAIN_ICON = "standard small icon.png";
	
	public static ImageDescriptor create(String name) {
		return Activator.getImageDescriptor(name);
	}
	
//	private static ImageDescriptor create(String prefix, String name, boolean useMissingImageDescriptor) {
//		IPath path= ICONS_PATH.append(prefix).append(name);
//		return createImageDescriptor(Activator.getDefault().getBundle(), path, useMissingImageDescriptor);
//	}
}
