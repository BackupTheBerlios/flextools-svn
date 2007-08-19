package com.dtsworkshop.flextools.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.jobs.Job;

/**
 * This class implements a job that should be triggered when a project is 
 * opened/loaded within Eclipse.
 * 
 * @author otupman
 *
 */
public abstract class AbstractProjectLoadContributor extends Job {
	private IProject project;
	
	public AbstractProjectLoadContributor(String name) {
		super(name);
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

}
