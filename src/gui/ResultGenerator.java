package gui;

import java.util.List;

import logic.CommandType;
import logic.Processor;
import logic.Result;
import database.BlockDate;
import database.Task;

public class ResultGenerator {

    private static Processor processor;
    private static TaskTableUI taskTable = new TaskTableUI();
    private static DateTableUI dateTable = new DateTableUI();

    public void start() {
        processor = Processor.getInstance();
        processor.addObserver(new TaskListUI());
        refreshTodoTable();
    }

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

    public boolean processDeleteAll(String input) {
        input = input.toLowerCase();
        if (input.equals("y") || input.equals("yes")) {
            Processor.reset();
            refreshTodoTable();
            return true;
        }

        return false;
    }

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
                default:
                    //
                    // Display
                    // ERROR?
            }

        }
        return String.format("Not able to process '%1$s'", input);
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
            return "No dates have been blocked";
        }

        feedbackSingleBlock(dates);
        assert (result.getCommandType() != null);

        switch (result.getCommandType()) {
        // check for confirmation
            case BLOCK:
                if (result.needsConfirmation()) {
                    return "Unable to block date. Date coincides with another blocked date or task.";
                }
                refreshBlockTable();
                return "BLOCKED: " + feedbackSingleBlock(dates);
            case UNBLOCK:
                refreshBlockTable();
                return "UNBLOCKED: " + feedbackSingleBlock(dates);

            case REDO:
                refreshBlockTable();
                return "Command Redone.";

            case UNDO:
                refreshBlockTable();
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
        return date.getStartDate().toString() + " to " +
               date.getEndDate().toString();
    }

    private String processTaskBasedResult(Result result) {
        List<Task> outputs = result.getTasks();

        assert (result.getCommandType() != null);

        switch (result.getCommandType()) {
            case ADD:
                refreshTodoTable();
                if (result.needsConfirmation()) {
                    return "Unable to add task. Task coincides with a blocked date.";
                }
                return feedbackMessage(outputs, "Added %1$s");
            case DELETE:
                refreshTodoTable();
                if (result.needsConfirmation()) {
                    return "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
                }
                return feedbackMessage(outputs, "Deleted %1$s");
            case EDIT:
                refreshTodoTable();
                return feedbackMessage(outputs, "Edited %1$s");
            case DISPLAY:

                if (outputs.size() == 0) {
                    return "No tasks to show.";
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
                return feedbackMessage(outputs, "Marked %1$s as todo.");
            case DONE:
                refreshTodoTable();
                return feedbackMessage(outputs, "Marked %1$s as done.");
            case UNDO:
                refreshTodoTable();
                return "Command Undone.";
            case REDO:
                refreshTodoTable();
                return "Command Redone.";
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
        if (dates.size() == 0) {
            return;
        }
        dateTable.update(dates);
    }
}