package parser;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import logic.Command;
import logic.CommandAdd;
import logic.CommandBlock;
import logic.CommandDelete;
import logic.CommandDisplay;
import logic.CommandDone;
import logic.CommandEdit;
import logic.CommandHelp;
import logic.CommandOthers;
import logic.CommandRestore;
import logic.CommandSearch;
import logic.CommandTodo;
import logic.CommandUnblock;
import database.BlockDate;
import database.DateTime;
import database.Task;

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
        assert (input != null);

        // The Parser will analyse the input word by word
        String[] commandItems = input.trim().split(" ");

        if (commandItems.length > 0) {
            // First word of command should be command type.
            // The rest of the words in the String are parameters.
            String commandType = getCommandWord(commandItems);
            String[] commandParams = removeCommandWord(commandItems);

            switch (commandType) {
                case TYPE_HELP:
                    return parseHelp(commandParams);

                case TYPE_ADD:
                    return parseAdd(commandParams);

                case TYPE_EDIT:
                    return parseEdit(commandParams);

                case TYPE_DELETE:
                    return parseDelete(commandParams);

                case TYPE_RESTORE:
                    return parseRestore(commandParams);

                case TYPE_SEARCH:
                    return parseSearch(commandParams);

                case TYPE_DISPLAY:
                case TYPE_SHOW:
                    return parseDisplay(commandParams);

                case TYPE_BLOCK:
                    return parseBlock(commandParams);

                case TYPE_UNBLOCK:
                    return parseUnblock(commandParams);

                case TYPE_DONE:
                    return parseDone(commandParams);

                case TYPE_TODO:
                    return parseTodo(commandParams);

                case TYPE_UNDO:
                case TYPE_REDO:
                case TYPE_EXIT:
                    return new CommandOthers(commandType);
            }
        }
        return new CommandOthers("error", "Error in initial command parsing");
    }

    /**
     * Returns the command word from a given string array. The command word is
     * assumed to be the first item of the array, and is returned in lower-case.
     * 
     * @param commandItems
     *            An array containing a command input split by spaces
     * @return String containing command word in lower-case
     */
    private static String getCommandWord(String[] commandItems) {
        return commandItems[0].toLowerCase();
    }

    /**
     * @param commandItems
     * @return
     */
    private static String[] removeCommandWord(String[] commandItems) {
        try {
            String[] commandParams = new String[commandItems.length - 1];
            for (int i = 1; i < commandItems.length; i++) {
                commandParams[i - 1] = commandItems[i];
            }
            return commandParams;
        } catch (Exception e) {
            return new String[0];
        }
    }

    // ========== INDIVIDUAL PARSE-COMMAND FUNCTIONS ==========//

    private static Command parseTodo(String[] commandParams) {
        List<TaskParam> todoFields = new ArrayList<TaskParam>();

        try {
            String firstWord = commandParams[0];
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

    private static Command parseDone(String[] commandParams) {
        List<TaskParam> doneFields = new ArrayList<TaskParam>();

        try {
            String firstWord = commandParams[0];
            String firstWordLC = firstWord.toLowerCase();

            if (DateParser.isValidDate(firstWord)) {
                doneFields.add(new TaskParam("rangeType", "date"));
                doneFields.add(new TaskParam("date", firstWord));
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

    private static Command parseUnblock(String[] commandParams) {
        List<TaskParam> unblockFields = new ArrayList<TaskParam>();

        try {
            String firstWord = commandParams[0];
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

    private static Command parseBlock(String[] commandParams) {
        if (commandParams.length == 0) {
            throw new IllegalArgumentException(
                    "You can't block nothing! Input a date or time frame!");
        }

        List<TaskParam> blockFields = new ArrayList<TaskParam>();

        boolean hasConnector = false;
        boolean hasFirstDate = false;
        boolean hasFirstTime = false;
        boolean hasSecondDate = false;
        boolean hasSecondTime = false;

        String firstDateStr = "";
        String secondDateStr = "";
        String firstTimeStr = "";
        String secondTimeStr = "";

        // Collect fields; each date and each time should only be filled once.
        int index;
        for (index = 0; index < commandParams.length; index++) {
            String currWord = commandParams[index];
            if (!hasConnector && currWord.equalsIgnoreCase("to")) {
                hasConnector = true;
            } else if (DateParser.isValidDate(currWord)) {
                if (!hasConnector && !hasFirstDate) {
                    firstDateStr = currWord;
                    hasFirstDate = true;
                } else if (hasConnector && !hasSecondDate) {
                    secondDateStr = currWord;
                    hasSecondDate = true;
                } else {
                    throw new IllegalArgumentException(
                            "That format for multiple dates is not accepted!");
                }
            } else if (DateParser.isValidTime(currWord)) {
                if (!hasConnector && !hasFirstTime) {
                    firstTimeStr = currWord;
                    hasFirstTime = true;
                } else if (hasConnector && !hasSecondTime) {
                    secondTimeStr = currWord;
                    hasSecondTime = true;
                } else {
                    throw new IllegalArgumentException(
                            "That format for multiple times is not accepted!");
                }
            } else if (currWord.isEmpty()) {
                // An empty string accommodates for multiple spaces
                continue;
            } else {
                // Stop checking upon reaching an invalid word.
                break;
            }
        }

        // Save leftover strings
        // TODO: Check if this is necessary/can be used.
        // Should be done for add and edit?
        int lastIndex = index;
        String leftovers = "";
        if (lastIndex < commandParams.length) {
            for (int i = lastIndex; i < commandParams.length; i++) {
                leftovers = leftovers + " " + commandParams[i];
            }
            leftovers = leftovers.trim();
        }

        // Check if any fields have been input
        if (!hasFirstDate && !hasFirstTime && !hasSecondDate && !hasSecondTime) {
            throw new IllegalArgumentException(
                    "No date or time detected! "
                            + "Please input a date/time range to block!");
        }

        // Fill in empty dates
        if (!hasFirstDate && !hasSecondDate) {
            firstDateStr = DateParser.getCurrDateStr();
            secondDateStr = firstDateStr;
        } else if (!hasFirstDate) {
            // TODO: Based on time stated
            firstDateStr = secondDateStr;
        } else if (!hasSecondDate) {
            secondDateStr = firstDateStr;
        }

        // Check order of date/times; switch if necessary.
        DateTime dateTime1 = new DateTime(firstDateStr, firstTimeStr);
        DateTime dateTime2 = new DateTime(secondDateStr, secondTimeStr);
        TaskParam startTp;
        TaskParam endTp;
        if (dateTime1.isEarlierThan(dateTime2) || dateTime1.equalsTo(dateTime2)) {
            startTp = new TaskParam("start", dateTime1.toString());
            endTp = new TaskParam("end", dateTime2.toString());
            // Fill in empty times
            if (!hasFirstTime) {
                startTp.addToField("0000");
            }
            if (!hasSecondTime) {
                endTp.addToField("2359");
            }
        } else {
            startTp = new TaskParam("start", dateTime2.toString());
            endTp = new TaskParam("end", dateTime1.toString());
            // Fill in empty times
            if (!hasFirstTime) {
                endTp.addToField("2359");
            }
            if (!hasSecondTime) {
                startTp.addToField("0000");
            }
        }

        // Create and return Command
        blockFields.add(startTp);
        blockFields.add(endTp);
        return new CommandBlock(blockFields);
    }

    private static Command parseDisplay(String[] commandParams) {
        List<TaskParam> displayFields = new ArrayList<TaskParam>();

        if (commandParams.length == 0) {
            displayFields.add(new TaskParam("rangeType", "all"));
        } else {
            String firstWord = commandParams[0];
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
        }

        return new CommandDisplay(displayFields);
    }

    private static Command parseDelete(String[] commandParams) {
        List<TaskParam> deleteFields = new ArrayList<TaskParam>();

        try {
            String firstWord = commandParams[0];
            if (DateParser.isValidDate(firstWord)) {
                // TODO: Remove start/end for most Commands
                deleteFields.add(new TaskParam("rangeType", "dates"));
                deleteFields.add(new TaskParam("start", firstWord));
                deleteFields.add(new TaskParam("end", firstWord));
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

    private static Command parseRestore(String[] commandParams) {
        List<TaskParam> restoreFields = new ArrayList<TaskParam>();

        try {
            String firstWord = commandParams[0];
            if (isInteger(firstWord)) {
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

    private static Command parseSearch(String[] commandParams) {
        List<TaskParam> searchFields = new ArrayList<TaskParam>();

        // TODO: REFACTOR
        try {
            if (commandParams.length == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }

            boolean donenessIndicated = false;
            boolean dateIndicated = false;
            for (int i = 0; i < commandParams.length; i++) {
                String currWord = commandParams[i];
                String currWordLC = currWord.toLowerCase();
                if (DateParser.isValidDate(currWord)) {
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

    private static Command parseEdit(String[] commandParams) {
        String currField = "name";
        String prevField = currField;
        String id;
        List<TaskParam> editFields = new ArrayList<TaskParam>();

        // Check Edit ID
        // TODO: Note that currently all non-delete parameters will be thrown to
        // "name:" if "delete" is the current field. Also, delete has no
        // shorthand.

        // try saveEditIdToField() catch return CommandOthers
        if (commandParams.length > 0 && isInteger(commandParams[0])) {
            id = commandParams[0];
            editFields.add(new TaskParam("id", id));
        } else {
            return new CommandOthers("error", "Invalid id for edit");
        }

        for (int i = 1; i < commandParams.length; i++) {
            String currWord = commandParams[i];
            String currWordLC = currWord.toLowerCase();

            if (isAddParamName(currWord) || currWordLC.equals("delete:")) {
                prevField = currField;
                currField = getParamName(currWord);
            } else if (containsParamName(currWord) ||
                       containsDeleteParam(currWord)) {
                String[] wordList = currWord.split(":");
                int endIndex = getLastPossibleParamIndex(currWord, wordList);

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

                String toAddToField = mergeWordsAfterIndex(wordList,
                                                           lastValidField);
                addToFieldParam(editFields, currField, toAddToField);
            } else if (hasValidHashTag(currWord)) {
                addTaskParamToField(editFields, "tag", currWord);
            } else if (currField.equals("delete")) {
                // check if it's a valid delete keyword (ignore otherwise)
                if (Arrays.asList(TASK_PARAM_LIST).contains(currWordLC)) {
                    // check for duplicate words (TODO: unnecessary?)
                    TaskParam deleteParam = getTaskParam(editFields, currField);
                    if (!deleteParam.getField().contains(currWordLC)) {
                        deleteParam.addToField(currWordLC);
                    }
                } else if (hasValidHashTag(currWord)) {
                    // What if an edited field is deleted?
                    // Refactor
                    TaskParam deleteParam = getTaskParam(editFields, currField);
                    if (!deleteParam.getField().contains(currWord)) {
                        deleteParam.addToField(currWord);
                    }
                } else {
                    addToFieldParam(editFields, prevField, currWord);
                }
            } else {
                addToFieldParam(editFields, currField, currWord);
            }
        }

        removeDuplicates(editFields);
        removeInvalidDateTimes(editFields);
        checkStartEndOrder(editFields);

        return new CommandEdit(editFields);
    }

    private static Command parseHelp(String[] commandParams) {
        String helpField = null;

        // TODO: change CommandHelp to process "invalid" as Command.ERROR?
        if (commandParams.length > 0) {
            if (isHelpParam(commandParams[0])) {
                helpField = commandParams[0].toLowerCase();
            } else {
                helpField = "invalid";
            }
        }

        return new CommandHelp(helpField);
    }

    private static Command parseAdd(String[] commandParams) {
        String currField = "name";
        List<TaskParam> addFields = new ArrayList<TaskParam>();

        for (int i = 0; i < commandParams.length; i++) {
            String currWord = commandParams[i];
            if (containsParamName(currWord)) {
                // Split up all parameter names
                String[] wordList = currWord.split(":");

                // Get the last valid parameter, and the index in wordList it
                // corresponds to. EndIndex checks if the last word in wordList
                // can
                // be a parameter name.
                int endIndex = getLastPossibleParamIndex(currWord, wordList);
                currField = getLastValidParamName(wordList, endIndex, currField);
                int lastValidField = getLastValidParamNameIndex(wordList,
                                                                endIndex);

                // Add remainder to the current field
                String toAddToField = mergeWordsAfterIndex(wordList,
                                                           lastValidField);
                addToFieldParam(addFields, currField, toAddToField);
            } else if (isAddParamName(currWord)) {
                currField = getParamName(currWord);
            } else if (hasValidHashTag(currWord)) {
                addTaskParamToField(addFields, "tag", currWord);
            } else {
                addToFieldParam(addFields, currField, currWord);
            }
        }

        removeDuplicates(addFields);
        removeInvalidDateTimes(addFields);
        checkStartEndOrder(addFields);

        return new CommandAdd(addFields);
    }

    /**
     * @param originalStr
     * @param wordList
     * @return
     */
    private static int getLastPossibleParamIndex(String originalStr,
                                                 String[] wordList) {
        if (originalStr.endsWith(":")) {
            return wordList.length;
        } else {
            return wordList.length - 1;
        }
    }

    /**
     * @param wordList
     * @param endIndex
     * @param currField
     * @return
     */
    private static String getLastValidParamName(String[] wordList,
                                                int endIndex, String currField) {
        for (int j = 0; j < endIndex; j++) {
            if (isAddParamName(wordList[j] + ":")) {
                currField = getParamName(wordList[j] + ":");
            } else {
                break;
            }
        }
        return currField;
    }

    /**
     * @param wordList
     * @param endIndex
     * @return
     */
    private static int getLastValidParamNameIndex(String[] wordList,
                                                  int endIndex) {
        int lastValidField = 0;
        for (int j = 0; j < endIndex; j++) {
            if (isAddParamName(wordList[j] + ":")) {
                lastValidField = j;
            } else {
                break;
            }
        }
        return lastValidField;
    }

    /**
     * @param wordList
     * @param index
     * @return
     */
    private static String mergeWordsAfterIndex(String[] wordList, int index) {
        String toAddToField = "";
        for (int k = index + 1; k < wordList.length; k++) {
            toAddToField = toAddToField.concat(wordList[k]);
            if (k != wordList.length - 1) {
                toAddToField = toAddToField.concat(":");
            }
        }
        return toAddToField;
    }

    /**
     * @param fields
     * @param field
     * @param content
     */
    private static void addToFieldParam(List<TaskParam> fields, String field,
                                        String content) {
        getTaskParam(fields, field).addToField(content);
    }

    /**
     * @param fields
     * @param paramName
     * @param paramField
     * @return
     */
    private static boolean addTaskParamToField(List<TaskParam> fields,
                                               String paramName,
                                               String paramField) {
        return fields.add(new TaskParam(paramName, paramField));
    }

    /**
     * @param fields
     */
    private static void removeInvalidDateTimes(List<TaskParam> fields) {
        String[] dateParams = new String[] { "due", "start", "end" };
        for (int i = 0; i < dateParams.length; i++) {
            String currParamName = dateParams[i];
            TaskParam currParam = getTaskParam(fields, currParamName);
            if (!DateParser.isValidDateTime(currParam.getField())) {
                fields.remove(currParam);
            }
        }
    }

    /**
     * @param fields
     */
    private static void checkStartEndOrder(List<TaskParam> fields) {
        TaskParam startTP = getTaskParam(fields, "start");
        TaskParam endTP = getTaskParam(fields, "end");
        if (!startTP.getField().isEmpty() && !endTP.getField().isEmpty()) {
            DateTime startDT = DateParser.parseToDateTime(startTP.getField());
            DateTime endDT = DateParser.parseToDateTime(endTP.getField());
            if (startDT.compareTo(endDT) > 0) {
                fields.remove(startTP);
                fields.remove(endTP);
                fields.add(new TaskParam("end", startTP.getField()));
                fields.add(new TaskParam("start", endTP.getField()));
            }
        }
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
                // NOTE: currWord.isEmpty() is to make sure the parser does not
                // add " " for each empty string
                // TODO: check other cases for this.
                if (param[pIndex].isEmpty() || currWord.isEmpty()) {
                    param[pIndex] = param[pIndex].concat(currWord);
                } else {
                    param[pIndex] = param[pIndex].concat(" " + currWord);
                }
            }
        }

        // Assign proper names and check if date values are null or invalid
        DateTime[] dateTimes = new DateTime[3];

        for (int i = 1; i <= 3; i++) {
            try {
                if (param[i].equals("null")) {
                    dateTimes[i - 1] = new DateTime();
                } else {
                    dateTimes[i - 1] = DateParser.parseToDateTime(param[i]);
                }
            } catch (IllegalArgumentException e) {
                // Will be reached if user keyed in invalid date
                dateTimes[i - 1] = null;
                System.out.println("Invalid date input detected!");
            }
        }

        // TODO: REORGANISE
        String name = param[0];
        DateTime due = dateTimes[0];
        DateTime start = dateTimes[1];
        DateTime end = dateTimes[2];

        Task newTask = new Task(name, due, start, end, tags);
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

    // ========== BLOCK PARSING METHODS ==========//

    /**
     * Forms a <code>BlockDate</code> object by parsing a <code>String</code>
     * containing the saved string literals of two <code>DateTime</code> objects
     * (starting and ending dates and times).
     * <p>
     * Note: the input <code>String</code> must be of the given format(below),
     * containing two pairs of dates and times, and the word "to" between them.
     * All 5 items must have spaces between them.
     * 
     * @param text
     *            format: "{@literal <date> <time> to <date> <time>}"
     * 
     */
    public static BlockDate parseToBlock(String text) {
        String[] fields = text.split(" ");
        assert (fields.length == 5) : "Invalid number of terms";
        assert (fields[2].equals("to")) : "Missing \"to\"";

        String startDate = fields[0];
        String startTime = fields[1];
        String endDate = fields[3];
        String endTime = fields[4];
        assert (DateParser.isValidDate(startDate) && DateParser
                .isValidTime(startTime)) : "Invalid start DateTime saved";
        assert (DateParser.isValidDate(endDate) && DateParser
                .isValidTime(endTime)) : "invalid end DateTime saved";

        DateTime startDt = new DateTime(startDate, startTime);
        DateTime endDt = new DateTime(endDate, endTime);

        return new BlockDate(startDt, endDt);
    }

}
