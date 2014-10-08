package GUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Logic.Processor;
import Logic.Result;
import Logic.CommandType;
import Storage.Task;


public class ResultGenerator {

    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private static final int FIRST_ELEMENT = 0;
    private static final String FORMAT_DOT_AND_SPACE = ". ";
    private static final String SUCCESSFUL_ADD = "Added %1$s";
    private static final String SUCCESSFUL_EDIT = "Edited %1$s";
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
    private static final String UNSUCCESSFUL_COMMAND_MESSAGE = "'%1$s'was not recognised.";
    private static final String UNSUCCESSFUL_DISPLAY_NO_TASKS = "You are a free man! You do not have any tasks.";
    private static final String EMPTY_MESSAGE = "That was read as empty. Try again.";
    private static final String ERROR_DISPLAY = "Unable to display.";
    private static final String ERROR_COMMAND_MESSAGE = "Houston, we have a problem";
    
   private static final String PARA_STRING_ID = "Task ID: ";
    private static final String PARA_STRING_NAME = "Name: ";
    private static final String PARA_STRING_MORE = "More: ";
    private static final String PARA_STRING_DUE = "Due: ";
    private static final String PARA_STRING_START = "Start: ";
    private static final String PARA_STRING_END = "End: ";
    private static final String PARA_STRING_PRIORITY = "Priority: ";
    private static final String PARA_STRING_TAG = "Tag: ";
    private static final String PARA_STRING_STATUS = "Status: ";
    private static final String PARA_STRING_VALUE_EMPTY = "<empty>";
    
    private static final String STATUS_DELETED = "Deleted";
    private static final String STATUS_TODO = "To Do";
    private static final String STATUS_DONE = "Done";
    
    private static final String HASHTAG = "#";

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
        	message = String.format(UNSUCCESSFUL_COMMAND_MESSAGE, userInput);
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
        if(itemsToDisplay == 0){
        	return UNSUCCESSFUL_DISPLAY_NO_TASKS;
        }
        else if(itemsToDisplay == 1){
        	return successfulDisplaySingleTask(tasks);
        }
        ArrayList<String> displayList = changeTaskListToString(tasks,
                itemsToDisplay);
        String successMessage = changeStringListToString(displayList);
        return successMessage;
    }
    
    public static String successfulDisplaySingleTask(ArrayList<Task> tasks){
    	Task task = tasks.get(0);
    	if(task == null){
    		return ERROR_DISPLAY;
    	}
    	// assumes name cannot empty
    	assert(!task.getName().isEmpty());
    	String name = task.getName();
    	int iD = task.getId();
    	String message = PARA_STRING_ID + iD + LINE_SEPARATOR + PARA_STRING_NAME + name;
    	message = addOtherParameters(task, message);
		
		return message;
    }

    // Format of each element in the arrayList is "(task id). (task name)"
    private static ArrayList<String> changeTaskListToString(
            ArrayList<Task> tasks, int size) {
        ArrayList<String> tasksInString = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
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
    
    private static String addOtherParameters(Task task, String message) {
		String more =  task.getMore();
    	message = addToMessage(message, PARA_STRING_MORE, more);
    	String due = task.getDue();
    	message = addToMessage(message, PARA_STRING_DUE, due);
    	String start =  task.getStart();
		message = addToMessage(message, PARA_STRING_START, start);
		String end =  task.getEnd();
		message = addToMessage(message, PARA_STRING_END, end);
		String priority =  task.getPriority();
		message = addToMessage(message, PARA_STRING_PRIORITY, priority);
		List<String> tags = task.getTags();
		message = addTags(message, tags);
		message = addStatus(message, task);
		return message;
	}
    
    private static String addTags(String message, List<String> tags){
    	if(tags.isEmpty() || tags == null){
    		message = addToMessage(message, PARA_STRING_TAG, PARA_STRING_VALUE_EMPTY);
    		return message;
    	}
    	int length = tags.size();
    	message = message + LINE_SEPARATOR + PARA_STRING_TAG;
    	for(int index = 0; index<length; index++){
    		String currentTag = tags.get(index);
    		if(isStatus(currentTag)){
    			// moves on to the next tag if its a status
    			currentTag = tags.get(index+1);
    		}
    		message = message + " " + currentTag;
    	}
    	return message;
    }
    
    // delete status takes precedence over todo and done
    private static String addStatus(String message, Task task){
    	message = message + LINE_SEPARATOR + PARA_STRING_STATUS;
    	if(task.isDeleted()){
    		message = message + STATUS_DELETED;
    	}else if(task.isDone()){
    		message = message + STATUS_DONE;
    	}
    	else{
    		message = message + STATUS_TODO;
    	}
    	
    	return message;
    }
    
    private static boolean isStatus(String tag){
    	String tagToCheck = tag.toLowerCase();
    	if(tagToCheck == HASHTAG + STATUS_DELETED || tagToCheck == HASHTAG + STATUS_TODO || tagToCheck == HASHTAG + STATUS_DONE){
    		return true;
    	}
    	
    	return false;
    }
    
    private static String addToMessage(String message, String parameters, String toAdd){
    	if(toAdd == null || toAdd.isEmpty() || toAdd.equals("null")){
    		message = message + LINE_SEPARATOR + parameters + PARA_STRING_VALUE_EMPTY;
    	}else{
    	message = message + LINE_SEPARATOR + parameters + toAdd;
    	}
    	return message;
    }
    
    
}
