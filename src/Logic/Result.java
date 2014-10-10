package Logic;

import java.util.List;

import Storage.Task;

public class Result {
	private List<Task> _tasks;
	private boolean _success;
	private CommandType _cmdExecuted;
	
	public Result(List<Task> tasks, boolean success, CommandType cmdExecuted) {
		this._tasks = tasks;
		this._success = success;
		this._cmdExecuted = cmdExecuted;
	}
	
	public List<Task> getTasks() {
		return _tasks;
	}

	public boolean isSuccess() {
		return _success;
	}
	
	public CommandType getCmdExecuted() {
		return _cmdExecuted;
	}
	
	public void setCommandType(CommandType cmdType) {
		this._cmdExecuted = cmdType;
	}
}
