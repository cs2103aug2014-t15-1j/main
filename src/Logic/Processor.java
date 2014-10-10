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
	
    private static Processor processor;
	private DataFile _file;
	private Stack<Command> _backwardHistory;
	private Stack<Command> _forwardHistory;
	private Stack<Task> _editedTask;
	private List<Task> _searchList;
	
	private Processor() {
	    _file = new DataFile();
	    _backwardHistory = new Stack<Command>();
	    _forwardHistory = new Stack<Command>();
	    _editedTask = new Stack<Task>();
	    _searchList = new ArrayList<Task>();
	}
	
	public static Processor getInstance() {
	    if (processor == null) {
	        processor = new Processor();
	    }
	    return processor;
	}
	
	public Result processInput(String input) throws IOException {
		Command cmd = Parser.parse(input);
		return processCommand(cmd);
	}
	
	private Result processCommand(Command cmd) throws IOException {
		return processCommand(cmd, true);
	}
	
	private Result processCommand(Command cmd, boolean userInput) throws IOException {
		List<Task> tasks = new ArrayList<Task>();
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
				_forwardHistory.clear();
				_backwardHistory.push(cmd);
				break;
			default:
				return;
		}
	}
	/* All the methods below returns true/false depending on the success
	 * Tasks to be display will be added to tasks.
	 */
	private boolean addTask(Command cmd, List<Task> tasks, boolean userInput) throws IOException {
		if (isBlocked(cmd)) {
			return false;
		}
		
		Task newTask = new Task(cmd.get("name"), cmd.get("more"), cmd.get("due"), cmd.get("start"), cmd.get("end"), cmd.get("priority"), cmd.getTags());
		tasks.add(newTask);
		
		return _file.addTask(newTask);
	}
	
	//Check if the date is blocked and allowed to be added
	private boolean isBlocked(Command cmd) {
		return false;
	}
	
	private boolean editTask(Command cmd, List<Task> tasks, boolean userInput) throws IOException {
		Task existingTask = getTaskById(cmd);
		
		if (existingTask != null) {
			Task oldTask = new Task(existingTask);
			_editedTask.push(oldTask);
			updateTaskParameters(cmd, existingTask);
			tasks.add(existingTask);
			_file.updateFile();
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
			existingTask.addTags(cmd.getTags());
		}
		if (cmd.get("delete") != null) {
			String parameterToRemove = cmd.get("delete");
			switch (parameterToRemove) {
				case "name":
					existingTask.resetName();
					break;
				case "more":
					existingTask.resetMore();
					break;
				case "due":
					existingTask.resetDue();
					break;
				case "start":
					existingTask.resetStart();
					break;
				case "end":
					existingTask.resetEnd();
					break;
				case "priority":
					existingTask.resetPriority();
					break;
				case "tags":
					existingTask.resetTags();
					break;
				default:
					return;
			}
		}
	}

	//Returns true if delete is executable.
	//List<Task> tasks = empty if deleting all the file.
	//else will contain at least 1 task.	
	private boolean deleteTask(Command cmd, List<Task> tasks, boolean userInput) {
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
	
	private boolean deleteTaskUsingID(Command cmd, List<Task> tasks) {
		Task t = getTaskById(cmd);
		if (t != null) {
			_file.deleteTask(t.getId());
			tasks.add(t);
		} else {
			return false;
		}
		
		return true;
	}
	
	private boolean deleteSearchedTasks(Command cmd, List<Task> tasks) {
		if (!_searchList.isEmpty()) {
			for (Task existingTask : _searchList) {
				if (existingTask != null) {
					_file.deleteTask(existingTask.getId());
					tasks.add(existingTask);
				}
			}
		} else {
			return false; //Nothing in previous search
		}
		
		return true;
	}

	private boolean restoreTask(Command cmd, List<Task> tasks, boolean userInput) throws IOException {
		switch (cmd.get("rangeType")) {
			case "id":
				return restoreUsingId(cmd, tasks);
			case "all":
				restoreAll(tasks);
				break;
			default:
				return false;
		}
		
		return true;
	}

	private boolean restoreUsingId(Command cmd, List<Task> tasks)	throws IOException {
		boolean success = _file.restore(Integer.parseInt(cmd.get("id")));
		if (success) {
			tasks.add(getTaskById(cmd));
		}
		return success;
	}

	private void restoreAll(List<Task> tasks) throws IOException {
		/*for (Task t: file.getDeletedTasks()) {
			tasks.add(t);
			//y
			file.restoreAll(t);
		}
		
		file.getDeletedTasks().clear();*/
	}
	
	public boolean deleteAllData() {
		_file.deleteAll();
		return false;
		//Clears all history
	}
	
	private boolean searchTasks(Command cmd, List<Task> tasks, boolean userInput) {
		List<Task> doneTasks = _file.getDoneTasks();
		List<Task> toDoTasks = _file.getToDoTasks();
		List<Task> deletedTask = _file.getDeletedTasks();
		
		//List<String> keywords = cmd.get("rangeType");
		String rangeType = cmd.get("rangeType");
		switch (rangeType) {
		case "dates":
			_file.getAllTasks();
			for (Task t: doneTasks) {
				cmd.get("start");
				cmd.get("end");
			}
			break;
		case "keys":
			break;
			default:
				return false;
		}
		
		for (Task t: doneTasks) {		
			//If found:
			tasks.add(t);
			_searchList.add(t);
		}
		for (Task t: toDoTasks) {		
			//If found:
			tasks.add(t);
			_searchList.add(t);
		}
		for (Task t: deletedTask) {		
			//If found:
			tasks.add(t);
			_searchList.add(t);
		}
		return false;
	}

	private boolean displayTask(Command cmd, List<Task> tasks) {
		return displayTask(cmd, tasks, true);
	}
	
	private boolean displayTask(Command cmd, List<Task> tasks, boolean userInput) {
		switch (cmd.get("rangeType")) {
			case "id":
				tasks.add(getTaskById(cmd));
				break;
			case "search":
				tasks = _searchList;
				break;
			case "all":
				tasks.addAll(_file.getToDoTasks());
				tasks.addAll(_file.getDoneTasks());
				break;
		}
		return true;
	}

	private Task getTaskById(Command cmd) {
		return _file.getTask(Integer.parseInt(cmd.get("id")));
	}

	private boolean blockDates(Command cmd) {
		return false;
	}

	private boolean unblockDates(Command cmd) {
		return false;
	}
	
	private boolean doneTasks(Command cmd, List<Task> tasks, boolean userInput) {
		Task existingTask = getTaskById(cmd);
		
		if (existingTask != null) {
			existingTask.setDone(true);
			tasks.add(existingTask);
			return true;
		}
		
		return false;
	}

	private boolean toDoTasks(Command cmd, List<Task> tasks, boolean userInput) {
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
		if (!_backwardHistory.isEmpty()) {
			Command backwardCommand = _backwardHistory.pop();
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
				_backwardHistory.push(backwardCommand);
				return false;
			}
			_forwardHistory.push(backwardCommand);
			return true;
		}
		return false;
	}

	private void undoAdd() {
		Task toDelete = _file.getToDoTasks().get(_file.getToDoTasks().size() - 1);
		if (toDelete != null) {
			_file.wipeTask(toDelete.getId());
		}
	}

	private void undoEdit() {
		Task prevTask = _editedTask.pop();
		
		for (Task existingTask: _file.getToDoTasks()) {
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
		existingTask.addTags(prevTask.getTags());
	}
	
	//APPLICABLE FOR ADD, EDIT, DELETE, RESTORE, BLOCK, UNBLOCK, UNDONE, DONE
	private boolean redoCommand(Command cmd, boolean userInput) throws IOException {
		if (!_forwardHistory.isEmpty()) {
			Command forwardCommand = _forwardHistory.pop();
			Result result = processCommand(forwardCommand, false);
			_backwardHistory.push(forwardCommand);
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
	
	public DataFile getFile() {
		return _file;
	}
}