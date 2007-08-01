package com.dtsworkshop.flextools.sampleEditors;

import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
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
	public static final String EDITOR_CONTEXT = "com.dtsworkshop.flextools.sampleEditors.XMLEditor";
	public XMLEditor() {
		super();
		//colorManager = new ColorManager();
		//setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		//setDocumentProvider(new XMLDocumentProvider());
		setEditorContextMenuId(EDITOR_CONTEXT);
	}

	@Override
	protected void updateSelectionDependentActions() {
		// TODO Auto-generated method stub
		super.updateSelectionDependentActions();
		
	}
	public static final String ACTION_EXTRACT_METHOD = "com.dtsworkshop.flextools.actions.refactor.extractMethod";
	@Override
	protected void editorContextMenuAboutToShow(IMenuManager arg0) {
		IAction extractMethodAction = getAction(ACTION_EXTRACT_METHOD);
		if(extractMethodAction != null) {
			arg0.add(extractMethodAction);			
		}
		
		super.editorContextMenuAboutToShow(arg0);
	}

}
