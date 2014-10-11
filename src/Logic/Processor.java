package Logic;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Stack;

import Parser.Command;
import Parser.Parser;
import Storage.DataFile;
import Storage.Task;

public class Processor extends Observable {
	
    private static Processor processor;
	private DataFile _file;
	private Stack<Command> _backwardHistory;
	private Stack<Command> _forwardHistory;
	private Stack<Task> _editedTask;
	private List<Task> _searchList;
	private Stack<Integer> _searchListSizeHistory;
	
	/* Constructor */
	private Processor() {
	    _file = new DataFile();
	    _backwardHistory = new Stack<Command>();
	    _forwardHistory = new Stack<Command>();
	    _editedTask = new Stack<Task>();
	    _searchList = new ArrayList<Task>();
	    _searchListSizeHistory = new Stack<Integer>();
	}
	
	/* 
     * @return Instance of Processor
     */
	public static Processor getInstance() {
	    if (processor == null) {
	        processor = new Processor();
	    }
	    return processor;
	}
	
	/* 
	 * @param String input
     * @return Result
     */
	public Result processInput(String input) throws Exception {
		Command cmd = Parser.parse(input);
		return processCommand(cmd);
	}
	
	/* 
     * @param Command cmd
     */
	private Result processCommand(Command cmd) throws Exception {
		return processCommand(cmd, true);
	}
	
	/* Executes the appropriate actions for each command
     * @param Command cmd
     * @param boolean userInput
     * @return Result
     *     boolean success
     *     List<Task> tasks - Contains Tasks that are affected in the operation
     *     CommandType cmdExecuted
     */
	private Result processCommand(Command cmd, boolean userInput) throws Exception {
		List<Task> tasks = new ArrayList<Task>();
		boolean success = false;
		
		if (cmd == null || cmd.getType() == CommandType.ERROR) {
			return new Result(null, false, CommandType.ERROR);
		}
		
		CommandType cmdType = cmd.getType();
		
		switch (cmdType) {
		    case HELP:
		        success = displayHelp(cmd);
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
				success = displayTask(cmd, tasks, userInput);
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
			case EXIT:
				success = true;
			default:
		}
		
		if (success && userInput) {
			updateCommandHistory(cmd);
			setChanged();
			notifyObservers(); //Calls update of the side panel class
		}
		
		return new Result(tasks, success, cmdType);
	}

	/* Adds command to the history list
     * @param Command cmd
     */
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
	
	/* Returns back to UI to display a 'HELP' picture?
	 * @return true
	 */
	private boolean displayHelp(Command cmd) {
	    return true;
	}
	
	/* Executes 'add' operation of a task 
	 * Add a Task to 'todo' List
	 * @return true/false on whether operation is performed 
	 */
	private boolean addTask(Command cmd, List<Task> tasks, boolean userInput) throws Exception {
		if (isBlocked(cmd)) {
			return false;
		}
		Task newTask = new Task(cmd.get("name"), cmd.get("more"), cmd.get("due"), cmd.get("start"), cmd.get("end"), cmd.getTags());
		tasks.add(newTask);
		return _file.addTask(newTask);
	}
	
	/* Check if the date is blocked
	 * @param Command cmd
	 * @return false/true depending on the validity of blocked dates
	 */
	private boolean isBlocked(Command cmd) {
		return false;
	}
	
	/* Executes "edit" operation
	 * Allow edit/deletion of parameters of a Task
     * @return true/false on whether operation is performed
     */
	private boolean editTask(Command cmd, List<Task> tasks, boolean userInput) throws Exception {
		Task existingTask = getTaskById(cmd);
		
		if (existingTask != null) {
			Task oldTask = new Task(existingTask);
			updateTaskParameters(cmd, existingTask);
			
			tasks.add(existingTask);
			_editedTask.push(oldTask);
			_file.updateFile();
			return true;
		}
		return false;
	}

