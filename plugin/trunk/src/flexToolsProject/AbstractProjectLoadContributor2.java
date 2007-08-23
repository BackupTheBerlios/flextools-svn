package flexToolsProject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.dtsworkshop.flextools.project.AbstractProjectLoadContributor;

public class AbstractProjectLoadContributor2 extends
		Job {

	public AbstractProjectLoadContributor2(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return Status.OK_STATUS;
	}

}
