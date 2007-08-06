package com.dtsworkshop.flextools.search.ui;

import java.util.logging.Logger;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
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
import org.eclipse.ui.PartInitException;

import com.adobe.flexbuilder.editors.common.editor.IFlexEditor;
import com.dtsworkshop.flextools.search.ClassSearchResult;
import com.dtsworkshop.flextools.search.SearchReference;

public class SearchResultPage extends AbstractTextSearchViewPage {

	public SearchResultPage() {
		super(FLAG_LAYOUT_FLAT);
	}

	@Override
	protected void clear() {
		// TODO: Need to clear the search page

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
			log.info("getElements()");
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
			log.info("getColumnImage()");
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
	
	//TODO: Either reimplement or borrow the EditorOpener
	private EditorOpener opener = new EditorOpener();
	
	private void handleDoubleClick(DoubleClickEvent event) {
		//TODO: Handle search result double click - show result in viewer
		Assert.isTrue(event.getSelection() instanceof StructuredSelection);
		StructuredSelection selection = (StructuredSelection)event.getSelection();
		if(selection.getFirstElement() instanceof SearchReference) {
			showReferenceInEditor((SearchReference)selection.getFirstElement());
		}
		else {
			//TODO: Throw exception on invalid double click selection
		}
	}
	
	private void showReferenceInEditor(SearchReference referenceToShow) {
		try {
			IEditorPart part = opener.open(referenceToShow.getFilePath(), true);
			Assert.isTrue(part instanceof IFlexEditor, "Editor is not a flex/FB editor");
			IFlexEditor editor = (IFlexEditor)part;
			editor.selectAndRevealInCodeView(referenceToShow.getFrom(), referenceToShow.getTo());
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void configureTreeViewer(TreeViewer viewer) {
		//TODO: Need to implement tree-based search result viewer
	}
	private Logger log = Logger.getLogger(SearchResultPage.class.getName());
	@Override
	protected void elementsChanged(Object[] objects) {
		//TODO: Don't think this should refresh - should add a new item
		getViewer().refresh(true);
	}

}
