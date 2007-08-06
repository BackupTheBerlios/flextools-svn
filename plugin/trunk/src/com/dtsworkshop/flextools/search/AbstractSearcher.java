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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.Assert;

import com.dtsworkshop.flextools.codemodel.IBuildStateVisitor;
import com.dtsworkshop.flextools.codemodel.ModelConstants;
import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.BuildStateDocument;
import com.dtsworkshop.flextools.model.NodeType;

public abstract class AbstractSearcher implements IBuildStateVisitor {

	protected List<SearchReference> matches;
	protected String namespace = ModelConstants.xmlNamespace;
	protected String query;
	protected IWorkspace workspace;

	public AbstractSearcher(IWorkspace workspace) {
		Assert.isNotNull(workspace);
		this.workspace = workspace;
		query = initialiseQuery();
		matches = new ArrayList<SearchReference>(100);
	}
	
	protected String getNamespaceDecl() {
		return String.format(
				"declare namespace mod='%s';",
			namespace
		);		
	}
	
	protected abstract String initialiseQuery();
	
	public void setNamespace(String newNamespace) {
		this.namespace = newNamespace;
	}

	public List<SearchReference> getMatches() {
		return matches;
	}

	protected IFile getBuildStateFile(BuildStateDocument document, IProject project) {
		IFile containingFile = project.getFile(document.getBuildState().getFile());
		return containingFile;
	}
	
	protected SearchReference referenceFromNodeType(IProject project, IFile containingFile, BuildReference type) {
		SearchReference newRef = new SearchReference();
		newRef.setFrom(type.getStartPos());
		newRef.setTo(type.getEndPos());
		
		newRef.setProject(project);
		newRef.setFilePath(containingFile);
		newRef.setNodeType(type);
		return newRef;
	}
}
