package logic;

import java.util.ArrayList;
import java.util.List;

import logic.Result.ResultType;
import parser.DateParser;
import parser.TaskParam;
import database.DateTime;
import database.Task;
import database.TaskType;

public class CommandBlock extends Command {

    private String name = "";
    private DateTime from = new DateTime();
    private DateTime to = new DateTime();
    private List<String> tags = new ArrayList<String>();
    
    public CommandBlock(List<TaskParam> content) {
        this(content, false);
    }

    protected CommandBlock(List<TaskParam> content, boolean isComplement) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for block";
        } else {
            this.type = CommandType.BLOCK;

            for (TaskParam param : content) {
                constructUsingParam(param);
            }
        }
    }

    private void constructUsingParam(TaskParam param) {
        switch (param.getName()) {
            case PARAM_NAME:
                this.name = name.concat(" " + param.getField()).trim();
                break;

            case PARAM_FROM:
                this.from = DateParser.parseToDateTime(param.getField());
                break;
                
            case PARAM_TO:
                this.to = DateParser.parseToDateTime(param.getField());
                break;

            case PARAM_TAG:
                this.tags.add(param.getField());

            default:
                this.type = CommandType.ERROR;
                this.error = "Block constructor parameter error";
        }
    }
    
    /**
     * Executes Block Command
     * 
     * @param userInput
     * @return Result
     */
    @Override
    protected Result execute(boolean userInput) {
        if (Processor.LOGGING_ENABLED) {
            Processor.getLogger().info("Executing 'Block' Command...");
        }
        Processor processor = Processor.getInstance();
        List<Task> blockRange = processor.getFile().getBlockTasks();
        boolean success = true;

        List<Task> outputs = new ArrayList<Task>();
       
        for (Task blockedDate : blockRange) {
            if (from.compareTo(blockedDate.getStart()) <= 0 &&
                    to.compareTo(blockedDate.getDue()) >= 0) {
                    success = false;
                    outputs.add(blockedDate);
                    break;
            } else if (blockedDate.getStart().compareTo(from) <= 0 &&
                    blockedDate.getDue().compareTo(to) >= 0) {
                success = false;
                outputs.add(blockedDate);
                break;
            }
        }

        if (success) {
            Task currBlock = new Task(name, from, to, new DateTime(), tags, TaskType.BLOCK);
            processor.getFile().add(currBlock);
            outputs.add(currBlock);
        }

        return new Result(outputs, success, CommandType.BLOCK,
                ResultType.BLOCKDATE);
    }

    // TODO: UPDATE FOR NEW TASK FORMAT.
    @Override
    protected Result executeComplement() {
        Processor processor = Processor.getInstance();
        List<Task> tasks = new ArrayList<Task>();
        boolean success = false;

        int taskId = processor.getFile().getBlockTasks().size() - 1;
        Task toDelete = processor.getFile().getBlockTasks().get(taskId);

        success = processor.getFile().permanentlyDelete(toDelete);

        if (success) {
            tasks.add(toDelete);
        }

        return new Result(tasks, success, getType(), ResultType.BLOCKDATE);
        /*
         * Processor processor = Processor.getInstance();
        boolean success = false;
        List<Task> outputs = new ArrayList<Task>();
        int blockIndex = processor.getFile().getBlockTasks().size() - 1;
        if (blockIndex >= 0) {
            Task currBlock = processor.getFile().getBlockTasks().get(blockIndex);
            success = processor.getFile().wipeTask(currBlock);
            outputs.add(currBlock);
        }
        return new Result(outputs, success, CommandType.UNBLOCK, ResultType.BLOCKDATE);*/

    }
    
    @Override
    public String get(String field) {
        switch (field) {
            case PARAM_FROM:
                return this.from.toString();

            case PARAM_TO:
                return this.to.toString();

            default:
                return null;
        }
    }
    
    @Override
    public String toString() {
        return "cmdblock name: " + this.name + " from: " + this.from + " to: " +
               this.to + " tags: " + this.tags;
    }
    
}
