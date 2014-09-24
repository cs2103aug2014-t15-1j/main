package Logic;

import java.util.Map;
import Parser.Command;
import Parser.Parser;

public class CommandProcessor {
	
	private boolean processInput(String input) {
		Command command = Parser.parse(input);
		Task task = new Task(command);
		processTask(task);
		return true;
	}
	
	private void processTask(Task t) {
		switch (t.get) {
			case ADD:
				break;
			case EDIT:
				break;
			//TO ADD OTHER COMMANDS:
			default:
		}
	}
}
