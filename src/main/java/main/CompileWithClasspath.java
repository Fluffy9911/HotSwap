package main;
import javax.tools.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class CompileWithClasspath {
    public static void main(String[] args) {
        // Specify the directory containing the source files
        String sourceDirectoryPath = "C:\\Users\\James.M\\eclipse-workspace\\jcat\\src\\main\\java\\";

        // Specify the classpath
        String classpath = System.getProperty("java.class.path");

        // Compile all Java files in the specified directory with the given classpath
        compileAllFilesWithClasspath(sourceDirectoryPath, classpath);
    }

    private static void compileAllFilesWithClasspath(String sourceDirectoryPath, String classpath) {
        // Get the JavaCompiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // Check if the compiler is available
        if (compiler == null) {
            System.out.println("JavaCompiler is not available. Ensure you are using a JDK, not just a JRE.");
            return;
        }

        // Create a file object for the source directory
        File sourceDirectory = new File(sourceDirectoryPath);

        // Check if the source directory exists
        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
            System.out.println("Specified source directory does not exist or is not a directory.");
            return;
        }

        // Get a file manager
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        // Get all Java files in the directory
        File[] sourceFiles = sourceDirectory.listFiles((dir, name) -> name.endsWith(".java"));

        // Prepare the compilation task
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFiles));
        compilationUnits.forEach((jfc)->{
        	System.out.println(jfc.getName());
        });
        // Specify the output directory for the compiled classes
        Iterable<String> options = List.of("-d", "./out/", "-classpath", classpath);

        // Perform the compilation
        boolean compilationSuccess = compiler.getTask(null, fileManager, null, options, null, compilationUnits).call();

        // Display compilation result
        if (compilationSuccess) {
            System.out.println("Compilation successful.");
        } else {
            System.out.println("Compilation failed.");
        }
    }
}
