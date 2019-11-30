/** 
 * JobTuple.java
 * 
 * @author Ning Kevin Chen
 * NetID: nkc160130
 * Coded in: Java
 * Class: SE 4348
 * Professor: Greg Ozbirn
 */
package com.se4348.Project3;

public class JobTuple {
	private String job_id;
	private int orig_start_time;
	private int tmp_start_time;
	private int remaining_duration;
	private int orig_duration;

	public JobTuple(String jid, int st, int dur) {
		job_id = jid;
		tmp_start_time = st;
		orig_start_time = st;
		remaining_duration = dur;
		orig_duration = dur;
	}

	public String getJobId() {
		return job_id;
	}

	public int getStartTime() {
		return tmp_start_time;
	}

	public int getDuration() {
		return remaining_duration;
	}

	public void setStartTime(int st) {
		tmp_start_time = st;
	}

	public void setDuration(int dur) {
		if (remaining_duration > 0) {
			remaining_duration = dur;
		} else {
			remaining_duration = 0;
		}
	}

	public void resetDuration() {
		remaining_duration = orig_duration;
	}

	public void resetStartTime() {
		tmp_start_time = orig_start_time;
	}

	public boolean equals(JobTuple j) {
		return this.job_id == j.job_id && this.orig_start_time == j.orig_start_time
				&& this.orig_duration == j.orig_duration;
	}
}
