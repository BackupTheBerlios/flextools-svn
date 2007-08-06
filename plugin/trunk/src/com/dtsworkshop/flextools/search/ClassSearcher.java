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

public class ClassSearcher extends AbstractSearcher implements IBuildStateVisitor {
	protected String searchText;
	protected SearchFor searchFor;
	protected LimitTo limit;
	
	public enum SearchFor {
		Type, Method, Field, Package, Constructor
	}
	
	public enum LimitTo {
		Declarations, Implementations, References, AllOccurences
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
		
		 
		searchForClassDeclaration(info);
		
		searchForImports(info);
		
		searchForDeriving(info);
		
		return true;
	}

	private void searchForClassDeclaration(SearchInfo info) {
		XmlObject[] results = performSearch(info.document, query);
		
		for(XmlObject xmlResult : results) {
			if(!(xmlResult instanceof ClassStateType)) {
				continue;
			}
			ClassStateType type = (ClassStateType)xmlResult;
			Object test = type.getDomNode().getOwnerDocument();
			boolean isMatch = searchText.equals(type.getName());
			if(!isMatch) {
				continue;
			}
			SearchReference newRef = info.createReference(type);
			newRef.setDescription(type.getName());
			matches.add(newRef);
		}
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

	public interface ISearchCommand {
		String getSearchText();
		void processResults(XmlObject [] results);
	}
	
	private void searchForDeriving(SearchInfo info) {
		XmlObject [] results = performSearch(info.document, 
			String.format("%s" +
			"$this//mod:ClassNode",
			getNamespaceDecl()
		));
		for(XmlObject xmlResult : results) {
			Assert.isTrue(xmlResult instanceof ClassStateType);
			ClassStateType classType = (ClassStateType)xmlResult;
			String extendsName = classType.getBaseClassName();
			if(extendsName == null) {
				continue;
			}
			boolean isMatch = searchText.equals(extendsName);
			if(isMatch) {
				SearchReference newRef = info.createReference(classType);
				newRef.setDescription(classType.getQualifiedName());
				matches.add(newRef);
			}
		}
	}
	
	private class SearchInfo {
		public ClassSearcher searcher;
		public BuildStateDocument document;
		public IProject project;
		public IFile containingFile;
		
		public SearchReference createReference(BuildReference sourceRef) {
			return referenceFromNodeType(project, containingFile, sourceRef);
		}
	}
	
	private void searchForImports(SearchInfo info) {
		XmlObject[] results = performSearch(info.document, 
			String.format(
				"%s" +
				"$this//mod:ImportNode",
				getNamespaceDecl()
		));
		
		for(XmlObject xmlResult : results) {
			if(!(xmlResult instanceof ImportNodeType)) {
				continue;
			}
			ImportNodeType importType = (ImportNodeType)xmlResult;
			 
			String localName = importType.getLocalName();
			if(localName == null) {
				//TODO: Work out why the local name might be null.
				continue;
			}
			boolean isMatch = localName.equals(searchText);
			if(!isMatch) {
				continue;
			}
			SearchReference newRef = info.createReference(importType);
			newRef.setDescription(importType.getQualifiedName());
			matches.add(newRef);
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

	public void setLimit(LimitTo limit) {
		this.limit = limit;
	}

	public SearchFor getSearchFor() {
		return searchFor;
	}

	public void setSearchFor(SearchFor searchFor) {
		this.searchFor = searchFor;
	}

	


}
