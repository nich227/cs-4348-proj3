package com.se4348.Project3;

import java.util.ArrayList;

public abstract class SchedulingAlg {
	protected ArrayList<JobTuple> jobs;
	protected ArrayList<boolean[]> run_matrix;

	public SchedulingAlg(ArrayList<JobTuple> j) {
		jobs = j;
		run_matrix = new ArrayList<>();
	}
	
	public abstract void run();
}
