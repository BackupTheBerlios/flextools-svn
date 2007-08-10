package com.dtsworkshop.flextools.codemodel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

import com.dtsworkshop.flextools.Activator;
import com.dtsworkshop.flextools.model.BuildStateDocument;

/**
 * Simple object to store information about a project's state.
 * 
 * @author otupman
 *
 */
public class ProjectStateEntry {
	private IProject project;
	private List<BuildStateDocument> states;
	
	public ProjectStateEntry() {
		states = new ArrayList<BuildStateDocument>(100);
	}
	
	public IPath getWorkingSpace() {
		return project.getWorkingLocation(Activator.PLUGIN_ID);
	}
	
	public IProject getProject() {
		return project;
	}
	public void setProject(IProject project) {
		this.project = project;
	}
	public List<BuildStateDocument> getStates() {
		return states;
	}
	public void setStates(List<BuildStateDocument> states) {
		this.states = states;
	}
	
	/**
	 * Finds the state document for the supplied path.
	 * The supplied path must be relative to the containing project
	 * 
	 * @param filePath The path of the file, relative to the containing project.
	 * @return If found, the state document; otherwise null
	 */
	public BuildStateDocument findStateForPath(IPath filePath) {
		String pathStateName = StateManagerHelpers.getStateName(filePath);
		BuildStateDocument foundDoc = null;
		for(BuildStateDocument currentDoc : states) {
			String filename = currentDoc.getBuildState().getFile();
			if(filename.equals(pathStateName)) {
				foundDoc = currentDoc;
				break;
			}
		}
		return foundDoc;
	}
}
