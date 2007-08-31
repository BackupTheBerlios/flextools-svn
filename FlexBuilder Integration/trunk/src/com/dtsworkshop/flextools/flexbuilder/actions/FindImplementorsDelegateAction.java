package com.dtsworkshop.flextools.flexbuilder.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import com.dtsworkshop.flextools.search.ClassSearcher;
import com.dtsworkshop.flextools.search.SearchQuery;


public class FindImplementorsDelegateAction extends AbstractTypeEditorActionDelegate implements IEditorActionDelegate {

	@Override
	protected void runWithType(TypeInfo info) {
		SearchQuery typeQuery = new SearchQuery(info.getQualifiedName());
		typeQuery.getSearcher()
		.setExactMatch(true)
		.setLimit(ClassSearcher.LimitTo.Implementations)
		.setSearchFor(ClassSearcher.SearchFor.Type)
		.setSearchText(info.getQualifiedName())
		.setCaseSensitive(true);

		NewSearchUI.runQueryInBackground(typeQuery);
	}

}
