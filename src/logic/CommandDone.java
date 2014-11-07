package logic;

import java.util.ArrayList;
import java.util.List;

import parser.DateParser;
import parser.TaskParam;
import database.DateTime;
import database.Task;

/**
 * This class extends abstract class Command CommandDone class is used to
 * complete operations related to the done/todo operations
 * 
 * @author Ter Yao Xiang
 *
 */
public class CommandDone extends Command {

    private static List<Task> lastTasksRange = new ArrayList<Task>();

    private String rangeType = "";

    private DateTime dateTime = new DateTime();

    private String id = "";

    private CommandTodo cmdTodo = null;

    public CommandDone(List<TaskParam> content) {
        this(content, false);
    }

    /**
     * This method constructs the CommandDone object
     * <p>
     * 
     * @param List
     *            {@literal<TaskParam>} content - Expected to contain TaskParam
     *            object <br>
     *            boolean isComplement - True if it is a child of another
     *            Command object
     */
    protected CommandDone(List<TaskParam> content, boolean isComplement) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for done";
        } else {
            this.type = CommandType.DONE;

            for (TaskParam param : content) {
                constructUsingParam(param);
            }
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
                ;
                assert (DateParser.isValidDate(param.getField())) : "Invalid date for done";
                this.dateTime = new DateTime(param.getField(), "");
                break;

            default:
                this.type = CommandType.ERROR;
                this.error = "Todo constructor parameter error";
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
     * Executes "done" operation
     * <p>
     * This is executed using the command:<br>
     * "done {@literal<id>}" or "done {@literal<date>}"
     * <p>
     * This method marks either one/several Tasks as 'done'
     * 
     * @return Result
     */
    @Override
    protected Result execute(boolean userInput) {
        if (Processor.LOGGING_ENABLED) {
            Processor.getLogger().info("Executing 'Done' Command...");
        }
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
                break;

        }

        return new Result(list, success, getType());
    }

    private boolean doneById(List<Task> list) {
        Processor processor = Processor.getInstance();
        boolean success = false;
        int taskId = Integer.parseInt(id);
        Task task = processor.getFile().getTask(taskId);
        if (task != null) {
            success = processor.getFile().markDone(task);
            if (success) {
                list.add(task);
            }
        }
        return success;
    }

    private boolean doneByDate(List<Task> list) {
        Processor processor = Processor.getInstance();
        boolean success = false;
        for (Task task : processor.getFile().getToDoTasks()) {
            if (task.getDue().getDate().equals(this.dateTime.getDate())) {
                list.add(task);
                success = true;
            }
        }
        for (Task task : list) {
            lastTasksRange.add(task);
            processor.getFile().markDone(task);
        }
        return success;
    }

    /**
     * Executes "todo" operation
     * <p>
     * Marks a 'done' task as 'todo'
     * 
     * @return Result
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
     * For "done {@literal<id>}", the list will contain only one Task.<br>
     * For "done {@literal<date>}", the list will contain only several Tasks.
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
