package gui;

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
    private ResultType resultType;

    @SuppressWarnings("rawtypes")
    public ResultStub(List outputs, boolean success, CommandType cmdType,
            boolean confirmation, ResultType resultType) {
        super(outputs, success, cmdType, confirmation, resultType);
        setTasks(outputs);
        setSuccess(success);
        setCmdType(cmdType);
        setConfirmation(confirmation);
        setResultType(resultType);
    }

    /*** MUTATORS ***/

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List getTasks() {
        return outputs;
    }

    public CommandType getCmdType() {
        return cmdType;
    }

    public ResultType getResultType() {
        return this.resultType;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    /*** ACCESSORS ***/

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

    public void setCmdType(CommandType cmdType) {
        this.cmdType = cmdType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

}
