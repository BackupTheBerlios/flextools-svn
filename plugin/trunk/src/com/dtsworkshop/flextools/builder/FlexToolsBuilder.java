/**
@BoilerplateBegin
This is the boiler plate license text.
Copyright (C) Oliver B. Tupman, 2007.
@BoilerplateEnd
*/
package com.dtsworkshop.flextools.builder;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.definitions.IASScope;
import com.adobe.flexbuilder.codemodel.project.IProjectLoadListener;
import com.adobe.flexbuilder.codemodel.tree.IASNode;
import com.adobe.flexbuilder.codemodel.tree.IFileNode;
import com.dtsworkshop.flextools.Activator;
import com.dtsworkshop.flextools.FlexToolsLog;
import com.dtsworkshop.flextools.model.BuildStateDocument;

/**
 * The core builder for Flex Tools. In reality this builder does very little
 * work itself. It processes the build information from Eclipse and passes
 * it on to any builders registered.
 *
 * @author otupman
 * @see AbstractFlexBuilderDeltaVisitor
 *
 */
public class FlexToolsBuilder extends IncrementalProjectBuilder {


	public static final String BUILDER_ID = "com.dtsworkshop.flextools.sampleBuilder";

	private static final String MARKER_TYPE = "com.dtsworkshop.flextools.xmlProblem";

	static Logger log = Logger.getLogger(FlexToolsBuilder.class.getName());
	public static final String dumpLocation = "d:\\builder state";


	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}


//	private void deleteMarkers(IFile file) {
//		try {
//			file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
//		} catch (CoreException ce) {
//		}
//	}

	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		try {
			monitor.beginTask("Removing project state.", 1);
			Activator.getStateManager().removeProjectState(getProject());
			monitor.worked(1);
			//getProject().accept(new FlexResourceVisitor());
			List<IResourceAsDeltaVisitor> visitors = Activator.getDefault().getVisitors();
			for(IResourceAsDeltaVisitor visitor : visitors) {
				visitor.setProject(getProject());
				getProject().accept(visitor);
			}
		} catch (CoreException e) {
			e.printStackTrace();
			FlexToolsLog.logError("Error occurred during build.", e);
		}
	}


	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		List<IResourceAsDeltaVisitor> visitors = Activator.getDefault().getVisitors();
		for(IResourceAsDeltaVisitor visitor : visitors) {
			visitor.setProject(getProject());
			delta.accept(visitor);
		}
	}
}
