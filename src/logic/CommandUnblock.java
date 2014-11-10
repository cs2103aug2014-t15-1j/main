//@author A0110751W
package logic;

import java.util.ArrayList;
import java.util.List;

import objects.Task;
import objects.TaskParam;

/**
 * OBSOLETE CLASS
 * 
 */
public class CommandUnblock extends Command {

    protected String id;

    public CommandUnblock(List<TaskParam> content) {
        this(content, false);
    }

    protected CommandUnblock(List<TaskParam> content, boolean isComplement) {
        assert (content != null);
        assert (!content.isEmpty());
        this.type = CommandType.UNBLOCK;

        for (TaskParam param : (List<TaskParam>) content) {
            constructUsingParam(param);
        }
    }

    private void constructUsingParam(TaskParam param) {
        switch (param.getName()) {
            case "id":
                this.id = param.getField();
                break;

            default:
                assert false : "Invalid constructor param - Received: " +
                               param.getName();
        }
    }

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
        Processor.log("Executing 'Unblock' Command...");
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

        return new Result(outputs, success, CommandType.UNBLOCK,
                DISPLAY_TAB_NO_CHANGE);
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
        return new Result(outputs, success, CommandType.BLOCK,
                DISPLAY_TAB_NO_CHANGE);
    }

    @Override
    public String toString() {
        return "cmdunblock id: " + this.id;
    }
}
