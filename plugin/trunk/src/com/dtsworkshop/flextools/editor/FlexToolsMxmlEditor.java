package com.dtsworkshop.flextools.editor;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.INavigationLocation;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;

import com.adobe.flexbuilder.editors.common.document.IFlexDocument;
import com.adobe.flexbuilder.editors.mxml.MXMLEditor;

public class FlexToolsMxmlEditor extends MXMLEditor {

	public void gotoMarker(IMarker marker) {
		// TODO Auto-generated method stub

	}

	public void partActivated(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	public void partBroughtToTop(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	public void partClosed(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	public void partDeactivated(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	public void partOpened(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	public void resourceChanged(IResourceChangeEvent event) {
		// TODO Auto-generated method stub

	}

	public IFlexDocument getCurrentActiveDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPreferenceStore getEditorPreferenceStore() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getInsertionPointOffset() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void selectAndRevealInCodeView(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public void selectAndRevealInCodeView(int arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	public IEditorInput getEditorInput() {
		// TODO Auto-generated method stub
		return null;
	}

	public IEditorSite getEditorSite() {
		// TODO Auto-generated method stub
		return null;
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// TODO Auto-generated method stub

	}

	public void addPropertyListener(IPropertyListener listener) {
		// TODO Auto-generated method stub

	}

	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public IWorkbenchPartSite getSite() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	public Image getTitleImage() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitleToolTip() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removePropertyListener(IPropertyListener listener) {
		// TODO Auto-generated method stub

	}

	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSaveOnCloseNeeded() {
		// TODO Auto-generated method stub
		return false;
	}

	public INavigationLocation createEmptyNavigationLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public INavigationLocation createNavigationLocation() {
		// TODO Auto-generated method stub
		return null;
	}

}
