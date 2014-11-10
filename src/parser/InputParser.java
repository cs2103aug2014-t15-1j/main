package parser;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import objects.DateTime;
import objects.TaskParam;
import logic.Command;
import logic.CommandAdd;
import logic.CommandBlock;
import logic.CommandDelete;
import logic.CommandDisplay;
import logic.CommandDone;
import logic.CommandEdit;
import logic.CommandOthers;
import logic.CommandRestore;
import logic.CommandSearch;
import logic.CommandTodo;

/**
 * The InputParser handles the parsing of Strings input by the user. It is
 * mostly self-contained, but relies on DateParser for date-related methods.
 */
//@author A0116208N
public class InputParser {

    // All possible Command types (in string)
    private static final String TYPE_HELP = "help";
    private static final String TYPE_GOTTA = "gotta";
    private static final String TYPE_ADD = "add";
    private static final String TYPE_EDIT = "edit";
    private static final String TYPE_DELETE = "delete";
    private static final String TYPE_RESTORE = "restore";
    private static final String TYPE_SEARCH = "search";
    private static final String TYPE_DISPLAY = "display";
    private static final String TYPE_SHOW = "show";
    private static final String TYPE_BLOCK = "block";
    private static final String TYPE_DONE = "done";
    private static final String TYPE_TODO = "todo";
    private static final String TYPE_UNDO = "undo";
    private static final String TYPE_REDO = "redo";
    private static final String TYPE_EXIT = "exit";
    private static final String TYPE_RESET = "reset";

    private static final String[] STR_ARRAY_EMPTY = new String[0];

    private static final String[] PARAMS_EDIT = { "due", "start", "tags" };
    private static final String[] PARAMS_BLOCK = { "from", "to" };
    private static final String[] PARAMS_DATE = { "due", "start" };
    private static final String[] PARAMS_DATE_FULL = { "due", "by", "start",
                                                      "from", "end", "to" };
    private static final String[] PARAMS_DISPLAY = { "search", "all", "done",
                                                    "deleted", "block",
                                                    "today", "tomorrow",
                                                    "upcoming", "someday" };
    private static final String[] PARAMS_DELETE = { "all", "search", "done" };
    private static final String[] PARAMS_STATUS = { "done", "deleted", "all" };

    // ========== MAIN PARSE METHOD ==========//

