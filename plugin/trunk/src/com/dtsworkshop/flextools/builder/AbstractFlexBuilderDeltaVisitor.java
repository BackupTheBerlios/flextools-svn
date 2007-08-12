package com.dtsworkshop.flextools.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;

import com.dtsworkshop.flextools.Activator;

/**
 * Default implementation for 
 * 
 * @author Ollie
 *
 */
public abstract class AbstractFlexBuilderDeltaVisitor  implements IResourceAsDeltaVisitor {
	private IProject project;
	
	public void setProject(IProject project) {
		this.project = project;
	}
	
	public IProject getProject() {
		return this.project;
	}

	public AbstractFlexBuilderDeltaVisitor() {
	}
	
	public abstract boolean canVisit(IResourceDelta delta);

	public abstract boolean changed(IResource resource);
	
	public boolean visit(IResource resource) {
		if(resource instanceof IFile) {
			changed(resource);
		}
		return true;
	}
	
	public boolean visit(IResourceDelta delta) {
		boolean skipChildren = true;
		if(canVisit(delta)) {
			IFile resource = (IFile)delta.getResource();
			
			switch(delta.getKind()) {
			case IResourceDelta.CHANGED:
				removeBuildState(resource);
			case IResourceDelta.ADDED:
				skipChildren = changed(resource);
				break;
			case IResourceDelta.REMOVED:
				removeBuildState(resource);
				break;
			} 
		//	skipChildren = doVisit(delta);
		}
		return skipChildren;
	}

	private void removeBuildState(IFile resource) {
		Activator.getStateManager().removeBuildState(getProject(), resource);
	}
	
}
