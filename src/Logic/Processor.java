package Logic;

import java.util.ArrayList;
import java.util.Map;

import Parser.Command;
import Parser.Parser;
import Storage.DataFile;

public class Processor {
	
	public Result processInput(String input) {
		Command cmd = Parser.parse(input);
		return processCommand(cmd);
	}
	
	private Result processCommand(Command cmd) {
		ArrayList<Task> tasks = null;
		boolean success = false;
		CommandType cmdType = cmd.getType();
		
		switch (cmdType) {
			case ADD:
				success = addTask(cmd, tasks);
				break;
			case EDIT:
				success = editTask(cmd, tasks);
				break;
			case DELETE:
				success = deleteTask(cmd, tasks);
				break;
			case RESTORE:
				success = restoreTask(cmd, tasks);
				break;
			case SEARCH:
				success = searchTasks(cmd, tasks);
				break;
			case DISPLAY:
				success = displayTask(cmd, tasks);
				break;
			case BLOCK:
				success = blockDates(cmd);
				break;
			case UNBLOCK:
				success = unblockDates(cmd);
				break;
			case DONE:
				success = doneTasks(cmd);
				break;
			case UNDONE:
				success = undoneTasks(cmd);
				break;
			case UNDO:
				success = undoCommand(cmd);
				break;
			case REDO:
				success = redoCommand(cmd);
				break;
			case CLEAR:
				success = clearScreen(cmd);
				break;
			case JOKE:
				success = showJoke(cmd);
				break;
			case EXIT:
				System.exit(0);
			default:
		}
		return new Result(tasks, success, cmdType);
	}

	/* All the methods below returns true/false depending on the success
	 * The 
	 */
	private boolean addTask(Command cmd, ArrayList<Task> tasks) {
		if (isBlocked(cmd)) {
			return false;
		}
		Task newTask = new Task(cmd.get("name"), cmd.get("more"), cmd.get("due"), cmd.get("start"), cmd.get("end"), cmd.get("piority"), cmd.get("tags"));
		tasks.add(newTask);
		return DataFile.write(newTask);
	}
	
	//Check if the date is blocked and allowed to be added
	private boolean isBlocked(Command cmd) {
		return false;
	}

	private boolean editTask(Command cmd, ArrayList<Task> tasks) {
		// TODO Auto-generated method stub
		DataFile.read(cmd.get(""));
		return false;
	}

	private boolean deleteTask(Command cmd, ArrayList<Task> tasks) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean restoreTask(Command cmd, ArrayList<Task> tasks) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean searchTasks(Command cmd, ArrayList<Task> tasks) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean displayTask(Command cmd, ArrayList<Task> tasks) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean blockDates(Command cmd) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean unblockDates(Command cmd) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean doneTasks(Command cmd) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean undoneTasks(Command cmd) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean undoCommand(Command cmd) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean redoCommand(Command cmd) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean clearScreen(Command cmd) {
		return true;
	}

	private boolean showJoke(Command cmd) {
		//Show joke
		return true;
	}

	
	
	
}
