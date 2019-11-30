/** 
 * SchedulingAlg.java
 * 
 * @author Ning Kevin Chen
 * NetID: nkc160130
 * Coded in: Java
 * Class: SE 4348
 * Professor: Greg Ozbirn
 */
package com.se4348.Project3;

import java.util.ArrayList;

public abstract class SchedulingAlg {
	protected ArrayList<JobTuple> jobs;
	protected ArrayList<boolean[]> run_matrix;

	public SchedulingAlg(ArrayList<JobTuple> j) {
		jobs = new ArrayList<>();
		for (JobTuple job : j) {
			jobs.add(job);
		}
		run_matrix = new ArrayList<>();
	}

	public abstract void run();
}
