package com.dtsworkshop.flextools.codemodel;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;


import com.dtsworkshop.flextools.Activator;
import com.dtsworkshop.flextools.project.AbstractProjectLoadContributor;

public class InitialiseProjectJob extends AbstractProjectLoadContributor {
	public static final String INIT_PROJECT_JOB_NAME = "Flex Tools Project Loader";
	private static Logger log = Logger.getLogger(InitialiseProjectJob.class);
	public InitialiseProjectJob() {
		super(INIT_PROJECT_JOB_NAME);
		log.info(String.format("Creating project initialisation job."));
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		log.debug("Running project initialisation job.");
		IProjectStateManager manager = Activator.getStateManager();
		manager.initialiseProject(getProject(), monitor);
		return Status.OK_STATUS;
	}

}
