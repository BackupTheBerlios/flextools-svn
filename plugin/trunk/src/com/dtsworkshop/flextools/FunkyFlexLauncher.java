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
package com.dtsworkshop.flextools;

import java.net.URL;

import com.adobe.flexbuilder.debug.launching.FlexLaunchDelegate;
import com.adobe.flexbuilder.debug.launching.IFlexLaunchConfiguration;
import com.adobe.flexbuilder.project.LaunchMode;

public class FunkyFlexLauncher extends FlexLaunchDelegate {

	public FunkyFlexLauncher() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected URL getProbableSwf(
			IFlexLaunchConfiguration flexLaunchConfiguration,
			LaunchMode launchMode) {
		return super.getProbableSwf(flexLaunchConfiguration, launchMode);
	}



}
