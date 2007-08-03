package com.dtsworkshop.flextools.codemodel;

import com.dtsworkshop.flextools.model.BuildStateDocument;

public interface IBuildStateVisitor {
	public boolean visit(BuildStateDocument document);
}
