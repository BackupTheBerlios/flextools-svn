package com.dtsworkshop.flextools.actions;

import java.util.logging.Logger;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class ExtractMethodAction implements IWorkbenchWindowActionDelegate {
	private static Logger logger = Logger.getLogger(ExtractMethodAction.class.getName());
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		logger.info("Init()");
	}

	public void run(IAction action) {
		// TODO Auto-generated method stub
		logger.info("Run()");
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		logger.info("selectionChanged()");
	}

}
