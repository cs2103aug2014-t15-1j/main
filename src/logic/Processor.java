package logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Stack;
import java.util.Timer;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;

import logic.Result.ResultType;
import parser.Parser;
import database.BlockDate;
import database.DataFile;
import database.Task;

/**
 * This class handles inputs from UI and interacts with other components for the
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
    
    protected final static boolean LOGGING_ENABLED = false;
    protected static boolean IS_UNIT_TEST = false;
    
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
	
	/** Last search performed*/
	private List<Task> lastSearch;
	
	/** List of Tasks without Due date/time */
	private List<Task> floatingTasks;
	
	/** List of Tasks with Due date/time */
	private List<Task> timedTasks;
	
	/** Logger for monitoring purposes */
	private static final Logger log = Logger.getLogger(Processor.class.getName());
	
    /** Stores input string for 'up' key **/
    private Stack<String> inputStringBackwardHistory;
    
    private String currentInputString;
    
	/** Stores input string for 'down' key **/
	private Stack<String> inputStringForwardHistory;
	
    private Timer secondsTimer;
	
	/** Default Constructor for Processor */
    private Processor() {
        this(IS_UNIT_TEST);
    }
    
	private Processor(boolean isTest) {
	    if (isTest) {
	        file = new DataFileStub();
	    } else {
	        file = new DataFile();
	    }
	    backwardCommandHistory = new Stack<Command>();
	    forwardCommandHistory = new Stack<Command>();
	    backwardSearchListHistory = new Stack<List<Task>>();
	    forwardSearchListHistory = new Stack<List<Task>>();
	    editedTaskHistory = new Stack<Task>();
	    lastSearch = new ArrayList<Task>();
	    floatingTasks = new ArrayList<Task>();
	    timedTasks = new ArrayList<Task>();
	    inputStringBackwardHistory = new Stack<String>();
	    inputStringForwardHistory = new Stack<String>();
	    currentInputString = "";
	    
	    secondsTimer = new Timer("secondsTimer", true);
	    secondsTimer.scheduleAtFixedRate(new TaskScheduler(), 0, (long) 1000);
	    
	    initialiseLogger();
	    updateFloatingAndTimedTasks();
	}
	
	private static void initialiseLogger() {
        if (LOGGING_ENABLED) {
    	    try {
    	        FileHandler fh = new FileHandler("Processor.log", true);  
    	        Formatter format = new LogFormatter();
    	        fh.setFormatter(format);
    	        log.addHandler(fh);
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
        }
    }

    /** 
	 * This method returns an instance of Processor
     * @return Instance of Processor
     */
	public static synchronized Processor getInstance() {
	    if (processor == null) {
	        processor = new Processor();
	    }
	    return processor;
	}
	
	public static Processor reset() {
	    processor.wipeFile();
	    processor.getTimer().cancel();
	    processor = new Processor();
	    return processor;
	}
	
	/**
	 * This methods processes the input by the user 
	 * @param String
     * @return Result
     */
	public Result processInput(String input) throws IllegalArgumentException {
	    updateInputHistory(input);
	    Command cmd = Parser.parse(input);
		assert cmd != null;
		return processCommand(cmd);
	}

    private void updateInputHistory(String input) {
        for (String strInput : inputStringForwardHistory) {
	        if (!strInput.isEmpty()) {
	            inputStringBackwardHistory.push(strInput);
	        }
        }
	    inputStringForwardHistory.clear();
	    inputStringBackwardHistory.push(input);
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
     *      - userInput determines if the command was given by the user or internally by the system
     *      
     * @return Result
     *      - boolean {@code success}<br>
     *      List{@literal <Task>} {@code tasks} - This reference is passed into 
     *      the methods to return {@code Task} that are affected in the operation<br>
     *      CommandType {@code cmdExecuted}
     */
	protected Result processCommand(Command cmd, boolean userInput) {
		if (cmd == null || cmd.getType() == CommandType.ERROR) {
		    if (LOGGING_ENABLED) {
		        log.warning("Error in the input, unable to perform operation.");
		    }
		    return new Result(null, false, CommandType.ERROR, ResultType.TASK);
		} else {
    		Result result = cmd.execute(userInput);
    		if (result.isSuccess() && !result.needsConfirmation() && userInput) {
    			updateCommandHistory(cmd);
    			if (LOGGING_ENABLED) {
    			    log.info(result.getCommandType() + " Command executed successfully");
    			}
    		}
    		updateUIPaneWindow();
    		return result;
		}
	}
	
	/** 
	 * Adds command to the history list
     * @param cmd
     */
	private void updateCommandHistory(Command cmd) {
		if (hasModifiedData(cmd)) {
				forwardCommandHistory.clear();
				backwardCommandHistory.push(cmd);
		}
	}

	private void updateUIPaneWindow() {
	    updateFloatingAndTimedTasks();
        setChanged();
        notifyObservers("updateui");
        if (LOGGING_ENABLED) {
            log.info("Updated side panel.");
        }
	}
	
	private boolean hasModifiedData(Command cmd) {
        switch (cmd.getType()) {
            case ADD:
            case DELETE:
            case EDIT:
            case RESTORE:
            case BLOCK:
            case UNBLOCK:
            case DONE:
            case TODO:
            case RESET:
                return true;
            default:
                return false;
        }
    }
    
	private void updateFloatingAndTimedTasks() {
	    clearPanelTaskList();
	    for (Task task : file.getToDoTasks()) {
	        if (!task.getDue().isEmpty()) {
	            timedTasks.add(task);
	        } else {
	            floatingTasks.add(task);
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
	    if (LOGGING_ENABLED) {
	        log.info("Fetching Timed Tasks");
	    }
        Collections.sort(timedTasks);
        return Collections.unmodifiableList(timedTasks);
	}
	
	public List<Task> fetchFloatingTasks() {
	    if (LOGGING_ENABLED) {
	        log.info("Fetching Floating Tasks");
	    }
	    Collections.sort(floatingTasks);
	    return Collections.unmodifiableList(floatingTasks);
	}
	
	public List<Task> fetchToDoTasks() {
	    if (LOGGING_ENABLED) {
	        log.info("Fetching Todo Tasks");
	    }
        return file.getToDoTasks();
    }
    
	
	public List<BlockDate> fetchBlockedDate() {
	    if (LOGGING_ENABLED) {
	        log.info("Fetching Blocked Dates");
	    }
        return file.getAllBlockDates();
    }
	
	public String fetchInputUpKey() {
	    if (!inputStringBackwardHistory.isEmpty()) {
	        inputStringForwardHistory.push(currentInputString);
            currentInputString = inputStringBackwardHistory.pop();
	    }
	    return currentInputString;
    }
	
	public String fetchInputDownKey() {
        if (!inputStringForwardHistory.isEmpty()) {
            inputStringBackwardHistory.push(currentInputString);
            currentInputString = inputStringForwardHistory.pop();
        }
        return currentInputString;
    }
	
	protected DataFile getFile() {
	    return file;
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
	
	protected Timer getTimer() {
	    return secondsTimer;
	}
}