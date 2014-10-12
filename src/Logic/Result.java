package Logic;

import java.util.List;

import Storage.Task;

/* A Result object contains the necessary information of an operation.
 * It is being returned from Processor via processInput(String) 
 *
 * @author Ter Yao Xiang
 */
public class Result {
    
	//Contains Tasks that are affected in an operation
    private List<Task> tasks;
    
	//True if the operation performed is successful, else False.
    private boolean success;
	
    //The type of command being executed in the operation
    //cmdType == CommandType.ERROR if unable to parse command
    private CommandType cmdType;
	
	public Result(List<Task> tasks, boolean success, CommandType cmdType) {
		this.tasks = tasks;
		this.success = success;
		this.cmdType = cmdType;
	}
	
	public List<Task> getTasks() {
		return tasks;
	}

	public boolean isSuccess() {
		return success;
	}
	
	public CommandType getCommandType() {
		return cmdType;
	}
	
	public void setCommandType(CommandType cmdType) {
		this.cmdType = cmdType;
	}
}
