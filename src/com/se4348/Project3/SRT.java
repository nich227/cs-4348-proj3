package com.se4348.Project3;

import java.util.ArrayList;

public class SRT extends SchedulingAlg {

	public SRT(ArrayList<JobTuple> j) {
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
			int min_node = -1;
			ArrayList<JobTuple> min_nodes = new ArrayList<>();
			int i = 0;
			for (JobTuple node : remaining_jobs) {
				if (node.getDuration() < min && cur_time >= node.getStartTime()) {
					min = node.getDuration();
				}
				i++;
			}
			for (JobTuple node : remaining_jobs) {
				if (node.getDuration() == min && cur_time >= node.getStartTime()) {
					min_nodes.add(node);
				}
			}
			if (min_nodes.size() == 1) {
				min_node = remaining_jobs.indexOf(min_nodes.get(0));
			} else if (min_nodes.size() > 1) {
				char lowest_letter = (char) 127;
				JobTuple lowest_node = null;
				for (JobTuple node : min_nodes) {
					if (remaining_jobs.get(remaining_jobs.indexOf(node)).getJobId().charAt(0) < lowest_letter) {
						lowest_letter = remaining_jobs.get(remaining_jobs.indexOf(node)).getJobId().charAt(0);
						lowest_node = node;
					}
				}
				min_node = remaining_jobs.indexOf(lowest_node);
			}

			cur_node = min_node;

			// Iterate until job finished (preemptive though and will interrupt if shorter
			// process found)
			while (remaining_jobs.get(cur_node).getDuration() > 0) {
				// Check node with lowest expected runtime (given it's already started)
				min = Integer.MAX_VALUE;
				min_node = 0;
				i = 0;
				for (JobTuple node : remaining_jobs) {
					if (node.getDuration() < min && cur_time >= node.getStartTime()) {
						min = node.getDuration();
						min_node = i;
					}
					i++;
				}
				cur_node = min_node;

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