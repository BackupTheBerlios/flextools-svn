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
package com.dtsworkshop.flextools.launch;

import java.util.logging.Level;
import java.util.logging.Logger;


import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;
import org.eclipse.debug.ui.RefreshTab;



public class LocalLaunchConfigTabGroup extends AbstractLaunchConfigurationTabGroup implements ILaunchConfigurationTabGroup {
	private ILaunchConfigurationTab[] tabs;
	
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		tabs = new ILaunchConfigurationTab[] {
			new LocalLaunchConfigTab()
			, new FlashVarsTab()
			, new RefreshTab()
			, new CommonTab()
		};
		
		for(ILaunchConfigurationTab tab : tabs) {
			tab.setLaunchConfigurationDialog(dialog);
		}
		setTabs(tabs);
	}

}
