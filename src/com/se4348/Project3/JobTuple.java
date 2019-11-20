package com.se4348.Project3;

public class JobTuple {
	private String job_id;
	private int start_time;
	private int duration;

	public JobTuple(String jid, int st, int dur) {
		job_id = jid;
		start_time = st;
		duration = dur;
	}

	public String getJobId() {
		return job_id;
	}

	public int getStartTime() {
		return start_time;
	}

	public int getDuration() {
		return duration;
	}
}
