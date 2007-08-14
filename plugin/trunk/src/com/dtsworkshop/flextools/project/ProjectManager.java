package com.dtsworkshop.flextools.project;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.dtsworkshop.flextools.Activator;
import com.dtsworkshop.flextools.builder.SampleNature;
import com.dtsworkshop.flextools.codemodel.IProjectStateManager;

/**
 * The project manager sits in the background and ensures that all
 * project-related resources are loaded and present.
 * 
 * 
 * @author otupman
 *
 */
public class ProjectManager {
	/**
	 * Scans the delta looking to see whether any projects have been opened.
	 * If they've been opened, the opened project is registered with the
	 * project manager.
	 * 
	 * @author otupman
	 *
	 */
	private final class ProjectOpenDetectingVisitor implements IResourceDeltaVisitor {
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			if(resource instanceof IProject) {
				loadProject((IProject)resource);
				return false;
			}
			return true;
		}
	}

	private List<IProject> openProjects;
	private IProjectStateManager stateManager;
	
	public ProjectManager() {
		openProjects = new ArrayList<IProject>(10);
		stateManager = Activator.getStateManager();
	}
	
	/**
	 * Initialises the manager. Ensures, at load time, that the project states
	 * are available. This is a blocking operation as the rest of Flex Tools
	 * cannot function without this data being available.
	 * 
	 * @param monitor The progress monitor to report progress
	 */
	public IStatus initialise(IProgressMonitor monitor) {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {

			public void resourceChanged(IResourceChangeEvent event) {
				handleResourceChanged(event);
			}
			
		});
		IProject [] openProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

		for(IProject project : openProjects) {
			try {
				if(project.isOpen()) {
					if(project.hasNature(SampleNature.NATURE_ID)) {
						loadProject(project);
					}	
				}
				
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		return Status.OK_STATUS;
	}
	
	public void registerProject(IProject project) {
		loadProject(project);
	}
	
	private void loadProject(IProject project) {
		if(!openProjects.contains(project)) {
			openProjects.add(project);			
		}		
	}
	
	private void closeProject(IProject project) {
		openProjects.remove(project);
	}

	private void handleResourceChanged(IResourceChangeEvent event) {
		IProject project;

		switch(event.getType()) {
		case IResourceChangeEvent.POST_CHANGE:
			try {
				event.getDelta().accept(new ProjectOpenDetectingVisitor());
			} catch (CoreException e) {
				e.printStackTrace();
			}
			break;
		case IResourceChangeEvent.PRE_CLOSE:
			project = (IProject)event.getResource();
			closeProject(project);
			break;
		case IResourceChangeEvent.PRE_DELETE:
			//TODO: Remove all project information stored and finalise the clearup
			project = (IProject)event.getResource();
			closeProject(project);
			break;
		}
	
	}
	
}
