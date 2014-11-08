package logic;

import java.util.ArrayList;
import java.util.List;

import parser.objects.TaskParam;
import database.Task;

public class CommandRestore extends Command {

    protected String rangeType = "";

    protected String id = "";

    /* Complement Command object of this Command Object */
    protected CommandDelete cmdDelete = null;

    public CommandRestore(List<TaskParam> content) {
        this(content, false);
    }

    protected CommandRestore(List<TaskParam> content, boolean isComplement) {
        assert (content != null);
        assert (!content.isEmpty());
        
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
                assert false : "Invalid input - Received: " + param.getName();
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
     * Executes "Restore" operation Restores a deleted Task Allows restore <id>,
     * restore search
     * 
     * @return true/false on whether operation is performed
     */
    @Override
    protected Result execute(boolean userInput) {
        if (Processor.LOGGING_ENABLED) {
            Processor.getLogger().info("Executing 'Restore' Command...");
        }
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        switch (rangeType) {
            case RANGE_TYPE_ID:
                success = restoreUsingId(list);
                break;

            case RANGE_TYPE_SEARCH:
                if (userInput) {
                    processor.getBackwardSearchListHistory()
                            .push(processor.fetchLastSearch());
                }
                success = restoreUsingSearch(list);
                break;

            default:
                success = false;

        }
        return new Result(list, success, getType(), DISPLAY_TAB_NO_CHANGE);
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
                for (Task t : restoreList) {
                    boolean success = processor.getFile().restore(t);
                    if (success) {
                        list.add(t);
                    }
                }
            }
            processor.getForwardSearchListHistory().push(restoreList);
        } catch (NullPointerException e) {
            Processor.getLogger().severe("backwardSearchListHistory is empty!");
            return false;
        }
        return true;
    }

    /**
     * Executes "delete" operation Deletes a task Allows delete <id>, delete
     * search, delete all
     * 
     * @return Result
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
