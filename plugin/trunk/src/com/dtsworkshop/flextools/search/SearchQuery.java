package com.dtsworkshop.flextools.search;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;

import com.dtsworkshop.flextools.codemodel.CodeModelManager;

public class SearchQuery implements ISearchQuery {
	
	public boolean canRerun() {
		return false;
	}

	public boolean canRunInBackground() {
		return false;
	}

	public String getLabel() {
		// TODO Auto-generated method stub
		return "SearchQuery:getLabel()";
	}
	protected ClassSearchResult result;
	
	public ISearchResult getSearchResult() {
		// TODO Auto-generated method stub
		return result;
	}
	protected String className;
	
	public SearchQuery(String className) {
		this.className = className;
	}

	public IStatus run(IProgressMonitor monitor)
			throws OperationCanceledException {
		ClassSearcher searcher = new ClassSearcher(
				className,
				ResourcesPlugin.getWorkspace()
			);
		CodeModelManager.getManager().acceptVisitor(searcher);
		System.out.println(String.format("Searcher found %d matches.", searcher.getMatches().size()));
		for(SearchReference ref : searcher.getMatches()) {
			System.out.println(String.format(
				"Match in file %s in project %s", ref.getFilePath().getName(),
				ref.getProject().getName()
			));
		}
		result = new ClassSearchResult(this);
		return new Status(Status.OK, "com.dtsworkshop.flextools", Status.OK, "Success", null);
	}

}
