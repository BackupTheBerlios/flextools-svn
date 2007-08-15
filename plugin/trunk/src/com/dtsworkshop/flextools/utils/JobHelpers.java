package com.dtsworkshop.flextools.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.jobs.Job;

import com.dtsworkshop.flextools.FlexToolsLog;

public class JobHelpers {
	/**
	 * Joins all of the jobs supplied.
	 * Will not return until all jobs have been joined.
	 * 
	 * Warning: can cause deadlocks, see @see Job.join()
	 * 
	 * @param jobsToJoin The list of jobs to join.
	 */
	public static void joinAllJobs(List<Job> jobsToJoin)  {
		for(Job currentJob : jobsToJoin) {
			try {
				currentJob.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				FlexToolsLog.logError(String.format("Error while waiting for a job to rejoin"), e);
			}
		}
	}

}
