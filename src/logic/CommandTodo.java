package logic;

import java.util.ArrayList;
import java.util.List;

import objects.DateTime;
import objects.Task;
import parser.Parser;
import parser.objects.TaskParam;

/**
 * This class extends abstract class Command. CommandTodo class is associated
 * with operations related to the done/todo operations
 * 
 * @author A0110751W
 *
 */
public class CommandTodo extends Command {

    private String rangeType = "";

    private String id = "";

    private DateTime dateTime = new DateTime();

    /* Complement Command object of this Command Object */
    private CommandDone cmdDone = null;

    public CommandTodo(List<TaskParam> content) {
        this(content, false);
    }

    protected CommandTodo(List<TaskParam> content, boolean isComplement) {
        assert content != null : "Constructor param is null";
        assert !content.isEmpty() : "Constructor param is empty";
        
        this.type = CommandType.TODO;

        for (TaskParam param : content) {
            constructUsingParam(param);
        }
        if (!isComplement) {
            initialiseComplementCommand(content);
        }
    }

    private void constructUsingParam(TaskParam param) {
        switch (param.getName()) {
            case PARAM_RANGE_TYPE:
                this.rangeType = param.getField();
                break;

            case PARAM_ID:
                this.id = param.getField();
                break;

            case PARAM_DATE:
                assert (Parser.isValidDate(param.getField())) : "Invalid date for done";
                this.dateTime = new DateTime(param.getField(), "");
                break;

            default:
                assert false : "Invalid constructor param - Received: " +
                               param.getName();
        }
    }

    private void initialiseComplementCommand(List<TaskParam> content) {
        this.cmdDone = new CommandDone(content, true);
    }

    @Override
    public String get(String field) {
        switch (field) {
            case PARAM_RANGE_TYPE:
                return this.rangeType;

            case PARAM_ID:
                return this.id;

            case PARAM_DATE:
                return this.dateTime.toString();

            default:
                return null;
        }
    }

    /**
     * This method executes the "todo" operation.
     * <p>
     * Marks a 'done' task as 'todo' or marks a range of tasks in the same
     * 'done' date as 'todo'.
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean)
     *         Result}
     */
    @Override
    protected Result execute(boolean userInput) {
        Processor.log("Executing 'Todo' Command...");

        Result result = new Result(null, false, getType(),
                DISPLAY_TAB_NO_CHANGE);
        switch (rangeType) {
            case RANGE_TYPE_ID:
                result = todoById();
                break;

            case RANGE_TYPE_DATE:
                if (!userInput) {
                    result = todoByDate();
                }
                break;

            default:
                assert false : "Invalid rangeType - Received: " + rangeType;
        }
        return result;
    }

    private Result todoById() {
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        try {
            success = markTaskAsTodo(list);
        } catch (NumberFormatException e) {
            if (Processor.LOGGING_ENABLED) {
                Processor.getLogger().warning("Error parsing Integer!");
            }
        }

        return new Result(list, success, getType(), DISPLAY_TAB_NO_CHANGE);
    }

    private boolean markTaskAsTodo(List<Task> list) {
        Processor processor = Processor.getInstance();
        int taskId = Integer.parseInt(id);
        Task task = processor.getFile().getTask(taskId);
        boolean success = processor.getFile().markToDo(task);
        if (success) {
            list.add(task);
        }
        return success;
    }

    private Result todoByDate() {
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        for (Task task : CommandDone.fetchLastDoneTasks()) {
            success = processor.getFile().markToDo(task);
            if (success) {
                list.add(task);
            }
        }
        return new Result(list, success, getType(), DISPLAY_TAB_NO_CHANGE);
    }

    /**
     * This method executes the "done" operation
     * <p>
     * Refer to {@link logic.CommandDone#execute(boolean)
     * CommandDone.execute(boolean)}
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean, String)
     *         Result}
     */
    @Override
    protected Result executeComplement() {
        return this.cmdDone.execute(false);
    }

    @Override
    public String toString() {
        return "cmdtodo rangeType: " + this.rangeType + " id: " + this.id +
               " date: " + this.dateTime;
    }
}
