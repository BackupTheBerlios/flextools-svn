package com.dtsworkshop.flextools.flexbuilder.initialisation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.project.IProjectLoadListener;
import com.adobe.flexbuilder.codemodel.project.IRegistrar;
import com.dtsworkshop.flextools.Activator;
import com.dtsworkshop.flextools.builder.SampleNature;

/**
 * Touches all of the FlexBuilder managed files to ensure that FB's codemodel
 * is loaded for every file. This ensures that FlexTools' codemodel is in sync
 * and ready.
 * 
 * @author otupman
 * TODO: Refactor this class to the FB integration project.
 */
public class FileToucherJob extends Job {

	public FileToucherJob(String name) {
		super(name);
	}

	private class ProjectLoadListener implements IProjectLoadListener {
		private Stack<IProject> projects;
		private IProgressMonitor monitor;
		private IProject currentProject;
		
		public ProjectLoadListener(Stack<IProject> projectsToLoad) {
			this.projects = projectsToLoad;
		}
		
		public boolean isCancelled() {
			return monitor.isCanceled();
		}

		public void loading(String arg0) {
			System.out.println("loading() - " + arg0);
		}

		public void phaseEnd(int arg0) {
			//TODO: This actually reports the end of the phase begun in phaseStart(), should handle this properly
			monitor.worked(1);
			System.out.println("phaseEnd() - Finished loading");
			nextProject();
		}

		public void phaseStart(int arg0) {
			System.out.println("Starting... " + arg0);
		}

		public void progress(int arg0) {
			System.out.println("Progress..." + arg0);
		}
		
		public void start(IProgressMonitor monitor) {
			this.monitor = monitor;
			monitor.beginTask("Loading projects", projects.size());
			nextProject();
		}

		private void nextProject() {
			if(projects.size() == 0) {
				return;
			}
			currentProject = projects.pop();
			monitor.subTask(String.format("Loading %s", currentProject.getName()));
			synchronized (CMFactory.getLockObject()) {
				IRegistrar registrar = CMFactory.getRegistrar();
				registrar.registerProject(currentProject, this);
			}
		}
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		Stack<IProject> managedProjects = new Stack<IProject>();
		for(IProject project : getManagedProjects()) {
			managedProjects.push(project);
		}
		ProjectLoadListener loader= new ProjectLoadListener(managedProjects);
		loader.start(monitor);
		IStatus loadStatus = null;
		if(monitor.isCanceled()) {
			loadStatus = new Status(IStatus.CANCEL, Activator.PLUGIN_ID, IStatus.OK, "Load cancelled", null);			
		}
		else {
			loadStatus = new Status(IStatus.OK, Activator.PLUGIN_ID, IStatus.OK, "Load complete.", null);
		}
		return loadStatus;
	}
	
	public static List<IProject> getFbProjects() {
		//TODO: A) Refactor to FB bridge project. B) Make it work!
		return getManagedProjects();
	}

	public static List<IProject> getManagedProjects() {
		List<IProject> managedProjects = new ArrayList<IProject>(10);
		IProject [] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for(IProject currentProject : projects) {
			try {
				if(currentProject.hasNature(SampleNature.NATURE_ID)) {
					managedProjects.add(currentProject);
				}
			} catch (CoreException e) {
				//TODO: Warn the user about missing this project.
			}
		}
		return managedProjects;
	}

}
