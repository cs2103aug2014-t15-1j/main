package logic;

import java.util.ArrayList;
import java.util.List;

import logic.Result.ResultType;
import parser.DateParser;
import parser.TaskParam;
import database.DateTime;
import database.Task;

public class CommandTodo extends Command {
    
    // Todo types [get("rangeType"); returns "last" | "id"]
    private String rangeType = "";

    // "id" data [get("id"); returns string]
    private String id = "";

    private DateTime dateTime = new DateTime();
    
    private CommandDone cmdDone = null;

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
                constructUsingParam(param);
            }
            if (!isComplement) {
                initialiseComplementCommand(content);
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

            case "date":
                assert (DateParser.isValidDate(param.getField())) : "Invalid date for done";
                this.dateTime = new DateTime(param.getField(), "");
                break;
            default:
                this.type = CommandType.ERROR;
                this.error = "Todo constructor parameter error";
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
        if (Processor.ENABLE_LOGGING) {
            Processor.getLogger().info("Executing 'Todo' Command...");
        }
        
        Result result = new Result(null, false, getType(), ResultType.TASK);
        switch (rangeType) {
            case "id":
                    result = todoById();
                break;
            case "date":
                if (!userInput) {
                    result = todoByDate();
                }
                break;
            default:
                break;
        }
        return result;
    }
    
    Result todoById() {
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        try {
            int taskId = Integer.parseInt(id);
            Task existingTask = processor.getFile().getTask(taskId);
            success = processor.getFile().toDoTask(existingTask);
            if (success) {
                list.add(existingTask);
            }
        } catch (NumberFormatException e) {
            if (Processor.ENABLE_LOGGING) {
                Processor.getLogger().warning("Error parsing Integer!");
            }
        }
        
        return new Result(list, success, getType(), ResultType.TASK);
    }
    
    Result todoByDate() {
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        for (Task task : CommandDone.fetchLastDoneTasks()) {
            success = processor.getFile().toDoTask(task);
            if (success) {
                list.add(task);
            }
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
