package com.dtsworkshop.flextools;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

import com.dtsworkshop.flextools.project.ProjectManager;

public class ProjectManagerStartupJob extends Job {

	public ProjectManagerStartupJob(String name) {
		super(name);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		ProjectManager manager = Activator.getDefault().getProjectManager();
		return manager.initialise(monitor);
	}

}
