package com.dtsworkshop.flextools.refactoring.ui;

import org.apache.log4j.Logger;
import org.eclipse.debug.internal.ui.views.memory.SetPaddedStringPreferencePage;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

import com.dtsworkshop.flextools.refactoring.ClassNameRefactoring;

public class RenameClassWizard extends RefactoringWizard {

	private static Logger log = Logger.getLogger(RenameClassWizard.class);
	
	public RenameClassWizard(Refactoring refactoring, int flags) {
		super(refactoring, flags);
		log.debug("Created for refactoring " + refactoring.getName());
	}

	@Override
	protected void addUserInputPages() {
		log.debug("Created type input page.");
		setDefaultPageTitle("Rename class");
		addPage(new TypeNameInputPage("Rename class", (ClassNameRefactoring)getRefactoring()));
	}

}
