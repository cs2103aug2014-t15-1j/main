package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;

import objects.DatabaseFacadeStub;
import parser.Parser;
import database.DatabaseFacade;
import database.DateTime;
import database.Task;

//import java.util.Observable; - Observer pattern not implemented

/**
 * This class handles inputs from UI and interacts with other components for the
 * necessary operations. It is dependent on <code>database</code> package for
 * operations related to the management of storage of <code>Task</code>. It is
 * also dependent on <code>parser</code> package to decipher the user input.
 * <p>
 * <i>Singleton pattern is applied and only <u>one</u> instance of Processor is
 * available per thread.</i>
 * <p>
 * Result object is being returned to the user interface for display purposes.
 * 
 * @author Ter Yao Xiang
 */

public class Processor {

    /** Logger for monitoring purposes */
    protected final static boolean LOGGING_ENABLED = false;
    private static final Logger log = Logger.getLogger(Processor.class
            .getName());

    protected static boolean IS_UNIT_TEST = false;

    /** Instance of Processor */
    private static Processor processor;

    /** Instance of DatabaseFacade */
    private DatabaseFacade file;

    /** Stores Command History for Undo */
    private Stack<Command> backwardCommandHistory;

    /** Stores Command History for Redo */
    private Stack<Command> forwardCommandHistory;

    /** Stores Search History for Undo Operations */
    private Stack<List<Task>> backwardSearchListHistory;

    /** Stores Search History for Redo Operations */
    private Stack<List<Task>> forwardSearchListHistory;

    /** Stores Task objects that are being edited */
    private Stack<Task> editedTaskHistory;

    /** Last search performed */
    private List<Task> lastSearch;

    /** List of Tasks without Due date/time */
    private List<Task> floatingTasks;

    /** Stores input string for 'up' key **/
    private Stack<String> inputStringBackwardHistory;

    private String currentInputString;

    /** Stores input string for 'down' key **/
    private Stack<String> inputStringForwardHistory;

    /**
     * Default Constructor for Processor.
     * <p>
     * Injects dependency to the UI
     */
    private Processor() {
        this(IS_UNIT_TEST);
    }

    private Processor(boolean isTest) {
        if (isTest) {
            file = new DatabaseFacadeStub();
        } else {
            file = new DatabaseFacade();
        }
        initialiseProcessor();
        initialiseLogger();
        updateFloatingTasksList();
    }

    private void initialiseProcessor() {
        backwardCommandHistory = new Stack<Command>();
        forwardCommandHistory = new Stack<Command>();
        backwardSearchListHistory = new Stack<List<Task>>();
        forwardSearchListHistory = new Stack<List<Task>>();
        editedTaskHistory = new Stack<Task>();
        lastSearch = new ArrayList<Task>();
        floatingTasks = new ArrayList<Task>();
        inputStringBackwardHistory = new Stack<String>();
        inputStringForwardHistory = new Stack<String>();
        currentInputString = "";
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
     * 
     * @return Instance of Processor
     */
    public static Processor getInstance() {
        if (processor == null) {
            processor = new Processor();
        }
        return processor;
    }

    /**
     * This method resets the instance of Processor By resetting the instance of
     * processor, the DataFile is also wiped. This bring the program back to its
     * initial state.
     * 
     * @return Instance of Processor
     */
    public static Processor reset() {
        processor.wipeFile();
        processor = new Processor();
        return processor;
    }

    /**
     * This method processes the input that is typed in by the user through the
     * text box provided in the interface.
     * 
     * @param input
     *            - Input given by the user.
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean)
     *         Result}
     * @throws IllegalArgumentException
     */
    public Result processInput(String input) throws IllegalArgumentException {
        assert input != null;
        if (LOGGING_ENABLED) {
            log.warning("Command entered: " + input);
        }
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
     * Executes the appropriate actions for each command.
     * 
     * @param cmd
     *            - Command object returned from Parser.
     * @param userInput
     *            - True if the command was given by the user.
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean)
     *         Result}
     */
    protected Result processCommand(Command cmd, boolean userInput) {
        if (cmd == null || cmd.getType() == null) {
            if (LOGGING_ENABLED) {
                log.warning("Error in the input, unable to perform operation.");
            }
            return new Result(null, false, null, "");
        } else {
            Result result = cmd.execute(userInput);
            if (result.isSuccess() && !result.needsConfirmation() && userInput) {
                updateCommandHistory(cmd);
                log(result.getCommandType() + " Command executed successfully");
            }
            updateFloatingTasksList();
            return result;
        }
    }

