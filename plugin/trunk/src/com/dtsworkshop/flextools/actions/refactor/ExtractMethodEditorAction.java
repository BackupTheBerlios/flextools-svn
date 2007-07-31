package com.dtsworkshop.flextools.actions.refactor;

import java.util.logging.Logger;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import com.adobe.flexbuilder.editors.actionscript.ActionScriptEditor;

public class ExtractMethodEditorAction implements IEditorActionDelegate {
	private static Logger logger = Logger.getLogger("ExtractMethodEditorAction");
	private ActionScriptEditor editor;
	
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		// TODO Auto-generated method stub
		logger.info("setActiveEditor()");
		editor = (ActionScriptEditor)targetEditor;
	}

	public void run(IAction action) {
		// TODO Auto-generated method stub
		logger.info("run()");
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		logger.info("selectionChanged()");
		action.setEnabled(true);
	}

}
