import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class FileBrowser {
	private static final Console console = System.console();
	private File currentDir = null;
	private CommandParser parser = null;
	
	public void run() {
		greetings();
		init();
		
		boolean cont = true;
		while(cont) {
			cont = readCommand();
		}
	}
	
	private static void greetings() {
		StringJoiner joiner = new StringJoiner("\n");
		joiner.add("******************************************");
		joiner.add("**                                      **");
		joiner.add("**        Welcome to John's shell       **");
		joiner.add("**                                      **");
		joiner.add("******************************************");
		System.out.println(joiner.toString());
	}
	
	private void init () {
		parser = new CommandParser();
		currentDir = new File(System.getProperty("user.dir"));
	}
	
	
	boolean readCommand() {
		String input = readInput();
		
		Operation op =  parser.parse(input);
		op.currentDir = currentDir;
		
		boolean stayInCmd = op.execute();
		currentDir = op.currentDir;
		
		return stayInCmd;
	}

	String readInput() {
		return console.readLine("UserName:" + currentDir.getAbsolutePath() + "$ ")
					.trim();
	}	
	
}