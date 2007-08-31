package com.dtsworkshop.flextools.flexbuilder.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import com.adobe.flexbuilder.codemodel.common.CMFactory;
import com.adobe.flexbuilder.codemodel.definitions.IDefinition;
import com.adobe.flexbuilder.codemodel.project.IProject;
import com.adobe.flexbuilder.codemodel.tree.IASNode;
import com.adobe.flexbuilder.codemodel.tree.IExpressionNode;
import com.adobe.flexbuilder.codemodel.tree.IFileNode;
import com.adobe.flexbuilder.editors.common.document.IFlexDocument;
import com.adobe.flexbuilder.editors.common.editor.IFlexEditor;
import com.dtsworkshop.flextools.search.ClassSearcher;
import com.dtsworkshop.flextools.search.SearchQuery;
import com.dtsworkshop.flextools.search.ClassSearcher.LimitTo;
import com.dtsworkshop.flextools.search.ClassSearcher.SearchFor;


public class FindReferencesAction extends AbstractTypeEditorActionDelegate implements IEditorActionDelegate {
	public FindReferencesAction() {
		super();
	}

	@Override
	protected void runWithType(TypeInfo info) {
		SearchQuery typeQuery = new SearchQuery(info.qualifiedName);
		typeQuery.getSearcher()
		.setExactMatch(true)
		.setLimit(ClassSearcher.LimitTo.AllOccurences)
		.setSearchFor(ClassSearcher.SearchFor.Type)
		.setSearchText(info.qualifiedName)
		.setCaseSensitive(true);

		NewSearchUI.runQueryInBackground(typeQuery);
	}
}
