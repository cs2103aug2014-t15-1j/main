package gui;

import java.util.List;

import logic.CommandType;
import logic.Processor;
import logic.Result;
import database.BlockDate;
import database.Task;

/**
 * Processes the Result objects returned by logic.Processor to update the graphical user interface accordingly.
 */
public class ResultGenerator {

    private static Processor processor;
    private static TaskTableUI taskTable;
    private static DateTableUI dateTable;
    private static ResultGenerator resultGen;

    /**
     * Returns an instance of ResultGenerator. Creates an instance if it has not been created
     * @return instance of ResultGenerator
     */
    public static ResultGenerator getInstance() {
        if (resultGen == null) {
            resultGen = new ResultGenerator();
        }
        return resultGen;
    }
    
    /**
     * Initializes the rest of the application and adds the appropriate user interface to observe logic.Processor
     */
    public void start() {
        initialiseAppilcation();
        addObservers();
        refreshTodoTable();
    }
    
    /**
     * Processes the users input and returns the appropriate feedback  message to user
     * @param input the input entered by the user
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
        }

    }
    
    /**
     * Processes the confirmation input entered by the user. 
     * If input is 'y' or 'yes', all tasks will be deleted. Otherwise, nothing is deleted
     * @param input input entered by user
     * @return true if successfully deleted, false otherwise
     */
    public boolean processDeleteAll(String input) {
        input = input.toLowerCase();
        if (input.equals("y") || input.equals("yes")) {
            Processor.reset();
            start();
            refreshTodoTable();
            return true;
        }

        return false;
    }
    
    /**
     * Processes the Result Object and returns appropriate feedback messages about result object
     * @param result Contains the details if a command was carried out
     * @param input input entered by user
     * @return String containing the feedback messages about the result object
     */
    public String processResult(Result result, String input) {

        if (result.isSuccess()) {

            if (isHelp(result)) {
                return "Help";
            }

            switch (result.getResultType()) {
                case BLOCKDATE:
                    return processDateBasedResult(result);
                case TASK:
                    return processTaskBasedResult(result);
            }

        }
        return String.format("Not able to process '%1$s'", input);
    }
    
    private void addObservers() {
        UpcomingTaskList upcommingList = UpcomingTaskList.getInstance();
        processor.addObserver(upcommingList);
        upcommingList.initialise();
        FloatingTaskList floatingList = FloatingTaskList.getInstance();
        processor.addObserver(floatingList);
        floatingList.initialise();
        BlockLabelUI blockLabelUI = BlockLabelUI.getInstance();
        processor.addObserver(blockLabelUI);
        TimeUI timeLabel = TimeUI.getInstance();
        Processor.getInstance().addObserver(timeLabel);
    }

    private void initialiseAppilcation() {
        processor = Processor.getInstance();
        taskTable = TaskTableUI.getInstance();
       dateTable = DateTableUI.getInstance();
    }
    
    private boolean isHelp(Result result) {
        if (result.getResultType() == null &&
            result.getCommandType().equals(CommandType.HELP)) {
            return true;
        }

        return false;
    }

    private String processDateBasedResult(Result result) {
        List<BlockDate> dates = result.getBlockedDates(); // get list of dates
        if (dates.size() == 0) {
            refreshBlockTable();
            return "No dates have been blocked";
        }

        assert (result.getCommandType() != null);

        switch (result.getCommandType()) {
            case BLOCK:
                if (result.needsConfirmation()) {
                    return "Unable to block date. Date coincides with another blocked date or task.";
                }
                refreshBlockTable();
                setDateTableSelection(dates);
                return "BLOCKED: " + feedbackSingleBlock(dates);

            case UNBLOCK:
                refreshBlockTable();
                setDateTableSelection(dates);
                return "UNBLOCKED: " + feedbackSingleBlock(dates);

            case REDO:
                if (result.needsConfirmation()) {
                    return "Unable to redo command. Date coincides with another blocked date or task.";
                }
                refreshBlockTable();
                setDateTableSelection(dates);
                return "Command Redone.";

            case UNDO:
                refreshBlockTable();
                setDateTableSelection(dates);
                return "Command Undone.";

            case DISPLAY:
                refreshBlockTable();
                return feedbackMessageMultiResults(dates,
                                                   "Showing %1$s blocks.");
            case SEARCH:
                dateTable.update(dates);
                return feedbackMessageMultiResults(dates, "Found %1$s results.");
            case ERROR:
                return "Not able to fully process command";

            default:
                // this line of code should never be reached
                throw new IllegalArgumentException("Illegal Command Type");
        }

    }

