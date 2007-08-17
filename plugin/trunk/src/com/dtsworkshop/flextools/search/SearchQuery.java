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
package com.dtsworkshop.flextools.search;

import java.awt.event.ItemListener;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.core.text.TextSearchEngine;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;

import com.dtsworkshop.flextools.Activator;
import com.dtsworkshop.flextools.codemodel.IProjectStateManager;

public class SearchQuery implements ISearchQuery {
	private Logger log = Logger.getLogger(SearchQuery.class);
	
	public SearchQuery() {
		result = new ClassSearchResult(this);
		searcher = new ClassSearcher(
			"",
			ResourcesPlugin.getWorkspace()
		);
		log.debug("Created search query");
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
		log.debug(String.format("Running search for %s", this.className));
		IProjectStateManager manager = Activator.getStateManager();
		manager.acceptVisitor(searcher, monitor);
		log.debug(String.format("Searcher found %d matches.", searcher.getMatches().size()));
		for(SearchReference ref : searcher.getMatches()) {
			if(listener != null) {
				listener.newResult(ref);
			}
			log.debug(String.format(
				"Match in file %s in project %s", ref.getFilePath().getName(),
				ref.getProject().getName()
			));
		}

		return new Status(Status.INFO, "com.dtsworkshop.flextools", Status.OK, "Success", null);
	}
	public static final String SEARCHING = "searching";
	private ClassSearcher searcher;
}
