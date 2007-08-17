package com.dtsworkshop.flextools.flexbuilder.initialisation;

import java.util.Stack;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.indices.IClassNameIndex;
import com.adobe.flexbuilder.codemodel.indices.IIndex;
import com.adobe.flexbuilder.codemodel.project.IProjectLoadListener;
import com.adobe.flexbuilder.codemodel.project.IRegistrar;
import com.adobe.flexbuilder.project.IFlexProject;
import com.dtsworkshop.flextools.project.AbstractProjectLoadContributor;

public class FbProjectLoader extends AbstractProjectLoadContributor  {
	
	public FbProjectLoader() {
		super("Loading FlexBuilder project");
	}


	private class ProjectLoadListener implements IProjectLoadListener {
		private IProgressMonitor monitor;
		
		public ProjectLoadListener() {
		}
		
		public boolean isCancelled() {
			return monitor.isCanceled();
		}

		public void loading(String arg0) {
			//System.out.println("loading() - " + arg0);
		}

		public void phaseEnd(int arg0) {
			//System.out.println("phaseEnd() - Finished loading");
			monitor.worked(100 - lastWorked);
		}

		public void phaseStart(int arg0) {
			//System.out.println("Starting... " + arg0);
		}
		private int lastWorked = 0;
		public void progress(int arg0) {
			//System.out.println("Progress..." + arg0);
			
			monitor.worked(arg0 - lastWorked);
			lastWorked = arg0;
		}
		
		public void start(IProgressMonitor monitor) {
			this.monitor = monitor;
			monitor.beginTask(
				String.format("Loading project %s", getProject().getName())
				, 100
			);
			synchronized (CMFactory.getLockObject()) {
				IRegistrar registrar = CMFactory.getRegistrar();
				registrar.registerProject(getProject(), this);
				IFlexProject proj;
				com.adobe.flexbuilder.codemodel.project.IProject flexProject = CMFactory.getManager().getProjectFor(getProject());
				IIndex retrievedIndex = flexProject.getIndex(IClassNameIndex.ID);
				IClassNameIndex classIndex = (IClassNameIndex)retrievedIndex;
				System.out.println("adsf");
			}
			monitor.done();
		}
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		ProjectLoadListener listener = new ProjectLoadListener();
		listener.start(monitor);
		return Status.OK_STATUS;
	}

}
