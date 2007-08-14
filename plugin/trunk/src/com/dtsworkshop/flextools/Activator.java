/**
*	Copyright (C) Oliver B. Tupman, 2007.
*	
*	This file is part of the Flex Tools Project.
*	
*	The Flex Tools Project is free software; you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation; either version 3 of the License, or
*	(at your option) any later version.
*	
*	The Flex Tools Project is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*	GNU General Public License for more details.
*	
*	You should have received a copy of the GNU General Public License
*	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.dtsworkshop.flextools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.framework.internal.core.AbstractBundle;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.dtsworkshop.flextools.builder.IResourceAsDeltaVisitor;
import com.dtsworkshop.flextools.codemodel.IProjectStateManager;
import com.dtsworkshop.flextools.codemodel.WorkingSpaceModelStateManager;
import com.dtsworkshop.flextools.project.ProjectManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.dtsworkshop.flextools";

	// The shared instance
	private static Activator plugin;
	private static IProjectStateManager stateManager = new WorkingSpaceModelStateManager();
	private List<IResourceAsDeltaVisitor> deltaVisitors = new ArrayList<IResourceAsDeltaVisitor>(2);
	
	private ProjectManager projectManager = new ProjectManager();
	
	public ProjectManager getProjectManager() {
		return projectManager;
	}
	
	
	public void addDeltaVisitor(IResourceAsDeltaVisitor newVisitor) {
		deltaVisitors.add(newVisitor);
	}
	
	public List<IResourceAsDeltaVisitor> getVisitors() {
		return deltaVisitors;
	}
	
	/**
	 * Gets the project build state manager for the plugin.
	 * 
	 * @return The build state manager for Flex Tools
	 */
	public static IProjectStateManager getStateManager() {
		return stateManager;
	}
	
	/**
	 * The constructor
	 */
	public Activator() {
		// Force initialisation of the model manager
		//CodeModelManager.getManager();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		initialise();
		FlexToolsStartup.doStartup();
	}
	
	private void initialise() {
//		stateManager.initialise();
//		loadExtensions();
		
	}
	
	public static final String DELTA_VISITOR_EXTENSIONID = "com.dtsworkshop.flextools.deltaVisitor";
	public static final String PROJECT_LOAD_JOBS_EXTENSIONID = "com.dtsworkshop.flextools.projectLoadJob";
	
	private void loadExtensions() {
		Object [] loadedExtensions = loadSimpleExtensions(DELTA_VISITOR_EXTENSIONID);
		for(Object extension : loadedExtensions) {
			addDeltaVisitor((IResourceAsDeltaVisitor)extension);
		}

	}

	public static Object [] loadSimpleExtensions(String extensionId) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		List<Object> loadedExtensions = new ArrayList<Object>(20);
		IConfigurationElement [] elements = registry.getConfigurationElementsFor(extensionId);
		for(IConfigurationElement currentVisitorElement : elements) {
			if(!currentVisitorElement.isValid()) {
				System.err.println("Isn't valid.");
			}
			try {
				loadedExtensions.add(currentVisitorElement.createExecutableExtension("class"));
				
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println(currentVisitorElement.getName());
		}
		return (Object[])loadedExtensions.toArray(new Object[loadedExtensions.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
