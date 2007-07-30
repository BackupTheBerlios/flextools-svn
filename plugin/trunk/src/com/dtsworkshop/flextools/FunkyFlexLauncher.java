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
		// TODO Auto-generated method stub
		return super.getProbableSwf(flexLaunchConfiguration, launchMode);
	}
	
	

}
