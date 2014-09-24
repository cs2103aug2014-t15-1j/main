package Parser;

import java.util.ArrayList;
import java.util.Arrays;

import Storage.Task;

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
    private static final String[] EDIT_DEL_LIST = { "name", "n", "more", "m",
                                                   "due", "d", "start", "s",
                                                   "end", "e", "priority", "p" };
    private static final String[] HELP_CMD_LIST = { "all", TYPE_ADD, TYPE_EDIT,
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

        try {
            String firstWord = commandItems[1];
            if (isDeleteParamName(firstWord.toLowerCase())) {
                deleteFields.add(new TaskParam("rangeType", firstWord.toLowerCase()));
            } else if (isInteger(firstWord)) {
                deleteFields.add(new TaskParam("rangeType", "id"));
                deleteFields.add(new TaskParam("id", firstWord));
            } else {
                return new CommandOthers("error", "Invalid argument for delete");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return new CommandOthers("error", "No arguments for delete");
        }

        return new CommandDelete(deleteFields);
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
        ArrayList<TaskParam> editFields = new ArrayList<TaskParam>();
        ArrayList<String> parsed = new ArrayList<String>();

        String id;

        if (commandItems.length > 1 && isInteger(commandItems[1])) {
            id = commandItems[1];
            editFields.add(new TaskParam("id", id));
        } else {
            return new CommandOthers("error", "Invalid id for edit");
        }

        for (int i = 2; i < commandItems.length; i++) {
            String currWord = commandItems[i];
            String currWordLC = currWord.toLowerCase();
            if (isAddParamName(currWordLC) || currWordLC.equals("delete:")) {
                currField = getParamName(currWordLC);
            } else if (hasValidHashTag(currWord)) {
                // TODO: are tags case sensitive?
                editFields.add(new TaskParam("tag", currWord));
            } else if (currField.equals("delete")) {
                // if it's a delete
                // check if it's a valid delete keyword (ignore otherwise)
                if (Arrays.asList(EDIT_DEL_LIST)
                        .contains(currWordLC)) {
                    // if delete already has content
                    if (paramAlreadyFilled(parsed, currField)) {
                        // check for duplicate words (TODO: unnecessary?)
                        TaskParam deleteParam = getTaskParam(editFields,
                                                             currField);
                        if (!deleteParam.getField().contains(currWordLC)) {
                            getTaskParam(editFields, currField)
                                    .addToField(currWordLC);
                        }
                    } else {
                        // normal: just make a new param
                        editFields.add(new TaskParam(currField, currWord));
                        parsed.add(currField);
                    }
                }
            } else {
                if (paramAlreadyFilled(parsed, currField)) {
                    getTaskParam(editFields, currField).addToField(currWord);
                } else {
                    editFields.add(new TaskParam(currField, currWord));
                    parsed.add(currField);
                }
            }
        }

        removeDuplicates(editFields);

        return new CommandEdit(editFields);
    }

    private static <E> void removeDuplicates(ArrayList<E> fields) {
        ArrayList<E> toDelete = new ArrayList<E>();

        for (int i = 0; i < fields.size(); i++) {
            for (int j = i + 1; j < fields.size(); j++) {
                if (fields.get(i).equals(fields.get(j))) {
                    toDelete.add(fields.get(j));
                    break;
                }
            }
        }

        for (E tp : toDelete) {
            fields.remove(tp);
        }
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
        ArrayList<TaskParam> addFields = new ArrayList<TaskParam>();
        ArrayList<String> parsed = new ArrayList<String>();

        for (int i = 1; i < commandItems.length; i++) {
            String currWord = commandItems[i];
            String currWordLC = currWord.toLowerCase();
            if (isAddParamName(currWordLC)) {
                currField = getParamName(currWordLC);
            } else if (hasValidHashTag(currWord)) {
                addFields.add(new TaskParam("tag", currWord));
            } else {
                if (paramAlreadyFilled(parsed, currField)) {
                    getTaskParam(addFields, currField).addToField(currWord);
                } else {
                    addFields.add(new TaskParam(currField, currWord));
                    parsed.add(currField);
                }
            }
        }

        removeDuplicates(addFields);

        return new CommandAdd(addFields);
    }

    private static String getParamName(String currWord) {
        String validParamName = removeLastChar(currWord).toLowerCase();
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
        return Arrays.asList(ADD_PARAM_LIST).contains(str.toLowerCase());
    }

    private static String removeLastChar(String word) {
        return word.substring(0, word.length() - 1);
    }

    private static boolean hasValidHashTag(String word) {
        return word.startsWith("#") && (word.length() > 1);
    }

    public static Task parseRawText(String text) {
        String currField = "name";
        String[] textItems = text.split(" ");
        String[] param = new String[] { "", "", "", "", "", "" };
        ArrayList<String> tags = new ArrayList<String>();

        for (int i = 0; i < textItems.length; i++) {
            String currWord = textItems[i];
            if (isAddParamName(currWord)) {
                currField = getParamName(currWord.toLowerCase());
            } else if (hasValidHashTag(currWord)) {
                tags.add(currWord);
            } else {
                int pIndex = getParamIndex(currField);
                if (param[pIndex].isEmpty()) {
                    param[pIndex].concat(currWord);
                } else {
                    param[pIndex].concat(" " + currWord);
                }
            }
        }

        return new Task(param[0], param[1], param[2], param[3], param[4],
                param[5], tags);
    }

    private static int getParamIndex(String currField) {
        switch (currField) {
            case "name":
                return 0;
            case "more":
                return 1;
            case "due":
                return 2;
            case "start":
                return 3;
            case "end":
                return 4;
            case "priority":
                return 5;
            default:
                System.out.println("rawparsing getParamIndex failure");
                return -1;
        }
    }

    public static void main(String[] args) {
        // TEST ADD
        System.out
                .println(Parser
                        .parse("add do homework m: it's #cs2103 cs2103 due: tomorrow end:"));
        System.out.println(Parser
                .parse("add name: do do due: wednesday m: dead task\n"));
        System.out
                .println(Parser
                        .parse("add name: do due: #cs2103 wed name: homework m: late start: priority: due: 9am end: now name: quickly\n"));
        System.out.println(Parser.parse("add"));

        // TEST DELETE
        System.out.println(Parser.parse("delete all"));
        System.out.println(Parser.parse("delete search"));
        System.out.println(Parser.parse("delete done"));
        System.out.println(Parser.parse("delete 11"));
        System.out.println(Parser.parse("delete days"));
        System.out.println(Parser.parse("delete"));

        // TEST HELP
        System.out.println(Parser.parse("help me"));
        System.out.println(Parser.parse("help"));
        System.out.println(Parser.parse("help all"));
        System.out.println(Parser.parse("help add"));

        // TEST EDIT
        System.out
                .println(Parser
                        .parse("edit 1 ten twenty more: addmore start: #cs2103 #cs2103 end: due: tmr delete: name"));
        System.out.println(Parser.parse("edit one two"));
        System.out.println(Parser.parse("edit"));
        System.out.println(Parser
                .parse("edit 2 delete: nil n: to: do: #cs2103 #cs2103"));
        System.out
                .println(Parser
                        .parse("edit 3 delete: name name nil name name n: todo homework delete: name name"));

        // TEST RAW PARSE
        System.out
                .println(Parser
                        .parseRawText("add name: do due: #cs2103 wed name: homework m: late "
                                      + "start: priority: due: 9am end: now name: quickly\n"));

    }

}
