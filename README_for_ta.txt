Instructions on building this project:
1.	Install Apache Ant, if you haven't already:
		https://ant.apache.org
2.	In the command line, at the root of the Project folder 
  	(with the build.xml file), type:
		ant clean build
3. 	Then, type:
		ant Proj3

NOTE 1:	To change the algorithm that you want to use, open the build.xml 
      	and change this line:

<target name="Proj3">
        <java classname="com.se4348.Project3.App" failonerror="true" fork="yes">
            <arg line="all"/> <-- change this line to fcfs, rr, fb, etc.
            <classpath refid="Project 3.classpath"/>
	</java>
</target>


NOTE 2:	If you want to change the jobs.txt, please do so with the one located in
	src, not in bin.

NOTE 3: I have also included a proj3.jar, for your convenience. To run, type:
		java -jar proj3.jar <algorithm>
	The jobs.txt must be in the same folder as this jar file.

NOTE 4: If you're running on Linux, use the official Oracle Java implementation, 
	and not open-source alternatives. 