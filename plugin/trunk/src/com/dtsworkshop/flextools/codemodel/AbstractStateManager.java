package com.dtsworkshop.flextools.codemodel;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.internal.utils.WrappedRuntimeException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.dtsworkshop.flextools.FlexToolsLog;
import com.dtsworkshop.flextools.model.BuildStateDocument;

public class AbstractStateManager {

	public AbstractStateManager() {
		super();
	}


	/**
	 * Processes a visitor across a list of build states.
	 * 
	 * @param visitor The visitor that wants to doing the visiting
	 * @param monitor Monitor to report progress (can be null)
	 * @param buildStates List of build states for the visitor to visit.
	 */
	protected void processVisitor(IBuildStateVisitor visitor, IProgressMonitor monitor, List<BuildStateDocument> buildStates) {
		for(BuildStateDocument currentDoc : buildStates) {
			if(monitor.isCanceled()) {
				return;
			}
			monitor.subTask(
				String.format("Checking %s", currentDoc.getBuildState().getFile())	
			);
			monitor.worked(1);
			boolean carryOn = visitor.visit(currentDoc);
			if(!carryOn) {
				break; // Stop!
			}
			else {
				// Carry on! (Mark & Lard from Radio 1 reference :D)
			}
		}
	}

	/**
	 * Writes a build state to the supplied filename, creating it if necessary.
	 * 
	 * @param state The state to write out
	 * @param stateFilename The destination file
	 */
	protected void writeBuildState(BuildStateDocument state, File stateFilename) {
		if(!stateFilename.exists()) {
			try {
				stateFilename.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				FlexToolsLog.logError(
					String.format("An exception occurred while trying to create the file %s", stateFilename.getName()), 
				e);
				return;
			}
		}
		try {
			state.save(stateFilename);
		} catch (IOException e) {
			e.printStackTrace();
			FlexToolsLog.logError(
				String.format("Error occurred while writing state file for %s", stateFilename.getName())
				, e
			);
		}
	}

}