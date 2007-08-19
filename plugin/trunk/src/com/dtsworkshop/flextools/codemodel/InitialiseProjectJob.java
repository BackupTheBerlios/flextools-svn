package com.dtsworkshop.flextools.codemodel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;


import com.dtsworkshop.flextools.Activator;
import com.dtsworkshop.flextools.project.AbstractProjectLoadContributor;

public class InitialiseProjectJob extends AbstractProjectLoadContributor {
	public static final String INIT_PROJECT_JOB_NAME = "Flex Tools Project Loader";
	
	public InitialiseProjectJob() {
		super(INIT_PROJECT_JOB_NAME);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IProjectStateManager manager = Activator.getStateManager();
		manager.initialiseProject(getProject(), monitor);
		return Status.OK_STATUS;
	}

}
