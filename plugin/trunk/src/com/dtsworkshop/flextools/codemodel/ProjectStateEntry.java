package com.dtsworkshop.flextools.codemodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
	private static Logger log = Logger.getLogger(ProjectStateEntry.class);
	private IProject project;
	private Map<String, BuildStateDocument> states;
	
	public ProjectStateEntry() {
		states = new HashMap<String, BuildStateDocument>(100);
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
	
	public void addState(BuildStateDocument newState) {
		String key = newState.getBuildState().getFile();
		if(states.containsKey(key)) {
			log.warn(String.format("Project %s already has state for file %s", project.getName(), newState.getBuildState().getFile()));
		}
		states.put(key, newState);
	}
	
	public List<BuildStateDocument> getStates() {
		return new ArrayList<BuildStateDocument>(states.values());
	}
	public void setStates(List<BuildStateDocument> states) {
		this.states.clear();
		for(BuildStateDocument doc : states) {
			addState(doc);
		}
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
		for(BuildStateDocument currentDoc : states.values()) {
			String filename = currentDoc.getBuildState().getFile();
			if(filename.equals(pathStateName)) {
				foundDoc = currentDoc;
				break;
			}
		}
		return foundDoc;
	}
}
