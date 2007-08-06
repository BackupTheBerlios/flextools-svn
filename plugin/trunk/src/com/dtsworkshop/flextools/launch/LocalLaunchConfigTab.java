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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.internal.resources.WorkspaceRoot;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTUtil;
import org.eclipse.debug.internal.ui.launchConfigurations.CreateLaunchConfigurationAction;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsMessages;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.navigator.ResourceSorter;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.definitions.ASDefinitionFilter;
import com.adobe.flexbuilder.codemodel.indices.IIndex;
import com.adobe.flexbuilder.debug.ui.launching.AbstractFlexLaunchTab;
import com.adobe.flexbuilder.debug.ui.launching.AbstractFlexMainTab;
import com.adobe.flexbuilder.debug.ui.launching.FlexLaunchTab;
import com.adobe.flexbuilder.debug.ui.launching.FlexMainTab;
import com.adobe.flexbuilder.project.FlexProjectManager;
import com.adobe.flexbuilder.project.IFlexProject;
import com.adobe.flexbuilder.project.actionscript.IActionScriptProject;
import com.adobe.flexbuilder.project.internal.FlexLibraryCore;
import com.adobe.flexbuilder.project.internal.FlexNature;

import com.adobe.flexbuilder.codemodel.project.IProject;
import com.adobe.flexbuilder.codemodel.tree.IProjectRootNode;

