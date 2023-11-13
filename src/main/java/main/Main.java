package main;

import java.io.File;
import java.util.Scanner;

import org.slf4j.Logger;

import main.comp.ProgramInfo;
import main.comp.Setup;
import main.comp.reflections.Project;
import main.comp.reflections.ProjectBuilder;

public class Main {

	public static void main(String[] args) {
		ProgramInfo pr = new ProgramInfo() {
			
			@Override
			public String path() {
				// TODO Auto-generated method stub
				return "C:\\Users\\James.M\\eclipse-workspace\\jcat\\src\\main\\java";
			}
			
			@Override
			public String mainMethodName() {
				// TODO Auto-generated method stub
				return "main";
			}
			
			@Override
			public String mainClassName() {
				// TODO Auto-generated method stub
				return "jcat.com.fluffy.parsing.TestP";
			}
			
			@Override
			public boolean allowRuntime() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		Logger l = (Logger) Setup.logger();
		Setup.init();
		
		Project p = ProjectBuilder.createProject(pr, Setup.setup(), new File("./hotswapping/compiled/"));
		p.checkReloads();
//		Scanner s = new Scanner(System.in);
//		while(true) {
//			
//			if(s.hasNext()) {
//				String i = s.nextLine();
//				
//				if(i.equals("reload")) {
//					ProjectBuilder.reload(pr,Setup.setup(),new File("./hotswapping/compiled/"));
//				}
//				if(i.equals("exit")) {
//					l.debug("Exiting...");
//					System.exit(0);
//				}
//			}
//			
//		}
//		
	}

}
