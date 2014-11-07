package logic;

import java.util.List;

import database.Task;

/**
 * A Result object contains the necessary information of an operation. It is
 * being returned from Processor via processInput(String)
 *
 * @author Ter Yao Xiang
 */
public class Result {

    // Contains Task/BlockDate Objects that are affected in an operation
    private List<Task> outputs;

    // True if the operation performed is successful, else False.
    private boolean success;

    // The type of command being executed in the operation
    // cmdType == CommandType.ERROR if unable to parse command
    // TODO: Include error encountered in Result?
    private CommandType cmdType;

    private boolean confirmation;
    
    private String displayTab;

    /** Dummy Constructor for Result */
    public Result() {
        this(null, false, null, false, null);
    }

    /**
     * Main Constructor for Result Object
     * <p>
     * Calls {@link #Result(List, boolean, CommandType, boolean, ResultType)
     * Overloaded Constructor}
     */
    public Result(List<Task> outputs, boolean success, CommandType cmdType, String displayTab) {
        this(outputs, success, cmdType, false, displayTab);
    }

    /**
     * Overloaded Constructor for Result Object
     * 
     * @param outputs
     *            - Contains relevant outputs for the user
     * @param success
     *            - {@code true} if operation is successful, else {@code false}.
     * @param cmdType
     *            - CommandType of the operation performed.
     * @param confirmation
     *            - {@code true} if requires confirmation from user,
     *            {@code false} if no further action is require from the user.
     * @param displayTab
     *            - The tab that the result is suppose to be displayed on
     */
    public Result(List<Task> outputs, boolean success, CommandType cmdType,
            boolean confirmation, String displayTab) {
        this.outputs = outputs;
        this.success = success;
        this.cmdType = cmdType;
        this.confirmation = confirmation;
        this.displayTab  = displayTab;
    }

    /** Accessors */
    public List<Task> getTasks() {
        return outputs;
    }

    public List<Task> getBlockedDates() {
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

    public String getDisplayTab() {
        return displayTab;
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
        return outputs + "[" + success + "][" + cmdType + "][" + confirmation +
               "][" + displayTab + "]";
    }
}
