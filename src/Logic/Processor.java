package Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Stack;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;

import Logic.Result.ResultType;
import Parser.Parser;
import Storage.BlockDate;
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
    
    /** Instance of Processor */
    private static Processor processor;
    
    /** Instance of DataFile */
    private DataFile file;
    
    /** Stores Command History for Undo */
	private Stack<Command> backwardCommandHistory;
	
	/** Stores Command History for Redo */
	private Stack<Command> forwardCommandHistory;
	
	/** Stores Search History for Undo Operations*/
	private Stack<List<Task>> backwardSearchListHistory;
	
	/** Stores Search History for Redo Operations */
	private Stack<List<Task>> forwardSearchListHistory;
	
	/** Stores Task objects that are being edited */
	private Stack<Task> editedTaskHistory;
	
	/** Store a List of Blocked Date */
    private List<BlockDate> blockedDateList;
	
    /** Stores a Stack of Deleted Block Date */
    private Stack<BlockDate> deletedBlockedDateList;
    
	/** Last search performed*/
	private List<Task> lastSearch;
	
	/** List of Tasks without Due date/time */
	private List<Task> floatingTasks;
	
	/** List of Tasks with Due date/time */
	private List<Task> timedTasks;
	
	/** Logger for monitoring purposes */
	private static final Logger log = Logger.getLogger(Processor.class.getName());
	
	/** Default Constructor for Processor */
	private Processor() {
	    file = new DataFile();
	    backwardCommandHistory = new Stack<Command>();
	    forwardCommandHistory = new Stack<Command>();
	    backwardSearchListHistory = new Stack<List<Task>>();
	    forwardSearchListHistory = new Stack<List<Task>>();
	    editedTaskHistory = new Stack<Task>();
	    lastSearch = new ArrayList<Task>();
	    floatingTasks = new ArrayList<Task>();
	    timedTasks = new ArrayList<Task>();
	    blockedDateList = new ArrayList<BlockDate>();
	    deletedBlockedDateList = new Stack<BlockDate>();
	    initialiseLogger();
	}
	
	/** 
	 * This method returns an instance of Processor
     * @return Instance of Processor
     */
	public static Processor getInstance() {
	    if (processor == null) {
	        processor = new Processor();
	    }
	    return processor;
	}
	
	public static Processor reset() {
	    processor.wipeFile();
	    processor = new Processor();
	    return processor;
	}
	
	private static void initialiseLogger() {
	    try {
	        FileHandler fh = new FileHandler("Processor.log");  
	        Formatter format = new LogFormatter();
	        fh.setFormatter(format);
	        log.addHandler(fh);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * This methods processes the input by the user 
	 * @param String
     * @return Result
     */
	public Result processInput(String input) {
		Command cmd = Parser.parse(input);
		return processCommand(cmd);
	}
	
	/**
	 * Overloaded by method processCommand(Command, boolean)
	 * 
     * @param Cmd
     */
	protected Result processCommand(Command cmd) {
		return processCommand(cmd, true);
	}
	
	/**
	 * Executes the appropriate actions for each command
     * 
     * @param cmd
     *      - Command Object returned from Parser
     *      
     * @param userInput
     *      - userInput determines if the command was given by the user or internally
     *      
     * @return Result
     *      - boolean {@code success}<br>
     *      List{@literal <Task>} {@code tasks} - This reference is passed into 
     *      the methods to return {@code Task} that are affected in the operation<br>
     *      CommandType {@code cmdExecuted}
     */
	protected Result processCommand(Command cmd, boolean userInput) {
		if (cmd == null || cmd.getType() == CommandType.ERROR) {
		    log.warning("Error in the input, unable to perform operation.");
			return new Result(null, false, CommandType.ERROR, ResultType.TASK);
		}
		Result result = cmd.execute(userInput);

		if (result.isSuccess() && !result.needsConfirmation() && userInput) {
			updateCommandHistory(cmd);
			updateUIPanelWindow(cmd);
			log.info("Command executed successfully");
		}
		return result;
	}

	private boolean hasModifiedTaskList(Command cmd) {
	    switch (cmd.getType()) {
    	    case ADD:
            case DELETE:
            case EDIT:
            case RESTORE:
            case BLOCK:
            case UNBLOCK:
            case DONE:
            case TODO:
                return true;
            default:
                return false;
	    }
	}
	
	/** 
	 * Adds command to the history list
     * @param cmd
     */
	private void updateCommandHistory(Command cmd) {
		if (hasModifiedTaskList(cmd)) {
				forwardCommandHistory.clear();
				backwardCommandHistory.push(cmd);
		}
	}

	private void updateUIPanelWindow(Command cmd) {
	    if (hasModifiedTaskList(cmd)) {
	        updateFloatingAndTimedTasks();
            setChanged();
            notifyObservers(); //Calls update of the side panel class
            log.info("Updated side panel.");
	    }
	}
	
	private void updateFloatingAndTimedTasks() {
	    clearPanelTaskList();
	    for (Task task : file.getToDoTasks()) {
	        if (task.getDue() == null && task.getEnd() == null) {
	            floatingTasks.add(task);
	        } else {
	            timedTasks.add(task);
	        }
	    }
	}
	
	private void clearPanelTaskList() {
	    floatingTasks.clear();
        timedTasks.clear();
	}
		
	private boolean wipeFile() {
	    return file.wipeFile();
	}
	
	public List<Task> getSearchList() {
        return Collections.unmodifiableList(lastSearch);
    }
	
	public List<Task> fetchTimedTasks() {
        log.info("Fetching Timed Tasks");
        return Collections.unmodifiableList(timedTasks);
	}
	
	public List<Task> fetchFloatingTasks() {
	    log.info("Fetching Floating Tasks");
	    return Collections.unmodifiableList(floatingTasks);
	}
	
	public List<BlockDate> fetchBlockedDate() {
        log.info("Fetching Blocked Dates");
        return Collections.unmodifiableList(blockedDateList);
    }
	
	protected DataFile getFile() {
	    return file;
	}
	
	protected List<BlockDate> getBlockedDates() {
	    return blockedDateList;
	}
	
	protected Stack<BlockDate> getDeletedBlockDates() {
        return deletedBlockedDateList;
    }

    protected static Logger getLogger() {
	    return log;
	}
	
	protected Stack<Task> getEditedTaskHistory() {
	    return editedTaskHistory;
	}
	
	protected Stack<Command> getBackwardCommandHistory() {
	    return backwardCommandHistory;
	}
	
	protected Stack<Command> getForwardCommandHistory() {
        return forwardCommandHistory;
    }
	
	protected Stack<List<Task>> getBackwardSearchListHistory() {
        return backwardSearchListHistory;
    }
	
	protected Stack<List<Task>> getForwardSearchListHistory() {
        return forwardSearchListHistory;
    }
	
	protected List<Task> getLastSearch() {
	    return lastSearch;
	}
	
	protected void initialiseNewSearchList() {
	    lastSearch = new ArrayList<Task>();
	}
}