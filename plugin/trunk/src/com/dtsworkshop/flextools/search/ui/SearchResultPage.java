/**
*	Copyright (C) Oliver B. Tupman, 2007.
*	
*	This file is part of the Flex Tools Project.
*	
*	The Flex Tools Project is free software; you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation; either version 3 of the License, or
*	(at your option) any later version.
*	
*	The Flex Tools Project is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*	GNU General Public License for more details.
*	
*	You should have received a copy of the GNU General Public License
*	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.dtsworkshop.flextools.search.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.search.internal.ui.text.EditorOpener;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.SearchResultEvent;
import org.eclipse.search.ui.text.AbstractTextSearchViewPage;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE.SharedImages;

import com.adobe.flexbuilder.editors.common.editor.IFlexEditor;
import com.adobe.flexbuilder.editors.common.ui.ObjectLabelProvider;

import com.dtsworkshop.flextools.Activator;
import com.dtsworkshop.flextools.FlexToolsLog;
import com.dtsworkshop.flextools.search.ClassSearchResult;
import com.dtsworkshop.flextools.search.SearchQuery;
import com.dtsworkshop.flextools.search.SearchReference;

public class SearchResultPage extends AbstractTextSearchViewPage {
	private static Logger log = Logger.getLogger(SearchResultPage.class);
	
	public SearchResultPage() {
		super(FLAG_LAYOUT_TREE);
		log.debug("Created.");
	}

	@Override
	protected void clear() {
		// TODO: Need to clear the search page
		((TreeViewer)getViewer()).setInput(new ClassSearchResult(new SearchQuery()));
	}

	/**
	 * Basic implementation of the content provider. Currently
	 * based upon the ClassSearchResult.
	 * 
	 * @author Ollie
	 *
	 */
	private static class ClassContentProvider implements IStructuredContentProvider
	{
		private ClassSearchResult result = null;
		
		public Object[] getElements(Object inputElement) {
			// inputElement is the ClassSearchResult
			// return matches?
//			log.info("getElements()");
			if(result == null) {
				return new Object[0]; 
			}
			
			return this.result.getElements();
		}

		private Logger log = Logger.getLogger("ClassContentProvider");
		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// oldInput - old one, duh
			// newInput = ClassSearchResult
			result = (ClassSearchResult)newInput;
		}
		
	}
	
	

	//TODO: Either reimplement or borrow the EditorOpener
	private EditorOpener opener = new EditorOpener();
	
	private void handleDoubleClick(DoubleClickEvent event) {
		//TODO: Handle search result double click - show result in viewer
		Assert.isTrue(event.getSelection() instanceof StructuredSelection);
		StructuredSelection selection = (StructuredSelection)event.getSelection();
		if(selection.getFirstElement() instanceof SearchReference) {
			showReferenceInEditor((SearchReference)selection.getFirstElement());
		}
		else if(selection.getFirstElement() instanceof IFile) {
			IFile selectedFile = (IFile)selection.getFirstElement();
			try {
				
				opener.open(selectedFile, true);
			} catch (PartInitException e) {
				e.printStackTrace();
				FlexToolsLog.logError(String.format("Error initialising editor for file %s", selectedFile.getName()), e);
			}
		}
		else {
			//TODO: Throw exception on invalid double click selection
		}
	}
	
	private void showReferenceInEditor(SearchReference referenceToShow) {
		if(referenceToShow.getFrom() == -1 || referenceToShow.getTo() == -1) {
			log.warn("Either the from or to of the reference just double clicked is -1");
		}
		try {
			IEditorPart part = opener.open(referenceToShow.getFilePath(), true);
			Assert.isTrue(part instanceof IFlexEditor, "Editor is not a flex/FB editor");
			IFlexEditor editor = (IFlexEditor)part;
			TextSelection selection = new TextSelection(
				referenceToShow.getFrom(), referenceToShow.getTo() - referenceToShow.getFrom()	
			);
			part.getEditorSite().getSelectionProvider().setSelection(selection);
			//editor.selectAndRevealInCodeView(referenceToShow.getFrom(), referenceToShow.getTo());
		} catch (PartInitException e) {
			e.printStackTrace();
			FlexToolsLog.logError(String.format("Error initialising part to show file %s", referenceToShow.getFilePath().getName()), e);
		}
		finally {
			
		}
	}

	@Override
	protected void configureTableViewer(TableViewer viewer) {
		viewer.setUseHashlookup(true); //TODO: What does setUseHashlookup do?
		viewer.setContentProvider(new ClassContentProvider());
		viewer.setLabelProvider(new ClassLabelProvider());
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				handleDoubleClick(event);
			}
			
		});
	}
	

	/**
	 * Label provider, not implemented yet.
	 * 
	 * @author Ollie
	 *
	 */
	private static class ClassLabelProvider implements ITableLabelProvider {
		
		
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO: Need to work out how to obtain images
			// TODO: What images are required? Mebbe stuff from the default Eclipse?
			SearchReference ref = (SearchReference)element;
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			SearchReference ref = (SearchReference)element;
			return ref.getFilePath().getName();
		}

		private Logger log = Logger.getLogger("ClassLabelProvider");
		public void addListener(ILabelProviderListener listener) {
			//TODO: What is listening?
			// TODO Auto-generated method stub
			log.info("addListener");
		}

		public void dispose() {
			// TODO Auto-generated method stub
			log.info("dispose()");
		}

		public boolean isLabelProperty(Object element, String property) {
			//TODO: Need to work out what comes in here
			log.info("isLabelProperty");
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
			//TODO: Remove listener from list (what list?!?!)
			log.info("removeListener");
		}
		
	}
	
	private class FlexTreeContentProvider implements ITreeContentProvider {
		private Map<IFile, List<SearchReference>> mappedReferences;
		
		private void createIndex() {
			Object [] refs = result.getElements();
			for(Object currentRef : refs) {
				if(!(currentRef instanceof SearchReference)) {
					continue;
				}
				SearchReference castRef = (SearchReference)currentRef;
				IFile file = castRef.getFilePath();
				List<SearchReference> fileReferences = getFileReferences(file);
				fileReferences.add(castRef);
			}
		}

		private List<SearchReference> getFileReferences(IFile file) {
			List<SearchReference> fileReferences;
			
			if(!mappedReferences.containsKey(file)) {
				mappedReferences.put(file, new ArrayList<SearchReference>(10));
			}
			fileReferences = mappedReferences.get(file);
			return fileReferences;
		}
		
		public Object[] getElements(Object inputElement) {
			// first call: inputElement = ClassSearchResult
			Object [] elements = result.getElements();
			if(elements.length == 0) {
				return new Object[0];
			}
			createIndex();
			
			return (Object[])mappedReferences.keySet().toArray(new Object[mappedReferences.keySet().size()]);
		}

		public Object[] getChildren(Object parentElement) {
			//System.out.println("Getting children for " + parentElement);
			Object [] children = null;
			if(parentElement instanceof IFile) {
				List<SearchReference> references =getFileReferences((IFile)parentElement); 
				children = (Object[])references.toArray(new Object[references.size()]);
			}
			else {
				children = new Object[0];
			}
			return children;
		}

		public Object getParent(Object element) {
			
			return null;
		}

		public boolean hasChildren(Object element) {
			if(element instanceof IFile) {
				return true;
			}
			return false;
		}

		public void dispose() {
			mappedReferences.clear();
			result = null;
		}
		
		private void resetWithResult(ClassSearchResult newResult) {
			result = newResult;
			mappedReferences = new HashMap<IFile, List<SearchReference>>(100);
		}
		
		ClassSearchResult result;
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			//first call: oldInput - null, newInput - ClassSearchResult
			if(oldInput != newInput) {
				resetWithResult((ClassSearchResult)newInput);				
			}			
		}
		
	}
	
	@Override
	protected void configureTreeViewer(TreeViewer viewer) {
		viewer.setUseHashlookup(true);
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				handleDoubleClick(event);
			}
			
		});
		viewer.setLabelProvider(new ILabelProvider() {
						
			public Image getImage(Object element) {
				// TODO: Implement image handling!
				return null;
			}

			public String getText(Object element) {
				if(element instanceof IFile) {
					return ((IFile)element).getProjectRelativePath().toPortableString().replace("/", ".");
				}
				else if(element instanceof SearchReference){
					SearchReference ref = (SearchReference)element;
					String description = String.format(
						"%s [%d - %d]",
						ref.getDescription(), ref.getFrom(), ref.getTo()
					);
					return description;
				}
				return element.toString();
			}

			public void addListener(ILabelProviderListener listener) {
			}

			public void dispose() {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
			}
			
		});
		viewer.setContentProvider(new FlexTreeContentProvider());
	}
	
	@Override
	protected void elementsChanged(Object[] objects) {
		//TODO: Don't think this should refresh - should add a new item
		getViewer().refresh(true);
	}

}
