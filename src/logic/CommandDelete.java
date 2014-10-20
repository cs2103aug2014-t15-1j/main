package logic;

import java.util.ArrayList;
import java.util.List;

import logic.Result.ResultType;
import parser.TaskParam;
import database.Task;

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

    private CommandRestore cmdRestore;
    
    public CommandDelete(List<TaskParam> content) {
        this(content, false);
    }
    
    protected CommandDelete(List<TaskParam> content, boolean isComplement) {
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
            if (!isComplement) {
                initialiseComplementCommand(content);
            }
        }
    }

    private void initialiseComplementCommand(List<TaskParam> content) {
        this.cmdRestore = new CommandRestore(content, true);
    }
    
    @Override
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

    @Override
    public String toString() {
        String result = "\n[[ CMD-Delete: ]]";
        result = result.concat("\nrangeType: " + rangeType);
        result = result.concat("\nid: " + id);

        return result;
    }
    
    /**
     * Executes "delete" operation<br>
     * Deletes a task<br>
     * Allows delete {@literal<id>}, delete search, delete all
     * @return Result
     */
    @Override
    protected Result execute(boolean userInput) {
        Processor.getLogger().info("Executing 'Delete' Command...");
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        boolean confirmation = false;
        
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
                confirmation = true;
                break;
            default:
                success = false;
        }
        return new Result(list, success, this.getType(), confirmation, ResultType.TASK);
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

    /**
     * Executes "Restore" operation<br>
     * Restores a deleted Task<br>
     * Allows restore {@literal<id>}, restore search
     * @return true/false on whether operation is performed
     */
    @Override
    protected Result executeComplement() {
        return cmdRestore.execute(false);
    }
}
