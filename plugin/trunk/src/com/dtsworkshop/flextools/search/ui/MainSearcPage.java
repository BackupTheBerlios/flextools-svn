package com.dtsworkshop.flextools.search.ui;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class MainSearcPage extends DialogPage implements ISearchPage {

	public MainSearcPage() {
		// TODO Auto-generated constructor stub
	}

	public MainSearcPage(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public MainSearcPage(String title, ImageDescriptor image) {
		super(title, image);
		// TODO Auto-generated constructor stub
	}

	public boolean performAction() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setContainer(ISearchPageContainer container) {
		// TODO Auto-generated method stub

	}

	public void createControl(Composite parent) {
		Group searchTextGroup = new Group(parent, SWT.BORDER);
		Text searchBox = new Text(searchTextGroup, SWT.BORDER);
	}

}
