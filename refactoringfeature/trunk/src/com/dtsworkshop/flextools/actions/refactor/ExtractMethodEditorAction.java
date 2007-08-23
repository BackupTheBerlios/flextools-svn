package com.dtsworkshop.flextools.actions.refactor;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.internal.tree.FunctionNode;
import com.adobe.flexbuilder.codemodel.project.IProject;
import com.adobe.flexbuilder.codemodel.tree.IASNode;
import com.adobe.flexbuilder.codemodel.tree.IFileNode;
import com.adobe.flexbuilder.editors.common.document.IFlexDocument;
import com.adobe.flexbuilder.editors.common.editor.AbstractFlexEditor;
import com.adobe.flexbuilder.project.FlexProjectManager;
import com.adobe.flexbuilder.project.IFlexProject;
import com.dtsworkshop.flextools.FlexToolsLog;
import com.dtsworkshop.flextools.refactoring.CodeInfoHelper;

public class ExtractMethodEditorAction implements IEditorActionDelegate {
	private static Logger logger = Logger.getLogger("ExtractMethodEditorAction");
	private AbstractFlexEditor editor;
	
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		// TODO Auto-generated method stub
		logger.info("setActiveEditor()");
		editor = (AbstractFlexEditor)targetEditor;
		
	}

	public void run(IAction action) {
		// TODO Auto-generated method stub
		logger.info("run()");
		IFlexDocument doc = editor.getCurrentActiveDocument();
		IFlexProject flexProject = FlexProjectManager.getFlexProject(doc.getIFile());
		synchronized (CMFactory.getLockObject())
        {
			IFileNode node = getDocumentAsNode(doc);
			
			if(node != null) {
				logger.info(node.getNodeStackTrace());
				logger.info(String.format("Got node [%s]", new Object[]{doc.getIFile().getName()}));
				visitedChildren = new ArrayList<IASNode>();
				StringBuilder builder = new StringBuilder();
				dumpNodes(doc, node, builder);
				System.out.println(builder.toString());
			}
			else {
				logger.info("Node is null.");
			}
        }
	}

	private IFileNode getDocumentAsNode(IFlexDocument doc) {
		IProject project = CMFactory.getManager().getProjectForDocument(doc);
		IFileNode node = project.findFileNodeInProject(doc.getIFile().getLocation());
		return node;
	}
	
	private ArrayList<IASNode> visitedChildren;
	
	public static void dumpNodes(IDocument document, IASNode startNode, StringBuilder builder) {
		builder.append(String.format(
			"<%s startPos='%d' endPos='%d' className='%s'>", 
			startNode.getNodeType(),
			startNode.getStart(),
			startNode.getEnd(),
			startNode.getClass().toString()
		));
		String nodeText = "";
				
		try {
			nodeText = document.get(startNode.getStart(), startNode.getEnd() - startNode.getStart());
		} catch (BadLocationException e) {
		}
		builder.append(String.format("<contents><![CDATA[%s]]></contents>", nodeText));
		
		IASNode [] children = startNode.getChildren();
		builder.append("<children>");
		for(IASNode child : children) {
			try {
				dumpNodes(document, child, builder);
			}
			catch(RuntimeException ex) {
				builder.append(String.format("<error when='dumping children'><![CDATA[%s]]></error>", ex.getMessage()));
			}			
		}
		builder.append("</children>");
		builder.append(String.format("</%s>", startNode.getNodeType()));
	}
	
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		//logger.info("selectionChanged()");
		action.setEnabled(true);
		if(selection instanceof TextSelection) {
			TextSelection sel = (TextSelection)selection;
//			logger.info(String.format("Selection: [%s] offset: %d", 
//				sel.getText(), sel.getOffset()
//			));
			IASNode currentNode = CodeInfoHelper.getInstance().findNode(
				editor.getCurrentActiveDocument(),
				sel.getOffset()
			);
//			logger.info(String.format("Current node: %s", currentNode.toString()));
			try {
				int fromLine = editor.getCurrentActiveDocument().getLineOfOffset(currentNode.getStart());
				int toLine = editor.getCurrentActiveDocument().getLineOfOffset(currentNode.getStart());
				String nodeData = editor.getCurrentActiveDocument().get(
						currentNode.getStart(), 
						currentNode.getEnd() - currentNode.getStart()
				);
//				logger.info(String.format("Node is from lines %d to %d", fromLine, toLine));
//				logger.info(String.format("Node's text is: \"%s\"", nodeData));
//				logger.info(String.format("Node type is: %s", currentNode.getNodeType()));
//				logger.info("Dump complete.");
			} catch (BadLocationException e) {
				e.printStackTrace();
				FlexToolsLog.logError(String.format("Error playing with document ranges"), e);
			}
		}
//		logger.info(selection.toString());
	}

}
