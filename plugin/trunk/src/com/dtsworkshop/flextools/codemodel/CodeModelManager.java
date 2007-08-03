package com.dtsworkshop.flextools.codemodel;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.dtsworkshop.flextools.model.BuildStateDocument;
import com.dtsworkshop.flextools.model.BuildStateType;
import com.dtsworkshop.flextools.model.NodeType;
import com.dtsworkshop.flextools.utils.ResourceHelper;

public class CodeModelManager {
	private static CodeModelManager manager;
	public static final String stateDirectoryRoot = "d:\\builder state";
	static {
		manager = new CodeModelManager();
		manager.initialise(new File(stateDirectoryRoot));
	}
	public static CodeModelManager getManager() {
		return manager;
	}
	
	private Map<String, Map<String, BuildStateDocument>> projectStates;
	
	public void acceptVisitor(IBuildStateVisitor visitor) {
		Collection<Map<String, BuildStateDocument>> states = projectStates.values();
		List<BuildStateDocument> buildStates = new ArrayList<BuildStateDocument>(100); 
		
		for(Map<String, BuildStateDocument> projectState : states) {
			buildStates.addAll(projectState.values());
		}
		
		for(BuildStateDocument currentDoc : buildStates) {
			boolean carryOn = visitor.visit(currentDoc);
			if(!carryOn) {
				break; // Stop!
			}
			else {
				// Carry on! (Mark & Lard from Radio 1 reference :D)
			}
		}
	}
	
	public CodeModelManager() {
		projectStates = new HashMap<String, Map<String,BuildStateDocument>>(5);
	}
		
	public void initialise(File stateDirectory) {
		File [] subFiles = stateDirectory.listFiles();
		for(File currentFile : subFiles) {
			if(currentFile.isDirectory()) {
				initialiseProject(currentFile);
			}
		}
	}
	
	private void initialiseProject(File projectDirectory) {
		File [] buildStates = projectDirectory.listFiles();
		Map<String, BuildStateDocument> projectStateMap = new HashMap<String, BuildStateDocument>(buildStates.length);
		for(File builtFile : buildStates) {
			try {
				BuildStateDocument document = BuildStateDocument.Factory.parse(builtFile);
				String filename = builtFile.getName();
				
				String path = filename.substring(0, filename.length() - 4);
				projectStateMap.put(path, document);
			} catch (XmlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		projectStates.put(projectDirectory.getName(), projectStateMap);
	}
	
	public void removeProjectState(IProject project) {
		projectStates.remove(project.getName());
	}
	
	public void removeBuildState(IProject project, IFile resource) {
		Map<String, BuildStateDocument> state = getProjectState(project.getName());
		String fileRef = getStateFilename(resource);
		state.remove(fileRef);
	}

	private String getStateFilename(IFile resource) {
		String resourcePath = resource.getProjectRelativePath().toString();
		String fileRef = toFilename(resourcePath);
		return fileRef;
	}
	
	private String toFilename(String resourcePath) {
		String fileRef = resourcePath.replaceAll("/", ".");
		return fileRef;
	}
	
	public void storeBuildState(BuildStateDocument state) {
		String path = state.getBuildState().getFile().replaceAll("/", ".");
		String projectName = state.getBuildState().getProject();
		Map<String, BuildStateDocument> projectMap = getProjectState(projectName);
		projectMap.put(path, state);
		writeBuildState(state);
	}
	
	private String toStateFilename(BuildStateDocument state) {
		// Made up of [state directory]\[project name]\[filename];
		String filename = getProjectPath(state.getBuildState().getProject());
		filename += File.separator + toFilename(state.getBuildState().getFile());
		filename += ".xml";
		return filename;
	}
	
	private String getProjectPath(String projectName) {
		String filename = stateDirectoryRoot;
		filename += File.separator + projectName;
		return filename;
	}
	
	private void writeBuildState(BuildStateDocument state) {
		BuildStateType buildState = state.getBuildState();
		String projectName = buildState.getProject();
		boolean success = createProjectDirectory(getProjectPath(projectName));
		if(!success) {
			return;
		}
		File stateFilename = new File(toStateFilename(state));
		if(!stateFilename.exists()) {
			try {
				stateFilename.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
		try {
			state.save(stateFilename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean createProjectDirectory(String projectName) {
		File projectDir = new File(projectName);
		if(!projectDir.exists()) {
			
			return projectDir.mkdir();
			
		}
		return true;
	}

	private Map<String, BuildStateDocument> getProjectState(String projectName) {
		if(!projectStates.containsKey(projectName)) {
			projectStates.put(projectName, new HashMap<String, BuildStateDocument>(50));
		}
		Map<String, BuildStateDocument> projectMap = projectStates.get(projectName);
		return projectMap;
	}
}