public class LocalLaunchConfigTab extends AbstractLaunchConfigurationTab implements
		ILaunchConfigurationTab {
	
	public static final String FLEX_NATURE = "com.adobe.flexbuilder.project.flexnature";
	
	private Logger logger = Logger.getLogger(LocalLaunchConfigTab.class.getName());
	
	private IFlexProject [] flexProjects;
	private Map<IFlexProject, List<String>> projectsToAppFiles;
	private String [] applicationNames;
	private Button testCheckbox;
	protected Combo projectCombo;
	protected Combo applicationFileCombo;
	Composite flexParent;

	LaunchUrlSelector debugSelector;
	LaunchUrlSelector runSelector;
	
	/** The project the user current has selected. If null, then the user has not yet selected a project */
	private IFlexProject selectedProject;
	private String selectedApplicationFilename;
	
	private void createTest(Composite parent) {
	}
	
	/**
	 * Gets the filename
	 * @param fileIndex
	 * @return
	 */
	private String getApplicationFilename(int fileIndex) {
		if(selectedProject == null) {
			return null;
		}
		List<String> projectFiles = projectsToAppFiles.get(selectedProject);
		return projectFiles.get(fileIndex);
	}
	
	
	public void createControl(Composite parent) {
		
	
		
		Composite controlGrouping = new Composite(parent, SWT.NONE);
		
		setControl(controlGrouping);
		GridLayout topLayout = new GridLayout();
		topLayout.marginWidth = 0;
		topLayout.marginHeight = 0;
		topLayout.numColumns = 1;
		controlGrouping.setLayout(topLayout);
		controlGrouping.setFont(parent.getFont());
		
		GridData projectAndAppData = new GridData(GridData.FILL_HORIZONTAL);
		projectAndAppData.widthHint = 400;
		
		setupProjectSelector(controlGrouping);
		projectCombo.setLayoutData(projectAndAppData);
		
		setupApplicationSelector(controlGrouping);
		applicationFileCombo.setLayoutData(projectAndAppData);
		
		createDebugLocationInput(controlGrouping);
		createRunLocationInput(controlGrouping);
		
		debugSelector.setLayout(topLayout);
		runSelector.setLayout(topLayout);
		debugSelector.setFont(parent.getFont());
		runSelector.setFont(parent.getFont());
		
		testCheckbox = super.createCheckButton(controlGrouping, "Test checkbox");
		logger.log(Level.INFO, "Form created");
	}

	private void createRunLocationInput(Composite controlGrouping) {
		runSelector = new LaunchUrlSelector(
			controlGrouping, SWT.LEAD,
			"Run launch location"
		);
		runSelector.addModifyEventListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
			
		});
	}
	
	

	private void createDebugLocationInput(Composite controlGrouping) {
		debugSelector = new LaunchUrlSelector(
			controlGrouping, SWT.LEAD,
			"Debug launch location"
		);
		debugSelector.addModifyEventListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
			
		});
	}

	
	private Combo setupApplicationSelector(Composite parent) {
		String labelText = "Select application file:";
		createLabel(parent, labelText);
		applicationFileCombo = new Combo(parent, SWT.BORDER);
		applicationFileCombo.addSelectionListener(
			new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					onApplicationFileCombo_selected(e);
				}
				
			}
		);
		
		
		return applicationFileCombo;
	}
	
	private void onApplicationFileCombo_selected(SelectionEvent e) {
		logger.info("Application file selected.");
		int fileIndex = applicationFileCombo.getSelectionIndex();
		selectApplicationFile(fileIndex);
		setDirty(true);
	}
	
	private void selectApplicationFile(int index) {
		if(selectedProject == null) {
			return;
		}
		List<String> projectFiles = projectsToAppFiles.get(selectedProject);
		selectedApplicationFilename = projectFiles.get(index);
	}
	
	private void selectApplicationFile(String applicationFilename) {
		if(selectedProject == null) {
			applicationFileCombo.select(0);
			return;
		}
		List<String> projectFiles = projectsToAppFiles.get(selectedProject);
		int fileIndex = projectFiles.indexOf(applicationFilename);
		applicationFileCombo.select(fileIndex);
	}

	private void selectProject(String projectName) {
		boolean found = false;
		IFlexProject currentProject = selectedProject;
		
		for(int i = 0; i < flexProjects.length; i++) {
			IFlexProject project = flexProjects[i];
			if(project.getProject().getName().equals(projectName)) {
				projectCombo.select(i);
				selectedProject = project;
				found = true;
				break;
			}
		}
		
		if(found) {
			if(currentProject != selectedProject) {
				setAppFilesFromProject(selectedProject);
				selectApplicationFile(0);
			}
		}
	}
	
	/**
	 * Selects a project by index
	 * 
	 * @param index The project to select.
	 */
	private void selectProject(int index) {
		selectedProject = flexProjects[index];
		setAppFilesFromProject(selectedProject);
		selectApplicationFile(0);
		setDirty(true);
	}
	
	private void setAppFilesFromProject(IFlexProject project) {
		List<String> appFilenames = projectsToAppFiles.get(project);
		String [] items = new String[appFilenames.size()];
		int index = 0;
		for(String name : appFilenames) {
			items[index] = name;
			index++;
		}
		applicationFileCombo.setItems(items);
	}
	
	
	private void createLabel(Composite parent, String labelText) {
		Label label = new Label(parent, SWT.LEAD);
		
		label.setText(labelText);
	}

	private Combo setupProjectSelector(Composite parent) {
		createLabel(parent, "Project:");
		projectCombo = new Combo(parent, SWT.BORDER);
		projectCombo.setItems(new String []{});
		projectCombo.addSelectionListener(
			new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
					
					logger.info("defaultSelected");
				}

				public void widgetSelected(SelectionEvent e) {
					onProjectCombo_selected(e);
				}
			}
		);
		return projectCombo;
	}

	private void onProjectCombo_selected(SelectionEvent e) {
		
		logger.info("widgetSelected");
		int index = projectCombo.getSelectionIndex();
		selectProject(index);
	}
	
	/**
	 * Returns the project the user has selected. If no project
	 * null will be returned.
	 * 
	 * @return The selected project; otherwise null.
	 */
	public IFlexProject getSelectedProject() {
		return selectedProject;
	}

	
	public String getName() {
		
		return "Local Launch";
	}

	public static final String ATTR_PROJECT_NAME = "com.dtsworkshop.flextools.ATTR_PROJECT_NAME";
	public static final String ATTR_APP_FILENAME = "com.dtsworkshop.flextools.ATTR_APP_FILENAME";
	public static final String ATTR_DEBUG_URL = "com.dtsworkshop.flextools.ATTR_DEBUG_URL";
	public static final String ATTR_RUN_URL = "com.dtsworkshop.flextools.ATTR_RUN_URL";
	
	
	public void initializeFrom(ILaunchConfiguration configuration) {
		
		logger.log(Level.INFO, "Form initialised");
		String appName = "";
		String projectName = "";

		try {
			selectedApplicationFilename = configuration.getAttribute(ATTR_APP_FILENAME, "");
			projectName = configuration.getAttribute(ATTR_PROJECT_NAME, "");
			debugSelector.setUrlText(configuration.getAttribute(ATTR_DEBUG_URL, ""));
			runSelector.setUrlText(configuration.getAttribute(ATTR_RUN_URL, ""));
		} catch (CoreException e) {
			setErrorMessage("Caught exception '" + e.getMessage() + "' when trying to get properties.");
		}
		
		updateFlexProjects();
		setupApplicationCombo();
		setupProjectCombo();
		selectProject(projectName);
		selectApplicationFile(selectedApplicationFilename);
		try {
			performApply(configuration.getWorkingCopy());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setDirty(false);
	}

	/**
	 * Sets up the project combo with the various applications.
	 *
	 */
	private void setupProjectCombo() {
		projectCombo.setItems(applicationNames);
	}

	/**
	 * Sets up the application combo with application names from the selected
	 * project.
	 *
	 */
	private void setupApplicationCombo() {
		logger.info("Setting up application combo box.");
		applicationNames = new String[flexProjects.length];
		int count = 0;
		projectsToAppFiles = new HashMap<IFlexProject, List<String>>();
		for(IFlexProject project : flexProjects) {
			applicationNames[count] = project.getProject().getName();

			IFile [] appFiles = project.getApplicationFiles();
			List<String> appFilenames = new ArrayList<String>(appFiles.length);
			for(IFile file : appFiles) {
				appFilenames.add(file.getName());
			}
			projectsToAppFiles.put(project, appFilenames);			
			
			count++;
		}
	}

	private void updateFlexProjects() {
		logger.info("Updating flex projects.");
		flexProjects = FlexProjectManager.getFlexProjects();
	}

//	private List<IProject> getFlexProjects() {
//		org.eclipse.core.resources.IProject [] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
//		List<IProject> flexProjects = new ArrayList<com.adobe.flexbuilder.codemodel.project.IProject>();
//		for(org.eclipse.core.resources.IProject proj : projects) {
//			boolean hasFlexNature = false;
//			try {
//				hasFlexNature = proj.hasNature(FLEX_NATURE);
//			} catch (CoreException e) {
//				e.printStackTrace();
//			}
//			if(hasFlexNature) {
//				IProject flexProject = CMFactory.getManager().getProjectFor(proj);
//				flexProjects.add(flexProject);
//			}
//		}
//		return flexProjects;
//	}

	
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		
		logger.log(Level.INFO, "Apply performed");
		
		configuration.setAttribute(
			ATTR_APP_FILENAME, 
			(selectedApplicationFilename == null) ? "" : selectedApplicationFilename
		);
		configuration.setAttribute(
			ATTR_PROJECT_NAME, 
			(selectedProject == null) ? "" : selectedProject.getProject().getName()	
		);
		configuration.setAttribute(
			ATTR_DEBUG_URL,
			debugSelector.getUrlText()
		);
		configuration.setAttribute(
			ATTR_RUN_URL,
			debugSelector.getUrlText()
		);
	}

	
	
	
	protected void setDirty(boolean dirty) {
		isValid(null);
		super.setDirty(dirty);
	}

	
	public boolean isValid(ILaunchConfiguration launchConfig) {
		setErrorMessage(null);
		setMessage(null);
		boolean isValid = true;
		if(selectedProject == null) {
			setErrorMessage("You must select a source project for this launch configuration.");
			isValid = false;
		}
		else if(selectedApplicationFilename == null) {
			setErrorMessage("You must select an application file for this launch configuration.");
			isValid = false;
		}
		else if(debugSelector.getUrlText().length() == 0) {
			setErrorMessage("You must select a debug location.");
			isValid = false;
		}
		else if(runSelector.getUrlText().length() == 0) {
			setErrorMessage("You must select a run location.");
			isValid = false;
		}
		return isValid;
	}
	
	

	
	public void activated(ILaunchConfigurationWorkingCopy workingCopy) {
		isValid(workingCopy);
		super.activated(workingCopy);
	}

	
	public void deactivated(ILaunchConfigurationWorkingCopy workingCopy) {
		isValid(workingCopy);
		
		super.deactivated(workingCopy);
	}

	
	public String getErrorMessage() {
		isValid(null);
		String errorMessage = super.getErrorMessage();
		return errorMessage;
	}

	
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		
		logger.log(Level.INFO, "Defaults set");
	}

}
