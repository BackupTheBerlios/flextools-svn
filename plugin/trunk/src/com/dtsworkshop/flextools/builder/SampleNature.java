/**
@BoilerplateBegin
This is the boiler plate license text.
Copyright (C) Oliver B. Tupman, 2007.
@BoilerplateEnd
*/
package com.dtsworkshop.flextools.builder;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

import com.dtsworkshop.flextools.Activator;

/**
 * The project nature for Flex Tools project. Currently users have to manually
 * add the nature to a project.
 * 
 * 
 * TODO: Make the addition of the builder automatic for any and all FlexBuilder
 * projects available.
 * TODO: Add bridge item to allow FT to ask integration items whether to add the nature
 * 
 * @author otupman
 *
 */
public class SampleNature implements IProjectNature {
	private static Logger log = Logger.getLogger(SampleNature.class);
	/**
	 * ID of this project nature
	 */
	public static final String NATURE_ID = "com.dtsworkshop.flextools.sampleNature";

	private IProject project;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#configure()
	 */
	public void configure() throws CoreException {
		IProjectDescription desc = project.getDescription();
		log.info(String.format("Adding Flex Tools nature to the project '%s'", project.getName()));
		ICommand[] commands = desc.getBuildSpec();

		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(FlexToolsBuilder.BUILDER_ID)) {
				return;
			}
		}

		ICommand[] newCommands = new ICommand[commands.length + 1];
		System.arraycopy(commands, 0, newCommands, 0, commands.length);
		ICommand command = desc.newCommand();
		command.setBuilderName(FlexToolsBuilder.BUILDER_ID);
		newCommands[newCommands.length - 1] = command;
		desc.setBuildSpec(newCommands);
		project.setDescription(desc, null);
		Activator.getDefault().getProjectManager().registerProject(project);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#deconfigure()
	 */
	public void deconfigure() throws CoreException {
		log.info(String.format("Removing the Flex Tools nature from project '%s'", project.getName()));
		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(FlexToolsBuilder.BUILDER_ID)) {
				ICommand[] newCommands = new ICommand[commands.length - 1];
				System.arraycopy(commands, 0, newCommands, 0, i);
				System.arraycopy(commands, i + 1, newCommands, i,
						commands.length - i - 1);
				description.setBuildSpec(newCommands);
				return;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#getProject()
	 */
	public IProject getProject() {
		return project;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject)
	 */
	public void setProject(IProject project) {
		this.project = project;
	}

}
