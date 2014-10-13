package Logic;

import java.util.ArrayList;
import java.util.List;

import Parser.TaskParam;
import Storage.Task;

public class CommandTodo extends Command {

    // Todo types [get("rangeType"); returns "last" | "id"]
    private String rangeType;

    // "id" data [get("id"); returns string]
    private String id;

    public CommandTodo(List<TaskParam> content) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for todo";
        } else {
            this.type = CommandType.TODO;

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
        String result = "\n[[ CMD-TODO: ]]";
        result = result.concat("\nrangeType: " + rangeType);
        result = result.concat("\nid: " + id);

        return result;
    }
    
    /**
     * Executes "todo" operation
     * Marks a 'done' task as 'todo'
     * @return Result
     */
    protected Result execute(boolean userInput) {
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
    
    protected Result executeComplement() {
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
}
