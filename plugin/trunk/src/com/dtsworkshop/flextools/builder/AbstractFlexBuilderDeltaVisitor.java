package com.dtsworkshop.flextools.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;

import com.dtsworkshop.flextools.Activator;

/**
 * Base implementation for a Flex Tools delta visitor. Clients implementing
 * class are provided with a series of helper methods to ensure the creation
 * of a visitor is easy.
 *
 * @author otupman
 *
 */
public abstract class AbstractFlexBuilderDeltaVisitor  implements IResourceAsDeltaVisitor {
	private IProject project;

	/**
	 * Sets the project that this delta visitor is visiting resources for.
	 *
	 * @param project The project that will be used.
	 */
	public void setProject(IProject project) {
		this.project = project;
	}

	/**
	 * Gets the project this visitor is visiting.
	 */
	public IProject getProject() {
		return this.project;
	}

	public AbstractFlexBuilderDeltaVisitor() {
	}

	public abstract boolean canVisit(IResource delta);

	public abstract boolean changed(IResource resource);

	public boolean visit(IResource resource) {
		if(resource instanceof IFolder) {
			return true;
		}

		if(resource instanceof IFile && canVisit(resource)) {
			changed(resource);
		}
		return true;
	}

	public boolean visit(IResourceDelta delta) {
		boolean skipChildren = true;

		IResource resource = delta.getResource();
		if(resource instanceof IFolder) {
			return true;
		}
		if(canVisit(resource)) {
			IFile file = (IFile)resource;

			switch(delta.getKind()) {
			case IResourceDelta.CHANGED:
				removeBuildState(file);
			case IResourceDelta.ADDED:
				skipChildren = changed(file);
				break;
			case IResourceDelta.REMOVED:
				removeBuildState(file);
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
