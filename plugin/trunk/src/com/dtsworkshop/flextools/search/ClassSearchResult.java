package com.dtsworkshop.flextools.search;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.search.ui.text.IEditorMatchAdapter;
import org.eclipse.search.ui.text.IFileMatchAdapter;
import org.eclipse.search.ui.text.Match;
import org.eclipse.search2.internal.ui.SearchView;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;

import com.dtsworkshop.flextools.model.NodeType;

public class ClassSearchResult extends AbstractTextSearchResult implements SearchQuery.ISearchQueryResultListener {
	protected SearchQuery query;
	
	
	
	public void newResult(SearchReference reference) {
		Match newMatch = new Match(reference, reference.getFrom(), reference.getTo() - reference.getFrom());
		addMatch(newMatch);
	}

	public ClassSearchResult(SearchQuery query) {
		this.query = query;

	}
	
	private static class FlexToolsEditorMatchAdapter implements IEditorMatchAdapter, IFileMatchAdapter {
		private ClassSearchResult result;
		public FlexToolsEditorMatchAdapter(ClassSearchResult result) {
			this.result = result;
			
		}
		public Match[] computeContainedMatches(AbstractTextSearchResult result, IFile file) {
			//TODO: What comes in here - what's the purpose of the IFile
			this.result = (ClassSearchResult)result;
			return this.result.getMatches(file);
		}

		public IFile getFile(Object element) {
			return ((SearchReference)element).getFilePath();
		}

		private Match [] getMatchFromResult(IFile file) {
			Object [] results = result.getElements();
//			for(SearchReference currentReference : results) {
//				if(currentReference.getFilePath().equals(file)) {
//					return result.getMatches(currentReference);
//				}
//			}
			return new Match[0];
		}

		public Match[] computeContainedMatches(AbstractTextSearchResult result, IEditorPart editor) {
			this.result = (ClassSearchResult)result;
			//the editor is XMLEditor, editor input is a File with the path relative to the workspace root
			FileEditorInput fileInput = (FileEditorInput)editor.getEditorInput();
			Match [] fileMatches = getMatchFromResult(fileInput.getFile());
			return fileMatches;
		}

		public boolean isShownInEditor(Match match, IEditorPart editor) {
			// TODO: What to do with this one?
			// match - current match to be displayed
			// editor - is it shown in the editor...?
			
			return true;
		}
		
	}
	
	@Override
	public IEditorMatchAdapter getEditorMatchAdapter() {
		// TODO Auto-generated method stub
		return new FlexToolsEditorMatchAdapter(this);
	}

	@Override
	public IFileMatchAdapter getFileMatchAdapter() {
		// TODO: Create better match adapter
		return new FlexToolsEditorMatchAdapter(this);
	}

	public ImageDescriptor getImageDescriptor() {
		// TODO: Investigate ImageDescriptors
		return null;
	}

	public String getLabel() {
		// TODO Auto-generated method stub
		return String.format(
				"Flex search for '%s'", 
				this.query.getClassNameSearch()
		);
	}

	public ISearchQuery getQuery() {
		return query;
	}

	public String getTooltip() {
		return String.format(
				"Flex search for '%s'",
				this.query.getClassNameSearch()
		);
	}

}
