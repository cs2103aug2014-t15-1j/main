package objects;

import java.util.List;

import logic.CommandType;
import logic.Result;

/*
 * This class is to be used for testing ONLY.
 * ResultStub allows for dependency injection of Result objects.
 * 
 */

public class ResultStub extends Result {

    @SuppressWarnings("rawtypes")
    private List outputs = null;
    private boolean success = false;
    private CommandType cmdType;
    private boolean confirmation = false;
    private String displayTab = "";

    
    @SuppressWarnings("rawtypes")
    public ResultStub(List outputs, boolean success, CommandType cmdType, String displayTab) {
        super(outputs, success, cmdType, displayTab);
        setTasks(outputs);
        setSuccess(success);
        setCmdType(cmdType);
        setDisplayTab(displayTab);;
    }
    
    @SuppressWarnings("rawtypes")
    public ResultStub(List outputs, boolean success, CommandType cmdType,
            boolean confirmation, String displayTab){
        super(outputs, success, cmdType, confirmation, displayTab);
        setTasks(outputs);
        setSuccess(success);
        setCmdType(cmdType);
        setDisplayTab(displayTab);
        setConfirmation(confirmation);
    }

    /*** ACCESSORS ***/

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List getTasks() {
        return outputs;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List getBlockedDates() {
        return outputs;
    }

    public CommandType getCmdType() {
        return cmdType;
    }

    public boolean isSuccess() {
        return this.success;
    }
    
    public String getDisplayTab(){
        return displayTab;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    /*** MUTATORS ***/

    @SuppressWarnings("rawtypes")
    public void setTasks(List outputs) {
        this.outputs = outputs;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }
    
    public void setDisplayTab(String displayTab){
        this.displayTab = displayTab;
    }

    public void setCmdType(CommandType cmdType) {
        this.cmdType = cmdType;
    }

}
