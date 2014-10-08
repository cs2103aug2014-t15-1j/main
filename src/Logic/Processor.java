package Logic;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import Parser.Command;
import Parser.Parser;
import Storage.DataFile;
import Storage.Task;

public class Processor {
	
	private static DataFile file = new DataFile();
	private static Stack<Command> backwardHistory = new Stack<Command>();
	private static Stack<Command> forwardHistory = new Stack<Command>();
	private static Stack<Task> editedTask = new Stack<Task>();
	private static ArrayList<Task> searchList = new ArrayList<Task>();
	
	public Result processInput(String input) throws IOException {
		Command cmd = Parser.parse(input);
		return processCommand(cmd);
	}
	
	private Result processCommand(Command cmd) throws IOException {
		return processCommand(cmd, true);
	}
	
	private Result processCommand(Command cmd, boolean userInput) throws IOException {
		ArrayList<Task> tasks = new ArrayList<Task>();
		boolean success = false;
		
		if (cmd == null || cmd.getType() == CommandType.ERROR) {
			return new Result(null, false, CommandType.ERROR);
		}
		
		CommandType cmdType = cmd.getType();
		
		switch (cmdType) {
			case ADD:
				success = addTask(cmd, tasks, userInput);
				break;
			case EDIT:
				success = editTask(cmd, tasks, userInput);
				break;
			case DELETE:
				success = deleteTask(cmd, tasks, userInput);
				break;
			case RESTORE:
				success = restoreTask(cmd, tasks, userInput);
				break;
			case SEARCH:
				success = searchTasks(cmd, tasks, userInput);
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
				success = doneTasks(cmd, tasks, userInput);
				break;
			case TODO:
				success = toDoTasks(cmd, tasks, userInput);
				break;
			case UNDO:
				success = undoCommand(cmd, userInput);
				break;
			case REDO:
				success = redoCommand(cmd, userInput);
				break;
			case CLEAR:
				success = clearScreen(cmd);
				break;
			case JOKE:
				success = showJoke(cmd);
				break;
			case EXIT:
				success = true;
			default:
		}
		
		if (success && userInput) {
			updateCommandHistory(cmd);
		}
		
		return new Result(tasks, success, cmdType);
	}

	/* All the methods below returns true/false depending on the success
	 * Tasks to be display will be added to tasks.
	 */
	private boolean addTask(Command cmd, ArrayList<Task> tasks, boolean userInput) throws IOException {
		if (isBlocked(cmd)) {
			return false;
		}
		
		Task newTask = new Task(cmd.get("name"), cmd.get("more"), cmd.get("due"), cmd.get("start"), cmd.get("end"), cmd.get("priority"), cmd.getTags());
		tasks.add(newTask);
		
		return file.addTask(newTask);
	}
	
	//Check if the date is blocked and allowed to be added
	private boolean isBlocked(Command cmd) {
		return false;
	}
	
	private boolean editTask(Command cmd, ArrayList<Task> tasks, boolean userInput) throws IOException {
		Task existingTask = getTaskById(cmd);
		
		if (existingTask != null) {
			Task oldTask = new Task(existingTask);
			editedTask.push(oldTask);
			updateTaskParameters(cmd, existingTask);
			tasks.add(existingTask);
			return true;
		}
		return false;
	}

	//Copies parameters from cmd to existingTask
	private void updateTaskParameters(Command cmd, Task existingTask) {
	    if (cmd.get("name") != null) {
			existingTask.setName(cmd.get("name"));
		}
		if (cmd.get("more") != null) {
			existingTask.setMore(cmd.get("more"));
		}
		if (cmd.get("due") != null) {
			existingTask.setDue(cmd.get("due"));				
		}
		if (cmd.get("start") != null) {
			existingTask.setStart(cmd.get("start"));
		}
		if (cmd.get("end") != null) {
			existingTask.setEnd(cmd.get("end"));
		}
		if (cmd.get("priority") != null) {
			existingTask.setPriority(cmd.get("priority"));
		}
		if (cmd.getTags() != null) {
			existingTask.setTags(cmd.getTags());
		}
	}

