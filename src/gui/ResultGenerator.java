package gui;

import java.util.List;

import logic.Processor;
import logic.Result;
import database.BlockDate;
import database.Task;

public class ResultGenerator {

    public String sendInput(String input) {
        Processor processor = Processor.getInstance();
        Result result = processor.processInput(input);
        assert (result != null);
        return processResult(result, input);
    }

    public boolean processDeleteAll(String input) {
        input = input.toLowerCase();
        if (input.equals("y") || input.equals("yes")) {
            Processor.reset();
            return true;
        }

        return false;
    }

    public void updateInterface(List<Task> tasks) {
        new UpdateUI(tasks);
    }

    public String processResult(Result result, String input) {

        if (result.isSuccess()) {
            switch (result.getResultType()) {
                case BLOCKDATE:
                    return processDateBasedResult(result);
                case TASK:
                    return processTaskBasedResult(result);
                default:
                    // HELP -- open dialog; press key to close note: cmdType is
                    // Display
                    // ERROR?
            }

        }
        return String.format("Not able to process '%1$s'", input);
    }

    // Not implemented yet
    private String processDateBasedResult(Result result) {
        List<BlockDate> dates = result.getBlockedDates(); // get list of dates
        String message;
        assert (dates.size() != 0);
        if (dates.size() == 1) {
            message = feedbackSingleBlock(dates);
            checkCommandType(result);
            // change table?
            switch (result.getCommandType()) {
            // check for confirmation
                case BLOCK:
                    return "BLOCKED: " + message;// format: BLOCKED: DateTime
                case UNBLOCK:
                    return "UNBLOCKED: " + message;// format: UNBLOCKED:
                                                   // DateTime
                case REDO:
                    return "Command Redone.";

                case UNDO:
                    return "Command Undone.";
                case ERROR:
                    return "Not able to fully process command";

                default:
                    // this line of code should never be reached
                    throw new IllegalArgumentException("Illegal Command Type");
            }
        }

        return feedbackMessageMultiResults(dates, "Showing %1$s blocks.");
    }

    private String feedbackSingleBlock(List<BlockDate> dates) {
        BlockDate date = dates.get(0);
        return date.getStartDate().toString() + " to " +
               date.getEndDate().toString();
    }

    private String processTaskBasedResult(Result result) {
        List<Task> outputs = result.getTasks();

        checkCommandType(result);
        switch (result.getCommandType()) {
            case ADD:
                updateInterface(outputs);
                if (result.needsConfirmation()) {
                    return "Unable to add task. Task coincides with a blocked date. Key 'y' to override date or 'n' to abort";
                }
                return feedbackMessage(outputs, "Added %1$s");
            case DELETE:
                updateInterface(outputs);
                if (result.needsConfirmation()) {
                    return "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
                }
                return feedbackMessage(outputs, "Deleted %1$s");
            case EDIT:
                updateInterface(outputs);
                return feedbackMessage(outputs, "Edited %1$s");
            case DISPLAY:
                updateInterface(outputs);
                if (outputs.size() == 0) {
                    return "No tasks to show.";
                }
                return feedbackMessageMultiResults(outputs,
                                                   "%1$s task(s) found.");
            case SEARCH:
                updateInterface(outputs);
                return feedbackMessageMultiResults(outputs,
                                                   "Found %1$s match(es).");
            case TODO:
                updateInterface(outputs);
                return feedbackMessage(outputs, "Marked %1$s as todo.");
            case DONE:
                updateInterface(outputs);
                return feedbackMessage(outputs, "Marked %1$s as done.");
            case UNDO:
                updateInterface(outputs);
                return "Command Undone.";
            case REDO:
                updateInterface(outputs);
                return "Command Redone.";
            case EXIT:
                return "exit";
            case ERROR:
                return "Not able to fully process command.";
            default:
                // this line of code should never be reached
                throw new IllegalArgumentException("Illegal Command Type");
        }
    }

    private String feedbackMessage(List<Task> tasks, String commandDone) {
        assert (tasks.size() == 1);
        Task taskDone = tasks.get(0);
        checkValidName(taskDone);
        return String.format(commandDone, taskDone.getName());
    }

    private String feedbackMessageMultiResults(@SuppressWarnings("rawtypes") List outputs,
                                               String feedback) {
        int size = outputs.size();
        return String.format(feedback, size);
    }

    private void checkValidName(Task task) {
        if (isValidString(task.getName())) {
            throw new NullPointerException("Task name is invalid");
        }
    }

    private void checkCommandType(Result result) throws NullPointerException {
        if (result.getCommandType() == null) {
            throw new NullPointerException("CommandType is null");
        }
    }

    private boolean isValidString(String parameter) {
        return parameter == null || parameter.isEmpty() ||
               parameter.equals("null");
    }
}