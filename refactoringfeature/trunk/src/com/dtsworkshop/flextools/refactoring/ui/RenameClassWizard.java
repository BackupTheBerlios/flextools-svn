package com.dtsworkshop.flextools.refactoring.ui;

import org.eclipse.debug.internal.ui.views.memory.SetPaddedStringPreferencePage;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

import com.dtsworkshop.flextools.refactoring.ClassNameRefactoring;

public class RenameClassWizard extends RefactoringWizard {

	public RenameClassWizard(Refactoring refactoring, int flags) {
		super(refactoring, flags);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addUserInputPages() {
		
		setDefaultPageTitle("Rename class");
		addPage(new TypeNameInputPage("Rename class", (ClassNameRefactoring)getRefactoring()));
	}

}
