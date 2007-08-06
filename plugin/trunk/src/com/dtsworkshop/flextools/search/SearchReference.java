/**
*	Copyright (C) Oliver B. Tupman, 2007.
*	
*	This file is part of the Flex Tools Project.
*	
*	The Flex Tools Project is free software; you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation; either version 3 of the License, or
*	(at your option) any later version.
*	
*	The Flex Tools Project is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*	GNU General Public License for more details.
*	
*	You should have received a copy of the GNU General Public License
*	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.dtsworkshop.flextools.search;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

import com.adobe.flexbuilder.codemodel.internal.tree.TypeNode;
import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.NodeType;

public class SearchReference {
	protected IFile filePath;
	protected IProject project;
	protected int from;
	protected int to;
	protected String description;
	protected BuildReference nodeType;
	
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
	public BuildReference getNodeType() {
		return nodeType;
	}
	public void setNodeType(BuildReference nodeType) {
		this.nodeType = nodeType;
	}
	
	
	
}
