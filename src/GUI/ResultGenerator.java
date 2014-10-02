package GUI;

import java.util.ArrayList;

public class ResultGenerator {

    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private static final int FIRST_ELEMENT = 0;
    private static final String FORMAT_DOT_AND_SPACE = ". ";
    private static final String SUCESSFUL_ADD = "Sucessfully added \"%1$s\"";
    private static final String SUCESSFUL_EDIT = "Sucessfully edited \"%1$s\"";
    private static final String SUCESSFUL_SEARCH = "Found %1$s result(s):";

    // to be changed at a later implementation
    private static final String SUCESSFUL_DELETE = "Sucessfully deleted!";
    private static final String SUCESSFUL_RESTORE = "Sucessfully restored!";
    private static final String SUCESSFUL_JOKE = "There are three kinds of people. Those who can count, and those who cannot."
            + LINE_SEPARATOR + "- Unknown";
    // private static final String SUCESSFUL_EXIT =
    // "The journey of a thousand miles begins with a single step."
    // + LINE_SEPARATOR + "- Lao Tzu" + LINE_SEPARATOR + "GoodBye! :)";

    // to be implement later SUCESSFUL: Block, Unblock, Help

    private static final String CODE_CLEAR = " clear";
    private static final String CODE_EXIT = " exit";
    private static final String UNSUCESSFUL_SEARCH_MESSAGE = "We could not find any results :( Try using different words?";
    private static final String ERROR_COMMAND_MESSAGE = "Opps! Looks like we could not process your command.";

    public static String sendInput(String userInput) {
        Processor processor = new Processor();
        Result result = processor.processInput(userInput);
        COMMAND_TYPE commandDone = result.getCmdExecuted();
        String message = getResultMessage(commandDone, result);
        return message;
    }

    public static String getResultMessage(COMMAND_TYPE commandDone,
            Result result) {
        ArrayList<Task> tasks = result.getTasks();
        switch (commandDone) {
            case ADD :
                return singleLineSuccessMessage(SUCESSFUL_ADD, tasks);
            case DELETE :
                return SUCESSFUL_DELETE;
            case EDIT :
                return singleLineSuccessMessage(SUCESSFUL_EDIT, tasks);
            case DISPLAY :
                return sucessfulDisplayMessage(tasks);
            case SEARCH :
                return successfulSearchMessage(tasks);
            case RESTORE :
                return SUCESSFUL_RESTORE;
            case JOKE :
                // to be changed
                return SUCESSFUL_JOKE;
            case CLEAR :
                // lets Main Screen know that screen is to be cleared
                return CODE_CLEAR;
            case EXIT :
                // to be changed
                return CODE_EXIT;
            default :
                return ERROR_COMMAND_MESSAGE;
        }
    }

    // Returns message of format "Sucessfully (task done) (task name)"
    // Pre-cond: tasks only has one element
    public static String singleLineSuccessMessage(String message,
            ArrayList<Task> tasks) {
        Task task = tasks.get(FIRST_ELEMENT);
        String taskName = task.getName();
        return String.format(message, taskName);
    }

    public static String successfulSearchMessage(ArrayList<Task> tasks) {
        int numOfSearchResults = tasks.size();
        if (numOfSearchResults == 0) {
            return UNSUCESSFUL_SEARCH_MESSAGE;
        }
        String successMessage = String.format(SUCESSFUL_SEARCH,
                numOfSearchResults) + LINE_SEPARATOR;
        String stringOfSearchResults = sucessfulDisplayMessage(tasks);
        successMessage = successMessage + stringOfSearchResults;
        return successMessage;
    }

    public static String sucessfulDisplayMessage(ArrayList<Task> tasks) {
        int itemsToDisplay = tasks.size();
        ArrayList<String> displayList = changeTaskListToString(tasks,
                itemsToDisplay);
        String successMessage = changeStringListToString(displayList);
        return successMessage;
    }

    // Format of each element in the arrayList is "(task id). (task name)"
    private static ArrayList<String> changeTaskListToString(
            ArrayList<Task> tasks, int numOfSearchResults) {
        ArrayList<String> tasksInString = new ArrayList<String>();
        for (int index = 0; index < numOfSearchResults; index++) {
            Task currentTask = tasks.get(index);
            String taskName = currentTask.getName();
            int taskId = currentTask.getId();
            tasksInString.add(taskId + FORMAT_DOT_AND_SPACE + taskName);
        }
        return tasksInString;
    }

    private static String changeStringListToString(ArrayList<String> list) {
        int length = list.size();
        String string = "";
        for (int index = 0; index < length - 1; index++) {
            String currentElement = list.get(index);
            string = string + currentElement + LINE_SEPARATOR;
        }

        // this prevents the last line from having an next line character
        // appended to it
        int lastIndex = length - 1;
        String lastElement = list.get(lastIndex);
        string = string + lastElement;
        return string;
    }
}
