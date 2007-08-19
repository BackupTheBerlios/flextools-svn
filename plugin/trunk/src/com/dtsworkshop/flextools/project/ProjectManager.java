package com.dtsworkshop.flextools.project;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import com.dtsworkshop.flextools.Activator;
import com.dtsworkshop.flextools.FlexToolsLog;
import com.dtsworkshop.flextools.builder.SampleNature;
import com.dtsworkshop.flextools.codemodel.IProjectStateManager;
import com.dtsworkshop.flextools.utils.JobHelpers;

/**
 * The project manager sits in the background and ensures that all
 * project-related resources are loaded and present.
 * 
 * 
 * @author otupman
 *
 */
public class ProjectManager {
	private static Logger log = Logger.getLogger(ProjectManager.class);
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
			log.debug(String.format("Visiting %s", resource.getName()));
			if(resource instanceof IProject) {
				log.debug("Is project, opening...");
				loadProject((IProject)resource);
				return false;
			}
			return true;
		}
	}
	
	private List<AbstractProjectLoadContributor> loadContributors = null;
	
	private List<AbstractProjectLoadContributor> getLoadContributors() {
		if(loadContributors == null) {
			log.debug("Load contributors not loaded. Loading...");
			loadExtensionPoints();
		}
		return loadContributors;
	}
	
	private void loadExtensionPoints() {
		loadContributors = new ArrayList<AbstractProjectLoadContributor>(10);
		Object [] loaders = Activator.loadSimpleExtensions(Activator.PROJECT_LOAD_JOBS_EXTENSIONID);
		for(Object loader : loaders) {
			Assert.isTrue(loader instanceof AbstractProjectLoadContributor);
			loadContributors.add((AbstractProjectLoadContributor)loader);
		}
	}

	private List<IProject> openProjects;
	private IProjectStateManager stateManager;
	
	public ProjectManager() {
		openProjects = new ArrayList<IProject>(10);
		stateManager = Activator.getStateManager();
		log.debug("Project manager started.");
	}
	
	/**
	 * Initialises the manager. Ensures, at load time, that the project states
	 * are available. This is a blocking operation as the rest of Flex Tools
	 * cannot function without this data being available.
	 * 
	 * @param monitor The progress monitor to report progress
	 */
	public IStatus initialise(IProgressMonitor monitor) {
		log.debug("Initialising...");
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
				else {
					log.debug(String.format("Project %s is not open, ignoring.", project.getName()));
				}
				
			} catch (CoreException e) {
				e.printStackTrace();
				FlexToolsLog.logError(String.format("Error occurred while trying to access the project %s", project.getName()), e);
			}
		}
		
		return Status.OK_STATUS;
	}
	
	public void registerProject(IProject project) {
		log.info(String.format("Registering project %s", project.getName()));
		loadProject(project);
	}
	
	private void callLoaders(IProject project) {
		log.debug("Calling the loader list");
		IProgressMonitor monitor = Platform.getJobManager().createProgressGroup();
		List<AbstractProjectLoadContributor> loaders = getLoadContributors();
		monitor.beginTask(
				String.format("Loading project")
			, loaders.size() * 10
		);		

		for(AbstractProjectLoadContributor loader : loaders) {
			loader.setProject(project);
			loader.setProgressGroup(monitor, 10);
			loader.schedule();
			try {
				loader.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				FlexToolsLog.logError(String.format("Error occurred while waiting for a project loader to rejoin"), e);
			}
		}
//		
//		for(AbstractProjectLoadContributor loader : loaders) {
//			try {
//				loader.join();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//				FlexToolsLog.logError(String.format("Error occurred while waiting for a project loader to rejoin"), e);
//			}
//		}
	}
	
	private void loadProject(IProject project) {
		if(!openProjects.contains(project)) {
			log.debug(String.format("Project not loaded, loading project %s", project.getName()));
			openProjects.add(project);
			callLoaders(project);
		}
		else {
			log.debug(String.format("Project %s already loaded", project.getName()));
		}
	}
	
	private void closeProject(IProject project) {
		log.debug(String.format("Project %s closed", project.getName()));
		openProjects.remove(project);
	}

	private void handleResourceChanged(IResourceChangeEvent event) {
		IProject project;
		
		switch(event.getType()) {
		case IResourceChangeEvent.POST_CHANGE:
			log.debug("Resources have changed. Processing...");
			try {
				event.getDelta().accept(new ProjectOpenDetectingVisitor());
			} catch (CoreException e) {
				e.printStackTrace();
				FlexToolsLog.logError(String.format("Error getting delta"), e);
			}
			break;
		case IResourceChangeEvent.PRE_CLOSE:
			project = (IProject)event.getResource();
			log.debug(String.format("Resources have changed. Project %s closing...", project.getName()));
			closeProject(project);
			break;
		case IResourceChangeEvent.PRE_DELETE:
			//TODO: Remove all project information stored and finalise the clearup
			project = (IProject)event.getResource();
			log.debug(String.format("Project %s is being deleted.", project.getName()));
			closeProject(project);
			break;
		}
	
	}
	
}
