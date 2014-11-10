//@author A0110751W
package logic;

import java.util.List;

import objects.Task;

/**
 * A Result object contains the necessary information of an operation. These
 * information that is being held inside the Result object consists of <i>the
 * Task(s) affected</i>, the <i>successfulness of the operation performed</i>,
 * the <i>type of operation</i> performed, the <i>display tab that the UI should
 * switch to</i> after the operation and whether the <i>operation requires a
 * confirmation check</i> from the user. <br>
 * It is being returned from Processor via processInput(String).
 *
 */
public class Result {

    // Contains Task Objects that are affected in an operation
    private List<Task> outputs;

    // True if the operation performed is successful, else False.
    private boolean success;

    private CommandType cmdType;

    private boolean confirmation;

    private String displayTab;

    /**
     * Default Constructor for Result.
     * <p>
     * Used as an invalid Result
     * 
     */
    public Result() {
        this(null, false, null, false, null);
    }

    /**
     * Constructor for Result Object
     * <p>
     * Calls {@link #Result(List, boolean, CommandType, boolean, displayTab)
     * Result(List, boolean, CommandType, boolean, false, displayTab)}<br>
     * <code> </code>
     * <p>
     * 
     * @param outputs
     *            - Contains relevant outputs for the user
     * @param success
     *            - {@code true} if operation is successful, else {@code false}.
     * @param cmdType
     *            - CommandType of the operation performed.
     * @param displayTab
     *            - The tab that the result is suppose to be displayed on.
     *
     */
    public Result(List<Task> outputs, boolean success, CommandType cmdType,
            String displayTab) {
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
        this.displayTab = displayTab;
    }

    /** Accessors */
    public List<Task> getTasks() {
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

    /** Mutators */
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
