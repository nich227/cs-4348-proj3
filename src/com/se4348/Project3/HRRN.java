/** 
 * HRRN.java
 * 
 * @author Ning Kevin Chen
 * NetID: nkc160130
 * Coded in: Java
 * Class: SE 4348
 * Professor: Greg Ozbirn
 */
package com.se4348.Project3;

import java.util.ArrayList;

public class HRRN extends SchedulingAlg {

	public HRRN(ArrayList<JobTuple> j) {
		super(j);
	}

	@Override
	public void run() {

		int cur_time = 0;
		// Iterate until all jobs have been visited
		ArrayList<JobTuple> remaining_jobs = new ArrayList<JobTuple>();
		for (JobTuple job : jobs) {
			remaining_jobs.add(job);
		}
		int cur_node;

		// But wait - run the earliest scheduled job first!
		// Check node with lowest start time
		int min = Integer.MAX_VALUE;
		int min_node = 0;
		int i = 0;
		for (JobTuple node : remaining_jobs) {
			if (node.getStartTime() < min) {
				min = node.getStartTime();
				min_node = i;
			}
			i++;
		}
		cur_node = min_node;

		// Running job that starts first
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
		remaining_jobs.remove(cur_node);

		// Now running all other jobs
		while (!remaining_jobs.isEmpty()) {

			// Choose max response ratio
			double max_val = Double.MIN_VALUE;
			ArrayList<Integer> jobs_starting_now = new ArrayList<>();
			for (int k = 0; k < remaining_jobs.size(); k++) {
				int wait_time = cur_time - remaining_jobs.get(k).getStartTime();
				double rr = (wait_time + remaining_jobs.get(k).getDuration())
						/ (remaining_jobs.get(k).getDuration() / 1.0);
				if (rr > max_val && cur_time >= remaining_jobs.get(k).getStartTime()) {
					max_val = rr;
				}
			}
			for (int k = 0; k < remaining_jobs.size(); k++) {
				int wait_time = cur_time - remaining_jobs.get(k).getStartTime();
				double rr = (wait_time + remaining_jobs.get(k).getDuration())
						/ (remaining_jobs.get(k).getDuration() / 1.0);
				if (rr == max_val && cur_time >= remaining_jobs.get(k).getStartTime()) {
					jobs_starting_now.add(k);
				}
			}
			cur_node = jobs_starting_now.get(0);
			jobs_starting_now.remove(0);
			if (!jobs_starting_now.isEmpty()) {
				// Some jobs that start at the same time must now start later
				if (!jobs_starting_now.isEmpty()) {
					for (int j = 1; j < jobs_starting_now.size(); j++) {
						remaining_jobs.get(jobs_starting_now.get(j))
								.setStartTime(remaining_jobs.get(jobs_starting_now.get(j)).getStartTime() + 1);
					}
				}
			}

			// Run the job
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

			remaining_jobs.remove(cur_node);
		}

		// Reset durations for each tuple back to original
		for (

		JobTuple job : jobs) {
			job.resetDuration();
		}
	}
}
