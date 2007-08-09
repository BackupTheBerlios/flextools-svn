package com.dtsworkshop.flextools.actions.refactor;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

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
		// TODO Auto-generated constructor stub
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		// TODO Auto-generated method stub

	}

	public void run(IAction action) {
		// TODO Auto-generated method stub
		ClassNameRefactoring refactorer = new ClassNameRefactoring();
		int info = RefactoringWizard.DIALOG_BASED_USER_INTERFACE;
		RenameClassWizard wizard = new RenameClassWizard(refactorer, info);
		RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(wizard);
		String titleForFailedChecks = "failed";
		try {
			op.run(window.getShell(), titleForFailedChecks);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
