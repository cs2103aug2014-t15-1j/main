//@author A0110751W
package logic;

import java.util.List;
import java.util.ArrayList;

import objects.DateTime;
import objects.Task;
import objects.TaskParam;
import parser.Parser;
import database.TaskType;

/**
 * This class extends Command and is associated with the Add Operation. It
 * supports the addition of new Tasks as well as the deletion of that newly
 * added task.
 * 
 */
public class CommandAdd extends Command {

    private String name = "";
    private DateTime start = new DateTime();
    private DateTime due = new DateTime();
    private DateTime completedOn = new DateTime();

    private List<String> tags = new ArrayList<String>();

    public CommandAdd(List<TaskParam> content) {
        assert content != null : "Constructor param is null";
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
                this.start = Parser.parseToDateTime(param.getField());
                break;

            case PARAM_DUE:
                this.due = Parser.parseToDateTime(param.getField());
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
     * Executes 'add' operation of a task<br>
     * This method adds a new Task to the Todo List; goes through a check to
     * certify whether it can be added.
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean, String)
     *         Result}
     */
    @Override
    protected Result execute(boolean userInput) {
        Processor.log("Executing 'Add' Command...");
        boolean success = false;
        List<Task> list = new ArrayList<Task>();

        boolean isBlock = isBlocked();
        if (!isBlock) {
            success = addNewTask(list);
        } else {
            throw new Error(ERROR_BLOCK_ADD);
        }

        assert (list.size() == 1);
        String displayTab = getDisplayTab(list.get(0));

        return new Result(list, success, getType(), false, displayTab);
    }

    private boolean addNewTask(List<Task> list) {
        boolean success = false;
        Task newTask = new Task(name, start, due, completedOn, tags,
                TaskType.TODO);
        success = Processor.getInstance().getFile().add(newTask);
        list.add(newTask);
        return success;
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
            List<Task> blockDates = processor.fetchBlockTasks();
            for (Task blockDate : blockDates) {
                if (overlapsWithBlockTask(blockDate)) {
                    return true;
                }
            }
        }
        return blocked;
    }

    /**
     * This method checks if the current <code>CommandAdd</code> object is
     * trying to add to to a date that is blocked. Returns True if this object
     * overlaps with one or more <code>Block Task</code>.
     * 
     * @return boolean
     */
    private boolean overlapsWithBlockTask(Task blockDate) {
        if (!start.isEmpty() && !due.isEmpty()) {
            return !(due.isEarlierThan(blockDate.getStart()) || !start
                    .isEarlierThan(blockDate.getDue()));
        } else if (!start.isEmpty()) {
            return hasOverlaps(start, blockDate);
        } else {
            return hasOverlaps(due, blockDate);
        }
    }

    private boolean hasOverlaps(DateTime date, Task blockDate) {
        return (date.getDate().equals(blockDate.getDue().getDate()) || date
                .getDate().equals(blockDate.getStart().getDate())) ||
               (date.isLaterThan(blockDate.getStart()) && date
                       .isEarlierThan(blockDate.getDue()));
    }

    /**
     * Executes complement for 'add' operation of a task<br>
     * This method deletes the newly added Task to the Todo List
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean)
     *         Result}
     */
    @Override
    protected Result executeComplement() {
        Processor processor = Processor.getInstance();
        List<Task> tasks = new ArrayList<Task>();
        int taskId = processor.fetchAllTasks().size();
        Task toDelete = processor.fetchTaskById(taskId);
        boolean success = processor.getFile().permanentlyDelete(toDelete);
        if (success) {
            tasks.add(toDelete);
        }

        return new Result(tasks, success, getType(), DISPLAY_TAB_NO_CHANGE);
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
                return null;
        }
    }

    public List<String> getTags() {
        return this.tags;
    }

    @Override
    public String toString() {
        return "cmdadd name: " + this.name + " start: " + this.start +
               " due: " + this.due + " tags: " + this.tags;
    }

}
