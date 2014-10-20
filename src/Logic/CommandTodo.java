package logic;

import java.util.ArrayList;
import java.util.List;

import database.DateTime;
import database.Task;
import parser.TaskParam;
import logic.Result.ResultType;

public class CommandTodo extends Command {

    // Todo types [get("rangeType"); returns "last" | "id"]
    private String rangeType;

    // "id" data [get("id"); returns string]
    private String id;

    private DateTime dateTime;
    
    private CommandDone cmdDone;

    public CommandTodo(List<TaskParam> content) {
        this(content, false);
    }
    
    protected CommandTodo(List<TaskParam> content, boolean isComplement) {
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
            if (!isComplement) {
                initialiseComplementCommand(content);
            }
        }
    }

    private void initialiseComplementCommand(List<TaskParam> content) {
        this.cmdDone = new CommandDone(content, true);
    }
    
    @Override
    public String get(String field) {
        switch (field) {
            case "rangeType":
                return this.rangeType;

            case "id":
                return this.id;

            case "date":
                return this.dateTime.toString();
                  
            default:
                return null;
        }
    }

    @Override
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
    @Override
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
        return new Result(list, success, getType(), ResultType.TASK);
    }
    
    /**
     * Executes "done" operation
     * Marks a task as 'done'
     * @return Result
     */
    @Override
    protected Result executeComplement() {
        return this.cmdDone.execute(false);
    }
}
