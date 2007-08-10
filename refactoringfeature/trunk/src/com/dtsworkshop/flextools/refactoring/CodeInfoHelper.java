package com.dtsworkshop.flextools.refactoring;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.jface.text.IDocument;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.project.IProject;
import com.adobe.flexbuilder.codemodel.tree.IASNode;
import com.adobe.flexbuilder.codemodel.tree.IFileNode;
import com.adobe.flexbuilder.editors.common.document.IFlexDocument;

public class CodeInfoHelper {
	private static CodeInfoHelper instance;
	
	static {
		instance = new CodeInfoHelper();
	}
	
	public static CodeInfoHelper getInstance() {
		return instance;
	}
	
	public IFileNode getDocumentAsNode(IFlexDocument doc) {
		IProject project = CMFactory.getManager().getProjectForDocument(doc);
		IFileNode node = project.findFileNodeInProject(doc.getIFile().getLocation());
		return node;
	}
	 
	private class SearchInfo {
		public IASNode node;
		public int childCount = 0;
		public SearchInfo(IASNode node) {
			this.node = node;
		}
	}
	
	public IASNode findNode(IDocument document, int position) {
		IASNode fileNode = getDocumentAsNode((IFlexDocument)document);
		Stack<IASNode> nodes = new Stack<IASNode>();
		nodes.push(fileNode);
		IASNode targetNode = null;
		
		while(targetNode == null) {
			IASNode currentNode = nodes.peek();
			
			boolean hasChildren = currentNode.getChildCount() > 0;
			if(hasChildren){
				for(IASNode child : currentNode.getChildren()) {
					boolean isInNodeRange = position >= child.getStart() && position < child.getEnd();
					if(isInNodeRange) {
						nodes.push(child);
						break;
					}
				}
				boolean isSameNode = currentNode.equals(nodes.peek());
				if(isSameNode) {
				// Falling through means that the target is whitespace.
					targetNode = currentNode;
				}
			}
			else {
				// As all nodes in the stack contain the target range and
				// this one has no children, it must be the found node.
				targetNode = nodes.peek();
			}
		}
		return targetNode;
	}






}
