package com.dtsworkshop.flextools.codemodel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.dtsworkshop.flextools.Activator;
import com.dtsworkshop.flextools.project.AbstractProjectLoadContributor;

public class InitialiseProjectJob extends AbstractProjectLoadContributor {

	public InitialiseProjectJob(String name) {
		super(name);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IProjectStateManager manager = Activator.getStateManager();
		manager.initialiseProject(getProject(), monitor);
		return Status.OK_STATUS;
	}

}
