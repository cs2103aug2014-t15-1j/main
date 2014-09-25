package Logic;

import java.util.ArrayList;
import Storage.Task;

public class Result {
	private ArrayList<Task> tasks;
	private boolean success;
	private CommandType cmdExecuted;
	
	public Result(ArrayList<Task> tasks, boolean success, CommandType cmdExecuted) {
		this.tasks = tasks;
		this.success = success;
		this.cmdExecuted = cmdExecuted;
	}
	
	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public boolean isSuccess() {
		return success;
	}
	
	public CommandType cmdExecuted() {
		return cmdExecuted;
	}
	
	public void setCommandType(CommandType cmdType) {
		this.cmdExecuted = cmdType;
	}
}
