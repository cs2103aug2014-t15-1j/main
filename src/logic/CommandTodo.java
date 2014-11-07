package logic;

import java.util.ArrayList;
import java.util.List;

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
            case PARAM_RANGE_TYPE:
                this.rangeType = param.getField();
                break;

            case PARAM_ID:
                this.id = param.getField();
                break;

            case PARAM_DATE:
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
            case PARAM_RANGE_TYPE:
                return this.rangeType;

            case PARAM_ID:
                return this.id;

            case PARAM_DATE:
                return this.dateTime.toString();

            default:
                return null;
        }
    }

    /**
     * Executes "todo" operation Marks a 'done' task as 'todo'
     * 
     * @return Result
     */
    @Override
    protected Result execute(boolean userInput) {
        if (Processor.LOGGING_ENABLED) {
            Processor.getLogger().info("Executing 'Todo' Command...");
        }

        Result result = new Result(null, false, getType());
        switch (rangeType) {
            case RANGE_TYPE_ID:
                result = todoById();
                break;

            case RANGE_TYPE_DATE:
                if (!userInput) {
                    result = todoByDate();
                }
                break;

            default:
                break;
        }
        return result;
    }

    private Result todoById() {
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        try {
            int taskId = Integer.parseInt(id);
            Task task = processor.getFile().getTask(taskId);
            if (task != null) {
                success = processor.getFile().markToDo(task);
                if (success) {
                    list.add(task);
                }
            }
        } catch (NumberFormatException e) {
            if (Processor.LOGGING_ENABLED) {
                Processor.getLogger().warning("Error parsing Integer!");
            }
        }

        return new Result(list, success, getType());
    }

    private Result todoByDate() {
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        for (Task task : CommandDone.fetchLastDoneTasks()) {
            success = processor.getFile().markToDo(task);
            if (success) {
                list.add(task);
            }
        }
        return new Result(list, success, getType());
    }

    /**
     * Executes "done" operation Marks a task as 'done'
     * 
     * @return Result
     */
    @Override
    protected Result executeComplement() {
        return this.cmdDone.execute(false);
    }

    @Override
    public String toString() {
        return "cmdtodo rangeType: " + this.rangeType + " id: " + this.id +
               " date: " + this.dateTime;
    }
}
