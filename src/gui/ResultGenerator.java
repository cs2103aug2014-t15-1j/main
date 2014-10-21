package gui;

import java.util.List;

import logic.Processor;
import logic.Result;
import database.DateTime;
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

    public String processResult(Result result, String input) {

        if (result.isSuccess()) {
            switch (result.getResultType()) {
                case BLOCKDATE:
                    return processDateBasedResult(result);
                case TASK:
                    return processTaskBasedResult(result);
            }

        }
        return String.format("Not able to process '%1$s'", input);
    }

    // Not implemented yet
    private String processDateBasedResult(Result result) {
        List<DateTime> dates = null; // get list of dates
        String message;
        assert (dates.size() != 0);
        if (dates.size() == 1) {
            message = feedbackSingleBlock(dates);

            // change table?
            switch (result.getCommandType()) {
            // check for confirmation
                case BLOCK:
                    return "BLOCKED: " + message;// format: BLOCKED: DateTime
                case UNBLOCK:
                    return "UNBLOCKED: " + message;// format: UNBLOCKED:
                                                   // DateTime
            }
        }

        return feedbackMessageMultiResults(dates, "Showing %1$s blocks.");
    }

    private String feedbackSingleBlock(List<DateTime> dates) {
        assert (dates.size() == 1);
        DateTime date = dates.get(0);
        return date.toString();
    }

    private String processTaskBasedResult(Result result) {
        List<Task> outputs = result.getTasks();

        switch (result.getCommandType()) {
            case ADD:
                return feedbackMessage(outputs, "Added %1$s");
            case DELETE:
                if (result.needsConfirmation()) {
                    return "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
                }
                return "Deleted!";
            case EDIT:
                return feedbackMessage(outputs, "Edited %1$s");
            case DISPLAY:
                if (outputs.size() == 0) {
                    return "No tasks to show.";
                }
                return feedbackMessageMultiResults(outputs,
                                                   "%1$s task(s) found.");
            case SEARCH:
                return feedbackMessageMultiResults(outputs,
                                                   "Found %1$s match(es).");
            case TODO:
                return feedbackMessage(outputs, "Marked %1$s as todo.");
            case DONE:
                return feedbackMessage(outputs, "Marked %1$s as done.");
            case UNDO:
                return "Command Undone.";
            case REDO:
                return "Command Redone.";
            case EXIT:
                return "exit";
            default:
                return "";
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
            throw new NullPointerException("Task name is null");
        }
    }

    private boolean isValidString(String parameter) {
        return parameter.equals(null) || parameter.isEmpty() ||
               parameter.equals("null");
    }
}