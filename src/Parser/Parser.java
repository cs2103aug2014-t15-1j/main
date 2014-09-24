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
    private static final String TYPE_TODO = "todo";
    private static final String TYPE_UNDO = "undo";
    private static final String TYPE_REDO = "redo";
    private static final String TYPE_CLEAR = "clear";
    private static final String TYPE_JOKE = "joke";
    private static final String TYPE_EXIT = "exit";

    private static final String[] ADD_PARAM_LIST = { "name:", "n:", "more:",
                                                    "m:", "due:", "d;",
                                                    "start:", "s:", "end:",
                                                    "e:", "priority:", "p:" };
    private static final String[] HELP_CMD_LIST = { TYPE_ADD, TYPE_EDIT,
                                                   TYPE_DELETE, TYPE_RESTORE,
                                                   TYPE_SEARCH, TYPE_DISPLAY,
                                                   TYPE_BLOCK, TYPE_UNBLOCK,
                                                   TYPE_DONE, TYPE_TODO,
                                                   TYPE_UNDO, TYPE_REDO,
                                                   TYPE_CLEAR, TYPE_JOKE,
                                                   TYPE_EXIT };

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

            case TYPE_TODO:
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
        ArrayList<TaskParam> deleteFields = new ArrayList<TaskParam>();

        String firstWord = commandItems[1];
        if (isDeleteParamName(firstWord.toLowerCase())) {
            deleteFields.add(new TaskParam("rangeType", firstWord));
        } else if (isInteger(firstWord)) {
            deleteFields.add(new TaskParam("rangeType", "id"));
            deleteFields.add(new TaskParam("id", firstWord));
        } else if (isDate(firstWord)) {
            deleteFields.add(new TaskParam("rangeType", "dates"));
            deleteFields.add(new TaskParam("start", firstWord));
            if (commandItems.length > 2 &&
                commandItems[2].toLowerCase().equals("to")) {
                deleteFields.add(new TaskParam("end", commandItems[3]));
            } else {
                deleteFields.add(new TaskParam("end", firstWord));
            }
        }

        return new CommandDelete(deleteFields);
    }

    private static boolean isDate(String firstWord) {
        // TODO Auto-generated method stub
        return true;
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isDeleteParamName(String firstWord) {
        return firstWord.equals("all") || firstWord.equals("search") ||
               firstWord.equals("done");
    }

    private static Command parseEdit(String[] commandItems) {
        String currField = "name";
        String currContent = "";
        ArrayList<TaskParam> editFields = new ArrayList<TaskParam>();
        ArrayList<String> parsed = new ArrayList<String>();

        // TODO: first word ID
        
        for (int i = 1; i < commandItems.length; i++) {
            String currWord = commandItems[i];
            if (isAddParamName(currWord)) {
                if (currContent.length() > 0) {
                    if (paramAlreadyFilled(parsed, currField)) {
                        getTaskParam(editFields, currField)
                                .addToField(currContent);
                    } else {
                        editFields.add(new TaskParam(currField, currContent));
                        parsed.add(currField);
                    }
                }
                currField = getParamName(currWord);
                currContent = "";
            } else if (hasValidHashTag(currWord)) {
                editFields.add(new TaskParam("tag", currWord));
            } else {
                currContent = currContent.concat(" " + currWord);
            }
        }

        if (!currContent.isEmpty()) {
            if (paramAlreadyFilled(parsed, currField)) {
                getTaskParam(editFields, currField).addToField(currContent);
            } else {
                editFields.add(new TaskParam(currField, currContent));
            }
        }

        return new CommandEdit(editFields);
    }

    private static Command parseHelp(String[] commandItems) {
        String helpField = null;

        if (commandItems.length > 1) {
            if (isHelpParam(commandItems[1])) {
                helpField = commandItems[1].toLowerCase();
            } else {
                helpField = "invalid";
            }
        }

        return new CommandHelp(helpField);
    }

    private static boolean isHelpParam(String string) {
        return Arrays.asList(HELP_CMD_LIST).contains(string.toLowerCase());
    }

    private static Command parseAdd(String[] commandItems) {
        String currField = "name";
        String currContent = "";
        ArrayList<TaskParam> addFields = new ArrayList<TaskParam>();
        ArrayList<String> parsed = new ArrayList<String>();

        for (int i = 1; i < commandItems.length; i++) {
            String currWord = commandItems[i];
            if (isAddParamName(currWord)) {
                if (currContent.length() > 0) {
                    if (paramAlreadyFilled(parsed, currField)) {
                        getTaskParam(addFields, currField)
                                .addToField(currContent);
                    } else {
                        addFields.add(new TaskParam(currField, currContent));
                        parsed.add(currField);
                    }
                }
                currField = getParamName(currWord);
                currContent = "";
            } else if (hasValidHashTag(currWord)) {
                addFields.add(new TaskParam("tag", currWord));
            } else {
                currContent = currContent.concat(" " + currWord);
            }
        }

        if (!currContent.isEmpty()) {
            if (paramAlreadyFilled(parsed, currField)) {
                getTaskParam(addFields, currField).addToField(currContent);
            } else {
                addFields.add(new TaskParam(currField, currContent));
            }
        }

        return new CommandAdd(addFields);
    }

    private static String getParamName(String currWord) {
        String validParamName = removeLastChar(currWord);
        String longParamName = convertIfShorthand(validParamName);
        return longParamName;
    }

    private static TaskParam getTaskParam(ArrayList<TaskParam> addFields,
                                          String currField) {
        for (TaskParam tp : addFields) {
            if (tp.getName().equals(currField)) {
                return tp;
            }
        }

        // Code should not reach this point
        TaskParam failSafe = new TaskParam(currField, "");
        addFields.add(failSafe);
        return failSafe;

    }

    private static boolean paramAlreadyFilled(ArrayList<String> parsed,
                                              String currField) {
        return parsed.contains(currField);
    }

    private static String convertIfShorthand(String currField) {
        switch (currField) {
            case "n":
                return "name";
            case "m":
                return "more";
            case "d":
                return "due";
            case "s":
                return "start";
            case "e":
                return "end";
            case "p":
                return "priority";
        }
        return currField;
    }

    private static boolean isAddParamName(String str) {
        return Arrays.asList(ADD_PARAM_LIST).contains(str);
    }

    private static String removeLastChar(String word) {
        return word.substring(0, word.length() - 1);
    }

    private static boolean hasValidHashTag(String word) {
        return word.startsWith("#") && (word.length() > 1);
    }

    public static String parseRawText(String text) {
        //TODO: create text-parsing function for pierce
        return null;
    }
    
    
    public static void main(String[] args) {
        System.out
                .println(Parser
                        .parse("add do homework m: it's #cs2103 cs2103 due: tomorrow end:"));
        System.out.println(Parser
                .parse("add name: do do due: wednesday m: dead task\n"));
        System.out
                .println(Parser
                        .parse("add name: do due: #cs2103 wed name: homework m: late start: priority: due: 9am end: now name: quickly\n"));

        System.out.println(Parser.parse("delete all"));
        System.out.println(Parser.parse("delete search"));
        System.out.println(Parser.parse("delete done"));
        System.out.println(Parser.parse("delete 11"));
        System.out.println(Parser.parse("delete days"));

        System.out.println(Parser.parse("help me"));
        System.out.println(Parser.parse("help"));
        System.out.println(Parser.parse("help add"));

    }

}
