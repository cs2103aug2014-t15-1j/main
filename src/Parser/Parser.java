package Parser;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import Logic.Command;
import Logic.CommandAdd;
import Logic.CommandBlock;
import Logic.CommandDelete;
import Logic.CommandDisplay;
import Logic.CommandDone;
import Logic.CommandEdit;
import Logic.CommandHelp;
import Logic.CommandOthers;
import Logic.CommandRestore;
import Logic.CommandSearch;
import Logic.CommandTodo;
import Logic.CommandUnblock;
import Storage.Task;

public class Parser {
    
    private static final String TYPE_ALL = "all";
    private static final String TYPE_HELP = "help";
    private static final String TYPE_ADD = "add";
    private static final String TYPE_EDIT = "edit";
    private static final String TYPE_DELETE = "delete";
    private static final String TYPE_RESTORE = "restore";
    private static final String TYPE_SEARCH = "search";
    private static final String TYPE_DISPLAY = "display";
    private static final String TYPE_SHOW = "show";
    private static final String TYPE_BLOCK = "block";
    private static final String TYPE_UNBLOCK = "unblock";
    private static final String TYPE_DONE = "done";
    private static final String TYPE_TODO = "todo";
    private static final String TYPE_UNDO = "undo";
    private static final String TYPE_REDO = "redo";
    private static final String TYPE_EXIT = "exit";

    // TODO: CONSIDER USING PARAM_FIRST_WORD = 1
    private static final String[] TASK_PARAM_LIST_COLON = { "name:", "n:",
                                                           "due:", "d:",
                                                           "start:", "s:",
                                                           "end:", "e:" };
    private static final String[] TASK_PARAM_LIST = { "name", "n", "due", "d",
                                                     "start", "s", "end", "e" };
    private static final String[] HELP_CMD_LIST = { TYPE_ALL, TYPE_ADD,
                                                   TYPE_EDIT, TYPE_DELETE,
                                                   TYPE_RESTORE, TYPE_SEARCH,
                                                   TYPE_DISPLAY, TYPE_BLOCK,
                                                   TYPE_UNBLOCK, TYPE_DONE,
                                                   TYPE_TODO, TYPE_UNDO,
                                                   TYPE_REDO, TYPE_EXIT };

    // ========== MAIN PARSE METHOD ==========//

