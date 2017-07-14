import java.io.*;
import java.nio.charset.*;

public class FileBrowser {
	private Console console = System.console();
	private File currentDir = null;
	private CommandParser parser = null;
	
	public void run() {
		init();		
		while(readCommand());
	}
	
	private void init () {
		// System.out.println("Working Directory = " + System.getProperty("user.dir"));			  
		parser = new CommandParser();
		currentDir = new File(System.getProperty("user.dir"));
	}
	
	
	boolean readCommand() {
		String input = readInput();
		
		Operation op =  parser.parse(input);
		op.currentDir = currentDir;
		
		boolean stayInCmd = op.execute();
		
		return stayInCmd;
	}

	String readInput() {
		return console.readLine("UserName:" + currentDir.getAbsolutePath() + " $")
					.trim();
	}	
	
}