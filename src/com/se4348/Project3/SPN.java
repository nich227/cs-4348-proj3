/** 
 * SPN.java
 * 
 * @author Ning Kevin Chen
 * NetID: nkc160130
 * Coded in: Java
 * Class: SE 4348
 * Professor: Greg Ozbirn
 */
package com.se4348.Project3;

import java.util.ArrayList;

public class SPN extends SchedulingAlg {

	public SPN(ArrayList<JobTuple> j) {
		super(j);
	}

	@Override
	public void run() {
		ArrayList<JobTuple> remaining_jobs = new ArrayList<JobTuple>();
		for (JobTuple job : jobs) {
			remaining_jobs.add(job);
		}
		int cur_time = 0;

		// Iterate until all jobs have been visited
		int cur_node = 0;
		while (!remaining_jobs.isEmpty()) {

			// Check node with lowest expected runtime (given it's already started)
			int min = Integer.MAX_VALUE;
			ArrayList<Integer> jobs_starting_now = new ArrayList<>();
			int i = 0;
			for (JobTuple node : remaining_jobs) {
				if (node.getDuration() < min && cur_time >= node.getStartTime()) {
					min = node.getDuration();
				}
				i++;
			}
			i = 0;
			for (JobTuple node : remaining_jobs) {
				if (node.getDuration() == min && cur_time >= node.getStartTime()) {
					jobs_starting_now.add(i);
				}
				i++;
			}
			cur_node = jobs_starting_now.get(0);
			jobs_starting_now.remove(0);
			if (!jobs_starting_now.isEmpty()) {
				for (Integer job : jobs_starting_now) {
					remaining_jobs.get(job).setStartTime(remaining_jobs.get(job).getStartTime() + 1);
				}
			}

			// Iterate until job finished
			while (remaining_jobs.get(cur_node).getDuration() > 0) {
				// Make array of running jobs at this time frame
				boolean[] time_frame_vals = new boolean[jobs.size()];
				int j = 0;
				for (JobTuple job : jobs) {
					if (job.getJobId() != remaining_jobs.get(cur_node).getJobId()) {
						time_frame_vals[j] = false;
					} else
						time_frame_vals[j] = true;
					j++;
				}

				run_matrix.add(time_frame_vals);
				remaining_jobs.get(cur_node).setDuration(remaining_jobs.get(cur_node).getDuration() - 1);
				cur_time++;
			}

			// Remove current node from list of remaining jobs to run
			remaining_jobs.remove(cur_node);
		}

		// Reset durations for each tuple back to original
		for (JobTuple job : jobs) {
			job.resetDuration();
		}
	}

}
