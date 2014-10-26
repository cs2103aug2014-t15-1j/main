package logic;

import java.util.List;

import database.BlockDate;
import database.Task;

/**
 * A Result object contains the necessary information of an operation.
 * It is being returned from Processor via processInput(String) 
 *
 * @author Ter Yao Xiang
 */
public class Result {
    
	//Contains Task/BlockDate Objects that are affected in an operation
    @SuppressWarnings("rawtypes")
    private List outputs;
    
	//True if the operation performed is successful, else False.
    private boolean success;
	
    //The type of command being executed in the operation
    //cmdType == CommandType.ERROR if unable to parse command
    //TODO: Include error encountered in Result?
    private CommandType cmdType;
	
    private boolean confirmation;
    private ResultType resultType;
    
    /** There are currently 2 ResultType that can be retrieved from Result
     *  The output can be accessed via {@link getTasks()} or {@link getBlockedDates}
     *  
     *  @author Yao Xiang
     */
    public enum ResultType {
        TASK,
        BLOCKDATE;
    }
    
    /** Dummy Constructor for Result */
    public Result() {
        this(null, false, CommandType.ERROR, false, null);
    }
    
    /** Main Constructor for Result Object */
    @SuppressWarnings("rawtypes")
    public Result(List outputs, boolean success, CommandType cmdType, ResultType resultType){
        this(outputs, success, cmdType, false, resultType);
    }
    
    /** Overloaded Constructor for Result Object
     * 
     * @param outputs
     *         - Contains relevant outputs for the user
     * @param success
     *         -  {@code true} if operation is successful, else {@code false}.
     * @param cmdType
     *         - CommandType of the operation performed.
     * @param confirmation
     *         - {@code true} if requires confirmation from user, {@code false} if no further action is require from the user.
     * @param resultType
     *         - equals  {@code ResultType.TASK} or {@code ResultType.BLOCKDATE}.
     */
	@SuppressWarnings("rawtypes")
    public Result(List outputs, boolean success, CommandType cmdType, boolean confirmation, ResultType resultType) {
		this.outputs = outputs;
		this.success = success;
		this.cmdType = cmdType;
		this.confirmation = confirmation;
		this.resultType = resultType;
	}
	
	/** Accessors */
	@SuppressWarnings("unchecked")
    public List<Task> getTasks() {
		return outputs;
	}
	
	@SuppressWarnings("unchecked")
    public List<BlockDate> getBlockedDates() {
	    return outputs;
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
	
	public ResultType getResultType() {
        return resultType;
    }
	
	// Mutators
	public void setSuccess(boolean success) {
	    this.success = success;
	}
	
	public void setCommandType(CommandType cmdType) {
		this.cmdType = cmdType;
	}
	
	@Override
	public String toString() {
	    return outputs + "[" + success + "][" + cmdType + "][" + confirmation + "][" +  resultType + "]";
	}
}
