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
import com.dtsworkshop.flextools.builder.processors.ModelProcessor;
import com.dtsworkshop.flextools.model.BuildStateDocument;

public class FlexToolsBuilder extends IncrementalProjectBuilder {


	public static final String BUILDER_ID = "com.dtsworkshop.flextools.sampleBuilder";

	private static final String MARKER_TYPE = "com.dtsworkshop.flextools.xmlProblem";
	
//	private boolean isParseableResource(IResource resource) {
//		boolean isParseableResource = resource.getName().endsWith(".as") || resource.getName().endsWith(".mxml");
//		return isParseableResource;
//	}
//	
//	class FlexDeltaVisitor implements IResourceDeltaVisitor {
//		
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
//		 */
//		public boolean visit(IResourceDelta delta) throws CoreException {
//			IResource resource = delta.getResource();
//			log.info(String.format("SampleDeltaVisitor:visit() Visiting %s", resource.getName()));
//			boolean isParseableResource = isParseableResource(resource);
//			switch (delta.getKind()) {
//			case IResourceDelta.CHANGED:
//				if(isParseableResource) {
//					Activator.getStateManager().removeBuildState(getProject(), (IFile)resource);
//				}
//			case IResourceDelta.ADDED:
//				// handle added resource
//				
//				if(isParseableResource) {
//					IFile file = (IFile)resource;
//					log.info(String.format("Visiting resource %s", resource.getName()));
//					synchronized (CMFactory.getLockObject())
//			        {
//						try {
//							if(!runWithFile(file)) {
//								return false;
//							}
//						} catch (RuntimeException e) {
//							e.printStackTrace();
//							return false;
//						}
//			        }
//				}
//				break;
//			case IResourceDelta.REMOVED:
//				if(isParseableResource) {
//					IFile fileResource = (IFile)resource;
//					CodeModelManager.getManager().removeBuildState(getProject(), fileResource);
//				}
//				
//				break;
//			}
//			//return true to continue visiting children.
//			return true;
//		}
//	}

	static Logger log = Logger.getLogger(FlexToolsBuilder.class.getName());
	public static final String dumpLocation = "d:\\builder state";
//	
//	private static final String xmlDumpNamespace = "http://www.dtsworkshop.com/flextools/model";
//	private static final String xmlns = "mod";
//	public static void dumpNodes(String document, IASNode startNode, StringBuilder builder) {
//		builder.append(String.format(
//			"<%s:%s startPos='%d' endPos='%d' className='%s'>",
//			xmlns,
//			startNode.getNodeType(),
//			startNode.getStart(),
//			startNode.getEnd(),
//			startNode.getClass().toString()
//		));
//		String nodeText = "";
//				
//		try {
//			nodeText = document.substring(startNode.getStart()+1, startNode.getEnd()+1);
//		} catch (RuntimeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			nodeText = "error getting contents";
//		}
//		
//		builder.append(String.format("<%s:contents><![CDATA[%s]]></%s:contents>", xmlns, nodeText, xmlns));
//		
//		IASNode [] children = startNode.getChildren();
//		//builder.append("<children>");
//		for(IASNode child : children) {
//			StringBuilder childrenBuilder = new StringBuilder();
//			try {
//				dumpNodes(document, child, childrenBuilder);
//				builder.append(childrenBuilder.toString());
//			}
//			catch(RuntimeException ex) {
//				builder.append(String.format("<!-- error when='dumping children' '%s' -->", ex.getMessage()));
//				//builder.append(String.format("<gotThisFar><![CDATA[%s]>></gotThisFar>", childrenBuilder.toString()));
//			}			
//		}
//		//builder.append("</children>");
//		builder.append(String.format("</%s:%s>", xmlns, startNode.getNodeType()));
//	}
//	
//
//	
//	private boolean runWithRegisteredFile(IFile file) {
//		
//		IPath filePath = file.getLocation();
//		com.adobe.flexbuilder.codemodel.project.IProject flexProject = CMFactory.getManager().getProjectFor(getProject());
//		IFileNode fileNode = flexProject.findFileNodeInProject(filePath);
//		IASScope scopedNode = fileNode.getScope();
//		StringBuilder outputData = new StringBuilder();
//		//outputData = getOutputDataFromHandDump(file, fileNode);
//		ModelProcessor processor = new ModelProcessor();
//		
//		BuildStateDocument stateDocument = processor.getStateDocument(fileNode, file);
//		Activator.getStateManager().storeBuildState(stateDocument);
//		return true;
//	}
//
//	boolean runWithFile(IFile file) {
//		//TODO: Implement file registration properly.
//		runWithRegisteredFile(file);
//		return true;
//	}
//
//	class FlexResourceVisitor implements IResourceVisitor {
//		public boolean visit(IResource resource) {
//
//			boolean isParseableResource = isParseableResource(resource);
//			if(isParseableResource) {
//				IFile file = (IFile)resource;
//				log.info(String.format("Visiting resource %s", resource.getName()));
//				synchronized (CMFactory.getLockObject())
//		        {
//					try {
//						if(!runWithFile(file)) {
//							return false;
//						}
//					} catch (RuntimeException e) {
//						e.printStackTrace();
//						return false;
//					}
//		        }
//			}
//			return true;
//		}
//
//	}

//	private void addMarker(IFile file, String message, int lineNumber,
//			int severity) {
//		try {
//			IMarker marker = file.createMarker(MARKER_TYPE);
//			marker.setAttribute(IMarker.MESSAGE, message);
//			marker.setAttribute(IMarker.SEVERITY, severity);
//			if (lineNumber == -1) {
//				lineNumber = 1;
//			}
//			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
//		} catch (CoreException e) {
//		}
//	}

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
