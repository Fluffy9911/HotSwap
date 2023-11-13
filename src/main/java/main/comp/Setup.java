package main.comp;

import java.io.File;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;

import main.comp.reflections.Project;
import main.comp.reflections.ProjectBuilder;

public class Setup {
public static boolean at_runtime = false;

private static JavaCompiler jc;

static Logger log;
public static void init() {
	log = LogManager.getLogger(Setup.class);
	log.debug("Starting Setup");
	
}

public static JavaCompiler  setup() {
	jc = ToolProvider.getSystemJavaCompiler();
	if(jc == null) {
		log.debug("Unable to gather compiler, Reloading not available");
	}else {
		at_runtime = true;
		log.debug("Found compiler, runtime compiling available - {}.",at_runtime);
		return jc;
	}
	return null;
	
}

public static Logger logger() {
	// TODO Auto-generated method stub
	return log;
}

public static void setupProject(ProgramInfo info,File dir) {
	Setup.init();
	
	Project p = ProjectBuilder.createProject(info, Setup.setup(),dir);
	p.checkReloads();
}

}
