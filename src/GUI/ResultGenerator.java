package GUI;

import java.util.ArrayList;
import java.util.List;

import Logic.CommandType;
import Logic.Processor;
import Logic.Result;
import Storage.Task;
/**
 * It creates a success or error message for any user input entered by the end user.
 * The class calls Processor to process the input and then converts the result object returned by processor into error or success messages
 * Note: All methods in this class are static
 * @author Sharon
 *
 */
public class ResultGenerator {

	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	private static final int FIRST_ELEMENT = 0;
	private static final String FORMAT_DOT_AND_SPACE = ". ";
	private static final String SUCCESSFUL_ADD = "Added %1$s";
	private static final String SUCCESSFUL_EDIT = "Edited %1$s";
	private static final String SUCCESSFUL_SEARCH = "Found %1$s result(s):";
	private static final String SUCCESSFUL_TODO = "Marked %1$s as to do";
	private static final String SUCCESSFUL_DONE = "Marked %1$s as done";
	private static final String SUCCESSFUL_DELETE = "Deleted!";
	private static final String SUCCESSFUL_RESTORE = "Restored!";
	private static final String SUCCESSFUL_UNDO = "Travelled back in time! Command has been undone";
	private static final String SUCCESSFUL_REDO = "Travelled into the future! Command has been redone";

	// to be changed at a later implementation -- display and show same implementation.
	// 
	// Not implemented: search, block, unblock, help
	private static final String SUCCESSFUL_BLOCK = "Successfully blocked"; // include:
																			// range
																			// blocked?
	private static final String SUCCESSFUL_UNBLOCK = "Successfully unblocked"; // include:
																				// range
																				// unblocked?

	// to be implement: Help
	private static final String CODE_EXIT = " exit";
	private static final String UNSUCCESSFUL_SEARCH_MESSAGE = "We could not find any results :( Try using different words?";
	private static final String UNSUCCESSFUL_COMMAND_MESSAGE = "'%1$s'was not recognised.";
	private static final String UNSUCCESSFUL_DISPLAY_NO_TASKS = "You are a free man! You do not have any tasks.";
	private static final String EMPTY_MESSAGE = "That was read as empty. Try again.";
	private static final String ERROR_DISPLAY = "Unable to display.";
	private static final String ERROR_COMMAND_MESSAGE = "Houston, we have a problem";

	private static final String PARA_STRING_ID = "Task ID: ";
	private static final String PARA_STRING_NAME = "Name: ";
	private static final String PARA_STRING_DUE = "Due: ";
	private static final String PARA_STRING_START = "Start: ";
	private static final String PARA_STRING_END = "End: ";
	private static final String PARA_STRING_TAG = "Tag: ";
	private static final String PARA_STRING_STATUS = "Status: ";
	private static final String PARA_STRING_VALUE_EMPTY = "<empty>";

	private static final String STATUS_DELETED = "Deleted";
	private static final String STATUS_TODO = "To Do";
	private static final String STATUS_DONE = "Done";

	private static final String HASHTAG = "#";
	
	/**
	 * This method passes a string object containing user commands to the Processor class to process.
	 * @param userInput a string containing commands that the user entered
	 * @return a String object containing success messages if the command was carried out or error messages if the command failed
	 * @throws Exception 
	 */
	public static String sendInput(String userInput) throws Exception {

		if (isEmpty(userInput)) {
			return EMPTY_MESSAGE;
		}
		Processor processor = Processor.getInstance();
		
		/** if(isResultValid(result)){
		//String message = getResultMessa ge(result);
		// find command Type
		// then see if command is successful --> return sucess
		// if command is unsuccessful --> return error message specific to commadn
		 else{}
		     **/
		Result result = processor.processInput(userInput);
		String message;
	
		if (result.isSuccess()) {
			CommandType commandDone = result.getCommandType();
			message = getResultMessage(commandDone, result);
		} else {
			message = String.format(UNSUCCESSFUL_COMMAND_MESSAGE, userInput);
		}
		return message;
	}

	private static boolean isEmpty(String line) {
		String message = line.trim();
		if (message.isEmpty()) {
			return true;
		}

		return false;
	}

	private static String getResultMessage(CommandType commandDone,
			Result result) {
		List<Task> tasks = result.getTasks();
		switch (commandDone) {
		case ADD:
			return singleLineSuccessMessage(SUCCESSFUL_ADD, tasks);
		case DELETE:
			return SUCCESSFUL_DELETE;
		case EDIT:
			return singleLineSuccessMessage(SUCCESSFUL_EDIT, tasks);
		case DISPLAY:
			return successfulDisplayMessage(tasks);
		case SEARCH:
			return successfulSearchMessage(tasks);
		case TODO:
			return singleLineSuccessMessage(SUCCESSFUL_TODO, tasks);
		case DONE:
			return singleLineSuccessMessage(SUCCESSFUL_DONE, tasks);
		case RESTORE:
			return SUCCESSFUL_RESTORE;
		case BLOCK:
			return SUCCESSFUL_BLOCK;
		case UNBLOCK:
			return SUCCESSFUL_UNBLOCK;
		case UNDO:
			return SUCCESSFUL_UNDO;
		case REDO:
			return SUCCESSFUL_REDO;
		case EXIT:
			// to be changed
			return CODE_EXIT;
		default:
			return ERROR_COMMAND_MESSAGE;
		}
	}

