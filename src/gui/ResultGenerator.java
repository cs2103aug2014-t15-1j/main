package gui;

import java.util.List;

import logic.Processor;
import logic.Result;
import database.Task;

/**
 * Processes the Result objects returned by logic.Processor to update the
 * graphical user interface accordingly.
 */
public class ResultGenerator {
    private static final String NO_NAME = "empty task";
    private static final String CODE_HELP = "Help";
    private static final String CODE_EXIT = "exit";
    
    private static final String FEEDBACK_ADD = "Added %1$s";
    private static final String FEEDBACK_BLOCK = "BLOCKED: %1$s to %2$s";
    private static final String FEEDBACK_UNBLOCK = "UNBLOCKED: %1$s to %2$s";
    private static final String FEEDBACK_DELETE = "Deleted %1$s";
    private static final String FEEDBACK_EDIT = "Edited %1$s";
    private static final String FEEDBACK_DISPLAY = "%1$s task(s) found.";
    private static final String FEEDBACK_SEARCH = "Found %1$s match(es).";
    private static final String FEEDBACK_TODO = "Marked %1$s as todo.";
    private static final String FEEDBACK_DONE = "Marked %1$s as done.";
    private static final String FEEDBACK_UNDO = "Command Undone.";
    private static final String FEEDBACK_REDO = "Command Redone.";
    private static final String FEEDBACK_RESTORE = "Restored %1$s.";
    private static final String FEEDBACK_UNSUCESSFUL = "Not able to process '%1$s'";
    
    private static final String FEEDBACK_DISPLAY_NO_TASKS = "No tasks to show.";
    private static final String FEEDBACK_SEARCH_NO_TASKS = "No matches found.";
    private static final String FEEDBACK_ADD_CONFRIMATION = "Unable to add task. Task coincides with a blocked date.";
    private static final String FEEDBACK_DELETE_CONFRIMATION = "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
    private static final String FEEDBACK_BLOCK_CONFRIMATION = "Unable to block date. Date coincides with another blocked date or task.";
    private static final String FEEDBACK_REDO_CONFRIMATION = "Unable to redo command. Date coincides with another blocked date or task.";
    
    private static Processor processor;
    private static TableManagement tableManagement = new TableManagement();
    private static ResultGenerator resultGen;

    /**
     * Returns an instance of ResultGenerator. Creates an instance if it has not
     * been created
     * 
     * @return instance of ResultGenerator
     */
    public static ResultGenerator getInstance() {
        if (resultGen == null) {
            resultGen = new ResultGenerator();
        }
        return resultGen;
    }

    /**
     * Initializes the rest of the application and adds the appropriate user
     * interface to observe logic.Processor
     */
    public void start() {
        initialiseAppilcation();
        refreshTables();
    }

