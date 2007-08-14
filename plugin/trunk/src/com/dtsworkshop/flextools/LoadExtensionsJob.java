package com.dtsworkshop.flextools;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;

import com.dtsworkshop.flextools.builder.IResourceAsDeltaVisitor;

public class LoadExtensionsJob extends Job {
	
	public LoadExtensionsJob(String name) {
		super(name);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(this.getName(), IProgressMonitor.UNKNOWN);
		Object [] loadedExtensions = Activator.loadSimpleExtensions(Activator.DELTA_VISITOR_EXTENSIONID);
		
		for(Object extension : loadedExtensions) {
			Activator.getDefault().addDeltaVisitor((IResourceAsDeltaVisitor)extension);
		}
		
		loadedExtensions = Activator.loadSimpleExtensions(Activator.PROJECT_LOAD_JOBS_EXTENSIONID);
		
		
		return new Status(Status.OK, Activator.PLUGIN_ID, IStatus.OK, "Flex Tools loaded", null);
	}

}
