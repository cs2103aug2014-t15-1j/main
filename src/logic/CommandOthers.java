package logic;

import java.util.List;

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
    private static final String TYPE_HELP = "help";
    private static final String TYPE_RESET = "reset";
    private static final String TYPE_UNDO = "undo";
    private static final String TYPE_REDO = "redo";
    private static final String TYPE_EXIT = "exit";

    public CommandOthers(String type) {
        assert type != null : "Constructor param is null";
        assert !type.isEmpty() : "Constructor param is empty";

        switch (type.toLowerCase()) {
            case TYPE_EXIT:
                this.type = CommandType.EXIT;
                break;

            case TYPE_REDO:
                this.type = CommandType.REDO;
                break;

            case TYPE_UNDO:
                this.type = CommandType.UNDO;
                break;

            case TYPE_RESET:
                this.type = CommandType.RESET;
                break;

            case TYPE_HELP:
                this.type = CommandType.HELP;
                break;

            default:
                assert false : "Invalid constructor param - Received: " + type;
        }
    }

    @Override
    protected Result execute(boolean userInput) {
        switch (getType()) {
            case UNDO:
                return executeUndo();

            case REDO:
                return executeRedo();

            case EXIT:
                return executeExit();

            case RESET:
                return executeReset();

            case HELP:
                return executeHelp();

            default:
                assert false : "Invalid command type - Received: " + getType();
                return new Result();
        }
    }

    protected Result executeUndo() {
        Processor.log("Executing 'Undo' Command...");
        Processor processor = Processor.getInstance();
        Result r = new Result(null, false, CommandType.UNDO, "");
        if (!processor.getBackwardCommandHistory().isEmpty()) {
            Command backwardCommand = processor.getBackwardCommandHistory()
                    .pop();
            r = executeComplement(backwardCommand);
            modifyHistory(backwardCommand, r.isSuccess(), false);
        }
        r.setCommandType(CommandType.UNDO);
        return r;
    }

    private Result executeComplement(Command backwardCommand) {
        switch (backwardCommand.getType()) {
            case ADD:
            case EDIT:
            case DELETE:
            case RESTORE:
            case BLOCK:
            case UNBLOCK:
            case TODO:
            case DONE:
                return backwardCommand.executeComplement();

            default:
                return new Result(null, false, null, "");
        }
    }

    /**
     * This method executes the "redo" operation. It is applicable for 'Add',
     * 'Edit', 'Delete', 'Restore', 'Block', 'Unblock', 'Done', 'Todo'
     * operations.
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean, String)
     *         Result}
     */
    protected Result executeRedo() {
        Processor.log("Executing 'Redo' Command...");
        Processor processor = Processor.getInstance();
        if (!processor.getForwardCommandHistory().isEmpty()) {
            Command forwardCommand = processor.getForwardCommandHistory().pop();
            Result result = processor.processCommand(forwardCommand, false);
            modifyHistory(forwardCommand, result.isSuccess(), true);
            result.setCommandType(CommandType.REDO);
            return result;
        }
        return new Result(null, false, getType(), false, DISPLAY_TAB_NO_CHANGE);
    }

    /**
     * This method pushes command to the respective stack after undo/redo
     * operation. Unsuccessful operations are taken into account.
     * 
     * @param cmd
     *            - <code>Command</code> object
     * @param success
     *            - True if operation is successful.
     * @param redo
     *            - True for "redo" operations, False for "undo" operations.
     */
    private void modifyHistory(Command cmd, boolean success, boolean redo) {
        Processor processor = Processor.getInstance();
        if (success && redo || !success && !redo) {
            processor.getBackwardCommandHistory().push(cmd);
        } else {
            processor.getForwardCommandHistory().push(cmd);
        }
    }

    private Result executeExit() {
        return new Result(null, true, getType(), DISPLAY_TAB_NO_CHANGE);
    }

    private Result executeReset() {
        return new Result(null, true, getType(), DISPLAY_TAB_ALL);
    }

    private Result executeHelp() {
        return new Result(null, true, getType(), DISPLAY_TAB_NO_CHANGE);
    }

    @Override
    public String toString() {
        return "cmdothers type: " + this.type;
    }

    @Override
    protected Result executeComplement() {
        return new Result();
    }
}
