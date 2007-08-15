package com.dtsworkshop.flextools.actions.refactor;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.dtsworkshop.flextools.FlexToolsLog;
import com.dtsworkshop.flextools.refactoring.ClassNameRefactoring;
import com.dtsworkshop.flextools.refactoring.ui.RenameClassWizard;

public class RenameClassAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	public RenameClassAction() {
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
	}

	public void run(IAction action) {
		ClassNameRefactoring refactorer = new ClassNameRefactoring();
		int info = RefactoringWizard.DIALOG_BASED_USER_INTERFACE;
		RenameClassWizard wizard = new RenameClassWizard(refactorer, info);
		RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(wizard);
		String titleForFailedChecks = "failed";
		try {
			op.run(window.getShell(), titleForFailedChecks);
		} catch (InterruptedException e) {
			e.printStackTrace();
			FlexToolsLog.logError(String.format("Error running the class name refactorer"), e);
		}
		
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
