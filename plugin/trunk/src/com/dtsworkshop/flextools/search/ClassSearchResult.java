package com.dtsworkshop.flextools.search;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.search.ui.text.IEditorMatchAdapter;
import org.eclipse.search.ui.text.IFileMatchAdapter;

public class ClassSearchResult extends AbstractTextSearchResult {
	protected SearchQuery query;
	
	public ClassSearchResult(SearchQuery query) {
		this.query = query;
	}
	
	@Override
	public IEditorMatchAdapter getEditorMatchAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFileMatchAdapter getFileMatchAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLabel() {
		// TODO Auto-generated method stub
		return "Class Search Result";
	}

	public ISearchQuery getQuery() {
		// TODO Auto-generated method stub
		return query;
	}

	public String getTooltip() {
		// TODO Auto-generated method stub
		return "ActionScript class search";
	}

}
