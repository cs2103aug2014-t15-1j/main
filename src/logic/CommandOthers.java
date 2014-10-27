package logic;

import logic.Result.ResultType;


/**
 * This Command object encompasses the commands that need no other parameter
 * inputs.
 * 
 * The follow commands are included: "undo", "redo", "exit" and
 * "error".
 * 
 * The "error" Command can be constructed in two ways: an incorrect input, or
 * intentionally with an error message [CommandOthers("error", msg)]. This Command
 * cannot be created by a user, and works more like a thrown Exception.
 * 
 * @author Justin Yeo Zi Xian & Ter Yao Xiang
 *
 */

public class CommandOthers extends Command {
    public CommandOthers(String type) {
        switch (type.toLowerCase()) {
            case "exit":
                this.type = CommandType.EXIT;
                break;

            case "redo":
                this.type = CommandType.REDO;
                break;

            case "undo":
                this.type = CommandType.UNDO;
                break;
                
            case "reset":
                this.type = CommandType.RESET;
                break;

            default:
                this.type = CommandType.ERROR;
                this.error = "Error in Command constructor (command type)";
        }
    }

    public CommandOthers(String type, String msg) {
        assert(type.equals("error"));
        this.type = CommandType.ERROR;
        this.error = msg;
    }

    @Override
    public String toString() {
        String result = "\n[[ CMD-OTHERS ]]";
        result = result.concat("\ncmd-type: " + type);
        result = result.concat("\ncmd-info: " + error);

        return result;
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
            default:
                return new Result(null, false, getType(), ResultType.TASK);
        }
    }
    
    protected Result executeUndo() {
        if (Processor.ENABLE_LOGGING) {
            Processor.getLogger().info("Executing 'Undo' Command...");
        }
        Processor processor = Processor.getInstance();
        Result r = new Result(null, false, CommandType.UNDO, null);
        if (!processor.getBackwardCommandHistory().isEmpty()) {
            Command backwardCommand = processor.getBackwardCommandHistory().pop();
            switch(backwardCommand.getType()) {
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
     * Executes "redo" operation
     * Applicable for 'Add', 'Edit', 'Delete', 'Restore', 'Block', 'Unblock', 'Done', 'Todo'
     * @return true/false on whether operation is performed
     */
    protected Result executeRedo() {
        if (Processor.ENABLE_LOGGING) {
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
     * This method pushes command to the respective stack after undo/redo
     * If undo/redo operation is unsuccessful, command is pushed back to the stack it was popped from
     * @param cmd
     * @param success
     * @param redo - true for redo operations, false for undo operations
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
