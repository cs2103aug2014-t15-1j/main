package logic;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Executes "display" operation
     * Allows display, display <id>, display search
     * Allows show, show <id>, show search
     * @return Result
     */
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Result execute(boolean userInput) {
        if (Processor.LOGGING_ENABLED) {
            Processor.getLogger().info("Executing 'Display' Command...");
        }
        Processor processor = Processor.getInstance();
        List list = new ArrayList();
        boolean success = true;
        ResultType resultType = ResultType.TASK;
        switch (rangeType) {
            case "id":
                int taskId = Integer.parseInt(id);
                list.add(processor.getFile().getTask(taskId));
                break;
            case "search":
                list = processor.getLastSearch();
                break;
            case "block":
                list = processor.getFile().getAllBlockDates();
                resultType = ResultType.BLOCKDATE;
                break;
            case "done":
                list = processor.getFile().getDoneTasks();
                break;
            case "deleted":
                list = processor.getFile().getDeletedTasks();
                break;
            case "todo":
                list = processor.getFile().getToDoTasks();
                break;
            case "all":
                list = processor.getFile().getAllTasks();
                break;
            default:
                success = false;
        }
        return new Result(list, success, getType(), resultType);
    }
}
