package Parser;

import java.util.ArrayList;
import java.util.Arrays;

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
    private static final String TYPE_BLOCK = "block";
    private static final String TYPE_UNBLOCK = "unblock";
    private static final String TYPE_DONE = "done";
    private static final String TYPE_TODO = "todo";
    private static final String TYPE_UNDO = "undo";
    private static final String TYPE_REDO = "redo";
    private static final String TYPE_CLEAR = "clear";
    private static final String TYPE_JOKE = "joke";
    private static final String TYPE_EXIT = "exit";

    // TODO: CONSIDER USING PARAM_FIRST_WORD = 1
    private static final String[] TASK_PARAM_LIST_COLON = { "name:", "n:",
                                                           "more:", "m:",
                                                           "due:", "d:",
                                                           "start:", "s:",
                                                           "end:", "e:",
                                                           "priority:", "p:" };
    private static final String[] TASK_PARAM_LIST = { "name", "n", "more", "m",
                                                     "due", "d", "start", "s",
                                                     "end", "e", "priority",
                                                     "p" };
    private static final String[] HELP_CMD_LIST = { TYPE_ALL, TYPE_ADD,
                                                   TYPE_EDIT, TYPE_DELETE,
                                                   TYPE_RESTORE, TYPE_SEARCH,
                                                   TYPE_DISPLAY, TYPE_BLOCK,
                                                   TYPE_UNBLOCK, TYPE_DONE,
                                                   TYPE_TODO, TYPE_UNDO,
                                                   TYPE_REDO, TYPE_CLEAR,
                                                   TYPE_JOKE, TYPE_EXIT };

    // ========== MAIN PARSE METHOD ==========//

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
                return parseTodo(commandItems);

            case TYPE_UNDO:
            case TYPE_REDO:
            case TYPE_CLEAR:
            case TYPE_JOKE:
            case TYPE_EXIT:
                return new CommandOthers(commandType);

            default:
                return new CommandOthers("error",
                        "Error in initial command parsing");
        }
    }

    // ========== INDIVIDUAL PARSE-COMMAND FUNCTIONS ==========//

    private static Command parseTodo(String[] commandItems) {
        ArrayList<TaskParam> todoFields = new ArrayList<TaskParam>();

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
        ArrayList<TaskParam> doneFields = new ArrayList<TaskParam>();

        try {
            String firstWord = commandItems[1];
            String firstWordLC = firstWord.toLowerCase();
            if (firstWordLC.equals("all")) {
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
        ArrayList<TaskParam> unblockFields = new ArrayList<TaskParam>();

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
        ArrayList<TaskParam> blockFields = new ArrayList<TaskParam>();

        // TODO: Consider date format (length 1, 2, 3?)
        try {
            /*
             * String firstWord = commandItems[1]; String firstWordLC =
             * firstWord.toLowerCase(); if (isDate(firstWord)) {
             * blockFields.add(new TaskParam("rangeType", firstWordLC)); } else
             * if (isInteger(firstWord)) { blockFields.add(new
             * TaskParam("rangeType", "id")); blockFields.add(new
             * TaskParam("id", firstWord)); } else { return new
             * CommandOthers("error", "Invalid argument for block"); }
             */
        } catch (ArrayIndexOutOfBoundsException e) {
            return new CommandOthers("error", "No arguments for block");
        }

        return new CommandBlock(blockFields);
    }

    private static Command parseDisplay(String[] commandItems) {
        ArrayList<TaskParam> displayFields = new ArrayList<TaskParam>();

        // CONSIDER: No arguments "display"?
        // TODO: MERGE DISPLAY, DELETE, RESTORE?
        try {
            String firstWord = commandItems[1];
            String firstWordLC = firstWord.toLowerCase();
            if (firstWordLC.equals("all") || firstWordLC.equals("block")) {
                displayFields.add(new TaskParam("rangeType", firstWordLC));
            } else if (isInteger(firstWord)) {
                displayFields.add(new TaskParam("rangeType", "id"));
                displayFields.add(new TaskParam("id", firstWord));
            } else {
                return new CommandOthers("error",
                        "Invalid argument for display");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return new CommandOthers("error", "No arguments for display");
        }

        return new CommandDisplay(displayFields);
    }

    private static Command parseSearch(String[] commandItems) {
        ArrayList<TaskParam> searchFields = new ArrayList<TaskParam>();

        // TODO: Add dates
        try {
            if (commandItems.length < 2) {
                throw new ArrayIndexOutOfBoundsException();
            }

            boolean donenessIndicated = false;
            for (int i = 1; i < commandItems.length; i++) {
                String currWord = commandItems[i];
                String currWordLC = currWord.toLowerCase();
                if (hasValidHashTag(currWord)) {
                    // TODO: Error Handling for multiple doneness keywords
                    if (currWordLC.equals("#done")) {
                        if (!donenessIndicated) {
                            searchFields.add(new TaskParam("tag", currWordLC));
                            donenessIndicated = true;
                        } else {
                            System.out
                                    .println("A done-ness keyword has already been indicated!");
                        }
                    } else if (currWordLC.equals("#todo")) {
                        if (!donenessIndicated) {
                            searchFields.add(new TaskParam("tag", currWordLC));
                            donenessIndicated = true;
                        } else {
                            System.out
                                    .println("A done-ness keyword has already been indicated!");
                        }
                    } else if (currWordLC.equals("#deleted")) {
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
        ArrayList<TaskParam> restoreFields = new ArrayList<TaskParam>();

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
        ArrayList<TaskParam> deleteFields = new ArrayList<TaskParam>();

        try {
            String firstWord = commandItems[1];
            if (isDeleteParamName(firstWord.toLowerCase())) {
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
        String id;
        ArrayList<TaskParam> editFields = new ArrayList<TaskParam>();

        // Check Edit ID
        if (commandItems.length > 1 && isInteger(commandItems[1])) {
            id = commandItems[1];
            editFields.add(new TaskParam("id", id));
        } else {
            return new CommandOthers("error", "Invalid id for edit");
        }

        for (int i = 2; i < commandItems.length; i++) {
            String currWord = commandItems[i];
            String currWordLC = currWord.toLowerCase();

            if (containsParamName(currWord) || containsDeleteParam(currWord)) {
                String[] wordList = currWord.split(":");
                int lastValidField = 0;
                // Check only until the second last word
                for (int j = 0; j < wordList.length - 1; j++) {
                    String toCheck = wordList[j] + ":";
                    if (isEditParamName(toCheck)) {
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
            } else if (isAddParamName(currWord) || currWordLC.equals("delete:")) {
                currField = getParamName(currWord);
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
        ArrayList<TaskParam> addFields = new ArrayList<TaskParam>();

        for (int i = 1; i < commandItems.length; i++) {
            String currWord = commandItems[i];
            if (containsParamName(currWord)) {
                String[] wordList = currWord.split(":");
                int lastValidField = 0;
                // Check only until the second last word
                for (int j = 0; j < wordList.length - 1; j++) {
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

    private static void convertToDate(ArrayList<TaskParam> addFields,
                                      String string) {
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

    private static boolean containsParam(ArrayList<TaskParam> addFields,
                                         String pName) {
        boolean result = false;
        for (TaskParam tp : addFields) {
            if (tp.getName().equals(pName)) {
                result = true;
            }
        }
        return result;
    }

    private static boolean isEditParamName(String toCheck) {
        return isAddParamName(toCheck) || toCheck.equals("delete:");
    }

    private static boolean containsParamName(String str) {
        boolean result = false;

        for (String name : TASK_PARAM_LIST_COLON) {
            if (str.startsWith(name) && str.length() > name.length()) {
                result = true;
            }
        }

        return result;
    }

    private static boolean containsDeleteParam(String str) {
        String delete = "delete";
        if (str.startsWith(delete) && str.length() > delete.length()) {
            return true;
        }

        return false;
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

    private static <E> void removeDuplicates(ArrayList<E> list) {
        ArrayList<E> toDelete = new ArrayList<E>();

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

    private static TaskParam getTaskParam(ArrayList<TaskParam> fields,
                                          String currField) {
        // Attempt to get TaskParam named currField from ArrayList
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
        return Arrays.asList(TASK_PARAM_LIST_COLON).contains(str.toLowerCase());
    }

    private static String removeLastChar(String word) {
        return word.substring(0, word.length() - 1);
    }

    private static boolean hasValidHashTag(String word) {
        return word.startsWith("#") && (word.length() > 1);
    }

    // ========== TASK PARSING METHODS ==========//
    public static Task parseToTask(String text) {
        String[] textItems = text.trim().split(" ");
        String[] param = new String[] { "", "", "", "", "", "" };
        ArrayList<String> tags = new ArrayList<String>();

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

        Task newTask = new Task(param[0], param[1], param[2], param[3],
                param[4], param[5], tags);
        newTask.setDone(isDone);
        return newTask;
    }

    private static int getParamIndex(String currField) {
        switch (currField.toLowerCase()) {
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
                System.out.println("raw-parsing getParamIndex failure");
                return -1;
        }
    }

}
