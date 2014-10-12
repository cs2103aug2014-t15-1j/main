package Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Stack;

import Parser.Command;
import Parser.Parser;
import Storage.DataFile;
import Storage.Task;

/* This class handles inputs from UI and interacts with other components for the
 * necessary operations. It is dependent on DataFile for operations related to 
 * the management of storage of Task. It is also dependent on Parser to decipher
 * user input.
 * 
 * Singleton pattern is applied and only one instance of Processor is available.
 * Result object is being returned to UI for display purposes.
 * 
 * @author Ter Yao Xiang
 */

public class Processor extends Observable {
	
    private static Processor processor;
	private DataFile file;
	private Stack<Command> backwardHistory;
	private Stack<Command> forwardHistory;
	private Stack<List<Task>> backwardSearchListHistory;
	private Stack<List<Task>> forwardSearchListHistory;
	private Stack<Task> editedTaskHistory;
	private List<Task> lastSearch;
	
	/* Constructor */
	private Processor() {
	    file = new DataFile();
	    backwardHistory = new Stack<Command>();
	    forwardHistory = new Stack<Command>();
	    backwardSearchListHistory = new Stack<List<Task>>();
	    forwardSearchListHistory = new Stack<List<Task>>();
	    editedTaskHistory = new Stack<Task>();
	    lastSearch = new ArrayList<Task>();
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
	
	/* Overloaded by method processCommand(Command, boolean)
	 * 
     * @param Command cmd
     */
	private Result processCommand(Command cmd) throws Exception {
		return processCommand(cmd, true);
	}
	
	/* Executes the appropriate actions for each command
     * 
     * @param Command cmd
     *      Command Object returned from Parser
     *      
     * @param boolean userInput
     *      userInput determines if the command was given by the user or internally
     *      
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
				success = blockDate(cmd, userInput);
				break;
			case UNBLOCK:
				success = unblockDate(cmd, userInput);
				break;
			case DONE:
				success = doneTasks(cmd, tasks, userInput);
				break;
			case TODO:
				success = toDoTasks(cmd, tasks, userInput);
				break;
			case UNDO:
				success = undoCommand(cmd, tasks, userInput);
				break;
			case REDO:
				success = redoCommand(cmd, tasks, userInput);
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
				forwardHistory.clear();
				backwardHistory.push(cmd);
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
		Task newTask = new Task(cmd.get("name"), cmd.get("due"), cmd.get("start"), cmd.get("end"), cmd.getTags());
		tasks.add(newTask);
		return file.addNewTask(newTask);
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
	    int taskId = 0;
	    try {
	        taskId = Integer.parseInt(cmd.get("id"));
	    } catch (Exception e) {
	        return false;
	    }
	    
	    if (taskId > 0) {
	        String taskName = cmd.get("name");
	        String taskDue = cmd.get("due");
	        String taskStart = cmd.get("start");
	        String taskEnd = cmd.get("end");
	        List<String> taskTags = cmd.getTags();
	        
    	    Task existingTask = file.getTask(taskId);
    	    Task oldTask = new Task(existingTask);
    	    
    	    editedTaskHistory.push(oldTask);
    	    tasks.add(existingTask);
    	    return file.editTask(existingTask, taskName, taskDue, taskStart, taskEnd, taskTags);
	    }
	    return false;
	    //Does it allows delete of parameters?
	}

	/* KIV: Removes a parameter in the Task */
	private void deleteParameter(Task existingTask, String parameterToRemove) {
        switch (parameterToRemove) {
            case "name":
                existingTask.resetName();
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
			    if (userInput) {
			        forwardSearchListHistory.push(lastSearch);
			    }
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
		Task t = file.getTask(Integer.parseInt(cmd.get("id")));
		if (t == null) {
		    return false;
		} else {
            tasks.add(t);
			return file.deleteTask(t);
		}
	}
	
	/* Deletes all Tasks in searchList */
	private boolean deleteSearchedTasks(Command cmd, List<Task> tasks) {
	    try {
            List<Task> deleteList = forwardSearchListHistory.pop();
            for (Task t : deleteList) {
                file.deleteTask(t);
                tasks.add(t);
            }
            backwardSearchListHistory.push(deleteList);
        } catch (NullPointerException e) {
            return false;
        }
        return true;
	}

	/* Executes "Restore" operation
	 * Restores a deleted Task
	 * Allows restore <id>, restore search
     * @return true/false on whether operation is performed
     */
	private boolean restoreTask(Command cmd, List<Task> tasks, boolean userInput) {
	    boolean success = false;
	    switch (cmd.get("rangeType")) {
    		case "id":
    			success = restoreUsingId(cmd, tasks);
    			break;
    		case "search":
    		    if (userInput) {
    		        backwardSearchListHistory.push(lastSearch);
        		}
        		success = restoreUsingSearch(cmd, tasks);
        		break;
    		default:
    			return false;
    	}
	    return success;
	}

	/* Restores a deleted Task using Id */
	private boolean restoreUsingId(Command cmd, List<Task> tasks) {
	    int taskId = Integer.parseInt(cmd.get("id"));
		boolean success = file.restoreTask(taskId);
		if (success) {
			tasks.add(file.getTask(taskId));
		}
		
		return success;
	}
	
	/* Restores all deleted Tasks due to 'delete search' */
	private boolean restoreUsingSearch(Command cmd, List<Task> tasks) {
	    try {
	        List<Task> restoreList = backwardSearchListHistory.pop();
	        for (Task t : restoreList) {
	            file.restoreTask(t);
	            tasks.add(t);
	        }
	        forwardSearchListHistory.push(restoreList);
	    } catch (NullPointerException e) {
	        return false;
	    }
	    return true;
	}

	//Use with *CAUTION* - Wipes entire DataFile
	public boolean deleteAllData() {
		file.wipeFile();
		return true;
	}

	/* Executes "search" operation
	 * Allows search <date>, search <key1> <key2> #tag1 #tag2
     * @return true/false on whether operation is performed
     */
	private boolean searchTasks(Command cmd, List<Task> tasks, boolean userInput) {
	    initialiseNewSearchList();
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
	
	/* Initiates new search list */
	private void initialiseNewSearchList() {
	    lastSearch = new ArrayList<Task>();
	}
	
	/* Performs search using date */
	private void searchUsingDate(String date, List<Task> tasks) {
	    List<Task> toDoTasks = file.getToDoTasks();
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
	    List<Task> toDoTasks = file.getToDoTasks();
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
                return true;
            }
        }
        return false;
	}
	
	/* Updates the Search result */
	private void updateSearchList(List<Task> tasks) {
	    for (Task task : tasks) {
	        lastSearch.add(task);
	    }
	}
	
	/* Executes "display" operation
	 * Allows display, display <id>, display search
	 * Allows show, show <id>, show search
     * @return true
     */
	private boolean displayTask(Command cmd, List<Task> tasks, boolean userInput) {
		switch (cmd.get("rangeType")) {
			case "id":
			    int taskId = Integer.parseInt(cmd.get("id"));
				tasks.add(file.getTask(taskId));
				break;
			case "search":
				tasks = lastSearch;
				break;
			case "all":
				tasks.addAll(file.getToDoTasks());
				break;
		}
		return true;
	}

	/* Executes "block" operation
     * @return true/false on whether operation is performed
     */
	private boolean blockDate(Command cmd, boolean userInput) {
		return false;
	}

	/* Executes "unblock" operation
     * @return true/false on whether operation is performed
     */
	private boolean unblockDate(Command cmd, boolean userInput) {
		return false;
	}
	
	/* Executes "done" operation
	 * Marks a task as 'done'
     * @return true/false on whether operation is performed
     */
	private boolean doneTasks(Command cmd, List<Task> tasks, boolean userInput) {
		int taskId = Integer.parseInt(cmd.get("id"));
	    Task existingTask = file.getTask(taskId);
	    tasks.add(existingTask);
        return file.doneTask(existingTask);
	}

	/* Executes "todo" operation
	 * Marks a 'done' task as 'todo'
     * @return true/false on whether operation is performed
     */
	private boolean toDoTasks(Command cmd, List<Task> tasks, boolean userInput) {
	    int taskId = Integer.parseInt(cmd.get("id"));
        Task existingTask = file.getTask(taskId);
		tasks.add(existingTask);
		return file.toDoTask(existingTask);
	}
	
	/* Executes "undo" operation
	 * Applicable for 'Add', 'Edit', 'Delete', 'Restore', 'Block', 'Unblock', 'Done', 'Todo'
     * @return true/false on whether operation is performed
     */
	private boolean undoCommand(Command cmd, List<Task> tasks, boolean userInput) {
	    boolean success = false;
		if (!backwardHistory.isEmpty()) {
			Command backwardCommand = backwardHistory.pop();
			switch(backwardCommand.getType()) {
				case ADD:
					success = undoAdd(backwardCommand, tasks);
					break;
				case EDIT:
					success = undoEdit(backwardCommand, tasks);
					break;
				case DELETE:
					success = restoreTask(backwardCommand, tasks, false);
					break;
				case RESTORE:
					success = deleteTask(backwardCommand, tasks, false);
					break;
				case BLOCK:
				    //unblock
				    success = unblockDate(backwardCommand, false);
                    break;
				case UNBLOCK:
				    //block
				    success = blockDate(backwardCommand, false);
                    break;
				case TODO:
					success = doneTasks(backwardCommand, tasks, false);
					break;
				case DONE:
					success = toDoTasks(backwardCommand, tasks, false);
					break;
				default:
				    return false;
			}
            modifyHistory(backwardCommand, success, false);
		}
		return success;
	}

	/* Undo the add command */
	private boolean undoAdd(Command cmd, List<Task> tasks) {
	    int taskId = file.getToDoTasks().size() - 1;
		Task toDelete = file.getToDoTasks().get(taskId);
		tasks.add(toDelete);
		return file.wipeTask(toDelete);
	}

	/* Reverts the previous edit of a Task */
	private boolean undoEdit(Command cmd, List<Task> tasks) {
		Task prevTask = editedTaskHistory.pop();
		
		String taskName = prevTask.getName();
		String taskDue = prevTask.getDue();
		String taskStart = prevTask.getStart();
		String taskEnd = prevTask.getEnd();
		List<String> taskTags = prevTask.getTags();
		
		tasks.add(prevTask);
		return file.editTask(prevTask.getId(), taskName, taskDue, taskStart, taskEnd, taskTags);
	}
	
	/* Executes "redo" operation
     * Applicable for 'Add', 'Edit', 'Delete', 'Restore', 'Block', 'Unblock', 'Done', 'Todo'
     * @return true/false on whether operation is performed
     */
	private boolean redoCommand(Command cmd, List<Task> tasks, boolean userInput) throws Exception {
		if (forwardHistory.isEmpty()) {
		    return false;
		} else {
			Command forwardCommand = forwardHistory.pop();
			Result result = processCommand(forwardCommand, false);
			tasks.addAll(result.getTasks());
			modifyHistory(forwardCommand, result.isSuccess(), true);
			return result.isSuccess();
		}
	}
	
	/*This method pushes command to the respective stack after undo/redo
	 *If undo/redo operation is unsuccessful, command is pushed back to the stack it was popped from
	 *@param cmd
	 *@param success
	 *@param redo - true for redo operations, false for undo operations
	 */
	private void modifyHistory(Command cmd, boolean success, boolean redo) {
	    if (success && redo || !success && !redo) {
	        backwardHistory.push(cmd);
	    } else {
	        forwardHistory.push(cmd);
	    }
	}
	
	public DataFile getFile() {
	    return file;
	}
}