    public static Command parse(String input) {
        // TODO: check command for errors

        // First word of the input should be the command type
        String[] commandItems = input.split(" ");
        String commandType = commandItems[0].toLowerCase();

        // TODO: commandParams = removeCommandWord(commandItems)
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
            case TYPE_SHOW:
                return parseDisplay(commandItems);

            case TYPE_BLOCK:
                return parseBlock(commandItems);

            case TYPE_UNBLOCK:
                return parseUnblock(commandItems);

            case TYPE_DONE:
                return parseDone(commandItems);

            case TYPE_TODO:
                return parseTodo(commandItems);

            case TYPE_UNDO:
            case TYPE_REDO:
            case TYPE_EXIT:
                return new CommandOthers(commandType);

            default:
                return new CommandOthers("error",
                        "Error in initial command parsing");
        }
    }

    // ========== INDIVIDUAL PARSE-COMMAND FUNCTIONS ==========//

    private static Command parseTodo(String[] commandItems) {
        List<TaskParam> todoFields = new ArrayList<TaskParam>();

        try {
            String firstWord = commandItems[1];
            String firstWordLC = firstWord.toLowerCase();
            if (firstWordLC.equals("last")) {
                todoFields.add(new TaskParam("rangeType", firstWordLC));
            } else if (isInteger(firstWord)) {
                todoFields.add(new TaskParam("rangeType", "id"));
                todoFields.add(new TaskParam("id", firstWord));
            } else {
                return new CommandOthers("error", "Invalid argument for todo");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return new CommandOthers("error", "No arguments for todo");
        }

        return new CommandTodo(todoFields);
    }

    private static Command parseDone(String[] commandItems) {
        List<TaskParam> doneFields = new ArrayList<TaskParam>();

        try {
            String firstWord = commandItems[1];
            String firstWordLC = firstWord.toLowerCase();
            if (isDate(firstWord)) {
                doneFields.add(new TaskParam("rangeType", "dates"));
                if (commandItems.length == 4 && commandItems[2].equals("to") &&
                    isDate(commandItems[3])) {
                    if (firstDateEarlier(firstWord, commandItems[3])) {
                        doneFields.add(new TaskParam("start", firstWord));
                        doneFields.add(new TaskParam("end", commandItems[3]));
                    } else {
                        doneFields.add(new TaskParam("end", firstWord));
                        doneFields.add(new TaskParam("start", commandItems[3]));
                    }
                } else if (commandItems.length == 2) {
                    doneFields.add(new TaskParam("start", firstWord));
                    doneFields.add(new TaskParam("end", firstWord));
                } else {
                    return new CommandOthers("error",
                            "Invalid arguments for block");
                }
            } else if (firstWordLC.equals("all")) {
                doneFields.add(new TaskParam("rangeType", firstWordLC));
            } else if (isInteger(firstWord)) {
                doneFields.add(new TaskParam("rangeType", "id"));
                doneFields.add(new TaskParam("id", firstWord));
            } else {
                return new CommandOthers("error", "Invalid argument for done");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return new CommandOthers("error", "No arguments for done");
        }

        return new CommandDone(doneFields);
    }

    private static Command parseUnblock(String[] commandItems) {
        List<TaskParam> unblockFields = new ArrayList<TaskParam>();

        try {
            String firstWord = commandItems[1];
            if (isInteger(firstWord)) {
                unblockFields.add(new TaskParam("id", firstWord));
            } else {
                return new CommandOthers("error",
                        "Invalid argument for unblock");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return new CommandOthers("error", "No arguments for unblock");
        }

        return new CommandUnblock(unblockFields);
    }

    private static Command parseBlock(String[] commandItems) {
        List<TaskParam> blockFields = new ArrayList<TaskParam>();

        // TODO: Consider date format (length 1, 2, 3?)
        // Currently: Date must be "DD/MM/YYYY" format
        try {
            String firstWord = commandItems[1];
            if (isDate(firstWord)) {
                // TODO: Very unforgiving formatting; doesn't allow any
                // additional statements
                if (commandItems.length == 4 && commandItems[2].equals("to") &&
                    isDate(commandItems[3])) {
                    if (firstDateEarlier(firstWord, commandItems[3])) {
                        blockFields.add(new TaskParam("start", firstWord));
                        blockFields.add(new TaskParam("end", commandItems[3]));
                    } else {
                        blockFields.add(new TaskParam("end", firstWord));
                        blockFields
                                .add(new TaskParam("start", commandItems[3]));
                    }
                } else if (commandItems.length == 2) {
                    blockFields.add(new TaskParam("start", firstWord));
                    blockFields.add(new TaskParam("end", firstWord));
                } else {
                    return new CommandOthers("error",
                            "Invalid arguments for block");
                }
            } else {
                return new CommandOthers("error", "Invalid arguments for block");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return new CommandOthers("error", "No arguments for block");
        }

        return new CommandBlock(blockFields);
    }

    private static boolean firstDateEarlier(String first, String second) {
        int[] date1 = splitToDatesInt(first);
        int[] date2 = splitToDatesInt(second);

        int day1 = date1[0];
        int mth1 = date1[1];
        int yr1 = date1[2];
        int day2 = date2[0];
        int mth2 = date2[1];
        int yr2 = date2[2];

        if (yr1 < yr2) {
            return true;
        } else if (yr1 == yr2) {
            if (mth1 < mth2) {
                return true;
            } else if (mth1 == mth2 && day1 < day2) {
                return true;
            }
        }

        System.out.println("second is earlier");
        return false;
    }

    private static int[] splitToDatesInt(String str) {
        assert (isDate(str));

        String[] split = str.split("/");

        int[] result = new int[3];
        result[0] = Integer.parseInt(split[0]);
        result[1] = Integer.parseInt(split[1]);
        result[2] = Integer.parseInt(split[2]);

        return result;
    }

    private static boolean isDate(String str) {
        // Tentatively, dates = "DD/MM/YYYY"
        boolean hasTwoSlashes;
        boolean hasValidCompLengths;
        boolean hasIntComponents;
        boolean hasValidIntComp;

        try {
            String[] components = str.split("/");
            hasTwoSlashes = (components.length == 3);
            hasValidCompLengths = (components[0].length() == 2) &&
                                  (components[1].length() == 2) &&
                                  (components[2].length() == 4);
            hasIntComponents = isInteger(components[0]) &&
                               isInteger(components[1]) &&
                               isInteger(components[2]);
            hasValidIntComp = isValidMonth(components[1]) &&
                              isValidDay(components[0], components[1]);
        } catch (Exception e) {
            return false;
        }
        return hasTwoSlashes && hasValidCompLengths && hasIntComponents &&
               hasValidIntComp;
    }

    private static boolean isValidDay(String day, String month) {
        int dayNum = Integer.parseInt(day);
        int monthNum = Integer.parseInt(month);

        switch (monthNum) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return dayNum > 0 && dayNum <= 31;

            case 2:
                return dayNum > 0 && dayNum <= 28;

            case 4:
            case 6:
            case 9:
            case 11:
                return dayNum > 0 && dayNum <= 30;
        }
        return false;
    }

    private static boolean isValidMonth(String string) {
        int monthNum = Integer.parseInt(string);
        return monthNum > 0 && monthNum <= 12;
    }

    private static Command parseDisplay(String[] commandItems) {
        List<TaskParam> displayFields = new ArrayList<TaskParam>();

        // CONSIDER: No arguments "display"?
        // TODO: MERGE DISPLAY, DELETE, RESTORE?
        try {
            String firstWord = commandItems[1];
            String firstWordLC = firstWord.toLowerCase();
            if (firstWordLC.equals("block")) {
                displayFields.add(new TaskParam("rangeType", firstWordLC));
            } else if (isInteger(firstWord)) {
                displayFields.add(new TaskParam("rangeType", "id"));
                displayFields.add(new TaskParam("id", firstWord));
            } else {
                return new CommandOthers("error",
                        "Invalid argument for display");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            displayFields.add(new TaskParam("rangeType", "all"));
        }
        
        return new CommandDisplay(displayFields);
    }

    private static Command parseSearch(String[] commandItems) {
        List<TaskParam> searchFields = new ArrayList<TaskParam>();

        // TODO: REFACTOR
        try {
            if (commandItems.length < 2) {
                throw new ArrayIndexOutOfBoundsException();
            }

            boolean donenessIndicated = false;
            boolean dateIndicated = false;
            for (int i = 1; i < commandItems.length; i++) {
                String currWord = commandItems[i];
                String currWordLC = currWord.toLowerCase();
                if (isDate(currWord)) {
                    if (!dateIndicated) {
                        searchFields.add(new TaskParam("date", currWord));
                        dateIndicated = true;
                    } else {
                        System.out.println("A date has already been indicated");
                        // TODO: Update GUI w/ error
                    }
                } else if (hasValidHashTag(currWord)) {
                    // TODO: Error Handling for multiple doneness keywords
                    if (currWordLC.equals("#done") ||
                        currWordLC.equals("#deleted") ||
                        currWordLC.equals("#todo")) {
                        if (!donenessIndicated) {
                            searchFields.add(new TaskParam("tag", currWordLC));
                            donenessIndicated = true;
                        } else {
                            System.out
                                    .println("A done-ness keyword has already been indicated!");
                        }
                    } else {
                        searchFields.add(new TaskParam("tag", currWord));
                    }
                } else {
                    searchFields.add(new TaskParam("word", currWord));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return new CommandOthers("error", "No arguments for search");
        }

        return new CommandSearch(searchFields);
    }

    private static Command parseRestore(String[] commandItems) {
        List<TaskParam> restoreFields = new ArrayList<TaskParam>();

        try {
            String firstWord = commandItems[1];
            if (firstWord.toLowerCase().equals("all")) {
                restoreFields.add(new TaskParam("rangeType", firstWord
                        .toLowerCase()));
            } else if (isInteger(firstWord)) {
                restoreFields.add(new TaskParam("rangeType", "id"));
                restoreFields.add(new TaskParam("id", firstWord));
            } else {
                return new CommandOthers("error",
                        "Invalid argument for restore");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return new CommandOthers("error", "No arguments for restore");
        }

        return new CommandRestore(restoreFields);
    }

    private static Command parseDelete(String[] commandItems) {
        List<TaskParam> deleteFields = new ArrayList<TaskParam>();

        try {
            String firstWord = commandItems[1];
            if (isDate(firstWord)) {
                deleteFields.add(new TaskParam("rangeType", "dates"));
                if (commandItems.length == 4 && commandItems[2].equals("to") &&
                    isDate(commandItems[3])) {
                    if (firstDateEarlier(firstWord, commandItems[3])) {
                        deleteFields.add(new TaskParam("start", firstWord));
                        deleteFields.add(new TaskParam("end", commandItems[3]));
                    } else {
                        deleteFields.add(new TaskParam("end", firstWord));
                        deleteFields
                                .add(new TaskParam("start", commandItems[3]));
                    }
                } else if (commandItems.length == 2) {
                    deleteFields.add(new TaskParam("start", firstWord));
                    deleteFields.add(new TaskParam("end", firstWord));
                } else {
                    return new CommandOthers("error",
                            "Too many arguments for delete (dates) detected");
                }
            } else if (isDeleteParamName(firstWord.toLowerCase())) {
                deleteFields.add(new TaskParam("rangeType", firstWord
                        .toLowerCase()));
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

    private static Command parseEdit(String[] commandItems) {
        String currField = "name";
        String prevField = "name";
        String id;
        List<TaskParam> editFields = new ArrayList<TaskParam>();

        // Check Edit ID
        // TODO: Note that currently all non-delete parameters will be thrown to
        // "name:" if "delete" is the current field. Also, delete has no
        // shorthand.
        if (commandItems.length > 1 && isInteger(commandItems[1])) {
            id = commandItems[1];
            editFields.add(new TaskParam("id", id));
        } else {
            return new CommandOthers("error", "Invalid id for edit");
        }

        for (int i = 2; i < commandItems.length; i++) {
            String currWord = commandItems[i];
            String currWordLC = currWord.toLowerCase();

            if (isAddParamName(currWord) || currWordLC.equals("delete:")) {
                prevField = currField;
                currField = getParamName(currWord);
            } else if (containsParamName(currWord) ||
                       containsDeleteParam(currWord)) {
                String[] wordList = currWord.split(":");
                int endIndex;
                if (currWord.endsWith(":")) {
                    endIndex = wordList.length;
                } else {
                    endIndex = wordList.length - 1;
                }

                int lastValidField = 0;
                // Check only until the second last word
                // TODO: Check below functionality for Add. (lowercase,
                // wordlist.length)
                for (int j = 0; j < endIndex; j++) {
                    String toCheck = wordList[j].toLowerCase() + ":";
                    if (isEditParamName(toCheck)) {
                        prevField = currField;
                        currField = getParamName(toCheck);
                        lastValidField = j;
                    } else {
                        break;
                    }
                }

                // Add remainder to the current field
                String toAddToField = "";
                for (int k = lastValidField + 1; k < wordList.length; k++) {
                    toAddToField = toAddToField.concat(wordList[k]);
                    if (k != wordList.length - 1) {
                        toAddToField = toAddToField.concat(":");
                    }
                }
                getTaskParam(editFields, currField).addToField(toAddToField);
            } else if (hasValidHashTag(currWord)) {
                editFields.add(new TaskParam("tag", currWord));
            } else if (currField.equals("delete")) {
                // check if it's a valid delete keyword (ignore otherwise)
                if (Arrays.asList(TASK_PARAM_LIST).contains(currWordLC)) {
                    // check for duplicate words (TODO: unnecessary?)
                    TaskParam deleteParam = getTaskParam(editFields, currField);
                    if (!deleteParam.getField().contains(currWordLC)) {
                        deleteParam.addToField(currWordLC);
                    }
                } else {
                    getTaskParam(editFields, prevField).addToField(currWord);
                }
            } else {
                getTaskParam(editFields, currField).addToField(currWord);
            }
        }

        removeDuplicates(editFields);

        return new CommandEdit(editFields);
    }

    private static Command parseHelp(String[] commandItems) {
        String helpField = null;

        // TODO: change CommandHelp to process "invalid" as Command.ERROR?
        if (commandItems.length > 1) {
            if (isHelpParam(commandItems[1])) {
                helpField = commandItems[1].toLowerCase();
            } else {
                helpField = "invalid";
            }
        }

        return new CommandHelp(helpField);
    }

    private static Command parseAdd(String[] commandItems) {
        String currField = "name";
        List<TaskParam> addFields = new ArrayList<TaskParam>();

        for (int i = 1; i < commandItems.length; i++) {
            String currWord = commandItems[i];
            if (containsParamName(currWord)) {
                String[] wordList = currWord.split(":");
                int endIndex;
                if (currWord.endsWith(":")) {
                    endIndex = wordList.length;
                } else {
                    endIndex = wordList.length - 1;
                }

                int lastValidField = 0;
                // Check until endIndex
                for (int j = 0; j < endIndex; j++) {
                    if (isAddParamName(wordList[j] + ":")) {
                        currField = getParamName(wordList[j] + ":");
                        lastValidField = j;
                    } else {
                        break;
                    }
                }

                // Add remainder to the current field
                String toAddToField = "";
                for (int k = lastValidField + 1; k < wordList.length; k++) {
                    toAddToField = toAddToField.concat(wordList[k]);
                    if (k != wordList.length - 1) {
                        toAddToField = toAddToField.concat(":");
                    }
                }
                getTaskParam(addFields, currField).addToField(toAddToField);
            } else if (isAddParamName(currWord)) {
                currField = getParamName(currWord);
            } else if (hasValidHashTag(currWord)) {
                addFields.add(new TaskParam("tag", currWord));
            } else {
                // IMPLEMENT TIME
                getTaskParam(addFields, currField).addToField(currWord);
            }
        }

        removeDuplicates(addFields);
        if (containsParam(addFields, "due")) {
            convertToDate(addFields, "due");
        }
        if (containsParam(addFields, "start")) {

        }
        if (containsParam(addFields, "end")) {

        }

        return new CommandAdd(addFields);
    }

    private static void convertToDate(List<TaskParam> addFields, String string) {
        // TODO: re-factor to getDate() and setDate();
        String[] months = { "jan", "january", "feb", "february", "march",
                           "mar", "april", "apr", "may", "june", "jun", "july",
                           "jul", "august", "aug", "september", "sep",
                           "october", "oct", "november", "nov", "december",
                           "dec" };
        // TODO: month max. days?

        TaskParam field = getTaskParam(addFields, string);
        String text = field.getField();

        String dDay;
        String dMonth;
        String dYear;

        /*
         * cases: (1) 20052014 (2) 20.05.2014 (3) 20-05-2014 (4) 20/05/2014 (5)
         * 20/05 (6) 20 May (7) 20 May 2014
         */

    }

    private static boolean containsParam(List<TaskParam> addFields, String pName) {
        boolean result = false;
        for (TaskParam tp : addFields) {
            if (tp.getName().equals(pName)) {
                result = true;
            }
        }
        return result;
    }

    private static boolean isEditParamName(String toCheck) {
        return isAddParamName(toCheck) ||
               toCheck.toLowerCase().equals("delete:");
    }

    private static boolean containsParamName(String str) {
        boolean result = false;

        for (String name : TASK_PARAM_LIST_COLON) {
            if (str.toLowerCase().startsWith(name) &&
                str.length() > name.length()) {
                result = true;
            }
        }

        return result;
    }

    private static boolean containsDeleteParam(String str) {
        String delete = "delete:";
        if (str.toLowerCase().startsWith(delete) &&
            str.length() > delete.length()) {
            return true;
        }

        return false;
    }

    public static boolean isInteger(String str) {
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

    private static <E> void removeDuplicates(List<E> list) {
        List<E> toDelete = new ArrayList<E>();

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).equals(list.get(j))) {
                    toDelete.add(list.get(j));
                    break;
                }
            }
        }

        for (E item : toDelete) {
            list.remove(item);
        }
    }

    private static boolean isHelpParam(String string) {
        return Arrays.asList(HELP_CMD_LIST).contains(string.toLowerCase());
    }

    private static String getParamName(String currWord) {
        String validParamName = removeLastChar(currWord).toLowerCase();
        String longParamName = convertIfShorthand(validParamName);
        return longParamName;
    }

    private static TaskParam getTaskParam(List<TaskParam> fields,
                                          String currField) {
        // Attempt to get TaskParam named currField from List
        for (TaskParam tp : fields) {
            if (tp.getName().equals(currField)) {
                return tp;
            }
        }

        // If not found, create it
        TaskParam newParam = new TaskParam(currField, "");
        fields.add(newParam);
        return newParam;

    }

    private static String convertIfShorthand(String currField) {
        switch (currField) {
            case "n":
                return "name";
            case "d":
                return "due";
            case "s":
                return "start";
            case "e":
                return "end";
        }
        return currField;
    }

    private static boolean isAddParamName(String str) {
        return Arrays.asList(TASK_PARAM_LIST_COLON).contains(str.toLowerCase());
    }

    private static String removeLastChar(String word) {
        return word.substring(0, word.length() - 1);
    }

    private static boolean hasValidHashTag(String word) {
        return word.startsWith("#") && (word.length() > 1);
    }
    // ========== DATE/TIME BASED METHODS ==========//
    

    // ========== TASK PARSING METHODS ==========//
    public static Task parseToTask(String text) {
        String[] textItems = text.trim().split(" ");
        String[] param = new String[] { "", "", "", "" };
        List<String> tags = new ArrayList<String>();

        String currField = "name";
        boolean isDone = false;

        for (int i = 0; i < textItems.length; i++) {
            String currWord = textItems[i];
            String currWordLC = currWord.toLowerCase();
            if (isAddParamName(currWord)) {
                currField = getParamName(currWord.toLowerCase());
            } else if (hasValidHashTag(currWord)) {
                if (currWordLC.equals("#todo")) {
                    isDone = false;
                } else if (currWordLC.equals("#done")) {
                    isDone = true;
                } else {
                    tags.add(currWord);
                }
            } else {
                int pIndex = getParamIndex(currField);
                if (param[pIndex].isEmpty()) {
                    param[pIndex] = param[pIndex].concat(currWord);
                } else {
                    param[pIndex] = param[pIndex].concat(" " + currWord);
                }
            }
        }

        Task newTask = new Task(param[0], param[1], param[2], param[3], tags);
        newTask.setDone(isDone);
        return newTask;
    }

    private static int getParamIndex(String currField) {
        switch (currField.toLowerCase()) {
            case "name":
                return 0;
            case "due":
                return 1;
            case "start":
                return 2;
            case "end":
                return 3;
            default:
                System.out.println("raw-parsing getParamIndex failure");
                return -1;
        }
    }

}
