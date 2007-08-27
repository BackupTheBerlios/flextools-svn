package com.dtsworkshop.flextools.refactoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;

import com.dtsworkshop.flextools.search.ClassSearcher;
import com.dtsworkshop.flextools.search.SearchQuery;
import com.dtsworkshop.flextools.search.SearchReference;

public class ClassNameRefactoring extends Refactoring {
	private static Logger log = Logger.getLogger(ClassNameRefactoring.class);
	
	private IFile typeFile;
	/**
	 * The qualified name of the type
	 */
	private String qualifiedName;
	/**
	 * The short name of the type
	 */
	private String oldShortName;
	/**
	 * The new short name for the type
	 */
	private String newShortName;
	/**
	 * The results of the search that lists the references to the type
	 */
	private List<SearchReference> references;
	
	/**
	 * Gets the new short name for the file
	 * 
	 * @return The new short name for the file
	 */
	public String getNewShortName() {
		return newShortName;
	}

	/**
	 * Gets the new short name for the file
	 * @param newShortName
	 */
	public void setNewShortName(String newShortName) {
		this.newShortName = newShortName;
	}

	public String getOldShortName() {
		return oldShortName;
	}

	public void setOldShortName(String oldShortName) {
		this.oldShortName = oldShortName;
	}

	public ClassNameRefactoring() {
		super();
	}

	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		//TODO: Perform classname conflict check
		log.debug("Performing final condition check");
		
		return new RefactoringStatus();
	}
	 
	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		log.debug("Checking initial conditions for refactoring.");
		SearchQuery query = new SearchQuery();
		query.getSearcher()
			.setExactMatch(true)
			.setLimit(ClassSearcher.LimitTo.AllOccurences)
			.setSearchFor(ClassSearcher.SearchFor.Type)
			.setSearchText(qualifiedName)
			.setCaseSensitive(true);
		
		log.debug("Running search for " + qualifiedName);
		query.run(pm);
		references = query.getSearcher().getMatches();
		List<SearchReference> badReferences = findBadReferences(references);
		RefactoringStatus status = new RefactoringStatus();
		for(SearchReference badRef : badReferences) {
			status.addWarning(String.format(
					"Reference '%s' in file '%s' has bad positions - is it included in your app?",
					badRef.getDescription(), badRef.getFilePath().getName()
			));
		}
		log.debug("Finished checking preconditions.");
		
		return status;
	}
	
	/**
	 * Does a search through the found references looking to see whether the 
	 * file hasn't been parsed correctly. Incorrectly parsed files have
	 * start or end positions of -1.
	 * 
	 * @param refs The references
	 * @return A list of references that have a start or end position of -1
	 */
	private List<SearchReference> findBadReferences(List<SearchReference> refs) {
		List<SearchReference> badRefs = new ArrayList<SearchReference>();
		for(SearchReference ref : refs) {
			if(ref.getFrom() == -1 || ref.getTo() == -1) {
				badRefs.add(ref);
			}
		}
		return badRefs;
	}
	
	

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		log.debug("Creating changes");
		CompositeChange parentChange = new CompositeChange(
			String.format(
				"Renaming class from '%s' to '%s'",
				oldShortName, newShortName
			)
		);
		Map<IFile, MultiTextEdit> fileToEditMap = new HashMap<IFile, MultiTextEdit>(40);
		for(SearchReference ref : references) {
			if(!fileToEditMap.containsKey(ref.getFilePath())) {
				fileToEditMap.put(ref.getFilePath(),
					new MultiTextEdit()
				);
			}
			MultiTextEdit change = fileToEditMap.get(ref.getFilePath());
			ReplaceEdit edit = new ReplaceEdit(
				ref.getFrom(), ref.getTo() - ref.getFrom(),
				newShortName
			);
			
			change.addChild(edit);
		}
		
		for(IFile currentFile : fileToEditMap.keySet()) {
			MultiTextEdit edit = fileToEditMap.get(currentFile);
			TextFileChange fileChange = new TextFileChange(
				String.format("Renaming %s", currentFile.getName()),
				currentFile
			);
			fileChange.setEdit(edit);
			parentChange.add(fileChange);
		}
		
		Change filenameChange = createFilenameChange();
		parentChange.add(filenameChange);
		log.debug("All changes created.");
		return parentChange;
	}
	
	private Change createFilenameChange() {
		IFile sourceFile = this.typeFile;
		String extension = sourceFile.getFileExtension();
		IPath targetFile = sourceFile.getProjectRelativePath().removeLastSegments(1);
		targetFile = targetFile.append(getNewShortName() + "." + extension);
		
		FilenameChange fileChange = new FilenameChange(sourceFile, targetFile);
		return fileChange;
	}

		
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "AsRefactoring";
	}

	/**
	 * Gets the qualified name of the target type
	 * @return
	 */
	public String getQualifiedName() {
		return qualifiedName;
	}

	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	public IProject getContainingProject() {
		return this.typeFile.getProject();
	}

	public IFile getTypeFile() {
		return typeFile;
	}

	public void setTypeFile(IFile typeFile) {
		this.typeFile = typeFile;
	}
	
	

}
