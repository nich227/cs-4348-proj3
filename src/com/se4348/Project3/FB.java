/** 
 * FB.java
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
import java.util.concurrent.Semaphore;

public class FB extends SchedulingAlg {

	// The only job of this thread is to add new jobs to the highest queue
	class AddJobs extends Thread {
		private volatile boolean th_running = true;

		public void terminate() {
			th_running = false;
		}

		public void run() {
			while (th_running) {
				try {
					fb_ready.acquire();
				} catch (InterruptedException e1) {
				}
				try {
					acc_queue.acquire();
				} catch (InterruptedException e) {
				}
				try {
					acc_time.acquire();
				} catch (InterruptedException e) {
				}
				ArrayList<JobTuple> jobs_added = new ArrayList<>();
				for (JobTuple job : jobs) {
					if (job.getStartTime() == cur_time && !fbq.get(0).contains(job)) {
						jobs_added.add(job);
					}
				}
				if (jobs_added.size() == 1) {
					fbq.get(0).add(jobs_added.get(0));
				} else if (jobs_added.size() > 1) {
					char lowest_letter = (char) 127;
					JobTuple lowest_node = null;
					for (JobTuple job : jobs_added) {
						if (remaining_jobs.get(remaining_jobs.indexOf(job)).getJobId().charAt(0) < lowest_letter) {
							lowest_letter = remaining_jobs.get(remaining_jobs.indexOf(job)).getJobId().charAt(0);
							lowest_node = job;
						}
					}
					fbq.get(0).add(lowest_node);
				}

				acc_time.release();
				acc_queue.release();
				addjobs_ready.release();

				// Simple trick to make thread terminate upon seeing boolean
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	protected final int NUM_QUEUES = 3;
	protected int cur_time = 0;
	protected ArrayList<Queue<JobTuple>> fbq = new ArrayList<>();
	protected ArrayList<JobTuple> remaining_jobs = new ArrayList<JobTuple>();

	protected Semaphore acc_time = new Semaphore(1);
	protected Semaphore acc_queue = new Semaphore(1);
	protected Semaphore fb_ready = new Semaphore(0);
	protected Semaphore addjobs_ready = new Semaphore(0);
	protected AddJobs job_listener = new AddJobs();

	public FB(ArrayList<JobTuple> j) {
		super(j);

		// Initialize the feedback queues
		for (int i = 0; i < NUM_QUEUES; i++)
			fbq.add(new LinkedList<JobTuple>());

		// Start the add jobs thread
		job_listener.start();
	}

	@Override
	public void run() {

		// Run until all jobs complete
		for (JobTuple job : jobs) {
			remaining_jobs.add(job);
		}
		JobTuple cpu = null;
		while (!remaining_jobs.isEmpty()) {
			// Wait and see if there is supposed to be any starting jobs at this time
			fb_ready.release();
			try {
				addjobs_ready.acquire();
			} catch (InterruptedException e) {
			}
			try {
				acc_queue.acquire();
			} catch (InterruptedException e1) {
			}

			// Find queue to run job from (starting from top queue)
			int num_queue_run = -1;
			for (int i = 0; i < fbq.size(); i++) {

				if (!fbq.get(i).isEmpty()) {
					num_queue_run = i;
					break;
				}
			}

			if(num_queue_run == -1)
				break;

			// Run current top job in CPU
			cpu = fbq.get(num_queue_run).remove();
			acc_queue.release();

			boolean queue_has_jobs = false;
			// Run until there are other jobs waiting in the queue
			while (!queue_has_jobs) {
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

				// Add time (time has passed)
				try {
					acc_time.acquire();
				} catch (InterruptedException e) {
				}
				cur_time++;
				acc_time.release();

				// Current job finished running (duration is 0)
				if (cpu.getDuration() == 0) {
					remaining_jobs.remove(remaining_jobs.indexOf(cpu));
					break;
				}

				fb_ready.release();
				try {
					addjobs_ready.acquire();
				} catch (InterruptedException e1) {
				}
				try {
					acc_queue.acquire();
				} catch (InterruptedException e) {
				}
				// Check to see if there are other jobs waiting in the queue (if so, move this
				// to a lower queue)
				for (Queue<JobTuple> queue : fbq) {
					if (!queue.isEmpty()) {
						queue_has_jobs = true;
						break;
					}
				}
				if (queue_has_jobs) {
					if (num_queue_run + 1 < fbq.size()) {
						fbq.get(num_queue_run + 1).add(cpu);
					} else
						fbq.get(num_queue_run).add(cpu);
				}
				acc_queue.release();
			}
		}

		// Reset durations for each tuple back to original
		for (JobTuple job : jobs) {
			job.resetDuration();
		}

		job_listener.terminate();
		try {
			job_listener.join();
		} catch (InterruptedException e) {
		}

	}

}

