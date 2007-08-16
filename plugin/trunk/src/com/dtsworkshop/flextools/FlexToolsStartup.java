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
				FlexToolsLog.logInfo(String.format("Running project %s", currentJob.getName()));
				currentJob.setProgressGroup(initMonitor, IProgressMonitor.UNKNOWN);
				currentJob.schedule(Job.LONG);
			}
			for(Job currentJob : initJobs) {
				currentJob.join();
				FlexToolsLog.logInfo(String.format("Job %s has finished.", currentJob.getName()));
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
