package com.dtsworkshop.flextools.actions.refactor;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.WorkspaceAction;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.project.IProject;
import com.adobe.flexbuilder.codemodel.tree.IASNode;
import com.adobe.flexbuilder.codemodel.tree.IExpressionNode;
import com.adobe.flexbuilder.codemodel.tree.IFileNode;
import com.adobe.flexbuilder.codemodel.tree.IScopedNode;
import com.adobe.flexbuilder.editors.actionscript.ActionScriptEditor;
import com.adobe.flexbuilder.editors.common.document.IFlexDocument;
import com.adobe.flexbuilder.editors.common.editor.AbstractFlexEditor;
import com.dtsworkshop.flextools.FlexToolsLog;
import com.dtsworkshop.flextools.builder.processors.ProcessorHelper;
import com.dtsworkshop.flextools.refactoring.ClassNameRefactoring;
import com.dtsworkshop.flextools.refactoring.ui.RenameClassWizard;

public class RenameClassActionFromEditor implements IEditorActionDelegate {
	private IEditorPart editor;
	
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.editor = targetEditor;
	}
	
	private IASNode findAsNodeByOffset(IASNode root, int offset) {
		IASNode [] children = root.getChildren();
		boolean isLeafNode = children.length == 0;
		if(isLeafNode) { // Is a leaf node, so it's the target.
			return root;
		}
		IASNode foundNode = null;
		for(IASNode child : children) {
			if(offset >= child.getStart() && offset <= child.getEnd()) {
				foundNode = findAsNodeByOffset(child, offset);
				break;
			}
		}
		return foundNode;
	}
	
	private String getQualifiedName(int offset) {
		String qualifiedName = null;
		AbstractFlexEditor asEditor = (AbstractFlexEditor)this.editor;
		IDocument doc = asEditor.getCurrentActiveDocument();
		synchronized (CMFactory.getLockObject()) {
			IProject project = CMFactory.getManager().getProjectForDocument(doc);
			IPath path = CMFactory.getManager().getPathForDocument(doc);
			IFileNode fileNode = project.findFileNodeInProject(path);
			fileNode.getScope();
			IASNode containingNode = findAsNodeByOffset(fileNode, offset);
			if(containingNode instanceof IExpressionNode) {
				IDefinition def = ((IExpressionNode)containingNode).getDefinition();
				if(def == null) {
					throw new RuntimeException("Definition isn't present.");
				}
				qualifiedName = def.getQualifiedName();
			}
		}
		return qualifiedName;
	}

	public void run(IAction action) {
		if(!(lastSelected instanceof TextSelection)) {
			FlexToolsLog.logWarning("Text selection isn't instance of TextSelection.");
			return;
		}
		TextSelection castedSelection = (TextSelection)lastSelected;
		String classQualifiedName = getQualifiedName(castedSelection.getOffset());
		if(classQualifiedName == null) {
			//TODO: output useful error message to the user!
			System.out.println("The user hadn't selected any text... I think...");
			FlexToolsLog.logWarning(
				String.format(
					"No text selected so cannot perform rename." +
					"Offset is: %d", castedSelection.getOffset()					
			));
			return;
		}
		IWorkbenchWindow window = editor.getSite().getWorkbenchWindow();
		
		ClassNameRefactoring refactorer = new ClassNameRefactoring();
		refactorer.setQualifiedName(classQualifiedName);
		refactorer.setOldShortName(ProcessorHelper.getLocalName(classQualifiedName));
		int info = RefactoringWizard.DIALOG_BASED_USER_INTERFACE;
		RenameClassWizard wizard = new RenameClassWizard(refactorer, info);
		
		RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(wizard);
		String titleForFailedChecks = "failed";
		try {
			Shell shell = window.getShell();
			op.run(shell, titleForFailedChecks);
		} catch (InterruptedException e) {
			e.printStackTrace();
			FlexToolsLog.logError(String.format("Error running the class rename refactorer"), e);
		}
	}
	private ISelection lastSelected = null;
	
	public void selectionChanged(IAction action, ISelection selection) {
		lastSelected = selection;
	}

}
