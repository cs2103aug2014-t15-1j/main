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
        List<BlockDate> blockRange = processor.getFile().getAllBlockDates();
        boolean success = true;
        List<BlockDate> outputs = new ArrayList<BlockDate>();
        
        for (BlockDate blockedDate : blockRange) {
            if (start.compareTo(blockedDate.getStart()) <= 0 &&
                    end.compareTo(blockedDate.getEnd()) >= 0) {
                    success = false;
                    outputs.add(blockedDate);
                    break;
            } else if (blockedDate.getStart().compareTo(start) <= 0 &&
                    blockedDate.getEnd().compareTo(end) >= 0) {
                success = false;
                outputs.add(blockedDate);
                break;
            }
        }
        
        if (success) {
            BlockDate currBlock = new BlockDate(start, end);
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
