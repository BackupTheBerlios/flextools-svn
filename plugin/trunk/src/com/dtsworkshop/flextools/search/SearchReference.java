package com.dtsworkshop.flextools.search;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

import com.adobe.flexbuilder.codemodel.internal.tree.TypeNode;
import com.dtsworkshop.flextools.model.NodeType;

public class SearchReference {
	protected IFile filePath;
	protected IProject project;
	protected int from;
	protected int to;
	protected String description;
	protected NodeType nodeType;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public IFile getFilePath() {
		return filePath;
	}
	public void setFilePath(IFile filePath) {
		this.filePath = filePath;
	}
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public IProject getProject() {
		return project;
	}
	public void setProject(IProject project) {
		this.project = project;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public NodeType getNodeType() {
		return nodeType;
	}
	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}
	
	
	
}
