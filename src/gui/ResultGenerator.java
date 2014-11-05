package gui;

import java.util.List;

import logic.CommandType;
import logic.Processor;
import logic.Result;
import database.BlockDate;
import database.Task;

public class ResultGenerator {

    private static Processor processor;
    private static TaskTableUI taskTable = TaskTableUI.getInstance();
    private static DateTableUI dateTable = DateTableUI.getInstance();
    private static ResultGenerator resultGen;

    public static ResultGenerator getInstance() {
        if (resultGen == null) {
            resultGen = new ResultGenerator();
        }
        return resultGen;
    }

    public void start() {
        processor = Processor.getInstance();
        UpcomingTaskList upcommingList = UpcomingTaskList.getInstance();
        processor.addObserver(upcommingList);
        upcommingList.initialise();
        FloatingTaskList floatingList = FloatingTaskList.getInstance();
        processor.addObserver(floatingList);
        floatingList.initialise();
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
            start();
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
        return date.getStartDate().toString() + " to " +
               date.getEndDate().toString();
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