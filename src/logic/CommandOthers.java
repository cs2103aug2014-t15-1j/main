package logic;

import java.util.Arrays;

import logic.Result.ResultType;

/**
 * This Command object encompasses the commands that need no other parameter
 * inputs.
 * <p>
 * The following commands are included: "undo", "redo", "reset", "help", "exit".
 * 
 * @author Justin Yeo Zi Xian & Ter Yao Xiang
 *
 */

public class CommandOthers extends Command {
    private static final String STR_HELP = "help";
    private static final String STR_RESET = "reset";
    private static final String STR_UNDO = "undo";
    private static final String STR_REDO = "redo";
    private static final String STR_EXIT = "exit";

    private static final String[] CMD_OTHERS = new String[] { STR_HELP,
                                                             STR_RESET,
                                                             STR_UNDO,
                                                             STR_REDO, STR_EXIT };

    public CommandOthers(String type) {
        assert (Arrays.asList(CMD_OTHERS).contains(type));
        
        switch (type.toLowerCase()) {
            case STR_EXIT:
                this.type = CommandType.EXIT;
                break;

            case STR_REDO:
                this.type = CommandType.REDO;
                break;

            case STR_UNDO:
                this.type = CommandType.UNDO;
                break;

            case STR_RESET:
                this.type = CommandType.RESET;
                break;

            case STR_HELP:
                this.type = CommandType.HELP;
                break;
        }
    }

    @Override
    protected Result execute(boolean userInput) {
        switch (getType()) {
            case REDO:
                return executeRedo();
                
            case UNDO:
                return executeUndo();
                
            case EXIT:
                return new Result(null, true, getType(), ResultType.TASK);
                
            case RESET:
                return new Result(null, true, getType(), true, ResultType.TASK);
                
            case HELP:
                return new Result(null, true, getType(), null);
                
            default:
                return new Result();
        }
    }

    protected Result executeUndo() {
        if (Processor.LOGGING_ENABLED) {
            Processor.getLogger().info("Executing 'Undo' Command...");
        }
        Processor processor = Processor.getInstance();
        Result r = new Result(null, false, CommandType.UNDO, null);
        if (!processor.getBackwardCommandHistory().isEmpty()) {
            Command backwardCommand = processor.getBackwardCommandHistory()
                    .pop();
            switch (backwardCommand.getType()) {
                case ADD:
                case EDIT:
                case DELETE:
                case RESTORE:
                case BLOCK:
                case UNBLOCK:
                case TODO:
                case DONE:
                    r = backwardCommand.executeComplement();
                    break;
                    
                default:
                    return new Result(null, false, CommandType.ERROR, null);
            }
            modifyHistory(backwardCommand, r.isSuccess(), false);
        }
        r.setCommandType(CommandType.UNDO);
        return r;
    }

    /**
     * Executes "redo" operation Applicable for 'Add', 'Edit', 'Delete',
     * 'Restore', 'Block', 'Unblock', 'Done', 'Todo'
     * 
     * @return true/false on whether operation is performed
     */
    protected Result executeRedo() {
        if (Processor.LOGGING_ENABLED) {
            Processor.getLogger().info("Executing 'Redo' Command...");
        }
        Processor processor = Processor.getInstance();
        if (!processor.getForwardCommandHistory().isEmpty()) {
            Command forwardCommand = processor.getForwardCommandHistory().pop();
            Result result = processor.processCommand(forwardCommand, false);
            modifyHistory(forwardCommand, result.isSuccess(), true);
            result.setCommandType(CommandType.REDO);
            return result;
        }
        return new Result(null, false, getType(), null);
    }

    /**
     * This method pushes command to the respective stack after undo/redo If
     * undo/redo operation is unsuccessful, command is pushed back to the stack
     * it was popped from
     * 
     * @param cmd
     * @param success
     * @param redo
     *            - true for redo operations, false for undo operations
     */
    private void modifyHistory(Command cmd, boolean success, boolean redo) {
        Processor processor = Processor.getInstance();
        if (success && redo || !success && !redo) {
            processor.getBackwardCommandHistory().push(cmd);
        } else {
            processor.getForwardCommandHistory().push(cmd);
        }
    }
}
