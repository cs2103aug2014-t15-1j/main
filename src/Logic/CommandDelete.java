package Logic;

import java.util.ArrayList;
import java.util.List;

import Parser.TaskParam;
import Storage.Task;

/**
 * This Command stores the delete type and if applicable, an id value.
 * 
 * Delete type: get("rangeType") [Values: "all", "search", "done", "id" ].
 * 
 * Delete id: get("id") [Value: string that can be parsed as int].
 * 
 * @author Justin Yeo Zi Xian & Ter Yao Xiang
 *
 */
public class CommandDelete extends Command {

    // Delete types
    private String rangeType;

    // Delete type data [get("id")]
    private String id;

    public CommandDelete(List<TaskParam> content) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for delete";
        } else {
            this.type = CommandType.DELETE;

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
                        this.error = "Delete constructor parameter error";
                }
            }
        }
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
        String result = "\n[[ CMD-Delete: ]]";
        result = result.concat("\nrangeType: " + rangeType);
        result = result.concat("\nid: " + id);

        return result;
    }
    
    /**
     * Executes "delete" operation
     * Deletes a task
     * Allows delete <id>, delete search, delete all
     * @return Result
     */
    protected Result execute(boolean userInput) {
        Processor.getLogger().info("Executing 'Delete' Command...");
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        switch (rangeType) {
            case "id":
                success = deleteTaskUsingID(list);
                break;
            case "search":
                if (userInput) {
                    processor.getForwardSearchListHistory().push(processor.getLastSearch());
                }
                success = deleteSearchedTasks(list);
                break;
            case "all":
                success = true;
                break;
            default:
                success = false;
        }
        return new Result(list, success, this.getType());
    }
    
    /** Deletes Task using Id */
    private boolean deleteTaskUsingID(List<Task> list) {
        Task t = Processor.getInstance().getFile().getTask(Integer.parseInt(id));

        if (t == null) {
            return false;
        } else {
            boolean success = Processor.getInstance().getFile().deleteTask(t);
            if (success) {
                list.add(t);
            }
            return success;
        }
    }
    
    /** Deletes all Tasks in searchList */
    private boolean deleteSearchedTasks(List<Task> list) {
        try {
            Processor processor = Processor.getInstance();
            List<Task> deleteList = processor.getForwardSearchListHistory().pop();
            for (Task t : deleteList) {
                processor.getFile().deleteTask(t);
                list.add(t);
            }
            processor.getBackwardSearchListHistory().push(deleteList);
        } catch (NullPointerException e) {
            Processor.getLogger().severe("forwardSearchListHistory is empty!");
            return false;
        }
        return true;
    }

    protected Result executeComplement() {
        return executeComplement(false);
    }
    
    /**
     * Executes "Restore" operation
     * Restores a deleted Task
     * Allows restore <id>, restore search
     * @return true/false on whether operation is performed
     */
    protected Result executeComplement(boolean userInput) {
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
        return new Result(list, success, getType());
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
}
