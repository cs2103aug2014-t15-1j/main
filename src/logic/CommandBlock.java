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

    // Block dates range [get("start"), get("end"); returns date]
    // If it's only 1 day, start = end
    private DateTime start = new DateTime();
    private DateTime due = new DateTime();
    
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
            case "start":
                this.start = DateParser.parseToDateTime(param.getField());
                break;

            case "end":
                this.due = DateParser.parseToDateTime(param.getField());
                break;

            default:
                this.type = CommandType.ERROR;
                this.error = "Block constructor parameter error";
        }
    }
    
    @Override
    public String get(String field) {
        switch (field) {
            case "start":
                return this.start.toString();

            case "end":
                return this.due.toString();

            default:
                return null;
        }
    }

    @Override
    public String toString() {
        String result = "\n[[ CMD-BLOCK: ]]";
        result = result.concat("\nstart: " + start);
        result = result.concat("\nend: " + due);

        return result;
    }
    


    /**
     * Executes Block Command
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
            if (start.compareTo(blockedDate.getStart()) <= 0 &&
                    due.compareTo(blockedDate.getDue()) >= 0) {
                    success = false;
                    outputs.add(blockedDate);
                    break;
            } else if (blockedDate.getStart().compareTo(start) <= 0 &&
                    blockedDate.getDue().compareTo(due) >= 0) {
                success = false;
                outputs.add(blockedDate);
                break;
            }
        }
        
        if (success) {
            Task currBlock = new Task("", start, due, new DateTime(), new ArrayList<String>(), TaskType.BLOCK);
            processor.getFile().addNewTask(currBlock);
            outputs.add(currBlock);
        }
        
        return new Result(outputs, success, CommandType.BLOCK, ResultType.BLOCKDATE);
    }
    
    @Override
    protected Result executeComplement() {
        Processor processor = Processor.getInstance();
        boolean success = false;
        List<Task> outputs = new ArrayList<Task>();
        int blockIndex = processor.getFile().getBlockTasks().size() - 1;
        if (blockIndex >= 0) {
            Task currBlock = processor.getFile().getBlockTasks().get(blockIndex);
            success = processor.getFile().wipeTask(currBlock);
            outputs.add(currBlock);
        }
        return new Result(outputs, success, CommandType.UNBLOCK, ResultType.BLOCKDATE);
        
    }
}
