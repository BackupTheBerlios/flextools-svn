package com.dtsworkshop.flextools.flexbuilder.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.project.IProject;
import com.adobe.flexbuilder.codemodel.tree.IASNode;
import com.adobe.flexbuilder.codemodel.tree.IExpressionNode;
import com.adobe.flexbuilder.codemodel.tree.IFileNode;
import com.adobe.flexbuilder.editors.common.document.IFlexDocument;
import com.adobe.flexbuilder.editors.common.editor.IFlexEditor;

/**
 * Utility base class for clients wishing to implement type-orientated editor
 * actions. This class handles all of the selection & type location for the
 * client. When the user invokes the action, it discovers the appropriate
 * information and then passes it off to the client.
 *  
 * @author otupman 
 *
 */
public abstract class AbstractTypeEditorActionDelegate {

	private IEditorPart editor;
	private static Logger log = Logger.getLogger(FindReferencesAction.class);
	private ISelection lastSelected = null;

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

	/**
	 * Simple value class representing some information about a type.
	 * Provided is the qualified name of the class, as well as a
	 * reference to the file that contains the  type.
	 * 
	 * @author otupman
	 *
	 */
	protected class TypeInfo {
		/**
		 * The full name (qualified name) of the type
		 */
		public String qualifiedName;
		/**
		 * Reference to the file that contains the type
		 */
		public IFile typeFile;
	}
	
	protected TypeInfo getQualifiedName(IEditorPart editor, int offset) {
		TypeInfo info = new TypeInfo();
		Assert.isTrue(editor instanceof IFlexEditor);
		
		IFlexEditor asEditor = (IFlexEditor)editor;
		IFlexDocument doc = (IFlexDocument)asEditor.getCurrentActiveDocument();
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
				String containingPath = def.getContainingSourceFilePath();
				IPath cPath = new Path(containingPath);
				IWorkspaceRoot wkRoot = ResourcesPlugin.getWorkspace().getRoot();
				IFile [] files = wkRoot.findFilesForLocation(cPath);
				Assert.isTrue(files.length > 0);
				//TODO: Find out when findFilesForLocation might return more than one result
				info.typeFile = files[0];
				info.qualifiedName = def.getQualifiedName();
			}
		}
		return info;
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		log.debug("Editor set for editor");
		this.editor = targetEditor;
	}

	/**
	 * Calls the method that should perform whatever action is required. The
	 * information about the type the user has selected will be passed in.
	 * 
	 * @param info Information about the type the user has selected.
	 */
	protected abstract void runWithType(TypeInfo info);
	
	public void run(IAction action) {
		TextSelection castedSelection = (TextSelection)lastSelected;
		TypeInfo selectedTypeInfo = getQualifiedName(this.editor, castedSelection.getOffset());
		String classQualifiedName = selectedTypeInfo.qualifiedName;
		
		if(classQualifiedName == null) {
			//TODO: output useful error message to the user!
			log.debug(String.format("Don't think the user has selected anything. Offset %d", castedSelection.getOffset()));
			return;
		}
		runWithType(selectedTypeInfo);
	}

	public AbstractTypeEditorActionDelegate() {
		super();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		lastSelected = selection;
	}

}