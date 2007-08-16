package com.dtsworkshop.flextools.codemodel;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;

import com.dtsworkshop.flextools.FlexToolsLog;
import com.dtsworkshop.flextools.builder.SampleNature;
import com.dtsworkshop.flextools.model.BuildStateDocument;

public class WorkingSpaceModelStateManager extends AbstractStateManager implements IProjectStateManager {

	private Map<String, ProjectStateEntry> projectStates;
	
	public WorkingSpaceModelStateManager() {
		projectStates = new HashMap<String, ProjectStateEntry>(10);
	}
	
	public void acceptVisitor(IBuildStateVisitor visitor,
			IProgressMonitor monitor) {
		List<BuildStateDocument> states = new ArrayList<BuildStateDocument>(100);
		
		for(ProjectStateEntry entry : projectStates.values()) {
			states.addAll(entry.getStates());
		}
		processVisitor(visitor, monitor, states);
	}

	/**
	 * @deprecated
	 * @param monitor
	 */
	public void initialise(IProgressMonitor monitor) {
		try {
		IProject [] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for(IProject project : projects) {
			try {
				if(!project.isOpen()) {
					//TODO: Need to handle the opening of projects after initialisation!
					continue; // project isn't open, we can't do nowt right now
				}
				if(project.hasNature(SampleNature.NATURE_ID)) {
					initialiseProject(project, monitor);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		}
		catch(RuntimeException ex) {
			ex.printStackTrace();
		}
	}

	public void initialiseProject(IProject project, IProgressMonitor monitor) {
		
		ProjectStateEntry newEntry = new ProjectStateEntry();
		newEntry.setProject(project);
		File projectDirectory = newEntry.getWorkingSpace().toFile();
		File [] stateFiles = projectDirectory.listFiles(new FilenameFilter() {
			public boolean accept(File arg0, String arg1) {
				return arg0.getName().endsWith(".xml");
			}			
		});
		monitor.beginTask(String.format("Loading project %s", project.getName()), stateFiles.length);
		for(File stateFile : stateFiles) {
			try {
				BuildStateDocument parsedDoc = BuildStateDocument.Factory.parse(stateFile);
				newEntry.getStates().add(parsedDoc);
			} catch (XmlException e) {
				e.printStackTrace();
				FlexToolsLog.logError(String.format("Error occurred while initialising project %s from state", project.getName()), e);
			} catch (IOException e) {
				e.printStackTrace();
				FlexToolsLog.logError(String.format("Error occurred while initialising project %s from state", project.getName()), e);
			}
			monitor.worked(1);
		}
		projectStates.put(project.getName(), newEntry);
		monitor.done();
	}
	
	public boolean isProjectManaged(IProject project) {
		return projectStates.containsKey(project.getName());
	}

	public ModelInfo getInfo() {
		ModelInfo info = new ModelInfo();
		info.numberOfProjects = projectStates.keySet().size();
		info.numberOfStates = 0;
		for(ProjectStateEntry entry : projectStates.values()) {
			info.numberOfStates+= entry.getStates().size();
		}
		return info;
	}
	
	public void registerProject(IProject project) {
		ProjectStateEntry entry = new ProjectStateEntry();
		entry.setProject(project);
		storeEntry(entry);
	}
	

	public boolean removeProjectState(IProject project) {
		ProjectStateEntry entry = getEntry(project);
		if(entry == null) {
			// Somehow we've not got the entry for the project, so we'll just report back that we succeeded in removing it...
			return true;
		}
		File projectDirectory = entry.getWorkingSpace().toFile();
		boolean success = projectDirectory.delete();
		if(success) {
			projectStates.remove(project.getName());
		}
		return success;
	}
	
	public void storeBuildState(BuildStateDocument state) {
		IProject project = getProjectForState(state);
		if(!isProjectManaged(project)) {
			registerProject(project);
		}
		ProjectStateEntry entry = getEntry(project);
		File targetStateFile = getStateFile(state, entry);
		writeBuildState(state, targetStateFile);
		entry.getStates().add(state);
	}



	public void removeBuildState(IProject project, IPath sourceFilePath) {
		ProjectStateEntry entry = projectStates.get(project);
		Assert.isNotNull(entry, "Project state entry for " + project.getName() + " is null.");
		BuildStateDocument document = entry.findStateForPath(sourceFilePath);
		File stateFile = getStateFile(document, entry);
		if(stateFile.exists()) {
			stateFile.delete();
		}
		entry.getStates().remove(document);
	}
	
	public void removeBuildState(IProject project, IFile resource) {
		removeBuildState(project, resource.getProjectRelativePath());
	}
	
	private File getStateFile(BuildStateDocument state, ProjectStateEntry entry) {
		String stateFilename  = getStateFilename(state);
		IPath statePath = entry.getWorkingSpace().append(stateFilename);
		File targetStateFile = statePath.toFile();
		return targetStateFile;
	}
	
	private String getStateFilename(BuildStateDocument state) {
		String filename = StateManagerHelpers.getStateName(state.getBuildState().getFile())  + ".xml";
		return filename;
	}
	
	private void storeEntry(ProjectStateEntry newEntry) {
		projectStates.put(newEntry.getProject().getName(), newEntry);
	}
	
	private ProjectStateEntry getEntry(IProject project) {
		return projectStates.get(project.getName());
	}

	private IProject getProjectForState(BuildStateDocument state) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(state.getBuildState().getProject());
	}

}
