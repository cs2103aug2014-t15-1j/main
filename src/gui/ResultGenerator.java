package gui;

import java.util.Calendar;
import java.util.List;

import logic.CommandType;
import logic.Processor;
import logic.Result;
import database.Task;

/**
 * Processes the Result objects returned by logic.Processor to update the graphical user interface accordingly.
 */
public class ResultGenerator {

    private static Processor processor;
    private static TableManagement tableManagement = new TableManagement();
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
        refreshTables();
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
            refreshTables();
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

           return processTaskBasedResult(result);

        }
        return String.format("Not able to process '%1$s'", input);
    }
    
    /**
     * Returns a previous input entered by user. Traverses up input history
     */
    public static String getUpKeyInput(){
        return processor.fetchInputUpKey();
    }
    
    /**
     * Returns a previous input entered by user. Traverses down input history
     */
    public static String getDownKeyInput(){
        return processor.fetchInputDownKey();
    }
    
    public static List<Task> getTimedTasks(){
        return processor.fetchTimedTasks();
    }
    
    public static List<Task> getToDoTasks(){
        System.out.println("refreshing table at get ToDo" + Calendar.getInstance().getTime().toString());
        return processor.fetchToDoTasks();
    }
    
    public static List<Task> getFloatingTasks(){
        return processor.fetchFloatingTasks();
    }
    
    public static List<Task> getDoneTasks(){
        return processor.fetchDoneTasks();
    }
    
    public static List<Task> getBlockTasks(){
        return processor.fetchBlockTasks();
    }

    private void initialiseAppilcation() {
        processor = Processor.getInstance();
    }

    private String feedbackSingleBlock(List<Task> dates) {
        Task date = dates.get(0);
        return date.getStart().toString() + " to " +
               date.getDue().toString();
    }

    private String processTaskBasedResult(Result result) {
        List<Task> outputs = result.getTasks();

        assert (result.getCommandType() != null);

        switch (result.getCommandType()) {
            case ADD:
                refreshTables();
                setTableSelection(outputs);
                if (result.needsConfirmation()) {
                    return "Unable to add task. Task coincides with a blocked date.";
                }
                return feedbackMessage(outputs, "Added %1$s");
                
            case DELETE:
                if (result.needsConfirmation()) {
                    return "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
                }
                refreshTables();
                setTableSelection(outputs);
                return feedbackMessage(outputs, "Deleted %1$s");
                
            case BLOCK:
                if (result.needsConfirmation()) {
                    return "Unable to block date. Date coincides with another blocked date or task.";
                }
                refreshTables();
                setTableSelection(outputs);
                return "BLOCKED: " + feedbackSingleBlock(outputs);

            case UNBLOCK:
                refreshTables();
                setTableSelection(outputs);
                return "UNBLOCKED: " + feedbackSingleBlock(outputs);

            case RESET:
                if (result.needsConfirmation()) {
                    return "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
                }
                refreshTables();
                return feedbackMessage(outputs, "Deleted %1$s");
                
            case EDIT:
                refreshTables();
                setTableSelection(outputs);
                return feedbackMessage(outputs, "Edited %1$s");
            case DISPLAY:
                if (outputs.size() == 0) {
                    return "Nothing to show.";
                } else if (outputs.size() == 1) {
                    setTableSelection(outputs);
                }
                updateTables(outputs);
                return feedbackMessageMultiResults(outputs,
                                                   "%1$s task(s) found.");
            case SEARCH:
                updateSearchTable(outputs);
                return feedbackMessageMultiResults(outputs,
                                                   "Found %1$s match(es).");
            case TODO:
                refreshTables();
                setTableSelection(outputs);
                return feedbackMessage(outputs, "Marked %1$s as todo.");
            case DONE:
                refreshTables();
                setTableSelection(outputs);
                return feedbackMessage(outputs, "Marked %1$s as done.");
            case UNDO:
                refreshTables();
                setTableSelection(outputs);
                return "Command Undone.";
            case REDO:
                if (result.needsConfirmation()) {
                    return "Unable to redo command. Date coincides with another blocked date or task.";
                }
                refreshTables();
                setTableSelection(outputs);
                return "Command Redone.";
            case RESTORE:
                refreshTables();
                setTableSelection(outputs);
                return feedbackMessage(outputs, "Restored %1$s.");
            case HELP:
                return "Help";
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

    private String feedbackMessageMultiResults(List<Task> outputs, String feedback) {
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

    private void refreshTables() {
        List<Task> toDo = processor.fetchToDoTasks();
        List<Task> floating = processor.fetchFloatingTasks();
        List<Task> done = processor.fetchDoneTasks();
        List<Task> blocked = processor.fetchBlockTasks();
        List<Task> timed = processor.fetchTimedTasks();
        tableManagement.updateToDoTable(toDo);
        tableManagement.updateFloatingTaskTable(floating);
        tableManagement.updateDoneTable(done);
        tableManagement.updateBlockTable(blocked);
        tableManagement.updateTimedTable(timed);
    }
    
    private void updateTables(List<Task> tasks){
        tableManagement.updateTable(tasks);
    }
    
    private void updateSearchTable(List<Task> tasks){
        tableManagement.updateSearchTable(tasks);
    }
    
    private void setTableSelection(List<Task> outputs) {
        tableManagement.setTableSelection(outputs.get(0));
    }

}