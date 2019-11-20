package com.se4348.Project3;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class App extends Application {

	public static String scheduling_alg;
	public static SchedulingAlg sch_alg;
	public static String sch_alg_type;
	public static ArrayList<JobTuple> jobs = new ArrayList<>();

	@Override
	public void start(Stage stage) {
		// Initializing web rendering
		final WebView wv = new WebView();
		final WebEngine we = wv.getEngine();
		String render = App.class.getResource("front-end/render.html").toExternalForm();
		we.setJavaScriptEnabled(true);

		// Execute JavaScript code when WebEngine loaded
		we.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			public void changed(ObservableValue ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					if (sch_alg_type == "ALL") {
						we.executeScript("onTabClick()");
					} else {
						we.executeScript("renderGraph(" + convertJS2DArray(sch_alg.run_matrix) + ","
								+ convertJSArray(jobs) + "," + "\"" + sch_alg_type + "\", false)");
					}
				}
			}
		});
		we.load(render);

		StackPane sp = new StackPane();
		sp.getChildren().add(wv);

		// Setting up the scene
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Scene scene = new Scene(sp, screenSize.getWidth() / 2, screenSize.getHeight() / 2);

		// Setting the title
		stage.setTitle("Project 3 - OS Scheduling Algorithms");

		// Setting icon image based off of if it's running off a jar
		if (App.class.getResource("App.class").toString().contains("jar:")) {
			stage.getIcons().add(new Image(App.class.getResourceAsStream("icons/schedule.png")));
		} else {
			stage.getIcons().add(new Image("file:src/com/se4348/Project3/icons/schedule.png"));
		}

		// Showing the scene
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		// Invalid arguments
		if (args.length != 1) {
			System.err.println(
					"ERROR: Invalid arguments supplied! Please run this program with any of the following arguments:\n"
							+ "FCFS, RR, SPN, SRT, HRRN, FB, ALL");
			System.exit(1);
		}

		// Set scheduling algorithm
		scheduling_alg = args[0];
		sch_alg_type = args[0];
		// Get input file (jobs.txt)
		// (needs to be supplied in same folder as jar if running with jar)
		File jobs_txt = null;
		if (App.class.getResource("App.class").toString().contains("jar:")) {
			jobs_txt = new File("jobs.txt");
		} else {
			jobs_txt = new File("src/com/se4348/Project3/jobs.txt");
		}

		// Read in file
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(jobs_txt));
		} catch (FileNotFoundException e1) {
			System.exit(e1.hashCode());
		}

		String tmp;
		try {
			while ((tmp = br.readLine()) != null) {
				String[] line = tmp.split("\t");
				jobs.add(new JobTuple(line[0], Integer.parseInt(line[1]), Integer.parseInt(line[2])));
			}
		} catch (IOException e) {
		}

		// Run the scheduling algorithms
		switch (scheduling_alg) {
		case "FCFS":
			sch_alg = new FCFS(jobs);
			sch_alg.run();
			break;
		case "RR":
			sch_alg = new RR(jobs);
			break;
		}

		// Start the JavaFX
		launch();
	}

	// Converting the Java 2-D array into a JavaScript 2-D array
	public static String convertJS2DArray(ArrayList<boolean[]> run_m) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		int i = 0;
		for (boolean[] nodes : run_m) {
			sb.append("[");
			int j = 0;
			for (boolean node : nodes) {
				if (node)
					sb.append("true");
				else
					sb.append("false");
				if (j != nodes.length - 1)
					sb.append(",");
				j++;
			}
			sb.append("]");
			if (i != run_m.size() - 1)
				sb.append(",");
			i++;
		}
		sb.append("]");

		return sb.toString();
	}

	// Converting the Java array into a JavaScript array
	public static String convertJSArray(ArrayList<JobTuple> jobs) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		int i = 0;
		for (JobTuple job : jobs) {
			sb.append("\"" + job.getJobId() + "\"");
			if (i != jobs.size() - 1)
				sb.append(",");
			i++;
		}
		sb.append("]");

		return sb.toString();
	}

}