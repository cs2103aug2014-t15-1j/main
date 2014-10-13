package Logic;

import java.util.ArrayList;
import java.util.List;

import Parser.TaskParam;
import Storage.Task;

//TODO: ADD DATES

public class CommandDone extends Command {

    // Done types [get("rangeType"); returns "all" | "id" | "dates"]
    private String rangeType;

    // Done data [get("id"), get("start"), get("end"); returns string]
    // If only 1 date, start = end;
    private String id;
    private String start;
    private String end;

    public CommandDone(List<TaskParam> content) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for done";
        } else {
            this.type = CommandType.DONE;

            for (TaskParam param : content) {
                switch (param.getName()) {
                    case "rangeType":
                        this.rangeType = param.getField();
                        break;

                    case "id":
                        this.id = param.getField();
                        break;
                        
                    case "start":
                        this.start = param.getField();
                        // Enforce start = end if one is blank
                        break;
                        
                    case "end":
                        this.end = param.getField();
                        break;

                    default:
                        this.type = CommandType.ERROR;
                        this.error = "Todo constructor parameter error";
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
        String result = "\n[[ CMD-DONE: ]]";
        result = result.concat("\nrangeType: " + rangeType);
        result = result.concat("\nid: " + id);
        result = result.concat("\nstart: " + start);
        result = result.concat("\nend: " + end);

        return result;
    }

    /**
     * Executes "done" operation
     * Marks a task as 'done'
     * @return Result
     */
    protected Result execute(boolean userInput) {
        Processor.getLogger().info("Executing 'Done' Command...");
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        
        int taskId = Integer.parseInt(id);
        Task existingTask = processor.getFile().getTask(taskId);
        success = processor.getFile().doneTask(existingTask);
        if (success) {
            list.add(existingTask);
        }
        return new Result(list, success, getType());
    }
    
    protected Result executeComplement() {
        Processor.getLogger().info("Executing 'Todo' Command...");
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        
        int taskId = Integer.parseInt(id);
        Task existingTask = processor.getFile().getTask(taskId);
        success = processor.getFile().toDoTask(existingTask);
        if (success) {
            list.add(existingTask);
        }
        return new Result(list, success, getType());
    }
}
