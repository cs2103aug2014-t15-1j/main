package logic;

import java.util.ArrayList;
import java.util.List;

import database.Task;
import parser.TaskParam;
import logic.Result.ResultType;

public class CommandRestore extends Command {

    // Restore types [get("rangeType"); returns "all" | "id"]
    protected String rangeType;

    // Restore type data [get("id"); returns string]
    protected String id;
    protected CommandDelete cmdDelete;
    
    public CommandRestore(List<TaskParam> content) {
        this(content, false);
    }    
    
    protected CommandRestore(List<TaskParam> content, boolean isComplement) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for restore";
        } else {
            this.type = CommandType.RESTORE;

            for (TaskParam param : content) {
                switch (param.getName()) {
                    case "rangeType":
                        this.rangeType = param.getField();
                        break;

                    case "id":
                        this.id = param.getField();
                        break;

                    default:
                        this.type = CommandType.ERROR;
                        this.error = "Restore constructor parameter error";
                }
            }
            if (!isComplement) {
                initialiseComplementCommand(content);
            }
        }
    }
    
    private void initialiseComplementCommand(List<TaskParam> content) {
        this.cmdDelete = new CommandDelete(content, true);
    }

    public String get(String field) {
        switch (field) {
            case "rangeType":
                return this.rangeType;

            case "id":
                return this.id;

            default:
                return null;
        }
    }

    public String toString() {
        String result = "\n[[ CMD-RESTORE: ]]";
        result = result.concat("\nrangeType: " + rangeType);
        result = result.concat("\nid: " + id);

        return result;
    }
    
    /**
     * Executes "Restore" operation
     * Restores a deleted Task
     * Allows restore <id>, restore search
     * @return true/false on whether operation is performed
     */
    protected Result execute(boolean userInput) {
        Processor.getLogger().info("Executing 'Restore' Command...");
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        switch (rangeType) {
            case "id":
                success = restoreUsingId(list);
                break;
            case "search":
                if (userInput) {
                    processor.getBackwardSearchListHistory().push(processor.getLastSearch());
                }
                success = restoreUsingSearch(list);
                break;
            default:
                success = false;
        }
        return new Result(list, success, getType(), ResultType.TASK);
    }

    /** Restores a deleted Task using Id */
    private boolean restoreUsingId(List<Task> list) {
        Processor processor = Processor.getInstance();
        int taskId = Integer.parseInt(id);
        Task task = processor.getFile().getTask(taskId);
        boolean success = processor.getFile().restoreTask(task);
        if (success) {
            list.add(task);
        }
        return success;
    }
    
    /** Restores all deleted Tasks due to 'delete search' */
    private boolean restoreUsingSearch(List<Task> list) {
        try {
            Processor processor = Processor.getInstance();
            List<Task> restoreList = processor.getBackwardSearchListHistory().pop();
            for (Task t : restoreList) {
                boolean success = processor.getFile().restoreTask(t);
                if (success) {
                    list.add(t);
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
     * Executes "delete" operation
     * Deletes a task
     * Allows delete <id>, delete search, delete all
     * @return Result
     */
    protected Result executeComplement() {
        return cmdDelete.execute(false);
    }
}
