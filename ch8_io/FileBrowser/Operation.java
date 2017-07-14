import java.util.*;
import java.io.*;

class Operation {
	OpType type;
	List<String> args = null;
	File currentDir = null; 
	
	public Operation(OpType type) {
		this.type = type;
	}
	
	public Operation(OpType type, List<String> args) {
		this.type = type;
		this.args = args;
	}
	
	public boolean execute() {
		boolean stayInCmd = true;
		switch(type) {
			case EXIT:
				stayInCmd = false;
				break;
			case SHOW_NESTED_FILES:
				showNested();
				break;
			case SHOW_CURRENT_PATH:
				System.out.println(currentDir.getAbsolutePath());
				break;
			case MAKE_DIRECTORY:
				createDirectory();
				break;
			case NAVIGATE_TO:
				break;
			case IDLE:
				break;
			default:
				System.out.println("Program works incorrectly");
				System.exit(13);
		}
		
		return stayInCmd;
	}
	
	
	private void showNested() {
		assert currentDir.isDirectory()
			: "Current file" + currentDir.getAbsolutePath() + " is not a directory.";
		
		for (File fileOrDir : currentDir.listFiles()) {
			boolean isDirectory = fileOrDir.isDirectory();
			String fileType = isDirectory ? "\tDIR\t" : "\t   \t";
			System.out.println(fileType + fileOrDir.getName());
		}
	}
	
	private void createDirectory() {
		String newDirPath = currentDir.getAbsolutePath() + "\\" + args.get(0);
		File newDir = new File(newDirPath);
		newDir.mkdir();
	}
}