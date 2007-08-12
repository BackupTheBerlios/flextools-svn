package com.dtsworkshop.flextools.flexbuilder.builder;

import com.dtsworkshop.flextools.Activator;

public class Registrar {
	static {
		Activator.getDefault().addDeltaVisitor(
			new FlexBuilderDeltaVisitor()	
		);
	}
}
