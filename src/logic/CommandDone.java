package logic;

import java.util.ArrayList;
import java.util.List;

import logic.Result.ResultType;
import parser.DateParser;
import parser.TaskParam;
import database.DateTime;
import database.Task;

public class CommandDone extends Command {


    private static List<Task> lastTasksRange = new ArrayList<Task>();
    
    // Done types [get("rangeType"); returns "all" | "id" | "date"]
    private String rangeType = "";

    private DateTime dateTime = new DateTime();
    // Done data [get("id"), get("date"); returns string]
    private String id = "";

    private CommandTodo cmdTodo = null;
    
    public CommandDone(List<TaskParam> content) {
        this(content, false);
    }
    
    protected CommandDone(List<TaskParam> content, boolean isComplement) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for done";
        } else {
            this.type = CommandType.DONE;

            for (TaskParam param : content) {
                constructUsingParam(param);
            }
            initialiseComplementCommand(content);
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
            
            case "date": ;
                assert (DateParser.isValidDate(param.getField())) : "Invalid date for done";
                this.dateTime = new DateTime(param.getField(), "");
                break;
                
            default:
                this.type = CommandType.ERROR;
                this.error = "Todo constructor parameter error";
        }
    }

    private void initialiseComplementCommand(List<TaskParam> content) {
        this.cmdTodo = new CommandTodo(content, true);
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

    /**
     * Executes "done" operation
     * Marks a task as 'done'
     * @return Result
     */
    @Override
    protected Result execute(boolean userInput) {
        if (Processor.LOGGING_ENABLED) {
            Processor.getLogger().info("Executing 'Done' Command...");
        }
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        switch (rangeType) {
            case "id":
                success = doneById(list);
                break;
                
            case "date":
                success = doneByDate(list);
                break;
                
            default:
                break;
                
        }
        
        return new Result(list, success, getType(), ResultType.TASK);
    }
    
    private boolean doneById(List<Task> list) {
        Processor processor = Processor.getInstance();
        boolean success = false;
        int taskId = Integer.parseInt(id);
        Task task = processor.getFile().getTask(taskId);
        if (task != null) {
            success = processor.getFile().doneTask(task);
            if (success) {
                list.add(task);
            }
        }
        return success;
    }
    
    private boolean doneByDate(List<Task> list) {
        Processor processor = Processor.getInstance();
        boolean success = false;
        for (Task task : processor.getFile().getToDoTasks()) {
            if (task.getDue().getDate().equals(this.dateTime.getDate())) {
                list.add(task);
                success = true;
            }
        }
        for (Task task : list) {
            lastTasksRange.add(task);
            processor.getFile().doneTask(task);
        }
        return success;
    }
    
    /**
     * Executes "todo" operation
     * Marks a 'done' task as 'todo'
     * @return Result
     */
    @Override
    protected Result executeComplement() {
        Result result = this.cmdTodo.execute(false);
        if (result.isSuccess()) {
            lastTasksRange.clear();
        }
        return result;
    }
    
    protected static List<Task> fetchLastDoneTasks() {
        return lastTasksRange;
    }
}
