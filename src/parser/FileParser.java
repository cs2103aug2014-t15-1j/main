package parser;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import database.DateTime;
import database.Task;
import database.TaskType;

public class FileParser {

    /** Used to initialise string values to empty string */
    private static final String STR_INIT = "";
    // Accepted inputs for Task Type
    private static final String TYPE_BLOCK = "block";
    private static final String TYPE_DONE = "done";
    private static final String TYPE_TODO = "todo";

    // Required keywords for parsing from file
    private static final String KEYWORD_BREAKPOINT = "###";
    private static final String KEYWORD_START = "start:";
    private static final String KEYWORD_DUE = "due:";
    private static final String KEYWORD_COMPLETED = "completed:";
    private static final String KEYWORD_TYPE = "type:";

    /** Stores the list of accepted keywords, excluding KEYWORD_BREAKPOINT */
    private static final String[] LIST_KEYWORDS = { KEYWORD_BREAKPOINT,
                                                   KEYWORD_START, KEYWORD_DUE,
                                                   KEYWORD_COMPLETED,
                                                   KEYWORD_TYPE };

    // Indices for storing Task parameters in an String array
    private static final int INDEX_NAME = 0;
    private static final int INDEX_START = 1;
    private static final int INDEX_DUE = 2;
    private static final int INDEX_COMPLETED = 3;
    private static final int INDEX_TASKTYPE = 4;

    /** Stores the indices of Date-Time parameters for reference */
    private static final int[] LIST_DATE_INDICES = { INDEX_START, INDEX_DUE,
                                                    INDEX_COMPLETED };

    // Indices used to retrieve DateTime parameters after extraction from String
    // array
    private static final int INDEX_START_DT = 0;
    private static final int INDEX_DUE_DT = 1;
    private static final int INDEX_COMPLETED_DT = 2;

    /**
     * See {@link Parser#parseToTask(String)}.
     */
    static Task parse(String text) {
        String[] textItems = removeEmptyStrings(text.split(" "));

        // Params correspond to: name, start, due, completed, type
        String[] params = new String[] { STR_INIT, STR_INIT, STR_INIT,
                                        STR_INIT, STR_INIT };
        List<String> tags = new ArrayList<String>();

        collectParams(textItems, params, tags);
        DateTime[] dateTimes = convertToDt(params);

        // Convert param inputs for Task constructor
        String name = params[INDEX_NAME];
        DateTime start = dateTimes[INDEX_START_DT];
        DateTime due = dateTimes[INDEX_DUE_DT];
        DateTime completed = dateTimes[INDEX_COMPLETED_DT];
        TaskType type = getTaskType(params);

        Task newTask = new Task(name, start, due, completed, tags, type);
        return newTask;
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

    /**
     * Parses through textItems and sorts words into params and tags. Valid tags
     * are added to tags, and valid parameters are placed into their respective
     * positions in params.
     */
    private static void collectParams(String[] textItems, String[] params,
                                      List<String> tags) {
        int fieldIndex = 0;
        boolean breakPointMet = false;

        for (int i = 0; i < textItems.length; i++) {
            String currWord = textItems[i];
            if (isValidKeyword(currWord)) {
                if (!breakPointMet) {
                    breakPointMet = true;
                } else {
                    fieldIndex++;
                }
            } else if (breakPointMet && hasValidHashTag(currWord)) {
                tags.add(currWord);
            } else {
                params[fieldIndex] = params[fieldIndex].concat(" " + currWord)
                        .trim();
            }
        }

        assert (fieldIndex < 5) : "Too many parameters for TaskParser!";
        assert (fieldIndex == 4) : "TaskParser is missing parameter names.";
    }

    private static boolean isValidKeyword(String word) {
        return Arrays.asList(LIST_KEYWORDS).contains(word.toLowerCase());
    }

    private static boolean hasValidHashTag(String word) {
        return word.startsWith("#") && (word.length() > 1);
    }

    /**
     * This method converts the String fields in <code>params</code> that are
     * supposed to be DateTimes in Task. Uses LIST_DATES_INDEX for reference.
     * 
     * @return A DateTime array containing the converted parameters.
     */
    private static DateTime[] convertToDt(String[] params) {
        List<DateTime> converted = new ArrayList<DateTime>();

        for (int i = 0; i < params.length; i++) {
            if (isMemberOf(LIST_DATE_INDICES, i)) {
                String currDtStr = params[i];
                if (currDtStr.isEmpty()) {
                    converted.add(new DateTime());
                } else {
                    assert (DateParser.isValidDateTime(currDtStr));
                    converted.add(DateParser.parseToDateTime(currDtStr));
                }
            }
        }

        assert converted.size() == LIST_DATE_INDICES.length : "Wrong number of DateTimes converted!";
        DateTime[] dateTimes = converted.toArray(new DateTime[0]);
        return dateTimes;
    }

    /** Checks if <code>toCheck</code> is a member of <code>arr</code>. */
    private static boolean isMemberOf(int[] arr, int toCheck) {
        for (int index = 0; index < arr.length; index++) {
            if (toCheck == arr[index]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the TaskType of a Task using the parameters collected. The
     * TaskType is read using the index <code>INDEX_TASKTYPE</code>.
     */
    private static TaskType getTaskType(String[] params) {
        TaskType type = TaskType.TODO;
        switch (params[INDEX_TASKTYPE].toLowerCase()) {
            case TYPE_TODO:
                break;

            case TYPE_DONE:
                type = TaskType.DONE;
                break;

            case TYPE_BLOCK:
                type = TaskType.BLOCK;
                break;

            default:
                assert false : "Invalid TaskType while parsing from file!";
        }
        return type;
    }

    // FOR EXPLORATORY TESTING PURPOSES
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Type as per file input:");

        String input = STR_INIT;
        while (!input.equals("exit")) {
            input = sc.nextLine();
            try {
                System.out.println(parse(input));
            } catch (AssertionError e) {
                System.out.println(e.getMessage());
            }
        }

        sc.close();
    }

}
