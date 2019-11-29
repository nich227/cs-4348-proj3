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
			int max_node = -1;
			ArrayList<Integer> max_nodes = new ArrayList<Integer>();
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
					max_nodes.add(k);
				}
			}
			// There is not a tie
			if (max_nodes.size() == 1) {
				max_node = max_nodes.get(0);
			}
			// There is at least one tie
			else if (max_nodes.size() > 1) {
				char lowest_letter = (char) 127;
				int max_node_loc = -1;
				for (Integer mn : max_nodes) {
					if (remaining_jobs.get(mn).getJobId().charAt(0) < lowest_letter) {
						lowest_letter = remaining_jobs.get(mn).getJobId().charAt(0);
						max_node_loc = mn;
					}
				}
				max_node = max_nodes.get(max_node_loc);
			}

			cur_node = max_node;

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
		for (JobTuple job : jobs) {
			job.resetDuration();
		}
	}
}
