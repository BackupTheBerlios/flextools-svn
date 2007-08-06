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
package com.dtsworkshop.flextools.editor;

import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.omg.PortableInterceptor.InterceptorOperations;

import com.adobe.flexbuilder.editors.actionscript.ActionScriptEditor;
import com.adobe.flexbuilder.editors.common.document.IFlexDocument;
import com.adobe.flexbuilder.editors.common.editor.IDoneTypingListener;
import com.adobe.flexbuilder.editors.common.editor.IFlexEditor;
import com.adobe.flexbuilder.editors.common.ui.folding.IFoldingType;

public class FlexToolsAsEditor 
		implements IDoneTypingListener, IFlexEditor, IOperationHistoryListener  {
	ActionScriptEditor editor = new ActionScriptEditor();

	public void addDoneTypingListener(IDoneTypingListener arg0) {
		editor.addDoneTypingListener(arg0);
	}

	public void addPropertyListener(IPropertyListener listener) {
		editor.addPropertyListener(listener);
	}

	public void createPartControl(Composite arg0) {
		editor.createPartControl(arg0);
	}

	public void dispose() {
		editor.dispose();
	}

	public void doneTyping(IDocument arg0) {
		editor.doneTyping(arg0);
	}

	public void doSave(IProgressMonitor monitor) {
		editor.doSave(monitor);
	}

	public void doSaveAs() {
		editor.doSaveAs();
	}

	public boolean equals(Object arg0) {
		return editor.equals(arg0);
	}

	public Object getAdapter(Class arg0) {
		return editor.getAdapter(arg0);
	}

	public IFlexDocument getCurrentActiveDocument() {
		return editor.getCurrentActiveDocument();
	}

	public IEditorInput getEditorInput() {
		return editor.getEditorInput();
	}

	public IPreferenceStore getEditorPreferenceStore() {
		return editor.getEditorPreferenceStore();
	}

	public IEditorSite getEditorSite() {
		return editor.getEditorSite();
	}

	public int getInsertionPointOffset() {
		return editor.getInsertionPointOffset();
	}

	public IWorkbenchPartSite getSite() {
		return editor.getSite();
	}

	public StyledText getStyledText() {
		return editor.getStyledText();
	}

	public String getTitle() {
		return editor.getTitle();
	}

	public Image getTitleImage() {
		return editor.getTitleImage();
	}

	public String getTitleToolTip() {
		return editor.getTitleToolTip();
	}

	public int hashCode() {
		return editor.hashCode();
	}

	public void historyNotification(OperationHistoryEvent arg0) {
		editor.historyNotification(arg0);
	}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		editor.init(site, input);
	}

	public boolean isDirty() {
		return editor.isDirty();
	}

	public boolean isSaveAsAllowed() {
		return editor.isSaveAsAllowed();
	}

	public boolean isSaveOnCloseNeeded() {
		return editor.isSaveOnCloseNeeded();
	}

	public void propertyChange(PropertyChangeEvent arg0) {
		editor.propertyChange(arg0);
	}

	public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent arg0) {
		editor.propertyChange(arg0);
	}

	public void removeDoneTypingListener(IDoneTypingListener arg0) {
		editor.removeDoneTypingListener(arg0);
	}

	public void removePropertyListener(IPropertyListener listener) {
		editor.removePropertyListener(listener);
	}

	public void selectAndRevealInCodeView(int arg0, int arg1, boolean arg2) {
		editor.selectAndRevealInCodeView(arg0, arg1, arg2);
	}

	public void selectAndRevealInCodeView(int arg0, int arg1) {
		editor.selectAndRevealInCodeView(arg0, arg1);
	}

	public void setFocus() {
		editor.setFocus();
	}

	public void toggleFoldingType(IFoldingType arg0, boolean arg1) {
		editor.toggleFoldingType(arg0, arg1);
	}

	public String toString() {
		return editor.toString();
	}
	
}
