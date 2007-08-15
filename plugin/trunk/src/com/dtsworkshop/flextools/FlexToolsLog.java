/**
 * Borrowed from the sample code in the book "Eclipse: Building Commercial
 * -quality plugins, 2nd edition". Highly recommended!
 */
package com.dtsworkshop.flextools;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class FlexToolsLog {
	public static void logError(Throwable exception) {
		logError("Unexpected exception", exception);
	}
	
	public static void logError(String message, Throwable exception) {
		log(IStatus.ERROR, IStatus.OK, message, exception);
	}
	
	public static void log(int severity, int code, String message,
	      Throwable exception) {
	    log(createStatus(severity, code, message, exception));
	}
	
	public static void log(int severity, String message) {
		log(severity, IStatus.OK, message, null);
	}

	public static void logWarning(String message) {
		log(IStatus.WARNING, message);
	}
	
	public static void logInfo(String message) {
		log(IStatus.INFO, message);
	}
	
	public static void logError(String message) {
		log(IStatus.ERROR, message);
	}
	
	public static IStatus createStatus(int severity, int code,
	      String message, Throwable exception) {
	   return new Status(severity, Activator.PLUGIN_ID, code, message, exception);
	}

	public static void log(IStatus status) {
	   Activator.getDefault().getLog().log(status);
	}
}
