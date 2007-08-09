package com.dtsworkshop.flextools.refactoring.ui;

import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

public class RenameClassWizard extends RefactoringWizard {

	public RenameClassWizard(Refactoring refactoring, int flags) {
		super(refactoring, flags);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addUserInputPages() {
		// TODO Auto-generated method stub
		addPage(new TypeNameInputPage("Rename class"));
	}

}
