package com.dtsworkshop.flextools.search;

import org.eclipse.core.resources.IWorkspace;

import com.dtsworkshop.flextools.model.BuildStateDocument;

public class FunctionSearcher extends AbstractSearcher {

	public FunctionSearcher(IWorkspace workspace) {
		super(workspace);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String initialiseQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean visit(BuildStateDocument document) {
		// TODO Auto-generated method stub
		return false;
	}

}