    protected static void log(String output) {
        if (LOGGING_ENABLED) {
            log.info(output);
        }
    }

    /**
     * This method updates the command history. Only commands that modifies
     * <code>Task</code> objects will be added into the history. Forward command
     * history will be cleared when a <code>Command</code> is added, similar to
     * how a web browser history works.
     * 
     * @param cmd
     */
    private void updateCommandHistory(Command cmd) {
        if (hasModifiedData(cmd)) {
            forwardCommandHistory.clear();
            backwardCommandHistory.push(cmd);
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

    private void updateFloatingTasksList() {
        clearFloatingTaskList();
        for (Task task : file.getToDoTasks()) {
            if (task.getDue().isEmpty()) {
                floatingTasks.add(task);
            }
        }
    }

    private void clearFloatingTaskList() {
        floatingTasks.clear();
    }

    private boolean wipeFile() {
        return file.resetData();
    }

    /**
     * This method fetches the previous command entered. Returns an empty string
     * if there is no last command entered
     * 
     * @return String
     */
    public String fetchPreviousCommand() {
        if (!inputStringBackwardHistory.isEmpty()) {
            inputStringForwardHistory.push(currentInputString);
            currentInputString = inputStringBackwardHistory.pop();
        }
        return currentInputString;
    }

    /**
     * This method fetches the next command entered. Returns an empty string if
     * there is no last command entered.
     * 
     * @return String
     */
    public String fetchNextCommand() {
        if (!inputStringForwardHistory.isEmpty()) {
            inputStringBackwardHistory.push(currentInputString);
            currentInputString = inputStringForwardHistory.pop();
        }
        return currentInputString;
    }

    /**
     * This method fetches results from the last search performed.
     * 
     * @return List{@literal<Task>} - Last search result
     */
    public List<Task> fetchSearchList() {
        return Collections.unmodifiableList(lastSearch);
    }

    /**
     * This method fetches all the tasks (inclusive of todo, done, block).
     * 
     * @return List{@literal<Task>} - All Tasks
     */
    public List<Task> fetchAllTasks() {
        return file.getAllTasks();
    }

    /**
     * This method fetches the task by Id.
     * 
     * @return {@link database.Task#Task(String, DateTime, DateTime, DateTime, List, database.TaskType)
     *         Task}
     */
    public Task fetchTaskById(int taskId) {
        return processor.getFile().getTask(taskId);
    }

    /**
     * This method fetches tasks that have no due dates
     * 
     * @return List{@literal<Task>} - Someday/Floating tasks
     */
    public List<Task> fetchFloatingTasks() {
        log("Fetching Floating Tasks");
        return Collections.unmodifiableList(floatingTasks);
    }

    /**
     * This method fetches tasks that have status marked as {@code Todo}
     * 
     * @return List{@literal<Task>} - {@code Todo} tasks
     */
    public List<Task> fetchToDoTasks() {
        log("Fetching Todo Tasks");
        return file.getToDoTasks();
    }

    /**
     * This method fetches tasks that have status marked as {@code Done}
     * 
     * @return List{@literal<Task>} - {@code Done} tasks
     */
    public List<Task> fetchDoneTasks() {
        log("Fetching Done Tasks");
        return file.getDoneTasks();
    }

    /**
     * This method fetches tasks that have status marked as {@code Deleted}
     * 
     * @return List{@literal<Task>} - {@code Deleted} tasks
     */
    public List<Task> fetchDeletedTasks() {
        log("Fetching Deleted Tasks");
        return file.getDeletedTasks();
    }

    /**
     * This method fetches tasks that have status marked as {@code Block}
     * 
     * @return List{@literal<Task>} - {@code Block} tasks
     */
    public List<Task> fetchBlockTasks() {
        log("Fetching Block Tasks");
        return file.getBlockTasks();
    }

    /**
     * This method fetches tasks that start or end today
     * 
     * @return List{@literal<Task>} - Tasks which start/due today
     */
    public List<Task> fetchTodayTasks() {
        log("Fetching Today Tasks");
        String todayDateStr = Parser.getCurrDateStr();
        DateTime todayDate = new DateTime(Parser.getCurrDateStr(), "2359");
        List<Task> output = new ArrayList<Task>();
        for (Task task : file.getAllTasks()) {
            if (hasSameDueOrStart(todayDateStr, task)) {
                output.add(task);
            } else if (isLaterThanDate(todayDate, task)) {
                break;
            }
        }
        return output;
    }

    private boolean hasSameDueOrStart(String dateStr, Task task) {
        return task.getStart().getDate().equals(dateStr) ||
                task.getDue().getDate().equals(dateStr);
    }

    private boolean isLaterThanDate(DateTime date, Task task) {
        return !task.getDue().isEarlierThan(date) &&
                   !task.getStart().isEarlierThan(date);
    }

    /**
     * This method fetches tasks that have start/due dates tomorrow
     * 
     * @return List{@literal<Task>} - Tasks which have start/due tomorrow
     */
    public List<Task> fetchTomorrowTasks() {
        log("Fetching Tomorrow Tasks");
        String tmrDateStr = Parser.getTmrDateStr();
        DateTime tmrDate = new DateTime(tmrDateStr, "2359");
        List<Task> output = new ArrayList<Task>();
        for (Task task : file.getAllTasks()) {
            if (hasSameDueOrStart(tmrDateStr, task)) {
                output.add(task);
            } else if (isLaterThanDate(tmrDate, task)) {
                break;
            }
        }
        return output;
    }

    /**
     * This method fetches tasks that have start/due dates within the next 2
     * weeks, but do not fall in today or tomorrow.
     * 
     * @return List{@literal<Task>} - Tasks which have start/due within the next
     *         2 weeks
     */
    public List<Task> fetchNextWeekTasks() {
        log("Fetching Next Week Tasks");
        // Anything that falls after tomorrow but is earlier than 15 days later
        // is "within the next 2 weeks"
        String tmrDate = Parser.getDateFromNowStr(2);
        DateTime tmr = new DateTime(tmrDate, "2359");
        String fifteenDaysLaterDate = Parser.getDateFromNowStr(15);
        DateTime fifteenDaysLater = new DateTime(fifteenDaysLaterDate, "0000");

        List<Task> output = new ArrayList<Task>();
        for (Task task : file.getAllTasks()) {
            DateTime currDue = task.getDue();
            DateTime currStart = task.getStart();
            if (dateIsBetween(tmr, fifteenDaysLater, currDue) ||
                dateIsBetween(tmr, fifteenDaysLater, currStart)) {
                output.add(task);
            }
        }
        return output;
    }

    /**
     * Returns true if input <code>date</code> is later than
     * <code>rangeStartEx</code> and is earlier than <code>rangeEndEx</code>.
     * <p>
     * <i>Note that the range is "exclusive"; dates equal to start/end DateTimes
     * are <u>not</u> considered "between" them.</i>
     */
    private boolean dateIsBetween(DateTime rangeStartEx, DateTime rangeEndEx,
                                  DateTime date) {
        return date.isLaterThan(rangeStartEx) && date.isEarlierThan(rangeEndEx);
    }

    protected DatabaseFacade getFile() {
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

    protected List<Task> fetchLastSearch() {
        return lastSearch;
    }

    protected void initialiseNewSearchList() {
        lastSearch = new ArrayList<Task>();
    }

    /*-- OBSOLETE CODE -- 
    private void updateUIPaneWindow() {
        updateFloatingAndTimedTasks();
        setChanged(); notifyObservers("updateui"); 
        if (LOGGING_ENABLED) {
            log.info("Updated side panel.");
        }
    }
    
    /*
     * This method fetches tasks that have due dates
     * 
     * @return List{@literal<Task>} - Tasks which contains time
     *
    public List<Task> fetchTimedTasks() {
        if (LOGGING_ENABLED) {
            log.info("Fetching Timed Tasks");
        }
        return Collections.unmodifiableList(timedTasks);
    }
     */
}