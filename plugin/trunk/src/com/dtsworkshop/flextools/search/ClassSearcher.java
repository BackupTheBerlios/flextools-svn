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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.xmlbeans.XmlObject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Path;

import sun.reflect.generics.tree.ClassTypeSignature;

import com.dtsworkshop.flextools.FlexToolsLog;
import com.dtsworkshop.flextools.codemodel.IBuildStateVisitor;
import com.dtsworkshop.flextools.codemodel.ModelConstants;
import com.dtsworkshop.flextools.model.BuildReference;
import com.dtsworkshop.flextools.model.BuildStateDocument;
import com.dtsworkshop.flextools.model.ClassInterfaceReference;
import com.dtsworkshop.flextools.model.ClassStateType;
import com.dtsworkshop.flextools.model.IdentifierNodeType;
import com.dtsworkshop.flextools.model.ImportNodeType;
import com.dtsworkshop.flextools.model.NodeType;

/**
 * 
 * Searcher that goes through the build states searching based upon the 
 * search parameters.
 * 
 * @author Oliver Tupman
 *
 */
public class ClassSearcher extends AbstractSearcher implements IBuildStateVisitor {
	protected String searchText;
	protected SearchFor searchFor = SearchFor.Type;
	protected LimitTo limit = LimitTo.AllOccurences;
	protected boolean isCaseSensitive = false;
	protected boolean exactMatch = false;
	
	List<BuildReference> storedMatches;
	
	/**
	 * Defines what the searcher should be looking for with regards to 
	 * the search text.
	 * <ul>
	 * 	<li>Type - Assumes the search is simply for the type name</li>
	 * 	<li>Field - The search is for a field on a class</li>
	 * 	<li>Method- The search is for a method</li>
	 * 	<li>Package - The search is for a package</li>
	 * 	<li>Constructor - The search is for a constructor for a type</li>
	 * </ul>
	 * 
	 * @author Oliver Tupman
	 *
	 */
	public enum SearchFor {
		/** Search for a type name */ Type
		, /** Search for a method */ Method
		, /** Search for a field on a class */ Field
		, /** Search for a package */ Package
		, /** Search for a constructor */Constructor
		
	}
	
	/**
	 * Determines what a search should be limited to.
	 * 
	 * @author Oliver Tupman
	 *
	 */
	public enum LimitTo {
		/** Limit to type declarations */
		Declarations
		, /** Limit to implementation of interfaces or classes */ Implementations
		, /** Limit to references to the class, rather than declarations */ References
		, /** Look for all occurences of the search text (i.e. no limiting) */ AllOccurences
		, /** Search for files that FB is not compiling */ Unreferenced
	}
	
	public ClassSearcher(String className, IWorkspace workspace) {
		super(workspace);
		this.searchText = className;
	}
	
	protected String initialiseQuery() {
		String query = String.format(
			"%s" +
			"$this//mod:ClassNode",
			getNamespaceDecl(),
			namespace,
			searchText
		);
		return query;
	}

	private static Logger log = Logger.getLogger("ClassSearcher"); 
	
	public boolean visit(BuildStateDocument document) {
		String projectName = document.getBuildState().getProject();

		SearchInfo info = new SearchInfo(this);
		info.searcher = this;
		info.project = workspace.getRoot().getProject(projectName);
		info.containingFile = getBuildStateFile(document, info.project);
		info.document = document;
		info.isCaseSensitive = isCaseSensitive;
		info.isSearchExact = this.exactMatch;
		
		log.info(String.format("Visiting file %s", info.containingFile.getName()));
		
		ISearchCommand [] commands = new ISearchCommand[0];
		if(limit == LimitTo.AllOccurences) {
			commands = new ISearchCommand[] { 
				new DerivingSearchCommand()
				, new ImportSearchCommand()
				//, new ClassDelcarationSearchCommand()
				, new ImplementationSearchCommand()
				, new ReferenceSearchCommand()
			};
		}
		else if(limit == LimitTo.Declarations) {
			commands = new ISearchCommand[] {
					new ClassDelcarationSearchCommand()
//					new DerivingSearchCommand()
//					, new ImportSearchCommand()
//					, 
				};
		}
		else if(limit == LimitTo.Implementations) {
			commands = new ISearchCommand[] {
					new ImplementationSearchCommand()
			};
		}
		else if(limit == LimitTo.References) {
			commands = new ISearchCommand[] {
				//new ReferenceSearchCommand()
			};
		}
		else if(limit == LimitTo.Unreferenced) {
			
		}
		storedMatches = new ArrayList<BuildReference>();
		for(ISearchCommand command : commands) {
			command.setSearcher(this);
			XmlObject [] results = performSearch(document, command.getSearchText(info));
			for(XmlObject result : results) {
				command.processResult(info, result);
			}
		}
		
		log.info(String.format("Got %d matches", matches.size()));
		return true;
	}


	private XmlObject[] performSearch(BuildStateDocument document, String searchQuery) {
		XmlObject [] results = null;
		
		try {
			results = document.selectPath(searchQuery);
		} catch (RuntimeException e) {
			e.printStackTrace();
			FlexToolsLog.logError(String.format("Error occurred while performing xpath search with query '%s'", searchQuery), e);
		}
		return results;
	}
	
	public String getSearchText() {
		return searchText;
	}

	public ClassSearcher setSearchText(String className) {
		this.searchText = className;
		return this;
	}

	public LimitTo getLimit() {
		return limit;
	}

	public ClassSearcher setLimit(LimitTo limit) {
		this.limit = limit;
		return this;
	}

	public SearchFor getSearchFor() {
		return searchFor;
	}

	public ClassSearcher setSearchFor(SearchFor searchFor) {
		this.searchFor = searchFor;
		return this;
	}

	public boolean isCaseSensitive() {
		return isCaseSensitive;
	}

	public ClassSearcher setCaseSensitive(boolean isCaseSensitive) {
		this.isCaseSensitive = isCaseSensitive;
		return this;
	}

	public boolean isExactMatch() {
		return exactMatch;
	}

	public ClassSearcher setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
		return this;
	}

	


}
