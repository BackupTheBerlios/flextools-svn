package com.dtsworkshop.flextools.search.ui;

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
		// TODO Auto-generated constructor stub
	}

	public MainSearchPage(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public MainSearchPage(String title, ImageDescriptor image) {
		super(title, image);
		// TODO Auto-generated constructor stub
	}

	public boolean performAction() {
		Assert.isNotNull(classSearchBox);
		
		SearchQuery searchQuery = new SearchQuery(classSearchBox.getText());
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

	public void createControl(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout mainLayout = new GridLayout(2, false);
		mainLayout.horizontalSpacing = 10;
		
		main.setLayout(mainLayout);
		main.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
		GridDataFactory itemGridFactory = GridDataFactory.createFrom(new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 1));
		classSearchBox = new Text(main, SWT.BORDER);
		classSearchBox.setLayoutData(itemGridFactory.create());
		Button caseSensitiveButton = new Button(main, SWT.CHECK);
		caseSensitiveButton.setLayoutData(itemGridFactory.create());
		caseSensitiveButton.setText("Case sensitive");
		caseSensitiveButton.setEnabled(false); //TODO: Enable case sensitive/insensitive searches.
		
		GridDataFactory columnGridData = GridDataFactory.createFrom(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
		
//		Composite searchOptionsComposite = new Composite(main, SWT.NONE);
//		searchOptionsComposite.setLayoutData(columnGridData.create());
		GridLayout optionsLayout = new GridLayout();
		
		optionsLayout.numColumns = 2;
	
		GridLayout limitLayout= new GridLayout();
		limitLayout.numColumns = 2;
		
		Group searchFor = new Group(main, SWT.NONE);
		searchFor.setText("Search For");
		searchFor.setLayoutData(columnGridData.create());
		searchFor.setLayout(limitLayout);
		
		Button searchForType = createCheckbox(searchFor, "Type", true);
		Button searchForMethod = createCheckbox(searchFor, "Method", false);
		Button searchForField = createCheckbox(searchFor, "Field", false);
		Button searchForPackage = createCheckbox(searchFor, "Package", false);
		Button searchForConstructor = createCheckbox(searchFor, "Constructor", false);
		//Type, method, field, package, constructor
		
		Group limits = new Group(main, SWT.NONE);

		limits.setText("Limit To");
		limits.setLayoutData(columnGridData.create());
		limits.setLayout(limitLayout);
		Button searchForDecls = createCheckbox(limits, "Delcarations", false);
		Button searchForImplementors = createCheckbox(limits, "Implementors", false);
		Button searchForReferences = createCheckbox(limits, "References", false);
		Button searchForAll = createCheckbox(limits, "All occurences", true);
		// declarations, implementator, references, all occurences, read accesses, write accesses
		
		
		
		
		setControl(main);
		
	}

	private Button createCheckbox(Group parent, String text, boolean initialState) {
		Button newButton = createCheckbox(parent, text);
		newButton.setSelection(initialState);
		return newButton;
	}
	
	private Button createCheckbox(Group parent, String text) {
		Button searchForType;
		searchForType = new Button(parent, SWT.CHECK);
		searchForType.setText(text);
		return searchForType;
	}

}