    /**
     * Parses the input String into a Command of the relevant type. The Command
     * will store relevant information contained in the String.
     * 
     * @return Command object of the relevant subclass
     * @throws IllegalArgumentException
     *             when a user input is invalid. The exception will contain a
     *             message related to the error.
     */
    static Command parse(String input) throws IllegalArgumentException {
        // Note: The parser is built to analyse input on a word-by-word basis.
        assert (input != null);

        // Split by spaces (break into "words"), ignoring empty Strings that
        // result from repeated spaces
        String[] commandItems = removeEmptyStrings(input.split(" "));

        if (commandItems.length > 0) {
            String commandType = getCommandWord(commandItems);
            String[] commandParams = removeFirstWord(commandItems);

            switch (commandType) {
                case TYPE_ADD:
                case TYPE_GOTTA:
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

                case TYPE_DONE:
                    return parseDone(commandParams);

                case TYPE_TODO:
                    return parseTodo(commandParams);

                case TYPE_UNDO:
                case TYPE_REDO:
                case TYPE_RESET:
                case TYPE_HELP:
                case TYPE_EXIT:
                    return new CommandOthers(commandType);
            }
        }
        throw new IllegalArgumentException("Invalid command!");
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
     * Returns a clone of the input String array, excluding the first word. This
     * is used to remove the command word, which is assumed to be the first item
     * of the array.
     * 
     * @return An array smaller than the input array by 1. Minimum size is 0.
     */
    private static String[] removeFirstWord(String[] commandItems) {
        try {
            String[] commandParams = new String[commandItems.length - 1];
            for (int i = 1; i < commandItems.length; i++) {
                commandParams[i - 1] = commandItems[i];
            }
            return commandParams;
        } catch (Exception e) {
            return STR_ARRAY_EMPTY;
        }
    }

    /**
     * Returns a clone of the input String array, excluding empty Strings. This
     * gets rid of repeated spaces in a user's input.
     * 
     * @return String array excluding empty strings. Minimum size is 0.
     * 
     */
    private static String[] removeEmptyStrings(String[] arr) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].isEmpty()) {
                list.add(arr[i]);
            }
        }

        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }

        return result;
    }

    // ========== INDIVIDUAL PARSE-COMMAND FUNCTIONS ==========//

    private static Command parseTodo(String[] commandParams) {
        List<TaskParam> todoFields = new ArrayList<TaskParam>();

        try {
            String param = commandParams[0];
            if (param.equalsIgnoreCase("last")) {
                addTaskParamToFields(todoFields, "rangeType", "last");
            } else if (isInteger(param)) {
                addTaskParamToFields(todoFields, "rangeType", "id");
                addTaskParamToFields(todoFields, "id", param);
            } else {
                throw new IllegalArgumentException("Invalid argument for todo!");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Missing arguments for todo!");
        }

        return new CommandTodo(todoFields);
    }

    private static Command parseDone(String[] commandParams) {
        List<TaskParam> doneFields = new ArrayList<TaskParam>();

        try {
            String firstWord = commandParams[0];
            if (DateParser.isValidDate(firstWord)) {
                addTaskParamToFields(doneFields, "rangeType", "date");
                addTaskParamToFields(doneFields, "date", firstWord);
            } else if (firstWord.equalsIgnoreCase("all")) {
                addTaskParamToFields(doneFields, "rangeType", "all");
            } else if (isInteger(firstWord)) {
                addTaskParamToFields(doneFields, "rangeType", "id");
                addTaskParamToFields(doneFields, "id", firstWord);
            } else {
                throw new IllegalArgumentException("Invalid argument for done!");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("No arguments for done");
        }

        return new CommandDone(doneFields);
    }

    private static Command parseBlock(String[] commandParams) {
        if (commandParams.length == 0) {
            throw new IllegalArgumentException(
                    "You can't block nothing! Input a date or time frame!");
        }

        String currFieldOrig = "";
        String currField = "";
        List<TaskParam> blockFields = new ArrayList<TaskParam>();
        ArrayList<String> availParams = generateParamArrayList(PARAMS_BLOCK);

        String currWord;
        boolean currHasDate = false;
        boolean currHasTime = false;

        for (int j = 0; j < commandParams.length; j++) {
            currWord = commandParams[j];
            if (hasValidHashTag(currWord)) {
                // No matter the current field, collect hashtags first
                blockFields.add(new TaskParam("tag", currWord));
            } else if (currField.equalsIgnoreCase(currWord) &&
                       !currWord.isEmpty() && !currHasDate && !currHasTime) {
                // If parameters are repeated, add the previous one to name
                addToFieldParam(blockFields, "name", currFieldOrig);
            } else if (availParams.contains(currWord.toLowerCase())) {
                // If the current word is an available parameter name
                if (isParamOf(PARAMS_BLOCK, currField) && !currHasDate &&
                    !currHasTime) {
                    // If the last parameter was not filled, it was not a
                    // parameter
                    addToFieldParam(blockFields, "name", currFieldOrig);
                    availParams.add(currField);
                }
                // Reassign currField values
                currField = currWord.toLowerCase();
                // Save the original word (with capitalisation)
                currFieldOrig = currWord;
                // Remove availability
                availParams.remove(currField);
                // Reset hasDate/Time values
                currHasDate = false;
                currHasTime = false;
            } else if (isParamOf(PARAMS_BLOCK, currField) &&
                       !currWord.isEmpty()) {
                // If currently a date parameter and string is not ""
                if (!currHasDate && DateParser.isValidDate(currWord)) {
                    addToFieldParam(blockFields, currField, currWord);
                    currHasDate = true;
                } else if (!currHasTime && DateParser.isValidTime(currWord)) {
                    addToFieldParam(blockFields, currField, currWord);
                    currHasTime = true;
                } else {
                    // If not a valid date/time, reset fields
                    // Add the parameter name to "name" if no date/time
                    // was assigned.
                    TaskParam nameParam = getTaskParam(blockFields, "name");
                    if (!currHasDate && !currHasTime) {
                        nameParam.addToField(currFieldOrig);
                        availParams.add(currField);
                    }
                    nameParam.addToField(currWord);
                    currField = "";
                    currFieldOrig = "";
                }
            } else {
                addToFieldParam(blockFields, "name", currWord);
            }
        }

        // catches if the last word was a parameter
        if (isParamOf(PARAMS_BLOCK, currField) && !currHasDate && !currHasTime) {
            addToFieldParam(blockFields, "name", currFieldOrig);
        }

        // Fill in empty dates
        TaskParam fromTp = getTaskParam(blockFields, "from");
        TaskParam toTp = getTaskParam(blockFields, "to");
        String fromStr = fromTp.getField();
        String toStr = toTp.getField();
        if (!DateParser.containsDate(fromStr) &&
            !DateParser.containsDate(toStr)) {
            String currDate = DateParser.getCurrDateStr();
            fromTp.addToField(currDate);
            toTp.addToField(currDate);
        } else if (!DateParser.containsDate(fromStr)) {
            String toDate = DateParser.getFirstDate(toStr);
            fromTp.addToField(toDate);
        } else if (!DateParser.containsDate(toStr)) {
            String fromDate = DateParser.getFirstDate(fromStr);
            toTp.addToField(fromDate);
        }

        // Check order of date/times; switch if necessary.
        // While checking, add the times as necessary
        fromStr = fromTp.getField();
        toStr = toTp.getField();
        DateTime fromDt = DateParser.parseToDateTime(fromStr);
        DateTime toDt = DateParser.parseToDateTime(toStr);
        if (fromDt.isLaterThan(toDt)) {
            if (fromDt.getDate().equals(toDt.getDate()) && (!DateParser
                    .containsTime(fromStr) || !DateParser.containsTime(toStr))) {
                // If the date is equal and at least one time is missing
                // Do nothing.
            } else {
                fromTp.setField(toStr);
                toTp.setField(fromStr);
            }

        }

        // Fill in empty times
        fromStr = fromTp.getField();
        toStr = toTp.getField();
        if (!DateParser.containsTime(fromStr)) {
            fromTp.addToField("0000");
        }
        if (!DateParser.containsTime(toStr)) {
            toTp.addToField("2359");
        }

        removeDuplicates(blockFields);

        return new CommandBlock(blockFields);

    }

    private static Command parseDisplay(String[] commandParams) {
        List<TaskParam> displayFields = new ArrayList<TaskParam>();

        if (commandParams.length == 0) {
            addTaskParamToFields(displayFields, "rangeType", "all");
        } else {
            String firstWord = commandParams[0];
            if (isParamOf(PARAMS_DISPLAY, firstWord)) {
                addTaskParamToFields(displayFields, "rangeType",
                                     firstWord.toLowerCase());
            } else if (isInteger(firstWord)) {
                addTaskParamToFields(displayFields, "rangeType", "id");
                addTaskParamToFields(displayFields, "id", firstWord);
            } else {
                throw new IllegalArgumentException(
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
                addTaskParamToFields(deleteFields, "rangeType", "dates");
                addTaskParamToFields(deleteFields, "start", firstWord);
                addTaskParamToFields(deleteFields, "due", firstWord);
            } else if (isParamOf(PARAMS_DELETE, firstWord)) {
                addTaskParamToFields(deleteFields, "rangeType",
                                     firstWord.toLowerCase());
            } else if (isInteger(firstWord)) {
                addTaskParamToFields(deleteFields, "rangeType", "id");
                addTaskParamToFields(deleteFields, "id", firstWord);
            } else {
                throw new IllegalArgumentException(
                        "Invalid argument for delete");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("No arguments for delete");
        }

        return new CommandDelete(deleteFields);
    }

    private static Command parseRestore(String[] commandParams) {
        List<TaskParam> restoreFields = new ArrayList<TaskParam>();

        for (int i = 0; i < commandParams.length; i++) {
            String word = commandParams[i];
            if (!word.isEmpty()) {
                if (isInteger(word)) {
                    restoreFields.add(new TaskParam("rangeType", "id"));
                    restoreFields.add(new TaskParam("id", word));
                    return new CommandRestore(restoreFields);
                } else {
                    throw new IllegalArgumentException(
                            "Invalid argument for restore");
                }
            }
        }

        throw new IllegalArgumentException("No arguments for restore");

    }

    private static Command parseSearch(String[] commandParams) {
        List<TaskParam> searchFields = new ArrayList<TaskParam>();

        try {
            // Get index of the first word
            int firstWordIndex = 0;
            String firstWord = commandParams[0];
            while (firstWord.isEmpty() && firstWordIndex < commandParams.length) {
                firstWordIndex++;
                firstWord = commandParams[firstWordIndex];
            }

            // Check if the first word is a valid status parameter
            int startIndex = firstWordIndex;
            if (isParamOf(PARAMS_STATUS, firstWord)) {
                addTaskParamToFields(searchFields, "status",
                                     firstWord.toLowerCase());
                startIndex = firstWordIndex + 1;
            }

            // Categorise the rest of the string
            boolean dateIndicated = false;
            for (int i = startIndex; i < commandParams.length; i++) {
                String currWord = commandParams[i];
                if (DateParser.isValidDate(currWord)) {
                    if (!dateIndicated) {
                        searchFields.add(new TaskParam("date", currWord));
                        dateIndicated = true;
                    } else {
                        throw new IllegalArgumentException(
                                "You can only allowed to search one date at a time!");
                    }
                } else if (hasValidHashTag(currWord)) {
                    searchFields.add(new TaskParam("tag", currWord));
                } else if (!currWord.isEmpty()) {
                    searchFields.add(new TaskParam("word", currWord));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("No arguments for search");
        }

        return new CommandSearch(searchFields);
    }

    private static Command parseEdit(String[] commandParams) {
        String currField = "";
        String currFieldOrig = "";
        String id;
        List<TaskParam> editFields = new ArrayList<TaskParam>();

        // TODO: Refactor to saveEditIdToField(); throw exceptions
        int firstWord = 0;
        try {
            id = commandParams[firstWord];
            while (id.isEmpty()) {
                firstWord++;
                id = commandParams[firstWord];
            }
            Integer.parseInt(id);
            editFields.add(new TaskParam("id", id));
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("No task specified for edit!");
        } catch (NumberFormatException f) {
            throw new IllegalArgumentException("Invalid task ID for edit!");
        }

        ArrayList<String> availDateParams = generateParamArrayList(PARAMS_DATE);
        ArrayList<String> availDeleteParams = generateParamArrayList(PARAMS_EDIT);

        String currWord;
        boolean currHasDate = false;
        boolean currHasTime = false;
        boolean currHasDelete = false;

        // TODO: Refactor
        for (int j = 1; j < commandParams.length; j++) {
            currWord = commandParams[j];
            if (hasValidHashTag(currWord)) {
                // No matter the current field, collect hashtags first
                editFields.add(new TaskParam("tag", currWord));
            } else if ((currField.equals(getDateParamEquiv(currWord)) || currField
                    .equalsIgnoreCase(currWord)) &&
                       !currWord.isEmpty() &&
                       !currHasDate && !currHasTime) {
                // If parameters are repeated, add the previous one to name
                addToFieldParam(editFields, "name", currFieldOrig);
            } else if (currField.equals("delete") && !currWord.isEmpty()) {
                // If the last parameter was a delete
                if (availDeleteParams.contains(getDateParamEquiv(currWord))) {
                    // If delete has not been filled and the currWord is valid
                    addTaskParamToFields(editFields, "delete",
                                         getDateParamEquiv(currWord));
                    currHasDelete = true;
                    availDeleteParams.remove(getDateParamEquiv(currWord));
                } else {
                    // Else assume delete was not intended as a param
                    addToFieldParam(editFields, "name", currFieldOrig);
                    addToFieldParam(editFields, "name", currWord);
                }
                // Delete resets after the first non-tag input
                currField = "";
                currFieldOrig = "";
            } else if (availDateParams.contains(getDateParamEquiv(currWord)) ||
                       currWord.equalsIgnoreCase("delete")) {
                // If the current word is an available parameter name
                if (isParamOf(PARAMS_DATE_FULL, currField) && !currHasDate &&
                    !currHasTime) {
                    // If the last parameter was not filled, it was not a
                    // parameter
                    addToFieldParam(editFields, "name", currFieldOrig);
                    availDateParams.add(currField);
                }
                if (currField.equals("delete") && !currHasDelete) {
                    addToFieldParam(editFields, "name", currFieldOrig);
                }
                // Reassign currField values
                currField = getDateParamEquiv(currWord);
                // Save the original word (with capitalisation)
                currFieldOrig = currWord;
                // Remove availability
                if (isParamOf(PARAMS_DATE_FULL, currField)) {
                    availDateParams.remove(currField);
                }
                // Reset boolean values
                currHasDate = false;
                currHasTime = false;
                currHasDelete = false;
            } else if (isParamOf(PARAMS_DATE_FULL, currField) &&
                       !currWord.isEmpty()) {
                // If currently a date parameter and string is not ""
                if (!currHasDate && DateParser.isValidDate(currWord)) {
                    addToFieldParam(editFields, currField, currWord);
                    currHasDate = true;
                } else if (!currHasTime && DateParser.isValidTime(currWord)) {
                    addToFieldParam(editFields, currField, currWord);
                    currHasTime = true;
                } else {
                    // If not a valid date/time, reset fields
                    // Add the parameter name to "name" if no date/time
                    // was assigned.
                    TaskParam nameParam = getTaskParam(editFields, "name");
                    if (!currHasDate && !currHasTime) {
                        nameParam.addToField(currFieldOrig);
                        availDateParams.add(currField);
                    }
                    nameParam.addToField(currWord);
                    currField = "";
                    currFieldOrig = "";
                }
            } else {
                addToFieldParam(editFields, "name", currWord);
            }
        }

        // catches if the last word was a parameter
        if ((isParamOf(PARAMS_DATE_FULL, currField) && !currHasDate && !currHasTime) ||
            (currField.equals("delete") && !currHasDelete)) {
            addToFieldParam(editFields, "name", currFieldOrig);
        }

        // Check for input time with missing date
        TaskParam startTP = getTaskParam(editFields, "start");
        TaskParam dueTP = getTaskParam(editFields, "due");
        String startStr = startTP.getField();
        String dueStr = dueTP.getField();
        if (DateParser.containsTime(startStr) &&
            !DateParser.containsDate(startStr)) {
            if (DateParser.containsDate(dueStr)) {
                String dueDate = DateParser.getFirstDate(dueStr);
                startTP.addToField(dueDate);
            } else {
                String currDate = DateParser.getCurrDateStr();
                startTP.addToField(currDate);
                if (DateParser.containsTime(dueStr)) {
                    dueTP.addToField(currDate);
                }
            }
        } else if (DateParser.containsTime(dueStr) &&
                   !DateParser.containsDate(dueStr)) {
            if (DateParser.containsDate(startStr)) {
                String startDate = DateParser.getFirstDate(startStr);
                dueTP.addToField(startDate);
            } else {
                String currDate = DateParser.getCurrDateStr();
                dueTP.addToField(currDate);
                if (DateParser.containsTime(startStr)) {
                    dueTP.addToField(currDate);
                }
            }
        }

        removeDuplicates(editFields);
        checkStartDueOrder(editFields);

        return new CommandEdit(editFields);
    }

    private static Command parseAdd(String[] commandParams) {
        String currFieldOrig = "";
        String currField = "";
        List<TaskParam> addFields = new ArrayList<TaskParam>();
        ArrayList<String> availParams = generateParamArrayList(PARAMS_DATE);

        String currWord;
        boolean currHasDate = false;
        boolean currHasTime = false;

        for (int j = 0; j < commandParams.length; j++) {
            currWord = commandParams[j];
            if (hasValidHashTag(currWord)) {
                // No matter the current field, collect hashtags first
                addFields.add(new TaskParam("tag", currWord));
            } else if (currField.equals(getDateParamEquiv(currWord)) &&
                       !currWord.isEmpty() && !currHasDate && !currHasTime) {
                // If parameters are repeated, add the previous one to name
                addToFieldParam(addFields, "name", currFieldOrig);
            } else if (availParams.contains(getDateParamEquiv(currWord))) {
                // If the current word is an available parameter name
                if (isParamOf(PARAMS_DATE_FULL, currField) && !currHasDate &&
                    !currHasTime) {
                    // If the last parameter was not filled, it was not a
                    // parameter
                    addToFieldParam(addFields, "name", currFieldOrig);
                    availParams.add(currField);
                }
                // Reassign currField values
                currField = getDateParamEquiv(currWord);
                // Save the original word (with capitalisation)
                currFieldOrig = currWord;
                // Remove availability
                availParams.remove(currField);
                // Reset hasDate/Time values
                currHasDate = false;
                currHasTime = false;
            } else if (isParamOf(PARAMS_DATE_FULL, currField) &&
                       !currWord.isEmpty()) {
                // If currently a date parameter and string is not ""
                if (!currHasDate && DateParser.isValidDate(currWord)) {
                    addToFieldParam(addFields, currField, currWord);
                    currHasDate = true;
                } else if (!currHasTime && DateParser.isValidTime(currWord)) {
                    addToFieldParam(addFields, currField, currWord);
                    currHasTime = true;
                } else {
                    // If not a valid date/time, reset fields
                    // Add the parameter name to "name" if no date/time
                    // was assigned.
                    TaskParam nameParam = getTaskParam(addFields, "name");
                    if (!currHasDate && !currHasTime) {
                        nameParam.addToField(currFieldOrig);
                        availParams.add(currField);
                    }
                    nameParam.addToField(currWord);
                    currField = "";
                    currFieldOrig = "";
                }
            } else {
                addToFieldParam(addFields, "name", currWord);
            }
        }

        // catches if the last word was a parameter
        if (isParamOf(PARAMS_DATE_FULL, currField) && !currHasDate &&
            !currHasTime) {
            addToFieldParam(addFields, "name", currFieldOrig);
        }

        // Check for input time with missing date
        TaskParam startTP = getTaskParam(addFields, "start");
        TaskParam dueTP = getTaskParam(addFields, "due");
        String startStr = startTP.getField();
        String dueStr = dueTP.getField();
        if (DateParser.containsTime(startStr) &&
            !DateParser.containsDate(startStr)) {
            if (DateParser.containsDate(dueStr)) {
                String dueDate = DateParser.getFirstDate(dueStr);
                startTP.addToField(dueDate);
            } else {
                String currDate = DateParser.getCurrDateStr();
                startTP.addToField(currDate);
                if (DateParser.containsTime(dueStr)) {
                    dueTP.addToField(currDate);
                }
            }
        } else if (DateParser.containsTime(dueStr) &&
                   !DateParser.containsDate(dueStr)) {
            if (DateParser.containsDate(startStr)) {
                String startDate = DateParser.getFirstDate(startStr);
                dueTP.addToField(startDate);
            } else {
                String currDate = DateParser.getCurrDateStr();
                dueTP.addToField(currDate);
                if (DateParser.containsTime(startStr)) {
                    dueTP.addToField(currDate);
                }
            }
        }

        removeDuplicates(addFields);
        checkStartDueOrder(addFields);

        return new CommandAdd(addFields);
    }

    private static boolean hasValidHashTag(String word) {
        return word.startsWith("#") && (word.length() > 1);
    }

    /**
     * Checks if input <code>word</code> is a member of input
     * <code>paramList</code>. This is used within the parser to check whether a
     * given word is a valid parameter.
     * 
     * @param paramList
     *            A list of valid parameters (to check against).
     * @param word
     *            The word to check.
     * @return <b>true</b> if <code>word</code> is a member of
     *         <code>paramList</code>.
     */
    private static boolean isParamOf(String[] paramList, String word) {
        return Arrays.asList(paramList).contains(word.toLowerCase());
    }

    /**
     * Creates a new, mutable ArrayList of Strings containing the members of the
     * input String Array.
     */
    private static ArrayList<String> generateParamArrayList(String[] paramList) {
        ArrayList<String> list = new ArrayList<String>();

        for (String p : paramList) {
            list.add(p);
        }

        return list;
    }

    /**
     * Converts alternative accepted user input for date parameters into
     * system-accepted ones for consistency in the parser.
     * <p>
     * Alternatives for "due": "by", "to", "end". <br>
     * Alternative for "start": "from".
     * */
    private static String getDateParamEquiv(String word) {
        String wordLC = word.toLowerCase();
        switch (wordLC) {
            case "by":
            case "to":
            case "end":
                return "due";

            case "from":
                return "start";
        }

        return wordLC;
    }

    /**
     * Searches <code>fields</code> and returns the TaskParam whose
     * <code>name</code> is equal to the input <code>currField</code>. If there
     * is no such TaskParam, this method creates it, adds it to
     * <code>fields</code> and returns it.
     */
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

    /**
     * @param fields
     */
    private static void checkStartDueOrder(List<TaskParam> fields) {
        TaskParam startTP = getTaskParam(fields, "start");
        TaskParam dueTP = getTaskParam(fields, "due");
        if (!startTP.getField().isEmpty() && !dueTP.getField().isEmpty()) {
            DateTime startDT = DateParser.parseToDateTime(startTP.getField());
            DateTime dueDT = DateParser.parseToDateTime(dueTP.getField());
            if (startDT.compareTo(dueDT) > 0) {
                fields.remove(startTP);
                fields.remove(dueTP);
                addTaskParamToFields(fields, "due", startTP.getField());
                addTaskParamToFields(fields, "start", dueTP.getField());
            }
        }
    }

    /**
     * Gets the TaskParam of the name <code>field</code> from the list
     * <code>fields</code>, and adds the input <code>content</code> to it.
     */
    private static void addToFieldParam(List<TaskParam> fields, String field,
                                        String content) {
        getTaskParam(fields, field).addToField(content);
    }

    /**
     * Adds a new TaskParam with the name <code>paramName</code> and content
     * <code>paramField</code> to the input <code>fields</code>.
     */
    private static boolean addTaskParamToFields(List<TaskParam> fields,
                                                String paramName,
                                                String paramField) {
        return fields.add(new TaskParam(paramName, paramField));
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

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // FOR TESTING PURPOSES (Exploratory)
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Input as a user would:");
        String input = "";
        while (!input.equals("exit")) {
            input = sc.nextLine();
            System.out.println(parse(input));
        }

        sc.close();
    }

}
