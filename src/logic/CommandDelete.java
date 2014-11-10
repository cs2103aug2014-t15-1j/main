package logic;

import java.util.ArrayList;
import java.util.List;

import objects.Task;
import objects.TaskParam;

/**
 * This class extends Command and performs the deletion of Tasks. Allows
 * deletion by Id, by search results or to wipe the file.
 * 
 * @author A0110751W
 *
 */
public class CommandDelete extends Command {

    private String rangeType = "";

    private String id = "";

    /* Complement Command object of this Command Object */
    private CommandRestore cmdRestore = null;

    public CommandDelete(List<TaskParam> content) {
        this(content, false);
    }

    protected CommandDelete(List<TaskParam> content, boolean isComplement) {
        assert content != null : "Constructor param is null";
        assert !content.isEmpty() : "Constructor param is empty";
        
        this.type = CommandType.DELETE;

        for (TaskParam param : content) {
            constructUsingParam(param);
        }
        if (!isComplement) {
            initialiseComplementCommand((List<TaskParam>) content);
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
        this.cmdRestore = new CommandRestore(content, true);
    }

    /**
     * Executes "delete" operation<br>
     * Deletes a task<br>
     * Allows delete {@literal<id>}, delete search, delete all
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean, String)
     *         Result}
     */
    @Override
    protected Result execute(boolean userInput) {
        Processor.log("Executing 'Delete' Command...");
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        boolean confirmation = false;

        switch (rangeType) {
            case RANGE_TYPE_ID:
                success = deleteTaskUsingID(list);
                break;

            case RANGE_TYPE_SEARCH:
                updateSearchListHistory(processor, userInput);
                success = deleteSearchedTasks(list);
                break;

            case RANGE_TYPE_ALL:
                success = true;
                confirmation = true;
                break;

            default:
                assert false : "Invalid rangeType - Received: " + rangeType;
        }

        return new Result(list, success, this.getType(), confirmation,
                DISPLAY_TAB_NO_CHANGE);
    }

    private void updateSearchListHistory(Processor processor, boolean userInput) {
        if (userInput) {
            List<Task> lastSearch = processor.fetchLastSearch();
            processor.getForwardSearchListHistory().push(lastSearch);
        }
    }

    /** Deletes Task using Id */
    private boolean deleteTaskUsingID(List<Task> list) {
        Processor processor = Processor.getInstance();
        Task task = processor.fetchTaskById(Integer.parseInt(id));
        boolean success = processor.getFile().delete(task);
        if (success) {
            list.add(task);
        }
        return success;
    }

    /** Deletes all Tasks in searchList */
    private boolean deleteSearchedTasks(List<Task> list) {
        try {
            Processor processor = Processor.getInstance();
            List<Task> deleteList = processor.getForwardSearchListHistory()
                    .pop();
            for (Task t : deleteList) {
                processor.getFile().delete(t);
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
     * This method executes the "Restore" operation.<br>
     * It restores a deleted <code>Task</code>.<br>
     * Does <i>restore {@literal<id>}</i> or <i> restore search</i> depending on
     * the <code>CommandDelete</code> object
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean)
     *         Result}
     */
    @Override
    protected Result executeComplement() {
        return cmdRestore.execute(false);
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

    @Override
    public String toString() {
        return "cmddelete rangetype: " + this.rangeType + " id: " + this.id;
    }
}
