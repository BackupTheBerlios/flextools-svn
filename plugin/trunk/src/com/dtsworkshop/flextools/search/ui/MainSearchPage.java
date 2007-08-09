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
package com.dtsworkshop.flextools.search.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ide.ResourceUtil;

import com.dtsworkshop.flextools.codemodel.CodeModelManager;
import com.dtsworkshop.flextools.search.ClassSearcher;
import com.dtsworkshop.flextools.search.SearchQuery;
import com.dtsworkshop.flextools.search.SearchReference;

public class MainSearchPage extends DialogPage implements ISearchPage {

	public MainSearchPage() {
	}

	public MainSearchPage(String title) {
		super(title);
	}

	public MainSearchPage(String title, ImageDescriptor image) {
		super(title, image);
		// TODO Auto-generated constructor stub
	}
	
	protected ClassSearcher.LimitTo getSearchLimit() {
		for(Button limitButton : limitButtonMap.keySet()) {
			if(limitButton.getSelection()) {
				return  limitButtonMap.get(limitButton);
			}
		}
		return ClassSearcher.LimitTo.AllOccurences;
	}

	public boolean performAction() {
		Assert.isNotNull(classSearchBox);
		
		SearchQuery searchQuery = new SearchQuery(classSearchBox.getText());
		ClassSearcher.LimitTo searchLimit = getSearchLimit();
		searchQuery.getSearcher()
			.setLimit(searchLimit)
			.setCaseSensitive(caseSensitiveButton.getSelection())
			.setExactMatch(exactMatchButton.getSelection())
			.setSearchText(classSearchBox.getText());
		
		//NewSearchUI.runQuery(new SearchQuery(classSearchBox.getText()));
		NewSearchUI.runQueryInBackground(searchQuery);
		return true;
	}

	ISearchPageContainer container;
	private Text classSearchBox;
	
	public void setContainer(ISearchPageContainer container) {
		// TODO Auto-generated method stub
		this.container = container;
	}
	
	private Map<Button, ClassSearcher.LimitTo> limitButtonMap = new HashMap<Button, ClassSearcher.LimitTo>();
	Button caseSensitiveButton;
	Button exactMatchButton;
	
	public void createControl(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout mainLayout = new GridLayout(2, false);
		mainLayout.horizontalSpacing = 10;
		
		main.setLayout(mainLayout);
		main.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
		GridDataFactory itemGridFactory = GridDataFactory.createFrom(new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 1));
		classSearchBox = new Text(main, SWT.BORDER);
		classSearchBox.setLayoutData(itemGridFactory.create());
		caseSensitiveButton = new Button(main, SWT.CHECK);
		caseSensitiveButton.setLayoutData(itemGridFactory.create());
		caseSensitiveButton.setText("Case sensitive");
		caseSensitiveButton.setEnabled(true);
		
		exactMatchButton = new Button(main, SWT.CHECK);
		exactMatchButton.setLayoutData(itemGridFactory.create());
		exactMatchButton.setText("Exact match");
		exactMatchButton.setEnabled(true);
		
		exactMatchButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				caseSensitiveButton.setEnabled(!exactMatchButton.getSelection());
			}
			
		});
		exactMatchButton.setSelection(false);
		//TODO: Need to implement restoring of these settings from preferences?
		caseSensitiveButton.setSelection(false);
		
		GridDataFactory columnGridData = GridDataFactory.createFrom(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
		
//		Composite searchOptionsComposite = new Composite(main, SWT.NONE);
//		searchOptionsComposite.setLayoutData(columnGridData.create());
		GridLayout optionsLayout = new GridLayout();
		
		optionsLayout.numColumns = 2;
	
		GridLayout limitLayout= new GridLayout();
		limitLayout.numColumns = 2;
		
		createSearchForOptions(main, columnGridData, limitLayout);
		createSearchLimits(main, columnGridData, limitLayout);
		
		setControl(main);
		
	}

	private void createSearchForOptions(Composite main, GridDataFactory columnGridData, GridLayout limitLayout) {
		Group searchFor = new Group(main, SWT.NONE);
		searchFor.setText("Search For");
		searchFor.setLayoutData(columnGridData.create());
		searchFor.setLayout(limitLayout);
		
		Button searchForType = createCheckbox(searchFor, "Type", true);
		searchForType.setEnabled(true);
		searchForType.setSelection(true);
		Button searchForMethod = createCheckbox(searchFor, "Method", false);
		searchForMethod.setEnabled(false);
		Button searchForField = createCheckbox(searchFor, "Field", false);
		searchForField.setEnabled(false);
		Button searchForPackage = createCheckbox(searchFor, "Package", false);
		searchForPackage.setEnabled(false);
		Button searchForConstructor = createCheckbox(searchFor, "Constructor", false);
		searchForConstructor.setEnabled(false);
		//Type, method, field, package, constructor
	}

	private void createSearchLimits(Composite main, GridDataFactory columnGridData, GridLayout limitLayout) {
		Group limits = new Group(main, SWT.NONE);

		limits.setText("Limit To");
		limits.setLayoutData(columnGridData.create());
		limits.setLayout(limitLayout);
		Button searchForDecls = createCheckbox(limits, "Delcarations", false);
		Button searchForImplementors = createCheckbox(limits, "Implementors", false);
		Button searchForReferences = createCheckbox(limits, "References", false);
		searchForReferences.setEnabled(false);
		Button searchForAll = createCheckbox(limits, "All occurences", true);
		searchForAll.setSelection(true);
		
		limitButtonMap.put(searchForDecls, ClassSearcher.LimitTo.Declarations);
		limitButtonMap.put(searchForImplementors, ClassSearcher.LimitTo.Implementations);
		limitButtonMap.put(searchForReferences, ClassSearcher.LimitTo.References);
		limitButtonMap.put(searchForAll, ClassSearcher.LimitTo.AllOccurences);
	}

	private Button createCheckbox(Group parent, String text, boolean initialState) {
		Button newButton = createCheckbox(parent, text);
		newButton.setSelection(initialState);
		return newButton;
	}
	
	private Button createCheckbox(Group parent, String text) {
		Button searchForType;
		searchForType = new Button(parent, SWT.RADIO);
		searchForType.setText(text);
		return searchForType;
	}

}
