package com.se4348.Project3;

import java.util.ArrayList;

public class FCFS extends SchedulingAlg {
	public FCFS(ArrayList<JobTuple> j) {
		super(j);
	}

	public void run() {
		// Visited array of nodes
		boolean[] visited = new boolean[jobs.size()];
		for (int i = 0; i < jobs.size(); i++)
			visited[i] = false;

		int cur_node = 0; // Current node
		int remaining_time = 0; // Remaining time for current running job
		boolean stop = false;
		
		while (!stop) {
			// Keep running until all nodes have been visited
			for (boolean visit : visited) {
				if (visit == false) {
					stop = false;
					break;
				} else {
					stop = true;
					continue;
				}
			}

			// Check next minimum node
			if (remaining_time == 0) {
				int min = Integer.MAX_VALUE;
				int min_node = 0;
				int i = 0;
				for (JobTuple node : jobs) {
					if (node.getStartTime() < min && !visited[i])
					{
						min = node.getStartTime();
						min_node = i;
					}
					i++;
				}
				cur_node = min_node;
				visited[min_node] = true;
				remaining_time = jobs.get(cur_node).getDuration();
			}
			
			
			//Make an array of all running jobs during this place in time
			boolean[] time_frame_vals = new boolean[jobs.size()];
			for(int i = 0; i < jobs.size(); i++) {
				if(i == cur_node) time_frame_vals[i] = true;
				else time_frame_vals[i] = false;
			}
			
			run_matrix.add(time_frame_vals);
			
			
			// Time has passed (after all operations have completed)
			remaining_time--;
		}
	}
}
