package com.dtsworkshop.flextools;

import org.apache.log4j.Logger;
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
	private static Logger log = Logger.getLogger(ProjectManagerStartupJob.class);
	
	public ProjectManagerStartupJob(String name) {
		super(name);
		log.debug("Created.");
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		log.debug("Running.");
		ProjectManager manager = Activator.getDefault().getProjectManager();
		return manager.initialise(monitor);
	}

}
