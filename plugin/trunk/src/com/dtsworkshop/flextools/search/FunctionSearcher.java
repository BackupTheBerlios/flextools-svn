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
