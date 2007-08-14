///**
//*	Copyright (C) Oliver B. Tupman, 2007.
//*	
//*	This file is part of the Flex Tools Project.
//*	
//*	The Flex Tools Project is free software; you can redistribute it and/or modify
//*	it under the terms of the GNU General Public License as published by
//*	the Free Software Foundation; either version 3 of the License, or
//*	(at your option) any later version.
//*	
//*	The Flex Tools Project is distributed in the hope that it will be useful,
//*	but WITHOUT ANY WARRANTY; without even the implied warranty of
//*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//*	GNU General Public License for more details.
//*	
//*	You should have received a copy of the GNU General Public License
//*	along with this program.  If not, see <http://www.gnu.org/licenses/>.
//*/
//package com.dtsworkshop.flextools.codemodel;
//
//import java.io.File;
//import java.io.FileFilter;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FilenameFilter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.apache.xmlbeans.XmlException;
//import org.eclipse.core.resources.IFile;
//import org.eclipse.core.resources.IProject;
//import org.eclipse.core.resources.IResource;
//import org.eclipse.core.runtime.IPath;
//import org.eclipse.core.runtime.IProgressMonitor;
//import org.eclipse.core.runtime.Path;
//
//import com.dtsworkshop.flextools.Activator;
//import com.dtsworkshop.flextools.model.BuildStateDocument;
//import com.dtsworkshop.flextools.model.BuildStateType;
//import com.dtsworkshop.flextools.model.NodeType;
//import com.dtsworkshop.flextools.utils.ResourceHelper;
//
///**
// * The CodeModelManager maintains a single point of entry to the build states
// * that are created when building a file or project.
// * 
// * 
// * @author otupman
// *
// */
//public class CodeModelManager extends AbstractStateManager implements IProjectStateManager {
//	private static CodeModelManager manager;
//	public static final String stateDirectoryRoot = "c:\\builder state";
//	static {
//		manager = new CodeModelManager();
//		manager.initialise(new File(stateDirectoryRoot));
//	}
//	/**
//	 * Gets the instance of the manager.
//	 * 
//	 * NB: Not guaranteed to be thread safe!
//	 * 
//	 * @return
//	 */
//	public static IProjectStateManager getManager() {
//		return manager;
//	}
//	
//		
//	Map<String, Map<String, BuildStateDocument>> projectStates;
//	/* (non-Javadoc)
//	 * @see com.dtsworkshop.flextools.codemodel.IProjectStateManager#acceptVisitor(com.dtsworkshop.flextools.codemodel.IBuildStateVisitor, org.eclipse.core.runtime.IProgressMonitor)
//	 */
//	public void acceptVisitor(IBuildStateVisitor visitor, IProgressMonitor monitor) {
//		Collection<Map<String, BuildStateDocument>> states = projectStates.values();
//		List<BuildStateDocument> buildStates = new ArrayList<BuildStateDocument>(100);
//		if(monitor != null) {
//			monitor.subTask("Gathering build states.");
//		}
//		for(Map<String, BuildStateDocument> projectState : states) {
//			buildStates.addAll(projectState.values());
//		}
//		
//		processVisitor(visitor, monitor, buildStates);
//
//	}
//
//	public CodeModelManager() {
//		projectStates = new HashMap<String, Map<String,BuildStateDocument>>(5);
//	}
//	
//	public void initialise(File stateDirectory) {
//		File [] subFiles = stateDirectory.listFiles();
//		for(File currentFile : subFiles) {
//			if(currentFile.isDirectory()) {
//				initialiseProject(currentFile);
//			}
//		}
//	}
//	
//	public void initialise() {
//		// Do nowt, we need to be called by the initialise(String) method.
//	}
//
//	public boolean isProjectManaged(IProject project) {
//		String projectName = project.getName();
//		return projectStates.containsKey(projectName);
//	}
//
//	private void initialiseProject(File projectDirectory) {
//		File [] buildStates = projectDirectory.listFiles();
//		Map<String, BuildStateDocument> projectStateMap = new HashMap<String, BuildStateDocument>(buildStates.length);
//		for(File builtFile : buildStates) {
//			try {
//				BuildStateDocument document = BuildStateDocument.Factory.parse(builtFile);
//				String filename = builtFile.getName();
//				
//				String path = filename.substring(0, filename.length() - 4);
//				projectStateMap.put(path, document);
//			} catch (XmlException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		projectStates.put(projectDirectory.getName(), projectStateMap);
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.dtsworkshop.flextools.codemodel.IProjectStateManager#registerProject(org.eclipse.core.resources.IProject)
//	 */
//	public void registerProject(IProject project) {
//		IPath projectWorkingLocation = project.getWorkingLocation(Activator.PLUGIN_ID);
//		
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.dtsworkshop.flextools.codemodel.IProjectStateManager#removeProjectState(org.eclipse.core.resources.IProject)
//	 */
//	public boolean removeProjectState(IProject project) {
//		if(projectStates.containsKey(project.getName())) {
//			projectStates.remove(project.getName());
//		}
//		
//		return removeStateFiles(project);
//	}
//	
//	private boolean removeStateFiles(IProject project) {
//		String projectPath = getProjectPath(project.getName());
//		File projectDir = new File(projectPath);
//		boolean success = false;
//		if(projectDir.exists()) {
//			 success = projectDir.delete();
//		}
//		return success;
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.dtsworkshop.flextools.codemodel.IProjectStateManager#removeBuildState(org.eclipse.core.resources.IProject, org.eclipse.core.resources.IFile)
//	 */
//	public void removeBuildState(IProject project, IFile resource) {
//		
//		Map<String, BuildStateDocument> state = getProjectState(project.getName());
//		String fileRef = getStateFilename(resource);
//		state.remove(fileRef);
//	}
//
//	private String getStateFilename(IFile resource) {
//		String resourcePath = resource.getProjectRelativePath().toString();
//		String fileRef = toFilename(resourcePath);
//		return fileRef;
//	}
//	
//	private String toFilename(String resourcePath) {
//		String fileRef = resourcePath.replaceAll("/", ".");
//		return fileRef;
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.dtsworkshop.flextools.codemodel.IProjectStateManager#storeBuildState(com.dtsworkshop.flextools.model.BuildStateDocument)
//	 */
//	public void storeBuildState(BuildStateDocument state) {
//		String path = state.getBuildState().getFile().replaceAll("/", ".");
//		String projectName = state.getBuildState().getProject();
//		Map<String, BuildStateDocument> projectMap = getProjectState(projectName);
//		projectMap.put(path, state);
//		writeBuildState(state);
//	}
//	
//	/** Maps between project (name) and it's state entry */
//	public ModelInfo getInfo() {
//		ModelInfo info = new ModelInfo();
//		//TODO: Cache this info if the model hasn't changed.
//		
//		info.numberOfProjects = projectStates.keySet().size();
//		for(String projectName : projectStates.keySet()) {
//			info.numberOfStates+= projectStates.get(projectName).values().size();
//		}
//		
//		return info;
//	}
//	
//	private String toStateFilename(BuildStateDocument state) {
//		// Made up of [state directory]\[project name]\[filename];
//		String filename = getProjectPath(state.getBuildState().getProject());
//		filename += File.separator + toFilename(state.getBuildState().getFile());
//		filename += ".xml";
//		return filename;
//	}
//	
//	private String getProjectPath(String projectName) {
//		String filename = stateDirectoryRoot;
//		filename += File.separator + projectName;
//		return filename;
//	}
//	
//	private void writeBuildState(BuildStateDocument state) {
//		BuildStateType buildState = state.getBuildState();
//		String projectName = buildState.getProject();
//		boolean success = createProjectDirectory(getProjectPath(projectName));
//		if(!success) {
//			return;
//		}
//		File stateFilename = new File(toStateFilename(state));
//		writeBuildState(state, stateFilename);
//	}
//
//	private boolean createProjectDirectory(String projectName) {
//		File projectDir = new File(projectName);
//		if(!projectDir.exists()) {
//			
//			return projectDir.mkdir();
//			
//		}
//		return true;
//	}
//
//	private Map<String, BuildStateDocument> getProjectState(String projectName) {
//		if(!projectStates.containsKey(projectName)) {
//			projectStates.put(projectName, new HashMap<String, BuildStateDocument>(50));
//		}
//		Map<String, BuildStateDocument> projectMap = projectStates.get(projectName);
//		return projectMap;
//	}
//}
