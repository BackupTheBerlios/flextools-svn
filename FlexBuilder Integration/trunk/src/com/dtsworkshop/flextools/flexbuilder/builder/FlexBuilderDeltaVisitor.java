/**
 *	Copyright (C) Oliver Tupman <otupman at dts-workshop dot com>, 2007.
 *	
 *	This file is part of the Flex Tools Project.
 *
 *    The Flex Tools Project is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    The Flex Tools Project is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.dtsworkshop.flextools.flexbuilder.builder;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.ui.texteditor.IDocumentProvider;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.common.IMXMLDataProvider;
import com.adobe.flexbuilder.codemodel.definitions.IClass;
import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.indices.IClassNameIndex;
import com.adobe.flexbuilder.codemodel.indices.ITagInformation;
import com.adobe.flexbuilder.codemodel.indices.ITagInformationIndex;
import com.adobe.flexbuilder.codemodel.internal.indices.IClassInheritanceIndex;
import com.adobe.flexbuilder.codemodel.mxml.MXMLData;
import com.adobe.flexbuilder.codemodel.mxml.MXMLTagData;
import com.adobe.flexbuilder.codemodel.mxml.MXMLTextData;
import com.adobe.flexbuilder.codemodel.mxml.MXMLUnitData;
import com.adobe.flexbuilder.codemodel.tree.IFileNode;
import com.adobe.flexbuilder.editors.actionscript.ActionScriptDocumentProvider;
import com.adobe.flexbuilder.editors.common.document.IFlexDocument;
import com.adobe.flexbuilder.editors.mxml.code.MXMLDocumentProvider;

import com.dtsworkshop.flextools.Activator;
import com.dtsworkshop.flextools.FlexToolsLog;
import com.dtsworkshop.flextools.builder.AbstractFlexBuilderDeltaVisitor;
import com.dtsworkshop.flextools.model.BuildStateDocument;
import com.dtsworkshop.flextools.model.IdentifierNodeType;

/**
 * FlexBuilder delta visitor. Processes the parse trees provided by FlexBuilder
 * therefore introducing a direct dependency upon FB. Makes use of some
 * features marked as 'internal' by FB, so it'll raise a fair few warnings.
 * 
 * @author otupman
 *
 */
public class FlexBuilderDeltaVisitor extends AbstractFlexBuilderDeltaVisitor {
	private static Logger log = Logger.getLogger(FlexBuilderDeltaVisitor.class);
	public FlexBuilderDeltaVisitor() {
		super();
		log.debug("Created.");
	}
	
	@Override
	public boolean canVisit(IResource resource) {
		return isParseableResource(resource);
	}

	/**
	 * <p>
	 * TextDocumentProvider internally connects to an IAdaptable interface
	 * that can convert to ILocationProvider (provides location information
	 * about the element) and IFile (the file itself).
	 * </p>
	 * <p>
	 * This class fakes the required information as I'm not sure what
	 * pre-built classes in Eclipse provide this information - probably
	 * just the editor input classes.
	 * </p>
	 * <p>
	 * TODO: Will an editor input class suit our requirements?
	 * </p>
	 * @author otupman
	 *
	 */
	private class FakeAdapter implements IAdaptable {
		private IFile sourceFile;
		public FakeAdapter(IFile source) {
			this.sourceFile = source;
		}
		public Object getAdapter(Class adapter) {
			if(adapter == ILocationProvider.class) {
				return new ILocationProvider() {

					public IPath getPath(Object element) {
						return sourceFile.getFullPath();
					}
					
				};
			}
			else if(adapter == IFile.class) {
				return sourceFile;
			}
			else {
				log.debug( 
					String.format("Asked for adapter '%s' I don't know of for file %s"
						, adapter.getName(), sourceFile.getName()
					)					
				);
			}
			return null;
		}		
	}
	private boolean runWithRegisteredFile(IFile file) {
		IDocumentProvider provider = getProviderForFile(file);
		if(provider == null) {
			return true;
		}
		
		IAdaptable fileAdapter = new FakeAdapter(file);
		try {
			provider.connect(fileAdapter);
		} catch (CoreException e) {
			e.printStackTrace();
			// Bail out
			return true;
		}
		IDocument doc = provider.getDocument(fileAdapter);
		provider.disconnect(file);
		IPath filePath = file.getLocation();
		com.adobe.flexbuilder.codemodel.project.IProject flexProject = CMFactory.getManager().getProjectFor(getProject());
		IFileNode fileNode = flexProject.findFileNodeInProject(filePath);
		Assert.isNotNull(fileNode);
		// Force getting the scope as this appears to trigger processing of some parse items
		fileNode.getScope(); 
		
		ModelProcessor processor = new ModelProcessor();
		IFlexDocument flexDoc = (IFlexDocument)doc;
		
		
		BuildStateDocument stateDocument = processor.getStateDocument(fileNode, file);
		if(doc instanceof IMXMLDataProvider) {			
			processMxmlFile((IFlexDocument)doc, stateDocument);			
		}
		Activator.getStateManager().storeBuildState(stateDocument);
		return true;
	}

