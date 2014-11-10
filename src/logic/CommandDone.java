package logic;

import java.util.ArrayList;
import java.util.List;

import objects.DateTime;
import objects.Task;
import objects.TaskParam;
import parser.Parser;

/**
 * This class extends abstract class Command. CommandDone class is associated
 * with operations related to the done/todo operations
 * 
 * @author A0110751W
 *
 */
public class CommandDone extends Command {

    private static List<Task> lastTasksRange = new ArrayList<Task>();

    private String rangeType = "";

    private String id = "";

    private DateTime dateTime = new DateTime();

    /* Complement Command object of this Command Object */
    private CommandTodo cmdTodo = null;

    public CommandDone(List<TaskParam> content) {
        this(content, false);
    }

    protected CommandDone(List<TaskParam> content, boolean isComplement) {
        assert content != null : "Constructor param is null";
        assert !content.isEmpty() : "Constructor param is empty";
        
        this.type = CommandType.DONE;

        for (TaskParam param : content) {
            constructUsingParam(param);
        }
        initialiseComplementCommand(content);
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
                assert false : "Invalid input - Received: " + param.getName();
        }
    }

    /**
     * This method initialises the CommandTodo object.
     * <p>
     * This object is used when performing when performing complement
     * operations.
     * 
     * @param content
     *            - Contains a list of TaskParam objects similar to it's parent
     *            Command
     */
    private void initialiseComplementCommand(List<TaskParam> content) {
        this.cmdTodo = new CommandTodo(content, true);
    }

    /**
     * This method executes the "done" operation
     * <p>
     * This is executed using the command:<br>
     * <i>done {@literal<id>}</i> or <i>done {@literal<date>}</i>
     * <p>
     * This method marks either one/several Tasks as "done"
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean, String)
     *         Result}
     */
    @Override
    protected Result execute(boolean userInput) {
        Processor.log("Executing 'Done' Command...");
        List<Task> list = new ArrayList<Task>();
        boolean success = false;

        switch (rangeType) {
            case RANGE_TYPE_ID:
                success = doneById(list);
                break;

            case RANGE_TYPE_DATE:
                success = doneByDate(list);
                break;

            default:
                assert false : "Invalid input - Received: " + rangeType;

        }

        return new Result(list, success, getType(), DISPLAY_TAB_NO_CHANGE);
    }

    private boolean doneById(List<Task> list) {
        Processor processor = Processor.getInstance();
        int taskId = Integer.parseInt(id);
        Task task = processor.getFile().getTask(taskId);
        boolean success = processor.getFile().markDone(task);
        if (success) {
            list.add(task);
        }
        return success;
    }

    private boolean doneByDate(List<Task> list) {
        boolean success = false;
        selectTasks(list);
        if (list.size() > 0) {
            success = true;
        }
        markSelectedTasksAsDone(list);
        return success;
    }

    private void selectTasks(List<Task> list) {
        Processor processor = Processor.getInstance();
        for (Task task : processor.getFile().getToDoTasks()) {
            if (task.getDue().getDate().equals(this.dateTime.getDate())) {
                list.add(task);
            }
        }
    }

    private void markSelectedTasksAsDone(List<Task> list) {
        Processor processor = Processor.getInstance();
        for (Task task : list) {
            lastTasksRange.add(task);
            processor.getFile().markDone(task);
        }
    }

    /**
     * This method executes the "todo" operation
     * <p>
     * Marks a "done" task as "todo"
     * <p>
     * Refer to {@link logic.CommandTodo#execute(boolean)
     * CommandTodo.execute(boolean)}
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean)
     *         Result}
     */
    @Override
    protected Result executeComplement() {
        Result result = this.cmdTodo.execute(false);
        if (result.isSuccess()) {
            lastTasksRange.clear();
        }
        return result;
    }

    /**
     * This method fetches a list of Tasks which are done in the last 'done'
     * operation
     * <p>
     * For <i>done {@literal<id>}</i>, the list will contain only one Task.<br>
     * For <i>done {@literal<date>}</i>, the list will contain only several
     * Tasks.
     * 
     * @return List{@literal<Task>}
     */
    protected static List<Task> fetchLastDoneTasks() {
        return lastTasksRange;
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

    @Override
    public String toString() {
        return "cmddone rangetype: " + this.rangeType + " id: " + this.id +
               " date: " + this.dateTime.toString();
    }
}
