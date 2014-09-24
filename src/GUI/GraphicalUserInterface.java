import java.util.ArrayList;
import java.util.Scanner;

import javax.activation.MailcapCommandMap;

public class GraphicalUserInterface {

    private static final String COMMAND_DONE_ADD = "added";
    private static final String COMMAND_DONE_DELETE = "deleted";
    private static final String COMMAND_DONE_EDITED = "edited";
    private static final String COMMAND_DONE_DONE = "marked as done";
    private static final String COMMAND_DONE_UNDONE = "marked as undone";

    private static final String COMMAND_DONE_UNDO = "undo";
    private static final String COMMAND_DONE_REDO = "redo";

    private static final String COMMAND_DONE_SEARCH = "We found %1$s(s) results: %n";

    private static final String COMMAND_DONE_EXIT = "The journey of a thousand miles begins with a single step.%n "
            + "- Lao Tzu%n GoodBye! :)";

    private static final String SUCESS_SINGLE_LINE = "Sucessfully %1$s \"%2$s\"";
    private static final String MESSAGE_WELCOME = "Welcome to Haystack! Enter “help” for more information.";
    private static final String COMMAND = "command: ";

    public static void main(String args) {
        displayWelcomeMessage();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showUserMessage(COMMAND);

        }
    }

    private static void displayWelcomeMessage() {
        showUserMessage(MESSAGE_WELCOME);
    }

    public void getUserInput(String Input) {
        Result result = Processor.processInput(Input);
        if (!isCommandSucessful(result)) {
            // show error message
        }
        ArrayList<String> sucessMessages = processResult(result);
        printArrayList(sucessMessages);
    }

    private static void showUserMessage(String message) {
        // print to screen
    }

    private static void printArrayList(ArrayList<String> arrayList) {
        int length = arrayList.size();
        for (int index = 0; index < length; index++) {
            String message = arrayList.get(index);
            showUserMessage(message);
        }
    }

    private static boolean isCommandSucessful(Result result) {
        if (result.getSucess()) {
            return true;
        }
        return false;
    }

    // processes Result and returns the command that was carried out
    public ArrayList<String> processResult(Result result) {
        CommandType command = result.getCommandType();
        ArrayList<Task> tasksDone = result.getTasks();
        ArrayList<String> sucessMessages = new ArrayList<String>();
        switch (command) {
            case HELP :
                
            case ADD :
                return getSucessMessage(tasksDone, COMMAND_DONE_ADD);
            case EDIT :
                return getSucessMessage(tasksDone, COMMAND_DONE_EDIT);
            case DELETE :
                return getSucessMessage(tasksDone, COMMAND_DONE_DELETE);
            case RESTORE :
                return getSucessMessage(tasksDone, COMMAND_DONE_RESTORE);
            case SEARCH :
                return getSucessMessageSearch(tasksDone);
            case DISPLAY :
                return getSucessMessageDisplay(tasksDone);
            //case BLOCK : to do            
            //case UNBLOCK : to do

            case DONE :
                return getSucessMessage(tasksDone, COMMAND_DONE_DONE);
            case UNDONE :
                return getSucessMessage(tasksDone, COMMAND_DONE_UNDONE);
            case UNDO :
                return getSucess
            case REDO :

            case CLEAR :
                clearScreen();
            //case JOKE : to do

            case EXIT :
                exitProgram();

        }
    }

    private ArrayList<String> getSucessMessage(ArrayList<Task> taskDone,
            String command) {
        Task tasksEdited = tasksDone.get(0);
        String name = taskEdited.getName();
        ArrayList<String> sucessMessages = new ArrayList<String>();
        sucessMessages.add(name);
        return sucessMessages;
    }

    private void exitProgram() {
        // TODO Auto-generated method stub
        showUserMessage(COMMAND_DONE_EXIT);
        System.exit(0);
    }

    // clears the screen of the GUI
    public void clearScreen() {
    }

}
