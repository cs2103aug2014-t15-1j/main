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
import logic.CommandOthers;
import logic.CommandRestore;
import logic.CommandSearch;
import logic.CommandTodo;
import logic.CommandUnblock;
import database.BlockDate;
import database.DateTime;
import database.Task;

// TODO: Class description with reason why it's static
public class Parser {

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
    private static final String TYPE_UNBLOCK = "unblock";
    private static final String TYPE_DONE = "done";
    private static final String TYPE_TODO = "todo";
    private static final String TYPE_UNDO = "undo";
    private static final String TYPE_REDO = "redo";
    private static final String TYPE_EXIT = "exit";
    private static final String TYPE_RESET = "reset";

    // TODO: REFACTOR MAGIC STRINGS
    // TODO: CONSIDER USING PARAM_FIRST_WORD = 1
    private static final int ADD_DATE_PARAM_NUM = 3;
    private static final String[] STR_ARRAY_EMPTY = new String[0];

    private static final String[] PARAMS_EDIT = { "due", "start", "tags" };
    private static final String[] PARAMS_DATE = { "due", "start" };
    private static final String[] PARAMS_DATE_FULL = { "due", "by", "start",
                                                      "from", "end", "to" };
    private static final String[] PARAMS_DISPLAY = { "search", "all", "done",
                                                    "deleted", "block" };
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
    public static Command parse(String input) throws IllegalArgumentException {
        assert (input != null);

        // The Parser will analyse the input word by word
        String[] commandItems = input.trim().split(" ");

        if (commandItems.length > 0) {
            String commandType = getFirstWord(commandItems);
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

                case TYPE_UNBLOCK:
                    return parseUnblock(commandParams);

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
    private static String getFirstWord(String[] commandItems) {
        return commandItems[0].toLowerCase();
    }

    /**
     * Returns a clone of the input String array, excluding the command word.
     * The command word is assumed to be the first item of the array.
     * 
     * @param commandItems
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

    // ========== INDIVIDUAL PARSE-COMMAND FUNCTIONS ==========//

    private static Command parseTodo(String[] commandParams) {
        List<TaskParam> todoFields = new ArrayList<TaskParam>();

        try {
            String param = commandParams[0].toLowerCase();
            if (param.equals("last")) {
                todoFields.add(new TaskParam("rangeType", param));
            } else if (isInteger(param)) {
                todoFields.add(new TaskParam("rangeType", "id"));
                todoFields.add(new TaskParam("id", param));
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
                doneFields.add(new TaskParam("rangeType", "date"));
                doneFields.add(new TaskParam("date", firstWord));
            } else if (firstWord.equalsIgnoreCase("all")) {
                doneFields.add(new TaskParam("rangeType", "all"));
            } else if (isInteger(firstWord)) {
                doneFields.add(new TaskParam("rangeType", "id"));
                doneFields.add(new TaskParam("id", firstWord));
            } else {
                throw new IllegalArgumentException("Invalid argument for done!");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("No arguments for done");
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
                throw new IllegalArgumentException(
                        "Invalid argument for unblock");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("No arguments for unblock");
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
        // TODO: Check if there's something wrong with this that needs trim().
        DateTime dateTime1 = DateParser.parseToDateTime((firstDateStr + " " + firstTimeStr).trim());
        DateTime dateTime2 = DateParser.parseToDateTime((secondDateStr + " " + secondTimeStr).trim());
        TaskParam startTp;
        TaskParam endTp;
        if (dateTime1.isEarlierThan(dateTime2) || dateTime1.equals(dateTime2)) {
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
            displayFields.add(new TaskParam("rangeType", "todo"));
        } else {
            for (int i = 0; i < commandParams.length; i++) {
                if (!commandParams[i].isEmpty()) {
                    String firstWord = commandParams[i];
                    if (isParamOf(PARAMS_DISPLAY, firstWord)) {
                        displayFields.add(new TaskParam("rangeType", firstWord
                                .toLowerCase()));
                    } else if (isInteger(firstWord)) {
                        displayFields.add(new TaskParam("rangeType", "id"));
                        displayFields.add(new TaskParam("id", firstWord));
                    } else {
                        throw new IllegalArgumentException(
                                "Invalid argument for display");
                    }
                    break;
                }
            }
        }

        return new CommandDisplay(displayFields);
    }

    private static Command parseDelete(String[] commandParams) {
        List<TaskParam> deleteFields = new ArrayList<TaskParam>();

        try {
            String firstWord = commandParams[0];
            if (DateParser.isValidDate(firstWord)) {
                deleteFields.add(new TaskParam("rangeType", "dates"));
                deleteFields.add(new TaskParam("start", firstWord));
                deleteFields.add(new TaskParam("due", firstWord));
            } else if (isParamOf(PARAMS_DELETE, firstWord)) {
                deleteFields.add(new TaskParam("rangeType", firstWord
                        .toLowerCase()));
            } else if (isInteger(firstWord)) {
                deleteFields.add(new TaskParam("rangeType", "id"));
                deleteFields.add(new TaskParam("id", firstWord));
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

        // TODO: REFACTOR
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
                addTaskParamToField(searchFields, "status",
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

        // try saveEditIdToField() catch return CommandOthers
        try {
            id = commandParams[0];
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
                    addTaskParamToField(editFields, "delete",
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
                // TODO: rename method to getParamEquiv?
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
                fields.add(new TaskParam("due", startTP.getField()));
                fields.add(new TaskParam("start", dueTP.getField()));
            }
        }
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

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // ========== TASK PARSING METHODS ==========//
    /**
     * Forms a <code>Task</code> object by parsing a <code>String</code>
     * containing the stored string literals.
     * <p>
     * Note that the input <code>String</code> must be of the given format(below),
     * contain all four parameters names ("start:" to "status:"), and have
     * spaces between tags and parameter names. The position of the tags is
     * flexible as long as it comes after "start:".
     * 
     * @param text
     *            format:
     *            {@literal "<name> ### start: <date/time> due: <date/time> completed: 
     * <date/time> <tags> status: <status>"}
     */
    public static Task parseToTask(String text) {
        String[] textItems = text.trim().split(" ");
        // name, start, due, completed, status
        String[] param = new String[] { "", "", "", "", "" };
        List<String> tags = new ArrayList<String>();

        int fieldIndex = 0;
        boolean isDone = false;
        boolean breakPointMet = false;

        for (int i = 0; i < textItems.length; i++) {
            String currWord = textItems[i];
            String currWordLC = currWord.toLowerCase();
            if (!breakPointMet) {
                if (currWord.equals("###")) {
                    breakPointMet = true;
                } else {
                    if (param[fieldIndex].isEmpty() || currWord.isEmpty()) {
                        param[fieldIndex] = param[fieldIndex].concat(currWord);
                    } else {
                        param[fieldIndex] = param[fieldIndex].concat(" " +
                                                                     currWord);
                    }
                }
            } else {
                if (currWordLC.equals("start:") || currWordLC.equals("due:") ||
                    currWordLC.equals("completed:") ||
                    currWordLC.equals("status:")) {
                    fieldIndex++;
                    assert (fieldIndex < 5) : "Too many parameters for TaskParser!";
                } else if (hasValidHashTag(currWord)) {
                    tags.add(currWord);
                } else {
                    // NOTE: currWord.isEmpty() is to make sure the parser does
                    // not add " " for each empty string
                    if (param[fieldIndex].isEmpty() || currWord.isEmpty()) {
                        param[fieldIndex] = param[fieldIndex].concat(currWord);
                    } else {
                        param[fieldIndex] = param[fieldIndex].concat(" " +
                                                                     currWord);
                    }
                }
            }
        }

        assert (fieldIndex == 4) : "TaskParser is missing parameter names.";

        // Convert param inputs
        String name = param[0];
        if (param[4].equalsIgnoreCase("done")) {
            isDone = true;
        }

        // Convert date/time inputs into DateTime objects
        DateTime[] dateTimes = new DateTime[ADD_DATE_PARAM_NUM];

        // TODO: REORGANISE
        // Indexes are assigned by 1 (start), 2 (due) 3 (completed)
        for (int i = 1; i <= ADD_DATE_PARAM_NUM; i++) {
            if (param[i].isEmpty()) {
                dateTimes[i - 1] = new DateTime();
            } else {
                assert (DateParser.isValidDateTime(param[i]));
                dateTimes[i - 1] = DateParser.parseToDateTime(param[i]);
            }
        }

        DateTime start = dateTimes[0];
        DateTime due = dateTimes[1];
        DateTime completed = dateTimes[2];

        Task newTask = new Task(name, start, due, completed, tags);
        newTask.setDone(isDone);
        return newTask;
    }

    // ========== BLOCK PARSING METHODS ==========//

    /**
     * Forms a <code>BlockDate</code> object by parsing a <code>String</code>
     * containing the saved string literals of two <code>DateTime</code> objects
     * (starting and ending dates and times).
     * <p>
     * Note that the input <code>String</code> must be of the given format(below),
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
        String dueDate = fields[3];
        String dueTime = fields[4];
        assert (DateParser.isValidDate(startDate) && DateParser
                .isValidTime(startTime)) : "Invalid start DateTime saved";
        assert (DateParser.isValidDate(dueDate) && DateParser
                .isValidTime(dueTime)) : "invalid due DateTime saved";

        DateTime startDt = new DateTime(startDate, startTime);
        DateTime dueDt = new DateTime(dueDate, dueTime);

        return new BlockDate(startDt, dueDt);
    }

}
