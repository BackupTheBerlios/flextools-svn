package com.dtsworkshop.flextools.search;

import java.util.ArrayList;
import java.util.List;
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
import com.dtsworkshop.flextools.model.ClassStateType;
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
		 
		//searchForClassDeclaration(info);
		
		//searchForImports(info);
		
		//searchForDeriving(info);
		ISearchCommand [] commands = new ISearchCommand[0];
		if(limit == LimitTo.AllOccurences) {
			commands = new ISearchCommand[] { 
				new DerivingSearchCommand()
				, new ImportSearchCommand()
				, new ClassDelcarationSearchCommand()
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
			commands = new ISearchCommand[0];
		}
		else if(limit == LimitTo.References) {
			commands = new ISearchCommand[0];
		}
		for(ISearchCommand command : commands) {
			XmlObject [] results = performSearch(document, command.getSearchText(info));
			for(XmlObject result : results) {
				command.processResult(info, result);
			}
		}
		
		return true;
	}


	private XmlObject[] performSearch(BuildStateDocument document, String searchQuery) {
		XmlObject [] results = null;
		
		try {
			results = document.selectPath(searchQuery);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
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
	
	private class DerivingSearchCommand implements ISearchCommand {

		public String getSearchText(SearchInfo info) {
			// TODO Auto-generated method stub
			return String.format("%s" +
					"$this//mod:ClassNode",
					info.searcher.getNamespaceDecl()
			);
		}

		public void processResult(SearchInfo info, XmlObject result) {
			Assert.isTrue(result instanceof ClassStateType);
			ClassStateType classType = (ClassStateType)result;
			String extendsName = classType.getBaseClassName();
			if(extendsName == null) {
				return;
			}
			//boolean isMatch = searchText.equals(extendsName);
			if(info.isExactMatch(extendsName)) {
				SearchReference newRef = info.createReference(classType);
				newRef.setDescription(classType.getQualifiedName());
				matches.add(newRef);
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
				newRef.setDescription(type.getName());
				matches.add(newRef);
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
				newRef.setDescription(importType.getQualifiedName());
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
				return searchText.contains(textToMatch);
			}
			else {
				return searchText.toLowerCase().contains(textToMatch.toLowerCase());
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