	// Returns message of format "Successfully (task done) (task name)"
	// Pre-condition: tasks only has one element
	private static String singleLineSuccessMessage(String message,
			List<Task> tasks) {
		assert (tasks.size() == 1);
		Task task = tasks.get(FIRST_ELEMENT);
		String taskName = task.getName();
		return String.format(message, taskName);
	}

	private static String successfulSearchMessage(List<Task> tasks) {
		int numOfSearchResults = tasks.size();
		if (numOfSearchResults == 0) {
			return UNSUCCESSFUL_SEARCH_MESSAGE;
		}
		String successMessage = String.format(SUCCESSFUL_SEARCH,
				numOfSearchResults) + LINE_SEPARATOR;
		String stringOfSearchResults = successfulDisplayMessage(tasks);
		successMessage = successMessage + stringOfSearchResults;
		return successMessage;
	}

	private static String successfulDisplayMessage(List<Task> tasks) {
		int itemsToDisplay = tasks.size();
		if (itemsToDisplay == 0) {
			return UNSUCCESSFUL_DISPLAY_NO_TASKS;
		} else if (itemsToDisplay == 1) {
			return successfulDisplaySingleTask(tasks);
		}

		List<String> displayList = changeTaskListToStringList(tasks,
				itemsToDisplay);
		String successMessage = changeStringListToString(displayList);
		return successMessage;
	}

	private static String successfulDisplaySingleTask(List<Task> tasks) {
		Task task = tasks.get(0);
		if (task == null) {
			return ERROR_DISPLAY;
		}
		// assumes name cannot empty
		assert (!task.getName().isEmpty());
		String name = task.getName();
		int iD = task.getId();
		String message = LINE_SEPARATOR + PARA_STRING_ID + iD + LINE_SEPARATOR
				+ PARA_STRING_NAME + name;
		message = addOtherParameters(task, message);

		return message;
	}

	// Format of each element in the List is "(task id). (task name)"

	private static List<String> changeTaskListToStringList(List<Task> tasks,
			int size) {
		List<String> tasksInString = new ArrayList<String>();
		for (int index = 0; index < size; index++) {
			Task currentTask = tasks.get(index);
			String taskName = currentTask.getName();
			int taskId = currentTask.getId();
			tasksInString.add(taskId + FORMAT_DOT_AND_SPACE + taskName);
		}
		return tasksInString;
	}

	private static String changeStringListToString(List<String> list) {
		int length = list.size();
		String string = "";
		for (int index = 0; index < length - 1; index++) {
			String currentElement = list.get(index);
			string = string + currentElement + LINE_SEPARATOR;
		}

		// this prevents the last line from having an next line character
		// appended to it
		int lastIndex = length - 1;
		String lastElement = list.get(lastIndex);
		string = string + lastElement;
		return string;
	}

	private static String addOtherParameters(Task task, String message) {
		String due = task.getDue().toString();
		message = addToMessage(message, PARA_STRING_DUE, due);
		String start = task.getStart().toString();
		message = addToMessage(message, PARA_STRING_START, start);
		String end = task.getEnd().toString();
		message = addToMessage(message, PARA_STRING_END, end);
		List<String> tags = task.getTags();
		message = addTags(message, tags);
		message = addStatus(message, task);
		return message;
	}

	// add tags
	private static String addTags(String message, List<String> tags) {
		if (tags.isEmpty() || tags == null) {
			message = addToMessage(message, PARA_STRING_TAG,
					PARA_STRING_VALUE_EMPTY);
			return message;
		}
		int length = tags.size();
		message = message + LINE_SEPARATOR + PARA_STRING_TAG;
		for (int index = 0; index < length; index++) {
			String currentTag = tags.get(index);
			if (isStatus(currentTag)) {
				// moves on to the next tag if its a status
				currentTag = tags.get(index + 1);
			}
			message = message + " " + currentTag;
		}
		return message;
	}

	// delete status takes precedence over todo and done
	private static String addStatus(String message, Task task) {
		message = message + LINE_SEPARATOR + PARA_STRING_STATUS;
		if (task.isDeleted()) {
			message = message + STATUS_DELETED;
		} else if (task.isDone()) {
			message = message + STATUS_DONE;
		} else {
			message = message + STATUS_TODO;
		}

		return message;
	}

	private static boolean isStatus(String tag) {
		String tagToCheck = tag.toLowerCase();
		if (tagToCheck == HASHTAG + STATUS_DELETED
				|| tagToCheck == HASHTAG + STATUS_TODO
				|| tagToCheck == HASHTAG + STATUS_DONE) {
			return true;
		}

		return false;
	}

	private static String addToMessage(String message, String parameters,
			String toAdd) {
		if (toAdd == null || toAdd.isEmpty() || toAdd.equals("null")) {
			message = message + LINE_SEPARATOR + parameters
					+ PARA_STRING_VALUE_EMPTY;
		} else {
			message = message + LINE_SEPARATOR + parameters + toAdd;
		}
		return message;
	}

}
