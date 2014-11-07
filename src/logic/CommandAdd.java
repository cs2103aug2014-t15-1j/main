package logic;

import java.util.List;
import java.util.ArrayList;

import parser.DateParser;
import parser.TaskParam;
import database.DateTime;
import database.Task;
import database.TaskType;

/**
 * This Command performs the Addition of new Tasks
 * 
 * @author Yao Xiang
 *
 */
public class CommandAdd extends Command {

    private String name = "";
    private DateTime start = new DateTime();
    private DateTime due = new DateTime();
    private DateTime completedOn = new DateTime();

    private List<String> tags = new ArrayList<String>();

    public CommandAdd(List<TaskParam> content) {
        this.type = CommandType.ADD;
        for (TaskParam param : content) {
            constructUsingParam(param);
        }
    }

    private void constructUsingParam(TaskParam param) {
        switch (param.getName()) {
            case PARAM_NAME:
                this.name = param.getField();
                break;

            case PARAM_START:
                this.start = DateParser.parseToDateTime(param.getField());
                break;

            case PARAM_DUE:
                this.due = DateParser.parseToDateTime(param.getField());
                break;

            case PARAM_TAG:
                this.tags.add(param.getField());
                break;

            default:
                System.out.println("Error in adding Add parameters");
        }
    }

    /**
     * Executes 'add' operation of a task<br>
     * This method adds a new Task to the Todo List
     * 
     * @return Result
     */
    @Override
    protected Result execute(boolean userInput) {
        boolean success = false;
        List<Task> list = new ArrayList<Task>();
        if (Processor.LOGGING_ENABLED) {
            Processor.getLogger().info("Executing 'Add' Command...");
        }
        boolean blockConfirmation = isBlocked();
        if (!blockConfirmation) {
            Task newTask = new Task(name, start, due, completedOn, tags,
                    TaskType.TODO);
            success = Processor.getInstance().getFile().add(newTask);
            list.add(newTask);
            blockConfirmation = false;
        }
        return new Result(list, success, getType(), blockConfirmation);
    }

    /**
     * This method checks if the Task contains dates that are already blocked.
     * 
     * @return boolean - {@code True} if when trying to add a Task with a
     *         specified date that fall within a blocked date range, else
     *         {@code False}.
     */
    private boolean isBlocked() {
        boolean blocked = false;
        if (!due.isEmpty() || !start.isEmpty()) {
            Processor processor = Processor.getInstance();
            List<Task> blockDates = processor.getFile().getBlockTasks();
            for (Task blockDate : blockDates) {
                if (overlapsWithBlockTask(blockDate)) {
                    return true;
                }
            }
        }
        return blocked;
    }

    /**
     * This method checks if the current CommandAdd object is trying to add to
     * to a date range that is blocked.
     * 
     * @return boolean - True if overlaps
     */
    private boolean overlapsWithBlockTask(Task blockDate) {
        if (!start.isEmpty() && !due.isEmpty()) {
            return !(due.isEarlierThan(blockDate.getStart()) || !start
                    .isEarlierThan(blockDate.getDue()));
        } else if (!start.isEmpty()) {
            return !(start.isEarlierThan(blockDate.getStart()) || !start
                    .isEarlierThan(blockDate.getDue()));
        } else {
            return !(due.isEarlierThan(blockDate.getStart()) || !due
                    .isEarlierThan(blockDate.getDue()));
        }
    }

    /**
     * Executes complement for 'add' operation of a task<br>
     * This method deletes the newly added Task to the Todo List
     * 
     * @return Result
     */
    @Override
    protected Result executeComplement() {
        Processor processor = Processor.getInstance();
        List<Task> tasks = new ArrayList<Task>();
        boolean success = false;

        int taskId = processor.getFile().getToDoTasks().size() - 1;
        Task toDelete = processor.getFile().getToDoTasks().get(taskId);

        success = processor.getFile().permanentlyDelete(toDelete);

        if (success) {
            tasks.add(toDelete);
        }

        return new Result(tasks, success, getType());
    }

    @Override
    public String get(String paramName) {
        switch (paramName) {
            case PARAM_NAME:
                return this.name;

            case PARAM_START:
                return this.start.toString();

            case PARAM_DUE:
                return this.due.toString();

            default:
                System.out.println("Add: Get's got a problem!");
                return null;
        }
    }

    @Override
    public List<String> getTags() {
        return this.tags;
    }

    @Override
    public String toString() {
        return "cmdadd name: " + this.name + " start: " + this.start +
               " due: " + this.due + " tags: " + this.tags;
    }

}