	/* Copies parameters from cmd to existingTask */
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
		if (cmd.getTags() != null) {
			existingTask.addTags(cmd.getTags());
		}
		if (cmd.get("delete") != null) {
			deleteParameter(existingTask, cmd.get("delete"));
		}
	}

	/* Removes a parameter in the Task */
	private void deleteParameter(Task existingTask, String parameterToRemove) {
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
            case "tags":
                existingTask.resetTags();
                break;
            default:
                return;
        }
	}
	
	/* Executes "delete" operation
	 * Deletes a task
	 * Allows delete <id>, delete search, delete all
     * @return true/false on whether operation is performed
     */
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
	
	/* Deletes Task using Id */
	private boolean deleteTaskUsingID(Command cmd, List<Task> tasks) {
		Task t = getTaskById(cmd);
		if (t == null) {
		    return false;
		} else {
            tasks.add(t);
			return _file.deleteTask(t.getId());
		}
	}
	
	/* Deletes all Tasks in _searchList */
	private boolean deleteSearchedTasks(Command cmd, List<Task> tasks) {
		if (_searchList.isEmpty()) {
		    return false;
		} else {
			for (Task existingTask : _searchList) {
				if (existingTask != null) {
				    tasks.add(existingTask);
					_file.deleteTask(existingTask.getId());
				}
			}
		}
		return true;
	}

	/* Executes "Restore" operation
	 * Restores a deleted Task
	 * Allows restore <id>, restore search
     * @return true/false on whether operation is performed
     */
	private boolean restoreTask(Command cmd, List<Task> tasks, boolean userInput) throws Exception {
		switch (cmd.get("rangeType")) {
			case "id":
				return restoreUsingId(cmd, tasks);
			case "search":
			    return restoreUsingSearch(cmd, tasks);
			default:
				return false;
		}
	}

	/* Restores a deleted Task using Id */
	private boolean restoreUsingId(Command cmd, List<Task> tasks) throws Exception {
		boolean success = _file.restore(Integer.parseInt(cmd.get("id")));
		if (success) {
			tasks.add(getTaskById(cmd));
		}
		return success;
	}
	
	/* Restores all deleted Tasks due to 'delete search' */
	private boolean restoreUsingSearch(Command cmd, List<Task> tasks) throws Exception {
	    int deletedTasksSize = _file.getDeletedTasks().size();
	    int restoreAmt = _searchListSizeHistory.pop();
	    for (int i = 0; i < restoreAmt; i++) {
	        int index = deletedTasksSize - restoreAmt;
	        _file.restore(_file.getDeletedTasks().get(index).getId());
	    }
	    return true;
	}

	//Use with *CAUTION* - Wipes entire DataFile
	public boolean deleteAllData() {
		_file.wipeFile();
		return true;
	}

	/* Executes "search" operation
	 * Allows search <date>, search <key1> <key2> #tag1 #tag2
     * @return true/false on whether operation is performed
     */
	private boolean searchTasks(Command cmd, List<Task> tasks, boolean userInput) {
	    clearPreviousSearch();
		String date = cmd.get("date");
		List<String> keywords = cmd.getKeywords();
		List<String> tags = cmd.getTags();
		
		if (date != null) {
		    searchUsingDate(date, tasks);
		} else if (keywords != null || tags != null) {
		    searchUsingKeyOrTags(keywords, tags, tasks);
		}
		
		updateSearchList(tasks);
		return true;
	}
	
	private void clearPreviousSearch() {
	    _searchList.clear();
	}
	
	/* Performs search using date */
	private void searchUsingDate(String date, List<Task> tasks) {
	    List<Task> toDoTasks = _file.getToDoTasks();
        for (Task t: toDoTasks) {
            if (t.getDue().equals(date)) {
                tasks.add(t);
            }
        }
	}
	
	/* Performs search using Keywords or Tags 
	 * Tries to find if tags is present first before searching for keywords
	 * */
	private void searchUsingKeyOrTags(List<String> keywords, List<String> tags, List<Task> tasks) {
	    List<Task> toDoTasks = _file.getToDoTasks();
        for (Task task: toDoTasks) {       
            boolean found = isTagged(task, tags, tasks);
            if (!found) {
                found = containsKeyword(task, keywords, tasks);
            }
        }
	}
	
	/* Checks if a Task is tagged under a tag in a List of tags*/
	private boolean isTagged(Task task, List<String> tags, List<Task> tasks) {
	    for (String tag: tags) {
            if (task.getTags().contains(tag)) {
                tasks.add(task);
                return true;
            }
        }
	    return false;
	}
	
	/* Checks if a Task contains a certain keyword in the List of keywords */
	private boolean containsKeyword(Task task, List<String> keywords, List<Task> tasks) {
        for (String key: keywords) {
            if (task.getName().toLowerCase().contains(key.toLowerCase())) {
                tasks.add(task);
                _searchList.add(task);
                return true;
            }
        }
        return false;
	}
	
	/* Updates the Search result */
	private void updateSearchList(List<Task> tasks) {
	    for (Task task : tasks) {
	        _searchList.add(task);
	    }
        _searchListSizeHistory.push(_searchList.size());
	}
	
	/* Executes "display" operation
	 * Allows display, display <id>, display search
	 * Allows show, show <id>, show search
     * @return true
     */
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

	/* Gets a Task by its Id */
	private Task getTaskById(Command cmd) {
		return _file.getTask(Integer.parseInt(cmd.get("id")));
	}

	/* Executes "block" operation
     * @return true/false on whether operation is performed
     */
	private boolean blockDates(Command cmd) {
		return false;
	}

	/* Executes "unblock" operation
     * @return true/false on whether operation is performed
     */
	private boolean unblockDates(Command cmd) {
		return false;
	}
	
	/* Executes "done" operation
	 * Marks a task as 'done'
     * @return true/false on whether operation is performed
     */
	private boolean doneTasks(Command cmd, List<Task> tasks, boolean userInput) {
		Task existingTask = getTaskById(cmd);
		if (existingTask != null) {
			existingTask.setDone(true);
			tasks.add(existingTask);
			return true;
		}
		return false;
	}

	/* Executes "todo" operation
	 * Marks a 'done' task as 'todo'
     * @return true/false on whether operation is performed
     */
	private boolean toDoTasks(Command cmd, List<Task> tasks, boolean userInput) {
		Task existingTask = getTaskById(cmd);
		if (existingTask != null) {
			existingTask.setDone(false);
			tasks.add(existingTask);
			return true;
		}
		return false;
	}
	
	/* Executes "undo" operation
	 * Applicable for 'Add', 'Edit', 'Delete', 'Restore', 'Block', 'Unblock', 'Done', 'Todo'
     * @return true/false on whether operation is performed
     */
	private boolean undoCommand(Command cmd, boolean userInput) {
		if (!_backwardHistory.isEmpty()) {
			Command backwardCommand = _backwardHistory.pop();
			try {
				switch(backwardCommand.getType()) {
					case ADD:
						undoAdd(cmd);
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
					    //unblock
						break;
					case UNBLOCK:
					    //block
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

	/* Undo the add command */
	private void undoAdd(Command cmd) {
		Task toDelete = _file.getToDoTasks().get(_file.getToDoTasks().size() - 1);
		if (toDelete != null) {
			_file.wipeTask(toDelete.getId());
		}
	}

	/* Reverts the previous edit of a Task */
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
		existingTask.setStart(prevTask.getStart());
		existingTask.setEnd(prevTask.getEnd());
		existingTask.addTags(prevTask.getTags());
	}
	
	/* Executes "redo" operation
     * Applicable for 'Add', 'Edit', 'Delete', 'Restore', 'Block', 'Unblock', 'Done', 'Todo'
     * @return true/false on whether operation is performed
     */
	private boolean redoCommand(Command cmd, boolean userInput) throws Exception {
		if (!_forwardHistory.isEmpty()) {
			Command forwardCommand = _forwardHistory.pop();
			Result result = processCommand(forwardCommand, false);
			_backwardHistory.push(forwardCommand);
			return result.isSuccess();
		}
		return false;
	}

	/* Executes "clear" operation
	 * Clears the screen
     * @return true/false on whether operation is performed
     */
	private boolean clearScreen(Command cmd) {
		return true;
	}
	
	public DataFile getFile() {
	    return _file;
	}
}