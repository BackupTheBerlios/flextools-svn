package com.dtsworkshop.flextools.ui.components;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class TypeRenameControl extends Composite {

	private Label label = null;
	private Text typeNameInput = null;

	public String getTypeText() {
		return typeNameInput.getText();
	}
	
	public void setTypeText(String newText) {
		typeNameInput.setText(newText);
	}
	
	public TypeRenameControl(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	
	private List<ModifyListener> modificationListeners = new ArrayList<ModifyListener>(5);
	public void addModifyListener(ModifyListener listener) {
		if(modificationListeners.contains(listener)) {
			return;
		}
		modificationListeners.add(listener);
	}
	
	public void removeModifyListener(ModifyListener listener) {
		if(modificationListeners.contains(listener)) {
			modificationListeners.remove(listener);
		}
	}
	
	
	private void initialize() {
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.widthHint = 200;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		label = new Label(this, SWT.NONE);
		label.setText("New name:");
		typeNameInput = new Text(this, SWT.BORDER);
		typeNameInput.setLayoutData(gridData);
		typeNameInput.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				for(ModifyListener listener : modificationListeners) {
					listener.modifyText(e);
				}
			}
			
		});
		this.setLayout(gridLayout);
		setSize(new Point(300, 200));
	}

}  //  @jve:decl-index=0:visual-constraint="9,10"
