package com.dtsworkshop.flextools.refactoring.ui;

import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.dtsworkshop.flextools.refactoring.ClassNameRefactoring;
import com.dtsworkshop.flextools.ui.components.TypeRenameControl;

import javax.swing.JButton;
import java.awt.Dimension;

public class TypeNameInputPage extends UserInputWizardPage {

	private TypeRenameControl renameControl;
	private ClassNameRefactoring refactorer;
	
	public TypeNameInputPage(String name, ClassNameRefactoring refactorer) {
		super(name);
		this.refactorer = refactorer;
	}

	public void createControl(Composite parent) {
		renameControl = new TypeRenameControl(parent, SWT.NONE);
		setControl(renameControl);
		renameControl.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				refactorer.setNewShortName(renameControl.getTypeText());
			}
			
		});
		renameControl.setTypeText(refactorer.getOldShortName());
	}

}
