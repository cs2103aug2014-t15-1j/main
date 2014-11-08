package logic;

import java.util.ArrayList;
import java.util.List;

import parser.Parser;
import parser.objects.TaskParam;
import database.DateTime;
import database.Task;
import database.TaskType;

public class CommandBlock extends Command {

    private String name = "";
    private DateTime from = new DateTime();
    private DateTime to = new DateTime();
    private List<String> tags = new ArrayList<String>();

    public CommandBlock(List<TaskParam> content) {
        this(content, false);
    }

    protected CommandBlock(List<TaskParam> content, boolean isComplement) {
        assert (content != null);
        assert (!content.isEmpty());

        this.type = CommandType.BLOCK;

        for (TaskParam param : content) {
            constructUsingParam(param);
        }
    }

    private void constructUsingParam(TaskParam param) {
        switch (param.getName()) {
            case PARAM_NAME:
                this.name = name.concat(" " + param.getField()).trim();
                break;

            case PARAM_FROM:
                this.from = Parser.parseToDateTime(param.getField());
                break;

            case PARAM_TO:
                this.to = Parser.parseToDateTime(param.getField());
                break;

            case PARAM_TAG:
                this.tags.add(param.getField());
                break;

            default:
                assert false : "Invalid constructor param - Received: " +
                               param.getName();
        }
    }

    /**
     * This method executes the Block Command. It adds a <code>Task</code> with
     * <code>TaskType.BLOCK</code>. This <code>Task</code> is intended to
     * reserved the specified date range for the user and prevents the user to
     * add any other Task to the same date range.
     * 
     * @param userInput
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean)
     *         Result}
     */
    @Override
    protected Result execute(boolean userInput) {
        if (Processor.LOGGING_ENABLED) {
            Processor.getLogger().info("Executing 'Block' Command...");
        }
        Processor processor = Processor.getInstance();
        List<Task> blockRange = processor.fetchBlockTasks();
        boolean success = true;

        List<Task> outputs = new ArrayList<Task>();

        for (Task blockedDate : blockRange) {
            if (from.compareTo(blockedDate.getStart()) <= 0 &&
                to.compareTo(blockedDate.getDue()) >= 0) {
                success = false;
                outputs.add(blockedDate);
                break;
            } else if (blockedDate.getStart().compareTo(from) <= 0 &&
                       blockedDate.getDue().compareTo(to) >= 0) {
                success = false;
                outputs.add(blockedDate);
                break;
            }
        }

        String displayTab = "";
        if (success) {
            Task currBlock = new Task(name, from, to, new DateTime(), tags,
                    TaskType.BLOCK);
            processor.getFile().add(currBlock);
            outputs.add(currBlock);
            displayTab = getDisplayTab(currBlock);
        } else {
            throw new Error("This Task overlaps with another Block Task");
        }

        return new Result(outputs, success, CommandType.BLOCK, displayTab);
    }

    /**
     * This method executes the complement operation for 'block' of a
     * <code>Task</code>.<br> It deletes the last added <code>Block Task</code>.
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean)
     *         Result}
     */
    @Override
    protected Result executeComplement() {
        Processor processor = Processor.getInstance();
        List<Task> tasks = new ArrayList<Task>();
        boolean success = false;

        int taskId = processor.fetchBlockTasks().size() - 1;
        Task toDelete = processor.fetchBlockTasks().get(taskId);

        success = processor.getFile().permanentlyDelete(toDelete);

        if (success) {
            tasks.add(toDelete);
        }

        String displayTab = getDisplayTab(toDelete);

        return new Result(tasks, success, getType(), displayTab);
    }

    @Override
    public String get(String field) {
        switch (field) {
            case PARAM_FROM:
                return this.from.toString();

            case PARAM_TO:
                return this.to.toString();

            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "cmdblock name: " + this.name + " from: " + this.from + " to: " +
               this.to + " tags: " + this.tags;
    }

}
