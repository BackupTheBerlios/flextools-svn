package com.dtsworkshop.flextools.actions.refactor;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.WorkspaceAction;

import com.dtsworkshop.flextools.refactoring.AsRefactoring;
import com.dtsworkshop.flextools.refactoring.ui.RenameClassWizard;

public class RenameClassActionFromEditor implements IEditorActionDelegate {
	private IEditorPart editor;
	
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.editor = targetEditor;
	}

	public void run(IAction action) {
		IWorkbenchWindow window = editor.getSite().getWorkbenchWindow();
		
		AsRefactoring refactorer = new AsRefactoring();
		int info = RefactoringWizard.DIALOG_BASED_USER_INTERFACE;
		RenameClassWizard wizard = new RenameClassWizard(refactorer, info);
		RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(wizard);
		String titleForFailedChecks = "failed";
		try {
			Shell shell = window.getShell();
			op.run(shell, titleForFailedChecks);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {

	}

}
