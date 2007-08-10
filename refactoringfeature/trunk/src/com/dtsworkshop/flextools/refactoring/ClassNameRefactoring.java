package com.dtsworkshop.flextools.refactoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.NullChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextEditBasedChange;
import org.eclipse.ltk.core.refactoring.TextEditBasedChangeGroup;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.osgi.baseadaptor.loader.ClasspathEntry;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.ui.internal.editors.text.StatusUtil;

import com.dtsworkshop.flextools.Activator;
import com.dtsworkshop.flextools.search.ClassSearcher;
import com.dtsworkshop.flextools.search.SearchQuery;
import com.dtsworkshop.flextools.search.SearchReference;

public class ClassNameRefactoring extends Refactoring {
	private String qualifiedName;
	private String oldShortName;
	private String newShortName;
	private List<SearchReference> references;
	
	public String getNewShortName() {
		return newShortName;
	}

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
		// TODO Auto-generated method stub
		return null;
	}
	 
	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		SearchQuery query = new SearchQuery();
		query.getSearcher()
			.setExactMatch(true)
			.setLimit(ClassSearcher.LimitTo.AllOccurences)
			.setSearchFor(ClassSearcher.SearchFor.Type)
			.setSearchText(qualifiedName)
			.setCaseSensitive(true);
		
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
		
		return status;
	}
	
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
		return parentChange;
	}

	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "AsRefactoring";
	}

	public String getQualifiedName() {
		return qualifiedName;
	}

	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

}
