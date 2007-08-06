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
package com.dtsworkshop.flextools.launch;

import org.eclipse.core.internal.resources.WorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.internal.navigator.filters.CoreExpressionFilter;

import com.adobe.flexbuilder.debug.launching.FlexLaunch;
import com.adobe.flexbuilder.debug.launching.FlexLaunchConfiguration;
import com.adobe.flexbuilder.debug.launching.FlexLaunchConfigurationWorkingCopy;
import com.adobe.flexbuilder.debug.launching.FlexLaunchDelegate;
import com.adobe.flexbuilder.debug.ui.launching.AbstractFlexLaunchTab;
import com.adobe.flexbuilder.debug.ui.launching.FlexLaunchTab;
import com.adobe.flexbuilder.project.internal.FlexBasedProject;

public class LocalFlexLauncher extends LaunchConfigurationDelegate{
	private FlexLaunchDelegate flexLaunch;

	public LocalFlexLauncher() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static final String ADOBE_PROJECT_NAME = "com.adobe.flexbuilder.debug.ATTR_PROJECT";
	public static final String ADOBE_APPNAME = "com.adobe.flexbuilder.debug.ATTR_APPLICATION";
	public static final String ADOBE_DEBUG_URL = "com.adobe.flexbuilder.debug.ATTR_DEBUG_URL";
	public static final String ADOBE_RUN_URL = "com.adobe.flexbuilder.debug.ATTR_RUN_URL";
	public static final String ADOBE_USE_DEFAULT_URLS = "com.adobe.flexbuilder.debug.ATTR_USE_DEFAULT_URLS";

	private String resolveVariableExpression(String variableExpression) {
		String resolvedExpression = "";
		try {
			resolvedExpression = VariablesPlugin
				.getDefault()
				.getStringVariableManager()
				.performStringSubstitution(variableExpression);
		}
		catch(CoreException ex) {
			ex.printStackTrace();
		}
		return resolvedExpression;
	}
	
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		flexLaunch = new FlexLaunchDelegate();
		
		//FlexLaunchConfiguration launchConfig = new FlexLaunchConfiguration(configuration);
		ILaunchConfigurationWorkingCopy workingCopy = configuration.getWorkingCopy();
		String projectName = configuration.getAttribute(LocalLaunchConfigTab.ATTR_PROJECT_NAME, "");
		workingCopy.setAttribute(
			ADOBE_PROJECT_NAME,
			projectName
		);
		String applicationFilename = configuration.getAttribute(LocalLaunchConfigTab.ATTR_APP_FILENAME, "");
		workingCopy.setAttribute(
			ADOBE_APPNAME,
			applicationFilename
		);
		String debugUrl = configuration.getAttribute(LocalLaunchConfigTab.ATTR_DEBUG_URL, "");
		workingCopy.setAttribute(
			ADOBE_DEBUG_URL,
			resolveVariableExpression(debugUrl)
		);
		String runUrl = configuration.getAttribute(LocalLaunchConfigTab.ATTR_RUN_URL, "");
		workingCopy.setAttribute(
			ADOBE_RUN_URL,
			resolveVariableExpression(runUrl)
		);
		workingCopy.setAttribute(
			ADOBE_USE_DEFAULT_URLS,
			false
		);
		//workingCopy.setAttribute(attributeName, value)
		flexLaunch.launch(workingCopy, mode, launch, monitor);
	}
}
