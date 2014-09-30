import java.util.ArrayList;

public class ResultGenerator {

    private static final String LINE_SEPARATOR = System
            .getProperty("line separator");
    private static final int FIRST_ELEMENT = 0;
    private static final String SUCESSFUL_ADD = "Sucessfully added \"%1$s\"";
    private static final String SUCESSFUL_EDIT = "Sucessfully edited \"%1$s\"";
    private static final String SUCESSFUL_SEARCH = "Found %1$s(s) results:";
    // TO DO stub: clear function

    // to be changed at a later implementation
    private static final String SUCESSFUL_DELETE = "Sucessfully deleted \"%1$s\"";
    private static final String SUCUSSFUL_EXIT = "The journey of a thousand miles begins with a single step."
            + LINE_SEPARATOR + "- Lao Tzu" + LINE_SEPARATOR + "GoodBye! :)";

    // to be implement later SUCESSFUL: Block, Unblock, Joke, Help

    public static String sendInput(String userInput) {
        Processor processor = new Processor();
        Result result = processor.processInput(userInput);
        CommandType commandDone = result.getCommandType();
        String message = getResultMessage(commandDone, result);
        return message;
    }

    public static String getResultMessage(CommandType commandDone, Result result){
        ArrayList<Task> tasks = result.getTasks();
        switch(commandDone){
            case: commandDone.ADD
            return singleLineSucessMsg(SUCESSFUL_ADD, tasks);
            case: commandDone.DELETE
            return singleLineSucessMsg(SUCESSFUL_DELETE, tasks);
        }
    }

    public static String singleLineSucessMessage(String message,
            ArrayList<Task> tasks) {
        Task task = tasks.get(FIRST_ELEMENT);
        String taskName = task.getName();
        return String.format(message, taskName);
    }
}