    /**
     * Processes the users input and returns the appropriate feedback message to
     * user
     * 
     * @param input
     *            the input entered by the user
     * @return feedback messages to be displayed to the user
     */
    public String sendInput(String input) {
        if (input.trim().isEmpty()) {
            return "";
        }

        Result result = null;
        try {
            result = processor.processInput(input);
            assert (result != null);
            return processResult(result, input);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (Error e) {
            return e.getMessage();
        }

    }

    /**
     * Processes the confirmation input entered by the user. If input is 'y' or
     * 'yes', all tasks will be deleted. Otherwise, nothing is deleted
     * 
     * @param input
     *            input entered by user
     * @return true if successfully deleted, false otherwise
     */
    public boolean processDeleteAll(String input) {
        input = input.toLowerCase();
        if (input.equals("y") || input.equals("yes")) {
            Processor.reset();
            start();
            refreshTables();
            return true;
        }

        return false;
    }

    /**
     * Processes the Result Object and returns appropriate feedback messages
     * about result object
     * 
     * @param result
     *            Contains the details if a command was carried out
     * @param input
     *            input entered by user
     * @return String containing the feedback messages about the result object
     */
    public String processResult(Result result, String input) {

        if (result.isSuccess()) {

            return processTaskBasedResult(result);

        }
        return String.format(FEEDBACK_UNSUCESSFUL, input);
    }

    /**
     * Returns a previous input entered by user. Traverses up input history
     */
    public static String getUpKeyInput() {
        return processor.fetchPreviousCommand();
    }

    /**
     * Returns a previous input entered by user. Traverses down input history
     */
    public static String getDownKeyInput() {
        return processor.fetchNextCommand();
    }

    public static List<Task> getAllTasks() {
        List<Task> all = processor.fetchAllTasks();
        return all;
    }

    public static List<Task> getTodayTasks() {
        return processor.fetchTodayTasks();
    }

    public static List<Task> getTomorrowsTasks() {
        return processor.fetchTomorrowTasks();
    }

    public static List<Task> getUpcomingTasks() {
        return processor.fetchNextWeekTasks();
    }

    public static List<Task> getFloatingTasks() {
        return processor.fetchFloatingTasks();
    }

    private void initialiseAppilcation() {
        processor = Processor.getInstance();
    }

    private String feedbackSingleBlock(List<Task> dates, String commandDone) {
        Task date = dates.get(0);
        return String.format(commandDone, date.getStart().toString(), date
                .getDue().toString());
    }

    private String processTaskBasedResult(Result result) {
        List<Task> outputs = result.getTasks();
        String tabName = result.getDisplayTab();

        assert (result.getCommandType() != null);

        switch (result.getCommandType()) {
            case ADD:
                refreshTables();
                setTableSelectionSingleTask(tabName, outputs);
                if (result.needsConfirmation()) {
                    return FEEDBACK_ADD_CONFRIMATION;
                }
                return feedbackMessage(outputs, FEEDBACK_ADD);

            case DELETE:
                if (result.needsConfirmation()) {
                    return FEEDBACK_DELETE_CONFRIMATION;
                }
                refreshTables();
                setTableSelectionSingleTask(tabName, outputs);
                return feedbackMessage(outputs, FEEDBACK_DELETE);

            case BLOCK:
                if (result.needsConfirmation()) {
                    return FEEDBACK_BLOCK_CONFRIMATION;
                }
                refreshTables();
                setTableSelectionSingleTask(tabName, outputs);
                return feedbackSingleBlock(outputs, FEEDBACK_BLOCK);

            case UNBLOCK:
                refreshTables();
                setTableSelectionSingleTask(tabName, outputs);
                return feedbackSingleBlock(outputs, FEEDBACK_UNBLOCK);

            case RESET:
                if (result.needsConfirmation()) {
                    return FEEDBACK_DELETE_CONFRIMATION;
                }
                refreshTables();
                return FEEDBACK_DELETE_CONFRIMATION;

            case EDIT:
                refreshTables();
                setTableSelectionSingleTask(tabName, outputs);
                return feedbackMessage(outputs, FEEDBACK_EDIT);
            case DISPLAY:
                if (outputs.size() == 0) {
                    setTableSelectionByName(tabName);
                    return FEEDBACK_DISPLAY_NO_TASKS;
                } else if (outputs.size() == 1) {
                    setTableSelectionSingleTask(tabName, outputs);
                }
                processDisplay(result);
                return feedbackMessageMultiResults(outputs, FEEDBACK_DISPLAY);
            case SEARCH:
                if (outputs.size() == 0) {
                    setTableSelectionByName(tabName);
                    return FEEDBACK_SEARCH_NO_TASKS;
                } else if (outputs.size() == 1) {
                    setTableSelectionSingleTask(tabName, outputs);
                }
                processDisplay(result);
                return feedbackMessageMultiResults(outputs, FEEDBACK_SEARCH);
            case TODO:
                refreshTables();
                setTableSelectionSingleTask(tabName, outputs);
                return feedbackMessage(outputs, FEEDBACK_TODO);
            case DONE:
                refreshTables();
                setTableSelectionSingleTask(tabName, outputs);
                return feedbackMessage(outputs, FEEDBACK_DONE);
            case UNDO:
                refreshTables();
                setTableSelectionSingleTask(tabName, outputs);
                return FEEDBACK_UNDO;
            case REDO:
                if (result.needsConfirmation()) {
                    setTableSelectionSingleTask(tabName, outputs);
                    return FEEDBACK_REDO_CONFRIMATION;
                }
                refreshTables();
                setTableSelectionSingleTask(tabName, outputs);
                return FEEDBACK_REDO;
            case RESTORE:
                refreshTables();
                setTableSelectionSingleTask(tabName, outputs);
                return feedbackMessage(outputs, FEEDBACK_RESTORE);
            case HELP:
                return CODE_HELP;
            case EXIT:
                return CODE_EXIT;
            default:
                throw new IllegalArgumentException("Illegal Command Type");
        }
    }

    private String feedbackMessage(List<Task> tasks, String commandDone) {
        assert (tasks.size() == 1);
        Task taskDone = tasks.get(0);
        if (taskDone.getName() == null || taskDone.getName().isEmpty()) {
            return String.format(commandDone, NO_NAME);
        }
        checkValidName(taskDone);
        return String.format(commandDone, taskDone.getName());
    }

    private String feedbackMessageMultiResults(List<Task> outputs,
                                               String feedback) {
        int size = outputs.size();
        return String.format(feedback, size);
    }

    private boolean checkValidName(Task task) {
        if (isValidString(task.getName())) {
            return false;
        }

        return true;
    }

    private boolean isValidString(String parameter) {
        return parameter == null || parameter.isEmpty() ||
               parameter.equals("null");
    }

    private void processDisplay(Result result) {
        refreshTables();
        String tabToSelect = result.getDisplayTab();
        List<Task> outputs = result.getTasks();
        tableManagement.updateTableByName(tabToSelect, outputs);
    }

    private void refreshTables() {
        tableManagement.refreshTables();
       
    }

    private void setTableSelectionByName(String tabName) {
        tableManagement.setTableSelectionByName(tabName);
    }

    private void setTableSelectionSingleTask(String tabName, List<Task> outputs) {
        if (outputs == null || outputs.isEmpty()) {
            tableManagement.setTableSelectionByName(tabName);
            return;
        }
        tableManagement.setTableSelectionByTask(outputs.get(0), tabName);
    }
}