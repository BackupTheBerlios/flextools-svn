package com.dtsworkshop.flextools.sampleEditors;

import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.revisions.RevisionInformation;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.INavigationLocation;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.IStatusField;
import org.eclipse.ui.texteditor.ITextEditorExtension3.InsertMode;

import com.adobe.flexbuilder.editors.actionscript.ActionScriptEditor;
import com.adobe.flexbuilder.editors.common.document.IFlexDocument;
import com.adobe.flexbuilder.editors.common.editor.IDoneTypingListener;
import com.adobe.flexbuilder.editors.common.ui.folding.IFoldingType;

public class XMLEditor extends ActionScriptEditor {
	
	private ColorManager colorManager;

	public XMLEditor() {
		super();
		//colorManager = new ColorManager();
		//setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		//setDocumentProvider(new XMLDocumentProvider());
	}
	public void dispose() {
		//colorManager.dispose();
		super.dispose();
	}
	public void addDoneTypingListener(IDoneTypingListener arg0) {
		super.addDoneTypingListener(arg0);
	}
	public void addPropertyListener(IPropertyListener l) {
		super.addPropertyListener(l);
	}
	public void addRulerContextMenuListener(IMenuListener listener) {
		super.addRulerContextMenuListener(listener);
	}
	public void close(boolean save) {
		super.close(save);
	}
	public INavigationLocation createEmptyNavigationLocation() {
		return super.createEmptyNavigationLocation();
	}
	public INavigationLocation createNavigationLocation() {
		return super.createNavigationLocation();
	}
	public void createPartControl(Composite arg0) {
		super.createPartControl(arg0);
	}
	public void doneTyping(IDocument arg0) {
		super.doneTyping(arg0);
	}
	public void doRevertToSaved() {
		super.doRevertToSaved();
	}
	public void doSave(IProgressMonitor progressMonitor) {
		super.doSave(progressMonitor);
	}
	public void doSaveAs() {
		super.doSaveAs();
	}
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}
	public IAction getAction(String actionID) {
		return super.getAction(actionID);
	}
	public Object getAdapter(Class arg0) {
		return super.getAdapter(arg0);
	}
	public String getContentDescription() {
		return super.getContentDescription();
	}
	public IFlexDocument getCurrentActiveDocument() {
		return super.getCurrentActiveDocument();
	}
	public IDocumentProvider getDocumentProvider() {
		return super.getDocumentProvider();
	}
	public IEditorInput getEditorInput() {
		return super.getEditorInput();
	}
	public IPreferenceStore getEditorPreferenceStore() {
		return super.getEditorPreferenceStore();
	}
	public IEditorSite getEditorSite() {
		return super.getEditorSite();
	}
	public IRegion getHighlightRange() {
		return super.getHighlightRange();
	}
	public int getInsertionPointOffset() {
		return super.getInsertionPointOffset();
	}
	public InsertMode getInsertMode() {
		return super.getInsertMode();
	}
	public int getOrientation() {
		return super.getOrientation();
	}
	public String getPartName() {
		return super.getPartName();
	}
	public ISelectionProvider getSelectionProvider() {
		ISelectionProvider provider = super.getSelectionProvider();
		return provider;
	}
	public IWorkbenchPartSite getSite() {
		return super.getSite();
	}
	public StyledText getStyledText() {
		return super.getStyledText();
	}
	public String getTitle() {
		return super.getTitle();
	}
	public Image getTitleImage() {
		return super.getTitleImage();
	}
	public String getTitleToolTip() {
		return super.getTitleToolTip();
	}
	public Annotation gotoAnnotation(boolean forward) {
		return super.gotoAnnotation(forward);
	}
	public int hashCode() {
		return super.hashCode();
	}
	public void historyNotification(OperationHistoryEvent arg0) {
		super.historyNotification(arg0);
	}
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
	}
	public boolean isChangeInformationShowing() {
		return super.isChangeInformationShowing();
	}
	public boolean isDirty() {
		return super.isDirty();
	}
	public boolean isEditable() {
		return super.isEditable();
	}
	public boolean isEditorInputModifiable() {
		return super.isEditorInputModifiable();
	}
	public boolean isEditorInputReadOnly() {
		return super.isEditorInputReadOnly();
	}
	public boolean isSaveAsAllowed() {
		return super.isSaveAsAllowed();
	}
	public boolean isSaveOnCloseNeeded() {
		return super.isSaveOnCloseNeeded();
	}
	public void markAsContentDependentAction(String actionId, boolean mark) {
		super.markAsContentDependentAction(actionId, mark);
	}
	public void markAsPropertyDependentAction(String actionId, boolean mark) {
		super.markAsPropertyDependentAction(actionId, mark);
	}
	public void markAsSelectionDependentAction(String actionId, boolean mark) {
		super.markAsSelectionDependentAction(actionId, mark);
	}
	public void markAsStateDependentAction(String actionId, boolean mark) {
		super.markAsStateDependentAction(actionId, mark);
	}
	public void propertyChange(PropertyChangeEvent arg0) {
		super.propertyChange(arg0);
	}
	public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent arg0) {
		super.propertyChange(arg0);
	}
	public void removeActionActivationCode(String actionID) {
		super.removeActionActivationCode(actionID);
	}
	public void removeDoneTypingListener(IDoneTypingListener arg0) {
		super.removeDoneTypingListener(arg0);
	}
	public void removePropertyListener(IPropertyListener l) {
		super.removePropertyListener(l);
	}
	public void removeRulerContextMenuListener(IMenuListener listener) {
		super.removeRulerContextMenuListener(listener);
	}
	public void resetHighlightRange() {
		super.resetHighlightRange();
	}
	public void selectAndReveal(int start, int length) {
		super.selectAndReveal(start, length);
	}
	public void selectAndRevealInCodeView(int arg0, int arg1, boolean arg2) {
		super.selectAndRevealInCodeView(arg0, arg1, arg2);
	}
	public void selectAndRevealInCodeView(int arg0, int arg1) {
		super.selectAndRevealInCodeView(arg0, arg1);
	}
	public void setAction(String actionID, IAction action) {
		super.setAction(actionID, action);
	}
	public void setActionActivationCode(String actionID, char activationCharacter, int activationKeyCode, int activationStateMask) {
		super.setActionActivationCode(actionID, activationCharacter, activationKeyCode, activationStateMask);
	}
	public void setFocus() {
		super.setFocus();
	}
	public void setHighlightRange(int offset, int length, boolean moveCursor) {
		super.setHighlightRange(offset, length, moveCursor);
	}
	public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data) {
		super.setInitializationData(cfig, propertyName, data);
	}
	
	public void setInsertMode(InsertMode newMode) {
		super.setInsertMode(newMode);
	}
	public void setStatusField(IStatusField field, String category) {
		super.setStatusField(field, category);
	}
	public void showBusy(boolean busy) {
		super.showBusy(busy);
	}
	public void showChangeInformation(boolean show) {
		super.showChangeInformation(show);
	}
	public void showHighlightRangeOnly(boolean showHighlightRangeOnly) {
		super.showHighlightRangeOnly(showHighlightRangeOnly);
	}
	public void showRevisionInformation(RevisionInformation info, String quickDiffProviderId) {
		super.showRevisionInformation(info, quickDiffProviderId);
	}
	public boolean showsHighlightRangeOnly() {
		return super.showsHighlightRangeOnly();
	}
	public void toggleFoldingType(IFoldingType arg0, boolean arg1) {
		super.toggleFoldingType(arg0, arg1);
	}
	public String toString() {
		return super.toString();
	}
	public void updatePartControl(IEditorInput input) {
		super.updatePartControl(input);
	}
	public boolean validateEditorInputState() {
		return super.validateEditorInputState();
	}

}
