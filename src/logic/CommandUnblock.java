package logic;

import java.util.ArrayList;
import java.util.List;

import parser.TaskParam;
import database.Task;

public class CommandUnblock extends Command {

    // Only one type of unblock search: "id"
    // Use get("id"); returns string
    protected String id;

    public CommandUnblock(List<TaskParam> content) {
        this(content, false);
    }

    protected CommandUnblock(List<TaskParam> content, boolean isComplement) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for unblock";
        } else {
            this.type = CommandType.UNBLOCK;

            for (TaskParam param : (List<TaskParam>) content) {
                constructUsingParam(param);
            }
        }
    }

    private void constructUsingParam(TaskParam param) {
        switch (param.getName()) {
            case "id":
                this.id = param.getField();
                break;

            default:
                this.type = CommandType.ERROR;
                this.error = "Unblock constructor parameter error";
        }
    }

    // TODO: Change to empty get()?
    @Override
    public String get(String field) {
        switch (field) {
            case "id":
                return this.id;

            default:
                return null;
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
            Processor.getLogger().info("Executing 'Unblock' Command...");
        }
        Processor processor = Processor.getInstance();
        boolean success = false;
        int unblockId = Integer.parseInt(id);
        List<Task> outputs = new ArrayList<Task>();

        if (unblockId > 0) {
            Task blockDate = processor.getFile().getTask(unblockId);
            if (blockDate != null) {
                success = processor.getFile().delete(blockDate);
                outputs.add(blockDate);
                success = true;
            }
        }

        return new Result(outputs, success, CommandType.UNBLOCK);
    }

    @Override
    protected Result executeComplement() {
        boolean success = false;
        Processor processor = Processor.getInstance();
        List<Task> outputs = new ArrayList<Task>();
        int unblockId = Integer.parseInt(id);
        success = processor.getFile().restore(unblockId);
        Task blockDate = processor.getFile().getTask(unblockId);
        outputs.add(blockDate);
        return new Result(outputs, success, CommandType.BLOCK);
    }

    @Override
    public String toString() {
        return "cmdunblock id: " + this.id;
    }
}
