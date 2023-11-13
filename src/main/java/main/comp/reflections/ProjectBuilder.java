package main.comp.reflections;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import org.apache.logging.log4j.Logger;

import main.comp.ProgramInfo;
import main.comp.Setup;

public class ProjectBuilder {
static Logger l = Setup.logger();
	
	public static  void reload(ProgramInfo info,JavaCompiler jc,File od) {
		l.debug("Starting Reload Search...");
		long ms = System.currentTimeMillis();
		String path = info.path();
		
		File directory = new File(path);
ArrayList<File> fileList = new ArrayList<>();
        // Check if the directory exists
        if (directory.exists() && directory.isDirectory()) {
            // Create a list to store all files
         

            // Recursively traverse the directory and its subdirectories
            getAllFiles(directory, fileList);
long n = System.currentTimeMillis() - ms;
           l.debug("Found: {} File in Dir: {}. it took: {}MS",fileList.size(),path,n);
            for (File file : fileList) {
            	 l.debug(file.getPath());
            }
        } else {
        	 l.debug("The specified directory does not exist or is not a directory.");
        }
        compileAndReload(info, jc, od, directory, fileList);
       
       
       
	}



	public static void compileAndReload(ProgramInfo info, JavaCompiler jc, File od, File directory,
			ArrayList<File> fileList) {
		l.debug("Starting Compile... {} ",directory.getAbsolutePath()+"\\");
        String classpath = System.getProperty("java.class.path");
        l.debug("Classpath: {} " , classpath);
        StandardJavaFileManager fileManager = jc.getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(fileList);
        compilationUnits.forEach((jfc)->{
        	System.out.println(jfc.getName());
        });
        // Specify the output directory for the compiled classes
        Iterable<String> options = List.of("-d", od.getPath(), "-classpath", classpath);

        // Perform the compilation
        boolean compilationSuccess = jc.getTask(null, fileManager, null, options, null, compilationUnits).call();

        l.debug("Compilation successful? {}", compilationSuccess);
        
       
       
       try {
    	   l.debug("Finding Class: {}. and executing method: {}.",info.mainClassName(),info.mainMethodName());
    	 
		Class<?> main = loadClassFromDirectory(info.mainClassName(), od);
		Method m = main.getMethod(info.mainMethodName());
		m.invoke(null);
	} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException e) {
		l.debug("Error Executing Class: {}. and executing method: {}",info.mainClassName(),info.mainMethodName());
		e.printStackTrace();
	}
	}
	
	
	    
	    public static void getAllFiles(File directory, List<File> fileList) {
	        // List all files in the current directory
	        File[] files = directory.listFiles();
	        if (files != null) {
	            for (File file : files) {
	                if (file.isFile() && file.getName().endsWith(".java")) {
	                    fileList.add(file);
	                } else if (file.isDirectory()) {
	                    // Recursively call the method for subdirectories
	                    getAllFiles(file, fileList);
	                }
	            }
	        }
	    }
	    public static void getAllFiles(ProgramInfo info, List<File> fileList) {
	        File directory = new File(info.path());
	        File[] files = directory.listFiles();
	        if (files != null) {
	            for (File file : files) {
	                if (file.isFile() && file.getName().endsWith(".java") && !fileList.contains(file)) {
	                    fileList.add(file);
	                } else if (file.isDirectory()) {
	                    // Recursively call the method for subdirectories
	                    getAllFiles(file, fileList);
	                }
	            }
	        }
	    }
	

	    private static Class<?> loadClassFromDirectory(String className, File directory) throws ClassNotFoundException, IOException {
	        URLClassLoader classLoader = new URLClassLoader(new URL[]{directory.toURI().toURL()});
	        return Class.forName(className, true, classLoader);
	    }
	    public static  Project createProject(ProgramInfo info,JavaCompiler jc,File od) {
			l.debug("Starting Reload Search...");
			long ms = System.currentTimeMillis();
			String path = info.path();
			
			File directory = new File(path);
	ArrayList<File> fileList = new ArrayList<>();
	        // Check if the directory exists
	        if (directory.exists() && directory.isDirectory()) {
	            // Create a list to store all files
	         

	            // Recursively traverse the directory and its subdirectories
	            getAllFiles(directory, fileList);
	long n = System.currentTimeMillis() - ms;
	           l.debug("Found: {} File in Dir: {}. it took: {}MS",fileList.size(),path,n);
	            for (File file : fileList) {
	            	 l.debug(file.getPath());
	            }
	        } else {
	        	 l.debug("The specified directory does not exist or is not a directory.");
	        }
	        return new Project(fileList, info, od);
	       
	       
		}

	
}