    private String feedbackSingleBlock(List<BlockDate> dates) {
        BlockDate date = dates.get(0);
        if(date.getStartTime() == null || date.getEndTime() == null){
            return date.getStartDate().toString() + " to " +
                   date.getEndDate().toString();
        } else{
            return date.getStartDate().toString() + " " + date.getStartTime()+ " to " +
                    date.getEndDate().toString() + " " + date.getEndTime();
        }
    }

    private String processTaskBasedResult(Result result) {
        List<Task> outputs = result.getTasks();

        assert (result.getCommandType() != null);

        switch (result.getCommandType()) {
            case ADD:
                refreshTodoTable();
                setTaskTableSelection(outputs);
                if (result.needsConfirmation()) {
                    return "Unable to add task. Task coincides with a blocked date.";
                }
                return feedbackMessage(outputs, "Added %1$s");
                
            case DELETE:
                if (result.needsConfirmation()) {
                    return "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
                }
                refreshTodoTable();
                setTaskTableSelection(outputs);
                return feedbackMessage(outputs, "Deleted %1$s");
                
            case RESET:
                if (result.needsConfirmation()) {
                    return "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
                }
                refreshTodoTable();
                return feedbackMessage(outputs, "Deleted %1$s");
                
            case EDIT:
                refreshTodoTable();
                setTaskTableSelection(outputs);
                return feedbackMessage(outputs, "Edited %1$s");
            case DISPLAY:
                if (outputs.size() == 0) {

                    return "No tasks to show.";
                } else if (outputs.size() == 1) {
                    setTaskTableSelection(outputs);
                }
                taskTable.update(outputs);
                return feedbackMessageMultiResults(outputs,
                                                   "%1$s task(s) found.");
            case SEARCH:
                taskTable.update(outputs);
                return feedbackMessageMultiResults(outputs,
                                                   "Found %1$s match(es).");
            case TODO:
                refreshTodoTable();
                setTaskTableSelection(outputs);
                return feedbackMessage(outputs, "Marked %1$s as todo.");
            case DONE:
                refreshTodoTable();
                setTaskTableSelection(outputs);
                return feedbackMessage(outputs, "Marked %1$s as done.");
            case UNDO:
                refreshTodoTable();
                setTaskTableSelection(outputs);
                return "Command Undone.";
            case REDO:
                refreshTodoTable();
                setTaskTableSelection(outputs);
                return "Command Redone.";
            case RESTORE:
                refreshTodoTable();
                setTaskTableSelection(outputs);
                return feedbackMessage(outputs, "Restored %1$s.");
            case EXIT:
                return "exit";
            case ERROR:
                return "The command format is wrong. Type help for command format";
            default:
                // this line of code should never be reached
                throw new IllegalArgumentException("Illegal Command Type");
        }
    }

    private String feedbackMessage(List<Task> tasks, String commandDone) {
        assert (tasks.size() == 1);
        Task taskDone = tasks.get(0);
        if (taskDone.getName() == null || taskDone.getName().isEmpty()) {
            return String.format(commandDone, "empty task");
        }
        checkValidName(taskDone);
        return String.format(commandDone, taskDone.getName());
    }

    @SuppressWarnings("rawtypes")
    private String feedbackMessageMultiResults(List outputs, String feedback) {
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

    private void refreshTodoTable() {
        List<Task> tasks = processor.fetchToDoTasks();
        taskTable.update(tasks);
    }

    private void refreshBlockTable() {
        List<BlockDate> dates = processor.fetchBlockedDate();
        dateTable.update(dates);
    }

    private void setTaskTableSelection(List<Task> outputs) {
        List<Task> tasks = processor.fetchToDoTasks();
        taskTable.setTableSection(outputs.get(0), tasks);
    }

    private void setDateTableSelection(List<BlockDate> outputs) {
        List<BlockDate> dates = processor.fetchBlockedDate();
        dateTable.setTableSection(outputs.get(0), dates);
    }
}