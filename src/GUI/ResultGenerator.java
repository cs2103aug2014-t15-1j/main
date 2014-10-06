package GUI;

import java.io.IOException;
import java.util.ArrayList;

import Logic.Processor;
import Logic.Result;
import Logic.CommandType;
import Storage.Task;


public class ResultGenerator {

    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private static final int FIRST_ELEMENT = 0;
    private static final String FORMAT_DOT_AND_SPACE = ". ";
    private static final String SUCCESSFUL_ADD = "Added: %1$s";
    private static final String SUCCESSFUL_EDIT = "Edited: %1$s";
    private static final String SUCCESSFUL_SEARCH = "Found %1$s result(s):";

    // to be changed at a later implementation
    private static final String SUCCESSFUL_DELETE = "Deleted!";
    private static final String SUCCESSFUL_RESTORE = "Restored!";
    private static final String SUCCESSFUL_UNDO = "Travelled back in time! Command has been undone";
    private static final String SUCCESSFUL_REDO = "Travelled into the future! Command has been redone";
    private static final String SUCCESSFUL_JOKE = "There are three kinds of people. Those who can count, and those who cannot."
            + LINE_SEPARATOR + "- Unknown";

    // to be implement later SUCESSFUL: Block, Unblock, Help

    private static final String CODE_CLEAR = " clear";
    private static final String CODE_EXIT = " exit";
    private static final String UNSUCCESSFUL_SEARCH_MESSAGE = "We could not find any results :( Try using different words?";
    private static final String UNSUCCESSFUL_COMMAND_MESSAGE = "Command could not be done. Operation unsucessful :(";
    private static final String EMPTY_MESSAGE = "That was read as empty. If your stuck, type 'help'";
    private static final String ERROR_COMMAND_MESSAGE = "Houston, we have a problem";

    public static String sendInput(String userInput) throws IOException {
        if(isEmpty(userInput)){
        	return EMPTY_MESSAGE;
        }
    	Processor processor = new Processor();
        Result result = processor.processInput(userInput);
        String message;
        if(result.isSuccess()){      
        	CommandType commandDone = result.getCmdExecuted();   
        	message = getResultMessage(commandDone, result);      	
        }
        else{      
        	message = UNSUCCESSFUL_COMMAND_MESSAGE;
        }
        return message;
    }
    
    public static boolean isEmpty(String line){
    	String message = line.trim();
    	if(message.isEmpty()){
    		return true;
    	}
    	
    	return false;
    }
    public static String getResultMessage(CommandType commandDone,
            Result result) {
        ArrayList<Task> tasks = result.getTasks();
        switch (commandDone) {
            case ADD :
                return singleLineSuccessMessage(SUCCESSFUL_ADD, tasks);
            case DELETE :
                return SUCCESSFUL_DELETE;
            case EDIT :
                return singleLineSuccessMessage(SUCCESSFUL_EDIT, tasks);
            case DISPLAY:
                return successfulDisplayMessage(tasks);
            case SEARCH :
                return successfulSearchMessage(tasks);
            case RESTORE :
                return SUCCESSFUL_RESTORE;
            case UNDO:
            	return SUCCESSFUL_UNDO;
            case REDO:
            	return SUCCESSFUL_REDO;
            case JOKE :
                // to be changed
                return SUCCESSFUL_JOKE;
            case CLEAR :
                // lets Main Screen know that screen is to be cleared
                return CODE_CLEAR;
            case EXIT :
                // to be changed
                return CODE_EXIT;
            default :
                return ERROR_COMMAND_MESSAGE;
        }
    }

    // Returns message of format "Successfully (task done) (task name)"
    // Pre-condition: tasks only has one element
    public static String singleLineSuccessMessage(String message,
            ArrayList<Task> tasks) {
    	assert(tasks.size()==1);
        Task task = tasks.get(FIRST_ELEMENT);
        String taskName = task.getName();
        return String.format(message, taskName);
    }

    public static String successfulSearchMessage(ArrayList<Task> tasks) {
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

    public static String successfulDisplayMessage(ArrayList<Task> tasks) {
        int itemsToDisplay = tasks.size();
        ArrayList<String> displayList = changeTaskListToString(tasks,
                itemsToDisplay);
        String successMessage = changeStringListToString(displayList);
        return successMessage;
    }

    // Format of each element in the arrayList is "(task id). (task name)"
    private static ArrayList<String> changeTaskListToString(
            ArrayList<Task> tasks, int numOfSearchResults) {
        ArrayList<String> tasksInString = new ArrayList<String>();
        for (int index = 0; index < numOfSearchResults; index++) {
            Task currentTask = tasks.get(index);
            String taskName = currentTask.getName();
            int taskId = currentTask.getId();
            tasksInString.add(taskId + FORMAT_DOT_AND_SPACE + taskName);
        }
        return tasksInString;
    }

    private static String changeStringListToString(ArrayList<String> list) {
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
}
