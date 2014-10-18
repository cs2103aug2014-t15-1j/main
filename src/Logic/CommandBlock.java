package Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Logic.Result.ResultType;
import Parser.DateParser;
import Parser.TaskParam;
import Storage.BlockDate;
import Storage.DateTime;

public class CommandBlock extends Command {

    // Block dates range [get("start"), get("end"); returns date]
    // If it's only 1 day, start = end
    private DateTime start;
    private DateTime end;
    
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
        }
    }

    
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
    protected Result execute(boolean userInput) {
        Processor.getLogger().info("Executing 'Block' Command...");
        Processor processor = Processor.getInstance();
        List<BlockDate> blockRange = processor.getBlockedDates();
        BlockDate currBlock = new BlockDate(start, end);
        boolean success = true;

        List<BlockDate> outputs = new ArrayList<BlockDate>();
        
        for (BlockDate blockedDate : blockRange) {
            if (blockedDate.contains(currBlock)) {
                success = false;
                outputs.add(blockedDate);
                break;
            }
        }
        
        if (success) {
            blockRange.add(currBlock);
            outputs.add(currBlock);
            Collections.sort(blockRange);
        }
        
        return new Result(outputs, success, CommandType.BLOCK, ResultType.BLOCKDATE);
    }
    
    protected Result executeComplement() {
        BlockDate currBlock = new BlockDate(start, end);
        Processor processor = Processor.getInstance();
        List<BlockDate> blockRange = processor.getBlockedDates();
        boolean success = blockRange.remove(currBlock);
        return new Result(blockRange, success, CommandType.BLOCK, ResultType.BLOCKDATE);
    }
}
