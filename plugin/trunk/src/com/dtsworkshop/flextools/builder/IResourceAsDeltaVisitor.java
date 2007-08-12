package com.dtsworkshop.flextools.builder;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;

/**
 * Interface defining a client that will participate in the Flex Tools build 
 * process. Despite the AS in the name, the visitor may choose to process
 * Flex MXML files as well (in fact, currently there is no FT difference 
 * between the two).
 * 
 * 
 * @author Ollie
 *
 */
public interface IResourceAsDeltaVisitor extends IResourceDeltaVisitor, IResourceVisitor {
	/**
	 * Returns whether the supplied resource can be visited
	 * 
	 * @param delta The delta being visited
	 * 
	 * @return True - the visitor can visit the resource, false otherwise
	 */
	boolean canVisit(IResourceDelta delta);
	/**
	 * Process the fact that the supplied resource has changed in some way.
	 * 
	 * @param resource The resource that has changed
	 * 
	 * @return Whether or not to process the children (true - yes, process them; false, no)
	 */
	boolean changed(IResource resource);
	/**
	 * Visit method to visit the delta.
	 * 
	 * @param delta The delta that is being visited
	 * @return Whether or not to process the children (true - yes, process them; false, no)
	 */
	boolean visit(IResourceDelta delta);
	
	/**
	 * Sets the project that's being processed at the moment.
	 * 
	 * @param project The project currently being built
	 */
	void setProject(IProject project);
}
