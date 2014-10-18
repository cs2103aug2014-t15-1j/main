package Logic;

import java.util.ArrayList;
import java.util.List;

import Logic.Result.ResultType;
import Parser.TaskParam;
import Storage.DateTime;
import Storage.Task;

public class CommandDone extends Command {

    // Done types [get("rangeType"); returns "all" | "id" | "dates"]
    private String rangeType;

    private DateTime dateTime;
    // Done data [get("id"), get("start"), get("end"); returns string]
    // If only 1 date, start = end;
    private String id;

    private CommandTodo cmdTodo;
    
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
                switch (param.getName()) {
                    case "rangeType":
                        this.rangeType = param.getField();
                        break;

                    case "id":
                        this.id = param.getField();
                        break;
                    
                    case "date": 
                        this.dateTime = null;
                        break;
                    default:
                        this.type = CommandType.ERROR;
                        this.error = "Todo constructor parameter error";
                }
            }
            initialiseComplementCommand(content);
        }
    }

    private void initialiseComplementCommand(List<TaskParam> content) {
        this.cmdTodo = new CommandTodo(content, true);
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

        return result;
    }

    /**
     * Executes "done" operation
     * Marks a task as 'done'
     * @return Result
     */
    protected Result execute(boolean userInput) {
        Processor.getLogger().info("Executing 'Done' Command...");
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
        Task existingTask = processor.getFile().getTask(taskId);
        success = processor.getFile().doneTask(existingTask);
        if (success) {
            list.add(existingTask);
        }
        return success;
    }
    
    private boolean doneByDate(List<Task> list) {
        Processor processor = Processor.getInstance();
        boolean success = false;
        for (Task task : processor.getFile().getToDoTasks()) {
            if  (task.getDue().equals(this.dateTime)) {
                task.setDone(true);
                list.add(task);
                success = true;
            }
        }
        return success;
    }
    
    /**
     * Executes "todo" operation
     * Marks a 'done' task as 'todo'
     * @return Result
     */
    protected Result executeComplement() {
        return this.cmdTodo.execute(false);
    }
}
