package logic;

import java.util.ArrayList;
import java.util.List;

import logic.Result.ResultType;
import parser.DateParser;
import parser.TaskParam;
import database.BlockDate;
import database.DateTime;

public class CommandBlock extends Command {

    // Block dates range [get("start"), get("end"); returns date]
    // If it's only 1 day, start = end
    private DateTime start = new DateTime();
    private DateTime end = new DateTime();
    
    public CommandBlock() {
    }
    
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
                this.end = DateParser.parseToDateTime(param.getField());
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
                return this.end.toString();

            default:
                return null;
        }
    }

    @Override
    public String toString() {
        String result = "\n[[ CMD-BLOCK: ]]";
        result = result.concat("\nstart: " + start);
        result = result.concat("\nend: " + end);

        return result;
    }
    

    /**
     * Executes Block Command
     * @param userInput
     * @return Result
     */
    @Override
    protected Result execute(boolean userInput) {
        if (Processor.ENABLE_LOGGING) {
            Processor.getLogger().info("Executing 'Block' Command...");
        }
        Processor processor = Processor.getInstance();
        List<BlockDate> blockRange = processor.getFile().getAllBlockDates();
        BlockDate currBlock = new BlockDate(start, end);
        boolean success = true;

        List<BlockDate> outputs = new ArrayList<BlockDate>();
        
        for (BlockDate blockedDate : blockRange) {
            if (blockedDate.contains(currBlock)) {
                success = false;
                outputs.add(blockedDate);
                break;
            } else if (currBlock.contains(blockedDate)) {
                success = false;
                outputs.add(blockedDate);
                break;
            }
        }
        
        if (success) {
            processor.getFile().addNewBD(currBlock);
            outputs.add(currBlock);
        }
        
        return new Result(outputs, success, CommandType.BLOCK, ResultType.BLOCKDATE);
    }
    
    @Override
    protected Result executeComplement() {
        Processor processor = Processor.getInstance();
        int blockIndex = processor.getFile().getAllBlockDates().size() - 1;
        BlockDate currBlock = processor.getFile().getAllBlockDates().get(blockIndex);
        List<BlockDate> outputs = new ArrayList<BlockDate>();
        boolean success = processor.getFile().wipeBD(currBlock);
        outputs.add(currBlock);
        return new Result(outputs, success, CommandType.UNBLOCK, ResultType.BLOCKDATE);
    }
}