	//Returns true if delete is executable.
	//ArrayList<Task> tasks = empty if deleting all the file.
	//else will contain at least 1 task.	
	private boolean deleteTask(Command cmd, ArrayList<Task> tasks, boolean userInput) {
		boolean success = false;
		switch (cmd.get("rangeType")) {
			case "id":
				success = deleteTaskUsingID(cmd, tasks);
				break;
			case "search":
				success = deleteSearchedTasks(cmd, tasks);
				break;
			case "all":
				return true;
			default:
				return false;
		}
		
		return success;
	}
	
	private boolean deleteTaskUsingID(Command cmd, ArrayList<Task> tasks) {
		Task t = file.getTask(Integer.parseInt(cmd.get("id")));
		if (t != null) {
			file.deleteTask(t.getId());
		} else {
			return false;
		}
		
		return true;
	}
	
	private boolean deleteSearchedTasks(Command cmd, ArrayList<Task> tasks) {
		if (!searchList.isEmpty()) {
			for (Task existingTask : searchList) {
				if (existingTask != null) {
					file.deleteTask(existingTask.getId());
				}
			}
		} else {
			return false; //Nothing in previous search
		}
		
		return true;
	}

	private boolean restoreTask(Command cmd, ArrayList<Task> tasks, boolean userInput) throws IOException {
		switch (cmd.get("rangeType")) {
			case "id":
				restoreUsingId(cmd, tasks);
				break;
			case "all":
				restoreAll(tasks);
				break;
			default:
				return false;
		}
		
		return true;
	}

	private void restoreUsingId(Command cmd, ArrayList<Task> tasks)	throws IOException {
		Task task = null;
		for (Task t: file.getDeletedTasks()) {
			if (t.getId() == Integer.parseInt(cmd.get("id"))) {
				task = t;
				break;
			}
		}
		
		if (task != null) {
			file.restore(task.getId());
		}
		
	}

	private void restoreAll(ArrayList<Task> tasks) throws IOException {
		for (Task t: file.getDeletedTasks()) {
			tasks.add(t);
			file.addTask(t);
		}
		
		file.getDeletedTasks().clear();
	}
	
	public boolean deleteAllData() {
		file.deleteAll();
		return false;
		//Clears all history
	}
	
	private boolean searchTasks(Command cmd, ArrayList<Task> tasks, boolean userInput) {
		List<Task> doneTasks = file.getDoneTasks();
		List<Task> toDoTasks = file.getToDoTasks();
		List<Task> deletedTask = file.getDeletedTasks();
		
		//ArrayList<String> keywords = cmd.get("rangeType");
		
		for (Task t: doneTasks) {		
			//If found:
			tasks.add(t);
			searchList.add(t);
		}
		for (Task t: toDoTasks) {		
			//If found:
			tasks.add(t);
			searchList.add(t);
		}
		for (Task t: deletedTask) {		
			//If found:
			tasks.add(t);
			searchList.add(t);
		}
		return false;
	}

	private boolean displayTask(Command cmd, ArrayList<Task> tasks) {
		return displayTask(cmd, tasks, true);
	}
	
	private boolean displayTask(Command cmd, ArrayList<Task> tasks, boolean userInput) {
		switch (cmd.get("rangeType")) {
			case "id":
				tasks.add(getTaskById(cmd));
				break;
			case "search":
				tasks = searchList;
				break;
			case "all":
				tasks.addAll(file.getToDoTasks());
				tasks.addAll(file.getDoneTasks());
				break;
		}
		return true;
	}

	private Task getTaskById(Command cmd) {
		return file.getTask(Integer.parseInt(cmd.get("id")));
	}

