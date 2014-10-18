package GUI;

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

	private static final int FIRST_ELEMENT = 0;
	private static final String SUCCESSFUL_ADD = "Added %1$s";
	private static final String SUCCESSFUL_EDIT = "Edited %1$s";
	private static final String SUCCESSFUL_SEARCH = "Found %1$s result(s):";
	private static final String SUCCESSFUL_TODO = "Marked %1$s as to do";
	private static final String SUCCESSFUL_DONE = "Marked %1$s as done";
	private static final String SUCCESSFUL_DELETE = "Deleted!";
	private static final String SUCCESSFUL_RESTORE = "Restored!";
	private static final String SUCCESSFUL_DISPLAY = "Showing %1$s tasks";
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
	private static final String ASK_CONFIRM_DELETE = "Are you sure you want to wipe file? This is irreversible";
	private static final String CODE_EXIT = "exit";
	private static final String UNSUCCESSFUL_SEARCH_MESSAGE = "We could not find any results :( Try using different words?";
	private static final String UNSUCCESSFUL_COMMAND_MESSAGE = "'%1$s'was not recognised.";
	private static final String UNSUCCESSFUL_DISPLAY_NO_TASKS = "You are a free man! You do not have any tasks.";
	private static final String EMPTY_MESSAGE = "That was read as empty. Try again.";
	private static final String ERROR_COMMAND_MESSAGE = "Houston, we have a problem";

	/**
	 * This method passes a string object containing user commands to the Processor class to process.
	 * @param userInput a string containing commands that the user entered
	 * @return a String object containing success messages if the command was carried out or error messages if the command failed
	 * @throws Exception 
	 */
	public static String sendInput(String userInput){

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
		    assert(!tasks.isEmpty());
		    updateInterface(tasks);
			return singleLineSuccessMessage(SUCCESSFUL_ADD, tasks);
		case DELETE:
		    // delete all --> implement confirmation
		    if(result.needsConfirmation()){
		        return ASK_CONFIRM_DELETE;
		    }
		    updateInterface(tasks);
		    return SUCCESSFUL_DELETE;
		case EDIT:
			return singleLineSuccessMessage(SUCCESSFUL_EDIT, tasks);
		case DISPLAY:
		    updateInterface(tasks);
			return successfulDisplayMessage(tasks);
		case SEARCH:
		    updateInterface(tasks);
			return successfulSearchMessage(tasks);
		case TODO:
			return singleLineSuccessMessage(SUCCESSFUL_TODO, tasks);
		case DONE:
			return singleLineSuccessMessage(SUCCESSFUL_DONE, tasks);
		case RESTORE:
			return SUCCESSFUL_RESTORE;
		case BLOCK:
		    // ask user, if user wants to block a place with tasks assigned
			return SUCCESSFUL_BLOCK;
		case UNBLOCK:
			return SUCCESSFUL_UNBLOCK;
		case UNDO:
		    
			return SUCCESSFUL_UNDO;
		case REDO:

			return SUCCESSFUL_REDO; 
		case EXIT:
			return CODE_EXIT;
		default:
			return ERROR_COMMAND_MESSAGE;
		}
	}

	private static void updateInterface(List<Task> tasks) {
        // TODO Auto-generated method stub
        new UpdateUI(tasks);
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
				numOfSearchResults);
		return successMessage;
	}
	
	private static String successfulDisplayMessage(List<Task> tasks){
	    int numOfTasks = tasks.size();
	    if(numOfTasks == 0){
	        return UNSUCCESSFUL_DISPLAY_NO_TASKS;
	    }
	    
	    return String.format(SUCCESSFUL_DISPLAY, numOfTasks);
	}



}
