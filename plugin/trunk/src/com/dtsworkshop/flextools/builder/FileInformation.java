package com.dtsworkshop.flextools.builder;

import java.util.List;
import java.util.Map;

import com.adobe.flexbuilder.codemodel.internal.tree.ClassNode;
import com.adobe.flexbuilder.codemodel.internal.tree.FunctionCallNode;
import com.adobe.flexbuilder.codemodel.internal.tree.FunctionNode;
import com.adobe.flexbuilder.codemodel.internal.tree.ImportNode;
import com.adobe.flexbuilder.codemodel.internal.tree.VariableNode;
import com.adobe.flexbuilder.codemodel.tree.IASNode;

public class FileInformation {
	protected List<FunctionNode> methods;
	protected List<ClassNode> classes;
	protected List<ImportNode> imports;
	protected List<VariableNode> variables;
	protected List<ClassMethodCall> methodCalls;
}
