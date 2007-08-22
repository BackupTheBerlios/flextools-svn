package com.dtsworkshop.flextools.flexbuilder.initialisation;

import java.util.Stack;

import org.apache.log4j.Logger;
import org.eclipse.core.internal.filebuffers.ExtensionsRegistry;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.IDocument;
import org.eclipse.search.internal.ui.text.EditorOpener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.definitions.IClass;
import com.adobe.flexbuilder.codemodel.indices.IClassNameIndex;
import com.adobe.flexbuilder.codemodel.indices.IIndex;
import com.adobe.flexbuilder.codemodel.project.IProjectLoadListener;
import com.adobe.flexbuilder.codemodel.project.IRegistrar;
import com.adobe.flexbuilder.codemodel.tree.IASNode;
import com.adobe.flexbuilder.codemodel.tree.IFileNode;
import com.adobe.flexbuilder.editors.actionscript.ActionScriptDocumentProvider;
import com.adobe.flexbuilder.editors.actionscript.ActionScriptEditor;
import com.adobe.flexbuilder.editors.actionscript.ActionScriptPlugin;
import com.adobe.flexbuilder.editors.common.CommonPlugin;
import com.adobe.flexbuilder.editors.common.EditorUtility;
import com.adobe.flexbuilder.editors.common.document.IFlexDocument;
import com.adobe.flexbuilder.editors.common.editor.FlexInformationControl;
import com.adobe.flexbuilder.editors.common.editor.FlexSourceViewer;

import com.adobe.flexbuilder.project.IFlexProject;
import com.dtsworkshop.flextools.project.AbstractProjectLoadContributor;

import com.adobe.flexbuilder.editors.actionscript.asdoc.*;

public class FbProjectLoader extends AbstractProjectLoadContributor  {
	private static Logger log = Logger.getLogger(FbProjectLoader.class);
	
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
		
		private void tryThis1(IPath filePath) {
			ActionScriptDocumentProvider provider = (ActionScriptDocumentProvider)ActionScriptPlugin.getDefault().getDocumentProvider();
			try {
				IFile file = getProject().getFile(filePath);
				provider.connect(file);
				IDocument doc1 = provider.getDocument(file);
				IFlexDocument doc = (IFlexDocument)CMFactory.getManager().getDocumentForPath(filePath);
				//LookupManager.Instance().getDefinition(doc, 1);
				EditorUtility.getOffsetInformation(doc, 1);
				provider.disconnect(file);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(RuntimeException ex) {
				ex.printStackTrace();
			}
			
			//IEditorPart part = EditorUtility.openIFileInEditor(doc.getIFile());
		}

		public void loading(String arg0) {
			//System.out.println("loading() - " + arg0);
			IPath filePath = new Path(arg0);
			IDocument doc = CMFactory.getManager().getDocumentForPath(filePath);
			try {
				tryThis1(filePath);
			}
			catch(RuntimeException e) {
				log.debug("Caught exception while running tryThis1", e);
			}
			if(doc instanceof IFlexDocument) {
				int referenceCount = ((IFlexDocument)doc).getReferenceCount();
				log.debug(String.format("References to %s = %d", arg0, referenceCount));
			}
			
			log.debug(String.format("Loading %s", arg0));
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
				//System.out.println("adsf");
				
				
				try {
					getProject().accept(new IResourceVisitor() {

						public boolean visit(IResource resource) throws CoreException {
							if(resource instanceof IFile) {
								IFile file = (IFile)resource;
								ActionScriptDocumentProvider provider = new ActionScriptDocumentProvider();
								
								try {
									provider.connect(file);
									IDocument temp = provider.getDocument(file);
									temp = temp;
									provider.disconnect(file);
								} catch (RuntimeException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								IDocument doc = CMFactory.getManager().getDocumentForPath(file.getProjectRelativePath());
								com.adobe.flexbuilder.codemodel.project.IProject fProject = CMFactory.getManager().getProjectFor(getProject());
								
								IFileNode fileNode = fProject.findFileNodeInProject(file.getProjectRelativePath());
								IASNode appNode = fProject.getApplicationRoot();
								IClassNameIndex index = (IClassNameIndex)fProject.getIndex(IClassNameIndex.ID);
								IClass [] allClasses = index.getAllClasses();
								log.debug("Hello!");
							}
							return true;
						}
						
					});
				} catch (CoreException e) {
					e.printStackTrace();
				}
				
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