	/**
	 * Post-processes an MXML file. This is intended to be called once the
	 * standard build parser has generated a state based upon the AS model.
	 * 
	 * This then goes through and appends the relevant information to do with
	 * things such as tag names and the classes that they relate to.
	 * 
	 * @param doc The document to work with
	 */
	private void processMxmlFile(IFlexDocument doc, BuildStateDocument stateDoc) {
		IMXMLDataProvider mxmlProvider = (IMXMLDataProvider)doc;
		MXMLData mxmlData = mxmlProvider.getMXMLData();
		List<MXMLUnitData> units = mxmlData.getUnits();
		
		
		for(MXMLUnitData mxmlUnit : units) {
			String logData = "";
			if(mxmlUnit instanceof MXMLTagData) {
				MXMLTagData tag = (MXMLTagData)mxmlUnit;
				logData = String.format(
					"name: %s", tag.getName()	
				);
				String name = tag.getName();
				ITagInformationIndex nameIndex = (ITagInformationIndex)CMFactory.getManager().getProjectForDocument(doc).getIndex(ITagInformationIndex.ID);
				ITagInformation tagInfo = nameIndex.getTagInformation(name, mxmlProvider);
				IClass tagClass = tagInfo.getClassForTag();
				if(tagClass == null) {
					log.warn("Null class for tag " + name);
					continue;
				}
				IdentifierNodeType classRef = stateDoc.getBuildState().addNewClassReference();
				
				classRef.setQualifiedName(tagClass.getQualifiedName());
				classRef.setName(tagClass.getShortName());
				int nameStart = tag.getNameStart();
				int nameEnd = tag.getNameEnd();
				nameStart = nameEnd - tagClass.getShortName().length();
				
				classRef.setStartPos(nameStart);
				classRef.setEndPos(nameEnd);
			}
			else {
				MXMLTextData data = (MXMLTextData)mxmlUnit;
				logData = String.format(
					"text: %s", data.getText()	
				);
			}
			logData+= String.format(" from: %d, to: %d", mxmlUnit.getStart(), mxmlUnit.getEnd());
			log.debug(logData);
		}
	}

	/**
	 * Factory method to get the appropriate document provider for the supplied file.
	 * 
	 * TODO: Surely FlexBuilder provides this functionality... investigate FB packages for an implementaiton
	 * 
	 * @param file The file to get an appropriate provider for
	 * @return The documentp provider for the file (or null, if one cannot be located).
	 */
	private IDocumentProvider getProviderForFile(IFile file) {
		IDocumentProvider provider = null;
		if(file.getFileExtension().equals("as")) {
			provider = new ActionScriptDocumentProvider();
		}
		else if(file.getFileExtension().equals("mxml")){
			provider = new MXMLDocumentProvider();
		}
		else {
			log.debug("Cannot parse file " + file.getName());
		}
		return provider;
	}

	boolean runWithFile(IFile file) {
		//TODO: Implement file registration properly.
		runWithRegisteredFile(file);
		return true;
	}

	
	@Override
	public boolean changed(IResource resource) {
		IFile file = (IFile)resource;
		log.debug(String.format("Processing file %s", resource.getName()));
		synchronized (CMFactory.getLockObject())
        {
			try {
				if(!runWithFile(file)) {
					return false;
				}
			} catch (RuntimeException e) {
				e.printStackTrace();
				FlexToolsLog.logError(e);
				log.error(String.format("Caught exception processing file %s", resource.getName()), e);
				return false;
			}
        }
		return true;
	}

	private boolean isParseableResource(IResource resource) {
		boolean isParseableResource = resource.getName().endsWith(".as") || resource.getName().endsWith(".mxml");
		return isParseableResource;
	}
}