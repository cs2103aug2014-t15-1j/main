package Logic;

import java.util.List;

import Storage.Task;

public class Result {
	private List<Task> tasks;
	private boolean success;
	private CommandType cmdExecuted;
	
	public Result(List<Task> tasks, boolean success, CommandType cmdExecuted) {
		this.tasks = tasks;
		this.success = success;
		this.cmdExecuted = cmdExecuted;
	}
	
	public List<Task> getTasks() {
		return tasks;
	}

	public boolean isSuccess() {
		return success;
	}
	
	public CommandType getCmdExecuted() {
		return cmdExecuted;
	}
	
	public void setCommandType(CommandType cmdType) {
		this.cmdExecuted = cmdType;
	}
}
