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

import org.apache.xmlbeans.XmlObject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Path;

import sun.reflect.generics.tree.ClassTypeSignature;

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


	public boolean visit(BuildStateDocument document) {
		String projectName = document.getBuildState().getProject();

		SearchInfo info = new SearchInfo();
		info.searcher = this;
		info.project = workspace.getRoot().getProject(projectName);
		info.containingFile = getBuildStateFile(document, info.project);
		info.document = document;
		info.isCaseSensitive = isCaseSensitive;
		info.isSearchExact = this.exactMatch;
		
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
		
		for(ISearchCommand command : commands) {
			XmlObject [] results = performSearch(document, command.getSearchText(info));
			for(XmlObject result : results) {
				command.processResult(info, result);
			}
		}
		
		Set<SearchReference> searchMatches = info.getMatches();
		for(SearchReference ref : searchMatches) {
			matches.add(ref);
		}
		return true;
	}


	private XmlObject[] performSearch(BuildStateDocument document, String searchQuery) {
		XmlObject [] results = null;
		
		try {
			results = document.selectPath(searchQuery);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * Defines a command that can take part in the search. Essentially
	 * an implementor should construct the appropriate xpath query
	 * and will then be called with the results.
	 * 
	 * @author Oliver Tupman
	 *
	 */
	public interface ISearchCommand {
		/** 
		 * Gets the XPath command to execute upon the build state.
		 * 
		 * @param info The SearchInfo that defines the general parameters
		 * @return The constructed xpath
		 */
		String getSearchText(SearchInfo info);
		/**
		 * Process a result from the xpath query
		 * 
		 * @param info The search information/parameters
		 * @param result The result found
		 */
		void processResult(SearchInfo info, XmlObject result);
	}
	
	private class UnreferencedSearchCommand implements ISearchCommand {

		public String getSearchText(SearchInfo info) {
			return String.format(
				"%s " +
				"$this//*"
			);
		}

		public void processResult(SearchInfo info, XmlObject result) {
			Assert.isTrue(result instanceof BuildReference);
			BuildReference ref = (BuildReference)result;
			boolean isUnreferenced = ref.getStartPos() == -1 || ref.getEndPos() == -1;
			
		}
		
	}
	
	private class DerivingSearchCommand implements ISearchCommand {

		public String getSearchText(SearchInfo info) {
			return String.format("%s" +
					"$this//mod:extends",
					info.searcher.getNamespaceDecl()
			);
		}

		public void processResult(SearchInfo info, XmlObject result) {
			Assert.isTrue(result instanceof ClassInterfaceReference);
			ClassInterfaceReference classType = (ClassInterfaceReference)result;
			String extendsName = classType.getShortName();
			
			//boolean isMatch = searchText.equals(extendsName);
			if(info.isMatch(extendsName)) {
				info.addReference(classType, 
					String.format("Extends %s", extendsName)
				);
			}			
		}		
	}


	private class ClassDelcarationSearchCommand implements ISearchCommand {

		public String getSearchText(SearchInfo info) {
			// TODO Auto-generated method stub
			return String.format(
					"%s" +
					"$this//mod:ClassNode",
					getNamespaceDecl()
			);
		}

		public void processResult(SearchInfo info, XmlObject result) {
			if(!(result instanceof ClassStateType)) {
				return;
			}
			ClassStateType type = (ClassStateType)result;
			Object test = type.getDomNode().getOwnerDocument();
			if(info.isExactMatch(type.getName())) {
				SearchReference newRef = info.createReference(type);
				newRef.setDescription("Class delcaration " + type.getName());
				matches.add(newRef);
			}
		}
		
	}
	
	private class ImplementationSearchCommand implements ISearchCommand {

		public String getSearchText(SearchInfo info) {
			return String.format(
				"%s " +
				"$this//mod:implements",
				getNamespaceDecl()
			);
		}

		public void processResult(SearchInfo info, XmlObject result) {
			Assert.isTrue(result instanceof ClassInterfaceReference);
			ClassInterfaceReference ref = (ClassInterfaceReference)result;
			if(info.isMatch(ref.getShortName())) {
				info.addReference(ref,
					String.format(
						"Implements %s", ref.getShortName()
				));
			}
		}
		
	}
	
	public class ReferenceSearchCommand implements ISearchCommand {

		public String getSearchText(SearchInfo info) {
			return String.format(
				"%s " +
				"$this//mod:IdentifierNode",
				getNamespaceDecl()
			);
		}

		public void processResult(SearchInfo info, XmlObject result) {
			Assert.isTrue(result instanceof IdentifierNodeType);
			IdentifierNodeType node = (IdentifierNodeType)result;
			String comparisonText = (info.isSearchExact) ? node.getQualifiedName() : node.getName();
			if(info.isMatch(comparisonText)) {
				info.addReference(
						node, 
						"Identifier reference " + comparisonText
				);
			}
		}
		
	}
	
	private class ImportSearchCommand implements ISearchCommand {

		public String getSearchText(SearchInfo info) {
			// TODO Auto-generated method stub
			return String.format(
					"%s" +
					"$this//mod:ImportNode",
					getNamespaceDecl()
			);
		}

		public void processResult(SearchInfo info, XmlObject result) {
			if(!(result instanceof ImportNodeType)) {
				return;
			}
			ImportNodeType importType = (ImportNodeType)result;
			 
			String localName = importType.getLocalName();
			if(localName == null) {
				//TODO: Work out why the local name might be null.
				return;
			}
			if(info.isExactMatch(localName)) {
				SearchReference newRef = info.createReference(importType);
				newRef.setDescription("Import ref " + importType.getQualifiedName());
				matches.add(newRef);
			}
		}
		
	}
	
	/**
	 * Defines the search information/parameters.
	 * 
	 * 
	 * @author Oliver Tupman
	 *
	 */
	private class SearchInfo {
		public ClassSearcher searcher;
		public BuildStateDocument document;
		public IProject project;
		public IFile containingFile;
		public boolean isCaseSensitive;
		public boolean isSearchExact = false;
		
		private Set<SearchReference> matches = new HashSet<SearchReference>(100);
		
		public Set<SearchReference> getMatches() {
			return matches;
		}
		
		/**
		 * Convenience method to create a reference from the supplied build
		 * state match.
		 * 
		 * @param sourceRef The build match/reference that should be given to the user
		 * @return The constructed search reference.
		 */
		public SearchReference createReference(BuildReference sourceRef) {
			return referenceFromNodeType(project, containingFile, sourceRef);
		}
		
		public SearchReference addReference(BuildReference sourceRef, String description) {
			SearchReference newRef = createReference(sourceRef);
			newRef.setDescription(description);
			//matches.add(newRef);
			if(!this.matches.contains(newRef)) {
				this.matches.add(newRef);
			}
			return newRef;
		}
		
		/**
		 * Determine whether the text provided is a match against the search text.
		 * The match will occur exact or in-exact depending on the search options
		 * provided by the client code.
		 * 
		 * 
		 * @param textToMatch The text to compare with the search text
		 * @return True - is a match; false otherwise.
		 */
		public boolean isMatch(String textToMatch) {
			if(exactMatch) {
				return isExactMatch(textToMatch);
			}
			else {
				return containsText(textToMatch);
			}
		}

		/**
		 * Determines whether the text provided contains the text to search for.
		 * Case sensitivity is determined by the options provided by the client
		 * code.
		 * 
		 * @param textToMatch The text to look for a match
		 * @return True - the text is contained in the parameter; false otherwise
		 */
		public boolean containsText(String textToMatch) {
			if(textToMatch == null) {
				return false;
			}
			if(isCaseSensitive) {
				return textToMatch.contains(searchText);
			}
			else {
				return textToMatch.toLowerCase().contains(searchText.toLowerCase());
			}
		}

		/**
		 * Determines whether the text provided is an exact match with the search
		 * text.
		 * 
		 * @param textToMatch The text to try and match with.
		 * @return True - the text matches the search text; false otherwise
		 */
		public boolean isExactMatch(String textToMatch) {
			if(textToMatch == null) {
				return false;
			}
			if(isCaseSensitive) {
				return searchText.equals(textToMatch);
			}
			else {
				return searchText.compareToIgnoreCase(textToMatch) == 0;
			}
		}
	}
	
	

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String className) {
		this.searchText = className;
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
