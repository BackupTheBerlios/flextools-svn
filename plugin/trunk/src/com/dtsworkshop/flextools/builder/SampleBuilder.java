package com.dtsworkshop.flextools.builder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.internal.runtime.Log;
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
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.tree.IASNode;
import com.adobe.flexbuilder.codemodel.tree.IFileNode;
import com.adobe.flexbuilder.project.FlexProjectManager;
import com.adobe.flexbuilder.project.IFlexProject;
import com.dtsworkshop.flextools.actions.ExtractMethodAction;
import com.dtsworkshop.flextools.codemodel.CodeModelManager;
import com.dtsworkshop.flextools.model.BuildStateDocument;

public class SampleBuilder extends IncrementalProjectBuilder {


	public static final String BUILDER_ID = "com.dtsworkshop.flextools.sampleBuilder";

	private static final String MARKER_TYPE = "com.dtsworkshop.flextools.xmlProblem";
	
	class SampleDeltaVisitor implements IResourceDeltaVisitor {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			log.info(String.format("SampleDeltaVisitor:visit() Visiting %s", resource.getName()));
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				// handle added resource
				boolean isParseableResource = isParseableResource(resource);
				if(isParseableResource) {
					IFile file = (IFile)resource;
					log.info(String.format("Visiting resource %s", resource.getName()));
					synchronized (CMFactory.getLockObject())
			        {
						try {
							if(!runWithFile(file)) {
								return false;
							}
						} catch (RuntimeException e) {
							e.printStackTrace();
							return false;
						}
			        }
				}
				break;
			case IResourceDelta.REMOVED:
				// handle removed resource
				break;
			case IResourceDelta.CHANGED:
				// handle changed resource
				
				break;
			}
			//return true to continue visiting children.
			return true;
		}
	}

	private static Logger log = Logger.getLogger(SampleBuilder.class.getName());
	public static final String dumpLocation = "d:\\builder state";
	
	private static final String xmlDumpNamespace = "http://www.dtsworkshop.com/flextools/model";
	private static final String xmlns = "mod";
	public static void dumpNodes(String document, IASNode startNode, StringBuilder builder) {
		builder.append(String.format(
			"<%s:%s startPos='%d' endPos='%d' className='%s'>",
			xmlns,
			startNode.getNodeType(),
			startNode.getStart(),
			startNode.getEnd(),
			startNode.getClass().toString()
		));
		String nodeText = "";
				
		try {
			nodeText = document.substring(startNode.getStart()+1, startNode.getEnd()+1);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			nodeText = "error getting contents";
		}
		
		builder.append(String.format("<%s:contents><![CDATA[%s]]></%s:contents>", xmlns, nodeText, xmlns));
		
		IASNode [] children = startNode.getChildren();
		//builder.append("<children>");
		for(IASNode child : children) {
			StringBuilder childrenBuilder = new StringBuilder();
			try {
				dumpNodes(document, child, childrenBuilder);
				builder.append(childrenBuilder.toString());
			}
			catch(RuntimeException ex) {
				builder.append(String.format("<!-- error when='dumping children' '%s' -->", ex.getMessage()));
				//builder.append(String.format("<gotThisFar><![CDATA[%s]>></gotThisFar>", childrenBuilder.toString()));
			}			
		}
		//builder.append("</children>");
		builder.append(String.format("</%s:%s>", xmlns, startNode.getNodeType()));
	}
	

	private boolean isParseableResource(IResource resource) {
		boolean isParseableResource = resource.getName().endsWith(".as") || resource.getName().endsWith(".mxml");
		return isParseableResource;
	}

	private boolean runWithFile(IFile file) {
		
		IPath filePath = file.getLocation();
		com.adobe.flexbuilder.codemodel.project.IProject flexProject = CMFactory.getManager().getProjectFor(getProject());
		IFileNode fileNode = flexProject.findFileNodeInProject(filePath);
		StringBuilder outputData = new StringBuilder();
		//outputData = getOutputDataFromHandDump(file, fileNode);
		ModelProcessor processor = new ModelProcessor();
		
		BuildStateDocument stateDocument = processor.getStateDocument(fileNode, file);
		CodeModelManager.getManager().storeBuildState(stateDocument);
		
//		outputData.append(processor.dumpNodes(fileNode, file));
//		String fileLocation = getBuildOutputFileLocation(file);
//		
//		try {
//			File targetFile = new File(fileLocation);
//			if(!targetFile.exists()) {
//				targetFile.createNewFile();
//			}
//			BufferedWriter writer = new BufferedWriter(new FileWriter(fileLocation));
//			writer.write(outputData.toString());
//			writer.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}


	private StringBuilder getOutputDataFromHandDump(IFile file, IFileNode fileNode) {
		StringBuilder outputData = new StringBuilder();
		String fileData = getFileContents(file);
		outputData.append(String.format(
			"<%s:buildState xmlns:%s=\"%s\" file=\"%s\" project=\"%s\">",
			xmlns,
			xmlns,
			xmlDumpNamespace,
			file.getProjectRelativePath(),
			file.getProject().getName()
		));
		
		dumpNodes(fileData, fileNode, outputData);
		outputData.append(String.format("</%s:buildState>", xmlns));
		return outputData;
	}


	private String getBuildOutputFileLocation(IFile file) {
		String fileLocation = file.getProjectRelativePath().toString();
		fileLocation = fileLocation.replaceAll("/", ".");
		fileLocation = fileLocation.replaceAll(":", "-");
		
		fileLocation = dumpLocation + "\\" +  fileLocation + ".xml";
		return fileLocation;
	}

	private String getFileContents(IFile file) {
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getContents()));
			
			String input = "";
			while((input = reader.readLine()) != null) {
				builder.append(input);
				builder.append("\n");
			}
			reader.close();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.toString();
	}
	
	class SampleResourceVisitor implements IResourceVisitor {
		public boolean visit(IResource resource) {

			boolean isParseableResource = isParseableResource(resource);
			if(isParseableResource) {
				IFile file = (IFile)resource;
				log.info(String.format("Visiting resource %s", resource.getName()));
				synchronized (CMFactory.getLockObject())
		        {
					try {
						if(!runWithFile(file)) {
							return false;
						}
					} catch (RuntimeException e) {
						e.printStackTrace();
						return false;
					}
		        }
			}
			return true;
		}

	}

	private void addMarker(IFile file, String message, int lineNumber,
			int severity) {
		try {
			IMarker marker = file.createMarker(MARKER_TYPE);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			if (lineNumber == -1) {
				lineNumber = 1;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		} catch (CoreException e) {
		}
	}

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


	private void deleteMarkers(IFile file) {
		try {
			file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
		}
	}

	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		try {
			getProject().accept(new SampleResourceVisitor());
		} catch (CoreException e) {
		}
	}


	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		delta.accept(new SampleDeltaVisitor());
	}
}
