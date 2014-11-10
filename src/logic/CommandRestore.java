package logic;

import java.util.ArrayList;
import java.util.List;

import objects.Task;
import objects.TaskParam;

/**
 * This class extends abstract class Command. CommandRestore performs operations
 * related to the restoration of deleted Tasks only.
 * 
 * @author A0110751W
 *
 */
public class CommandRestore extends Command {

    protected String rangeType = "";

    protected String id = "";

    /* Complement Command object of this Command Object */
    protected CommandDelete cmdDelete = null;

    public CommandRestore(List<TaskParam> content) {
        this(content, false);
    }

    protected CommandRestore(List<TaskParam> content, boolean isComplement) {
        assert content != null : "Constructor param is null";
        assert !content.isEmpty() : "Constructor param is empty";
        
        this.type = CommandType.RESTORE;

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

            default:
                assert false : "Invalid constructor param - Received: " +
                               param.getName();
        }
    }

    private void initialiseComplementCommand(List<TaskParam> content) {
        this.cmdDelete = new CommandDelete(content, true);
    }

    @Override
    public String get(String field) {
        switch (field) {
            case PARAM_RANGE_TYPE:
                return this.rangeType;

            case PARAM_ID:
                return this.id;

            default:
                return null;
        }
    }

    /**
     * This method executes the "Restore" operation.<br>
     * It restores a deleted <code>Task</code>.<br>
     * Does <i>restore {@literal<id>}</i> or <i> restore search</i> depending on
     * the <code>CommandDelete</code> object
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean, String)
     *         Result}
     */
    @Override
    protected Result execute(boolean userInput) {
        Processor.log("Executing 'Restore' Command...");
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        switch (rangeType) {
            case RANGE_TYPE_ID:
                success = restoreUsingId(list);
                break;

            case RANGE_TYPE_SEARCH:
                updateSearchListHistory(userInput);
                success = restoreUsingSearch(list);
                break;

            default:
                assert false : "Invalid rangeType - Received: " + rangeType;

        }
        return new Result(list, success, getType(), DISPLAY_TAB_NO_CHANGE);
    }

    private void updateSearchListHistory(boolean userInput) {
        Processor processor = Processor.getInstance();
        if (userInput) {
            processor.getBackwardSearchListHistory()
                    .push(processor.fetchLastSearch());
        }
    }

    /** Restores a deleted Task using Id */
    private boolean restoreUsingId(List<Task> list) {
        Processor processor = Processor.getInstance();
        int taskId = Integer.parseInt(id);
        boolean success = false;
        Task task = processor.getFile().getTask(taskId);

        if (task != null) {
            success = processor.getFile().restore(task);
            if (success) {
                list.add(task);
            }
        }
        return success;
    }

    /** Restores all deleted Tasks due to 'delete search' */
    private boolean restoreUsingSearch(List<Task> list) {
        try {
            Processor processor = Processor.getInstance();
            List<Task> restoreList = processor.getBackwardSearchListHistory()
                    .pop();
            if (restoreList != null) {
                restoreLastSearch(list, processor, restoreList);
            }
            processor.getForwardSearchListHistory().push(restoreList);
        } catch (NullPointerException e) {
            Processor.getLogger().severe("backwardSearchListHistory is empty!");
            return false;
        }
        return true;
    }

    private void restoreLastSearch(List<Task> list, Processor processor,
                                   List<Task> restoreList) {
        for (Task t : restoreList) {
            boolean success = processor.getFile().restore(t);
            if (success) {
                list.add(t);
            }
        }
    }

    /**
     * Executes "delete" operation<br>
     * Deletes a task<br>
     * Allows delete {@literal<id>}, delete search, delete all
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean)
     *         Result}
     */
    @Override
    protected Result executeComplement() {
        return cmdDelete.execute(false);
    }

    @Override
    public String toString() {
        return "cmdrestore rangetype: " + this.rangeType + " id: " + this.id;
    }
}
