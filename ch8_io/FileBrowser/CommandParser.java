import java.util.*;

public class CommandParser {
	public Operation parse(String input) {
		Operation op;
		
		if (input.isEmpty()) {
			op = new Operation(OpType.IDLE);
		}
		else {
			String[] opAndArgs = input.split(" +",0);
			
			List<String> args = new ArrayList<>();
			for (int i = 1; i < opAndArgs.length; i++) {
				args.add(opAndArgs[i]);
				// System.out.println("arg: \"" + opAndArgs[i] + "\"");
			}
			
			String operationName = opAndArgs[0];
			switch(operationName) {
				case "exit":
					op = new Operation(OpType.EXIT);
					break;
				case "ls":
					op = new Operation(OpType.SHOW_NESTED_FILES);
					break;
				case "pwd":
					op = new Operation(OpType.SHOW_CURRENT_PATH);
					break;
				case "mkdir":
					op = new Operation(OpType.MAKE_DIRECTORY, args);
					break;
				case "cd":
					op = new Operation(OpType.NAVIGATE_TO, args);
					break;
				default:
					System.out.println("'" + operationName + "' is not supported yet :(");
					op = new Operation(OpType.IDLE);
			}
		}
		
		return op;
	}
}