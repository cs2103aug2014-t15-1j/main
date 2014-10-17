package Logic;

import java.util.List;

import Storage.Task;

/**
 * A Result object contains the necessary information of an operation.
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
    //TODO: Include error encountered in Result?
    private CommandType cmdType;
	
    private boolean confirmation;
    
    /** Constructor for Result Object */
    public Result(List<Task> tasks, boolean success, CommandType cmdType){
        this(tasks, success, cmdType, false);
    }
    
	public Result(List<Task> tasks, boolean success, CommandType cmdType, boolean confirmation) {
		this.tasks = tasks;
		this.success = success;
		this.cmdType = cmdType;
		this.confirmation = confirmation;
	}
	
	/** Accessors */
	public List<Task> getTasks() {
		return tasks;
	}

	public boolean isSuccess() {
		return success;
	}
	
	public CommandType getCommandType() {
		return cmdType;
	}
	
	public boolean needsConfirmation() {
	    return confirmation;
	}
	
	// Mutators
	public void setSuccess(boolean success) {
	    this.success = success;
	}
	
	public void setCommandType(CommandType cmdType) {
		this.cmdType = cmdType;
	}
}
