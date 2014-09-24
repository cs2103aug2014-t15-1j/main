package Parser;

import java.util.ArrayList;
import java.util.Arrays;

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

    private static final String[] ADD_PARAM_LIST = { "name:", "n:", "more:", "m:",
                                                    "due:", "d;", "start:", "s:",
                                                    "end:", "e:", "priority:", "p:" };

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
            case TYPE_REDO:
            case TYPE_CLEAR:
            case TYPE_JOKE:
            case TYPE_EXIT:
                return new CommandOthers(commandType);
                
            default:
                return new CommandOthers("error");
        }
    }

    private static Command parseUndone(String[] commandItems) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Command parseDone(String[] commandItems) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Command parseUnblock(String[] commandItems) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Command parseBlock(String[] commandItems) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Command parseDisplay(String[] commandItems) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Command parseSearch(String[] commandItems) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Command parseRestore(String[] commandItems) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Command parseDelete(String[] commandItems) {
        return null;
    }

    private static Command parseEdit(String[] commandItems) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Command parseHelp(String[] commandItems) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Command parseAdd(String[] commandItems) {
        String currField = "name";
        String currContent = "";
        ArrayList<TaskParam> addFields = new ArrayList<TaskParam>();
        ArrayList<String> parsed = new ArrayList<String>();

        for (int i = 1; i < commandItems.length; i++) {
            String currWord = commandItems[i];
            if (isParamName(currWord)) {
                if (!parsed.contains(currField)) {
                    addFields.add(new TaskParam(currField, currContent.trim()));
                    // TODO: parsed can be "n" or "name". problem
                    // Change .contains to alreadyFilled or something
                    // This one should check shorthands for the full words
                    parsed.add(currField);
                    currContent = "";
                } else {
                    // TODO: GETTER function for parsed fields
                    // PLUS concat for already parsed.
                }
                // TODO: Change removeLastChar to getParamName()
                currField = removeLastChar(currWord);
            } else if (hasValidHashTag(currWord)) {
                // TODO: Maybe no need to remove first char
                addFields.add(new TaskParam("tag", currWord));
            } else {
                currContent = currContent.concat(" " + currWord);
            }
        }
        
        if (!currContent.isEmpty()) {
            addFields.add(new TaskParam(currField, currContent.trim()));
        }
        
        System.out.println(addFields);
        return new CommandAdd(addFields);
    }

    private static boolean isParamName(String str) {
        return Arrays.asList(ADD_PARAM_LIST).contains(str);
    }

    private static String removeLastChar(String word) {
        return word.substring(0, word.length() - 1);
    }

    private static boolean hasValidHashTag(String word) {
        return word.startsWith("#") && (word.length() > 1);
    }
    
    public static void main(String[] args) {
        System.out.println(Parser.parse("add do homework m: it's #cs2103 cs2103 due: tomorrow end:"));
        System.out.println(Parser.parse("add name: do do due: wednesday m: dead task\n"));
    }

}
