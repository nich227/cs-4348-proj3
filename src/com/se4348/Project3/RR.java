/** 
 * RR.java
 * 
 * @author Ning Kevin Chen
 * NetID: nkc160130
 * Coded in: Java
 * Class: SE 4348
 * Professor: Greg Ozbirn
 */
package com.se4348.Project3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class RR extends SchedulingAlg {

	public RR(ArrayList<JobTuple> j) {
		super(j);
	}

	// Note: Quantum value for RR is 1
	@Override
	public void run() {
		Queue<JobTuple> readyQueue = new LinkedList<>();
		int cur_time = 0;
		ArrayList<JobTuple> remaining_jobs = new ArrayList<JobTuple>();
		for (JobTuple job : jobs) {
			remaining_jobs.add(job);
		}

		// Go through all of the jobs
		JobTuple cpu = null;
		while (!remaining_jobs.isEmpty()) {

			// Find any jobs that should be starting at this time
			for (JobTuple job : jobs) {
				if (job.getStartTime() == cur_time) {
					readyQueue.add(job);
				}
			}

			// Put previous job at the bottom of the queue (if it's still running)
			if (remaining_jobs.contains(cpu))
				readyQueue.add(cpu);

			// Run the top of the queue
			cpu = readyQueue.remove();

			// Make array of running jobs at this time frame
			boolean[] time_frame_vals = new boolean[jobs.size()];
			int i = 0;
			for (JobTuple job : jobs) {
				if (job.getJobId() != cpu.getJobId()) {
					time_frame_vals[i] = false;
				} else
					time_frame_vals[i] = true;
				i++;
			}

			// Add running jobs to timeframe matrix
			run_matrix.add(time_frame_vals);

			cpu.setDuration(cpu.getDuration() - 1);
			cur_time++;

			// Current job finished running (duration is 0)
			if (cpu.getDuration() == 0) {
				remaining_jobs.remove(remaining_jobs.indexOf(cpu));
			}
		}

		// Reset durations for each tuple back to original
		for (JobTuple job : jobs) {
			job.resetDuration();
		}
	}
}
