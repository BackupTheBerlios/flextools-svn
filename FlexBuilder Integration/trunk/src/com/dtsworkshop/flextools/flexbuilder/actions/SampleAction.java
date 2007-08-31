package com.dtsworkshop.flextools.flexbuilder.actions;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.jface.dialogs.MessageDialog;

//import uk.co.badgersinfoil.metaas.ActionScriptFactory;
//import uk.co.badgersinfoil.metaas.ActionScriptParser;
//import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.project.IProject;
import com.adobe.flexbuilder.codemodel.tree.IFileNode;
import com.adobe.flexbuilder.project.IFlexProject;
import com.dtsworkshop.flextools.flexbuilder.builder.ModelProcessor;
import com.dtsworkshop.flextools.model.BuildStateDocument;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class SampleAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	private static Logger log = Logger.getLogger(SampleAction.class); 
	/**
	 * The constructor.
	 */
	public SampleAction() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		MessageDialog.openInformation(
			window.getShell(),
			"Flex Tools FlexBuilder Integration",
			"Hello, Eclipse world");
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		log.debug("Selection changed");
//		if(selection instanceof TreeSelection) {
//			Object selectedItem = ((TreeSelection)selection).getFirstElement();
//			if(selectedItem instanceof IFile) {
//				IFile file = (IFile)selectedItem;
//				
//				
//				FileReader reader = null;
//				try {
//					reader = new FileReader(file.getLocation().toFile());
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//					return;
//				}
////				ActionScriptFactory factory = new ActionScriptFactory();
////				ActionScriptParser parser = factory.newParser();
////				try {
////					ASCompilationUnit result = parser.parse(reader);
////					System.out.println("Got result");
////				} catch (RuntimeException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
//				
////				ModelProcessor processor = new ModelProcessor();
////				IFileNode fileNode = null;
////				
////				synchronized (CMFactory.getLockObject()) {
////					IProject project =  CMFactory.getManager().getProjectFor(file.getProject());
////					fileNode = project.findFileNodeInProject(file.getLocation());
////					BuildStateDocument doc = processor.getStateDocument(fileNode, file);
////					System.out.println("Boo");
////				}
//			}
//		}
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}