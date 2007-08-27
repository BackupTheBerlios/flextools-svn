package com.dtsworkshop.flextools.refactoring;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

public class FilenameChange extends Change {
	private IFile sourceFile;
	private IPath targetPath;
	
	public FilenameChange(IFile sourceFile, IPath targetPath) {
		this.sourceFile = sourceFile;
		this.targetPath = targetPath;
	}

	@Override
	public Object getModifiedElement() {
		return sourceFile;
	}

	@Override
	public String getName() {
		return String.format("Renaming %s to %s", sourceFile.getName(), targetPath.toString());
	}

	@Override
	public void initializeValidationData(IProgressMonitor pm) {

	}

	@Override
	public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		boolean exists = targetPath.toFile().exists();
		if(exists) {
			return RefactoringStatus.createFatalErrorStatus(
				String.format(
					"Cannot rename %s to %s as %s already exists"
					, sourceFile.getName()
					, targetPath.toString()
					, targetPath.toString()
				)
			);
		}
		return RefactoringStatus.create(Status.OK_STATUS);
	}
	private Change createUndo(IFile fromFile, IPath toPath) {
		return new FilenameChange(fromFile, toPath);
	}
	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		IPath currentPath = sourceFile.getProjectRelativePath();
		IPath newName = targetPath.removeFirstSegments(currentPath.segmentCount() - 1);
		
		IPath movePath = newName;
		this.sourceFile.move(movePath, true, pm);
		Change undoAction = new FilenameChange(sourceFile, currentPath);
		return undoAction;
		
//		File file = this.sourceFile.getLocation().toFile();
//		File targetFile = this.targetPath.toFile();
//		if(targetFile.exists()) {
//			throw new RuntimeException("File " + targetFile.getName() + " exists.");
//		}
//		
//		try {
//			if(!targetFile.createNewFile()) {
//				throw new RuntimeException("Error creating new file " + targetFile.getName());
//			}
//			BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
//			BufferedReader reader = new BufferedReader(new FileReader(file));
//			String inputString = "";
//			while((inputString = reader.readLine()) != null) {
//				writer.write(inputString);
//			}
//			writer.close();
//			reader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new RuntimeException("IO error creating new file", e);
//		}
//		IProject containingProject = this.sourceFile.getProject();
//		IPath toPath = this.sourceFile.getLocation();
//		if(!file.delete()) {
//			targetFile.delete();
//			throw new RuntimeException("Could not delete source file.");
//		}
//		IFile createdFile = containingProject.getFile(targetPath); 
//		return createUndo(createdFile, toPath);
	}

}
