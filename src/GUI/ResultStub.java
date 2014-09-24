import java.util.ArrayList;

public class ResultStub extends Result {

    // tasks contains the tasks that need to be printed out for search
    // for tasks that return a single message, it contains the task that has
    // been added
    private ArrayList<Task> tasks;
    private boolean sucess = false;
    private CommandType cmdExecuted;

    public boolean getSucess() {
        return this.sucess;
    }

    public CommandType cmdExecuted() {
        return this.cmdExecuted;
    }

    public ArrayList<String> getTasks() {
        return this.tasks;
    }
}
