import java.util.ArrayList;

public class GraphicalUserInterface {
    public void getUserInput(String Input) {

        private static final String SUCESS_SINGLE_LINE = "Sucessfully %1$s \"%2$s\"";
        private static Result result = Processor.processInput(Input);
        if (!isCommandSucessful(result)) {
            // show error message
        }
        ArrayList<String> sucessMessages = processResult(result);
        printArrayList(sucessMessages);
    }

    private void showUserMessage(String message) {
        // print to screen
    }

    private void printArrayList(ArrayList<String> arrayList) {
        int length = arrayList.size();
        for (int index = 0; index < length; index++) {
            String message = arrayList.get(index);
            showUserMessage(message);
        }
    }

    private boolean isCommandSucessful(Result result) {
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
                return processAdd(tasksDone);
            case EDIT :
                return processEdit(tasksDone);
            case DELETE :

            case RESTORE :

            case SEARCH :

            case DISPLAY :

            case BLOCK :
            case UNBLOCK :
            case DONE :
            case UNDONE :
            case UNDO :
            case REDO :
            case CLEAR :
            case JOKE :
            case EXIT :
        }
    }

    // clears the screen of the GUI
    public void clearScreen() {
    }

}
