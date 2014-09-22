package Parser;

public class Parser {

    private static final String TYPE_HELP = "help";
    private static final String TYPE_ADD = "add";
    private static final String TYPE_EDIT = "edit";
    private static final String TYPE_DELETE = "delete";
    private static final String TYPE_RESTORE = "restore";
    private static final String TYPE_SEARCH = "search";
    private static final String TYPE_DISPLAY = "display";
    private static final String TYPE_BLOCK = "block";
    private static final String TYPE_UNBLOCK = "unblock";
    private static final String TYPE_DONE = "done";
    private static final String TYPE_UNDONE = "undone";
    private static final String TYPE_UNDO = "undo";
    private static final String TYPE_REDO = "redo";
    private static final String TYPE_CLEAR = "clear";
    private static final String TYPE_JOKE = "joke";
    private static final String TYPE_EXIT = "exit";

    public static Command parse(String input) {
        // TODO: check command for errors

        // First word of the input should be the command type
        String[] commandItems = input.split(" ");
        String commandType = commandItems[0].toLowerCase();

        // TODO: create parsing methods
        switch (commandType) {
            case TYPE_HELP:
                return parseHelp(commandItems);
                
            case TYPE_ADD:
                return parseAdd(commandItems);
                
            case TYPE_EDIT:
                return parseEdit(commandItems);
                
            case TYPE_DELETE:
                return parseDelete(commandItems);
                
            case TYPE_RESTORE:
                return parseRestore(commandItems);
                
            case TYPE_SEARCH:
                return parseSearch(commandItems);
                
            case TYPE_DISPLAY:
                return parseDisplay(commandItems);
                
            case TYPE_BLOCK:
                return parseBlock(commandItems);
                
            case TYPE_UNBLOCK:
                return parseUnblock(commandItems);
                
            case TYPE_DONE:
                return parseDone(commandItems);
                
            case TYPE_UNDONE:
                return parseUndone(commandItems);
                
            case TYPE_UNDO:
                return new CommandUndo();
                
            case TYPE_REDO:
                return new CommandRedo();
                
            case TYPE_CLEAR:
                return new CommandClear();
                
            case TYPE_JOKE:
                return new CommandJoke();
                
            case TYPE_EXIT:
                return new CommandExit();
                
            default:
                return null;
        }
    }
}
