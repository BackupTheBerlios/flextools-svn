package com.dtsworkshop.flextools.search.ui;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
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
		Group searchTextGroup = new Group(parent, SWT.BORDER);
		GridLayout topLayout = new GridLayout();
		topLayout.marginWidth = 0;
		topLayout.marginHeight = 0;
		topLayout.numColumns = 1;
		searchTextGroup.setLayout(topLayout);
		classSearchBox = new Text(searchTextGroup, SWT.BORDER);
		setControl(searchTextGroup);
	}

}
