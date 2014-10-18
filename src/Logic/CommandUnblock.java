package Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Logic.Result.ResultType;
import Parser.TaskParam;
import Storage.BlockDate;

public class CommandUnblock extends Command {

    // Only one type of unblock search: "id"
    // Use get("id"); returns string
    protected String id;

    private CommandBlock cmdBlock;
    
    public CommandUnblock(List<TaskParam> content) {
        this(content, false);
    }
    
    protected CommandUnblock(List<TaskParam> content, boolean isComplement) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for unblock";
        } else {
            this.type = CommandType.UNBLOCK;

            for (TaskParam param : content) {
                switch (param.getName()) {
                    case "id":
                        this.id = param.getField();
                        break;

                    default:
                        this.type = CommandType.ERROR;
                        this.error = "Unblock constructor parameter error";
                }
            }
        }
    }
    
    // TODO: Change to empty get()?
    public String get(String field) {
        switch (field) {
            case "id":
                return this.id;

            default:
                return null;
        }
    }

    /**
     * @return the cmdBlock
     */
    public CommandBlock getCmdBlock() {
        return cmdBlock;
    }

    /**
     * @param cmdBlock the cmdBlock to set
     */
    public void setCmdBlock(CommandBlock cmdBlock) {
        this.cmdBlock = cmdBlock;
    }

    public String toString() {
        String result = "\n[[ CMD-Unblock: ]]";
        result = result.concat("\nid: " + id);

        return result;
    }

    /**
     * Executes Block Command
     * @param userInput
     * @return Result
     */
    protected Result execute(boolean userInput) {
        Processor.getLogger().info("Executing 'Unblock' Command...");
        Processor processor = Processor.getInstance();
        List<BlockDate> blockRange = processor.getBlockedDates();
        boolean success = false;
        int unblockId = Integer.parseInt(id) - 1;
        List<BlockDate> outputs = new ArrayList<BlockDate>();
        
        if (unblockId < blockRange.size() && unblockId > 0) {
            BlockDate blockedDate = blockRange.remove(unblockId);
            processor.getDeletedBlockDates().push(blockedDate);
            outputs.add(blockedDate);
            success = true;
        }
        
        return new Result(outputs, success, CommandType.UNBLOCK, ResultType.BLOCKDATE);
    }
    
    protected Result executeComplement() {
        boolean success = false;
        Processor processor = Processor.getInstance();
        List<BlockDate> outputs = null;
        List<BlockDate> blockRange = processor.getBlockedDates();
        
        if (!processor.getDeletedBlockDates().isEmpty()) {
            BlockDate blockDate = processor.getDeletedBlockDates().pop();
            outputs = new ArrayList<BlockDate>();
            outputs.add(blockDate);
            success = blockRange.add(blockDate);
            Collections.sort(blockRange);
        }
        
        return new Result(outputs, success, CommandType.UNBLOCK, ResultType.BLOCKDATE);
    }
}
