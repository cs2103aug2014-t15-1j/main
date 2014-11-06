package logic;

import java.util.ArrayList;
import java.util.List;

import database.Task;
import logic.Result.ResultType;
import parser.TaskParam;

public class CommandDisplay extends Command {

    /* Restore types - "all","id" */
    private String rangeType = "";

    /* Restore type data [get("id"); returns string] */
    private String id = "";

    public CommandDisplay(List<TaskParam> content) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for display";
        } else {
            this.type = CommandType.DISPLAY;

            for (TaskParam param : content) {
                constructUsingParam(param);
            }
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
                this.type = CommandType.ERROR;
                this.error = "Restore constructor parameter error";
        }
    }

    /**
     * Executes "display" operation
     * Allows display, display <id>, display search
     * Allows show, show <id>, show search
     * @return Result
     */
    @Override
    protected Result execute(boolean userInput) {
        if (Processor.LOGGING_ENABLED) {
            Processor.getLogger().info("Executing 'Display' Command...");
        }
        
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = true;
        ResultType resultType = ResultType.TASK;
        
        switch (rangeType) {
            case RANGE_TYPE_ID:
                int taskId = Integer.parseInt(id);
                list.add(processor.getFile().getTask(taskId));
                break;
                
            case RANGE_TYPE_SEARCH:
                list = processor.getLastSearch();
                break;
                
            case RANGE_TYPE_BLOCK:
                list = processor.getFile().getBlockTasks();
                resultType = ResultType.BLOCKDATE;
                break;
                
            case RANGE_TYPE_DONE:
                list = processor.getFile().getDoneTasks();
                break;
                
            case RANGE_TYPE_DELETED:
                list = processor.getFile().getDeletedTasks();
                break;
                
            case RANGE_TYPE_TODO:
                list = processor.getFile().getToDoTasks();
                break;
                
            case RANGE_TYPE_ALL:
                list = processor.getFile().getAllTasks();
                break;
                
            default:
                success = false;
                
        }
        
        return new Result(list, success, getType(), resultType);
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
        return "cmddisplay rangetype: " + this.rangeType + " id: " + this.id;
    }
}
