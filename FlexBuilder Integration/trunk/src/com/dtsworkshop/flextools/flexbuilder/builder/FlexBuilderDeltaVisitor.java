/**
 *	Copyright (C) Ollie, 2007.
 *	
 *	This file is part of the Flex Tools Project.
 *
 *    The Flex Tools Project is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    The Flex Tools Project is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.dtsworkshop.flextools.flexbuilder.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IPath;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.definitions.IASScope;
import com.adobe.flexbuilder.codemodel.tree.IFileNode;
import com.dtsworkshop.flextools.Activator;
import com.dtsworkshop.flextools.builder.AbstractFlexBuilderDeltaVisitor;
import com.dtsworkshop.flextools.model.BuildStateDocument;

/**
 * FlexBuilder delta visitor. Processes the parse trees provided by FlexBuilder
 * therefore introducing a direct dependency upon FB. Makes use of some
 * features marked as 'internal' by FB, so it'll raise a fair few warnings.
 * 
 * @author otupman
 *
 */
public class FlexBuilderDeltaVisitor extends AbstractFlexBuilderDeltaVisitor {

	public FlexBuilderDeltaVisitor() {
		super();
	}
	
	@Override
	public boolean canVisit(IResourceDelta delta) {
		// TODO Auto-generated method stub
		return isParseableResource(delta.getResource());
	}

	
	private boolean runWithRegisteredFile(IFile file) {
		
		IPath filePath = file.getLocation();
		com.adobe.flexbuilder.codemodel.project.IProject flexProject = CMFactory.getManager().getProjectFor(getProject());
		IFileNode fileNode = flexProject.findFileNodeInProject(filePath);
		// Force getting the scope as this appears to trigger processing of some parse items
		fileNode.getScope(); 
		ModelProcessor processor = new ModelProcessor();
		
		BuildStateDocument stateDocument = processor.getStateDocument(fileNode, file);
		Activator.getStateManager().storeBuildState(stateDocument);
		return true;
	}

	boolean runWithFile(IFile file) {
		//TODO: Implement file registration properly.
		runWithRegisteredFile(file);
		return true;
	}

	
	@Override
	public boolean changed(IResource resource) {
		IFile file = (IFile)resource;
		//FlexToolsBuilder.log.info(String.format("Visiting resource %s", resource.getName()));
		synchronized (CMFactory.getLockObject())
        {
			try {
				if(!runWithFile(file)) {
					return false;
				}
			} catch (RuntimeException e) {
				e.printStackTrace();
				return false;
			}
        }
		return true;
	}

	private boolean isParseableResource(IResource resource) {
		boolean isParseableResource = resource.getName().endsWith(".as") || resource.getName().endsWith(".mxml");
		return isParseableResource;
	}
}