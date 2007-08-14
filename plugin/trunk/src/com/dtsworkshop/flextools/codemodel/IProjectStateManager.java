package com.dtsworkshop.flextools.codemodel;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

import com.dtsworkshop.flextools.model.BuildStateDocument;

/**
 * Interface defining the API for interacting with a component that manages
 * the model state for projects.
 * 
 * @author otupman
 *
 */
public interface IProjectStateManager {

	/**
	 * Determines whether the state manager is currently managing the
	 * supplied project.
	 * 
	 * @param project The project to test for
	 * @return If the manager is managing the project, true; otherwise false
	 */
	public abstract boolean isProjectManaged(IProject project);
	
	/**
	 * Tells the state manager to perform any initialisation required.
	 * This is guaranteed to be called once the plugin is loading (i.e.
	 * so the workspace is ready).
	 * @deprecated Pleas use initialiseProject(IProject project, IProgressMonitor monitor)
	 */
	public abstract void initialise(IProgressMonitor monitor);
	
	/**
	 * Initialises an individual project.
	 * 
	 * @param project The project to initialise
	 * @param monitor Monitor to report progress to the user
	 */
	public abstract void initialiseProject(IProject project, IProgressMonitor monitor);
	
	/**
	 * Gets some information about the model
	 * 
	 * @return The model information
	 */
	public abstract ModelInfo getInfo();

	/**
	 * Method to allow visitors to be navigated through the build state
	 * 
	 * @param visitor The visitor to doing the visiting
	 * @param monitor Monitor to record progress, may be null
	 */
	public abstract void acceptVisitor(IBuildStateVisitor visitor,
			IProgressMonitor monitor);

	/**
	 * Registers that a project needs to have a model managed.
	 *  
	 * @param project The project to add management to
	 */
	public abstract void registerProject(IProject project);

	/**
	 * Remove the model manager (and in turn state) from the project.
	 * 
	 * @param project The project to remove the state from
	 * 
	 * @return True - removal successful, all resources cleared up; false if an error occurred
	 */
	public abstract boolean removeProjectState(IProject project);

	/**
	 * Removes the build state for a specific resource.
	 * 
	 * @param project The containing project
	 * @param resource The resource to remove
	 */
	public abstract void removeBuildState(IProject project, IFile resource);

	/**
	 * Stores a build state
	 * @param state The state to store
	 */
	public abstract void storeBuildState(BuildStateDocument state);

}