	private boolean blockDates(Command cmd) {
		return false;
	}

	private boolean unblockDates(Command cmd) {
		return false;
	}
	
	private boolean doneTasks(Command cmd, ArrayList<Task> tasks, boolean userInput) {
		Task existingTask = getTaskById(cmd);
		
		if (existingTask != null) {
			existingTask.setDone(true);
			tasks.add(existingTask);
			return true;
		}
		
		return false;
	}

	private boolean toDoTasks(Command cmd, ArrayList<Task> tasks, boolean userInput) {
		Task existingTask = getTaskById(cmd);
		
		if (existingTask != null) {
			existingTask.setDone(false);
			tasks.add(existingTask);
			return true;
		}
		
		return false;
	}
	
	//APPLICABLE FOR ADD, EDIT, DELETE, RESTORE, BLOCK, UNBLOCK, UNDONE, DONE
	private boolean undoCommand(Command cmd, boolean userInput) {
		if (!backwardHistory.isEmpty()) {
			Command backwardCommand = backwardHistory.pop();
			//Do the complement of backwardCommand
			try {
				switch(backwardCommand.getType()) {
					case ADD:
						undoAdd();
						break;
					case EDIT:
						undoEdit();
						break;
					case DELETE:
						restoreTask(backwardCommand, new ArrayList<Task>(), false);
						break;
					case RESTORE:
						deleteTask(backwardCommand, new ArrayList<Task>(), false);
						break;
					case BLOCK:
						break;
					case UNBLOCK:
						break;
					case TODO:
						doneTasks(backwardCommand, new ArrayList<Task>(), false);
						break;
					case DONE:
						toDoTasks(backwardCommand, new ArrayList<Task>(), false);
						break;
					default:
						return false;
				}
			} catch (Exception e) {
				backwardHistory.push(backwardCommand);
				return false;
			}
			forwardHistory.push(backwardCommand);
			return true;
		}
		return false;
	}

	private void undoAdd() {
		Task toDelete = file.getToDoTasks().get(file.getToDoTasks().size() - 1);
		if (toDelete != null) {
			file.deleteTask(toDelete.getId());
		}
	}

	private void undoEdit() {
		Task prevTask = editedTask.pop();
		
		for (Task existingTask: file.getToDoTasks()) {
			if (existingTask.getId() == prevTask.getId()) {
				copyTaskParameters(prevTask, existingTask);
				break;
			}
		}
	}

	//Copies parameters from prevTask to existingTask
	private void copyTaskParameters(Task prevTask, Task existingTask) {
		existingTask.setName(prevTask.getName());
		existingTask.setMore(prevTask.getMore());
		existingTask.setDue(prevTask.getDue());
		existingTask.setPriority(prevTask.getPriority());
		existingTask.setStart(prevTask.getStart());
		existingTask.setEnd(prevTask.getEnd());
		existingTask.setTags(prevTask.getTags());
	}
	
	//APPLICABLE FOR ADD, EDIT, DELETE, RESTORE, BLOCK, UNBLOCK, UNDONE, DONE
	private boolean redoCommand(Command cmd, boolean userInput) throws IOException {
		if (!forwardHistory.isEmpty()) {
			Command forwardCommand = forwardHistory.pop();
			Result result = processCommand(forwardCommand, false);
			backwardHistory.push(forwardCommand);
			return result.isSuccess();
		}
		return false;
	}

	private boolean clearScreen(Command cmd) {
		return true;
	}

	private boolean showJoke(Command cmd) {
		//Show joke
		return true;
	}
	
	private void updateCommandHistory(Command cmd) {
		switch (cmd.getType()) {
			case ADD:
			case DELETE:
			case EDIT:
			case RESTORE:
			case BLOCK:
			case UNBLOCK:
			case DONE:
			case TODO:
				forwardHistory.clear();
				backwardHistory.push(cmd);
				break;
			default:
				return;
		}
	}
}