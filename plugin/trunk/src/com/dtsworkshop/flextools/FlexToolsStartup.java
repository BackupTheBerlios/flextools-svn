package com.dtsworkshop.flextools;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IStartup;

public class FlexToolsStartup implements IStartup {

	public static void doStartup() {
		FlexToolsLog.logInfo("Running startup jobs...");
		IProgressMonitor initMonitor = Platform.getJobManager().createProgressGroup();
		Job [] initJobs = new Job[] {
			new LoadExtensionsJob("Loading extensions")
			, new ProjectManagerStartupJob("Loading open projects")
		};
		try {
			for(Job currentJob : initJobs) {
				
				currentJob.setProgressGroup(initMonitor, IProgressMonitor.UNKNOWN);
				currentJob.schedule();
			}
			for(Job currentJob : initJobs) {
				currentJob.join();
			}
		}
		catch(InterruptedException ex) {
			ex.printStackTrace();
			FlexToolsLog.logError("Error occurred when running statup jobs", ex);
		}
		finally {
			initMonitor.done();
		}
		
	}
	
	public void earlyStartup() {
		// Doing nowt for the moment...
	}

}
