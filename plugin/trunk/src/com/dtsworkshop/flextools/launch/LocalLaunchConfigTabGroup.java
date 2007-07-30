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
	@Override
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
