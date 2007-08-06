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
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.internal.ui.PixelConverter;
import org.eclipse.debug.internal.ui.SWTUtil;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsMessages;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.internal.navigator.resources.workbench.ResourceExtensionSorter;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.navigator.ResourcePatternFilter;
import org.eclipse.ui.views.navigator.ResourceSorter;

public class LaunchUrlSelector extends Composite {

	private Text debugLocation;
	private Group group;
	
	public String getUrlText() {
		return debugLocation.getText();
	}
	
	public void setUrlText(String urlText) {
		debugLocation.setText(urlText);
	}
	
	public LaunchUrlSelector(Composite parent, int style, String label) {
		super(parent, style);
		group = new Group(parent, SWT.NONE);
		group.setText(label);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		
		group.setLayout(layout);
		
		setupLocationInput();
		
		Composite selectionGroup = new Composite(group, SWT.NONE);
		GridLayout selectionLayout = new GridLayout();
		selectionLayout.numColumns = 3;
		selectionGroup.setLayout(selectionLayout);
//		Button button1 = createPushButton(selectionGroup, "Button 1", null);
//		Button button2 = createPushButton(selectionGroup, "Button 2", null);
//		Button button3 = createPushButton(selectionGroup, "Button 3", null);
		createFileSelectors(selectionGroup);
	}
	
	private List<ModifyListener> modifyListeners = new ArrayList<ModifyListener>();
	
	/**
	 * Adds a listener to the location text input.
	 * 
	 * @param listener The listener to add.
	 */
	public void addModifyEventListener(ModifyListener listener) {
		modifyListeners.add(listener);
	}
	
	/**
	 * Removes a listener from the listeners to the text input.
	 * 
	 * @param listenerToRemove The listener to remove.
	 */
	public void removeModifyListener(ModifyListener listenerToRemove) {
		modifyListeners.remove(listenerToRemove);
	}
	
	private void setupLocationInput() {
		debugLocation = new Text(group, SWT.LEFT | SWT.BORDER | SWT.SINGLE);
		GridData textData = new GridData();
		textData.widthHint = 500;
		debugLocation.setLayoutData(textData);
		debugLocation.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				for(ModifyListener listener : modifyListeners) {
					listener.modifyText(event);
				}
			}
		});
	}

	private void createTextInput(Composite parent) {
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 300;
		debugLocation = new Text(parent, SWT.LEAD | SWT.SINGLE);
		debugLocation.setLayoutData(data);
	}

	protected Button createPushButton(Composite parent, String label, Image image) {
		return SWTUtil.createPushButton(parent, label, image);
	}
	

	private void createFileSelectors(Composite parent) {
		
		Button fWorkspaceBrowse;
		Button fFileBrowse;
		Button fVariables;
        fWorkspaceBrowse = createPushButton(
        		parent, 
        		LaunchConfigurationsMessages.CommonTab_12, 
        		null
        ); 
        fWorkspaceBrowse.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
                		getShell(), 
                		new WorkbenchLabelProvider(), 
                		new WorkbenchContentProvider()
                );
                dialog.setTitle(LaunchConfigurationsMessages.CommonTab_13); 
                dialog.setMessage(LaunchConfigurationsMessages.CommonTab_14); 
                dialog.setInput(ResourcesPlugin.getWorkspace().getRoot()); 
                dialog.setSorter(new ResourceSorter(ResourceSorter.NAME));
                ResourcePatternFilter filter = new ResourcePatternFilter();
                //TODO: Discover why the pattern matching doesn't work.
                filter.setPatterns(new String[] {
                		"*.swf"
                		, "*.mxml"
                		, "*.as"
                		, "*.txt"
                		, "*.xml"
                });
                dialog.addFilter(filter);
                if (dialog.open() == IDialogConstants.OK_ID) {
                    IResource resource = (IResource) dialog.getFirstResult();
                    String arg = resource.getFullPath().toString();
                    String fileLoc = VariablesPlugin.getDefault().getStringVariableManager().generateVariableExpression("workspace_loc", arg); //$NON-NLS-1$
                    debugLocation.setText(fileLoc);
                }
            }
        });
        fFileBrowse = createPushButton(parent, LaunchConfigurationsMessages.CommonTab_7, null);
        fFileBrowse.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	if(debugLocation == null) {
            		throw new NullPointerException("Text file box is null.");
            	}
                String filePath = debugLocation.getText();
                FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
                dialog.setFilterExtensions(new String[] {
                	  "*.htm"
                	, "*.html"
                }
                );
                filePath = dialog.open();
                if (filePath != null) {
                	debugLocation.setText(filePath);
                }
            }
        });
        fVariables = createPushButton(parent, LaunchConfigurationsMessages.CommonTab_9, null); 
        fVariables.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(getShell());
				dialog.open();
				String variable = dialog.getVariableExpression();
				if (variable != null) {
					debugLocation.insert(variable);
				}
            }
            public void widgetDefaultSelected(SelectionEvent e) {}
        });
	}
}
