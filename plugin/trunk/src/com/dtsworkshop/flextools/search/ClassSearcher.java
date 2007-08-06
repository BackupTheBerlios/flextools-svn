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

public class ClassSearcher extends AbstractSearcher implements IBuildStateVisitor {
	protected String className;
	
	
	public ClassSearcher(String className, IWorkspace workspace) {
		super(workspace);
		this.className = className;
	}
	
	protected String initialiseQuery() {
		String query = String.format(
			"%s" +
			"$this//mod:ClassNode",
			getNamespaceDecl(),
			namespace,
			className
		);
		return query;
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
			Object test = type.getDomNode().getOwnerDocument();
			boolean isMatch = className.equals(type.getName());
			if(!isMatch) {
				//continue;
			}
			SearchReference newRef = referenceFromNodeType(project, containingFile, type);
			newRef.setDescription(type.getName());
			matches.add(newRef);
		}
		return true;
	}

	


}
