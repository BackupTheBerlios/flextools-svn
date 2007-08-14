package com.dtsworkshop.flextools;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

import com.dtsworkshop.flextools.project.ProjectManager;

/**
 * Starts up the project manager and intialises it, ready for Flex
 * Tools to be used.
 * 
 * @author otupman
 *
 */
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
