package com.dtsworkshop.flextools.refactoring.ui;

import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class TypeNameInputPage extends UserInputWizardPage {

	public TypeNameInputPage(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void createControl(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		Text classNameInput = new Text(main, SWT.BORDER);
		setControl(main);
	}

}
