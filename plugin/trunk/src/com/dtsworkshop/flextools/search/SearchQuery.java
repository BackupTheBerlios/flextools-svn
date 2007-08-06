package com.dtsworkshop.flextools.search;

import java.awt.event.ItemListener;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.core.text.TextSearchEngine;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;

import com.dtsworkshop.flextools.codemodel.CodeModelManager;

public class SearchQuery implements ISearchQuery {
	
	public SearchQuery() {
		result = new ClassSearchResult(this);
	}
	
	public boolean canRerun() {
		return false;
	}

	public boolean canRunInBackground() {
		//TODO: Need to work out what locking needs to occur on the workspace
		return true;
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
	
	public String getClassNameSearch() {
		return className;
	}
	
	public SearchQuery(String className) {
		this();
		this.className = className;
		listener = result;
		searcher = new ClassSearcher(
				className,
				ResourcesPlugin.getWorkspace()
			);
	}
	
	public ClassSearcher getSearcher() {
		return this.searcher;
	}
	
	public interface ISearchQueryResultListener {
		void newResult(SearchReference reference);
	}
	
	
	
	private ISearchQueryResultListener listener;

	public IStatus run(IProgressMonitor monitor)
			throws OperationCanceledException {
		
		CodeModelManager manager = CodeModelManager.getManager();
		manager.acceptVisitor(searcher, monitor);
		System.out.println(String.format("Searcher found %d matches.", searcher.getMatches().size()));
		for(SearchReference ref : searcher.getMatches()) {
			if(listener != null) {
				listener.newResult(ref);
			}
			System.out.println(String.format(
				"Match in file %s in project %s", ref.getFilePath().getName(),
				ref.getProject().getName()
			));
		}

		return new Status(Status.INFO, "com.dtsworkshop.flextools", Status.OK, "Success", null);
	}
	public static final String SEARCHING = "searching";
	private ClassSearcher searcher;
}
