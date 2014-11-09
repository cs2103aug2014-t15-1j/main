package logic;

import java.util.List;
import java.util.ArrayList;

import parser.Parser;
import parser.objects.TaskParam;
import database.DateTime;
import database.Task;

/**
 * This class extends abstract class Command. CommandEdit performs operations
 * related to the changing of Task parameters (excludes it's 'done' status).
 * 
 * @author A0110751W
 *
 */
public class CommandEdit extends Command {

    private String id = "";
    private String name = "";
    private DateTime due = new DateTime();
    private DateTime start = new DateTime();
    private List<String> tags = new ArrayList<String>();
    private List<String> delete = new ArrayList<String>();

    public CommandEdit(List<TaskParam> content) {
        assert (content != null);
        assert (!content.isEmpty());
        this.type = CommandType.EDIT;
        for (TaskParam param : content) {
            constructUsingParam(param);
        }
    }

    private void constructUsingParam(TaskParam param) {
        switch (param.getName()) {
            case PARAM_ID:
                this.id = param.getField();
                break;

            case PARAM_NAME:
                this.name = param.getField();
                break;

            case PARAM_DUE:
                this.due = Parser.parseToDateTime(param.getField());
                break;

            case PARAM_START:
                this.start = Parser.parseToDateTime(param.getField());
                break;

            case PARAM_TAG:
                this.tags.add(param.getField());
                break;

            case PARAM_DELETE:
                this.delete.add(param.getField());
                break;

            default:
                assert false : "Invalid constructor param - Received: " +
                               param.getName();
        }
    }

    @Override
    public String get(String paramName) {
        switch (paramName) {
            case PARAM_ID:
                return this.id;

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

    @Override
    public List<String> getTags() {
        return this.tags;
    }

    /** Returns delete parameter names (Task fields to delete). */
    @Override
    public List<String> getDelete() {
        return this.delete;
    }

    /**
     * This method executes the "edit" operation. It performs edit/deletion of
     * parameters of a Task
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean, String)
     *         Result}
     */
    @Override
    protected Result execute(boolean userInput) {
        Processor.log("Executing 'Edit' Command...");
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = false;

        int taskId = getTaskId();
        Task existingTask = processor.getFile().getTask(taskId);
        
        if (existingTask != null) {
            Task oldTask = new Task(existingTask);
            updateDeletedParam();
            success = canEditTask(list, existingTask, oldTask);
        }
        return new Result(list, success, getType(), DISPLAY_TAB_NO_CHANGE);
    }

    private boolean canEditTask(List<Task> list, Task existingTask, Task oldTask) {
        Processor processor = Processor.getInstance();
        boolean success = processor.getFile().edit(existingTask, name, start,
                                                   due, tags);
        if (success) {
            updateProcessorInfo(list, oldTask, existingTask);
        }
        return success;
    }

    private void updateDeletedParam() {
        for (String deleteParam : delete) {
            switch (deleteParam) {
                case DELETE_NAME:
                    name = null;
                    break;

                case DELETE_DUE:
                    due = null;
                    break;

                case DELETE_START:
                    start = null;
                    break;

                case DELETE_TAGS:
                    tags = null;
                    break;

                default:
                    assert false : "Invalid delete param - Received: " +
                                   deleteParam;
            }
        }
    }

    private int getTaskId() {
        int taskId = 0;
        try {
            taskId = Integer.parseInt(id);
        } catch (Exception e) {
            if (Processor.LOGGING_ENABLED) {
                Processor.getLogger().warning("Invalid Task Id!");
            }
        }
        return taskId;
    }

    private void updateProcessorInfo(List<Task> list, Task oldTask,
                                     Task existingTask) {
        Processor processor = Processor.getInstance();
        processor.getEditedTaskHistory().push(oldTask);
        list.add(existingTask);
    }

    /**
     * This method reverts the last 'Edit' operation performed.
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, String)
     *         Result}
     */
    @Override
    protected Result executeComplement() {
        Processor processor = Processor.getInstance();
        boolean success = false;
        List<Task> tasks = new ArrayList<Task>();

        Task prevTask = processor.getEditedTaskHistory().pop();

        String taskName = prevTask.getName();
        DateTime taskStart = prevTask.getStart();
        DateTime taskDue = prevTask.getDue();
        List<String> taskTags = prevTask.getTags();

        tasks.add(prevTask);
        success = processor.getFile().edit(prevTask.getId(), taskName,
                                           taskStart, taskDue, taskTags);
        return new Result(tasks, success, getType(), DISPLAY_TAB_NO_CHANGE);
    }

    @Override
    public String toString() {
        return "cmdedit id: " + this.id + " name: " + this.name + " start: " +
               this.start + " due: " + this.due + " tags: " + this.tags +
               " delete: " + this.delete;
    }
}
