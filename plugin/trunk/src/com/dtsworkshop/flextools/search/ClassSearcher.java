package com.dtsworkshop.flextools.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.Path;

import sun.reflect.generics.tree.ClassTypeSignature;

import com.dtsworkshop.flextools.codemodel.IBuildStateVisitor;
import com.dtsworkshop.flextools.codemodel.ModelConstants;
import com.dtsworkshop.flextools.model.BuildStateDocument;
import com.dtsworkshop.flextools.model.ClassStateType;
import com.dtsworkshop.flextools.model.NodeType;

public class ClassSearcher implements IBuildStateVisitor {

	protected List<SearchReference> matches;
	protected String className;
	protected String namespace = ModelConstants.xmlNamespace;
	protected String query;
	protected IWorkspace workspace;
	
	public void setNamespace(String newNamespace) {
		this.namespace = newNamespace;
	}
	
	public ClassSearcher(String className, IWorkspace workspace) {
		this.className = className;
		this.workspace = workspace;
		query = initialiseQuery();
		matches = new ArrayList<SearchReference>(100);
	}
	/*
	 String queryExpression =
    "declare namespace xq='http://xmlbeans.apache.org/samples/xquery/employees';" +
    "$this/xq:employees/xq:employee/xq:phone[contains(., '(206)')]";
	 */
	
	protected String initialiseQuery() {
		String query = String.format(
			"declare namespace mod='%s';" +
			"$this//mod:ClassNode",
			namespace,
			className
		);
		return query;
	}
	
	public List<SearchReference> getMatches() {
		return matches;
	}
	
	public boolean visit(BuildStateDocument document) {
		String projectName = document.getBuildState().getProject();
		IProject project = null;
		IFile containingFile = null;
		
		XmlObject [] results = null;
		
		try {
			results = document.selectPath(query);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		if(results.length > 0) {
			project = workspace.getRoot().getProject(projectName);
			containingFile = getBuildStateFile(document, project);
		}
		
		for(XmlObject xmlResult : results) {
			if(!(xmlResult instanceof ClassStateType)) {
				continue;
			}
			ClassStateType type = (ClassStateType)xmlResult;
			boolean isMatch = className.equals(type.getName());
			if(!isMatch) {
				continue;
			}
			SearchReference newRef = new SearchReference();
			newRef.setFrom(type.getStartPos());
			newRef.setTo(type.getEndPos());
			newRef.setDescription(type.getName());
			newRef.setProject(project);
			newRef.setFilePath(containingFile);
			matches.add(newRef);
		}
		return true;
	}

	private IFile getBuildStateFile(BuildStateDocument document, IProject project) {
		IFile containingFile = project.getFile(document.getBuildState().getFile());
		return containingFile;
	}

}
