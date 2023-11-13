package main.comp.reflections;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.Logger;

import main.comp.ProgramInfo;
import main.comp.Setup;

public class Project {
List<File> files;
ProgramInfo info;
long start = 0;
File od;
Logger l = Setup.logger();

public Project(List<File> files, ProgramInfo info,File odd) {
	super();
	this.files = files;
	this.info = info;
	od = odd;
	start = System.currentTimeMillis();
}

public void reload() {
	if(needReload()) {
		ProjectBuilder.compileAndReload(info, Setup.setup(), od, new File(info.path()), (ArrayList<File>) files);
		start = System.currentTimeMillis();
	}
}

public boolean needReload() {
	
	for (Iterator iterator = files.iterator(); iterator.hasNext();) {
		File file = (File) iterator.next();
		try {
			if(isInThreshold(start, Files.getLastModifiedTime(file.toPath()).toMillis())) {
				l.debug("Files need reloading!");
				return true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	l.debug("No File need reloading, skipping...");
	return false;
}

public void collectFiles() {
	ProjectBuilder.getAllFiles(info, files);
}

public long getCheckDelay() {
	return 1000;
}

public boolean isInThreshold(long start,long input) {
	return input > start;
}

public void checkReloads() {
	Timer t = new Timer();
	t.schedule(new TimerTask() {

		@Override
		public void run() {
			collectFiles();
			reload();
			
		}
		
	}, 0, getCheckDelay());
}

}
