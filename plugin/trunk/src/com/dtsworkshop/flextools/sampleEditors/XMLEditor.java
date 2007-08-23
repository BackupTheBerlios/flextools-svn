//package com.dtsworkshop.flextools.sampleEditors;
//
//import org.eclipse.jface.action.IAction;
//import org.eclipse.jface.action.IMenuManager;
//
//import com.adobe.flexbuilder.editors.actionscript.ActionScriptEditor;
//
//public class XMLEditor extends ActionScriptEditor {
//	
//	private ColorManager colorManager;
//	public static final String EDITOR_CONTEXT = "com.dtsworkshop.flextools.sampleEditors.XMLEditor";
//	public XMLEditor() {
//		super();
//		//colorManager = new ColorManager();
//		//setSourceViewerConfiguration(new XMLConfiguration(colorManager));
//		//setDocumentProvider(new XMLDocumentProvider());
//		setEditorContextMenuId(EDITOR_CONTEXT);
//	}
//
//	@Override
//	protected void updateSelectionDependentActions() {
//		// TODO Auto-generated method stub
//		super.updateSelectionDependentActions();
//		
//	}
//	public static final String ACTION_EXTRACT_METHOD = "com.dtsworkshop.flextools.actions.refactor.extractMethod";
//	@Override
//	protected void editorContextMenuAboutToShow(IMenuManager arg0) {
//		IAction extractMethodAction = getAction(ACTION_EXTRACT_METHOD);
//		if(extractMethodAction != null) {
//			arg0.add(extractMethodAction);			
//		}
//		
//		super.editorContextMenuAboutToShow(arg0);
//	}
//
//}
